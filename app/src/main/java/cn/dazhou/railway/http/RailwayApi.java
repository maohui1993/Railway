package cn.dazhou.railway.http;

import cn.dazhou.railway.splash.functions.work.bean.Function;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by hooyee on 2017/8/8.
 */

public interface RailwayApi {
    @GET("railway_test/{url}")
    Call<Function> getData(@Path(value = "url", encoded = true) String url, @Query("usercode") String key);

    @GET("{icon_path}")
    Call<ResponseBody> downFile(@Path(value = "icon_path", encoded = true) String iconPath);
}
