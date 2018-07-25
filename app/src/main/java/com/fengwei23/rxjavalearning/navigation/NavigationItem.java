package com.fengwei23.rxjavalearning.navigation;

import android.graphics.drawable.Drawable;

/**
 * author: w.feng
 * time  : 2018/07/24
 * desc  :
 */
public class NavigationItem {
    private String mText;
    private Drawable mDrawable;

    public NavigationItem(String text, Drawable drawable){
        mText = text;
        mDrawable = drawable;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public void setDrawable(Drawable drawable) {
        mDrawable = drawable;
    }

}
