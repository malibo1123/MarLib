package com.mar.lib.example;

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
//        VerticalSwitchTextView competCapabilityTex = (VerticalSwitchTextView)findViewById(R.id.award);
//        ArrayList<String> content = new ArrayList<>(2);
//        content.add("荣耀榜主");
//        content.add("排名NO1");
//        competCapabilityTex.setTextContent(content);
//        competCapabilityTex.setSwitchSameDirection(false);
//        competCapabilityTex.startSwitch();
    }
}
