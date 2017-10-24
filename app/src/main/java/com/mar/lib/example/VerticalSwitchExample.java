package com.mar.lib.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mar.lib.view.widget.VerticalSwitchTextView;

import java.util.ArrayList;

public class VerticalSwitchExample extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_vertical_switch_text);

        VerticalSwitchTextView competCapabilityTex = (VerticalSwitchTextView)findViewById(R.id.award);
        ArrayList<String> content = new ArrayList<>(2);
        content.add("荣耀榜主");
        content.add("排名NO1");
        competCapabilityTex.setTextContent(content);
        competCapabilityTex.setSwitchSameDirection(false);
        competCapabilityTex.startSwitch();
    }
}
