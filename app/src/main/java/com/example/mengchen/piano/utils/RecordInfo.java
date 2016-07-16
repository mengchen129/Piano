package com.example.mengchen.piano.utils;

import android.view.View;

public class RecordInfo {
    private int tunes;
    private View button;

    public RecordInfo(int tunes, View button) {
        this.tunes = tunes;
        this.button = button;
    }

    public RecordInfo() {}

    public int getTunes() {
        return tunes;
    }

    public View getButton() {
        return button;
    }

    public String toString() {
        return "{tunes=" + this.tunes + "}";
    }
}
