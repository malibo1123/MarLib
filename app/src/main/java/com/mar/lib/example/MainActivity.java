package com.mar.lib.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.mar.lib.view.widget.VerticalSwitchTextView;
import com.mar.lib.example.R;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
//        tv.setText(stringFromJNI());
        VerticalSwitchTextView competCapabilityTex = (VerticalSwitchTextView)findViewById(R.id.award);
        ArrayList<String> content = new ArrayList<>(2);
        content.add("荣耀榜主");
        content.add("排名NO1");
        competCapabilityTex.setTextContent(content);
        competCapabilityTex.setSwitchSameDirection(false);
        competCapabilityTex.startSwitch();
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
