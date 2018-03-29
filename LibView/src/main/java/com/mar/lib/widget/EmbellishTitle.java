package com.mar.lib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mar.lib.view.R;

/**
 * Created by malibo on 2017/11/9.
 */

public class EmbellishTitle extends RelativeLayout {
    private TextView titleTv;
    private TextView titleLeftTv;
    private TextView titleRightTv;
    private ImageView leftIcon;
    private ImageView rightIcon;

    public EmbellishTitle(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public EmbellishTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public EmbellishTitle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs, defStyle);
    }

    private void initView(Context context, AttributeSet attrs, int defStyle){
        View.inflate(context, R.layout.widget_embellish_title, this);
        titleTv = (TextView)findViewById(
                R.id.EmbellishTitle_CenterText);
        titleLeftTv = (TextView)findViewById(
                R.id.EmbellishTitle_CenterTextLeft);
        titleRightTv = (TextView)findViewById(
                R.id.EmbellishTitle_CenterTextRight);
        leftIcon = (ImageView)findViewById(
                R.id.EmbellishTitle_LeftIcon);
        rightIcon = (ImageView) findViewById(
                R.id.EmbellishTitle_RightIcon);
        initAttri(context, attrs, defStyle);
    }

    private void initAttri(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.EmbellishTitle);
        if(a!=null){
            String titleText = a.getString(
                    R.styleable.EmbellishTitle_embellishTitleText);
            if(!TextUtils.isEmpty(titleText))
                titleTv.setText(titleText);
            String titleLeftText = a.getString(
                    R.styleable.EmbellishTitle_embellishTitleLeft);
            if(!TextUtils.isEmpty(titleLeftText))
                titleLeftTv.setText(titleLeftText);
            String titleRightText = a.getString(
                    R.styleable.EmbellishTitle_embellishTitleRight);
            if(!TextUtils.isEmpty(titleRightText))
                titleRightTv.setText(titleRightText);
            int titleCenterTextColor = a.getColor(
                    R.styleable.EmbellishTitle_embellishTitleTextCenterColor, Color.BLACK);
            titleTv.setTextColor(titleCenterTextColor);
            int titleSidesColor = a.getColor(
                    R.styleable.EmbellishTitle_embellishTitleTextSidesColor, Color.YELLOW);
            titleLeftTv.setTextColor(titleSidesColor);
            titleRightTv.setTextColor(titleSidesColor);
            float titleTextSize = a.getDimension(
                    R.styleable.EmbellishTitle_embellishTitleTextSize,22);
            titleTv.setTextSize(titleTextSize);
            titleLeftTv.setTextSize(titleTextSize);
            titleRightTv.setTextSize(titleTextSize);

            Drawable icon = a.getDrawable(R.styleable.EmbellishTitle_embellishTitleIcon);
            if(icon!=null){
                leftIcon.setImageDrawable(icon);
                rightIcon.setImageDrawable(icon);
            }
            a.recycle();
        }
    }
}
