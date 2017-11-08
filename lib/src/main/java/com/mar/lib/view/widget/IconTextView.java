package com.mar.lib.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mar.lib.R;

/** 主要用于图片配文字的场景<br>
 * textValue--------------------要显示的文本<br>
   textSize----------------要显示文本的大小<br>
   textColor---------------要显示的文本颜色<br>
   myIcon--------------------要显示的图片资源id<br>
   iconSize----------------图片大小<br>
   iconPosition------------图片位置（可以置于文字的上下左右）<br>
   textIconMargin------------文本和图片之间的间距<br>
 * @author marblema
 * @time 2016-04-08
 */
public class IconTextView extends LinearLayout {
	private static final int Up = 0;
	private static final int Down = 1;
	private static final int Left = 2;
	private static final int Right = 3;
	private static final int IconPositionDefault = Up;
	private static final int IconSizeDefault = LayoutParams.WRAP_CONTENT;
	private static final int TextIconMarginDefault = 5;
	private TextView textView;
	private ImageView iconView;
	private int iconPosition = IconPositionDefault;
	private int iconSize = IconSizeDefault;
	private int textIconMargin = TextIconMarginDefault;
	private int iconResId = -1;
    private int textColor = -1;

	public IconTextView(Context context) {
		super(context);
		initView(context, null, 0);
	}

	public IconTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context, attrs, 0);
	}

	public IconTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context, attrs, defStyle);
	}

	@SuppressWarnings("ResourceType")
	private void initView(Context context, AttributeSet attrs, int defStyle){
		textView = new TextView(context);
		iconView = new ImageView(context);
//		textView.setSingleLine();
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.IconTextView);
		if(a!=null){
			readValues(a);
			a.recycle();
		}
        LayoutParams tParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        LayoutParams iParams = new LayoutParams(
                iconSize,iconSize);
        switch (iconPosition){
            case Down://图片在文字下面
                iParams.topMargin = textIconMargin;
                setView(tParams,iParams,true,VERTICAL);
                break;
            case Left://图片在文字左边
                iParams.rightMargin = textIconMargin;
                setView(tParams,iParams,false,HORIZONTAL);
                break;
            case Right://图片在文字右边
                iParams.leftMargin = textIconMargin;
                setView(tParams,iParams,true,HORIZONTAL);
                break;
            case Up:// no break
            default://默认图片在文字上面
                tParams.topMargin = textIconMargin;
                setView(tParams,iParams,false,VERTICAL);
                break;
        }
	}

	private void setView(LayoutParams tParams,LayoutParams iParams,
                         boolean textFirst,int orientation){
	    removeAllViews();
        setOrientation(orientation);
        if(orientation==VERTICAL) {
            iParams.gravity = Gravity.CENTER_HORIZONTAL;
            tParams.gravity = Gravity.CENTER_HORIZONTAL;
        }else if(orientation==HORIZONTAL) {
            iParams.gravity = Gravity.CENTER_VERTICAL;
            tParams.gravity = Gravity.CENTER_VERTICAL;
        }
        if(textFirst) {
            addView(textView, tParams);
            addView(iconView, iParams);
        }else{
            addView(iconView, iParams);
            addView(textView, tParams);
        }
    }

    private void readValues(TypedArray a){
        iconPosition = a.getInt(
                R.styleable.IconTextView_iconPosition,IconPositionDefault);
        iconSize = a.getDimensionPixelSize(
                R.styleable.IconTextView_iconSize,IconSizeDefault);
        textIconMargin = a.getDimensionPixelSize(
                R.styleable.IconTextView_textIconMargin,TextIconMarginDefault);
        iconResId = a.getResourceId(
                R.styleable.IconTextView_myIcon,0);
        if(iconResId>0)
            iconView.setImageResource(iconResId);
        String text = a.getString(
                R.styleable.IconTextView_textValue);
        if(!TextUtils.isEmpty(text))
            textView.setText(text);
        int textSize = a.getInt(
                R.styleable.IconTextView_textSize, -1);
        if(textSize>0)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
        textColor = a.getColor(
                R.styleable.IconTextView_textColor, -1);
        textView.setTextColor(textColor);
    }

	public void setText(CharSequence text){
		textView.setText(text);
	}

	public void setText(int strRes){
		textView.setText(strRes);
	}

	public void setIcon(Bitmap icon){
		iconView.setImageBitmap(icon);
	}

	public void setIcon(int resId){
		iconResId = resId;
		iconView.setImageResource(resId);
	}

	public TextView getTextView(){
		return textView;
	}

	public ImageView getIconView(){
		return iconView;
	}
}
