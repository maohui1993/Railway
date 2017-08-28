package cn.dazhou.im.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import java.io.File;

import cn.dazhou.im.R;
import cn.dazhou.im.entity.VideoInfo;
import cn.dazhou.im.util.MediaPlayerUtils;

/**
 * @author hooyee
 * 视频播放
 */
public class VideoActivity extends Activity implements MediaPlayer.OnCompletionListener {
    private static final String EXTRA_DATA = "data";
    private int mLeft;
    private int mTop;
    private float mScaleX;
    private float mScaleY;

    private ImageView imageView;
    private View content;
    private Drawable mBackground;
    private SurfaceView mSurfaceView;
    private MediaPlayerUtils mMediaPlayer;

    private ImageView startVideo;
    private ImageView suspendVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video);

        imageView = (ImageView) findViewById(R.id.iv_thumbnail);
        mSurfaceView = (SurfaceView) findViewById(R.id.sv_video);
        content = findViewById(R.id.content);
        startVideo = (ImageView) findViewById(R.id.iv_start);
        suspendVideo = (ImageView) findViewById(R.id.iv_suspend);

        mSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mMediaPlayer.getState()) {
                    case MediaPlayerUtils.PLAYING:
                        startVideo.setVisibility(View.GONE);
                        suspendVideo.setVisibility(View.VISIBLE);
                        mMediaPlayer.changeState(MediaPlayerUtils.PAUSED);
                        break;
                    case MediaPlayerUtils.PAUSED:
                        startVideo.setVisibility(View.GONE);
                        suspendVideo.setVisibility(View.GONE);
                        mMediaPlayer.changeState(MediaPlayerUtils.PLAYING);
                        break;
                    case MediaPlayerUtils.STOPPED:
                        startVideo.setVisibility(View.GONE);
                        suspendVideo.setVisibility(View.GONE);
                        mMediaPlayer.changeState(MediaPlayerUtils.PLAYING);
                        break;
                }
            }
        });

        VideoInfo info = getIntent().getParcelableExtra(EXTRA_DATA);

        mMediaPlayer = new MediaPlayerUtils(this);
        mMediaPlayer.setOnCompletionListener(this);
        init(info);
    }

    public void init(final VideoInfo videoInfo) {
        if (videoInfo == null) {
            return;
        }
        Uri uri = Uri.fromFile(new File(videoInfo.getVideoUrl()));
        mMediaPlayer.setDataUri(uri);
        mMediaPlayer.setSurfaceHolder(mSurfaceView.getHolder());

        final int left = videoInfo.getLocationX();
        final int top = videoInfo.getLocationY();
        final int width = videoInfo.getWidth();
        final int height = videoInfo.getHeight();

        // 用于做淡入淡出动画
        mBackground = new ColorDrawable(Color.BLACK);
        content.setBackground(mBackground);

        // 获取video的第一帧
        final Bitmap bmp = MediaPlayerUtils.getVideoThumbnail(videoInfo.getVideoUrl());
        imageView.setImageBitmap(bmp);
        // 通过video的第一帧获取video文件的宽度和高度值，并得出surfaceView的尺寸
        setVideoSize(bmp);

        imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);

                int location[] = new int[2];
                imageView.getLocationOnScreen(location);
                // 计算退出视频时，动画平移的位置
                mLeft = left - location[0];
                mTop = top - location[1];
                // 计算动画缩放到什么大小是视屏的大小
                mScaleX = width * 1.0f / imageView.getWidth();
                mScaleY = height * 1.0f / imageView.getHeight();
                activityEnterAnim();
                return true;
            }
        });
    }

    private int translationX;
    private int translationY;

    private void activityEnterAnim() {
        imageView.setPivotX(0);
        imageView.setPivotY(0);
        imageView.setScaleX(mScaleX);
        imageView.setScaleY(mScaleY);
        imageView.setTranslationX(mLeft);
        imageView.setTranslationY(mTop);
        imageView.animate().scaleX(1).scaleY(1).translationX(translationX).translationY(translationY).
                setDuration(500).setInterpolator(new DecelerateInterpolator()).start();
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(mBackground, "alpha", 0, 255);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.setDuration(500);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mMediaPlayer.changeState(MediaPlayerUtils.PLAYING);
                imageView.setVisibility(View.GONE);
            }
        });
        objectAnimator.start();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void activityExitAnim(Runnable runnable) {
        content.setPivotX(0);
        content.setPivotY(0);
        content.animate().scaleX(mScaleX).scaleY(mScaleY).translationX(mLeft).translationY(mTop).
                withEndAction(runnable).
                setDuration(500).setInterpolator(new DecelerateInterpolator()).start();
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(mBackground, "alpha", 255, 0);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.setDuration(500);
        objectAnimator.start();
    }

    @Override
    public void onBackPressed() {

        activityExitAnim(new Runnable() {
            @Override
            public void run() {
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    protected void onDestroy() {
        mMediaPlayer.release();
        super.onDestroy();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        startVideo.setVisibility(View.VISIBLE);
        mMediaPlayer.changeState(MediaPlayerUtils.STOPPED);
    }

    public static void startItself(Context context, Parcelable data) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra(EXTRA_DATA, data);
        context.startActivity(intent);
    }

    private void setVideoSize(Bitmap videoThumbnail) {

        // // Get the dimensions of the video
        int videoWidth = videoThumbnail.getWidth();
        int videoHeight = videoThumbnail.getHeight();
        float videoProportion = (float) videoWidth / (float) videoHeight;

        // Get the width of the screen
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        float screenProportion = (float) screenWidth / (float) screenHeight;

        // Get the SurfaceView layout parameters
        android.view.ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();
        android.view.ViewGroup.LayoutParams image = imageView.getLayoutParams();
        if (videoProportion > screenProportion) {
            lp.width = screenWidth;
            lp.height = (int) ((float) screenWidth / videoProportion);
            image.width=lp.width;
            image.height=lp.height;
        } else {
            lp.width = (int) (videoProportion * (float) screenHeight);
            lp.height = screenHeight;
            image.width=lp.width;
            image.height=lp.height;
        }

        translationX = (screenWidth - image.width)/2;
        translationY = (screenHeight - image.height)/2;
        // Commit the layout parameters
        mSurfaceView.setLayoutParams(lp);
        imageView.setLayoutParams(image);
    }
}
