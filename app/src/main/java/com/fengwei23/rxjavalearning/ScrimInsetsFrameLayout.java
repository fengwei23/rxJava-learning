package com.fengwei23.rxjavalearning;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.FrameLayout;

/**
 * author: w.feng
 * time  : 2018/07/24
 * desc  :
 */
public class ScrimInsetsFrameLayout extends FrameLayout {

    private Drawable mInsetForeground;

    private Rect mInsets;

    private Rect mTempRect = new Rect();

    private OnInsetsCallback mOnInsetsCallback;

    public ScrimInsetsFrameLayout(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public ScrimInsetsFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ScrimInsetsFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScrimInsetsFrameLayout, defStyleAttr, 0);
        if (a == null) {
            return;
        }
        mInsetForeground = a.getDrawable(R.styleable.ScrimInsetsFrameLayout_insetForeground);
        a.recycle();
        setWillNotDraw(true);

    }


    @Override
    public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
        return super.dispatchApplyWindowInsets(insets);
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        mInsets = new Rect(insets);
        setWillNotDraw(mInsetForeground == null);
        ViewCompat.postInvalidateOnAnimation(this);
        if (mOnInsetsCallback != null) {
            mOnInsetsCallback.onInsetsChanged(insets);
        }
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        int width = getWidth();
        int height = getHeight();
        if (mInsets != null && mInsetForeground != null) {
            int sc = canvas.save();
            canvas.translate(getScrollX(), getScrollY());

            mTempRect.set(0, 0, width, mInsets.top);
            mInsetForeground.setBounds(mTempRect);
            mInsetForeground.draw(canvas);


            mTempRect.set(0, height - mInsets.bottom, width, height);
            mInsetForeground.setBounds(mTempRect);
            mInsetForeground.draw(canvas);

            mTempRect.set(0, mInsets.top, mInsets.left, height - mInsets.bottom);
            mInsetForeground.setBounds(mTempRect);
            mInsetForeground.draw(canvas);

            mTempRect.set(width - mInsets.right, mInsets.top, width, height - mInsets.bottom);
            mInsetForeground.setBounds(mTempRect);
            mInsetForeground.draw(canvas);

            canvas.restoreToCount(sc);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mInsetForeground != null) {
            mInsetForeground.setCallback(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mInsetForeground != null) {
            mInsetForeground.setCallback(null);
        }
    }

    public void setOnInsetsCallback(OnInsetsCallback onInsetsCallback) {
        mOnInsetsCallback = onInsetsCallback;
    }

    public interface OnInsetsCallback {

        void onInsetsChanged(Rect insets);
    }
}
