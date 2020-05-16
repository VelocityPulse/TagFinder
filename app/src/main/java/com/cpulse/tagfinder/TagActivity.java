package com.cpulse.tagfinder;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cpulse.tagfinder.adapters.TagAdapter;

public class TagActivity extends AppCompatActivity {

    public static String KEY_TAG_LIST = "KEY_TAG_LIST";

    private static String TAG = "TAG ACTIVITY";

    private String[] mTags;
    private TagAdapter mTagAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);

        Bundle lBundle = getIntent().getExtras();
        if (lBundle != null)
            mTags = lBundle.getStringArray(KEY_TAG_LIST);

        initUI();
    }

    public boolean onCreateOptionsMenu(Menu iMenu) {
//        getMenuInflater().inflate(R.menu.topic_menu, iMenu);
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

    private void initUI() {
        setSupportActionBar((Toolbar) findViewById(R.id.activity_tag_toolbar));

        RecyclerView lRecyclerView = findViewById(R.id.tag_recycler_view);
        lRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTagAdapter = new TagAdapter(mTags);
        lRecyclerView.setAdapter(mTagAdapter);

        mTagAdapter.setOnClickListener(new TagAdapter.OnItemClicked() {
            @Override
            public void onItemClicked(int iPosition) {
                // TODO : Item clicked
            }
        });
    }

}