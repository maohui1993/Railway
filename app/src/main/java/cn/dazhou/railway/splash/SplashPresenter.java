package cn.dazhou.railway.splash;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import cn.dazhou.database.util.StringUtil;
import cn.dazhou.im.IMLauncher;
import cn.dazhou.railway.R;

/**
 * Created by hooyee on 2017/6/1.
 */

public class SplashPresenter implements SplashContract.Presenter {
    private static final int REQUEST_CODE = 200;
    private Context mContext;
    private SplashContract.View mView;

    public SplashPresenter(Context context, SplashContract.View view) {
        mContext = context;
        mView = view;
        mView.setPresenter(this);
    }

    public void parseQRcode() {
        Intent intent = new Intent(mContext, CaptureActivity.class);
        ((AppCompatActivity) mContext).startActivityForResult(intent, REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    final String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(mContext, "解析结果:" + result, Toast.LENGTH_LONG).show();
                    new AlertDialog.Builder(mContext)
                            .setTitle("添加好友")
                            .setMessage("是否确认添加" + result + "为好友？")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        IMLauncher.addFriend(StringUtil.getRealJid(result));
                                    } catch (IMLauncher.IMException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton("取消", null)
                            .create()
                            .show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(mContext, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = mView.getDrawerLayout();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void changeText() {
        mView.changeText();
    }

    @Override
    public boolean canBack() {
        return mView.canBack();
    }
}
