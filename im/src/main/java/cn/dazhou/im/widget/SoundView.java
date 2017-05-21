package cn.dazhou.im.widget;

import android.content.Context;
import android.os.Environment;
import android.util.AttributeSet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by hooyee on 2017/5/10.
 */

public class SoundView extends android.support.v7.widget.AppCompatImageView {

    private String voicePath;
    private int mType;   // 与ChatMsgEntry中 的type一样

    public SoundView(Context context) {
        this(context, null);
    }

    public SoundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public String getVoicePath() {
        return voicePath;
    }

    public void setVoicePath(String voicePath) {
        this.voicePath = voicePath;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public int getType() {
        return mType;
    }
}
