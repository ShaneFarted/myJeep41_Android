package cn.jeeper41.jeeper.forum;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import cn.jeeper41.jeeper.AsyncImageLoader;
import cn.jeeper41.jeeper.R;

import static android.content.ContentValues.TAG;
import static cn.jeeper41.jeeper.R.string.FORUM_AUTHOR;
import static cn.jeeper41.jeeper.R.string.FORUM_OPENNUM;
import static java.security.AccessController.getContext;

/**
 * Created by Shane on 2016-12-18.
 */

public class PostListAdapter extends BaseAdapter{

    private AsyncImageLoader asyncImageLoader;
    private List<JSONArray> data;
    private LayoutInflater layoutInflater;
    private Context context;
    private ListView postChooseView;

    public PostListAdapter(Context context, ListView postChooseView, List<JSONArray> data){
        this.context=context;
        this.postChooseView = postChooseView;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);
        asyncImageLoader = new AsyncImageLoader();
    }

    public final class PostListView{
        public ImageView image;
        public TextView postTopic;
        public TextView postAuthor;
        public TextView postCommentNum;
    };

    @Override
    public int getCount() {
        if(data.size() == 1){
            return data.get(0).length();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        try {
            return data.get(0).get(0);
        } catch (JSONException e) {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PostListAdapter.PostListView view = null;
        if(convertView==null){
            //获得组件，实例化组件
            view = new PostListAdapter.PostListView();
            convertView=layoutInflater.inflate(R.layout.forum_post_item, null);
            //先不要图片，后期可加上帖子图片预览，效果如百度贴吧view.image=(ImageView)convertView.findViewById(R.id.ivForumIcon);
            view.postTopic=(TextView)convertView.findViewById(R.id.tvPostTopic);
            view.postAuthor=(TextView)convertView.findViewById(R.id.tvPostAuthor);
            view.postCommentNum=(TextView)convertView.findViewById(R.id.tvPostCommentNum);
            convertView.setTag(view);
        }else{
            view=(PostListAdapter.PostListView)convertView.getTag();
        }
        //String url = "http://img1.imgtn.bdimg.com/it/u=2099421159,497813704&fm=23&gp=0.jpg";
        //view.image.setTag(url);
        //Drawable cachedImage = asyncImageLoader.loadDrawable(url, new AsyncImageLoader.ImageCallback() {
        //    public void imageLoaded(Drawable imageDrawable, String imageUrl) {
        //ImageView imageViewByTag = (ImageView) postChooseView.findViewWithTag(imageUrl);
        //        if (imageViewByTag != null) {
        //            imageViewByTag.setImageDrawable(imageDrawable);
        //        }
        //    }
        //});
        //if (cachedImage == null) {
            //view.image.setImageResource(R.drawable.hello);
        //}else{
        //    view.image.setImageDrawable(cachedImage);
        //}
        //绑定数据
        try {
            if(data.size() == 1) {
                JSONObject jo = data.get(0).getJSONObject(position);
                view.postTopic.setText(jo.getString("topicname"));
                view.postAuthor.setText(context.getString(R.string.FORUM_AUTHOR)+jo.getString("userid"));
                view.postCommentNum.setText(context.getString(R.string.FORUM_OPENNUM)+jo.getString("postcount"));
            }
        } catch (JSONException e) {
            view.postTopic.setText("获取失败!");
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
