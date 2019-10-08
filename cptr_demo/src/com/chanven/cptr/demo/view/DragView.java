package com.chanven.cptr.demo.view;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;

public class DragView extends android.support.v7.widget.AppCompatTextView {
    float moveX;
    float moveY;

    public DragView(Context context) {
        super(context);
        init(context);
    }

    public DragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    int widthPixels = 0;
    int heightPixels = 0;

    public void init(Context context) {
//        DisplayMetrics display = context.getResources().getDisplayMetrics();
//        widthPixels = display.widthPixels;
//        heightPixels = display.heightPixels;
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics dm = new DisplayMetrics();
//        wm.getDefaultDisplay().getMetrics(dm);
//        widthPixels = dm.widthPixels;
//        heightPixels = dm.heightPixels - getStatusBarHeight(context);
        //适配过华为全面屏切换模式
        WindowManager windowManager =
                (WindowManager) context.getSystemService(Context.
                        WINDOW_SERVICE);
        final Display display = windowManager.getDefaultDisplay();
        Point outPoint = new Point();
        if (Build.VERSION.SDK_INT >= 19) {
            // 可能有虚拟按键的情况
            display.getRealSize(outPoint);
        } else {
            // 不可能有虚拟按键
            display.getSize(outPoint);
        }
        widthPixels = outPoint.x;
        heightPixels = outPoint.y - getStatusBarHeight(context);
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                moveX = event.getX();
                moveY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //setTranslationX View的位置变了，但是getLeft(),getTop()等参数不会有变化
//                setTranslationX(getX() + (event.getX() - moveX));
//                setTranslationY(getY() + (event.getY() - moveY));
                //
                int offsetX = (int) (event.getX() - moveX);
                int offsetY = (int) (event.getY() - moveY);
                Log.e("nemo", "---onTouchEvent " + getLeft() + "," + getTop());
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
                //判断边界
                int leftMargin = getLeft() + offsetX;
                int topMargin = getTop() + offsetY;
                if (leftMargin < 0) {
                    leftMargin = 0;
                }
                if (topMargin < 0) {
                    topMargin = 0;
                }
                if (leftMargin > widthPixels - getWidth()) {
                    leftMargin = widthPixels - getWidth();
                }
                if (topMargin > heightPixels - getHeight()) {
                    topMargin = heightPixels - getHeight();
                }
                layoutParams.leftMargin = leftMargin;
                layoutParams.topMargin = topMargin;
                setLayoutParams(layoutParams);
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return true;
    }

}