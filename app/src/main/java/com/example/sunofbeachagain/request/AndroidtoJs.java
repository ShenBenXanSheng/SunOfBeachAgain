package com.example.sunofbeachagain.request;


import android.util.Log;
import android.webkit.JavascriptInterface;

public class AndroidtoJs extends Object{

    private final AndroidToJsCallBack mAndroidToJsCallBack;

    public AndroidtoJs(AndroidToJsCallBack androidToJsCallBack) {
        mAndroidToJsCallBack = androidToJsCallBack;
    }


    // 被JS调用的方法必须加入@JavascriptInterface注解
    @JavascriptInterface
    public void callSobNative(String msg) {

        if (mAndroidToJsCallBack != null) {
            mAndroidToJsCallBack.recMsg(msg);
        }
    }




    public interface AndroidToJsCallBack {
        void recMsg(String msg);
    }
}
