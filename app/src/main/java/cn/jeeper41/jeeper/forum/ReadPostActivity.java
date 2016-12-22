package cn.jeeper41.jeeper.forum;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cn.jeeper41.jeeper.R;
import cn.jeeper41.jeeper.entity.UserApplication;
import cn.jeeper41.jeeper.service.ForumCallBack;
import cn.jeeper41.jeeper.service.ForumService;
import cn.jeeper41.jeeper.wiget.RefreshListView;

/**
 * Created by Shane on 2016-12-19.
 */

public class ReadPostActivity extends AppCompatActivity{
    private RefreshListView ReadPostView;
    private ReadPostAdapter adapter;
    private Context context = this;
    private List<JSONObject> postJSONList;
    private Handler handler;
    private String topicId;
    private Button btnSend;
    private EditText etReplyContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_post);
        btnSend=(Button) this.findViewById(R.id.btnSendReply);
        ReadPostView = (RefreshListView) findViewById(R.id.lvPostContent);
        //adapter = new ReadPostAdapter(this,ReadPostView,postJSONList);
        //ReadPostView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
        // 获取参数
        topicId = getIntent().getStringExtra("topicId");
        postJSONList= new LinkedList<JSONObject>();
        try {
            adapter=new ReadPostAdapter(context,postJSONList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ReadPostView.setAdapter(adapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etReplyContent=(EditText) findViewById(R.id.etWriteReply);
                //check if already logged in
                UserApplication Userapp=new UserApplication();
                if (Userapp.getUser()!=null){
                    Toast.makeText(context,etReplyContent.getText(),Toast.LENGTH_SHORT);
                }
                else
                    Toast.makeText(context,"请先登录后回复",Toast.LENGTH_SHORT);
                //send reply to server
            }
        });

        // refreash listener
         ReadPostView.setOnRefreashListener(new RefreshListView.OnRefreashListener() {
            @Override
            public void onRefreash() {
                loadMoreData(topicId);
           }

            @Override
            public void onLoadMore() {
                loadMoreData(topicId);
            }
        });

        handler = new Handler(getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        ReadPostView.onRefreashComplete();
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
        ReadPostView.refresh();
    }

    private void loadMoreData(final String topicid){
        new ForumService().getDetailPostList(topicid,new ForumCallBack() {
            @Override
            public void onFormFinish(JSONArray list) { //list里为result解析后的array
                if(list != null){
                        try {
                            List<JSONObject> PostsList = new LinkedList<JSONObject>();
                            for(int i=0;i<list.length();i++) {
                                PostsList.add(list.getJSONObject(i));
                            }
                            adapter=new ReadPostAdapter(context,PostsList);
                            adapter.notifyDataSetChanged();
                            ReadPostView.setAdapter(adapter);


                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                else{
                    handler.sendEmptyMessage(2);
                }
                handler.sendEmptyMessage(1);
            }
        });
    }
}
