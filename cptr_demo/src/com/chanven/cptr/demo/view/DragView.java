package com.chanven.cptr.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

public class DragView extends android.support.v7.widget.AppCompatTextView {
    float moveX;
    float moveY;

    public DragView(Context context) {
        super(context);
    }

    public DragView(Context context, AttributeSet attrs) {
        super(context, attrs);
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
                int offsetX = (int) (event.getX() - moveX);
                int offsetY = (int) (event.getY() - moveY);
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
                layoutParams.leftMargin = getLeft() + offsetX;
                layoutParams.topMargin = getTop() + offsetY;
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