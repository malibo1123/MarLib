package com.mar.lib.example;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.widget.TextView;

import com.mar.lib.widget.SwitchLayout;
import com.mar.lib.widget.VerticalSwitchTextView;

import java.util.ArrayList;

public class SwitchLayoutExample extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean whichLayout = R.id.example_switch_layout1==getIntent().getIntExtra(
                "example",0);
        int resId = whichLayout?R.layout.example_switch_layout:R.layout.example_switch_layout2;
        setContentView(resId);
        if(!whichLayout){
            final SwitchLayout switchLayout = findViewById(R.id.widgt_switch_layout);
            addTextView(switchLayout,"----------tv1------------");
            addTextView(switchLayout,"--------------tv2---------");
            addTextView(switchLayout,"--------tv3------------");

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                switchLayout.setVisibility(View.VISIBLE);
//            }
//        }, 5000);
        }
    }

    private void addTextView(SwitchLayout switchLayout,String text){
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,24f);
        tv.setTextColor(Color.BLACK);
        switchLayout.addView(tv);
    }
}
