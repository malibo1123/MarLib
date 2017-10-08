package com.mar.lib.view.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.mar.lib.R;

import java.util.List;

/**
 * 可以在垂直方向滚动切换的Textview ---自定义view
 * switchDuaration -----切换时间；
 * idleDuaration -----停留时间；
 * alignment -----text的对齐方式；
 * switchOrientation -----垂直切换的方向，可以向上或者向下；
 */
public class VerticalSwitchTextView extends AppCompatTextView {
    private static final int DEFAULT_SWITCH_DURATION = 200;
    private static final int DEFAULT_IDLE_DURATION = 3000;
    public static final int TEXT_ALIGN_CENTER = 0;
    public static final int TEXT_ALIGN_LEFT = 1;
    public static final int TEXT_ALIGN_RIGHT = 2;
    private Context mContext;

    private List<String> lists;//会循环显示的文本内容
    private int contentSize;
    private String outStr;//当前滑出的文本内容
    private String inStr;//当前滑入的文本内容
    private float textBaseY;//文本显示的baseline
    private int currentIndex = 0;//当前显示到第几个文本

    private int switchDuaration = DEFAULT_SWITCH_DURATION;//切换时间
    private int idleDuaration = DEFAULT_IDLE_DURATION;//间隔时间
    private int switchOrientation = 0;
    private int alignment = TEXT_ALIGN_CENTER;
    private boolean sameDirection = true;//是否固定只朝一个方向滚动
    private float currentAnimatedValue = 0.0f;
    private ValueAnimator animator;

    private int verticalOffset = 0;
    private int mWidth;
    private int mHeight;
    private int paddingLeft = 0;
    private int paddingBottom = 0;
    private int paddingTop = 0;
    private int paddingRight = 0;

    private Paint mPaint;

    public VerticalSwitchTextView(Context context) {
        this(context, null);
    }

    public VerticalSwitchTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalSwitchTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VerticalSwitchTextView);
        try {
            switchDuaration = array.getInt(R.styleable.VerticalSwitchTextView_switchDuaration, DEFAULT_SWITCH_DURATION);
            idleDuaration = array.getInt(R.styleable.VerticalSwitchTextView_idleDuaration, DEFAULT_IDLE_DURATION);
            switchOrientation = array.getInt(R.styleable.VerticalSwitchTextView_switchOrientation, 0);
            alignment = array.getInt(R.styleable.VerticalSwitchTextView_alignment, TEXT_ALIGN_CENTER);
        } finally {
            array.recycle();
        }
        init();
    }

    private void init() {
        mPaint = getPaint();
        mPaint.setColor(getCurrentTextColor());

        animator = ValueAnimator.ofFloat(0f, 1f).setDuration(switchDuaration);
        animator.setStartDelay(idleDuaration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentAnimatedValue = (float) animation.getAnimatedValue();
                if (currentAnimatedValue < 1.0f) {
                    invalidate();
                }
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(contentSize==0)
                    return;
                currentIndex = (++currentIndex) % contentSize;
                outStr = lists.get(currentIndex);
                inStr = lists.get((currentIndex + 1) % contentSize);
                if(!sameDirection){
//                    if(switchOrientation==0)
//                        switchOrientation = 1;
//                    else if(switchOrientation == 1)
//                        switchOrientation = 0;
                    switchOrientation = (switchOrientation + 1) % 2 ;
                }
                animator.setStartDelay(idleDuaration);
                animator.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
    }

    /**
     * 设置循环显示的文本内容
     * @param content 内容list
     */
    public void setTextContent(List<String> content) {
        lists = content;
        if (lists == null || lists.size() == 0) {
            return;
        }
        contentSize = lists.size();
        outStr = lists.get(0);
        if (contentSize > 1) {
            inStr = lists.get(1);
        } else {
            inStr = lists.get(0);
        }

        if (contentSize > 0) {
            animator.start();
        }
    }

    /**设置是否只朝一个方向滚动，默认只朝一个方向滚动。
     * 如果只有两行数据可以设置此方法为false，这样就会
     * 每一次滚动方向与上一次方向相反
     * @param sameDirection  true----朝一个方向；false上下滚动
     */
    public void setSwitchSameDirection(boolean sameDirection){
        this.sameDirection = sameDirection;
    }

    /**
     * 主要用来调整TextView的高度
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);

        Rect bounds = new Rect();
        if (contentSize <= 0) {
            return;
        }
        String text = lists.get(0);
        mPaint.getTextBounds(text, 0, text.length(), bounds);
        int textHeight = bounds.height();

        paddingLeft = getPaddingLeft();
        paddingBottom = getPaddingBottom();
        paddingTop = getPaddingTop();
        paddingRight = getPaddingRight();
        mHeight = textHeight + paddingBottom + paddingTop;

        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        //计算文字高度
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        //计算文字的baseline
        textBaseY = mHeight - (mHeight - fontHeight) / 2 - fontMetrics.bottom;

        setMeasuredDimension(mWidth, mHeight);
    }

    /**
     * 文本中轴线X坐标
     */
    private float inTextStartX;
    private float outTextStartX;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (contentSize <= 0) {
            return;
        }
        //计算绘制的文字中心位置
        switch (alignment) {
            case TEXT_ALIGN_CENTER:
//                inTextStartX = (mWidth - paddingLeft - paddingRight - mPaint.measureText(inStr)) / 2 + paddingLeft;
//                outTextStartX = (mWidth - paddingLeft - paddingRight -mPaint.measureText(outStr)) / 2 + paddingLeft;
                inTextStartX = (mWidth + paddingLeft - paddingRight - mPaint.measureText(inStr)) / 2 ;
                outTextStartX = (mWidth + paddingLeft - paddingRight -mPaint.measureText(outStr)) / 2 ;
                break;
            case TEXT_ALIGN_LEFT:
                inTextStartX = paddingLeft;
                outTextStartX = paddingLeft;
                break;
            case TEXT_ALIGN_RIGHT:
                inTextStartX = mWidth - paddingRight - mPaint.measureText(inStr);
                outTextStartX = mWidth - paddingRight - mPaint.measureText(outStr);
                break;
        }
        //直接使用mHeight控制文本绘制，会因为text的baseline的问题不能居中显示
        verticalOffset = Math.round(2 * textBaseY * (0.5f - currentAnimatedValue));
        if (switchOrientation == 0) {//向上滚动切换
            if (verticalOffset > 0) {
                canvas.drawText(outStr, outTextStartX,verticalOffset, mPaint);
            } else {
                canvas.drawText(inStr, inTextStartX,2 * textBaseY + verticalOffset, mPaint);
            }
        } else {//向下滚动切换
            if (verticalOffset > 0) {
                canvas.drawText(outStr, outTextStartX,2 * textBaseY - verticalOffset, mPaint);
            } else {
                canvas.drawText(inStr, inTextStartX, -verticalOffset, mPaint);
            }
        }
    }

//    @Override
//    protected void onWindowVisibilityChanged(int visibility) {
//        super.onWindowVisibilityChanged(visibility);
//        if (visibility == VISIBLE) {
//            if(animator!=null) {
//                animator.cancel();
//                animator.start();
//            }
//        } else {
//            if(animator!=null)
//                animator.cancel();
//        }
//    }
}