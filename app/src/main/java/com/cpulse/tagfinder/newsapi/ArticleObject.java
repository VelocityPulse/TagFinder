package com.cpulse.tagfinder.newsapi;

import android.graphics.Bitmap;

import com.cpulse.tagfinder.ImageDownloaderAsyncTask;
import com.cpulse.tagfinder.core.LogManager;

public class ArticleObject {

    private static final String TAG = "ARTICLE OBJECT";

    private String mTitle;
    private String mDescription;
    private String mURL;
    private String mURLToImage;
    private Bitmap mBitmap;

    private ImageDownloaderAsyncTask mBitmapAsyncTask;

    private OnBitmapReady mOnBitmapReady;

    public ArticleObject(String iTitle, String iDescription, String iURL, String iURLToImage) {
        mTitle = iTitle;
        mDescription = iDescription;
        mURL = iURL;
        mURLToImage = iURLToImage;
    }

    public void startImageDownload() {
        if (mBitmapAsyncTask != null)
            return;

        mBitmapAsyncTask = new ImageDownloaderAsyncTask(new ImageDownloaderAsyncTask.OnAsyncTaskListener() {
            @Override
            public void onSuccessDownload(Bitmap iBitmap) {
                LogManager.info(TAG, "On success download");
                mBitmap = iBitmap;
                if (mOnBitmapReady != null)
                    mOnBitmapReady.onBitmapReady();
            }

            @Override
            public void onDownloadError() {
                mBitmapAsyncTask = null;
            }
        });
        mBitmapAsyncTask.execute(mURLToImage);
    }

    public void abortImageDownload() {
        if (mBitmapAsyncTask != null) {
            mBitmapAsyncTask.cancel(true);
        }
        mBitmap = null;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getURL() {
        return mURL;
    }

    public String getURLToImage() {
        return mURLToImage;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setOnBitmapReady(OnBitmapReady iOnBitmapReady) {
        mOnBitmapReady = iOnBitmapReady;
    }

    public interface OnBitmapReady {
        void onBitmapReady();
    }
}