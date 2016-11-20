package cn.jeeper41.jeeper.blog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import cn.jeeper41.jeeper.R;
import cn.jeeper41.jeeper.entity.Article;
import cn.jeeper41.jeeper.wiget.JeeperTitleBar;

import static cn.jeeper41.jeeper.config.Global.DETAIL_URL;

public class ArticleDetailActivity extends JeeperTitleBar {

    private WebView articleWebView ;
    private Article article;
    private ProgressBar pbProgress;
    LinearLayout.LayoutParams statusBarHeightParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        // show back
        showBackwardView(R.string.bar_backward,true);
        setTitle("");
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("article");
        article = (Article) bundle.get("article");
        // init data
        pbProgress = (ProgressBar) findViewById(R.id.pb_progress);
        articleWebView = (WebView)findViewById(R.id.article_web_view);
        articleWebView.getSettings().setJavaScriptEnabled(true);
        articleWebView.getSettings().setBlockNetworkImage(false);
        articleWebView.getSettings().setBlockNetworkLoads(false);
        articleWebView.setWebChromeClient(new WebChromeClient() {//监听网页加载
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    // 网页加载完成
                    pbProgress.setVisibility(View.GONE);
                } else {
                    // 加载中
                    pbProgress.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        articleWebView.loadUrl(DETAIL_URL+article.getPageIndex()+"/"+article.getId());
    }

}
