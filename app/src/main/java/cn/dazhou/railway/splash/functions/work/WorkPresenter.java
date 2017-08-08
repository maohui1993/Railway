package cn.dazhou.railway.splash.functions.work;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.dazhou.database.FunctionItemModel;
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
    private List<FunctionItemModel> datas = new ArrayList<>();
    private GridAdapter mAdapter;

    public WorkPresenter(Context context, WorkContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void initData() {
        getDataFromServer("http://192.168.1.39:8080/");

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
                for (int i = 0; i < 10; i++) {
                    FunctionItemModel model = new FunctionItemModel();
                    model.setJid("1" + i);
                    model.setShortName("a" + i);
                    model.setIconUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502193620557&di=a8207d45ef048f5080ccea293702466e&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F019f9c5542b8fc0000019ae980d080.jpg%40900w_1l_2o_100sh.jpg");
                    datas.add(model);
                }
                mView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.i("retrofit", t.getMessage());
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(mContext, datas.get(position).getShortName(), Toast.LENGTH_SHORT).show();
    }
}
