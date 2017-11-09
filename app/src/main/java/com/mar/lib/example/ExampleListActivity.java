package com.mar.lib.example;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ExampleListActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_list_activity);

        // Example of a call to a native method
//        TextView tv = (TextView) findViewById(R.id.sample_text);
//        tv.setText(stringFromJNI());
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
//    public native String stringFromJNI();

    public void showWidgetExample(View view){
        showActivity(WidgetExample.class);
    }

    public void showVerticalSwitchText(View view){
        showActivity(VerticalSwitchExample.class);
    }

    public void showRoundedLetter(View view){
        showActivity(RoundedLetterExample.class);
    }

    public void showIconText(View view){
        showActivity(IconTextExample.class);
    }

    public void showNameValueEdit(View view){
        showActivity(NameValueEditExample.class);
    }

    private void showActivity(Class<? extends Activity> cls){
        Intent i = new Intent(this,cls);
        startActivity(i);
    }
}
