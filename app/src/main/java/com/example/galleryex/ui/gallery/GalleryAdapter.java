package com.example.galleryex.ui.gallery;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.galleryex.databinding.GridViewItemBinding;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class GalleryAdapter extends ListAdapter<ImageWrapper, GalleryAdapter.ImageHolder>
{
	private final OnImageSelectedListener _imageSelectedListener;

	protected GalleryAdapter(@NonNull DiffUtil.ItemCallback<ImageWrapper> diffCallback, @NonNull OnImageSelectedListener imageSelectedListener)
	{
		super(diffCallback);
		_imageSelectedListener = imageSelectedListener;
	}

	@NonNull
	@Override
	public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		GridViewItemBinding binding = GridViewItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
		return new ImageHolder(binding);
	}

	@Override
	public void onBindViewHolder(@NonNull ImageHolder holder, int position)
	{
		ImageWrapper imageWrapper = getItem(position);
		holder.bind(imageWrapper);
	}

	class ImageHolder extends RecyclerView.ViewHolder
	{
		private final GridViewItemBinding _binding;

		public ImageHolder(@NonNull GridViewItemBinding binding)
		{
			super(binding.getRoot());
			_binding = binding;
		}

		public void bind(ImageWrapper imageWrapper)
		{
			itemView.setOnClickListener((v) ->
			{
				if (imageWrapper.getBlurredBitmap() == null)
				{
					imageWrapper.setBlurState(ImageWrapper.BlurState.IN_PROGRESS);
					notifyItemChanged(getAdapterPosition());

					imageWrapper.setPosition(getAdapterPosition());
					_imageSelectedListener.onImageSelected(imageWrapper);
				}
			});
			_binding.setImageWrapper(imageWrapper);
			_binding.executePendingBindings();
		}
	}

	interface OnImageSelectedListener
	{
		void onImageSelected(ImageWrapper imageWrapper);
	}
}
