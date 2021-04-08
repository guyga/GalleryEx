package com.example.galleryex.ui;

import android.app.Application;

import com.example.galleryex.gallery.BlurApplier;
import com.example.galleryex.gallery.GalleryProvider;
import com.example.galleryex.networking.ServerApi;
import com.example.galleryex.persistency.LinksStorage;
import com.example.galleryex.repository.ImageRepository;
import com.example.galleryex.repository.ImageUploadManager;
import com.example.galleryex.ui.gallery.GalleryViewModelFactory;
import com.example.galleryex.ui.links.LinksViewModelFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainApplication extends Application
{
	private ExecutorService _executorService;

	private GalleryProvider    _galleryProvider;
	private BlurApplier        _blurApplier;
	private ServerApi          _serverApi;
	private ImageRepository    _imageRepository;
	private ImageUploadManager _imageUploadManager;
	private LinksStorage       _linksStorage;

	private GalleryViewModelFactory _galleryViewModelFactory;
	private LinksViewModelFactory   _linksViewModelFactory;

	@Override
	public void onCreate()
	{
		super.onCreate();

		_executorService = Executors.newScheduledThreadPool(1);

		_galleryProvider = new GalleryProvider(this, _executorService);
		_blurApplier = new BlurApplier(this, _executorService);
		_serverApi = new ServerApi();
		_imageRepository = new ImageRepository(_serverApi.getServiceApi());
		_linksStorage = new LinksStorage(this, _executorService);
		_imageUploadManager = new ImageUploadManager(this, _blurApplier, _imageRepository, _linksStorage, _executorService);

		_galleryViewModelFactory = new GalleryViewModelFactory(_galleryProvider, _imageUploadManager);
		_linksViewModelFactory = new LinksViewModelFactory(_linksStorage);
	}

	public GalleryViewModelFactory getGalleryViewModelFactory()
	{
		return _galleryViewModelFactory;
	}

	public LinksViewModelFactory getLinksViewModelFactory()
	{
		return _linksViewModelFactory;
	}
}
