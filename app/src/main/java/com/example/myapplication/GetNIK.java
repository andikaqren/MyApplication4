package com.example.myapplication;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GetNIK {
    @FormUrlEncoded
    @POST("getProvinsi.php")
    Call<Value> sendIdProvinsi(@Field("id")String idpronvinsi);

    @FormUrlEncoded
    @POST("getKota.php")
    Call<Value> sendIdKota(@Field("id")String idkota);

    @FormUrlEncoded
    @POST("getKecamatan.php")
    Call<Value> sendIdKecamatan(@Field("id")String idkecamatan);
}
