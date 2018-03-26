package com.mar.lib.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import com.mar.lib.R;
import com.mar.lib.log.slf4j.Logger;
import com.mar.lib.log.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.mar.lib.util.StrUtils.isStrNull;


/**
 * 此自定义控件主要用来显示能够垂直翻转的多个文本<br>
 * 主要用于翻转显示文本<br><br>在xml中可以自定义的参数如下：<br>
 *      switchDuaration --------------- 文本切换的时间；<br>
 *      idleDuaration ----------------- 文本停留的时间；<br>
 *      sameDirection ----------------- 是否只朝一个方向滚动，默认为true
 *                      只朝一个方向滚动，可以设置为false这样会上下交替滚动；<br>
 *      alignment -------------------- 文字的对齐方式，可以是左对齐(left)或者
 *                      居中(center)或者右对齐(right)；<br>
 *      switchOrientation -------------定义翻转的方向，可以是向上翻转或者向下翻转；<br>
 *      stringList --------------------定义需要翻转显示的文本list，多条之间以“|”分割，<br>
 *              比如要显示三条文本，第一条未I，第二条为love，第三条为you，那么传入的定义为：
 *              I|love|you<br>
 *      verticalSwitchStyle -----------定义垂直切换风格，支持两种风格：<br>
 *          1、（inAndOut）切出和切入同时进行；<br>
 *          2、（firstOutThanIn）先切出再切入<br><br>
 *
 *  如果在代码中调用，可以使用{@link #setTextContent(List)}函数进行设置。
 *  <pre class="prettyprint">
 *      VerticalSwitchTextView competCapabilityTex = (VerticalSwitchTextView)findViewById(R.id.VerticalSwitchTextView1);
 ArrayList<VerticalSwitchTextView.TextItem> content = new ArrayList<>(2);
 content.add(new VerticalSwitchTextView.TextItem("荣耀榜主 排名\nNO1",
 new int[]{Color.RED,Color.GREEN},
 new int[]{0,4,11}));
 content.add(new VerticalSwitchTextView.TextItem("杨柳青青江水平杨柳青青江水平杨柳青青江被剪切了吗\n闻郎江上踏歌声\n东边日出西边雨\n道是无晴却有晴"));
 competCapabilityTex.setTextContent(content);
 *  </pre>
 *  <ol>
 *  <li>{@link TextItem#text}用来传入需要显示的文字，文字可以包括换行符；
 *  <li>{@link TextItem#colors}用来设置颜色，{@link TextItem#text}可以分成不同颜色的几段，
 *              {@link TextItem#colors}用来描述这几段的颜色。比如上面的“荣耀榜主 排名\nNO1”分
 *              成了两段不同的颜色，“荣耀榜主”文字颜色为红色，“  排名\nNO1”颜色
 *              为绿色，那么{@link TextItem#colors}需要用两个元素的数组来描述{Color.RED,Color.GREEN}
 *  <li>{@link TextItem#marks}用来配合设置颜色，{@link TextItem#text}可以分成不同颜色的几段，
 *              {@link TextItem#colors}用来描述这几段的颜色而{@link TextItem#marks}用来描述怎么
 *              划分。比如上面的“荣耀榜主 排名\nNO1”分成了两段“荣耀榜主”和“  排名\nNO1”，
 *              也就是分别为[0,4]以及[4,11].注意这里面包含了一个空格，必须计算在内，换行符"\n"
 *              算作1个字符。
 *  </ol>
 *  <b>注意：1、可以不设置{@link TextItem#colors}和{@link TextItem#marks}；但是设置
 *              了{@link TextItem#colors}就必须设置{@link TextItem#marks}<br>
 *       2、{@link TextItem#marks}的长度一定比{@link TextItem#colors}的长度大1；<br>
 *       3、{@link TextItem#marks}数组一定是且必须是以0开始，以{@link TextItem#text}的长度结束；<br></b>
 *  <br><b>另：<br>
 *  1、该轮播控件默认朝一个方向滚动，如果希望可以上下交替滚动，可以调用函数{@link #setSwitchSameDirection(boolean)}
 *  来进行设置，只需要传入false即可。<br>
 *  2、如果只是需要更新list中的某一项，可以使用函数{@link #updateItem(TextItem, int)}，效率更高</b>
 *
 * Created by malibo on 2018/3/9.
 */
public class VerticalSwitchTextView extends AppCompatTextView implements
        ValueAnimator.AnimatorUpdateListener,Animator.AnimatorListener{
    public Logger logger = LoggerFactory.getLogger(VerticalSwitchTextView.class.getSimpleName());
    private static final int DEFAULT_SWITCH_DURATION = 200;//默认切换时间
    private static final int DEFAULT_IDLE_DURATION = 3000;//默认停留时间
    public static final int TEXT_ALIGN_CENTER = 0;
    public static final int TEXT_ALIGN_LEFT = 1;
    public static final int TEXT_ALIGN_RIGHT = 2;
    public static final int StyleInAndOut = 1;
    public static final int StyleFirstOutThenIn = 2;

    private List<TextItem> lists;//会循环显示的文本内容
    private int contentSize;
    private TextItem outItem;//当前滑出的文本内容
    private TextItem inItem;//当前滑入的文本内容
    private float textBaseY;//文本显示的baseline
    private int currentIndex = 0;//当前显示到第几个文本

    private int switchDuaration = DEFAULT_SWITCH_DURATION;//切换时间
    private int idleDuaration = DEFAULT_IDLE_DURATION;//间隔时间
    private int switchOrientation = 0;
    private int alignment = TEXT_ALIGN_CENTER;
    private boolean sameDirection = true;//是否固定只朝一个方向滚动
    private int switchStyle = StyleInAndOut;
    private float currentAnimatedValue = 0.0f;
    private ValueAnimator animator;//轮播的切换动画
    private boolean hasCut = false;//记录是否已经对文本进行裁剪，如果设置居中而文本过长，会对文本进行裁剪

    private int mWidth;//控件宽度
    private int mHeight;//控件高度
    private int addHeightTimes = 1;//用来记录最多的item有多少行
    private int paddingLeft = 0,paddingBottom = 0,paddingTop = 0,paddingRight = 0;//上下左右四个padding
    private float baseLine = 0;//第一行绘制的基线位置
    private float fontHeight;//字体高度
    private Paint mPaint;//用来绘制文字的画刷
    private float inTextStartX;//渐入文本中轴线X坐标
    private float outTextStartX;//渐出文本中轴线X坐标

    public VerticalSwitchTextView(Context context) {
        this(context, null);
    }

    public VerticalSwitchTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalSwitchTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VerticalSwitchTextView);
        String stringList = null;
        try {
            switchDuaration = array.getInt(R.styleable.VerticalSwitchTextView_switchDuaration, DEFAULT_SWITCH_DURATION);
            idleDuaration = array.getInt(R.styleable.VerticalSwitchTextView_idleDuaration, DEFAULT_IDLE_DURATION);
            switchOrientation = array.getInt(R.styleable.VerticalSwitchTextView_switchOrientation, 0);
            switchStyle = array.getInt(R.styleable.VerticalSwitchTextView_verticalSwitchStyle, StyleInAndOut);
            alignment = array.getInt(R.styleable.VerticalSwitchTextView_alignment, TEXT_ALIGN_CENTER);
            sameDirection = array.getBoolean(R.styleable.VerticalSwitchTextView_sameDirection, true);
            stringList = array.getString(R.styleable.VerticalSwitchTextView_stringList);
        }catch (Exception e){
            logger.warn("获取自定义属性出错",e);
        }finally {
            array.recycle();
        }
        init();
        if(!isStrNull(stringList)) {
            String[] strArray = stringList.split("\\|");
            if(strArray.length>0){
                ArrayList<TextItem> lists = new ArrayList<TextItem>(strArray.length);
                for (String aStrArray : strArray) {
                    TextItem item = new TextItem(aStrArray);
                    lists.add(item);
                }
                setTextContent(lists);
            }
        }
    }

    /**
     * 设置循环显示的文本内容
     * 此方法调用后如果传入的list的大小大于1会自动开始垂直轮播切换
     * 所以如果调用了此函数无需再显示调用{@link #startSwitch()}
     * @param content 内容list
     */
    public void setTextContent(List<TextItem> content) {
        lists = content;
        if (lists == null || lists.size() == 0) {
            return;
        }
        hasCut = false;
        contentSize = lists.size();
        getAddHeightTimes(content);
        //如果文本换行数增加，需要调整文本高度
        if(addHeightTimes>1) {
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            fontHeight = fontMetrics.bottom - fontMetrics.top + 1;
            mHeight = (int) (fontHeight  * addHeightTimes) + paddingBottom + paddingTop;
            setHeight(mHeight);
        }
        getInAndOutItem();
        if (contentSize > 1) {
            startSwitch();
        }else{
            if(animator!=null)
                animator.cancel();
            currentAnimatedValue = 1.0f;
            invalidate();
        }
    }

    /**
     * 如果只是需要更新list中的某一项，可以使用此函数，效率更高
     * @param item 新的item
     * @param index 新item的索引
     */
    public void updateItem(TextItem item, int index){
        if(item==null || TextUtils.isEmpty(item.text) ||
                lists==null || lists.size()<=0 ||
                index<0 || index>lists.size()){
            return;
        }
        lists.set(index,item);
        outItem = lists.get(currentIndex);
        inItem = lists.get((currentIndex + 1) % contentSize);
        invalidate();
    }

    /**
     * 如果只是需要更新list中的某一项，可以使用此函数，效率更高
     * @param text 需要更新的文本
     * @param index 更新的item的索引
     */
    public void updateText(String text, int index){
        if(TextUtils.isEmpty(text) ||
                lists==null || lists.size()<=0 ||
                index<0 || index>lists.size()){
            return;
        }
        lists.get(index).text = text;
        invalidate();
    }

    /**设置是否只朝一个方向滚动，默认只朝一个方向滚动。
     * 如果只有两行数据可以设置此方法为false，这样就会
     * 每一次滚动方向与上一次方向相反
     * @param sameDirection  true----朝一个方向；false上下滚动
     *                       默认值为true，只朝一个方向滚动
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

    /**获取传入的list中的最大行数*/
    private void getAddHeightTimes(List<TextItem> content){
        if(contentSize>0){
            for(int i=0;i<contentSize;i++){
                String[] ss = content.get(i).text.split("\n");
                if(ss.length>addHeightTimes){
                    addHeightTimes = ss.length;
                }
            }
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

    private void getInAndOutItem(){
        if (lists == null || lists.size() == 0)
            return;
        if(currentIndex>=lists.size())
            currentIndex = 0;

        outItem = lists.get(currentIndex);
        if(lists.size()>1) {
            inItem = lists.get((currentIndex + 1) % lists.size());
        }else{
            inItem = lists.get(0);
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if(animator!=null)
            animator.cancel();
        currentAnimatedValue = 1.0f;
        if(lists != null && lists.size() > 0) {
            contentSize = 0;
            lists.clear();
        }
        invalidate();
        super.setText(text, type);
    }

    public List<TextItem> getListItems() {
        return lists;
    }

    /**
     * 主要用来调整TextView的高度
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
        fontHeight = fontMetrics.bottom - fontMetrics.top + 1;
        mHeight = (int)(fontHeight * addHeightTimes) + paddingBottom + paddingTop;
        textBaseY = mHeight - (mHeight - fontHeight) / 2 - fontMetrics.bottom;
        baseLine = paddingTop - fontMetrics.top;
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (contentSize <= 0 || inItem ==null || outItem ==null) {
            return;
        }
        if(!hasCut)
            cutStr();
        String[] inStrs = inItem.text.split("\n");
        inTextStartX = getStartX(inStrs.length<=1?inItem.text:inStrs[0]);
        String[] outStrs = outItem.text.split("\n");
        outTextStartX = getStartX(outStrs.length<=1?outItem.text:outStrs[0]);
        if(switchStyle== StyleFirstOutThenIn)
            drawFirstInThanOut(canvas);
        else
            drawInAndOutStr(canvas);
    }

    private float getStartX(String text) {
        //计算绘制的文字中心水平开始位置
        switch (alignment) {
            case TEXT_ALIGN_LEFT:
                return paddingLeft;
            case TEXT_ALIGN_RIGHT:
                return mWidth - paddingRight - mPaint.measureText(text);
            case TEXT_ALIGN_CENTER:
            default:
                return (mWidth + paddingLeft - paddingRight -
                        mPaint.measureText(text)) / 2;
        }
    }

    private void cutStr(){
        if (lists == null || lists.size() == 0)
            return;
        int maxSpace = mWidth - paddingLeft - paddingRight;
        if (maxSpace <= 0)
            return;
        for (int i = 0; i < lists.size(); i++) {//需要对每一个字符串进行裁剪
            String[] splits = lists.get(i).text.split("\n");
            for(int j=0;j<splits.length;j++) {
                boolean shouldCut = false;
                int count = 0;//用来判断最多裁剪次数，防止死循环
                while (true) {
                    count++;
                    float startX = (maxSpace - mPaint.measureText(splits[j])) / 2 + paddingLeft;
                    if (startX < paddingLeft) {
                        splits[j] = splits[j].substring(0, splits[j].length() - 1);
                        shouldCut = true;
                    } else {
                        break;
                    }
                    if (count >= splits[j].length()) break;
                }
                if (shouldCut)
                    splits[j] = splits[j].substring(0, splits[j].length() - 1) + "...";
            }
            if(splits.length>1){
                StringBuilder sb = new StringBuilder(splits[0]);
                for(int p=1;p<splits.length;p++){
                    sb.append("\n");
                    sb.append(splits[p]);
                }
                lists.get(i).text = sb.toString();
            }else{
                lists.get(i).text = splits[0];
            }
        }
        hasCut = true;
        getInAndOutItem();
    }

    private void drawInAndOutStr(Canvas canvas){
        if(currentAnimatedValue==1.0f)
            drawText(canvas, inItem, inTextStartX,baseLine);
        else{
            if (switchOrientation == 0) {//向上滚动切换
                float verticalOffsetOut = baseLine-mHeight*currentAnimatedValue;
                float verticalOffsetIn = baseLine+mHeight-mHeight*currentAnimatedValue;
                drawText(canvas, outItem, outTextStartX,verticalOffsetOut);
                drawText(canvas, inItem, inTextStartX,verticalOffsetIn);
            } else {//向下滚动切换
                float verticalOffsetOut = baseLine+mHeight*currentAnimatedValue;
                float verticalOffsetIn = baseLine-mHeight+mHeight*currentAnimatedValue;
                drawText(canvas, outItem, outTextStartX,verticalOffsetOut);
                drawText(canvas, inItem, inTextStartX,verticalOffsetIn);
            }
        }
    }

    private void drawFirstInThanOut(Canvas canvas){
        float verticalOffset = 2 * textBaseY * (0.5f - currentAnimatedValue);
        if (switchOrientation == 0) {//向上滚动切换
            if (verticalOffset > 0)
                drawText(canvas, outItem, outTextStartX,verticalOffset);
            else
                drawText(canvas, inItem, inTextStartX,2 * textBaseY + verticalOffset);
        } else {//向下滚动切换
            if (verticalOffset > 0)
                drawText(canvas, outItem, outTextStartX,2 * textBaseY - verticalOffset);
            else
                drawText(canvas, inItem, inTextStartX, -verticalOffset);
        }
    }

    private void drawText(Canvas canvas, TextItem textItem, float beginX, float beginY){
        boolean drawSucess = true;
        String[] splits = textItem.text.split("\n");
        if(splits.length<addHeightTimes){
            beginY += fontHeight*(addHeightTimes-splits.length)/2;
        }
        if(textItem.marks!=null && textItem.marks.length!=0 &&
                textItem.colors!=null && textItem.colors.length!=0 &&
                textItem.marks.length==textItem.colors.length+1){
            int itemLength = textItem.text.length();
            for(int i=0;i<textItem.colors.length;i++){
                if(textItem.marks[i]>itemLength ||
                        textItem.marks[i+1]>itemLength){
                    drawSucess = false;
                    break;
                }
                mPaint.setColor(textItem.colors[i]);
                String s = textItem.text.substring(textItem.marks[i],textItem.marks[i+1]);

                String[] ss = s.split("\n");
                if(ss.length<=1) {
                    canvas.drawText(s, beginX, beginY, mPaint);
                    if(s.endsWith("\n")) {
                        if(i<textItem.colors.length-1 && textItem.marks[i+1]<=itemLength &&
                                textItem.marks[i+2]<=itemLength) {
                            beginX = getStartX(textItem.text.substring(
                                    textItem.marks[i+1],textItem.marks[i+2]));
                            beginY += fontHeight;
                        }else{
                            beginX += mPaint.measureText(s);
                        }
                    }else{
                        beginX += mPaint.measureText(s);
                    }
                }else{
                    for(int j=0;j<ss.length;j++){
                        canvas.drawText(ss[j], beginX, beginY, mPaint);
                        if(j<ss.length-1) {
                            beginY += fontHeight;
                            beginX = getStartX(ss[j+1]);
                        }else{
                            beginX += mPaint.measureText(ss[j]);
                        }
                    }
                }
            }
        }else{
            drawSucess = false;
        }
        if(!drawSucess){
            mPaint.setColor(getCurrentTextColor());
            String[] ss = textItem.text.split("\n");
            if(ss.length<=1) {
                canvas.drawText(textItem.text, beginX, beginY, mPaint);
            }else{
                for(int i=0;i<ss.length;i++){
                    canvas.drawText(ss[i], beginX, beginY, mPaint);
                    if(i<ss.length-1) {
                        beginY += fontHeight;
                        beginX = getStartX(ss[i+1]);
                    }else{
                        beginX += mPaint.measureText(ss[i]);
                    }
                }
            }
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
        outItem = lists.get(currentIndex);
        inItem = lists.get((currentIndex + 1) % contentSize);
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

    public static class TextItem{
        @NonNull
        public String text = "";
        int[] colors;
        int[] marks;

        public TextItem(String text){
            if(text!=null){
                this.text = text;
            }
        }

        public TextItem(String text, int[] colors, int[] marks){
            if(text!=null){
                this.text = text;
            }
            this.colors = colors;
            this.marks = marks;
        }
    }
}