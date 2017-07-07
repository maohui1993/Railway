package cn.dazhou.im.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
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

    private byte state;
    private int position;

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
        startPlay(uri, null);
    }

    /**
     * 播放视频
     *
     * @param uri
     * @param surfaceView
     * @throws IOException
     */
    public void startPlay(Uri uri, final SurfaceView surfaceView) throws IOException {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setDataSource(mContext, uri);
        mMediaPlayer.setDisplay(surfaceView.getHolder());
        mMediaPlayer.prepare(); // might take long! (for buffering, etc)
        mMediaPlayer.start();
        changeState(PLAYING);

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                changeState(STOPPED);
            }
        });
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

    /**
     *
     * @return 执行方法后的状态
     */
    public byte suspendOrRestart() {
        if (Utils.checkNotNull(mMediaPlayer)) {
            switch (state) {
                case PLAYING :
                    mMediaPlayer.pause();
                    position = mMediaPlayer.getCurrentPosition();
                    changeState(PAUSED);
                    break;
                case PAUSED :
//                    mMediaPlayer.seekTo(position);
                    mMediaPlayer.start();
                    changeState(PLAYING);
                    break;
                case STOPPED :
                    mMediaPlayer.seekTo(0);
                    mMediaPlayer.start();
                    changeState(PLAYING);
                    break;
            }
        }
        return state;
    }

    private void changeState(byte state) {
        this.state = state;
    }
}
