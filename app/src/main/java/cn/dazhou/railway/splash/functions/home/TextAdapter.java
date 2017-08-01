package cn.dazhou.railway.splash.functions.home;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hooyee on 2017/8/1.
 */

public class TextAdapter extends RecyclerArrayAdapter<String> {
    List<TextViewHolder> viewHolders = new ArrayList<>();

    public TextAdapter(Context context, List<String> objects) {
        super(context, objects);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        TextViewHolder viewHolder = new TextViewHolder(parent);
        viewHolders.add(viewHolder);
        return viewHolder;
    }

    public List<TextViewHolder> getViewHolders() {
        return viewHolders;
    }
}
