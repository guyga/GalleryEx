package com.example.galleryex.repository;

import com.example.galleryex.networking.ServerApi;
import com.example.galleryex.networking.UploadResponse;

import java.io.File;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Making file upload requests through {@link ServerApi}
 */
public class ImageRepository
{
	private ServerApi.IServerApi _serverApi;

	public ImageRepository(ServerApi.IServerApi serverApi)
	{
		_serverApi = serverApi;
	}

	/**
	 * Uploads a given file and returns the result in the form of {@link Result}
	 *
	 * @param file - file to be uploaded
	 * @return upload result
	 */
	public LiveData<Result<UploadResponse>> upload(File file)
	{
		MutableLiveData<Result<UploadResponse>> liveData = new MutableLiveData<>();

		RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
		MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

		_serverApi.uploadImage(body).enqueue(new Callback<UploadResponse>()
		{
			@Override
			public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response)
			{
				if (response.isSuccessful() && response.body().isSuccess())
				{
					liveData.postValue(new Result.Success<UploadResponse>(response.body()));
				}
				else
				{
					liveData.postValue(new Result.Error<UploadResponse>(new Exception(response.message())));
				}
			}

			@Override
			public void onFailure(Call<UploadResponse> call, Throwable t)
			{
				liveData.postValue(new Result.Error<UploadResponse>(t));
			}
		});

		return liveData;
	}
}
