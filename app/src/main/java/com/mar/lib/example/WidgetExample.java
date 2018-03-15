package com.mar.lib.example;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.mar.lib.enviews.ENDownloadView;
import com.mar.lib.enviews.ENLoadingView;
import com.mar.lib.widget.VerticalSwitchTextView;

import java.util.ArrayList;

public class WidgetExample extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_widget);
        VerticalSwitchTextView competCapabilityTex = (VerticalSwitchTextView)findViewById(R.id.VerticalSwitchTextView1);
        ArrayList<VerticalSwitchTextView.TextItem> content = new ArrayList<>(2);
        content.add(new VerticalSwitchTextView.TextItem("荣耀榜主1 排名\nNO1",
                new int[]{Color.RED,Color.GREEN},
                new int[]{0,5,12}));
        content.add(new VerticalSwitchTextView.TextItem("杨柳青青江水平杨柳青青江水平杨柳青青江被剪切了吗\n闻郎江上踏歌声\n东边日出西边雨\n道是无晴却有晴"));
        content.add(new VerticalSwitchTextView.TextItem("荣耀榜主2\n排名NO1",
                new int[]{Color.YELLOW,Color.BLUE},
                new int[]{0,6,11}));
        content.add(new VerticalSwitchTextView.TextItem("荣耀榜主3\n排名NO1",
                new int[]{Color.BLACK,Color.YELLOW},
                new int[]{0,5,11}));
        competCapabilityTex.setTextContent(content);
    }
}
