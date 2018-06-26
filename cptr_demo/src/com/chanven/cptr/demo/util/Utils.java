package com.chanven.cptr.demo.util;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by Administrator on 2018/6/22.
 */

public class Utils {
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int getStatusBarHeight(Context context) {
        try {
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            int height = resources.getDimensionPixelSize(resourceId);
            return height;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

}
