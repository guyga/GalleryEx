package com.example.galleryex.ui.gallery;

import android.graphics.Bitmap;
import android.net.Uri;

public class ImageWrapper
{
	public enum BlurState
	{READY, IN_PROGRESS}

	private Uri       _uri;
	private BlurState _blurState;
	private Bitmap    _blurredBitmap;
	private int       _position;

	public ImageWrapper(Uri uri)
	{
		_uri = uri;
		_blurState = BlurState.READY;
	}

	public Uri getUri()
	{
		return _uri;
	}

	public BlurState getBlurState()
	{
		return _blurState;
	}

	public void setBlurState(BlurState blurState)
	{
		_blurState = blurState;
	}

	public void setBlurredBitmap(Bitmap blurredBitmap)
	{
		_blurredBitmap = blurredBitmap;
	}

	public Bitmap getBlurredBitmap()
	{
		return _blurredBitmap;
	}

	public void setPosition(int position)
	{
		_position = position;
	}

	public int getPosition()
	{
		return _position;
	}
}

