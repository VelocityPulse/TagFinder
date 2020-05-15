package com.cpulse.tagfinder;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class BrowserActivity extends AppCompatActivity {

    private static String TAG = "BROWSER ACTIVITY";

    private WebView mView;
    private SwipeRefreshLayout mSwipe;

    private String mCurrentURL = "https://www.google.com/";

    public static boolean isNullOrEmpty(String iStr) {
        return iStr == null || iStr.trim().isEmpty();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        // UI init
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.browser_action_bar);

        View lActionBarView = getSupportActionBar().getCustomView();
        EditText editText = lActionBarView.findViewById(R.id.edit_text_search);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    processURLSearch(v.getText().toString());
                    handled = true;
                }
                return handled;
            }
        });

        mView = findViewById(R.id.web_view);

        mSwipe = findViewById(R.id.swipe);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPage(mCurrentURL);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshPage(mCurrentURL);
    }

    public void refreshPage(String iURL) {

        mView.getSettings().setJavaScriptEnabled(true);
        mView.getSettings().setAppCacheEnabled(true);
        mView.loadUrl(iURL);
        mSwipe.setRefreshing(true);
        mView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                mSwipe.setRefreshing(false);
            }

        });
    }

    public void processURLSearch(String iURL) {
        if (isNullOrEmpty(iURL))
            return;
        else if (iURL.startsWith("http://") || iURL.startsWith("https://"))
            refreshPage(iURL);
        else
            refreshPage("http://" + iURL);
    }

    public boolean onCreateOptionsMenu(Menu iMenu) {
        getMenuInflater().inflate(R.menu.browser_menu, iMenu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem iItem) {
        switch (iItem.getItemId()) {
            case R.id.action_edit:
                /* DO EDIT */
                return true;
            case R.id.action_add:
                /* DO ADD */
                return true;
            case R.id.action_delete:
                /* DO DELETE */
                return true;
        }
        return super.onOptionsItemSelected(iItem);
    }

    public void onClickRefresh(View iView) {
        refreshPage(mCurrentURL);
    }

    // TODO : UTILS
    public void showLongToast(final Activity iActivity, final String iToast) {
        iActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(iActivity, iToast, Toast.LENGTH_LONG).show();
            }
        });
    }


}
