package com.cpulse.tagfinder;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cpulse.tagfinder.adapters.ArticleAdapter;
import com.cpulse.tagfinder.core.Utils;
import com.cpulse.tagfinder.newsapi.ArticleObject;
import com.cpulse.tagfinder.newsapi.NewsAPIRequest;

public class ArticleActivity extends AppCompatActivity {

    public static String KEY_API_QUERY;
    private static String TAG = "ARTICLE ACTIVITY";

    private RecyclerView mRecyclerView;
    private ArticleAdapter mArticleAdapter;
    private ProgressBar mProgressBar;
    private String mAPIQuery;
    private ArticleObject[] mArticleObjects;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Bundle lBundle = getIntent().getExtras();
        if (lBundle != null)
            mAPIQuery = lBundle.getString(KEY_API_QUERY);
        if (Utils.isNullOrEmpty(mAPIQuery)) {
            Utils.showLongToast(this, "An error occurred...");
            return;
        }

        initializeGUI();
        initializeRequestAPI();
    }

    @Override
    protected void onDestroy() {
        if (mArticleObjects != null) {
            for (ArticleObject lItem : mArticleObjects) {
                lItem.abortImageDownload();
            }
        }
        super.onDestroy();
    }

    private void initializeGUI() {
        mProgressBar = findViewById(R.id.article_loading_logo);
        ((TextView) findViewById(R.id.article_title)).setText(mAPIQuery);

        mRecyclerView = findViewById(R.id.article_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mArticleAdapter = new ArticleAdapter(new ArticleObject[0]);
        mRecyclerView.setAdapter(mArticleAdapter);
    }

    private void initializeRequestAPI() {
        NewsAPIRequest lRequest = new NewsAPIRequest();
        lRequest.requestForArticleAndBlog(mAPIQuery, new NewsAPIRequest.OnRequestResult() {
            @Override
            public void onRequestResult(final ArticleObject[] iArticleObjects) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingLogo();
                        showRecyclerView(iArticleObjects);
                        mArticleObjects = iArticleObjects;
                    }
                });
            }

            @Override
            public void onRequestError() {
                Utils.showToast(ArticleActivity.this, "An error occurred in the request api");
                finish();
            }

            @Override
            public void onInternetMissing() {
                Utils.showToast(ArticleActivity.this, "Internet is missing");
                finish();
            }
        });
    }

    private void showRecyclerView(ArticleObject[] iArticleObjects) {
        AlphaAnimation lFadeIn = new AlphaAnimation(0, 1);
        lFadeIn.setInterpolator(new AccelerateInterpolator());
        lFadeIn.setDuration(500);
        lFadeIn.setStartOffset(500);
        mRecyclerView.startAnimation(lFadeIn);
        mArticleAdapter.setArticleObjectList(iArticleObjects);
    }

    private void hideLoadingLogo() {
        AlphaAnimation lFadeOut = new AlphaAnimation(1, 0);
        lFadeOut.setInterpolator(new AccelerateInterpolator());
        lFadeOut.setDuration(800);
        lFadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mProgressBar.startAnimation(lFadeOut);
    }

    public void onHomeButtonPressed(View view) {
        onBackPressed();
    }
}