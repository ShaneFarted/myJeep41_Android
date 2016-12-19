package cn.jeeper41.jeeper.service;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.jeeper41.jeeper.config.Global;
import cn.jeeper41.jeeper.utils.HttpHelper;

/**
 * Forum
 * Created by linx on 2016/12/18.
 */

public class ForumService {

    /**
     * find forum
     * @return
     */
    public void getForum(final ForumCallBack callBack){
        HttpHelper.sendGet(Global.FORUM_URL, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                callBack.onFormFinish(null);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.isSuccessful()){
                    String jsonStr = response.body().string();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(jsonStr);
                        JSONArray result = jsonObject.getJSONArray("result");
                        callBack.onFormFinish(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    callBack.onFormFinish(null);
                }
            }
        });
    }

    /**
     * find forum
     * @return
     */
    public void getSubForum(String forumGroupId,final ForumCallBack callBack){
        HttpHelper.sendGet(Global.FORUM_URL, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                callBack.onFormFinish(null);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.isSuccessful()){
                    String jsonStr = response.body().string();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(jsonStr);
                        JSONArray result = jsonObject.getJSONArray("result");
                        callBack.onFormFinish(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    callBack.onFormFinish(null);
                }
            }
        });
    }

    public void getPostList(String forumId,final ForumCallBack callBack){
        HttpHelper.sendGet(Global.POSTLIST_URL+"?forumid="+forumId, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                callBack.onFormFinish(null);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.isSuccessful()){
                    String jsonStr = response.body().string();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(jsonStr);
                        JSONArray result = jsonObject.getJSONArray("result");
                        callBack.onFormFinish(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    callBack.onFormFinish(null);
                }
            }
        });
    }
}
