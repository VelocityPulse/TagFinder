package com.cpulse.tagfinder;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cpulse.tagfinder.adapters.ArticleAdapter;
import com.cpulse.tagfinder.core.Utils;
import com.cpulse.tagfinder.newsapi.ArticleObject;

import java.util.ArrayList;

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

        initializeGUI();
    }

    private void initializeGUI() {
        RecyclerView lRecyclerView = findViewById(R.id.article_recycler_view);
        lRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mArticleAdapter = new ArticleAdapter(new ArrayList<ArticleObject>());
        lRecyclerView.setAdapter(mArticleAdapter);
    }
}
