package com.cpulse.tagfinder.newsapi;

import com.cpulse.tagfinder.core.LogManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsAPIRequest {

    private static String TAG = "NEWS API";

    private static String API_KEY = "c366d4ade7e64b1db376e26c1639b31d";
    private static String API_URL = "https://newsapi.org/v2/everything";
    private static String API_URL_KEY = API_URL + "?apiKey=" + API_KEY;

    private Thread mRequestThread;
    private boolean mInterruptThread = false;
    private OnRequestResult mOnRequestResult;

    public void requestForArticleAndBlog(final String iTextRequest,
                                         final OnRequestResult iOnRequestResult) {

        mOnRequestResult = iOnRequestResult;
        mInterruptThread = false;
        mRequestThread = new Thread(new Runnable() {
            @Override
            public void run() {
                LogManager.info(TAG, "Start thread");
                HttpURLConnection lURLConnection = null;
                String lURLRequestLink = API_URL_KEY;
                lURLRequestLink += "&q=" + iTextRequest;
                lURLRequestLink += "&sortBy=popularity";

                try {

                    URL lURL = new URL(lURLRequestLink);
                    lURLConnection = (HttpURLConnection) lURL.openConnection();
                    InputStream lInputStream = new BufferedInputStream(lURLConnection.getInputStream());
                    parseJSONObject(readStream(lInputStream));

                } catch (MalformedURLException iE) {
                    iE.printStackTrace();
                } catch (IOException iE) {
                    iE.printStackTrace();
                } finally {
                    if (lURLConnection != null)
                        lURLConnection.disconnect();
                }
                LogManager.info(TAG, "Request API finished");

            }
        });
        mRequestThread.start();
    }

    public void stopRequest() {
        if (mRequestThread != null && mRequestThread.isAlive()) {
            mInterruptThread = true;
            mRequestThread.interrupt();
        }
    }

    private String readStream(InputStream iInputStream) {
        String oOutput = null;
        ByteArrayOutputStream lResult = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int lLength;

        try {
            while ((lLength = iInputStream.read(buffer)) != -1) {
                lResult.write(buffer, 0, lLength);
            }
            oOutput = lResult.toString("UTF-8");
        } catch (UnsupportedEncodingException iE) {
            iE.printStackTrace();
        } catch (IOException iE) {
            iE.printStackTrace();
        }

        return oOutput;
    }

    private void parseJSONObject(String iCompleteJSON) {
        List<ArticleObject> lArticleObjectList = new ArrayList<>();
        JSONObject lRootJSON;
        JSONArray lJSONArticleArray;
        JSONObject lJSONArticle;

        try {
            lRootJSON = new JSONObject(iCompleteJSON);
            lJSONArticleArray = lRootJSON.getJSONArray("articles");
            int lIndex = -1;
            while (++lIndex < lJSONArticleArray.length()) {
                lJSONArticle = lJSONArticleArray.getJSONObject(lIndex);

                lArticleObjectList.add(new ArticleObject(
                        lJSONArticle.getString("title"),
                        lJSONArticle.getString("description"),
                        lJSONArticle.getString("url"),
                        lJSONArticle.getString("urlToImage")
                ));
            }

        } catch (JSONException iE) {
            iE.printStackTrace();
        }

        if (mOnRequestResult != null)
            mOnRequestResult.onRequestResult(lArticleObjectList.toArray(new ArticleObject[0]));
    }


    public interface OnRequestResult {
        void onRequestResult(ArticleObject[] iArticleObjects);

        void onRequestError();

        void onInternetMissing();
    }
}
