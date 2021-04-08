package com.example.galleryex.networking;

import com.google.gson.annotations.SerializedName;

public class UploadResponse
{
	@SerializedName("data")
	private Upload _upload;

	@SerializedName("status")
	private int _status;

	@SerializedName("success")
	private boolean _success;

	public Upload getUpload()
	{
		return _upload;
	}

	public int getStatus()
	{
		return _status;
	}

	public boolean isSuccess()
	{
		return _success;
	}

	public class Upload
	{
		@SerializedName("link")
		private String _link;

		public String getLink()
		{
			return _link;
		}
	}
}
