package cn.jeeper41.jeeper.forum;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cn.jeeper41.jeeper.LoginActivity;
import cn.jeeper41.jeeper.R;
import cn.jeeper41.jeeper.config.Global;
import cn.jeeper41.jeeper.entity.UserApplication;
import cn.jeeper41.jeeper.service.ForumCallBack;
import cn.jeeper41.jeeper.service.ForumService;
import cn.jeeper41.jeeper.service.RequestHandler;
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
    UserApplication Userapp;

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
        ReadPostView.setEnableLoadMore(false);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etReplyContent=(EditText) findViewById(R.id.etWriteReply);
                //check if already logged in
                Userapp=(UserApplication)getApplication();
                if (Userapp.getUser().getUserid().length()>0){
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    //Toast.makeText(getApplicationContext(),etReplyContent.getText(),Toast.LENGTH_SHORT).show();
                    //send reply to server
                    replyPost(Userapp.getUser().getUserid());
                }
                else {
                    Toast.makeText(getApplicationContext(), context.getString(R.string.FORUM_PLEASE_LOGIN), Toast.LENGTH_SHORT).show();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

        // refreash listener
         ReadPostView.setOnRefreashListener(new RefreshListView.OnRefreashListener() {
            @Override
            public void onRefreash() {
                loadMoreData(topicId);
                adapter.notifyDataSetChanged();
           }

            @Override
            public void onLoadMore() {
                loadMoreData(topicId);
                adapter.notifyDataSetChanged();
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

    //post方法回复帖子
    private void replyPost(final String uid){
        final String pcontent=((EditText)findViewById(R.id.etWriteReply)).getText().toString();

        class Testconn extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ReadPostActivity.this,"Posting...","Please Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) { //s为服务器返回的值
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equals("")) {
                    Toast.makeText(context,context.getString(R.string.FORUM_REPLY_SUCCESS),Toast.LENGTH_SHORT).show();
                    etReplyContent.setText("");
                    ReadPostView.refresh();
                }
                else
                    Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
                    //Toast.makeText(context,context.getString(R.string.FORUM_REPLY_FAILED),Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("pcontent", pcontent);
                    params.put("userid", uid);
                    params.put("topicid", topicId);
                    RequestHandler rh = new RequestHandler();
                    String res = rh.sendPostRequest(Global.SENDREPLY_URL, params);
                    return res;

            }
        }
        if (pcontent.length()<=0||pcontent.trim().isEmpty()) {
            Toast.makeText(context,getString(R.string.FORUM_REPLY_IS_NULL),Toast.LENGTH_SHORT).show();
        }
        else {
            Testconn test = new Testconn();
            test.execute();
        }
    }

    private void loadMoreData(final String topicid){
        new ForumService().getDetailPostList(topicid,new ForumCallBack() {
            @Override
            public void onFormFinish(JSONArray list) { //list里为result解析后的array
                if(list != null){
                        try {
                            postJSONList.clear();
                            //给楼主贴放入标题
                            list.getJSONObject(0).put("topicname",getIntent().getStringExtra("topicName"));
                            for(int i=0;i<list.length();i++) {
                                postJSONList.add(list.getJSONObject(i));
                            }
                            //adapter.notifyDataSetChanged();


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
