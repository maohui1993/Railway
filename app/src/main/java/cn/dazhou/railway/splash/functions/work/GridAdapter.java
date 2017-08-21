package cn.dazhou.railway.splash.functions.work;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.dazhou.database.FunctionItemModel;
import cn.dazhou.railway.R;

/**
 * Created by hooyee on 2017/6/20.
 */

public class GridAdapter extends BaseAdapter {
    private int resId;
    private LayoutInflater inflater;
    List<FunctionItemModel> datas;
    Context context;

    public GridAdapter(Context context, List<FunctionItemModel> data, int res) {
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
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.text = (TextView) convertView.findViewById(R.id.text);

            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        holder.text.setText(datas.get(position).getFunctionname());
        Glide.with(context)
                .load(datas.get(position).getIconUrl())
                .placeholder(R.drawable.ic_launcher)
                .into(holder.image);
        return convertView;
    }

    public List<FunctionItemModel> getAllData() {
        return datas;
    }

    class ViewHolder {
        TextView text;
        ImageView image;
    }
}
