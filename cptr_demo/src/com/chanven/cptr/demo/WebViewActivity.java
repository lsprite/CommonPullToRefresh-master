package com.chanven.cptr.demo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.chanven.cptr.demo.util.AndroidJavaScript;

/**
 * Created by Administrator on 2018/6/19.
 */

public class WebViewActivity extends AppCompatActivity {
    WebView webView;
    ProgressBar loadingbar;
    boolean isOnPause = false;
    //
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);
        //
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        collapsingToolbarLayout.setTitle("DesignLibrarySample");
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        //
        initView();
        setWebView();
        webView.loadUrl("https://www.baidu.com");
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        try {
            if (isOnPause) {
                if (webView != null) {
                    webView.getClass().getMethod("onResume").invoke(webView, (Object[]) null);
                }
                isOnPause = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        try {
            if (webView != null) {
                webView.getClass().getMethod("onPause").invoke(webView, (Object[]) null);
                isOnPause = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.setVisibility(View.GONE);
        webView.removeAllViews();
        webView.destroy();
    }

    private void initView() {
        // TODO Auto-generated method stub
        webView = (WebView) findViewById(R.id.web_webview);
        loadingbar = (ProgressBar) findViewById(R.id.loadingbar);
        loadingbar.setVisibility(View.VISIBLE);
        loadingbar.setMax(100);
        //
    }

    private AndroidJavaScript androidJavaScript;

    private void setWebView() {
        // TODO Auto-generated method stub
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // 设置WebView属�?�，能够执行Javascript脚本
        WebSettings settings = webView.getSettings();
        // 支持缩放
        settings.setSupportZoom(false);
        // 启用内置缩放装置
        settings.setBuiltInZoomControls(false);
        // 启用JS脚本
        settings.setJavaScriptEnabled(true);
        // 支持Html5 Video播放
        webView.getSettings().setJavaScriptEnabled(true);
        androidJavaScript = new AndroidJavaScript(this, webView);
        webView.addJavascriptInterface(androidJavaScript, "qtzhy");
        settings.setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        // 不使用缓存：
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        // webView.getSettings().setAppCacheEnabled(true); // 开启缓存功能
        // webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // // 缓存模式
        // webView.getSettings().setDomStorageEnabled(true);
        webView.setWebChromeClient(m_chromeClient);
        webView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                        long contentLength) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                overridePendingTransition(0, 0);
                // DownloadManager downloadManager = (DownloadManager)
                // getSystemService(DOWNLOAD_SERVICE);
                // DownloadManager.Request request = new
                // DownloadManager.Request(
                // Uri.parse(url));
                // request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                // request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                // | DownloadManager.Request.NETWORK_WIFI);
                // request.setVisibleInDownloadsUi(true);
                // long downloadId = downloadManager.enqueue(request);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                loadingbar.setVisibility(View.VISIBLE);
                loadingbar.setProgress(0);
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } else {
                    view.loadUrl(url); // 加载新的url
                }
                return true; // 返回true,代表事件已处�?,事件流到此终�?
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loadingbar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                // TODO Auto-generated method stub
                super.doUpdateVisitedHistory(view, url, isReload);
                try {
                    if (androidJavaScript.isNeedClearHistory()) {
                        androidJavaScript.setNeedClearHistory(false);
                        webView.clearHistory();// 清除历史记录
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });
    }

    private WebChromeClient m_chromeClient = new WebChromeClient() {

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            loadingbar.setProgress(newProgress);
            if (newProgress == 100) {
                loadingbar.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }

        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            try {
//                final MyAlertDialog dialog = new MyAlertDialog(WebActivity.this);
//                dialog.setTitle("提示");
//                dialog.setMessage(message);
//                dialog.setOnlyOneButton("确定", new OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        // TODO Auto-generated method stub
//                        dialog.dismiss();
//                    }
//                });
//                WindowManager m = getWindowManager();
//                WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
//                Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
//                Window dialogWindow = dialog.getWindow();
//                p.width = (int) (d.getWidth() * 0.80); // 宽度设置为屏幕的0.8
//                dialogWindow.setAttributes(p);
//                dialog.show();
            } catch (Exception e) {
                // TODO: handle exception
                Log.e("", e.getMessage());
            }
            result.confirm();// 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
            return true;
            // return super.onJsAlert(view, url, message, result);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            try {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                    overridePendingTransition(0, 0);
                }
            } catch (Exception e) {
                // TODO: handle exception
                finish();
                overridePendingTransition(0, 0);
            }
            return true;
        }
        return false;
    }
}
