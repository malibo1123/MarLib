package com.mar.lib.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.mar.lib.log.slf4j.Logger;
import com.mar.lib.log.slf4j.LoggerFactory;

public class VerticalSwitchLayout extends FrameLayout implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
    public Logger logger = LoggerFactory.getLogger(VerticalSwitchLayout.class.getSimpleName());
    private static final int DEFAULT_SWITCH_DURATION = 500;//默认切换时间
    private static final int DEFAULT_IDLE_DURATION = 3000;//默认停留时间

    private int switchDuaration;//切换时间
    private int idleDuaration;//间隔时间
    private boolean sameDirection = true;//是否固定只朝一个方向滚动
//    private int mHeight;

    public VerticalSwitchLayout(@NonNull Context context) {
        super(context);
        init(null);

    }

    public VerticalSwitchLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public VerticalSwitchLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VerticalSwitchLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private int childCounts;
    private void init(AttributeSet attrs){
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.VerticalSwitchTextView);
        try {
            switchDuaration = array.getInt(R.styleable.VerticalSwitchTextView_switchDuaration, DEFAULT_SWITCH_DURATION);
            idleDuaration = array.getInt(R.styleable.VerticalSwitchTextView_idleDuaration, DEFAULT_IDLE_DURATION);
            sameDirection = array.getBoolean(R.styleable.VerticalSwitchTextView_sameDirection, true);
            switchOrientation = array.getInt(R.styleable.VerticalSwitchTextView_switchOrientation, 0);
        }catch (Exception e){
            logger.warn("获取自定义属性出错",e);
        }finally {
            array.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        childCounts = getChildCount();
    }

    private ValueAnimator animator;//轮播的切换动画
    private float currentAnimatedValue = 0.0f;
    private int currentIndex = 0;//
    private int switchOrientation = 0;
    private View inView;
    private View outView;

    private void initAnimator() {
        animator = ValueAnimator.ofInt(0,switchDuaration+idleDuaration).setDuration(switchDuaration+idleDuaration);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.addUpdateListener(this);
        animator.addListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    /**
     * 开始垂直切换动画
     */
    public void startSwitch(){
        if(childCounts>1){
            if(animator==null)
                initAnimator();
            if(animator!=null && !animator.isRunning())
                animator.start();
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        int currentValue = (int) animation.getAnimatedValue();
//        logger.info("animator:"+animation.hashCode()+",currentValue:"+currentValue);
        if (currentValue < switchDuaration) {
            currentAnimatedValue = ((float)currentValue)/((float)switchDuaration);
            moveView();
        }else if(currentAnimatedValue!=1.0f) {//确保如果最后一次取值由比switchDuaration小跳到比它大造成绘制不居中的情况不会出现
            currentAnimatedValue = 1.0f;
            moveView();
        }
    }

    private void moveView(){
        if(currentAnimatedValue==1.0f) {
            if(inView!=null)
                inView.setTranslationY(0);
        }else{
            float verticalOffsetOut,verticalOffsetIn;
            float mHeight = getHeight();
            if (switchOrientation == 0) {//向上滚动切换
                verticalOffsetOut = -mHeight*currentAnimatedValue;
                verticalOffsetIn = mHeight-mHeight*currentAnimatedValue;
            } else {//向下滚动切换
                verticalOffsetOut = mHeight*currentAnimatedValue;
                verticalOffsetIn = mHeight+mHeight*currentAnimatedValue;
            }
            if(inView!=null)
                inView.setTranslationY(verticalOffsetIn);
            if(outView!=null)
                outView.setTranslationY(verticalOffsetOut);
        }
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
        if(childCounts<=1)
            return;
        currentIndex = (++currentIndex) % childCounts;
        inView = getChildAt(currentIndex);
        outView = getChildAt((currentIndex + 1) % childCounts);
        if(!sameDirection){
            if(currentIndex+1==childCounts)
                switchOrientation = (switchOrientation + 1) % 2 ;
        }
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == VISIBLE)
            startSwitch();
        else if(animator!=null)
            animator.cancel();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startSwitch();
    }

    @Override
    protected void onDetachedFromWindow() {
        if(animator!=null)
            animator.cancel();
        super.onDetachedFromWindow();
    }


    @Override
    public void onAnimationStart(Animator animation) {}

    @Override
    public void onAnimationEnd(Animator animation) {}

    @Override
    public void onAnimationCancel(Animator animation) {}
}
