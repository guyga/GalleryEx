package com.example.galleryex.ui.links;

import com.example.galleryex.persistency.LinksStorage;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class LinksViewModelFactory implements ViewModelProvider.Factory
{
	private LinksStorage _linksStorage;

	public LinksViewModelFactory(LinksStorage linksStorage)
	{
		_linksStorage = linksStorage;
	}

	@NonNull
	@Override
	public <T extends ViewModel> T create(@NonNull Class<T> modelClass)
	{
		if (modelClass.isAssignableFrom(LinksViewModel.class))
		{
			return (T) new LinksViewModel(_linksStorage);
		}
		throw new IllegalArgumentException("Unknown ViewModel Class");
	}
}
