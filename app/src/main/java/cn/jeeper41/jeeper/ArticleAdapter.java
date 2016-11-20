package cn.jeeper41.jeeper;

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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import cn.jeeper41.jeeper.entity.Article;

import static android.content.ContentValues.TAG;

/**
 * Created by joker on 2016/11/15.
 */

public class ArticleAdapter extends BaseAdapter {

    private AsyncImageLoader asyncImageLoader;
    private List<Article> data;
    private LayoutInflater layoutInflater;
    private Context context;
    private ListView articleList;

    public ArticleAdapter(Context context,ListView articleList,List<Article> data){
        this.context=context;
        this.articleList = articleList;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);
        asyncImageLoader = new AsyncImageLoader();
    }

    public final class ArticleView{
        public ImageView image;
        public TextView title;
        public TextView desc;
        public TextView comment;
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
        ArticleView view = null;
        if(convertView==null){
            //获得组件，实例化组件
            view = new ArticleView();
            convertView=layoutInflater.inflate(R.layout.article_item, null);
            view.image=(ImageView)convertView.findViewById(R.id.article_brand);
            view.title=(TextView)convertView.findViewById(R.id.article_title);
            view.desc=(TextView)convertView.findViewById(R.id.article_desc);
            view.comment = (TextView) convertView.findViewById(R.id.article_comment);
            convertView.setTag(view);
        }else{
            view=(ArticleView)convertView.getTag();
        }
        String url = (String) data.get(position).getBrand();
        view.image.setTag(url);
        Drawable cachedImage = asyncImageLoader.loadDrawable(url, new AsyncImageLoader.ImageCallback() {
            public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                ImageView imageViewByTag = (ImageView) articleList.findViewWithTag(imageUrl);
                if (imageViewByTag != null) {
                    imageViewByTag.setImageDrawable(imageDrawable);
                }
            }
        });
        if (cachedImage == null) {
            view.image.setImageResource(R.drawable.hello);
        }else{
            view.image.setImageDrawable(cachedImage);
        }
        //绑定数据
        view.title.setText((String)data.get(position).getTitle());
        view.desc.setText((String)data.get(position).getDesc());
        view.comment.setText((String)data.get(position).getComment());
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
