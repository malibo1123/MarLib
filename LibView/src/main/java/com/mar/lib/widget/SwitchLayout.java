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
 * 一个支持轮播的控件
 * 支持上下左右四个方向轮播，支持载入布局或者动态代码调用addView()函数：<br>
 *  几个xml属性如下：<br>
 *  switchTime---------------切换时间，如不设置，则是默认时间500ms；<br>
 *  idleTime----------------停留时间，如不设置，则是默认时间3000ms；<br>
 *  switchStyle-------------切换风格，支持三种切换风格（inAndOutSameTime---切入和切出同时进行，
 *  outThenInSameDirection---先切入后切出，但是切出的时候和切入的方向相同，
 *  outThenInDiffDirection---先切出再切入，但是切出的时候和切入的方向相反），
 *  如不设置，则是默认的切入和切出同时进行；<br>
 *  switchDirection---------切换方向，支持上下左右四个方向[toTop,toBottom,toLeft,toRight]；<br>
 *  switchInterpolator------对动画速度的控制，实际上是设置动画时间轴的差值方式，
 *  可以是线性匀速的(linearInterpolator)，
 *  可以是加速的(accelerateInterpolator),可以是减速的（decelerateInterpolator），
 *  可以是先加速再减速的(accelerateDecelerateInterpolator)，可以是到达结束时弹跳的(bounceInterpolator)，
 *  也可以是先后退再加速向前的(anticipateInterpolator)<br>
 * Created by malibo on 2018/10/31.
 */
public class SwitchLayout extends FrameLayout implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener, ViewGroup.OnHierarchyChangeListener {
    private static final int DEFAULT_SWITCH_TIME = 500;//默认切换时间
    private static final int DEFAULT_IDLE_Time = 3000;//默认停留时间

    private int switchTime;//切换时间
    private int idleTime;//间隔时间
    private SwitchStyle switchStyle;//切换风格
    private SwitchDirection switchDirection;//切换方向
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
            switchDirection = SwitchDirection.getDirection(array.getInt(
                    R.styleable.SwitchLayout_switchDirection, SwitchDirection.ToLeft.getValue()));
            if(switchDirection==null){
                switchDirection = SwitchDirection.ToLeft;
            }

            switchStyle = SwitchStyle.getStyle(array.getInt(R.styleable.SwitchLayout_switchPlayStyle,
                    SwitchStyle.StyleInAndOutSameTime.getValue()));
            if(switchStyle==null){
                switchStyle = SwitchStyle.StyleInAndOutSameTime;
            }

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
                    moveViewInOrOut(false,switchStyle
                            .getValue()==SwitchStyle.StyleOutThenInSameDirection.getValue());
                }else if (currentValue < switchTime) {
                    currentAnimatedValue = ((float)currentValue - switchTime/2.0f)/((float)switchTime/2.0f);
                    moveViewInOrOut(true,switchStyle
                            .getValue()==SwitchStyle.StyleOutThenInSameDirection.getValue());
                }else if(currentAnimatedValue!=1.0f) {//确保如果最后一次取值由比switchDuaration小跳到比它大造成绘制不居中的情况不会出现
                    currentAnimatedValue = 1.0f;
                    moveViewInAndOut();
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
            case ToRight:
                verticalOffsetOutX = mWidth * currentAnimatedValue;
                verticalOffsetInX = mWidth * currentAnimatedValue - mWidth;
                break;
            case ToTop:
                verticalOffsetOutY = -mHeight * currentAnimatedValue;
                verticalOffsetInY = mHeight - mHeight * currentAnimatedValue;
                break;
            case ToBottom:
                verticalOffsetOutY = mHeight * currentAnimatedValue;
                verticalOffsetInY = mHeight * currentAnimatedValue - mHeight;
                break;
            case ToLeft:
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
            case ToRight:
                verticalOffsetX = isIn?(mWidth * currentAnimatedValue - mWidth):
                        (mWidth * currentAnimatedValue*(sameInDirection?-1:1));
                break;
            case ToTop:
                verticalOffsetY = isIn?(mHeight - mHeight * currentAnimatedValue):
                        (mHeight * currentAnimatedValue*(sameInDirection?1:-1));
                break;
            case ToBottom:
                verticalOffsetY = isIn?(mHeight * currentAnimatedValue - mHeight):
                        (mHeight * currentAnimatedValue*(sameInDirection?-1:1));
                break;
            case ToLeft:
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
        if(currentIndex>=0 && currentIndex<getChildCount())
            showOnlyOneChild(currentIndex);
    }

    /**
     * 停止轮播，并将视图切换到指定的视图view
     * @param view 希望切换到的视图
     *             如果传入的视图view没有添加到SwitchLayout过，那么该函数不会有效果
     */
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
            currentIndex = index;
            if(animator!=null)
                animator.cancel();
        }
    }

    /**
     * 开始动画，注意，SwitchLayout是自动开始轮播不需要手动条用的。
     * 但是如果代码动态调用停止了动画，比如调用了{@link #stopSwitch()}
     * 或者{@link #switchToView(View)} ，那么可以调用此函数重新开始动画
     */
    public void startSwich(){
        if(animator==null)
            initAnimator();
        if(!animator.isRunning())
            animator.start();
    }

    /**
     * 停止轮播
     */
    public void stopSwitch(){
        if(animator!=null)
            animator.cancel();
    }

    public int getSwitchTime() {
        return switchTime;
    }

    public void setSwitchTime(int switchTime) {
        if(switchTime>0)
            this.switchTime = switchTime;
    }

    public int getIdleTime() {
        return idleTime;
    }

    public void setIdleTime(int idleTime) {
        if(idleTime>0)
            this.idleTime = idleTime;
    }

    public SwitchDirection getSwitchDirection() {
        return switchDirection;
    }

    public void setSwitchDirection(SwitchDirection switchDirection) {
        this.switchDirection = switchDirection;
    }

    public SwitchStyle getSwitchStyle() {
        return switchStyle;
    }

    public void setSwitchStyle(SwitchStyle switchStyle) {
        this.switchStyle = switchStyle;
    }

    public int getSwitchInterpolator() {
        return switchInterpolator;
    }

    public void setSwitchInterpolator(int switchInterpolator) {
        if(switchInterpolator>=1 && switchInterpolator<=6)
            this.switchInterpolator = switchInterpolator;
    }

    /**
     * 切换方向，共支持上、下、左、右四个切换方向
     */
    public enum SwitchDirection{
        ToLeft(1),ToRight(2),ToTop(3),ToBottom(4);

        private int direction;
        SwitchDirection(int direction){
            this.direction = direction;
        }

        public static SwitchDirection getDirection(int direction){
            switch (direction){
                case 1:
                    return ToLeft;
                case 2:
                    return ToRight;
                case 3:
                    return ToTop;
                case 4:
                    return ToBottom;
                default:
                    return null;
            }
        }

        public int getValue(){
            return direction;
        }
    }

    /**
     * 切换风格，共支持三种切换风格:
     * StyleInAndOutSameTime-----------切出和切进同时进行
     * StyleOutThenInSameDirection-----先出后进，进出一个方向
     * StyleOutThenInDiffDirection-----先出后进，进出方向不同
     */
    public enum SwitchStyle{
        StyleInAndOutSameTime(1)//切出和切进同时进行
        ,StyleOutThenInSameDirection(2)//先出后进，进出一个方向
        ,StyleOutThenInDiffDirection(3)//先出后进，进出方向不同
        ;

        private int style;
        SwitchStyle(int style){
            this.style = style;
        }

        public static SwitchStyle getStyle(int style){
            switch (style){
                case 1:
                    return StyleInAndOutSameTime;
                case 2:
                    return StyleOutThenInSameDirection;
                case 3:
                    return StyleOutThenInDiffDirection;
                default:
                    return null;
            }
        }

        public int getValue(){
            return style;
        }
    }
}
