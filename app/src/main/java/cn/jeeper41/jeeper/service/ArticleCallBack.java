package cn.jeeper41.jeeper.service;

import java.util.List;

import cn.jeeper41.jeeper.entity.Article;

/**
 * Created by Administrator on 2016/11/15.
 */

public interface ArticleCallBack {
    /**
     * @param list
     */
    public void onFinish(List<Article> list);
}
