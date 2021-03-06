package com.cpulse.tagfinder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cpulse.tagfinder.core.LogManager;
import com.cpulse.tagfinder.core.Utils;

/*
    Hours : 1h30 Research & Documentation
            Friday : 3pm to 7pm (4h)
            Saturday : 4pm to 8:30pm (4h30)
            Sunday : 4pm to 10pm (6h) (-1h eating)
            10min norm
            Total 14h
 */

public class BrowserActivity extends AppCompatActivity {

    private static String TAG = "BROWSER ACTIVITY";
    private static String HOME_URL = "https://migrationology.com/south-korean-food-dishes/";

    private WebView mWebView;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipe;
    private EditText mSearchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        initializeGUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.isNullOrEmpty(mWebView.getUrl()))
            processURLSearch(HOME_URL);
    }

    public boolean onCreateOptionsMenu(Menu iMenu) {
        getMenuInflater().inflate(R.menu.browser_menu, iMenu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem iItem) {
        switch (iItem.getItemId()) {
            case android.R.id.home:
                mWebView.reload();
            case R.id.action_go_back:
                tryToGoBack();
                return true;
            case R.id.action_go_forward:
                tryToGoForward();
                return true;
            case R.id.action_scan:
                startScan();
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

    private void initializeGUI() {
        setSupportActionBar((Toolbar) findViewById(R.id.activity_browser_toolbar));

        mProgressBar = findViewById(R.id.browser_progressbar);
        mProgressBar.setMin(0);
        mProgressBar.setMax(100);
        mSearchEditText = findViewById(R.id.edit_text_search);
        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView iTextView, int iActionId, KeyEvent iEvent) {
                boolean oHandled = false;
                if (iActionId == EditorInfo.IME_ACTION_SEARCH) {
                    processURLSearch(iTextView.getText().toString());
                    oHandled = true;
                    Utils.hideKeyboard(BrowserActivity.this, iTextView);
                }
                return oHandled;
            }
        });

        mWebView = findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.setWebViewClient(new TagFinderWebViewClient(mWebView) {

            @Override
            public void doUpdateVisitedHistory(WebView iView, String iUrl, boolean iIsReload) {
                if (!iIsReload)
                    mSearchEditText.setText(iUrl);
                super.doUpdateVisitedHistory(iView, iUrl, iIsReload);
            }

            @Override
            public void onPageStarted(WebView iWebView, String iUrl, Bitmap iFavicon) {
                mProgressBar.setProgress(0);
                mSwipe.setRefreshing(true);
                super.onPageStarted(iWebView, iUrl, iFavicon);
            }

            public void onPageFinished(WebView iView, String iUrl) {
                mSwipe.setRefreshing(false);
                super.onPageFinished(iView, iUrl);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView iView, int iNewProgress) {
                LogManager.info(TAG, "PROGRESS : " + iNewProgress);
                super.onProgressChanged(iView, iNewProgress);
                mProgressBar.setProgress(iNewProgress);
            }
        });

        mSwipe = findViewById(R.id.swipe);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mWebView.reload();
            }
        });
    }

    private void processURLSearch(String iURL) {
        if (Utils.isNullOrEmpty(iURL))
            return;
        else if (iURL.startsWith("http://") || iURL.startsWith("https://"))
            mWebView.loadUrl(iURL);
        else
            mWebView.loadUrl("http://" + iURL);
    }

    private void startScan() {
        if (mSwipe.isRefreshing()) {
            Utils.showToast(this, "Page loading");
            return;
        }

        String lHTML = ((TagFinderWebViewClient) mWebView.getWebViewClient()).getHTML();

        String[] lTags = MetaParser.getArticlesTag(lHTML);
        if (lTags.length < 1) {
            Utils.showToast(this, "No articles found");
        } else {
            startTagActivity(lTags);
        }
    }

    private void startTagActivity(String[] iTags) {
        Intent lIntent = new Intent(BrowserActivity.this, TagActivity.class);
        lIntent.putExtra(TagActivity.KEY_TAG_LIST, iTags);
        startActivity(lIntent);
    }

    private boolean tryToGoBack() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }

    private boolean tryToGoForward() {
        if (mWebView.canGoForward()) {
            mWebView.goForward();
            return true;
        }
        return false;
    }

    public void onClickRefresh(View iView) {
        mWebView.reload();
    }
}