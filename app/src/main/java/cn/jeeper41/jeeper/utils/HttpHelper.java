package cn.jeeper41.jeeper.utils;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

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


}
