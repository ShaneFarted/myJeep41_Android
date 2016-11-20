package cn.jeeper41.jeeper.service;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.jeeper41.jeeper.config.Global;
import cn.jeeper41.jeeper.entity.Article;
import cn.jeeper41.jeeper.utils.HttpHelper;
import cn.jeeper41.jeeper.utils.JSONHelper;

/**
 * Created by joker on 2016/11/15.
 */
public class ArticleService {
    /**
     * find article by pageindex
     * @param pageIndex
     * @return
     */
    public void getArticle(String pageIndex,final ArticleCallBack callBack){
        HttpHelper.sendGet(Global.SERVER_URL+pageIndex, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                callBack.onFinish(null);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.isSuccessful()){
                    String jsonStr = response.body().string();
                    JSONArray json = new JSONArray();
                    List<Article> list = (List<Article>) JSONHelper.parseCollection(jsonStr, ArrayList.class, Article.class);
                    callBack.onFinish(list);
                }
                else {
                    callBack.onFinish(null);
                }
            }
        });
    }
}
