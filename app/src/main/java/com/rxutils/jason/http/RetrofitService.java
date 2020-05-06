package com.rxutils.jason.http;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * 在本类写所有的请求接口
 * Created by jason on 18/5/31.
 */

public interface RetrofitService {

    @FormUrlEncoded
    @POST()
    Observable<ResponseBody> doPostRequest(@Url String url, @FieldMap Map<String, String> map); //共用POST

    @POST
    Observable<ResponseBody> doPostRequest(@Url String url, @Body RequestBody requestBody);      //共用JSON

    @GET
    Observable<ResponseBody> doGetRequest(@Url String url);                                     // 共用get,直接把参数放到url?后
}


