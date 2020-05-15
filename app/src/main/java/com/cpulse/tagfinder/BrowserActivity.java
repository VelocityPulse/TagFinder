package com.cpulse.tagfinder;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cpulse.tagfinder.core.LogManager;
import com.cpulse.tagfinder.core.Utils;

import java.io.InputStream;
import java.io.OutputStream;

public class BrowserActivity extends AppCompatActivity {

    private static String TAG = "BROWSER ACTIVITY";
    private static String HOME_URL = "https://www.google.fr/";

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

    @JavascriptInterface
    public void processHTML(String html) {
        if (html == null)
            return;
        int interval = 2400;

        int arrayLength = (int) Math.ceil(((html.length() / (double)interval)));
        String[] result = new String[arrayLength];

        int j = 0;
        int lastIndex = result.length - 1;
        for (int i = 0; i < lastIndex; i++) {
            result[i] = html.substring(j, j + interval);
            j += interval;
        } //Add the last bit
        result[lastIndex] = html.substring(j);


        for (String item : result) {
            LogManager.error(item);
        }
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
        mWebView.addJavascriptInterface(this, "HTMLOUT");

        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void doUpdateVisitedHistory(WebView iView, String iUrl, boolean iIsReload) {
                super.doUpdateVisitedHistory(iView, iUrl, iIsReload);
                if (!iIsReload)
                    mSearchEditText.setText(iUrl);
            }

            @Override
            public void onPageStarted(WebView iView, String url, Bitmap iFavIcon) {
                mSwipe.setRefreshing(true);
                super.onPageStarted(iView, url, iFavIcon);
            }

            public void onPageFinished(WebView iView, String iUrl) {
                mSwipe.setRefreshing(false);
                super.onPageFinished(iView, iUrl);

                mWebView.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
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

/*
    int interval = 2400;

    int arrayLength = (int) Math.ceil(((html.length() / (double)interval)));
    String[] result = new String[arrayLength];

    int j = 0;
    int lastIndex = result.length - 1;
                                for (int i = 0; i < lastIndex; i++) {
        result[i] = html.substring(j, j + interval);
        j += interval;
    } //Add the last bit
    result[lastIndex] = html.substring(j);


                                for (String item : result) {
        LogManager.error(item);
    }
*/


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

//        final AlertDialog lDialog = Utils.createProgressDialogNoButton(this);
//        lDialog.show();
//        mWebView.evaluateJavascript(
//                "(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();",
//                new ValueCallback<String>() {
//                    @Override
//                    public void onReceiveValue(String html) {
//                        Log.d("HTML", html);
//                        lDialog.dismiss();
//                        if (html.contains("<meta property="))
//                            LogManager.error(TAG, "CONTAINS");
//                    }
//                });
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
