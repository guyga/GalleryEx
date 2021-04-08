package com.example.galleryex.ui.links;

import android.widget.TextView;

import java.util.List;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class LinksBindingAdapter
{
	@BindingAdapter("linksList")
	public static void bindLinks(RecyclerView recyclerView, List<String> links)
	{
		((ListAdapter) recyclerView.getAdapter()).submitList(links);
	}

	@BindingAdapter("link")
	public static void bindLinks(TextView textView, String link)
	{
		textView.setText(link);
	}
}
