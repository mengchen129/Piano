package com.example.mengchen.piano;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;

public class RecordPlayInterface {

    Activity context;

    public RecordPlayInterface(Activity ctx) {
        context = ctx;
    }

    @JavascriptInterface
    public void play(String recordInfo) {
        Intent intent = new Intent();
        intent.putExtra("recordJson", recordInfo);
        context.setResult(1, intent);
        context.finish();
    }
}
