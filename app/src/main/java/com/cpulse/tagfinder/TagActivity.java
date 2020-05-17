package com.cpulse.tagfinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cpulse.tagfinder.adapters.TagAdapter;
import com.cpulse.tagfinder.core.Utils;

public class TagActivity extends AppCompatActivity {

    public static String KEY_TAG_LIST = "KEY_TAG_LIST";

    private static String TAG = "TAG ACTIVITY";

    private String[] mTags;
    private TagAdapter mTagAdapter;

    @Override
    public void onCreate(@Nullable Bundle iSavedInstanceState) {
        super.onCreate(iSavedInstanceState);
        setContentView(R.layout.activity_tag);

        Bundle lBundle = getIntent().getExtras();
        if (lBundle != null)
            mTags = lBundle.getStringArray(KEY_TAG_LIST);
        if (mTags == null || mTags.length < 1) {
            Utils.showLongToast(this, "An error occurred...");
            return;
        }
        initializeGUI();
    }

    public boolean onCreateOptionsMenu(Menu iMenu) {
        return super.onCreateOptionsMenu(iMenu);
    }

    public boolean onOptionsItemSelected(MenuItem iItem) {
        switch (iItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(iItem);
    }

    private void initializeGUI() {
        setSupportActionBar((Toolbar) findViewById(R.id.activity_tag_toolbar));

        RecyclerView lRecyclerView = findViewById(R.id.tag_recycler_view);
        lRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTagAdapter = new TagAdapter(mTags);
        lRecyclerView.setAdapter(mTagAdapter);

        mTagAdapter.setOnClickListener(new TagAdapter.OnItemClicked() {
            @Override
            public void onItemClicked(int iPosition) {
                startArticleActivity(iPosition);
            }
        });
    }

    private void startArticleActivity(int iTagIndex) {
        Intent myIntent = new Intent(TagActivity.this, ArticleActivity.class);
        myIntent.putExtra(ArticleActivity.KEY_API_QUERY, mTags[iTagIndex]);
        startActivity(myIntent);
    }

    public void onHomeButtonPressed(View iView) {
        onBackPressed();
    }
}