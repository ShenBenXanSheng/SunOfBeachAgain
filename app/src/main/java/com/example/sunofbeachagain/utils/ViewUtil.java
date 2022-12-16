package com.example.sunofbeachagain.utils;

import android.view.View;

public class ViewUtil {
    public static int getViewWidth(View view) {
        int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthSpec, heightSpec);
        int measuredWidth = view.getMeasuredWidth();//测量得到的textview的宽
        return measuredWidth;
    }

}

