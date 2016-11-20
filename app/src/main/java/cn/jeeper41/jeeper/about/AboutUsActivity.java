package cn.jeeper41.jeeper.about;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import cn.jeeper41.jeeper.R;
import cn.jeeper41.jeeper.wiget.JeeperTitleBar;

public class AboutUsActivity extends JeeperTitleBar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        // show back
        showBackwardView(R.string.bar_backward,true);
        setTitle(R.string.MENU_ABOUT);
        PackageManager pm = getPackageManager();
        String versionName = "Unknow";
        try {
            PackageInfo pi = pm.getPackageInfo("cn.jeeper41.jeeper", 0);
            versionName = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {

        }

        TextView currentVer = (TextView)findViewById(R.id.current_version);
        currentVer.setText(currentVer.getText()+versionName);


    }
}
