package cn.dazhou.railway.splash.functions.contact;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.decoration.StickyHeaderDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.dazhou.database.FriendModel;
import cn.dazhou.railway.R;

/**
 * Created by hooyee on 2017/06/08.
 */
public class StickyHeaderAdapter implements StickyHeaderDecoration.IStickyHeaderAdapter<StickyHeaderAdapter.HeaderHolder> {

    private LayoutInflater mInflater;
    private List<FriendModel> datas;

    private char[] header = {
        'N','a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '*'
    };

    private String[] reference = {
        "新消息","A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L","M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "*"
    };

    public StickyHeaderAdapter(Context context, List<FriendModel> allData) {
        mInflater = LayoutInflater.from(context);
        datas = allData;
    }

    @Override
    public long getHeaderId(int position) {
        if (position < datas.size()) {
            char firstChar = datas.get(position).getName().toLowerCase().charAt(0);
            // 如果有新消息
            if (datas.get(position).isHasNewMsg()) {
                return 0;
            }
            int i = 0;
            for (; i < header.length; i++) {
                if (header[i] == firstChar) {
                    return i;
                }
            }
        }
        return header.length - 1;

    }

    @Override
    public HeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
        final View view = mInflater.inflate(R.layout.group_item, parent, false);
        return new HeaderHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderHolder viewHolder, int position) {
        viewHolder.header.setText(reference[(int)getHeaderId(position)]);
    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        public TextView header;

        public HeaderHolder(View itemView) {
            super(itemView);
            header = (TextView) itemView;
        }
    }
}
