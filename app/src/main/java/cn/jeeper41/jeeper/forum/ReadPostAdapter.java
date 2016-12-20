package cn.jeeper41.jeeper.forum;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.jeeper41.jeeper.R;

/**
 * Created by Shane on 2016-12-20.
 */

public class ReadPostAdapter extends BaseAdapter{
    public final Map<String, Adapter> sections = new LinkedHashMap<String, Adapter>();
    public final ArrayAdapter<String> headers;
    public final static int TYPE_SECTION_HEADER = 0;

    public ReadPostAdapter(Context context) {
        headers = new ArrayAdapter<String>(context, R.layout.forum_header, R.id.list_header_title);
    }

    public Object getItem(int position) {
        for (Object section : this.sections.keySet()) {
            Adapter adapter = sections.get(section);
            int size = adapter.getCount() + 1;

            // check if position inside this section
            if (position == 0)
                return section;
            if (position < size)
                return adapter.getItem(position - 1);

            // otherwise jump into next section
            position -= size;
        }
        return null;
    }


    @Override
    public int getCount() {
        // total together all sections, plus one for each section header
        int total = 0;
        for (Adapter adapter : this.sections.values())
            total += adapter.getCount() + 1;
        return total;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int sectionnum = 0;
        for (Object section : this.sections.keySet()) {
            Adapter adapter = sections.get(section);
            int size = adapter.getCount() + 1;

            // check if position inside this section
            if (position == 0)
                return headers.getView(sectionnum, null, parent);
            if (position < size)
                return adapter.getView(position - 1, null, parent);
            // otherwise jump into next section
            position -= size;
            sectionnum++;
        }
        return null;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
}
