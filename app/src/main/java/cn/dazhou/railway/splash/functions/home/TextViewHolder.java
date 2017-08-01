package cn.dazhou.railway.splash.functions.home;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import cn.dazhou.railway.R;

/**
 * Created by hooyee on 2017/8/1.
 */

public class TextViewHolder extends BaseViewHolder<String> {
    private TextView mText;

    public TextViewHolder(View itemView) {
        super((ViewGroup) itemView, R.layout.notice_view);
        mText = $(R.id.tx_notice_name);
    }

    @Override
    public void setData(String value){
        mText.setText(value);
    }

    public TextView getView() {
        return mText;
    }
}
