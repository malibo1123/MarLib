package com.mar.lib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mar.lib.view.R;

/**
 * 此自定义控件主要用来展示“名称-值”类型的视图，且需要输入值
 * 比如：
 * “金额     5元”、“支付方式     余额”…………<br>
 * 此控件宽度为占满MATCH-PARENT，高度为包含WRAP-CONTENT，
 * 名称和值之间的间隔由nameValueMargin自定义属性指定。<br>
 * 此控件支持的自定义属性如下：<br>
 * name----------设置名称（如上述的金额）<br>
 * value---------设置值（如上述的5元）<br>
 * nameSize------设置名称文字大小<br>
 * valueSize-----设置值的文字大小<br>
 * nameColor-----设置名称文字颜色<br>
 * valueColor----设置值的文字颜色<br>
 * valueHint-----设置值的默认显示（输入提示）<br>
 * rightIcon-----设置值右边的icon，如果没有设置则不显示<br>
 * nameValueMargin----设置名称和值之间的间距<br>
 * @author marblema
 * @time 2016-04-08
 */
public class NameValueEdit extends RelativeLayout{
	private TextView nameTextView;
	private EditText valueEditView;
	private ImageView rightIcon;

	public NameValueEdit(Context context) {
		super(context);
		initView(context, null, 0);
	}

	public NameValueEdit(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context, attrs, 0);
	}

	public NameValueEdit(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context, attrs, defStyle);
	}

	private void initView(Context context, AttributeSet attrs, int defStyle){
		View.inflate(context, R.layout.widget_name_value_edit, this);
		nameTextView = (TextView)findViewById(
				R.id.ControlNameValueEdit_NameText);
		valueEditView = (EditText)findViewById(
				R.id.ControlNameValueEdit_ValueEdit);
		rightIcon = (ImageView) findViewById(
				R.id.ControlNameValueEdit_RightIcon);
		initAttri(context, attrs, defStyle);
	}

	private void initAttri(Context context, AttributeSet attrs, int defStyle) {
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.NameValueEdit);
		if(a!=null){
			setNameTextView(a);
			setValueEditView(a);
			setRightIcon(a);
			a.recycle();
		}
	}

	private void setNameTextView(TypedArray a){
		String nameText = a.getString(R.styleable
				.NameValueEdit_name);
		if(!TextUtils.isEmpty(nameText))
			nameTextView.setText(nameText);
		int nameTextSize = a.getInt(R.styleable
				.NameValueEdit_nameSize, -1);
		if(nameTextSize!=-1)
			nameTextView.setTextSize(TypedValue
					.COMPLEX_UNIT_SP,nameTextSize);
		int nameTextColor = a.getColor(R.styleable
				.NameValueEdit_nameColor, -1);
//		if(nameTextColor!=-1)
			nameTextView.setTextColor(nameTextColor);
	}

	private void setValueEditView(TypedArray a){
		String hintText = a.getString(R.styleable
				.NameValueEdit_valueHint);
		String valueText = a.getString(R.styleable
				.NameValueEdit_value);
		if(!TextUtils.isEmpty(valueText))
			valueEditView.setText(valueText);
		if(!TextUtils.isEmpty(hintText))
			valueEditView.setHint(hintText);
		int size = a.getInt(R.styleable
				.NameValueEdit_valueSize, -1);
		if(size!=-1)
			valueEditView.setTextSize(TypedValue
					.COMPLEX_UNIT_SP,size);
		int color = a.getColor(R.styleable
				.NameValueEdit_valueColor, -1);
		int hintColor = a.getColor(R.styleable
				.NameValueEdit_hintColor, -1);
//		if(color!=-1)
			valueEditView.setTextColor(color);
		valueEditView.setHintTextColor(hintColor);
		int nameValueMargin = a.getDimensionPixelSize(
				R.styleable.NameValueEdit_nameValueMargin,-1);
		if(nameValueMargin!=-1)
			((LayoutParams) valueEditView.getLayoutParams())
					.setMargins(nameValueMargin,0,0,0);
	}

	private void setRightIcon(TypedArray a){
		int resId = a.getResourceId(R.styleable
				.NameValueEdit_rightIcon,0);
		if(resId>0)
			rightIcon.setImageResource(resId);

		boolean valueEditAble = a.getBoolean(R.styleable
				.NameValueEdit_valueEditAble,true);
		valueEditView.setFocusable(valueEditAble);
		valueEditView.setEnabled(valueEditAble);
	}

	
	public void setValueText(CharSequence text){
		valueEditView.setText(text);
	}

    public String getValueText(){
         return valueEditView.getText().toString();
    }

    public void setNameText(String text){
		nameTextView.setText(text);
	}

	public EditText getValueEditView(){
		return valueEditView;
	}

	public void setValueEditEnable(boolean enable){
		valueEditView.setFocusable(enable);
		valueEditView.setEnabled(enable);
	}

	public void setRightIconClickListener(OnClickListener listener){
		rightIcon.setOnClickListener(listener);
	}

	public ImageView getRightIcon(){
		return rightIcon;
	}

//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		performClick();
//		return false;
//	}


	@Override
	public void setOnClickListener(OnClickListener l) {
		super.setOnClickListener(l);
		valueEditView.setOnClickListener(l);
		rightIcon.setOnClickListener(l);
	}
}
