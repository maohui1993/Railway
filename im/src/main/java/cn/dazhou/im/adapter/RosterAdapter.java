package cn.dazhou.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.jivesoftware.smack.roster.RosterEntry;

import java.util.ArrayList;
import java.util.List;

import cn.dazhou.im.R;

/**
 * Created by hooyee on 2017/5/8.
 */

public class RosterAdapter extends BaseAdapter {
    private List<RosterEntry> mDatas;
    private Context mContext;

    public RosterAdapter(Context context, List<RosterEntry> datas) {
        mContext = context;
        mDatas = datas;
        if (datas == null) {
            mDatas = new ArrayList<>();
        }
    }

    public void updateDatas(List<RosterEntry> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.roster_item, null);
            holder.textView = (TextView) convertView.findViewById(R.id.item1);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(mDatas.get(position).getName());
        return convertView;
    }

    public static class ViewHolder {
        public TextView textView;
    }
}
