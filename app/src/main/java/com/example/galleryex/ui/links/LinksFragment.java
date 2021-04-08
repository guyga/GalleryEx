package com.example.galleryex.ui.links;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.galleryex.databinding.FragmentLinksBinding;
import com.example.galleryex.ui.MainApplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class LinksFragment extends Fragment implements LinksAdapter.OnLinkSelectedListener
{
	private LinksViewModel _viewModel;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		FragmentLinksBinding binding = FragmentLinksBinding.inflate(inflater);
		binding.setLifecycleOwner(getViewLifecycleOwner());
		_viewModel = new ViewModelProvider(this, ((MainApplication) (getActivity().getApplication())).getLinksViewModelFactory()).get(LinksViewModel.class);
		binding.setViewModel(_viewModel);
		binding.recycler.setAdapter(new LinksAdapter(new DiffCallback(), this));
		return binding.getRoot();
	}

	@Override
	public void onLinkSelected(String link)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_BROWSABLE);
		intent.setData(Uri.parse(link));

		PackageManager packageManager = getActivity().getPackageManager();
		if (intent.resolveActivity(packageManager) != null)
		{
			startActivity(intent);
		}
		else
		{
			Toast.makeText(getActivity(), "Unable to open link", Toast.LENGTH_SHORT).show();
		}
	}
}
