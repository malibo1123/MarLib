package com.mar.lib.example;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.mar.lib.widget.HVScrollView;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class AnchorPendantContainer extends HVScrollView{
    private static final String TAG = AnchorPendantContainer.class.getCanonicalName();
    /**被此ScrollView包裹的内部容器*/
    private ViewGroup internalContainer;

    public AnchorPendantContainer(Context context) {
        super(context);
        init();
    }

    public AnchorPendantContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnchorPendantContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
//        setOnScrollChangeListener(this);
//        setOnTouchListener(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view = getChildAt(0);
        if(!(view instanceof ViewGroup)){//如果传入的被包裹的根布局不是ViewGroup直接抛出异常
            throw new RuntimeException("the internalContainer you put in the scrollview must be a viewGroup");
        }
        internalContainer = (ViewGroup) view;
//        internalContainer.setChildrenDrawingOrderEnabled(true);
//        try {
//            Method method = internalContainer.getClass().getMethod(
//                    "setChildrenDrawingOrderEnabled",boolean.class);
//            method.setAccessible(true);
//            method.invoke(internalContainer,true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**滑动到了最边缘的view时触发*/
    public interface ScrollToEdgeListener{
        public void scrollToEdge(int which);
    }
    private ScrollToEdgeListener scrollToEdgeListener;
    public void setScrollToEdgeListener(ScrollToEdgeListener scrollToEdgeListener){
        this.scrollToEdgeListener = scrollToEdgeListener;
    }

    public void auto2NextOrLastChild2(boolean next){
        boolean isHorizontal = (getScrollOrientation() == SCROLL_ORIENTATION_HORIZONTAL);
        if (internalContainer != null) {
            int scroll = isHorizontal?getScrollX():getScrollY();
            int oriLen = isHorizontal?getWidth():getHeight();
            int scrollTo = oriLen + scroll;
            int childCount = internalContainer.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = internalContainer.getChildAt(i);
                if(child.getVisibility()!=VISIBLE){
                    continue;
                }
                int childLen = isHorizontal ? child.getWidth(): child.getHeight();
                int maxLen = isHorizontal ? child.getRight():child.getBottom();
                int attatchP = (isHorizontal ? child.getPaddingRight():
                        child.getPaddingBottom())+getMarginReflect(child.getLayoutParams());
                if (maxLen+attatchP>=scrollTo) {
                    boolean hasTo = maxLen-scrollTo<=childLen/3;
                    int index = next?Math.min(hasTo?i+1:i,childCount -1):
                            Math.max(hasTo?i-1:i-2,0);
                    View toChild = internalContainer.getChildAt(index);
                    int to = Math.max((isHorizontal ? toChild.getRight(): toChild.getBottom())-oriLen,0);
                    scrollTo(isHorizontal ? to : 0, isHorizontal ? 0 : to);
                    break;
                }
            }
        }
    }

    public void auto2NextOrLastPage(boolean next){
        boolean isHorizontal = (getScrollOrientation() == SCROLL_ORIENTATION_HORIZONTAL);
        if (internalContainer != null) {
            int scroll = isHorizontal?getScrollX():getScrollY();
            int oriLen = isHorizontal?getWidth():getHeight();
            int scrollTo = next?(oriLen + scroll):scroll;
            int childCount = internalContainer.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = internalContainer.getChildAt(i);
                if(child.getVisibility()!=VISIBLE){
                    continue;
                }
                int childLen = isHorizontal ? child.getWidth(): child.getHeight();
                int maxLen = isHorizontal ? child.getRight(): child.getBottom();
                int attatchP = (isHorizontal ? child.getPaddingRight():
                        child.getPaddingBottom())+getMarginReflect(child.getLayoutParams());
                if (maxLen+attatchP>=scrollTo) {
                    boolean hasTo = maxLen-scrollTo<=childLen/3;
                    if (next) {
                        int index = Math.min(hasTo?i+1:i,childCount -1);
                        View toChild = internalContainer.getChildAt(index);
                        int to = Math.max((isHorizontal ? toChild.getLeft(): toChild.getTop()),0);
                        scrollTo(isHorizontal ? to : 0, isHorizontal ? 0 : to);
                    }else {
                        int to = Math.max((hasTo?(maxLen + attatchP):
                                isHorizontal?child.getLeft():child.getTop()) - oriLen, 0);
                        scrollTo(isHorizontal ? to : 0, isHorizontal ? 0 : to);
                    }
                    break;
                }
            }
        }
    }

    public void auto2NextOrLastPage2(boolean next){
        if(internalContainer==null){
            return;
        }
        ArrayList<ChildInfo> childrenInfo = collectChildrenInfo();
        if(childrenInfo==null || childrenInfo.size()<1){
            return;
        }
        boolean isHorizontal = (getScrollOrientation() == SCROLL_ORIENTATION_HORIZONTAL);
        int scroll = isHorizontal?getScrollX():getScrollY();
        int oriLen = isHorizontal?getWidth():getHeight();
        int scrollTo = next?(oriLen + scroll):scroll;
        int size = childrenInfo.size();
        for (int i = 0; i < size; i++) {
            ChildInfo childInfo = childrenInfo.get(i);
            if (childInfo.positionEnd+childInfo.lenAfter>=scrollTo) {
                boolean hasTo = childInfo.positionEnd-scrollTo<=childInfo.length/3;
                if (next) {
                    int index = Math.min(hasTo?i+1:i,size -1);
                    ChildInfo toChildInfo = childrenInfo.get(index);
                    scrollTo(isHorizontal ? toChildInfo.positionStart : 0,
                            isHorizontal ? 0 : toChildInfo.positionStart);
                }else {
                    int index = Math.max(hasTo?i:i-1,0);
                    ChildInfo toChildInfo = childrenInfo.get(index);
                    int to = Math.max(toChildInfo.positionEnd-oriLen,0);
                    scrollTo(isHorizontal ? to : 0, isHorizontal ? 0 : to);
                }
                break;
            }
        }
    }

    private ArrayList<ChildInfo> collectChildrenInfo(){
        ArrayList<ChildInfo> childrenInfo = new ArrayList<>();
        boolean isHorizontal = (getScrollOrientation() == SCROLL_ORIENTATION_HORIZONTAL);
        if (internalContainer != null) {
            int childCount = internalContainer.getChildCount();
            for(int i=0;i<childCount;i++){
                View view = internalContainer.getChildAt(i);
                if (view.getVisibility()==VISIBLE) {
                    ChildInfo childInfo = getChildInfo(view,isHorizontal);
                    childInfo.index = i;
                    boolean add = false;
                    int size = childrenInfo.size();
                    for(int j = 0; j< size; j++){
                        ChildInfo childBefore = childrenInfo.get(j);
                        if(childInfo.positionStart<childBefore.positionStart){
                            childrenInfo.add(j,childInfo);
                            add = true;
                            break;
                        }
                    }
                    if(!add){
                        childrenInfo.add(childInfo);
                    }
                }
            }
        }
        return childrenInfo;
    }

    private ChildInfo getChildInfo(View view,boolean isHorizontal){
        ChildInfo childInfo = new ChildInfo();
        MarginLayoutParams params = (MarginLayoutParams) view.getLayoutParams();
        if(isHorizontal){
            childInfo.positionStart = view.getLeft();
            childInfo.positionEnd = view.getRight();
            childInfo.length = view.getWidth();
            childInfo.lenBefore = view.getPaddingLeft()+params.leftMargin;
            childInfo.lenAfter = view.getPaddingRight()+params.rightMargin;
        }else {
            childInfo.positionStart = view.getTop();
            childInfo.positionEnd = view.getBottom();
            childInfo.length = view.getHeight();
            childInfo.lenBefore = view.getPaddingTop()+params.topMargin;
            childInfo.lenAfter = view.getPaddingBottom()+params.bottomMargin;
        }
        return childInfo;
    }

    /*
    public void auto2NextOrLastChild(boolean next){
        if (internalContainer != null) {
            boolean isHorizontal = (getScrollOrientation() == SCROLL_ORIENTATION_HORIZONTAL);
            int scroll = isHorizontal?getScrollX():getScrollY();
            int oriLen = isHorizontal?getWidth():getHeight();
            int scrollTo = next?(oriLen + scroll):scroll;
            for (int i = 0; i < internalContainer.getChildCount() ; i++) {
                View child = internalContainer.getChildAt(i);
                if(child.getVisibility()!=VISIBLE){
                    continue;
                }
                int childLen = isHorizontal ? child.getWidth(): child.getHeight();
                int position = isHorizontal ? child.getRight(): child.getBottom();
                if (position>scrollTo && (position-childLen-scrollTo>(next? -childLen*2/3:-childLen/3))) {
                    View lastChild = next?child:internalContainer.getChildAt(Math.max(i-1,0));
                    mScrollState = SCROLL_STATE_IDLE;
                    smoothScrollTo(isHorizontal?(next?child.getRight()-oriLen:lastChild.getLeft()):0,
                            isHorizontal?0:(next?child.getBottom()-oriLen:lastChild.getTop()));
                    break;
                }
            }
        }
    }
*/
    @Override
    protected void onScrollChanged(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        super.onScrollChanged(scrollX, scrollY, oldScrollX, oldScrollY);
//        Log.d(TAG, "新的X:"+scrollX+",新的Y："+scrollY);
        detachIfScrollToEdige(scrollX,scrollY);
//        if(isFingerUp) {
//            downOrRight = (scrollX>oldScrollX || scrollY>oldScrollY);
//            scrollToHoleView(scrollX, scrollY);
//        }
        Log.i(TAG,"mScrollState:"+mScrollState);
    }

    @Override
    public void setScrollState(int scrollState) {
        //尝试了很多覆写函数，自测修改并覆写此函数有效果,表示滚动停止
//        if (scrollState == 0 && mScrollState != 0) {
//            scrollToHoleView(getScrollX(), getScrollY());
//        }
        super.setScrollState(scrollState);
    }

    private void detachIfScrollToEdige(int scrollX, int scrollY){
        if (getScrollOrientation() == SCROLL_ORIENTATION_HORIZONTAL) {
            if (scrollX == 0) {
                Log.d(TAG, "滑动到最左边-----> 左边");
                if(scrollToEdgeListener!=null){
                    scrollToEdgeListener.scrollToEdge(0);
                }
            }
            if (getChildAt(getChildCount() - 1).getWidth() == (getWidth() + scrollX)) {
                Log.d(TAG, "滑动到最右=边-----> 右边");
                if(scrollToEdgeListener!=null){
                    scrollToEdgeListener.scrollToEdge(1);
                }
            }

        } else if (getScrollOrientation() == SCROLL_ORIENTATION_VERTICAL) {
            if (scrollY == 0) {
                Log.d(TAG, "滑动到最上边-----> 上边");
                if(scrollToEdgeListener!=null){
                    scrollToEdgeListener.scrollToEdge(2);
                }
            }

            if (getChildAt(getChildCount() - 1).getHeight() == (getHeight() + scrollY)) {
                Log.d(TAG, "滑动到最下边-----> 下边");
                if(scrollToEdgeListener!=null){
                    scrollToEdgeListener.scrollToEdge(3);
                }
            }
        }
    }

    /**滑动停止的时候检测要滑动到整个子view保证最后一个子view不会被截断*/
    public void scrollToHoleView(int scrollX, int scrollY) {
        boolean isHorizontal = (getScrollOrientation() == SCROLL_ORIENTATION_HORIZONTAL);
        if (internalContainer != null) {
            int scroll = isHorizontal?scrollX:scrollY;
            Log.d(TAG, "scroll===>" + scroll);
            int oriLen = isHorizontal?getWidth():getHeight();
            int scrollTo = oriLen + scroll;
            for (int i = 0; i < internalContainer.getChildCount(); i++) {
                View child = internalContainer.getChildAt(i);
                if(child.getVisibility()!=VISIBLE){
                    continue;
                }
                int childLen = isHorizontal ? child.getWidth(): child.getHeight();
                int maxLen = isHorizontal ? child.getRight(): child.getBottom();
                if (scrollTo >= maxLen-childLen/2 && scrollTo<maxLen) {
                    smoothScrollTo(isHorizontal?(child.getRight()-oriLen):0,
                            isHorizontal?0:(child.getBottom()-oriLen));
                    break;
                }else if (scrollTo >maxLen-childLen && scrollTo<=maxLen-childLen/2) {
                    smoothScrollTo(isHorizontal?(child.getLeft()-oriLen):0,
                            isHorizontal?0:(child.getTop()-oriLen));
                    break;
                }
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean result = super.onTouchEvent(ev);
//        if(ev.getAction()==MotionEvent.ACTION_UP){
//            isFingerUp = true;
//            Log.d(TAG, "手指抬起");
//            scrollToHoleView(getScrollX(), getScrollY());
//        }else{
//            isFingerUp = false;
//        }
        return result;
    }

//    @Override
//    public void onScrollChange(HVScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//        Log.d(TAG, "新的X:"+scrollX+",新的Y："+scrollY);
//        detachIfScrollToEdige(scrollX,scrollY);
//        if(isFingerUp) {
//            downOrRight = (scrollX>oldScrollX || scrollY>oldScrollY);
//            scrollToHoleView(scrollX, scrollY);
//        }
//        Log.i(TAG,"mScrollState:"+mScrollState);
//    }

    @Override
    public void onStopNestedScroll(View target) {
        super.onStopNestedScroll(target);
//        Log.d(TAG, "onStopNestedScroll,滑动停止");
    }

    @Override
    public void stopNestedScroll() {
        super.stopNestedScroll();
//        Log.d(TAG, "stopNestedScroll,滑动停止");
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        boolean result = super.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
//        Log.d(TAG, "dxConsumed:"+dxConsumed+",dyConsumed:"+dyConsumed+",dxUnconsumed:"+
//                dxUnconsumed+",dyUnconsumed:"+dyUnconsumed);
        return result;
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        boolean result = super.dispatchNestedFling(velocityX, velocityY, consumed);
//        Log.d(TAG, "velocityX:"+velocityX+",velocityY:"+velocityY+",consumed:"+
//                consumed);
        return result;
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
//        Log.d(TAG, "新的X:"+scrollX+",新的Y："+scrollY);
//        Log.d(TAG, "onOverScrolled,clampedX:"+clampedX+",clampedY:"+clampedY);

//        if(clampedX || clampedY) {
//            scrollToHoleView(scrollX, scrollY);
//        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure,能否水平滑动："+canScrollHorizontally()+
                ",能否垂直滑动："+canScrollVertically());
    }

    private int getMarginReflect(ViewGroup.LayoutParams lp){
        int result = 0;
        boolean isHorizontal = (getScrollOrientation() == SCROLL_ORIENTATION_HORIZONTAL);
        try {
            Field field = lp.getClass().getField(isHorizontal?"rightMargin":"bottomMargin");
            field.setAccessible(true);
            result = field.getInt(lp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private class ChildInfo{
        public int index;
        public int length;
        public int lenBefore;
        public int lenAfter;
        public int positionStart;
        public int positionEnd;
    }
}
