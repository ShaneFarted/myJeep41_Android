package cn.jeeper41.jeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

public class HelloActivity extends AppCompatActivity {

    private Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_hello);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        TimerTask hello = new TimerTask(){
            @Override
            public void run() {
                //停止播放，启动主Activity
                timer.cancel();
                Intent intent = new Intent(HelloActivity.this,  MainActivity.class);
                startActivity(intent);
                finish();  //不加这一句，按回退键就会回到欢迎界面不合理。
            }
        };


        timer = new Timer(true);
        timer.schedule(hello, 2000); //延迟200毫秒执行
    }

}
