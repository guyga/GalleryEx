package com.example.galleryex.ui.gallery;

import android.net.Uri;

import com.example.galleryex.gallery.GalleryProvider;
import com.example.galleryex.repository.ImageUploadManager;
import com.example.galleryex.repository.UploadTask;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class GalleryViewModel extends ViewModel
{
	private final GalleryProvider    _galleryProvider;
	private final ImageUploadManager _imageUploadManager;

	private MutableLiveData<Boolean>              _fetchImagesLiveData;
	private LiveData<List<ImageWrapper>>          _imagesLiveData;
	private MutableLiveData<GalleryLoadingStatus> _galleryLoadingStatus;
	private LiveData<UploadTask>                  _uploadResponse;
	private MutableLiveData<Boolean>              _navigateToLinks;

	public GalleryViewModel(GalleryProvider galleryProvider, ImageUploadManager imageUploadManager)
	{
		_galleryProvider = galleryProvider;
		_imageUploadManager = imageUploadManager;

		initGallery();
		initUploads();

		fetchImages();

		_navigateToLinks = new MutableLiveData<>();
	}

	/**
	 * Initiates all the LiveData objects necessary for the gallery - Loading state and gallery
	 * fetching result
	 */
	private void initGallery()
	{
		_galleryLoadingStatus = new MutableLiveData<>();
		_fetchImagesLiveData = new MutableLiveData<>();

		_imagesLiveData = Transformations.switchMap(_fetchImagesLiveData, v ->
				Transformations.map(_galleryProvider.getAllImages(),
						imagesUri ->
						{
							List<ImageWrapper> images = new ArrayList<>();
							if (imagesUri == null)
							{
								_galleryLoadingStatus.postValue(GalleryLoadingStatus.ERROR);
							}
							else if (imagesUri.isEmpty())
							{
								_galleryLoadingStatus.postValue(GalleryLoadingStatus.EMPTY);
							}
							else
							{
								for (Uri uri : imagesUri)
								{
									images.add(new ImageWrapper(uri));
								}
								_galleryLoadingStatus.postValue(GalleryLoadingStatus.DONE);
							}
							return images;
						}
				));
	}

	/**
	 * Initiates the upload process LiveData.
	 * Upon completing, updated the current task state.
	 */
	private void initUploads()
	{
		_uploadResponse = Transformations.map(_imageUploadManager.getUploadResponse(), uploadTask ->
		{
			uploadTask.getImageWrapper().setBlurState(ImageWrapper.BlurState.READY);
			return uploadTask;
		});
	}

	/**
	 * Starts the gallery fetching process
	 */
	private void fetchImages()
	{
		_galleryLoadingStatus.setValue(GalleryLoadingStatus.LOADING);
		_fetchImagesLiveData.setValue(true);
	}

	/**
	 * Starts the blur & upload process
	 *
	 * @param imageWrapper image container to be process
	 */
	public void startBlurProcess(ImageWrapper imageWrapper)
	{
		_imageUploadManager.submit(imageWrapper);
	}

	public LiveData<List<ImageWrapper>> getImagesLiveData()
	{
		return _imagesLiveData;
	}

	public LiveData<GalleryLoadingStatus> getGalleryLoadingStatus()
	{
		return _galleryLoadingStatus;
	}

	public LiveData<UploadTask> getUploadResponse()
	{
		return _uploadResponse;
	}

	public void navigateToLinks()
	{
		_navigateToLinks.postValue(true);
	}

	public void doneNavigateToLinks()
	{
		_navigateToLinks.postValue(false);
	}

	public LiveData<Boolean> getNavigateToLinks()
	{
		return _navigateToLinks;
	}
}
