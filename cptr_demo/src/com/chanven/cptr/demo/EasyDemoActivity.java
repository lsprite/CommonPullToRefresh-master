package com.chanven.cptr.demo;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.chanven.cptr.demo.view.ObservableScrollView;


public class EasyDemoActivity extends Activity {

    private ObservableScrollView scrollView;


    private TextView textView;

    private int fadingHeight = 600; // 当ScrollView滑动到什么位置时渐变消失（根据需要进行调整）
    private Drawable drawable; // 顶部渐变布局需设置的Drawable
    //    private static final int START_ALPHA = 0;//scrollview滑动开始位置
//    private static final int END_ALPHA = 255;//scrollview滑动结束位置
    private static final int START_ALPHA = 100;//scrollview滑动开始位置
    private static final int END_ALPHA = 255;//scrollview滑动结束位置
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.easydemo_layout);
        imageView = (ImageView) findViewById(R.id.imageview);
        scrollView = (ObservableScrollView) findViewById(R.id.scrollview);
        textView = (TextView) findViewById(R.id.textview);
        drawable = getResources().getDrawable(R.color.colorAccent);
        drawable.setAlpha(START_ALPHA);
        textView.setBackgroundDrawable(drawable);
        initListeners();
    }

    private void initListeners() {
        ViewTreeObserver vto = imageView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imageView.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                fadingHeight = imageView.getHeight();

                scrollView.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
                    @Override
                    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                        // TODO Auto-generated method stub
                        if (y > fadingHeight) {
                            y = fadingHeight; // 当滑动到指定位置之后设置颜色为纯色，之前的话要渐变---实现下面的公式即可
                        } else if (y < 0) {
                            y = 0;
                        } else {
                        }
                        drawable.setAlpha(y * (END_ALPHA - START_ALPHA) / fadingHeight
                                + START_ALPHA);
                    }
                });
            }
        });


    }

}
