package com.mar.lib.view.widget;

import android.content.Context;
import android.content.res.ColorStateList;
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
   myTextColorSelector-------要显示的文本的ColorStateList，其优先级高于myTextColor<br>
   myIcon--------------------要显示的图片资源id<br>
   iconSize----------------图片大小<br>
   iconPosition------------图片位置（可以置于文字的上下左右）<br>
   textIconMargin------------文本合图片之间的间距<br>
   textClicked-------------点击或者选中的文字颜色<br>
   iconClicked-------------点击或者选中时要显示的icon<br>
 * @author marblema
 * @time 2016-04-08
 */
public class IconTextView extends LinearLayout {
	private static final int Up = 0;
	private static final int Down = 1;
	private static final int Left = 2;
	private static final int Right = 3;
	private TextView textView;
	private ImageView iconView;

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
		textView.setSingleLine();
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.IconTextView);
		if(a!=null){
			int iconPosition = a.getInt(
					R.styleable.IconTextView_iconPosition,0);
			int iconSize = a.getDimensionPixelSize(
					R.styleable.IconTextView_iconSize,
					LayoutParams.WRAP_CONTENT);
			int textIconMargin = a.getDimensionPixelSize(
					R.styleable.IconTextView_textIconMargin,0);

			LayoutParams tParams = new LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			LayoutParams iParams = new LayoutParams(
					iconSize,iconSize);
			switch (iconPosition){
			case Down://图片在文字下面
				setOrientation(VERTICAL);
				iParams.topMargin = textIconMargin;
				iParams.gravity = Gravity.CENTER_HORIZONTAL;
				tParams.gravity = Gravity.CENTER_HORIZONTAL;
				addView(textView,tParams);
				addView(iconView,iParams);
				break;
			case Left://图片在文字左边
				setOrientation(HORIZONTAL);
				iParams.rightMargin = textIconMargin;
				iParams.gravity = Gravity.CENTER_VERTICAL;
				tParams.gravity = Gravity.CENTER_VERTICAL;
				addView(iconView,iParams);
				addView(textView,tParams);
				break;
			case Right://图片在文字右边
				setOrientation(HORIZONTAL);
				iParams.leftMargin = textIconMargin;
				iParams.gravity = Gravity.CENTER_VERTICAL;
				tParams.gravity = Gravity.CENTER_VERTICAL;
				addView(textView,tParams);
				addView(iconView,iParams);
				break;
			case Up:// no break
			default://默认图片在文字上面
				setOrientation(VERTICAL);
				tParams.topMargin = textIconMargin;
				iParams.gravity = Gravity.CENTER_HORIZONTAL;
				tParams.gravity = Gravity.CENTER_HORIZONTAL;
				addView(iconView,iParams);
				addView(textView,tParams);
				break;
			}

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
			myTextColorSelector = a.getResourceId(
					R.styleable.IconTextView_myTextColorSelector, 0);
//			if(textColor >0)
			if(myTextColorSelector != 0) {
				ColorStateList csl=(ColorStateList)getResources().getColorStateList(myTextColorSelector);
				textView.setTextColor(csl);
			} else {
				textView.setTextColor(textColor);
			}

			iconClickedResId = a.getResourceId(
					R.styleable.IconTextView_iconClicked,0);
			textClickedColor = a.getColor(
					R.styleable.IconTextView_textClicked, -1);
//			LogUtil.i("cft","textClickedColor1:"+textClickedColor);
			a.recycle();
		}
//		setGravity(Gravity.CENTER_VERTICAL);
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

	public void setClickIcon(int resId){
		iconClickedResId = resId;
	}

	public TextView getTextView(){
		return textView;
	}

	public ImageView getIconView(){
		return iconView;
	}

	private int iconResId = -1;
	private int textColor = -1;
	private int iconClickedResId = -1;
	private int textClickedColor = -1;
	private int myTextColorSelector = 0;
	public void setStateClicked(boolean selection){
		if(selection) {
			if (iconClickedResId > 0)
				iconView.setImageResource(iconClickedResId);
//			if (textClickedColor >= 0)
				textView.setTextColor(textClickedColor);
//			LogUtil.i("cft","textClickedColor2:"+textClickedColor);
		}else{
			if(iconResId>0)
				iconView.setImageResource(iconResId);
//			if(textColor>=0)
				textView.setTextColor(textColor);
		}
	}
}
