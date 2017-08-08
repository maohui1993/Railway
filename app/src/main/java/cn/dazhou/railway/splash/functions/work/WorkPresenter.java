package cn.dazhou.railway.splash.functions.work;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.dazhou.railway.R;
import cn.dazhou.railway.http.RailwayApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by hooyee on 2017/8/8.
 */

public class WorkPresenter implements WorkContract.Presenter {
    private WorkContract.View mView;
    private Context mContext;
    private List<GridAdapter.Item> datas = new ArrayList<>();
    private GridAdapter mAdapter;

    // 图标
    private int[] mEffectArray = {
            R.drawable.record, R.drawable.check,
            R.drawable.breakdown, R.drawable.date
    };

    public WorkPresenter(Context context, WorkContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void initData() {
        getDataFromServer("http://192.168.1.39:8080/");
        for (int i = 0; i < mEffectArray.length; i++) {
            datas.add(new GridAdapter.Item(mEffectArray[i], ""));
        }
        mAdapter = new GridAdapter(mContext, datas, R.layout.grid_item);
    }


    @Override
    public void getDataFromServer(String url) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .build();
        RailwayApi railwayApi = retrofit.create(RailwayApi.class);
        Call call = railwayApi.getHome("index.html");
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Toast.makeText(mContext, response.toString(), Toast.LENGTH_SHORT).show();
                Log.i("retrofit", response.toString());
                mView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.i("retrofit", t.getMessage());
            }
        });
    }
}
