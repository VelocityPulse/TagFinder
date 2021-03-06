package com.cpulse.tagfinder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.cpulse.tagfinder.core.LogManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageDownloaderAsyncTask extends AsyncTask<String, Void, Bitmap> {

    private static final String TAG = "IMAGE DOWNLOADER";

    private OnAsyncTaskListener mOnAsyncTaskListener;

    public ImageDownloaderAsyncTask(OnAsyncTaskListener iOnAsyncTaskListener) {
        mOnAsyncTaskListener = iOnAsyncTaskListener;
    }

    @Override
    protected Bitmap doInBackground(String... iParams) {
        return downloadBitmap(iParams[0]);
    }

    @Override
    protected void onPostExecute(Bitmap iBitmap) {
        if (isCancelled() || iBitmap == null) {
            return;
        }

        if (mOnAsyncTaskListener != null)
            mOnAsyncTaskListener.onSuccessDownload(iBitmap);
    }

    private Bitmap downloadBitmap(String iUrl) {
        HttpURLConnection lURLConnection = null;

        try {
            URL lURL = new URL(iUrl);
            lURLConnection = (HttpURLConnection) lURL.openConnection();
            InputStream lInputStream = lURLConnection.getInputStream();
            if (lInputStream != null)
                return BitmapFactory.decodeStream(lInputStream);

        } catch (MalformedURLException iE) {
            iE.printStackTrace();

        } catch (IOException iE) {
            if (lURLConnection != null)
                lURLConnection.disconnect();
            LogManager.error(TAG, "Error downloading image from " + iUrl);
            iE.printStackTrace();

        } finally {
            if (lURLConnection != null) {
                lURLConnection.disconnect();
            }
        }
        return null;
    }

    public interface OnAsyncTaskListener {
        void onSuccessDownload(Bitmap iBitmap);

        void onDownloadError();
    }
}