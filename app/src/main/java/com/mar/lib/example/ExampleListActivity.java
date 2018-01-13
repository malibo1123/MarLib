package com.mar.lib.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

    public void showEnviewsExample(View view){
        showActivity(EnviesExample.class);
    }

    public void showAndroidWidgetNotOftenUseExample(View view){
        showActivity(AndroidNotOftenUseWidgetExample.class);
    }

    public void showPhysicsExample(View view){
        showActivity(PhysicsExampleActivity.class);
    }

    public void showSpeedViewExample(View view){
        showActivity(SpeedViewExample.class);
    }

    private void showActivity(Class<? extends Activity> cls){
        Intent i = new Intent(this,cls);
        startActivity(i);
    }

//    public void testPostTime(View view){
//        TextView tv = (TextView) findViewById(R.id.test_post_time);
//        tv.append("\nsendMsg开始时间："+System.currentTimeMillis());
//        for(int i=0;i<1000;i++){
//            mHandler.sendEmptyMessage(i+1);
//        }
//        tv.append("\nsendMsg结束时间："+System.currentTimeMillis());
//        tv.append("\npost开始时间："+System.currentTimeMillis());
//        for(int i=0;i<1000;i++){
//            final int tmp = i+1;
//            mHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(ExampleListActivity.this,
//                            "post:"+tmp,Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//        tv.append("\npost结束时间："+System.currentTimeMillis());
//    }
//
//    private Handler mHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            Toast.makeText(ExampleListActivity.this,
//                    "sendMsg:"+msg.what,Toast.LENGTH_SHORT).show();
//        }
//    };
}
