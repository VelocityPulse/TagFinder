package com.cpulse.tagfinder;

import android.os.Bundle;
import android.view.View;

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

    private ArticleAdapter mArticleAdapter;
    private String mAPIQuery = "";

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

        NewsAPIRequest lRequest = new NewsAPIRequest();
        lRequest.requestForArticleAndBlog("test", new NewsAPIRequest.OnRequestResult() {
            @Override
            public void onRequestResult(final ArticleObject[] iArticleObjects) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mArticleAdapter.setArticleObjectList(iArticleObjects);
                    }
                });
            }

            @Override
            public void onRequestError() {

            }

            @Override
            public void onInternetMissing() {

            }
        });
        initializeGUI();
    }

    private void initializeGUI() {
        RecyclerView lRecyclerView = findViewById(R.id.article_recycler_view);
        lRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mArticleAdapter = new ArticleAdapter(new ArticleObject[0]);
        lRecyclerView.setAdapter(mArticleAdapter);
    }

    public void onHomeButtonPressed(View view) {
        onBackPressed();
    }
}
