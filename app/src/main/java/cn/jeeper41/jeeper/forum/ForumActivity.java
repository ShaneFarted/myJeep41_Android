package cn.jeeper41.jeeper.forum;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import cn.jeeper41.jeeper.R;
import cn.jeeper41.jeeper.service.ForumCallBack;
import cn.jeeper41.jeeper.service.ForumService;
import cn.jeeper41.jeeper.wiget.JeeperTitleBar;
import cn.jeeper41.jeeper.wiget.RefreshListView;

/**
 * Created by Shane on 2016-12-14.
 */

public class ForumActivity extends JeeperTitleBar {
    private RefreshListView forumListView;
    private Context context = this;
    private SeparatedListAdapter adapter;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_choose);
        showBackwardView(R.string.bar_backward,true);
        setTitle("");
        forumListView = (RefreshListView) findViewById(R.id.lvForumChoose);
        adapter = new SeparatedListAdapter(this);
        forumListView.setAdapter(adapter);

        // set onclick even
        forumListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), R.string.NO_DATA,
                        Toast.LENGTH_SHORT).show();

                /*                try {
                   *//* JSONObject jsonObject = forumList.get(position-1);
                    Intent intent = new Intent();
                    intent.setClass(ForumActivity.this, SubForumActivity.class);
                    intent.putExtra("groupId",jsonObject.getString("groupid"));
                    startActivity(intent);*//*
                } catch (JSONException e) {

                }*/
            }
        });
        // refreash listener
        forumListView.setOnRefreashListener(new RefreshListView.OnRefreashListener() {
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
                        forumListView.onRefreashComplete();
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
        forumListView.refresh();
    }
    /**
     * laod more data
     * @param pageIndex
     */
    private void loadMoreData(final String pageIndex){
        new ForumService().getForum(new ForumCallBack() {
            @Override
            public void onFormFinish(JSONArray list) {
                if(list != null){
                    for(int i=0;i<list.length();i++){
                        try {
                            JSONObject jo = list.getJSONObject(i);
                            JSONArray subForum = jo.getJSONArray("subforum");
                            List<JSONObject> sectionList = new LinkedList<JSONObject>();
                            for (int j = 0; j < subForum.length(); j++) {
                                sectionList.add(subForum.getJSONObject(j));
                            }
                            adapter.addSection(jo.getString("groupname"),new ForumAdapter(context,forumListView,sectionList));
                        }catch (Exception e){
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
