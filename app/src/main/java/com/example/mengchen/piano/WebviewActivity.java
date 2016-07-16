package com.example.mengchen.piano;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.mengchen.piano.utils.RecordPlayInterface;

public class WebviewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        final WebView webview = (WebView) this.findViewById(R.id.webview);
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowUniversalAccessFromFileURLs(true);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        final String recordJson = intent.getStringExtra("recordJson");
        final String recordTune = intent.getStringExtra("recordTune");


        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (recordJson != null) {
                    webview.loadUrl("javascript:getRecordInfo('" + recordJson + "', '" + recordTune + "')");
                }
                super.onPageFinished(view, url);
            }
        });

        webview.addJavascriptInterface(new RecordPlayInterface(this), "external");

        webview.loadUrl(url);
    }
}
