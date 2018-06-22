package com.chanven.cptr.demo.util;

import android.content.Context;

/**
 * Created by Administrator on 2018/6/22.
 */

public class Utils {
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
