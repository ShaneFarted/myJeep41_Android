package cn.jeeper41.jeeper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.jeeper41.jeeper.config.Global;
import cn.jeeper41.jeeper.service.RequestHandler;

/**
 * Created by Shane on 2016-12-14.
 */

public class ForumActivity extends AppCompatActivity implements ListView.OnItemClickListener{
    private ListView listView;
    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_choose);
        listView = (ListView) findViewById(R.id.lvForumChoose);
        listView.setOnItemClickListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getInitJSON();

    }

    private void getInitJSON(){
        class Testconn extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ForumActivity.this,"Please Wait...","Initializing Forum...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING=s;
                showForumGroups();
            }

            @Override
            protected String doInBackground(Void... v) {
                RequestHandler rh = new RequestHandler();
                String res = rh.sendGetRequest(Global.FORUM_URL);
                return res;
            }
        }
        Testconn test=new Testconn();
        test.execute();
    }

    private void showForumGroups(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray("result");

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString("groupid");
                String name = jo.getString("groupname");

                HashMap<String,String> forumgroupmap = new HashMap<>();
                forumgroupmap.put("groupid",id);
                forumgroupmap.put("groupname",name);
                list.add(forumgroupmap);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                ForumActivity.this, list, R.layout.forum_group_item,
                new String[]{"groupid","groupname"},
                new int[]{R.id.tvForumId, R.id.tvForumName});

        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, MainActivity.class);  //应该为具体板块内的activity
        HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
        String empId = map.get("groupid").toString();
        intent.putExtra("forum_id", empId);
        startActivity(intent);
    }
}
