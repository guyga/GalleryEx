package com.example.galleryex.ui.links;

import com.example.galleryex.persistency.LinksStorage;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class LinksViewModel extends ViewModel
{
	private final LinksStorage           _linksStorage;
	private       LiveData<List<String>> _links;

	public LinksViewModel(LinksStorage linksStorage)
	{
		_linksStorage = linksStorage;
		_links = Transformations.map(_linksStorage.getLinks(), links -> links);
	}

	public LiveData<List<String>> getLinks()
	{
		return _links;
	}
}
