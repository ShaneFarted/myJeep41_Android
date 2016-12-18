package cn.jeeper41.jeeper.entity;

import android.app.Application;

/**
 * Created by Administrator on 2016-12-17.
 */

public class UserApplication extends Application {
    private User currentuser;
    private static final User user=new User("","");
    @Override
    public void onCreate() {
        super.onCreate();
        setUser(user); //初始化全局变量
    }

    public User getUser() {
        return currentuser;
    }
    public void setUser(User user) {
        this.currentuser = user;
    }
}
