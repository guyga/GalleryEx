package com.example.galleryex.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.galleryex.R;
import com.example.galleryex.databinding.FragmentGalleryBinding;
import com.example.galleryex.networking.UploadResponse;
import com.example.galleryex.repository.Result;
import com.example.galleryex.ui.MainApplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

public class GalleryFragment extends Fragment implements GalleryAdapter.OnImageSelectedListener
{
	private FragmentGalleryBinding _binding;
	private GalleryViewModel       _viewModel;
	private AlertDialog            _dialog;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		_binding = FragmentGalleryBinding.inflate(inflater);
		_binding.setLifecycleOwner(getViewLifecycleOwner());
		_viewModel =
				new ViewModelProvider(this, ((MainApplication) getActivity().getApplication()).getGalleryViewModelFactory()).get(GalleryViewModel.class);
		_binding.setViewModel(_viewModel);
		_binding.imagesGrid.setAdapter(new GalleryAdapter(new DiffCallback(), this));

		setHasOptionsMenu(true);

		return _binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		_viewModel.getUploadResponse().observe(getViewLifecycleOwner(), uploadTask ->
		{
			Result<UploadResponse> response = uploadTask.getResponse();
			if (response instanceof Result.Error)
			{
				showErrorDialog(((Result.Error) response).getError());
			}
			((GalleryAdapter) _binding.imagesGrid.getAdapter()).notifyItemChanged(uploadTask.getImageWrapper().getPosition());
		});

		_viewModel.getNavigateToLinks().observe(getViewLifecycleOwner(), navigate ->
		{
			if (navigate)
			{
				Navigation.findNavController(getView()).navigate(R.id.action_galleryFragment_to_linksFragment);
				_viewModel.doneNavigateToLinks();
			}
		});
	}

	private void showErrorDialog(String error)
	{
		if (_dialog == null)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(error);
			builder.setCancelable(false);
			builder.setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss());
			builder.setOnDismissListener(dialog -> _dialog = null);
			_dialog = builder.show();
		}
	}

	@Override
	public void onImageSelected(ImageWrapper imageWrapper)
	{
		_viewModel.startBlurProcess(imageWrapper);
	}

	@Override
	public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
	{
		inflater.inflate(R.menu.gallery_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item)
	{
		if (item.getItemId() == R.id.links)
		{
			_viewModel.navigateToLinks();
			return true;
		}
		return false;
	}

	@Override
	public void onDestroy()
	{
		if (_dialog != null)
		{
			_dialog.dismiss();
		}
		super.onDestroy();
	}
}
