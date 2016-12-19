package cn.jeeper41.jeeper.forum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
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
 * Created by linx on 2016/12/18.
 */

public class SubForumActivity extends JeeperTitleBar {
    private RefreshListView subforumListView;
    private Context context = this;
    private List<JSONArray> subforumList = new ArrayList<JSONArray>();
    private Handler handler;
    private String forumGroupId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_choose);
        showBackwardView(R.string.bar_backward,true);
        // 获取参数
        forumGroupId = getIntent().getStringExtra("groupId");
        setTitle("");
        subforumListView = (RefreshListView) findViewById(R.id.lvForumChoose);
        subforumListView.setAdapter(new ForumAdapter(context,subforumListView,subforumList));

        // set onclick even
        subforumListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONObject jsonObject = subforumList.get(0).getJSONObject(position-1);
                    Intent intent = new Intent();
                    intent.setClass(SubForumActivity.this, PostListActivity.class);
                    intent.putExtra("forumId",jsonObject.getString("forumid"));
                    startActivity(intent);
                   // Toast.makeText(getApplicationContext(),jsonObject.getString("forumid"),
                   //         Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        // refreash listener
        subforumListView.setOnRefreashListener(new RefreshListView.OnRefreashListener() {
            @Override
            public void onRefreash() {
                loadMoreData("0");
            }

            @Override
            public void onLoadMore() {
                loadMoreData("0");
            }
        });

        handler = new Handler(getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        subforumListView.onRefreashComplete();
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
        subforumListView.refresh();
    }
    /**
     * laod more data
     * @param groupId
     */
    private void loadMoreData(final String groupId){
        new ForumService().getSubForum(groupId,new ForumCallBack() {
            @Override
            public void onFormFinish(JSONArray list) {
                if(list != null){
                    subforumList.clear();
                    for(int i = 0;i<list.length();i++){
                        try {
                            JSONObject jo = list.getJSONObject(i);
                            if(forumGroupId.equals(jo.getString("groupid"))) {
                                subforumList.add(jo.getJSONArray("subforum"));
                                break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    handler.sendEmptyMessage(2);
                }
                handler.sendEmptyMessage(1);
            }
        });
    }
}
