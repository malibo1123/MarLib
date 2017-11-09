package com.mar.lib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mar.lib.R;

/**
 * Created by malibo on 2017/11/9.
 */

public class EmbellishTitle extends RelativeLayout {
    private TextView titleTextView;
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
        titleTextView = (TextView)findViewById(
                R.id.EmbellishTitle_CenterText);
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
                titleTextView.setText(titleText);
            int titleTextColor = a.getColor(
                    R.styleable.EmbellishTitle_embellishTitleTextColor, Color.BLACK);
            titleTextView.setTextColor(titleTextColor);
            int titleTextSize = a.getDimensionPixelSize(
                    R.styleable.EmbellishTitle_embellishTitleTextSize,22);
            titleTextView.setTextSize(titleTextSize);
            a.recycle();
        }
    }
}
