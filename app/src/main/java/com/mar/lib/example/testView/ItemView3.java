package com.mar.lib.example.testView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mar.lib.example.R;

public class ItemView3 extends RelativeLayout {
    public ItemView3(Context context) {
        super(context);
        init();
    }

    public ItemView3(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ItemView3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.layout_item3, this, true);
        rootView.findViewById(R.id.tv3).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "点击item3", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
