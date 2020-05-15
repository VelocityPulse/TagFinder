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
    private static String HOME_URL = "https://www.google.fr/";

    private WebView mWebView;
    private SwipeRefreshLayout mSwipe;
    private EditText mSearchEditText;

    private String mCurrentURL = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        initUI();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCurrentURL == null)
            processURLSearch(HOME_URL);
        refreshPage(mCurrentURL);
    }

    public boolean onCreateOptionsMenu(Menu iMenu) {
        getMenuInflater().inflate(R.menu.browser_menu, iMenu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem iItem) {
        switch (iItem.getItemId()) {
            case R.id.action_go_back:
                tryToGoBack();
                return true;
            case R.id.action_go_forward:
                tryToGoForward();
                return true;
            case R.id.action_scan:
                /* DO SCAN */
                return true;
        }
        return super.onOptionsItemSelected(iItem);
    }

    @Override
    public void onBackPressed() {
        if (tryToGoBack())
            return;
        super.onBackPressed();
    }

    private void initUI() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.browser_action_bar);

        View lActionBarView = getSupportActionBar().getCustomView();
        mSearchEditText = lActionBarView.findViewById(R.id.edit_text_search);
        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView iTextView, int iActionId, KeyEvent iEvent) {
                boolean oHandled = false;
                if (iActionId == EditorInfo.IME_ACTION_SEARCH) {
                    processURLSearch(iTextView.getText().toString());
                    oHandled = true;
                }
                return oHandled;
            }
        });

        mWebView = findViewById(R.id.web_view);
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void doUpdateVisitedHistory(WebView iView, String iUrl, boolean iIsReload) {
                super.doUpdateVisitedHistory(iView, iUrl, iIsReload);
                if (!iIsReload) {
                    mSearchEditText.setText(iUrl);
                    showLongToast(BrowserActivity.this, iUrl);
                }
            }

            public void onPageFinished(WebView view, String url) {
                mSwipe.setRefreshing(false);
            }

        });

        mSwipe = findViewById(R.id.swipe);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPage(mCurrentURL);
            }
        });
    }

    private void init() {
    }

    private void refreshPage(String iURL) {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.loadUrl(iURL);
        mSwipe.setRefreshing(true);

    }

    private void processURLSearch(String iURL) {
        if (isNullOrEmpty(iURL))
            return;
        else if (iURL.startsWith("http://") || iURL.startsWith("https://"))
            refreshPage(iURL);
        else
            refreshPage("http://" + iURL);
    }

    private boolean tryToGoBack() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            // TODO : Update edit text
            return true;
        }
        return false;
    }

    private boolean tryToGoForward() {
        if (mWebView.canGoForward()) {
            mWebView.goForward();
// TODO : Update edit text
            return true;
        }
        return false;
    }


    // TODO : UTILS
    private void showLongToast(final Activity iActivity, final String iToast) {
        iActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(iActivity, iToast, Toast.LENGTH_LONG).show();
            }
        });
    }

    // TODO : UTILS
    private boolean isNullOrEmpty(String iStr) {
        return iStr == null || iStr.trim().isEmpty();
    }
}
