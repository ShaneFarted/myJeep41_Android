package cn.jeeper41.jeeper.entity;

/**
 * Created by Administrator on 2016-12-17.
 */

public class User {
    public String userid;
    public String displayname;

    //构造函数Constructor
    public User(String Userid,String Displayname){
        this.userid=Userid;
        this.displayname=Displayname;
    }
    public String getUserid() {
        return userid;
    }
    public String getDisplayname() {
        return displayname;
    }
}
