package cn.jeeper41.jeeper.forum;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
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
import cn.jeeper41.jeeper.wiget.RefreshListView;

/**
 * Created by Shane on 2016-12-19.
 */

public class ReadPostActivity extends AppCompatActivity{
    private RefreshListView postChooseView;
    private Context context = this;
    private List<JSONArray> postJSONList = new ArrayList<JSONArray>();
    private Handler handler;
    private String topicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_post);
        //showBackwardView(R.string.bar_backward,true);
        //setTitle("");
        // 获取参数
        topicId = getIntent().getStringExtra("topicname");
        TextView tvPostTopic=(TextView)findViewById(R.id.tvPostTopic);
        tvPostTopic.setText(getIntent().getStringExtra("topicname"));
        postChooseView = (RefreshListView) findViewById(R.id.lvPostChoose);
        postChooseView.setAdapter(new PostListAdapter(context,postChooseView,postJSONList));

        // set onclick event
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
                //loadMoreData(topicId);
            }

            @Override
            public void onLoadMore() {
                //loadMoreData(topicId);
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
}
