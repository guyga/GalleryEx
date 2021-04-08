package com.example.galleryex.ui.gallery;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

class DiffCallback extends DiffUtil.ItemCallback<ImageWrapper>
{

	@Override
	public boolean areItemsTheSame(@NonNull ImageWrapper oldItem, @NonNull ImageWrapper newItem)
	{
		return oldItem == newItem;
	}

	@Override
	public boolean areContentsTheSame(@NonNull ImageWrapper oldItem, @NonNull ImageWrapper newItem)
	{
		return oldItem.getUri().getPath().equals(newItem.getUri().getPath());
	}
}
