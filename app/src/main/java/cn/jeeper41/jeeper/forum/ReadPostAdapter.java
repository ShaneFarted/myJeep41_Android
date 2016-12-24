package cn.jeeper41.jeeper.forum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.jeeper41.jeeper.R;

/**
 * Created by Shane on 2016-12-20.
 */

public class ReadPostAdapter extends BaseAdapter{
    private List<JSONObject> objectList = new LinkedList<JSONObject>();//数据集合
    private Context mContext;

    public final static int TYPE_MAINPOST = 0;
    public final static int TYPE_REPLYPOST = 1;


    public ReadPostAdapter(Context context, List<JSONObject> data) throws JSONException {
        super();
        this.objectList=data;
        this.mContext=context;
    }

    /**
     * 主贴内容
     * @author shane
     *
     */
    class mainPostHolder {
        TextView tvMainpostAuthor;
        TextView tvMainpostTitle;
        TextView tvMainpostContent;
    }

    /**
     * 回复贴内容
     * @author Shane
     *
     */
    class replyPostHolder {
        TextView tvReplyAuthor;
        TextView tvReplyContent;
        TextView tvReplyIndex;
    }

    public Object getItem(int position) {
        return objectList.get(position);
    }


    @Override
    public int getCount() {
        return objectList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mpView = null;
        View rpView = null;
        int currentType = getItemViewType(position);
        if (currentType == TYPE_MAINPOST) {
            mainPostHolder Holder = null;
            if (convertView == null) {
                Holder = new mainPostHolder();
                mpView = LayoutInflater.from(mContext).inflate(
                        R.layout.forum_mainpost_item, null);
                Holder.tvMainpostAuthor = (TextView) mpView
                        .findViewById(R.id.tvMainpostAuthor);
                Holder.tvMainpostContent = (TextView) mpView
                        .findViewById(R.id.tvMainpostContent);
                Holder.tvMainpostTitle = (TextView) mpView
                        .findViewById(R.id.tvMainpostTitle);
                mpView.setTag(Holder);
                convertView = mpView;
            } else {
                Holder = (mainPostHolder) convertView.getTag();
            }
            try {
                Holder.tvMainpostAuthor.setText(objectList.get(position).getString("displayname"));
                Holder.tvMainpostContent.setText(objectList.get(position).getString("postcontent"));
                Holder.tvMainpostTitle.setText(objectList.get(position).getString("topicname"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (currentType == TYPE_REPLYPOST) {
            replyPostHolder rpHolder = null;
            if (convertView == null) {
                rpHolder = new replyPostHolder();
                rpView = LayoutInflater.from(mContext).inflate(
                        R.layout.forum_reply_item, null);
                rpHolder.tvReplyAuthor = (TextView) rpView
                        .findViewById(R.id.tvReplyAuthor);
                rpHolder.tvReplyContent = (TextView) rpView
                        .findViewById(R.id.tvReplyContent);
                rpHolder.tvReplyIndex = (TextView) rpView
                        .findViewById(R.id.tvReplyIndex);
                rpView.setTag(rpHolder);
                convertView = rpView;
            } else {
                rpHolder = (replyPostHolder) convertView.getTag();
            }
            try {
                rpHolder.tvReplyAuthor.setText(objectList.get(position).getString("displayname"));
                rpHolder.tvReplyContent.setText(objectList.get(position).getString("postcontent"));
                rpHolder.tvReplyIndex.setText(objectList.get(position).getString("postindex"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return convertView;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0) {
            return TYPE_MAINPOST;// 主贴
        } else  {
            return TYPE_REPLYPOST;// 回复贴
        }

    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }
}
