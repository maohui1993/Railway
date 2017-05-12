package cn.dazhou.im.modle;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by hooyee on 2017/5/9.
 */

public class SoundRecord {

    private static String DIR_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    private String tmpPath;
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
        tmpPath = DIR_PATH + System.currentTimeMillis() + ".ogg";
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

    public void stopRecording() throws Exception{

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
            while ((length = in.read(bytes)) != -1) {
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
        // 将正在播放的语音停止掉
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        mPlayer = new MediaPlayer();
        try {
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            mPlayer.setDataSource(fis.getFD());
            mPlayer.prepare();
            mPlayer.start();
//            mPlayer.setDataSource(path);
//            mPlayer.prepare();
//            mPlayer.start();
        } catch (IOException e) {
            Log.i("TAG", "prepare() failed");
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

//    public void dispose() {
//        stopRecording();
//        stopPlaying();
//    }

    public void deleteAudioFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }
}
