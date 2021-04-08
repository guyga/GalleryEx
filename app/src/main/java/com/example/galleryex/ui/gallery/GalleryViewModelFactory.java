package com.example.galleryex.ui.gallery;

import com.example.galleryex.gallery.GalleryProvider;
import com.example.galleryex.repository.ImageUploadManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class GalleryViewModelFactory implements ViewModelProvider.Factory
{
	private GalleryProvider    _galleryProvider;
	private ImageUploadManager _imageUploadManager;

	public GalleryViewModelFactory(GalleryProvider galleryProvider, ImageUploadManager imageUploadManager)
	{
		_galleryProvider = galleryProvider;
		_imageUploadManager = imageUploadManager;
	}

	@NonNull
	@Override
	public <T extends ViewModel> T create(@NonNull Class<T> modelClass)
	{
		if (modelClass.isAssignableFrom(GalleryViewModel.class))
		{
			return (T) new GalleryViewModel(_galleryProvider, _imageUploadManager);
		}
		throw new IllegalArgumentException("Incorrect ViewModel class");
	}
}
