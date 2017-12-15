package com.mar.lib.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.mar.lib.R;
import com.mar.lib.util.StrUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 此自定义控件主要用来显示能够垂直翻转的多个文本<br>
 * 主要用于翻转显示文本<br><br>在xml中可以自定义的参数如下：<br>
 *      switchDuaration --------------- 文本切换的时间；<br>
 *      idleDuaration ----------------- 文本停留的时间；<br>
 *      alignment -------------------- 文字的对齐方式，可以是左对齐(left)或者
 *                      居中(center)或者右对齐(right)；<br>
 *      switchOrientation -------------定义翻转的方向，可以是向上翻转或者向下翻转；<br>
 *      stringList --------------------定义需要翻转显示的文本list，多条之间以“|”分割，<br>
 *              比如要显示三条文本，第一条未your，第二条为are，第三条为beach，那么传入的定义为：
 *              your|are|beach<br>
 *      verticalSwitchStyle -----------定义垂直切换风格，支持两种风格：<br>
 *          1、（inAndOut）切出和切入同时进行；<br>
 *          2、（firstOutThanIn）先切出再切入<br><br>
 * Created by malibo on 2017/10/23.
 */
public class VerticalSwitchTextView extends AppCompatTextView implements
        ValueAnimator.AnimatorUpdateListener,Animator.AnimatorListener{
    private static final int DEFAULT_SWITCH_DURATION = 200;//默认切换时间
    private static final int DEFAULT_IDLE_DURATION = 3000;
    public static final int TEXT_ALIGN_CENTER = 0;
    public static final int TEXT_ALIGN_LEFT = 1;
    public static final int TEXT_ALIGN_RIGHT = 2;
    public static final int StyleInAndOut = 1;
    public static final int StyleFirstOutThanIn = 2;
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
    private int switchStyle = StyleInAndOut;
    private float currentAnimatedValue = 0.0f;
    private ValueAnimator animator;
    private boolean hasCut = false;//记录是否已经对文本进行裁剪，如果设置居中而文本过长，会对文本进行裁剪

//    private float verticalOffset = 0;
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
        String stringList;
        try {
            switchDuaration = array.getInt(R.styleable.VerticalSwitchTextView_switchDuaration, DEFAULT_SWITCH_DURATION);
            idleDuaration = array.getInt(R.styleable.VerticalSwitchTextView_idleDuaration, DEFAULT_IDLE_DURATION);
            switchOrientation = array.getInt(R.styleable.VerticalSwitchTextView_switchOrientation, 0);
            switchStyle = array.getInt(R.styleable.VerticalSwitchTextView_verticalSwitchStyle, StyleInAndOut);
            alignment = array.getInt(R.styleable.VerticalSwitchTextView_alignment, TEXT_ALIGN_CENTER);
            sameDirection = array.getBoolean(R.styleable.VerticalSwitchTextView_sameDirection, true);
            stringList = array.getString(R.styleable.VerticalSwitchTextView_stringList);
        } finally {
            array.recycle();
        }
        init();
        if(!StrUtils.isStrNull(stringList)) {
            String[] strArray = stringList.split("\\|");
            if(strArray!=null && strArray.length>0){
                ArrayList<String> lists = new ArrayList<String>(strArray.length);
                for(int i=0;i<strArray.length;i++){
                    lists.add(strArray[i]);
                }
                setTextContent(lists);
            }
        }
    }

    /**
     * 设置循环显示的文本内容
     * 此方法调用后如果传入的list的大小大于1会自动开始垂直轮播切换
     * @param content 内容list
     */
    public void setTextContent(List<String> content) {
        lists = content;
        if (lists == null || lists.size() == 0) {
            return;
        }
        hasCut = false;
        contentSize = lists.size();
        getInAndOutStr();
        if (contentSize > 1) {
            startSwitch();
        }else{
            if(animator!=null)
                animator.cancel();
            currentAnimatedValue = 1.0f;
            invalidate();
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
     * 开始垂直切换动画
     */
    public void startSwitch(){
        if(lists!=null && lists.size()>1){
            if(animator==null)
                init();
            if(animator!=null && !animator.isRunning())
                animator.start();
        }
    }

    private void init() {
        mPaint = getPaint();
        mPaint.setColor(getCurrentTextColor());

        animator = ValueAnimator.ofInt(0,switchDuaration+idleDuaration).setDuration(switchDuaration+idleDuaration);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.addUpdateListener(this);
        animator.addListener(this);
    }

    private void getInAndOutStr(){
        if (lists == null || lists.size() == 0)
            return;
        if(currentIndex>=lists.size())
            currentIndex = 0;

        outStr = lists.get(currentIndex);
        if(lists.size()>1) {
            inStr = lists.get((currentIndex + 1) % lists.size());
        }else{
            inStr = lists.get(0);
        }
    }

    private float baseLine = 0;
    /**
     * 主要用来调整TextView的高度
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        paddingLeft = getPaddingLeft();
        paddingBottom = getPaddingBottom();
        paddingTop = getPaddingTop();
        paddingRight = getPaddingRight();
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        if (contentSize <= 0) {
            return;
        }
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        //计算文字高度
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        mHeight = (int)(fontHeight+1) + paddingBottom + paddingTop;
        textBaseY = mHeight - (mHeight - fontHeight) / 2 - fontMetrics.bottom;
        baseLine = paddingTop - fontMetrics.top;
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d("marLib","高度："+( bottom - top));
    }

    /**
     * 文本中轴线X坐标
     */
    private float inTextStartX;
    private float outTextStartX;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (contentSize <= 0 || inStr==null || outStr==null) {
            return;
        }

        //计算绘制的文字中心位置
        switch (alignment) {
            case TEXT_ALIGN_CENTER:
                if(!hasCut)
                    cutStr();
                inTextStartX = (mWidth + paddingLeft - paddingRight - mPaint.measureText(inStr)) / 2;
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
        if(switchStyle==StyleFirstOutThanIn)
            drawFirstInThanOut(canvas);
        else
            drawInAndOutStr(canvas);
    }

    private void cutStr(){
        if (lists == null || lists.size() == 0)
            return;
        int maxSpace = mWidth - paddingLeft - paddingRight;
        if (maxSpace <= 0)
            return;
        for (int i = 0; i < lists.size(); i++) {//需要对每一个字符串进行裁剪
            String s = lists.get(i);
            boolean shouldCut = false;
            int count = 0;//用来判断最多裁剪次数，防止死循环
            while (true) {
                count++;
                float startX = (maxSpace - mPaint.measureText(s)) / 2 + paddingLeft;
                if (startX < paddingLeft) {
                    s = s.substring(0,s.length()-1);
                    shouldCut = true;
                }else{
                    break;
                }
                if(count>=s.length()) break;
            }
            if(shouldCut)
                lists.set(i,s.substring(0,s.length()-1)+"...");
        }
        hasCut = true;
        getInAndOutStr();
    }

    private void drawInAndOutStr(Canvas canvas){
        if(currentAnimatedValue==1.0f)
            canvas.drawText(inStr, inTextStartX,baseLine, mPaint);
        else{
            if (switchOrientation == 0) {//向上滚动切换
                float verticalOffsetOut = baseLine-mHeight*currentAnimatedValue;
                float verticalOffsetIn = baseLine+mHeight-mHeight*currentAnimatedValue;
                canvas.drawText(outStr, outTextStartX,verticalOffsetOut, mPaint);
                canvas.drawText(inStr, inTextStartX,verticalOffsetIn, mPaint);
            } else {//向下滚动切换
                float verticalOffsetOut = baseLine+mHeight*currentAnimatedValue;
                float verticalOffsetIn = baseLine-mHeight+mHeight*currentAnimatedValue;
                canvas.drawText(outStr, outTextStartX,verticalOffsetOut, mPaint);
                canvas.drawText(inStr, inTextStartX,verticalOffsetIn, mPaint);
            }
        }
    }

    private void drawFirstInThanOut(Canvas canvas){
        float verticalOffset = 2 * textBaseY * (0.5f - currentAnimatedValue);
        if (switchOrientation == 0) {//向上滚动切换
            if (verticalOffset > 0)
                canvas.drawText(outStr, outTextStartX,verticalOffset, mPaint);
            else
                canvas.drawText(inStr, inTextStartX,2 * textBaseY + verticalOffset, mPaint);
        } else {//向下滚动切换
            if (verticalOffset > 0)
                canvas.drawText(outStr, outTextStartX,2 * textBaseY - verticalOffset, mPaint);
            else
                canvas.drawText(inStr, inTextStartX, -verticalOffset, mPaint);
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        int currentValue = (int) animation.getAnimatedValue();
//        Log.d("marLib","animator:"+animation.hashCode()+",currentValue:"+currentValue);
        if (currentValue < switchDuaration) {
            currentAnimatedValue = ((float)currentValue)/((float)switchDuaration);
            invalidate();
        }else if(currentAnimatedValue!=1.0f) {//确保如果最后一次取值由比switchDuaration小跳到比它大造成绘制不居中的情况不会出现
            currentAnimatedValue = 1.0f;
            invalidate();
        }
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
        if(contentSize==0)
            return;
        currentIndex = (++currentIndex) % contentSize;
        outStr = lists.get(currentIndex);
        inStr = lists.get((currentIndex + 1) % contentSize);
        if(!sameDirection){
            if(currentIndex+1==contentSize)
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
    public void setTextSize(float size) {
        super.setTextSize(size);
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

    public boolean isRunning(){
        return animator!=null && animator.isRunning();
    }
}