package cn.jeeper41.jeeper.utils;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.util.Map;
import java.util.Set;

/**
 * Created by joker on 2016/11/15.
 */

public class HttpHelper {

    /**
     * Get
     * @param url
     * @param callback
     */
    public static void sendGet(String url,Callback callback){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(callback);
    }


    /**
     * Post
     * @param url
     * @param callback
     */
    public static void sendPost(String url,Map<String,String> params, Callback callback){
        Set<String> keys = params.keySet();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for(String key : keys){
            builder.add(key,params.get(key));
        }
        RequestBody formBody =builder.build();
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url).post(formBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(callback);
    }
}
