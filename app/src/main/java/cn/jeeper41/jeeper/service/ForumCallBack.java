package cn.jeeper41.jeeper.service;

import org.json.JSONArray;

/**
 * forum callback
 * Created by linx on 2016/12/18.
 */

public interface ForumCallBack {
    /**
     * @param list
     */
    void onFormFinish(JSONArray list);
}
