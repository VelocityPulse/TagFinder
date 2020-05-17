package com.cpulse.tagfinder.newsapi;

import android.graphics.Bitmap;

import com.cpulse.tagfinder.ImageDownloaderAsyncTask;

public class ArticleObject {

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