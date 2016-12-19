package cn.jeeper41.jeeper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.jeeper41.jeeper.config.Global;
import cn.jeeper41.jeeper.entity.User;
import cn.jeeper41.jeeper.entity.UserApplication;
import cn.jeeper41.jeeper.service.RequestHandler;

public class LoginActivity extends Activity implements OnClickListener{
    private UserApplication Userapp;
    private Button btn_login;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_login = (Button) this.findViewById(R.id.btnLogin);

        layout=(LinearLayout)findViewById(R.id.pop_layout);

        //添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity
        layout.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",
                        Toast.LENGTH_SHORT).show();
            }
        });
        //添加按钮监听
        btn_login.setOnClickListener(this);
    }

    //实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event){
        finish();
        return true;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                if(isValid()){
                    loginAuthentication();
                }
                else
                    Toast.makeText(this,getString(R.string.LOGIN_INVALID),Toast.LENGTH_SHORT).show();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;
            default:
                break;
        }
        //finish();
    }
    //用户名密码非空
    private boolean isValid(){
        final String usn=((EditText)findViewById(R.id.etUsername)).getText().toString();
        final String psw=((EditText)findViewById(R.id.etPassword)).getText().toString();
        if(usn==null||psw==null||usn.length()<=0||psw.length()<=0)
            return false;
        else
            return true;
    }
//数据库连接，并显示等待
    private void loginAuthentication(){
        final String usn=((EditText)findViewById(R.id.etUsername)).getText().toString();
        final String psw=((EditText)findViewById(R.id.etPassword)).getText().toString();
        class Testconn extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LoginActivity.this,"Logging in...","Please Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(saveLogin(s)) //退出后就没了
                    finish();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put("username",usn);
                params.put("password",psw);
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Global.AUTHENTICATION_URL, params);
                return res;
            }
        }
        Testconn test=new Testconn();
        test.execute();
    }

    private boolean saveLogin(String JSON){
        JSONObject jsonObject = null;
        try{
            jsonObject = new JSONObject(JSON);
            JSONArray result = jsonObject.getJSONArray("result");
            JSONObject jo = result.getJSONObject(0);
            //SharedPreferences sp = getSharedPreferences("userprofile", Context.MODE_PRIVATE);
            //SharedPreferences.Editor editor = sp.edit();
            //把数据进行保存
            //如果选择了记住我则写入
            //editor.putString("saveduserid", jo.getString("userid"));
            //提交数据
            //editor.commit();
            User us=new User(jo.getString("userid"),jo.getString("displayname"));
            Toast.makeText(LoginActivity.this,"Willkommen "+us.getDisplayname(),Toast.LENGTH_LONG).show();
            Userapp=(UserApplication) getApplication();
            Userapp.setUser(us);
            return true;
        }
        catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),getString(R.string.LOGIN_INVALID),Toast.LENGTH_SHORT).show();
            return false;
        }

    }

}