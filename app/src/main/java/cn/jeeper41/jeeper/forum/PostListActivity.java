package cn.jeeper41.jeeper.forum;

        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Message;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.TextView;
        import android.widget.Toast;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.List;

        import cn.jeeper41.jeeper.R;
        import cn.jeeper41.jeeper.service.ForumCallBack;
        import cn.jeeper41.jeeper.service.ForumService;
        import cn.jeeper41.jeeper.wiget.JeeperTitleBar;
        import cn.jeeper41.jeeper.wiget.RefreshListView;

/**
 * Created by Shane on 2016-12-18.
 */

public class PostListActivity extends JeeperTitleBar {
    private RefreshListView postChooseView;
    private Context context = this;
    private List<JSONArray> postJSONList = new ArrayList<JSONArray>();
    private Handler handler;
    private String forumId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_choose);
        showBackwardView(R.string.bar_backward,true);
        setTitle("");
        // 获取参数
        forumId = getIntent().getStringExtra("forumId");
        TextView tvForumTitle=(TextView)findViewById(R.id.tvForumTitle);
        tvForumTitle.setText(getIntent().getStringExtra("forumName"));
        postChooseView = (RefreshListView) findViewById(R.id.lvPostChoose);
        postChooseView.setAdapter(new PostListAdapter(context,postChooseView,postJSONList));

        // set onclick even
        postChooseView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONObject jsonObject = postJSONList.get(0).getJSONObject(position-1);
                    Toast.makeText(context,jsonObject.getString("topicid"),Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent();
                    //intent.setClass(PostListActivity.this, ReadPostActivity.class);
                    //intent.putExtra("topicId",jsonObject.getString("topicid"));
                    //startActivity(intent);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), R.string.NO_DATA,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        // refreash listener
        postChooseView.setOnRefreashListener(new RefreshListView.OnRefreashListener() {
            @Override
            public void onRefreash() {
                loadMoreData(forumId);
            }

            @Override
            public void onLoadMore() {
                loadMoreData(forumId);
            }
        });

        handler = new Handler(getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        postChooseView.onRefreashComplete();
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), R.string.NO_DATA,
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }

            }
        };

        // init list
        postChooseView.refresh();
    }
    /**
     * laod more data
     * @param forumid
     * //pageIndex
     */
    private void loadMoreData(final String forumid){
        new ForumService().getPostList(forumid,new ForumCallBack() {
            @Override
            public void onFormFinish(JSONArray list) {
                if(list != null){
                    postJSONList.clear();
                    postJSONList.add(list);
                }else{
                    handler.sendEmptyMessage(2);
                }
                handler.sendEmptyMessage(1);
            }
        });
    }
}

