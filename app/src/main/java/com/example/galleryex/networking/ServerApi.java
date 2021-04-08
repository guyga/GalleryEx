package com.example.galleryex.networking;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Contains the necessary networking
 */
public class ServerApi
{
	private static final String CLIENT_ID     = "71db532a4f0b802";
	private static final String CLIENT_SECRET = "20ea058872ab14426e076ded6f62ce4638a0c898";

	private final IServerApi _serviceApi;

	public ServerApi()
	{
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("https://api.imgur.com")
				.addConverterFactory(GsonConverterFactory.create())
				.build();

		_serviceApi = retrofit.create(IServerApi.class);
	}

	public IServerApi getServiceApi()
	{
		return _serviceApi;
	}

	public interface IServerApi
	{
		@Multipart
		@Headers("Authorization: Client-ID " + CLIENT_ID)
		@POST("/3/upload")
		Call<UploadResponse> uploadImage(@Part MultipartBody.Part image);
	}
}
