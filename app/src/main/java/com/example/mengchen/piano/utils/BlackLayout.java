package com.example.mengchen.piano.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class BlackLayout extends ViewGroup {
    public BlackLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
        int screenWidth = this.getWidth();
        double whiteKeyWidth = screenWidth / 20.0;
        double childWidth = whiteKeyWidth / 3 * 2;
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = this.getChildAt(i);

            if (i == 1 || i == 5 || i == 8 || i == 12 || i == 15 || i == 19) {
                child.setVisibility(INVISIBLE);
            }

            int l = (int)(whiteKeyWidth * (i) - childWidth / 2);
            int r = (int)(l + childWidth);

            child.layout(l, 0, r, 350);
        }
    }

}