package cn.dazhou.railway.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Hooyee on 2017/3/6.
 * mail: hooyee01_moly@foxmail.com
 */

public class RippleButton extends android.support.v7.widget.AppCompatButton {
    private RippleDrawable rippleDrawable;

    public RippleButton(Context context) {
        this(context, null);
    }

    public RippleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        rippleDrawable = new RippleDrawable();
        rippleDrawable.setCallback(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rippleDrawable.setBounds(0, 0, getWidth(), getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rippleDrawable.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        rippleDrawable.onTouch(event);
        return super.onTouchEvent(event);
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return who == rippleDrawable || super.verifyDrawable(who);
    }
}
