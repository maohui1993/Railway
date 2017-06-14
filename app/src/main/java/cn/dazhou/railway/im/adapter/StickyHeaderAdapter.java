package cn.dazhou.railway.im.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.decoration.StickyHeaderDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import cn.dazhou.railway.R;
import cn.dazhou.railway.im.db.FriendModel;

/**
 * Created by hooyee on 2017/06/08.
 */
public class StickyHeaderAdapter implements StickyHeaderDecoration.IStickyHeaderAdapter<StickyHeaderAdapter.HeaderHolder> {

    private LayoutInflater mInflater;
    private List<FriendModel> datas;

    private List<Character> referenceHeaderId;

    private char[] header = {
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '*'
    };

    public StickyHeaderAdapter(Context context, List<FriendModel> allData) {
        mInflater = LayoutInflater.from(context);
        datas = allData;
        initHeader();
    }

    private void initHeader() {
        referenceHeaderId = new ArrayList<>();
        for (char c : header) {
            referenceHeaderId.add(c);
        }
    }

    @Override
    public long getHeaderId(int position) {
        if (position < datas.size()) {
            char firstChar = datas.get(position).getName().toLowerCase().charAt(0);

            int i = 0;
            for (; i < referenceHeaderId.size(); i++) {
                if (referenceHeaderId.get(i) == firstChar) {
                    return i;
                }
            }
        }
        return referenceHeaderId.size() - 1;

    }

    @Override
    public HeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
        final View view = mInflater.inflate(R.layout.group_item, parent, false);
        return new HeaderHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderHolder viewHolder, int position) {
        viewHolder.header.setText(referenceHeaderId.get((int)getHeaderId(position)).toString());
    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        public TextView header;

        public HeaderHolder(View itemView) {
            super(itemView);
            header = (TextView) itemView;
        }
    }
}
