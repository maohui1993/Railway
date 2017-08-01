package cn.dazhou.railway.splash.functions.home;

import android.content.Context;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by hooyee on 2017/8/1.
 */

public class HomePresenter implements HomeContract.Presenter {
    private HomeContract.View mView;
    private Context mContext;

    public HomePresenter(Context context, HomeContract.View view) {
        mContext = context;
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(mContext, "点击item: " + position, Toast.LENGTH_SHORT).show();

        TextView v = (TextView) mView.getViewByPosition(position);
        int[] positions = new int[2];
        v.getLocationOnScreen(positions);
        TextView target = new TextView(mContext);
        target.setLayoutParams(v.getLayoutParams());
        target.setX(positions[0]);
        target.setY(positions[1]);
        target.setText(v.getText());
        mView.addView(target);
//        TranslateAnimation animation = new TranslateAnimation(target.getX(), 100, target.getY(), 100);
        TranslateAnimation animation = new TranslateAnimation(0, 100, 0, -100);
        animation.setDuration(1000);
        target.setAnimation(animation);
    }
}
