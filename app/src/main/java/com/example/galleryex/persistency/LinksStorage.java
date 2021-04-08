package com.example.galleryex.persistency;

import android.content.Context;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * Saves and retrieves the saved data through a local file
 */
public class LinksStorage
{
	private static final String                        LINKS_FILE = "uploadedLinks.json";
	private final        String                        _dirPath;
	private final        ExecutorService               _executorService;
	private              MutableLiveData<List<String>> _linksLiveData;

	public LinksStorage(Context context, ExecutorService executorService)
	{
		_dirPath = context.getFilesDir().getPath();
		_executorService = executorService;

		_linksLiveData = new MutableLiveData<>();
		getLinksInternal(links -> _linksLiveData.postValue(links));
	}

	/**
	 * Adds link to storage
	 *
	 * @param link - link to be added
	 */
	public void insert(String link)
	{
		getLinksInternal(links ->
		{
			links.add(link);
			writeToFile(_dirPath + File.separator + LINKS_FILE, links);
			_linksLiveData.postValue(links);
		});
	}

	public LiveData<List<String>> getLinks()
	{
		return _linksLiveData;
	}

	/**
	 * retrieves the saved links as a list
	 *
	 * @param callback - links callback
	 */
	private void getLinksInternal(@NonNull LinksListCallback callback)
	{
		_executorService.submit(() ->
		{
			List<String> uploadedLinks = new ArrayList<>();
			File file = new File(_dirPath + File.separator + LINKS_FILE);
			if (file.exists())
			{
				try
				{
					InputStream is = new FileInputStream(file);
					int size = is.available();
					byte[] buffer = new byte[size];
					is.read(buffer);
					is.close();

					String json = new String(buffer, "UTF-8");
					uploadedLinks = new Gson().fromJson(json, List.class);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			callback.onLinkListReady(uploadedLinks);
		});
	}

	/**
	 * @param filePath - file to save data to
	 * @param object   - data to be saved
	 */
	private void writeToFile(String filePath, Object object)
	{
		_executorService.submit(() ->
		{
			File file = new File(filePath);
			String jsonStr = new Gson().toJson(object);
			try
			{
				FileWriter writer = new FileWriter(file, false);
				writer.write(jsonStr);
				writer.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		});
	}

	interface LinksListCallback
	{
		void onLinkListReady(List<String> links);
	}
}
