package com.example.galleryex.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import com.example.galleryex.ui.gallery.ImageWrapper;

import java.util.concurrent.ExecutorService;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * Applies blur to a given image
 */
public class BlurApplier
{
	private static final int             BLUR_RADIUS = 15; // Supported range 0 < radius <= 25
	private final        ExecutorService _executorService;
	private final        Context         _context;

	public BlurApplier(Context context, ExecutorService executorService)
	{
		_context = context;
		_executorService = executorService;
	}

	/**
	 * Applies blur to the image represented by {@link ImageWrapper#getUri()}
	 *
	 * @param imageWrapper image container to be blurred
	 * @return imageWrapper after blur
	 */
	public LiveData<ImageWrapper> applyBlur(@NonNull ImageWrapper imageWrapper)
	{
		MutableLiveData<ImageWrapper> liveData = new MutableLiveData<>();
		_executorService.submit(() ->
		{
			try
			{
				Bitmap srcBitmap = MediaStore.Images.Media.getBitmap(_context.getContentResolver(), imageWrapper.getUri());
				Bitmap copyBitmap = srcBitmap.copy(Bitmap.Config.ARGB_8888, true);
				Bitmap outputBitmap = Bitmap.createBitmap(copyBitmap);

				RenderScript renderScript = RenderScript.create(_context);
				ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));

				Allocation allocationIn = Allocation.createFromBitmap(renderScript, srcBitmap);
				Allocation allocationOut = Allocation.createFromBitmap(renderScript, outputBitmap);

				scriptIntrinsicBlur.setRadius(BLUR_RADIUS);
				scriptIntrinsicBlur.setInput(allocationIn);
				scriptIntrinsicBlur.forEach(allocationOut);

				allocationOut.copyTo(outputBitmap);

				renderScript.destroy();

				imageWrapper.setBlurredBitmap(outputBitmap);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				imageWrapper.setBlurredBitmap(null);
			}
			liveData.postValue(imageWrapper);
		});
		return liveData;
	}
}
