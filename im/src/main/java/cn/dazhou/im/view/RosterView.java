package cn.dazhou.im.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.dazhou.im.R;
import cn.dazhou.im.R2;
import cn.dazhou.im.view.adapter.RosterAdapter;

/**
 * Created by hooyee on 2017/5/8.
 */

public class RosterView extends LinearLayout{
    @BindView(R2.id.lv_roster)
    ListView mRosterLv;
    RosterAdapter mAdapter;
    public RosterView(Context context) {
        this(context, null);
    }

    public RosterView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RosterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.roster_view, this);
        ButterKnife.bind(this);
        mAdapter = new RosterAdapter(context, null);
        mRosterLv.setAdapter(mAdapter);
    }

    public void updateRosterData(List datas) {
        mAdapter.updateDatas(datas);
    }
}
