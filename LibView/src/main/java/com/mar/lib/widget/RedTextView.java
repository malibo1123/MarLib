package com.mar.lib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;

import com.mar.lib.view.R;
/**
 * @autor yincheng
 * @date 2017/7/3 003 16:03
 */
public class RedTextView extends AppCompatTextView {

    private boolean mHideOnNull = true;
    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private int inner_circle_color;
    private int outer_oval_color;
    private int outer_oval_width;
    private int inner_circle_radius;
    private int mCount = 0;
    private float density;

    public RedTextView(Context context) {
        this(context, null);
    }

    public RedTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public RedTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RedTextView, 0, 0);
        try {
            inner_circle_color = a.getColor(R.styleable.RedTextView_inner_circle_color,
                    Color.RED);

            outer_oval_color = a.getColor(R.styleable.RedTextView_outer_oval_color,
                    Color.WHITE);

            outer_oval_width = a.getDimensionPixelSize(R.styleable.RedTextView_outer_oval_width, 1);

            inner_circle_radius = a.getDimensionPixelSize(R.styleable.RedTextView_inner_circle_radius, 8);

        } finally {
            a.recycle();
        }

        init(context);
    }

    private void init(Context context) {
        density = context.getResources().getDisplayMetrics().density;

        mPaint = new Paint();

        // set default font
        setTextColor(Color.WHITE);
        setTypeface(Typeface.DEFAULT_BOLD);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);

        setGravity(Gravity.CENTER);

        // default values
        setHideOnNull(true);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (isHideOnNull() && (text == null || text.toString().equalsIgnoreCase("0"))) {
            setVisibility(View.GONE);
        } else {
            setVisibility(View.VISIBLE);
        }
        super.setText(text, type);
    }

    public boolean isHideOnNull() {
        return mHideOnNull;
    }

    public void setHideOnNull(boolean hideOnNull) {
        mHideOnNull = hideOnNull;
        setText(getText());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mCount == 0) {
            return;
        }

        mPaint.setColor(outer_oval_color);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        canvas.drawCircle(mWidth / 2, mHeight / 2, inner_circle_radius + outer_oval_width, mPaint);

        mPaint.reset();
        mPaint.setColor(inner_circle_color);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        canvas.drawCircle(mWidth / 2, mHeight / 2, inner_circle_radius, mPaint);

        if (mCount >= 1 && mCount <= 9) {
            mPaint.setColor(outer_oval_color);
            mPaint.setTextSize(9 * density);
            mPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(String.valueOf(mCount), mWidth / 2, mHeight * 0.75f, mPaint);
        } else if (mCount >= 10 && mCount <= 99) {
            mPaint.setColor(outer_oval_color);
            mPaint.setTextSize(8 * density);
            mPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(String.valueOf(mCount), mWidth / 2, mHeight * 0.72f, mPaint);
        } else if (mCount >= 100) {
            mPaint.setColor(outer_oval_color);
            mPaint.setTextSize(7 * density);
            mPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("99+", mWidth / 2, mHeight * 0.72f, mPaint);
        }

    }

    public void setBadgeCount(int count) {
        this.mCount = count;
        invalidate();
    }

}
