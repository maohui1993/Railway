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
    private static String DIR_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;

    private byte[] mSoundByte;
    private File mSoundFile;


    public SoundView(Context context) {
        this(context, null);
    }

    public SoundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public byte[] getSoundByte() {
        return mSoundByte;
    }

    public void setSoundByte(byte[] mSoundByte) {
        this.mSoundByte = mSoundByte;
        initSoundDataSource();
    }

    public File getSoundFile() {
        return mSoundFile;
    }

    private void initSoundDataSource() {
        mSoundFile  = new File(DIR_PATH + System.currentTimeMillis() + ".ogg");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(mSoundFile);
            out.write(mSoundByte);
            out.flush();
            if (!mSoundFile.exists()) {
                mSoundFile.createNewFile();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
