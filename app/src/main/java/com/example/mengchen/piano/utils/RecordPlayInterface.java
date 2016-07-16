package com.example.mengchen.piano.utils;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;

public class RecordPlayInterface {

    Activity context;

    public RecordPlayInterface(Activity ctx) {
        context = ctx;
    }

    @JavascriptInterface
    public void play(String recordInfo, String tune) {
        Intent intent = new Intent();
        intent.putExtra("recordJson", recordInfo);
        intent.putExtra("recordTune", tune);
        context.setResult(1, intent);
        context.finish();
    }
}
