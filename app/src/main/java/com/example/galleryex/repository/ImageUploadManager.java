package com.example.galleryex.repository;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.galleryex.R;
import com.example.galleryex.gallery.BlurApplier;
import com.example.galleryex.networking.UploadResponse;
import com.example.galleryex.persistency.LinksStorage;
import com.example.galleryex.ui.gallery.ImageWrapper;

import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

/**
 * Responsible for managing a upload tasks queue,
 * where each task includes the following stages:
 * 		1. Blur the image
 * 		2. Copy the blurred image to a temporary file
 * 		3. Upload the temporary file
 */
public class ImageUploadManager
{
	private final Context         _context;
	private final BlurApplier     _blurApplier;
	private final ImageRepository _imageRepository;
	private final LinksStorage    _linksStorage;
	private final ExecutorService _executorService;

	private MutableLiveData<UploadTask> _processUploadTask;
	private LiveData<UploadTask>        _response;

	private LinkedList<UploadTask> _uploadQueue;
	private UploadTask             _currentUploadTask;

	public ImageUploadManager(Context context, BlurApplier blurApplier, ImageRepository imageRepository, LinksStorage linksStorage, ExecutorService executorService)
	{
		_context = context;
		_blurApplier = blurApplier;
		_imageRepository = imageRepository;
		_linksStorage = linksStorage;
		_executorService = executorService;
		_processUploadTask = new MutableLiveData<>();
		_uploadQueue = new LinkedList<>();

		_response = Transformations.switchMap(_processUploadTask, uploadTask -> makeBlurImageLiveData(uploadTask));
	}

	/**
	 * 1st stage of the task - apply blur
	 *
	 * @param uploadTask - current upload task
	 * @return current task after the blur has been completed
	 */
	private LiveData<UploadTask> makeBlurImageLiveData(UploadTask uploadTask)
	{
		return Transformations.switchMap(_blurApplier.applyBlur(uploadTask.getImageWrapper()),
				imageWrapper ->
				{
					if (imageWrapper.getBlurredBitmap() == null)
					{
						MutableLiveData<UploadTask> blurErrorLiveData = new MutableLiveData<>();
						uploadTask.setResponse(new Result.Error<>(new Exception(_context.getString(R.string.error_blur_failure))));
						blurErrorLiveData.postValue(uploadTask);
						processNext();
						return blurErrorLiveData;
					}
					return makeCopyFileLiveData(uploadTask);
				});
	}

	/**
	 * 2nd stage of the task - copy the blurry bitmap to a temporary file
	 *
	 * @param uploadTask - current upload task
	 * @return current task after a temporary file for the blurry image has been created
	 */
	private LiveData<UploadTask> makeCopyFileLiveData(UploadTask uploadTask)
	{
		return Transformations.switchMap(copyBitmapToFile(uploadTask.getImageWrapper().getBlurredBitmap()), fileToUpload ->
		{
			if (fileToUpload == null)
			{
				MutableLiveData<UploadTask> errorLiveData = new MutableLiveData<>();
				uploadTask.setResponse(new Result.Error<UploadResponse>(new Exception(_context.getString(R.string.error_new_file_error))));
				errorLiveData.postValue(uploadTask);
				processNext();
				return errorLiveData;
			}

			return makeUploadLiveData(uploadTask, fileToUpload);
		});
	}

	/**
	 * 3rd stage of the task - Upload the file
	 *
	 * @param uploadTask   - current upload task
	 * @param fileToUpload - file to be uploaded
	 * @return current task after upload request has been completed
	 */
	private LiveData<UploadTask> makeUploadLiveData(UploadTask uploadTask, File fileToUpload)
	{
		return Transformations.map(_imageRepository.upload(fileToUpload), response ->
		{
			uploadTask.setResponse(response);

			if (response instanceof Result.Success)
			{
				String link = ((UploadResponse) ((Result.Success) response)._data).getUpload().getLink();
				_linksStorage.insert(link);
			}
			processNext();

			return uploadTask;
		});
	}

	/**
	 * submit new upload task.
	 *
	 * @param imageWrapper - Image container, which the image referred to by
	 *                     {@link ImageWrapper#getUri()} will be blurred first and then uploaded
	 */
	public void submit(ImageWrapper imageWrapper)
	{
		_uploadQueue.add(new UploadTask(imageWrapper));
		processTaskIfAvailable();
	}

	/**
	 * Advances to process the next upload task
	 */
	private void processNext()
	{
		_currentUploadTask = null;
		if (!_uploadQueue.isEmpty())
		{
			_currentUploadTask = _uploadQueue.removeFirst();
			_processUploadTask.postValue(_currentUploadTask);
		}
	}

	/**
	 * Advances to process the next upload task, if no current task is running.
	 * Should be used only through {@link #submit(ImageWrapper)}.
	 */
	private void processTaskIfAvailable()
	{
		if (_currentUploadTask == null && !_uploadQueue.isEmpty())
		{
			_currentUploadTask = _uploadQueue.removeFirst();
			_processUploadTask.postValue(_currentUploadTask);
		}
	}

	public LiveData<UploadTask> getUploadResponse()
	{
		return _response;
	}

	/**
	 * Creates a temporary file from the given bitmap
	 * and returns the file when finished
	 *
	 * @param bitmap - source bitmap
	 * @return destination file
	 */
	private LiveData<File> copyBitmapToFile(Bitmap bitmap)
	{
		MutableLiveData<File> liveData = new MutableLiveData<>();
		_executorService.submit(() ->
		{
			try
			{
				File outputFile = File.createTempFile("tempBitmap", null);

				FileOutputStream outputStream = new FileOutputStream(outputFile);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
				liveData.postValue(outputFile);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				liveData.postValue(null);
			}
		});
		return liveData;
	}
}
