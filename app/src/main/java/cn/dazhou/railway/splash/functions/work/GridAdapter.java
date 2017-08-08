package cn.dazhou.railway.splash.functions.work;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import cn.dazhou.railway.R;
import cn.dazhou.railway.util.Tool;

/**
 * Created by hooyee on 2017/6/20.
 */

public class GridAdapter extends BaseAdapter {
    private int resId;
    private LayoutInflater inflater;
    List<Item> datas;
    Context context;

    public GridAdapter(Context context, List<Item> data, int res) {
        this.context = context;
        resId = res;
        datas = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        //加载布局为一个视图
        if(convertView == null) {
            convertView = inflater.inflate(resId, null);
            holder = new ViewHolder();
            holder.image = (ImageButton) convertView.findViewById(R.id.image);
            ViewGroup.LayoutParams params = holder.image.getLayoutParams();
            params.height = Tool.dip2px(context, params.height);
            params.width = Tool.dip2px(context, params.width);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        holder.text.setText(datas.get(position).text);
        holder.image.setImageResource(datas.get(position).imageRes);
        return convertView;
    }

    class ViewHolder {
        TextView text;
        ImageButton image;
    }

    public static class Item {
        int imageRes;
        String text;

        public Item(int imageRes, String text) {
            this.imageRes = imageRes;
            this.text = text;
        }
    }
}
