package com.mar.lib.widget;

/**
 * Created by wm on 2014/8/15.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import com.mar.lib.view.R;

public class UISwitch extends View implements View.OnTouchListener, View.OnClickListener {

    private static final int TOUCH_MODE_IDLE = 0;
    private static final int TOUCH_MODE_DOWN = 1;
    private static final int TOUCH_MODE_DRAGGING = 2;

    private float mTouchX;
    private float mTouchY;

    private int mTouchMode;
    private int mTouchSlop;

    private boolean NowChoose = false;

    private boolean OnSlip = false;

    private float NowX;

    private Rect Btn_On, Btn_Off;

    private OnChangedListener mChgLsn;

    private Bitmap bg_on, bg_off, slip_btn;

    private int height;
    private int width;

    private boolean isLowerDPI = false;

    private Context mContext;

    public UISwitch(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public UISwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public UISwitch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    private Bitmap getScaleBitmap(Bitmap on) {
        int newWidth, newHeight;
        int width = on.getWidth();
        int height = on.getHeight();
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.heightPixels;
        if (dm.density > 1.5 && dm.density < 2.5) {
            newWidth = on.getWidth() / (8 / 5);
            newHeight = on.getHeight() / (8 / 5);
        } else if (dm.density >= 2.5) {
            newWidth = on.getWidth() / (8 / 5);
            newHeight = on.getHeight() / (8 / 5);
        } else {
            newWidth = on.getWidth() / 2;
            newHeight = on.getHeight() / 2;
            isLowerDPI = true;
        }


        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(on, 0, 0, width, height, matrix, true);
    }

    private void init() {
        bg_on = BitmapFactory.decodeResource(getResources(),
                R.drawable.image_switch_on);
        bg_off = BitmapFactory.decodeResource(getResources(),
                R.drawable.image_switch_off);
        slip_btn = BitmapFactory.decodeResource(getResources(),
                R.drawable.image_switch_thumb);

        Btn_On = new Rect(0, 0, slip_btn.getWidth(), slip_btn.getHeight());
        Btn_Off = new Rect(bg_off.getWidth() - slip_btn.getWidth(), 0,
                bg_off.getWidth(), slip_btn.getHeight());

        ViewConfiguration config = ViewConfiguration.get(getContext());
        mTouchSlop = config.getScaledTouchSlop();

        setOnTouchListener(this);
        setOnClickListener(this);

        setLayoutSize();
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                switch (mTouchMode) {
                    case TOUCH_MODE_IDLE:
                        break;
                    case TOUCH_MODE_DOWN:
                        final float x = event.getX();
                        final float y = event.getY();
                        if (Math.abs(x - mTouchX) > mTouchSlop
                                || Math.abs(y - mTouchY) > mTouchSlop) {
                            mTouchMode = TOUCH_MODE_DRAGGING;
                            getParent().requestDisallowInterceptTouchEvent(true);
                            return true; // 不需要让父类处理了
                        }
                        break;
                    case TOUCH_MODE_DRAGGING:
                        OnSlip = true;
                        NowX = event.getX();
                        invalidate();
                        return true; // 不需要让父类处理了
                    default:
                        break;
                }
                break;
            case MotionEvent.ACTION_DOWN:
                final float x = event.getX();
                final float y = event.getY();
                if (isEnabled() && hitView(x, y)) {
                    mTouchMode = TOUCH_MODE_DOWN;
                    mTouchX = x;
                    mTouchY = y;
                }
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mTouchMode == TOUCH_MODE_DRAGGING) {
                    boolean LastChoose = NowChoose;
                    if (event.getX() >= (bg_on.getWidth() / 2)) {
                        NowX = bg_on.getWidth() - slip_btn.getWidth() / 2;
                        NowChoose = true;
                    } else {
                        NowX = NowX - slip_btn.getWidth() / 2;
                        NowChoose = false;
                    }
                    invalidate();
                    if (mChgLsn != null && (LastChoose != NowChoose))
                        mChgLsn.onChanged(NowChoose);
                    return true; // 不需要让父类处理了
                }
                mTouchMode = TOUCH_MODE_IDLE;
                OnSlip = false;
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {// 绘图函数
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        Matrix matrix = new Matrix();
        Paint paint = new Paint();
        float x;
        {
            if (OnSlip) {
                if (NowX < (bg_on.getWidth() / 2))// 滑动到前半段与后半段的背景不同,在此做判断
                    canvas.drawBitmap(bg_off, matrix, paint);// 画出关闭时的背景
                else
                    canvas.drawBitmap(bg_on, matrix, paint);// 画出打开时的背景
            } else {
                if (NowChoose)
                    canvas.drawBitmap(bg_on, matrix, paint);// 画出打开时的背景
                else
                    canvas.drawBitmap(bg_off, matrix, paint);// 画出关闭时的背景
            }
            if (OnSlip) {// 是否是在滑动状态,

                if (NowX >= bg_on.getWidth())// 是否划出指定范围,不能让游标跑到外头,必须做这个判断
                    x = bg_on.getWidth() - slip_btn.getWidth() / 2;// 减去游标1/2的长度...
                else
                    x = NowX - slip_btn.getWidth() / 2;
            } else {// 非滑动状态
                if (NowChoose)// 根据现在的开关状态设置画游标的位置
                    x = Btn_Off.left;
                else
                    x = Btn_On.left;
            }
            if (x < 0)// 对游标位置进行异常判断...
                x = 0;
            else if (x > bg_on.getWidth() - slip_btn.getWidth())
                x = bg_on.getWidth() - slip_btn.getWidth();
            canvas.drawBitmap(slip_btn, x, 0, paint);// 画出游标.
        }
    }

    /**
     * 设置开关切换事件
     */
    public void setOnChangedListener(OnChangedListener l) {
        mChgLsn = l;
    }

    public interface OnChangedListener {
        abstract void onChanged(boolean checkState);
    }

    /**
     * 引发UI上的变化，触发事件
     */
    public void setChecked(boolean isChecked) {
        NowChoose = isChecked;
        invalidate();
        if (mChgLsn != null)
            mChgLsn.onChanged(NowChoose);
    }

    /**
     * 仅仅引发UI上的变化，不会触发事件
     */
    public void setSelected(boolean isSelected) {
        NowChoose = isSelected;
        invalidate();
    }

    public boolean isChecked() {
        return NowChoose;
    }

    @Override
    public void onClick(View v) {
        setChecked(!NowChoose);
    }

    private boolean hitView(float x, float y) {
        if (x > bg_on.getWidth() || y > bg_on.getHeight()) {
            return false;
        }
        return true;
    }

    /**
     * 由switch_off这张图片来决定view的大小
     */
    private void setLayoutSize() {
        Bitmap bm = BitmapFactory.decodeResource(getResources(),
                R.drawable.image_switch_off);
        if (isLowerDPI) {
            height = bm.getHeight() / 2;
            width = bm.getWidth() / 2;
        } else {
            height = bm.getHeight() / (6 / 5);
            width = bm.getWidth() / (6 / 5);
        }
        bm.recycle();
    }

}