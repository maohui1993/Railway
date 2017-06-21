package cn.dazhou.railway.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.dazhou.railway.R;

/**
 * Created by hooyee on 2017/6/15.
 */

public class MultiText extends LinearLayout {
    private TextView mLeftText;
    private TextView mRightText;
    private ImageView mRightImage;

    public MultiText(Context context) {
        this(context, null);
    }

    public MultiText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.multi_text, this, true);
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.MultiText);
        init(typeArray);
    }

    public void setText(String text) {
        mRightText.setText(text);
    }

    public String getText() {
        return mRightText.getText().toString();
    }

    private void init(TypedArray typeArray) {
        mLeftText = (TextView) findViewById(R.id.tx_left);
        mRightText = (TextView) findViewById(R.id.tx_right);
        mRightImage = (ImageView) findViewById(R.id.ig_right);
        String textLeft = typeArray.getString(R.styleable.MultiText_leftText);
        String textRight = typeArray.getString(R.styleable.MultiText_rightText);
        int imageResId = typeArray.getResourceId(R.styleable.MultiText_rightImage, -1);

        mLeftText.setText(textLeft);
        mRightText.setText(textRight);
        if (imageResId != -1) {
            mRightImage.setImageResource(imageResId);
            mRightImage.setVisibility(VISIBLE);
            mRightText.setVisibility(GONE);
        }
    }

}
