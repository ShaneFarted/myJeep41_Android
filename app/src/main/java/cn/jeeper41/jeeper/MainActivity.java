package cn.jeeper41.jeeper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.jeeper41.jeeper.about.AboutUsActivity;
import cn.jeeper41.jeeper.blog.ArticleDetailActivity;
import cn.jeeper41.jeeper.entity.Article;
import cn.jeeper41.jeeper.entity.UserApplication;
import cn.jeeper41.jeeper.forum.ForumActivity;
import cn.jeeper41.jeeper.service.ArticleCallBack;
import cn.jeeper41.jeeper.service.ArticleService;
import cn.jeeper41.jeeper.wiget.RefreshListView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RefreshListView articleView = null;
    private Context context = this;
    private List<Article> articleList = new ArrayList<Article>();
    private Integer currPageIndex = 1;
    private Handler handler;
    private UserApplication Userapp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
//边栏选项监听器
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//点击头像弹出登录框
        View headerView = navigationView.getHeaderView(0);
        ImageView Login = (ImageView) headerView.findViewById(R.id.ivPortrait);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });
        // init view
        articleView = (RefreshListView) findViewById(R.id.articleList);

        articleView.setAdapter(new ArticleAdapter(context,articleView, articleList));

        // set onclick even
        articleView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ArticleDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("article", articleList.get(position-1));
                intent.putExtra("article", bundle);
                startActivity(intent);
            }
        });
        // refreash listener
        articleView.setOnRefreashListener(new RefreshListView.OnRefreashListener() {
            @Override
            public void onRefreash() {
                currPageIndex = 1;
                loadMoreData(currPageIndex.toString());
            }

            @Override
            public void onLoadMore() {
                currPageIndex++;
                loadMoreData(currPageIndex.toString());
            }
        });

        handler = new Handler(getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        articleView.onRefreashComplete();
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
        articleView.refresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Userapp=(UserApplication)getApplication();
        TextView textView = (TextView)findViewById(R.id.tvCurrentUsername);
        if(Userapp.getUser().getUserid().length()>0){
            textView.setText(Userapp.getUser().getDisplayname().toString());
        }
    }
    /**
     * laod more data
     * @param pageIndex
     */
    public void loadMoreData(final String pageIndex){
        new ArticleService().getArticle(pageIndex, new ArticleCallBack() {
            @Override
            public void onFinish(List<Article> list) {
                if(list != null){
                    if("0".equals(pageIndex)){
                        articleList.clear();
                    }
                    articleList.addAll(list);
                }else{
                    handler.sendEmptyMessage(2);
                }
                handler.sendEmptyMessage(1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about_us) {
            // Handle the camera action
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, AboutUsActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_forum) {
            // Handle the camera action
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, ForumActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
