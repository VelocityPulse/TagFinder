package com.cpulse.tagfinder;

import android.graphics.Bitmap;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * This class processes the catch of the HTML web page
 */
public class TagFinderWebViewClient extends WebViewClient {

    private WebView mWebView;
    private String mHTML;

    TagFinderWebViewClient(WebView iWebView) {
        mWebView = iWebView;
        mWebView.addJavascriptInterface(this, "HTMLOUT");
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    public void onPageFinished(WebView iView, String iUrl) {
        super.onPageFinished(iView, iUrl);
        mWebView.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
    }

    @JavascriptInterface
    public void processHTML(String iHTML) {
        if (iHTML == null)
            return;

        mHTML = iHTML;
    }

    public String getHTML() {
        return mHTML;
    }
}
