package cn.dazhou.railway.splash.functions.work;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.dazhou.database.FunctionItemModel;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.R;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.http.RailwayApi;
import cn.dazhou.railway.splash.functions.work.bean.Function;
import cn.dazhou.railway.splash.functions.work.http.FunctionActivity;
import cn.dazhou.railway.util.SharedPreferenceUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
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
    RailwayApi railwayApi;

    public WorkPresenter(Context context, WorkContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void initData() {
        String url = "http://" + MyApp.gServerIp + ":" + SharedPreferenceUtil.getInt(mContext, "work_port", 8080) + "/";
        getDataFromServer(url);

        mAdapter = new GridAdapter(mContext, datas, R.layout.grid_item);
    }

    @Override
    public void getDataFromServer(String url) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(url)
                .build();
        railwayApi = retrofit.create(RailwayApi.class);
        Call<Function> call = railwayApi.getData("SysFunction/query", "admin");
        // 访问主页获取图标URL
        call.enqueue(new Callback<Function>() {
            @Override
            public void onResponse(Call<Function> call, Response<Function> response) {
                Log.i("retrofit", response.toString());
                List<FunctionItemModel> data = null;
                if (response.body() != null) {
                    data = response.body().getData();
                }
                if (data != null) {
                    datas.addAll(data);
                }
                for (int i = 0; i < datas.size(); i++) {
                    String localUrl = Constants.FILE_PATH + "icon_" + MyApp.gCurrentUsername + i + ".png";
                    String iconUrl = datas.get(i).getIconUrl();
                    downFile(iconUrl, localUrl);
                    datas.get(i).setIconUrl(localUrl);
                    datas.get(i).save();
                }
                mView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.i("retrofit", t.getMessage());
            }
        });
    }

    private void downFile(String url, final String fileFullName) {
        Call<ResponseBody> downCall = railwayApi.downFile(url);
        downCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                Log.i("retrofit", response.toString());
                if (response.isSuccessful()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            writeResponseBodyToDisk(response.body(), fileFullName);
                        }
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("retrofit", t.getMessage());
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(mContext, datas.get(position).getFunctionname(), Toast.LENGTH_SHORT).show();
        FunctionActivity.startItself(mContext, datas.get(position));
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, String fileFullName) {
        try {
            // todo change the file location/name according to your needs
            File iconFile = new File(fileFullName);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            if(!iconFile.getParentFile().exists()) {
                if(iconFile.getParentFile().mkdirs()) {
                    iconFile.createNewFile();
                }
            }

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
                Log.d(Constants.LOG_TAG_HTTP, "file download: " + e.getMessage());
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
