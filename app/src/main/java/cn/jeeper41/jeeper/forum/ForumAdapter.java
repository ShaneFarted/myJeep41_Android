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
import java.util.List;

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

    public ForumAdapter(Context context, ListView forumListView, List<JSONObject> data){
        this.context=context;
        this.forumListView = forumListView;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);
        asyncImageLoader = new AsyncImageLoader();
    }

    public final class ForumeView{
        public ImageView image;
        public TextView title;
        public Boolean header;
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
        if(convertView==null){
            //获得组件，实例化组件
            view = new ForumeView();
            convertView = layoutInflater.inflate(R.layout.forum_group_item, null);
            //view.image = (ImageView) convertView.findViewById(R.id.ivForumIcon);
            view.title = (TextView) convertView.findViewById(R.id.tvForumName);
            convertView.setTag(view);
        }else{
            view=(ForumeView)convertView.getTag();
        }
       /* String url = "http://img1.imgtn.bdimg.com/it/u=2099421159,497813704&fm=23&gp=0.jpg";
        view.image.setTag(url);
        Drawable cachedImage = asyncImageLoader.loadDrawable(url, new AsyncImageLoader.ImageCallback() {
            public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                ImageView imageViewByTag = (ImageView) forumListView.findViewWithTag(imageUrl);
                if (imageViewByTag != null) {
                    imageViewByTag.setImageDrawable(imageDrawable);
                }
            }
        });
        if (cachedImage == null) {
            view.image.setImageResource(R.drawable.hello);
        } else {
            view.image.setImageDrawable(cachedImage);
        }*/
        //绑定数据
        try {
            view.title.setText(data.get(position).getString("forumname"));
        }catch (Exception e){

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
