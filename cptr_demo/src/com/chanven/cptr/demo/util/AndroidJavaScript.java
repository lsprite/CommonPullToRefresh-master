package com.chanven.cptr.demo.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class AndroidJavaScript {
    private Activity activity;
    private WebView webView;
    private boolean needClearHistory = false;

    public boolean isNeedClearHistory() {
        return needClearHistory;
    }

    public void setNeedClearHistory(boolean needClearHistory) {
        this.needClearHistory = needClearHistory;
    }

    public AndroidJavaScript(Activity activity, WebView webView) {
        // TODO Auto-generated constructor stub
        this.activity = activity;
        this.webView = webView;
    }

    @JavascriptInterface
    public void Totel(String phone) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + phone));
        activity.startActivity(intent);
    }

}
