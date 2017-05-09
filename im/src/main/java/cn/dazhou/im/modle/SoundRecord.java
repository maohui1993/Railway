package cn.dazhou.im.modle;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by hooyee on 2017/5/9.
 */

public class SoundRecord {

    private String tmpPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    private MediaRecorder mMediaRecorder;
    private MediaPlayer mPlayer;

    private long mRecordTime;

    public String getTmpPath() {
        return tmpPath;
    }

    public long getRecordTime() {
        return mRecordTime;
    }

    public void startRecording() {
        tmpPath = tmpPath + System.currentTimeMillis() + ".ogg";
        if (mMediaRecorder != null) {
            mMediaRecorder.release();
        }
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mMediaRecorder.setOutputFile(tmpPath);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mMediaRecorder.prepare();
            mRecordTime = System.currentTimeMillis();
        } catch (IOException e) {
            Log.i("ringtone", "prepare() failed");
        }

        mMediaRecorder.start();
    }

    public void stopRecording() {

        if (mMediaRecorder == null) {
            return;
        }
        mMediaRecorder.stop();
        mMediaRecorder.release();
        mMediaRecorder = null;
        mRecordTime = System.currentTimeMillis() - mRecordTime;
    }

    public byte[] getSoundRecord() {
        File file = new File(tmpPath);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        FileInputStream in = null;
        byte[] bytes = new byte[1024];
        try {
            in = new FileInputStream(file);
            int length = 0;
            while((length = in.read(bytes)) != -1) {
                out.write(bytes, 0, length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return out.toByteArray();
    }

    public void startPlaying(String path) {
        if (mPlayer != null) {
            mPlayer.release();
        }
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(path);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.i("ringtone", "prepare() failed");
        }
    }

    public void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(tmpPath);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.i("ringtone", "prepare() failed");
        }
    }

    private void stopPlaying() {
        if (mPlayer == null) {
            return;
        }
        mPlayer.release();
        mPlayer = null;
    }

    public void dispose() {
        stopRecording();
        stopPlaying();
    }

    public void deleteAudioFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }
}
