package com.chanven.cptr.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

public class MyWebView extends WebView {
    private OnScrollListener mOnScrollListener;

    private int lastScrollY;

    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:

                lastScrollY = this.getScrollY();
                break;
            case MotionEvent.ACTION_MOVE:

                if (Math.abs(lastScrollY - this.getScrollY()) > 10) {
//                    if (lastScrollY > this.getScrollY())//向上拉
//                    {
//                    } else//向下拉
//                    {
//                    }
                    if (mOnScrollListener != null) {
                        mOnScrollListener.between(this.getScrollY() - lastScrollY);
                    }

                    lastScrollY = this.getScrollY();
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return super.onTouchEvent(ev);
    }

    public OnScrollListener getmOnScrollListener() {
        return mOnScrollListener;
    }

    public void setmOnScrollListener(OnScrollListener mOnScrollListener) {
        this.mOnScrollListener = mOnScrollListener;
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        // webview的高度
        float webcontent = getContentHeight() * getScale();
        // 当前webview的高度
        float webnow = getHeight() + getScrollY();
        if (Math.abs(webcontent - webnow) < 1) {
            //处于底端
            if (mOnScrollListener != null) {
                mOnScrollListener.onPageEnd(l, t, oldl, oldt);
            }
        } else if (getScrollY() == 0) {
            //处于顶端
            if (mOnScrollListener != null) {
                mOnScrollListener.onPageTop(l, t, oldl, oldt);
            }
        }
        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(l - oldl, t - oldt);
        }
        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollChanged(l, t, oldl, oldt);
        }

    }

    ;

    public interface OnScrollListener {


        public void onPageEnd(int l, int t, int oldl, int oldt);

        public void onPageTop(int l, int t, int oldl, int oldt);

        public void onScroll(int dx, int dy);

        public void between(int dy);

        public void onScrollChanged(int l, int t, int oldl, int oldt);
    }

}
