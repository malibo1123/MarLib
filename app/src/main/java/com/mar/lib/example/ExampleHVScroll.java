package com.mar.lib.example;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ExampleHVScroll extends AppCompatActivity {
    private static final String TAG = "ExampleHVScroll";
    private AnchorPendantContainer hScrollContainer, vScrollContainer;
    private ViewGroup hContainer;
    private RelativeLayout vContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_hvscrollview);
        initHScroller();
        initVScroller();
        initHScroller2();

    }

    private void initVScroller(){
        vScrollContainer = findViewById(R.id.my_scroll_container_vertical);
        vContainer = findViewById(R.id.vertical_container);

        final TextView[] children = new TextView[10];
        final int[] rules = new int[]{0,1,2,3,4,5,6,7,8,9};
        for(int i=0;i<10;i++){
            children[i] = addChildToVScroller(i,vContainer,10);
        }

        findViewById(R.id.btn_reRule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tmp = rules[0];
                for(int i=0;i<rules.length-1;i++){
                    rules[i] = rules[i+1];
                }
                rules[9] = tmp;

                tmp = rules[3];
                rules[3] = rules[7];
                rules[7] = tmp;


                for(int i=0;i<10;i++){
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                            children[rules[i]].getLayoutParams();
                    if(i==0) {
                        params.addRule(RelativeLayout.BELOW, 0);
                    }else{
                        params.addRule(RelativeLayout.BELOW, children[rules[i-1]].getId());
                    }
                }
                vContainer.requestLayout();
            }
        });

        findViewById(R.id.btn_next_page2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vScrollContainer.auto2NextOrLastPage2(true);
            }
        });

        findViewById(R.id.btn_last_page2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vScrollContainer.auto2NextOrLastPage2(false);
            }
        });
    }

    private TextView addChildToVScroller(int i,ViewGroup container,int multi){
        TextView tv = new TextView(this);
        tv.setBackgroundColor(Color.RED + i*150);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        tv.setText(String.valueOf(i+1));
        tv.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(150, 150);
        tv.setId((i+1)*multi);
        container.addView(tv, params);
        return tv;
    }

    private void initHScroller(){
        hScrollContainer = findViewById(R.id.my_scroll_container);
        hContainer = findViewById(R.id.sc_container);

        for(int i=0;i<10;i++){
            addChildToScroller(i,hContainer);
        }

        final ImageView indicator = findViewById(R.id.indicator);
        hScrollContainer.setScrollToEdgeListener(new AnchorPendantContainer.ScrollToEdgeListener() {
            @Override
            public void scrollToEdge(int which) {
                if(which==0){
                    indicator.setImageResource(R.drawable.icon_back);
                }else if(which==1){
                    indicator.setImageResource(R.drawable.icon_back_click);
                }
            }
        });

        findViewById(R.id.btn_next_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hScrollContainer.auto2NextOrLastChild2(true);
            }
        });

        findViewById(R.id.btn_last_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hScrollContainer.auto2NextOrLastChild2(false);
            }
        });
        findViewById(R.id.btn_next_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hScrollContainer.auto2NextOrLastPage2(true);
            }
        });

        findViewById(R.id.btn_last_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hScrollContainer.auto2NextOrLastPage2(false);
            }
        });
        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChildToScroller(hContainer.getChildCount(),hContainer);
            }
        });
    }

    private void initHScroller2(){
        final AnchorPendantContainer hScrollContainer = findViewById(
                R.id.my_scroll_container_horizontal_relative);
        final RelativeLayout hContainer = findViewById(R.id.horizontal_container_relative);

        final TextView[] children = new TextView[10];
        final int[] rules = new int[10];
        for(int i=0;i<10;i++){
            rules[i] = i;
            children[i] = addChildToVScroller(i,hContainer,200);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                    children[rules[i]].getLayoutParams();
            params.leftMargin = 30;
        }
        for(int i=0;i<10;i++){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                    children[rules[i]].getLayoutParams();
            if(i==9) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.removeRule(RelativeLayout.RIGHT_OF);
                }
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                }
                params.addRule(RelativeLayout.RIGHT_OF, children[rules[i+1]].getId());
            }
        }
        findViewById(R.id.btn_reRule_relative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tmp = rules[0];
                for(int i=0;i<rules.length-1;i++){
                    rules[i] = rules[i+1];
                }
                rules[9] = tmp;

                tmp = rules[3];
                rules[3] = rules[7];
                rules[7] = tmp;


                for(int i=0;i<10;i++){
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                            children[rules[i]].getLayoutParams();
                    if(i==9) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            params.removeRule(RelativeLayout.RIGHT_OF);
                        }
                        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    }else{
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            params.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        }
                        params.addRule(RelativeLayout.RIGHT_OF, children[rules[i+1]].getId());
                    }
                }
                hContainer.requestLayout();
            }
        });

        findViewById(R.id.btn_next_page3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hScrollContainer.auto2NextOrLastPage2(true);
            }
        });

        findViewById(R.id.btn_last_page3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hScrollContainer.auto2NextOrLastPage2(false);
            }
        });
    }

    private void addChildToScroller(int i,ViewGroup container){
        TextView tv = new TextView(this);
        tv.setBackgroundColor(Color.RED + i*150);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        tv.setText(String.valueOf(i+1));
        tv.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300, 150);
        params.rightMargin = 15;
//        tv.setTag(R.id.view_priority,Integer.valueOf(i));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tv.setZ(i);
        }
        container.addView(tv, params);
    }
}
