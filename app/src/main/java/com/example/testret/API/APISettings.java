package com.example.testret.API;

import com.example.testret.Models.Position;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface APISettings {

    @GET("all")
    Single<List<Position>> getPositions();

    @GET("imei")
    Single<Position> getPositionByImei(@Query("imei") String imei);

    @POST("add")
    Single<Void> AddPosition(@Body Position position);

    @PUT("update")
    Single<Position> updatePosition(@Body Position position);
}
