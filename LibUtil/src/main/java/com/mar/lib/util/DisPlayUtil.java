package com.mar.lib.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * Created by neavo on 14-3-15.
 */

public class DisPlayUtil {

    public static final float getDensity(Context ctx) {
        return ctx.getResources().getDisplayMetrics().density;
    }

    public static final float getScaledDensity(Context ctx) {
        return ctx.getResources().getDisplayMetrics().scaledDensity;
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int getScreenRealWidth(Activity activity) {
        int widthPixels;
        WindowManager w = activity.getWindowManager();
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        // since SDK_INT = 1;
        widthPixels = metrics.widthPixels;
        if (android.os.Build.VERSION.SDK_INT >= 14 && android.os.Build.VERSION.SDK_INT < 17) {
            try {
                widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
            } catch (Exception ignored) {

            }
        } else if (android.os.Build.VERSION.SDK_INT >= 17) {
            try {
                DisplayMetrics realMetrics = new DisplayMetrics();
                d.getRealMetrics(realMetrics);
                widthPixels = realMetrics.widthPixels;
            } catch (Exception ignored) {

            }
        } else {
            try {
                widthPixels = (Integer) Display.class.getMethod("getRealWidth").invoke(d);
            } catch (Exception ignored) {

            }
        }
        return widthPixels;
    }

    public static int getNavigationBarHeight(Activity mActivity) {
        int vh = 0;
        WindowManager windowManager = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - windowManager.getDefaultDisplay().getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vh;
    }


    /**
     * 得到屏幕真实高度包含NavigationBar
     *
     * @param activity
     * @return
     */
    public static int getScreenRealHeight(Activity activity) {
        int heightPixels;
        WindowManager w = activity.getWindowManager();
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        // since SDK_INT = 1;
        heightPixels = metrics.heightPixels;
        // includes window decorations (statusbar bar/navigation bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
            try {
                heightPixels = (Integer) Display.class
                        .getMethod("getRawHeight").invoke(d);
            } catch (Exception ignored) {
            }
            // includes window decorations (statusbar bar/navigation bar)
        else if (Build.VERSION.SDK_INT >= 17)
            try {
                android.graphics.Point realSize = new android.graphics.Point();
                Display.class.getMethod("getRealSize",
                        android.graphics.Point.class).invoke(d, realSize);
                heightPixels = realSize.y;
            } catch (Exception ignored) {
            }
        return heightPixels;
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕的宽度,比较长和宽，取较短的
     *
     * @param context
     * @return
     */
    public static int getPortraitScreenWidth(Context context) {
        int width = getScreenWidth(context);
        int height = getScreenHeight(context);
        return width < height ? width : height;
    }

    /**
     * 获取屏幕的宽度,比较长和宽，取较长的
     *
     * @param context
     * @return
     */
    public static int getLandScreenWidth(Context context) {
        int width = getScreenWidth(context);
        int height = getScreenHeight(context);
        return width < height ? height : width;
    }


    /**
     * 获取屏幕高度，不包括虚拟条
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 判断屏幕是否可以旋转
     *
     * @param activity
     * @return
     */
    public static boolean enableScreenRotation(Activity activity) {
        int value = Settings.System.getInt(activity.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 1);
        return value == 1;
    }

    /**
     * 获取系统设置屏幕亮度
     */
    public static float getScreenBrightness(Context context) {
        int value = 0;
        ContentResolver cr = context.getContentResolver();
        try {
            value = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        } catch (java.lang.SecurityException e) {
            e.printStackTrace();
        }
        return value / 255f;
    }

    public static boolean showTip = false; //修改亮度失败后的提示，只显示一次提示。

    public static void setScreenBrightness(Activity activity, float brightness) {
        saveScreenBrightness(activity, (int) (brightness * 255));
    }

    /**
     * 设置当前屏幕亮度值 0--255
     */
    private static void saveScreenBrightness(Activity activity, int paramInt){
        Window localWindow = activity.getWindow();
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        float f = paramInt / 255.0F;
        localLayoutParams.screenBrightness = f;
        localWindow.setAttributes(localLayoutParams);
    }

    /**
     * 设置当前activity屏幕亮度为配置值
     */
    public static void setActivityBrightByConfig(Activity activity) {
        //获取配置文件亮度
        float configBright =  configBright = activity.getWindow().getAttributes().screenBrightness;

        //设置为系统亮度
        if (configBright < 0) {
            configBright = DisPlayUtil.getScreenBrightness(activity);
        }

        DisPlayUtil.setActivityScreenBright(activity, configBright);
    }


    /**
     * 设置当前activity屏幕亮度
     */
    public static void setActivityScreenBright(Activity activity, float brightness) {
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.screenBrightness = brightness;

        if (params.screenBrightness > 1.0f) {
            params.screenBrightness = 1.0f;
        } else if (params.screenBrightness < 0.01f) {
            params.screenBrightness = 0.01f;
        }
        activity.getWindow().setAttributes(params);
    }

    /**
     * 获取当前activity屏幕亮度
     *
     * @return
     */
    public static float getActivityScreenBright(Activity activity) {
        float brightness = activity.getWindow().getAttributes().screenBrightness;
        if (brightness <= 0.01f)
            brightness = DisPlayUtil.getScreenBrightness(activity);
        return brightness;
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /***
     * 图片的缩放方法
     *
     * @param bgimage   ：源图片资源
     * @param newWidth  ：缩放后宽度
     * @param newHeight ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    public static Bitmap zoomImage(Bitmap bitmap, float scale) {
        if (scale == 1.0f) {
            return bitmap;
        }
        // 获取这个图片的宽和高
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(scale, scale);
        Bitmap new_bitmap = Bitmap.createBitmap(bitmap, 0, 0, (int) width,
                (int) height, matrix, true);
        return new_bitmap;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        return ((BitmapDrawable) drawable).getBitmap();
    }
}
