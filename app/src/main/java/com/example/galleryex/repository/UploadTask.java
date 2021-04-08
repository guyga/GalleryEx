package com.example.galleryex.repository;

import com.example.galleryex.networking.UploadResponse;
import com.example.galleryex.ui.gallery.ImageWrapper;

public class UploadTask
{
	private ImageWrapper           _imageWrapper;
	private Result<UploadResponse> _response;

	public UploadTask(ImageWrapper imageWrapper)
	{
		_imageWrapper = imageWrapper;
	}

	public void setResponse(Result<UploadResponse> response)
	{
		_response = response;
	}

	public ImageWrapper getImageWrapper()
	{
		return _imageWrapper;
	}

	public Result<UploadResponse> getResponse()
	{
		return _response;
	}
}
