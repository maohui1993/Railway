package cn.dazhou.im.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * Created by hooyee on 2017/7/6.
 */

public class MediaPlayerUtils {
    public static final byte PLAYING = 0x01;
    public static final byte STOPPED = 0x02;
    public static final byte PAUSED = 0x03;
    private MediaPlayer mMediaPlayer;
    private Context mContext;

    private byte state = STOPPED;

    private Uri dataUri;
    private SurfaceHolder surfaceHolder;

    private MediaPlayer.OnCompletionListener mListener;

    public MediaPlayerUtils(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 播放音频
     *
     * @param uri
     * @throws IOException
     */
    public void startPlay(Uri uri) throws IOException {
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
        mListener = listener;
    }

    public void stopPlay() {
        if (Utils.checkNotNull(mMediaPlayer)) {
            mMediaPlayer.stop();
        }
    }

    public void release() {
        if (Utils.checkNotNull(mMediaPlayer)) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        state = STOPPED;
    }

    public void changeState(byte state) {
        this.state = state;
        executeState();
    }

    public void executeState() {
        switch (state) {
            case PLAYING:
                if (mMediaPlayer != null) {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    mMediaPlayer = null;
                }
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setOnCompletionListener(mListener);
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mMediaPlayer.setDataSource(mContext, dataUri);
                    mMediaPlayer.setDisplay(surfaceHolder);
                    mMediaPlayer.prepare(); // might take long! (for buffering, etc)
                    mMediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case PAUSED:
                if (Utils.checkNotNull(mMediaPlayer)) {
                    mMediaPlayer.pause();
                }
                break;
            case STOPPED:
                if (Utils.checkNotNull(mMediaPlayer)) {
                    mMediaPlayer.seekTo(0);
                    mMediaPlayer.stop();
                }
                break;
        }
    }

    public byte getState() {
        return state;
    }

    public void setDataUri(Uri dataUri) {
        this.dataUri = dataUri;
    }

    public void setSurfaceHolder(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }

    // 获取视频缩略图
    public static Bitmap getVideoThumbnail(String filePath) {
        Bitmap b = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            b = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();

        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return b;
    }
}
