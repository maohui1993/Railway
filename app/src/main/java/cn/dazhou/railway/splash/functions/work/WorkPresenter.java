package cn.dazhou.railway.splash.functions.work;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.dazhou.database.FunctionItemModel;
import cn.dazhou.railway.R;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.http.RailwayApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;

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
        Call<ResponseBody> call = railwayApi.getHome("index.html");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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

        Call<ResponseBody> downCall = railwayApi.downFile("");
        downCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            writeResponseBodyToDisk(response.body());
                        }
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(mContext, datas.get(position).getShortName(), Toast.LENGTH_SHORT).show();
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File iconFile = new File(Constants.FILE_PATH + "icon_" + System.currentTimeMillis() + ".png");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(iconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(Constants.LOG_TAG_HTTP, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
