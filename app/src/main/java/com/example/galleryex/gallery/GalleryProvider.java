package com.example.galleryex.gallery;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import androidx.lifecycle.MutableLiveData;

/**
 * Retrieves all the gallery images
 */
public class GalleryProvider
{
	private final Context         _context;
	private       ExecutorService _executorService;

	public GalleryProvider(Context context, ExecutorService executorService)
	{
		_context = context;
		_executorService = executorService;
	}

	/**
	 * Creates a query to retrieve all the images
	 *
	 * @return list of images uris
	 */
	public MutableLiveData<List<Uri>> getAllImages()
	{
		MutableLiveData<List<Uri>> imagesLiveData = new MutableLiveData<>();

		_executorService.submit(() ->
		{
			List<Uri> images = new ArrayList<>();

			String[] projection = new String[]{MediaStore.Video.Media._ID};
			try
			{
				Cursor cursor = _context.getContentResolver().query(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						projection,
						null,
						null,
						null
				);
				int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
				while (cursor.moveToNext())
				{
					long imageId = cursor.getLong(idColumn);
					Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageId);
					images.add(imageUri);
				}
				cursor.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			imagesLiveData.postValue(images);
		});
		return imagesLiveData;
	}
}
