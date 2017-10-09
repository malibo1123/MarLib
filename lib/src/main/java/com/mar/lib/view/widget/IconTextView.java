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

/**
 * 此控件宽度为占满MATCH-PARENT，高度为包含WRAP-CONTENT，
 * 名称和值之间的最小间隔为10dp。名称居于最左边，值居于最右边;<br>
 * 此控件支持的自定义属性如下：<br>
 * control:nameText----------设置名称（如上述的金额）<br>
 * control:valueText---------设置值（如上述的5元）<br>
 * control:nameTextSize------设置名称文字大小<br>
 * control:valueTextSize-----设置值的文字大小<br>
 * control:nameTextColor-----设置名称文字颜色<br>
 * control:myTextColorSelector---可以设置文字颜色的选择器<br>
 * control:valueTextColor----设置值的文字颜色<br>
 * control:defaultTextSize---设置默认文字大小<br>
 * control:defaultTextColor--设置默认的文字颜色<br>
 * control:nameLinePosition--设置名称文字风格，支持
 *       粗体（bold）、下划线（under）、删除线（middle）<br>
 * control:valueLinePosition-设置值的文字风格，支持
 *       粗体（bold）、下划线（under）、删除线（middle）<br>
 * @author marblema
 * @time 2016-04-08
 */
public class IconTextView extends LinearLayout {
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

	private void initView(Context context, AttributeSet attrs, int defStyle){
		textView = new TextView(context);
		iconView = new ImageView(context);
		textView.setSingleLine();
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.ControlIconTextView);
		if(a!=null){
			int iconPosition = a.getInt(
					R.styleable.ControlIconTextView_myIconPosition,0);
			int iconSize = a.getDimensionPixelSize(
					R.styleable.ControlIconTextView_myIconSize,
					LayoutParams.WRAP_CONTENT);
			int textIconMargin = a.getDimensionPixelSize(
					R.styleable.ControlIconTextView_textIconMargin,0);

			LayoutParams tParams = new LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			LayoutParams iParams = new LayoutParams(
					iconSize,iconSize);
			switch (iconPosition){
			case 1://图片在文字下面
				setOrientation(VERTICAL);
				iParams.topMargin = textIconMargin;
				iParams.gravity = Gravity.CENTER_HORIZONTAL;
				tParams.gravity = Gravity.CENTER_HORIZONTAL;
				addView(textView,tParams);
				addView(iconView,iParams);
				break;
			case 2://图片在文字左边
				setOrientation(HORIZONTAL);
				iParams.rightMargin = textIconMargin;
				iParams.gravity = Gravity.CENTER_VERTICAL;
				tParams.gravity = Gravity.CENTER_VERTICAL;
				addView(iconView,iParams);
				addView(textView,tParams);
				break;
			case 3://图片在文字右边
				setOrientation(HORIZONTAL);
				iParams.leftMargin = textIconMargin;
				iParams.gravity = Gravity.CENTER_VERTICAL;
				tParams.gravity = Gravity.CENTER_VERTICAL;
				addView(textView,tParams);
				addView(iconView,iParams);
				break;
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
					R.styleable.ControlIconTextView_myIcon,0);
			if(iconResId>0)
				iconView.setImageResource(iconResId);
			String text = a.getString(
					R.styleable.ControlIconTextView_myText);
			if(!TextUtils.isEmpty(text))
				textView.setText(text);
			int textSize = a.getInt(
					R.styleable.ControlIconTextView_myTextSize, -1);
			if(textSize>0)
				textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
			textColor = a.getColor(
					R.styleable.ControlIconTextView_myTextColor, -1);
			myTextColorSelector = a.getResourceId(
					R.styleable.ControlIconTextView_myTextColorSelector, 0);
//			if(textColor >0)
			if(myTextColorSelector != 0) {
				ColorStateList csl=(ColorStateList)getResources().getColorStateList(myTextColorSelector);
				textView.setTextColor(csl);
			} else {
				textView.setTextColor(textColor);
			}

			iconClickedResId = a.getResourceId(
					R.styleable.ControlIconTextView_myIconClicked,0);
			textClickedColor = a.getColor(
					R.styleable.ControlIconTextView_myTextClicked, -1);
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
