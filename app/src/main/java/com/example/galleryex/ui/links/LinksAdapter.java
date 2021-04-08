package com.example.galleryex.ui.links;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.galleryex.databinding.ItemLinkBinding;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class LinksAdapter extends ListAdapter<String, LinksAdapter.ViewHolder>
{
	private OnLinkSelectedListener _onLinkSelectedListener;

	protected LinksAdapter(@NonNull DiffUtil.ItemCallback<String> diffCallback, @NonNull OnLinkSelectedListener onLinkSelectedListener)
	{
		super(diffCallback);
		_onLinkSelectedListener = onLinkSelectedListener;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		ItemLinkBinding binding = ItemLinkBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
		return new ViewHolder(binding);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position)
	{
		String link = getItem(position);
		holder.bind(link);
	}

	class ViewHolder extends RecyclerView.ViewHolder
	{
		private final ItemLinkBinding _binding;

		public ViewHolder(@NonNull ItemLinkBinding binding)
		{
			super(binding.getRoot());
			_binding = binding;
		}

		public void bind(String link)
		{
			_binding.setLink(link);
			itemView.setOnClickListener((v) ->
			{
				_onLinkSelectedListener.onLinkSelected(link);
			});
			_binding.executePendingBindings();
		}
	}

	interface OnLinkSelectedListener
	{
		void onLinkSelected(String link);
	}
}
