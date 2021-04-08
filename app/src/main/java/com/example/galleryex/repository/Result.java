package com.example.galleryex.repository;

public abstract class Result<T>
{
	private Result()
	{
	}

	public static final class Success<T> extends Result<T>
	{
		public T _data;

		public Success(T data)
		{
			_data = data;
		}
	}

	public static final class Error<T> extends Result<T>
	{
		public Throwable _throwable;

		public Error(Throwable throwable)
		{
			_throwable = throwable;
		}

		public String getError()
		{
			return _throwable.getMessage();
		}
	}
}
