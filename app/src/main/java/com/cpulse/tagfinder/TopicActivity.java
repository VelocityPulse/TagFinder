package com.cpulse.tagfinder;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cpulse.tagfinder.adapters.TopicAdapter;

public class TopicActivity extends AppCompatActivity {

    public static String KEY_TOPIC_LIST = "KEY_TOPIC_LIST";

    private static String TAG = "TOPIC ACTIVITY";

    private String[] mTopics;
    private TopicAdapter mTopicAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        Bundle lBundle = getIntent().getExtras();
        if (lBundle != null) {
            mTopics = lBundle.getStringArray(KEY_TOPIC_LIST);
        }

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
        setSupportActionBar((Toolbar) findViewById(R.id.activity_topic_toolbar));

//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
//        getSupportActionBar().setCustomView(R.layout.action_bar_topic);

        RecyclerView lRecyclerView = findViewById(R.id.topic_recycler_view);
        lRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTopicAdapter = new TopicAdapter(mTopics);
        lRecyclerView.setAdapter(mTopicAdapter);

        mTopicAdapter.setOnClickListener(new TopicAdapter.OnItemClicked() {
            @Override
            public void onItemClicked(int iPosition) {
                // TODO : Item clicked
            }
        });
    }

}
