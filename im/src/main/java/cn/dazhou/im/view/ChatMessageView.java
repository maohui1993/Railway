package cn.dazhou.im.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.dazhou.im.R;
import cn.dazhou.im.R2;

/**
 * Created by hooyee on 2017/5/9.
 */

public class ChatMessageView extends RelativeLayout {
    @BindView(R2.id.tx_info_item)
    TextView mMsgText;
    @BindView(R2.id.image_info_item)
    ImageView mMsgImage;

    public ChatMessageView(Context context) {
        this(context, null);
    }

    public ChatMessageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatMessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.chat_message_view, this);
        ButterKnife.bind(this);
    }

    public void setText(String text) {
        mMsgText.setText(text);
        mMsgText.setVisibility(View.VISIBLE);
    }

    public void setImage(Bitmap bmp) {
        mMsgImage.setImageDrawable(new BitmapDrawable(null, bmp));
        mMsgImage.setVisibility(View.VISIBLE);
    }
}
