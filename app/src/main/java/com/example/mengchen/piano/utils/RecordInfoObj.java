package com.example.mengchen.piano.utils;

import java.util.Arrays;

public class RecordInfoObj {

    public long time;
    public Integer[] tunes;

    @Override
    public String toString() {
        return "[time=" + this.time + ", tunes=" + Arrays.toString(this.tunes) + "]";
    }
}
