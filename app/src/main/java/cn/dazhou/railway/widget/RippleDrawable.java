package cn.dazhou.railway.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;

/**
 * Created by Hooyee on 2017/3/6.
 * mail: hooyee01_moly@foxmail.com
 */

public class RippleDrawable extends Drawable {
    public static final int ANIMATION_DURATION = 500;
    public static final int ANIMATION_DURATION_LONG = 5000;
    private int alpha;
    private Paint paint;
    private float pointX;
    private float pointY;
    private ObjectAnimator mAnimator;

    private float radius;

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public RippleDrawable() {
        paint = new Paint();
        paint.setColor(Color.parseColor("#dedede"));
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setAlpha(50);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(pointX, pointY, radius, paint);
    }

    @Override
    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void onTouch(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN :
                pointX = event.getX();
                pointY = event.getY();
                mAnimator = startAnimation(ANIMATION_DURATION_LONG);
                break;
            case MotionEvent.ACTION_UP :
                if (mAnimator != null) {
                    mAnimator.removeAllListeners();
                    mAnimator.cancel();
                }
                startAnimation(ANIMATION_DURATION);
                break;
        }
    }

    public ObjectAnimator startAnimation(int duration) {
        Rect r = getBounds();
        ObjectAnimator mAnimator = ObjectAnimator.ofFloat(this, "radius", radius, r.right + 100);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setDuration(duration);
        mAnimator.start();
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidateSelf();
            }
        });
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                radius = 0;
                invalidateSelf();
            }
        });
        return mAnimator;
    }
}
