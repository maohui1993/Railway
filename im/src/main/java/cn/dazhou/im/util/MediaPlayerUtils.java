package cn.dazhou.im.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

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
    private int position;

    private Uri dataUri;
    private SurfaceHolder surfaceHolder;

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
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mMediaPlayer.setDataSource(mContext, dataUri);
                    mMediaPlayer.setDisplay(surfaceHolder);
                    mMediaPlayer.prepare(); // might take long! (for buffering, etc)
                    mMediaPlayer.start();
                    mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            changeState(STOPPED);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case PAUSED:
                if (!Utils.checkNotNull(mMediaPlayer)) {
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
}
