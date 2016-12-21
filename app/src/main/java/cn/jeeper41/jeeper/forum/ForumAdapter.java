package cn.jeeper41.jeeper.forum;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jeeper41.jeeper.AsyncImageLoader;
import cn.jeeper41.jeeper.R;

import static android.content.ContentValues.TAG;

/**
 * Created by joker on 2016/11/15.
 */

public class ForumAdapter extends BaseAdapter {

    private AsyncImageLoader asyncImageLoader;
    private List<JSONObject> data;
    private LayoutInflater layoutInflater;
    private Context context;
    private ListView forumListView;
    private Map<String,Integer> forumIcons = new HashMap<String,Integer>();


    public ForumAdapter(Context context, ListView forumListView, List<JSONObject> data){
        this.context=context;
        this.forumListView = forumListView;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);
        asyncImageLoader = new AsyncImageLoader();

        forumIcons.put("5",R.drawable.forumicon);
        forumIcons.put("6",R.drawable.forumicon);
        forumIcons.put("7",R.drawable.forumicon);
        forumIcons.put("8",R.drawable.forumicon);
        forumIcons.put("9",R.drawable.forumicon);
        forumIcons.put("10",R.drawable.forumicon);
        forumIcons.put("11",R.drawable.forumicon);
        forumIcons.put("12",R.drawable.forumicon);
        forumIcons.put("13",R.drawable.forumicon);
        forumIcons.put("14",R.drawable.forumicon);
        forumIcons.put("15",R.drawable.forumicon);
        forumIcons.put("16",R.drawable.forumicon);
        forumIcons.put("17",R.drawable.forumicon);
        forumIcons.put("18",R.drawable.forumicon);
        forumIcons.put("19",R.drawable.forumicon);
        forumIcons.put("20",R.drawable.forumicon);
        forumIcons.put("21",R.drawable.forumicon);
        forumIcons.put("22",R.drawable.forumicon);
        forumIcons.put("23",R.drawable.forumicon);
        forumIcons.put("24",R.drawable.forumicon);
        forumIcons.put("25",R.drawable.forumicon);
        forumIcons.put("26",R.drawable.forumicon);
    }

    public final class ForumeView{
        public ImageView image;
        public TextView title;
        public TextView forumCount;
    };

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ForumeView view = null;
        //绑定数据
        try {
            JSONObject jo = data.get(position);

            if(convertView==null){
                //获得组件，实例化组件
                view = new ForumeView();
                convertView = layoutInflater.inflate(R.layout.forum_group_item, null);
                view.image = (ImageView) convertView.findViewById(R.id.ivForumIcon);
                view.title = (TextView) convertView.findViewById(R.id.tvForumName);
                view.forumCount = (TextView) convertView.findViewById(R.id.tvForumCount);
                convertView.setTag(view);
            }else{
                view=(ForumeView)convertView.getTag();
            }



            if(jo.has("forumid")) {
                view.image.setVisibility(View.VISIBLE);
                view.forumCount.setVisibility(View.VISIBLE);
                view.title.setText(jo.getString("forumname"));
                view.forumCount.setText(context.getString(R.string.FORUM_TOPIC_COUNT) + jo.getString("topiccount") + "" +
                        "    " + context.getString(R.string.FORUM_POST_COUNT) + jo.getString("postcount"));
                if (forumIcons.containsKey(jo.getString("forumid"))) {
                    view.image.setImageResource(forumIcons.get(jo.getString("forumid")));
                } else {
                    view.image.setImageResource(R.drawable.forumicon);
                }
            }
            else{
                view.image.setVisibility(View.GONE);
                view.forumCount.setVisibility(View.GONE);
                view.title.setText(jo.getString("groupname"));
                view.title.setTextSize(20);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }

    /**
     * 从服务器取图片
     *http://bbs.3gstdy.com
     * @param url
     * @return
     */
    public static Bitmap getHttpBitmap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            Log.d(TAG, url);
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setConnectTimeout(0);
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
