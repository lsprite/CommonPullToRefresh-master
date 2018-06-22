package com.chanven.cptr.demo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.chanven.cptr.demo.R;
import com.chanven.cptr.demo.util.Utils;

public class AnimTitleView extends View {
    //背景的原始颜色和目标颜色
    private static final int bgFromColor = Color.parseColor("#CC323744");
    private static final int bgToColor = Color.parseColor("#FF2296F3");
    //阈值，在这个值内渐变
    private static final int Threshold = 300;
    // 控件的宽高
    private int width;
    private int height;

    private Paint paint;
    //背景的原始位置、目标位置、当前位置
    private RectF bgFromRect = new RectF();
    private RectF bgToRect = new RectF();
    private RectF bgRect = new RectF();
    //图标的位置
    private RectF iconRect = new RectF();
    //文字的原始位置、目标位置、当前位置
    private RectF textFromRect = new RectF();
    private RectF textToRect = new RectF();
    private RectF textRect = new RectF();
    //图标与是否显示图标
    private Bitmap icon;
    private boolean showIcon;
    //标题与字体大小、颜色
    private String title;
    private float textSize;
    private int textColor;
    //背景颜色和圆角
    private int bgColor;
    private int bgRound;
    //外层ScrollView的scrollY
    private int scrollY;
    //左上边距、内补白、图标与文字的边距
    private int leftMargin;
    private int topMargin;
    private int leftRightPadding;
    private int topBottomPadding;
    private int drawablePadding;

    public AnimTitleView(Context context) {
        super(context);
        init();
    }

    public AnimTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnimTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // 抗锯齿
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_notifications);
        title = "";
        textSize = Utils.dip2px(getContext(), 16);
        bgRound = Utils.dip2px(getContext(), 45);
        bgColor = bgFromColor;
        textColor = Color.parseColor("#FFFFFF");

        leftMargin = Utils.dip2px(getContext(), 10);
        topMargin = Utils.dip2px(getContext(), 10);
        leftRightPadding = Utils.dip2px(getContext(), 13);
        topBottomPadding = Utils.dip2px(getContext(), 2);
        drawablePadding = Utils.dip2px(getContext(), 8);
    }

    public void setTitle(String title) {
        this.title = title;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        if (width != 0 && height != 0) {
            paint.setTextSize(textSize);
            //格式化title，防止文字过长的异常情况，过长则截断加...
            formatTitle(title, 1);
            //得到文本的绘制宽度和高度
            float textWidth = paint.measureText(title);
            Paint.FontMetrics metrics = paint.getFontMetrics();
            float textHeight = metrics.bottom - metrics.top;
            //图标只有显示与不显示，位置信息只有一个
            iconRect.set(leftMargin + leftRightPadding,
                    (height - icon.getHeight()) / 2,
                    leftMargin + leftRightPadding + icon.getWidth(),
                    (height - icon.getHeight()) / 2 + icon.getHeight());
            textFromRect.set(leftMargin + leftRightPadding + icon.getWidth() + drawablePadding,
                    (height - textHeight) / 2,
                    leftMargin + leftRightPadding + icon.getWidth() + drawablePadding + textWidth,
                    (height - textHeight) / 2 + textHeight);
            bgFromRect.set(leftMargin,
                    Math.min(iconRect.top, textFromRect.top) - topBottomPadding,
                    leftMargin + leftRightPadding + icon.getWidth() + drawablePadding + textWidth + leftRightPadding,
                    Math.max(iconRect.bottom, textFromRect.bottom) + topBottomPadding);
            textToRect.set((width - textWidth) / 2,
                    (height - textHeight) / 2,
                    (width - textWidth) / 2 + textWidth,
                    (height - textHeight) / 2 + textHeight);
            bgToRect.set(-bgRound, 0, width + bgRound, height);
            //在测量后，需要立马调用一次，否则第一次进来不会draw
            setScrollY(scrollY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景
        paint.setColor(bgColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(bgRect, bgRound, bgRound, paint);
        //绘制icon
        if (showIcon) {
            canvas.drawBitmap(icon, null, iconRect, paint);
        }
        //绘制文字
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        Paint.FontMetrics metrics = paint.getFontMetrics();
        float drawX = (textRect.width() - paint.measureText(title)) / 2 + textRect.left;
        float drawY = textRect.height() / 2 + (metrics.bottom - metrics.top) / 2 - metrics.bottom + textRect.top;
        canvas.drawText(title, drawX, drawY, paint);
    }

    /**
     * 在外界的ScrollView的回调中调用
     */
    @Override
    public void setScrollY(int scrollY) {
        //超出阈值，则直接采用阈值
        if (scrollY > Threshold) {
            scrollY = Threshold;
        }
        this.scrollY = scrollY;
        //得到比例
        float scale = scrollY * 1.0f / Threshold;
        bgRect.set(bgFromRect.left - (bgFromRect.left - bgToRect.left) * scale,
                bgFromRect.top - (bgFromRect.top - bgToRect.top) * scale,
                bgFromRect.right + (bgToRect.right - bgFromRect.right) * scale,
                bgFromRect.bottom + (bgToRect.bottom - bgFromRect.bottom) * scale);
        bgColor = evaluateColor(bgFromColor, bgToColor, scale);
        showIcon = (scrollY == 0);
        textRect.set(textFromRect.left - (textFromRect.left - textToRect.left) * scale,
                textFromRect.top,
                textFromRect.right + (textToRect.right - textFromRect.right) * scale,
                textFromRect.bottom);
        invalidate();
    }

    /**
     * 文字过长，每次截断一个字符，然后加上...继续测量，递归调用直到满足要求
     */
    private void formatTitle(String text, int count) {
        float textWidth = paint.measureText(text);
        if (textWidth >= width - leftMargin - leftRightPadding * 2 - icon.getWidth() - drawablePadding) {
            String temp = title.substring(0, title.length() - count) + "...";
            formatTitle(temp, count + 1);
        } else {
            title = text;
        }
    }

    /**
     * 颜色渐变，需要把ARGB分别拆开进行渐变
     */
    private int evaluateColor(int startValue, int endValue, float fraction) {
        if (fraction <= 0) {
            return startValue;
        }
        if (fraction >= 1) {
            return endValue;
        }
        int startInt = startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return ((startA + (int) (fraction * (endA - startA))) << 24)
                | ((startR + (int) (fraction * (endR - startR))) << 16)
                | ((startG + (int) (fraction * (endG - startG))) << 8)
                | ((startB + (int) (fraction * (endB - startB))));
    }
}