package cn.dazhou.railway.http;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by hooyee on 2017/8/8.
 */

public interface RailwayApi {
    @GET("home/{id}")
    Call<ResponseBody> getHome(@Path("id") String id);

    @GET("home/{icon_path}")
    Call<ResponseBody> downFile(@Path("icon_path") String iconPath);
}
