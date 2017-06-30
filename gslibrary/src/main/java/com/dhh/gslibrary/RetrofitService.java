package com.dhh.gslibrary;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by zyx on 16-10-31.
 * 网络处理接口
 */

public interface RetrofitService {

    @GET
   <T> Observable<T> get(@Url String url, @QueryMap Map<String, String> prarams);

    @POST
    Observable<ResponseBody> post(@Url String url, @QueryMap Map<String, String> params, @Body Map<String, String> body);

    @POST
    Observable<ResponseBody> post(@Url String url, @QueryMap Map<String, String> params, @Body List<Map> body);

    @Multipart
    @POST
    Observable<ResponseBody> post(@Url String url, @QueryMap Map<String, String> params, @Body MultipartBody.Part file);

    @Streaming
    @POST
    Observable<ResponseBody> post(@Url String url, @QueryMap Map<String, String> params, @Body RequestBody file);
}
