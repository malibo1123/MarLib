package com.mar.lib.util;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

/**
 * Created by malibo on 2017/10/8.
 */

public class SDKUtil {
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static void setBackgroundDrawable(Drawable drawable,View view){
        if(Build.VERSION.SDK_INT<16)
            view.setBackgroundDrawable(drawable);
        else
            view.setBackground(drawable);
    }
}
