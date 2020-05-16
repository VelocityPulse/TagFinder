package com.cpulse.tagfinder;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cpulse.tagfinder.core.LogManager;
import com.cpulse.tagfinder.core.Utils;

/*
    Hours : Friday : 3pm to 7pm (4h)
            Saturday : 4pm to
 */

public class BrowserActivity extends AppCompatActivity {

    private static String TAG = "BROWSER ACTIVITY";
    private static String HOME_URL = "https://migrationology.com/south-korean-food-dishes/";
    //    private static String HOME_URL = "https://www.google.fr/";

    private WebView mWebView;
    private SwipeRefreshLayout mSwipe;
    private EditText mSearchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        initUI();
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
            public void onPageStarted(WebView iView, String url, Bitmap iFavIcon) {
                mSwipe.setRefreshing(true);
                super.onPageStarted(iView, url, iFavIcon);
            }

            public void onPageFinished(WebView iView, String iUrl) {
                mSwipe.setRefreshing(false);
                super.onPageFinished(iView, iUrl);
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

        String[] lArticles = MetaParser.getArticlesTag(lHTML);
        for (String lItem : lArticles) {
            LogManager.error(TAG, lItem);
        }
        if (lArticles.length < 1) {
            Utils.showToast(this, "No articles found");
        }
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

    public void onClickRefresh(View view) {
        mWebView.reload();
    }
}
