package com.example.galleryex.ui.gallery;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.galleryex.R;

import java.util.List;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class GalleryBindingAdapter
{
	@BindingAdapter("imagesList")
	public static void bindImages(RecyclerView recyclerView, List<ImageWrapper> images)
	{
		((GalleryAdapter) recyclerView.getAdapter()).submitList(images);
	}

	@BindingAdapter("imageUri")
	public static void bindImage(ImageView imageView, ImageWrapper imageWrapper)
	{
		if (imageWrapper.getBlurredBitmap() == null)
		{
			Glide.with(imageView.getContext())
					.load(imageWrapper.getUri())
					.apply(new RequestOptions().centerCrop())
					.into(imageView);
		}
		else
		{
			Glide.with(imageView.getContext())
					.load(imageWrapper.getBlurredBitmap())
					.apply(new RequestOptions().centerCrop())
					.into(imageView);
		}
	}

	@BindingAdapter("loadingStatus")
	public static void bindLoader(View view, GalleryLoadingStatus status)
	{
		if (status == GalleryLoadingStatus.LOADING)
		{
			view.setVisibility(View.VISIBLE);
		}
		else
		{
			view.setVisibility(View.GONE);
		}
	}

	@BindingAdapter("emptyTextStatus")
	public static void bindEmptyText(TextView textView, GalleryLoadingStatus status)
	{
		switch (status)
		{
			case LOADING:
			case DONE:
			{
				textView.setVisibility(View.GONE);
				break;
			}
			case EMPTY:
			{
				textView.setText(R.string.no_data_available);
				textView.setVisibility(View.VISIBLE);
				break;
			}
			case ERROR:
			{
				textView.setText(R.string.error_fetching_data);
				textView.setVisibility(View.VISIBLE);
				break;
			}
		}
	}

	@BindingAdapter("blurState")
	public static void bindBlurState(View view, ImageWrapper.BlurState blurState)
	{
		if (blurState == ImageWrapper.BlurState.IN_PROGRESS)
		{
			view.setVisibility(View.VISIBLE);
		}
		else
		{
			view.setVisibility(View.GONE);
		}
	}
}

