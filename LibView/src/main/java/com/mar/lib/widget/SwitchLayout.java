package com.mar.lib.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.mar.lib.util.DisPlayUtil;
import com.mar.lib.view.R;


/**
 * 颜值直播间小时榜和礼物周榜切换，写了一个轮播layout
 * 支持上下左右四个方向轮播，支持载入布局或者动态代码调用addView()函数：<br>
 *  几个xml属性如下
 *  switchTime---------切换时间；<br>
 *  idleTime---------停留时间；<br>
 *  switchStyle---------切换风格，支持三种切换风格；<br>
 *  switchDirection---------切换方向，支持上下左右四个方向；<br>
 *
 * Created by malibo on 2018/10/31.
 */
public class SwitchLayout extends FrameLayout implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener, ViewGroup.OnHierarchyChangeListener {
    private static final int DEFAULT_SWITCH_TIME = 500;//默认切换时间
    private static final int DEFAULT_IDLE_Time = 3000;//默认停留时间
    //四个切换方向：左右上下
    private static final int DirectionLeft = 1;
    private static final int DirectionRight = 2;
    private static final int DirectionTop = 3;
    private static final int DirectionBottom = 4;
    //三种切换风格
    private static final int StyleInAndOutSameTime = 1;//切出和切进同时进行
    private static final int StyleOutThenInSameDirection = 2;//先出后进，进出一个方向
    private static final int StyleOutThenInDiffDirection = 3;//先出后进，进出方向不同

    private int switchTime;//切换时间
    private int idleTime;//间隔时间
    private int switchStyle;//切换风格
    private int switchDirection;//切换方向
    private int switchInterpolator;

    public SwitchLayout(@NonNull Context context) {
        super(context);
        init(null);
    }

    public SwitchLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SwitchLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SwitchLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private int childCounts;
    private void init(AttributeSet attrs){
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.SwitchLayout);
        try {
            switchTime = array.getInt(R.styleable.SwitchLayout_switchTime, DEFAULT_SWITCH_TIME);
            idleTime = array.getInt(R.styleable.SwitchLayout_idleTime, DEFAULT_IDLE_Time);
            switchDirection = array.getInt(R.styleable.SwitchLayout_switchDirection, DirectionLeft);
            switchStyle = array.getInt(R.styleable.SwitchLayout_switchPlayStyle, StyleInAndOutSameTime);
            switchInterpolator = array.getInt(R.styleable.SwitchLayout_switchInterpolator, 1);
        }catch (Exception e){
            Log.w("VerticalSwitchLayout","获取自定义属性出错"+e.toString());
        }finally {
            array.recycle();
        }
        //监听动态添加或者移除子view
        setOnHierarchyChangeListener(this);
        setClipChildren(true);
        setClipToPadding(true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        childCounts = getChildCount();
        showOnlyOneChild(0);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        if (visibility!=getVisibility() && visibility==VISIBLE) {
            if(animator!=null && animator.isRunning())
                animator.cancel();
        }
        super.onWindowVisibilityChanged(visibility);
        if (visibility == VISIBLE) {
            startSwitch();
        }else if(animator!=null)
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
    public void onChildViewAdded(View parent, View child) {
        childCounts = getChildCount();
        if(parent==this && child!=null)
            child.setVisibility(INVISIBLE);
        if(childCounts > 1){
            if(inView==null || outView==null || animator==null || !animator.isRunning()){
                startSwitch();
            }
        }else{
            showOnlyOneChild(0);
        }
    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
        childCounts = getChildCount();
        if(childCounts < 2){
            if(animator!=null)
                animator.cancel();
            inView = null;
            outView = null;
            showOnlyOneChild(0);
        }
    }

    private void showOnlyOneChild(int which){
        if(childCounts>=1){
            if(getVisibility()!=VISIBLE){
                setVisibility(VISIBLE);
            }
            for(int i=0;i<childCounts;i++){
                View view = getChildAt(i);
                if(view!=null){
                    view.setVisibility(i==which?VISIBLE:INVISIBLE);
                }
            }
        }else{
            setVisibility(GONE);
        }
    }

    private ValueAnimator animator;//轮播的切换动画
    private float currentAnimatedValue = 0.0f;
    private int currentIndex = 0;
    private View inView;
    private View outView;

    private void initAnimator() {
        animator = ValueAnimator.ofInt(0,switchTime+idleTime).setDuration(switchTime+idleTime);
        if(switchInterpolator==2) {
            animator.setInterpolator(new AccelerateInterpolator());
        }else if(switchInterpolator==3) {
            animator.setInterpolator(new DecelerateInterpolator());
        }else if(switchInterpolator==4) {
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
        }else if(switchInterpolator==5) {
            animator.setInterpolator(new AnticipateInterpolator());
        }else if(switchInterpolator==6) {
            animator.setInterpolator(new BounceInterpolator());
        }else {
            animator.setInterpolator(new LinearInterpolator());
        }
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setStartDelay(idleTime);
        animator.addUpdateListener(this);
        animator.addListener(this);
    }

    /**
     * 开始垂直切换动画
     */
    public void startSwitch(){
        if(childCounts>1){
            if(animator==null)
                initAnimator();
            inView = getChildAt(1);
            outView = getChildAt(0);
            showOnlyOneChild(0);
            if(animator!=null && !animator.isRunning())
                animator.start();
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        int currentValue = (int) animation.getAnimatedValue();
        switch (switchStyle){
            case StyleOutThenInSameDirection:
            case StyleOutThenInDiffDirection:
                if (currentValue < switchTime/2.0f) {
                    currentAnimatedValue = ((float)currentValue)/((float)switchTime/2.0f);
                    moveViewInOrOut(false,switchStyle==StyleOutThenInSameDirection);
                }else if (currentValue < switchTime) {
                    currentAnimatedValue = ((float)currentValue - switchTime/2.0f)/((float)switchTime/2.0f);
                    moveViewInOrOut(true,switchStyle==StyleOutThenInSameDirection);
                }else if(currentAnimatedValue!=1.0f) {//确保如果最后一次取值由比switchDuaration小跳到比它大造成绘制不居中的情况不会出现
                    currentAnimatedValue = 1.0f;
                    moveViewInAndOut();
//                    if (inView != null) {
//                        inView.setVisibility(VISIBLE);
//                    }
//                    if (outView != null) {
//                        outView.setVisibility(INVISIBLE);
//                    }
                }
                break;
            case StyleInAndOutSameTime:
            default:
                if (currentValue < switchTime) {
                    currentAnimatedValue = ((float)currentValue)/((float)switchTime);
                    moveViewInAndOut();
                }else if(currentAnimatedValue!=1.0f) {//确保如果最后一次取值由比switchDuaration小跳到比它大造成绘制不居中的情况不会出现
                    currentAnimatedValue = 1.0f;
                    moveViewInAndOut();
                }
                break;
        }
    }

    private void moveViewInAndOut() {
        float verticalOffsetOutX = 0, verticalOffsetInX = 0,verticalOffsetOutY = 0, verticalOffsetInY = 0;
        final float viewMagrin = DisPlayUtil.dip2px(getContext(),5);
        float mHeight = getHeight() + viewMagrin;
        float mWidth = getWidth() + viewMagrin;
        switch (switchDirection){
            case DirectionRight:
                verticalOffsetOutX = mWidth * currentAnimatedValue;
                verticalOffsetInX = mWidth * currentAnimatedValue - mWidth;
                break;
            case DirectionTop:
                verticalOffsetOutY = -mHeight * currentAnimatedValue;
                verticalOffsetInY = mHeight - mHeight * currentAnimatedValue;
                break;
            case DirectionBottom:
                verticalOffsetOutY = mHeight * currentAnimatedValue;
                verticalOffsetInY = mHeight * currentAnimatedValue - mHeight;
                break;
            case DirectionLeft:
            default:
                verticalOffsetOutX = -mWidth * currentAnimatedValue;
                verticalOffsetInX = mWidth - mWidth * currentAnimatedValue;
                break;
        }

        if (inView != null) {
            inView.setTranslationY(verticalOffsetInY);
            inView.setTranslationX(verticalOffsetInX);
        }
        if (outView != null) {
            outView.setTranslationY(verticalOffsetOutY);
            outView.setTranslationX(verticalOffsetOutX);
        }
        for (int i = 0; i < childCounts; i++) {
            View view = getChildAt(i);
            if (view != null) {
                if(currentAnimatedValue<1.0f)
                    view.setVisibility((view == inView || view == outView) ? VISIBLE : INVISIBLE);
                else
                    view.setVisibility((view == inView) ? VISIBLE : INVISIBLE);
            }
        }
    }

    private void moveViewInOrOut(boolean isIn,boolean sameInDirection) {
        float verticalOffsetX = 0,verticalOffsetY = 0;
        final float viewMagrin = DisPlayUtil.dip2px(getContext(),5);
        float mHeight = getHeight() + viewMagrin;
        float mWidth = getWidth() + viewMagrin;
        switch (switchDirection){
            case DirectionRight:
                verticalOffsetX = isIn?(mWidth * currentAnimatedValue - mWidth):
                        (mWidth * currentAnimatedValue*(sameInDirection?-1:1));
                break;
            case DirectionTop:
                verticalOffsetY = isIn?(mHeight - mHeight * currentAnimatedValue):
                        (mHeight * currentAnimatedValue*(sameInDirection?1:-1));
                break;
            case DirectionBottom:
                verticalOffsetY = isIn?(mHeight * currentAnimatedValue - mHeight):
                        (mHeight * currentAnimatedValue*(sameInDirection?-1:1));
                break;
            case DirectionLeft:
            default:
                verticalOffsetX = isIn?(mWidth - mWidth * currentAnimatedValue):
                        (mWidth * currentAnimatedValue*(sameInDirection?1:-1));
                break;
        }
        if(isIn && inView != null) {
            inView.setTranslationY(verticalOffsetY);
            inView.setTranslationX(verticalOffsetX);
        }else if (!isIn && outView != null) {
            outView.setTranslationY(verticalOffsetY);
            outView.setTranslationX(verticalOffsetX);
        }
        for (int i = 0; i < childCounts; i++) {
            View view = getChildAt(i);
            if (view != null)
                view.setVisibility((view == (isIn ? inView : outView)) ? VISIBLE : INVISIBLE);
        }
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
        if(childCounts<=1)
            return;
        currentIndex = (++currentIndex) % childCounts;
        outView = getChildAt(currentIndex);
        inView = getChildAt((currentIndex + 1) % childCounts);
    }

    @Override
    public void onAnimationStart(Animator animation) { }

    @Override
    public void onAnimationEnd(Animator animation) {}

    @Override
    public void onAnimationCancel(Animator animation) {
        showOnlyOneChild(currentIndex%childCounts);
    }

    public void switchToView(View view){
        if(view==null)
            return;
        int index = -1;
        if(childCounts>1){
            for(int i=0;i<childCounts;i++){
                View v = getChildAt(i);
                if(view==v){
                    index = i;
                    v.setTranslationY(0);
                    v.setTranslationX(0);
                    break;
                }
            }
        }
        if(index>=0){
            if(animator!=null && animator.isRunning())
                animator.cancel();
            showOnlyOneChild(index);
            currentIndex = index-1;
            if(animator!=null)
                animator.start();
        }
    }
}
