package com.mar.lib.view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mar.lib.R;
import com.mar.lib.util.DisPlayUtil;

/**
 * 此自定义控件主要用来展示“名称-值”类型的视图，比如：
 * “金额     5元”、“支付方式     余额”…………<br>
 * 此控件宽度为占满MATCH-PARENT，高度为包含WRAP-CONTENT，
 * 名称和值之间的最小间隔为10dp。名称居于最左边，值居于最右边;<br>
 * 此控件支持的自定义属性如下：<br>
 * control:nameText----------设置名称（如上述的金额）<br>
 * control:valueText---------设置值（如上述的5元）<br>
 * control:nameTextSize------设置名称文字大小<br>
 * control:valueTextSize-----设置值的文字大小<br>
 * control:nameTextColor-----设置名称文字颜色<br>
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
public class NameValueText extends RelativeLayout{
	private TextView nameTextView;
	private TextView valueTextView;
	
	private String nameText;
	private String valueText;
	private int nameTextSize;
	private int valueTextSize;
	private int nameTextColor;
	private int valueTextColor;
	private int nameTextLine;
	private int valueTextLine;
	
	private int defaultTextSize;
//	private int defaultValueTextSize;
	private int defaultTextColor;
	
	public NameValueText(Context context) {
		super(context);
		initView(context, null, 0);
	}
	
	public NameValueText(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context, attrs, 0);
	}
	
	public NameValueText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context, attrs, defStyle);
	}

	private void initView(Context context, AttributeSet attrs, int defStyle){
		View.inflate(context, R.layout.widget_name_value_text, this);
		valueTextView = (TextView)findViewById(R.id.CustomNameValueText_ValueText);
		nameTextView = (TextView)findViewById(R.id.CustomNameValueText_NameText);
		
		initAttri(context, attrs, defStyle);
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void initAttri(Context context, AttributeSet attrs, int defStyle){
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.CustomNameValueText);
		if(a!=null){
			nameText = a.getString(R.styleable.CustomNameValueText_nameText);
			valueText = a.getString(R.styleable.CustomNameValueText_valueText);
			nameTextSize = a.getInt(R.styleable.CustomNameValueText_nameTextSize, -1);
			valueTextSize = a.getInt(R.styleable.CustomNameValueText_valueTextSize, -1);
			nameTextLine = a.getInt(R.styleable.CustomNameValueText_nameLinePosition, -1);
			valueTextLine = a.getInt(R.styleable.CustomNameValueText_valueLinePosition, -1);
			nameTextColor = a.getColor(R.styleable.CustomNameValueText_nameTextColor, -1);
			valueTextColor = a.getColor(R.styleable.CustomNameValueText_valueTextColor, -1);

			defaultTextSize = a.getInt(R.styleable.CustomNameValueText_defaultTextSize, -1);
			defaultTextColor = a.getColor(R.styleable.CustomNameValueText_defaultTextColor, -1);

            int valueDrawableRightResId = a.getResourceId(R.styleable.CustomNameValueText_valueDrawableRight,0);
            int valueDrawableRightPadding = a.getDimensionPixelSize(R.styleable.CustomNameValueText_valueDrawableRightPadding,0);
        	a.recycle();
        if(!TextUtils.isEmpty(nameText)) {
			nameTextView.setText(nameText);
			setValueTextPadding();
		}
        if(!TextUtils.isEmpty(valueText)) {
			valueTextView.setText(valueText);
		}
        if(nameTextSize!=-1)
        	nameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,nameTextSize);
        else if(defaultTextSize!=-1)
        	nameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,defaultTextSize);
        if(valueTextSize!=-1)
        	valueTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,valueTextSize);
        else if(defaultTextSize!=-1)
        	valueTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,defaultTextSize);
        	
        if(nameTextColor!=-1)
        	nameTextView.setTextColor(nameTextColor);
        else
        	nameTextView.setTextColor(defaultTextColor);
        if(valueTextColor!=-1)
        	valueTextView.setTextColor(valueTextColor);
        else
        	valueTextView.setTextColor(defaultTextColor);
        
        if(nameTextLine!=-1)
        	nameTextView.getPaint().setFlags(nameTextLine);
        if(valueTextLine!=-1)
        	valueTextView.getPaint().setFlags(valueTextLine);
        if(valueDrawableRightResId>0) {
            Drawable drawable = null;
            if(Build.VERSION.SDK_INT<21)
                drawable= context.getResources()
                    .getDrawable(valueDrawableRightResId);
            else
                drawable = context.getDrawable(valueDrawableRightResId);
            valueTextView.setCompoundDrawablesWithIntrinsicBounds(null, null,drawable, null);
//            valueTextView.setCompoundDrawables(null, null,drawable, null);
            if (valueDrawableRightPadding > 0)
                valueTextView.setCompoundDrawablePadding(valueDrawableRightPadding);
        }
		}
	}
	
	public void setValueText(CharSequence text){
		valueText = text.toString();
		valueTextView.setText(text);
	}

	public String getValueText(){
		return valueTextView.getText().toString();
	}

	public void setNameText(String text){
		nameText = text.toString();
		nameTextView.setText(text);
		setValueTextPadding();
	}

	public TextView getValueTextView(){
		return valueTextView;
	}

	public TextView getNameTextView() {
		return nameTextView;
	}

	public void setDefaultTextSize(float size){
		nameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
		valueTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
	}

	public void setDefaultTextColor(int color){
		nameTextView.setTextColor(color);
		valueTextView.setTextColor(color);
	}
	
	private void setValueTextPadding(){
		float left = nameTextView.getPaint().measureText(nameText)
				+ DisPlayUtil.dip2px(getContext(),25.0f)+
				nameTextView.getPaddingLeft();
		valueTextView.setPadding((int)left,valueTextView.getPaddingTop(),
				valueTextView.getPaddingRight(),
				valueTextView.getPaddingRight());
	}
}
