package com.example.galleryex.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.galleryex.R;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		if (!hasStoragePermission())
		{
			requestStoragePermission();
		}
		else
		{
			startNavigation();
		}
	}

	private boolean hasStoragePermission()
	{
		return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
	}

	private void requestStoragePermission()
	{
		registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->
		{
			if (isGranted)
			{
				startNavigation();
			}
			else
			{
				finish();
			}
		}).launch(Manifest.permission.READ_EXTERNAL_STORAGE);
	}

	private void startNavigation()
	{
		setContentView(R.layout.activity_main);
	}
}