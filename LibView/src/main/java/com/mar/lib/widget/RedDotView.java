package com.mar.lib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.mar.lib.util.DisPlayUtil;

/**
 * Created by Administrator on 2015/4/24.
 */
public class RedDotView extends View {
    private Paint paint;
    private Context context;

    private float radius;

    public RedDotView(Context context) {
        super(context);
        init(context);
    }

    public RedDotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        paint.setColor(Color.parseColor("#ff3000"));

        radius = 4 * DisPlayUtil.getDensity(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int center = getWidth() / 2;
        canvas.drawCircle(center, center, radius, paint);

        super.onDraw(canvas);
    }

    public void addToParent(ViewGroup parent, ViewGroup.LayoutParams params) {
        parent.addView(this, params);
    }

    public void removeFromParent(ViewGroup parent) {
        if (parent != null) {
            parent.removeView(this);
        }
    }

    public float getRadius() {
        return radius;
    }
}
