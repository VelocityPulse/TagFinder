package com.cpulse.tagfinder;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cpulse.tagfinder.adapters.ArticleAdapter;
import com.cpulse.tagfinder.core.Utils;

public class ArticleActivity extends AppCompatActivity {

    public static String KEY_API_QUERY;
    private static String TAG = "ARTICLE ACTIVITY";

    private ArticleAdapter mArticleAdapter;
    private String mAPIQuery = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    }


}
