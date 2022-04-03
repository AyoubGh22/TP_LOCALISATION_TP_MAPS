package com.example.testret.API;

import android.annotation.SuppressLint;

import com.example.testret.Models.Position;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import retrofit2.Response;

public class Repository {
    private final APISettings apiSettings;

    @Inject
    public Repository(APISettings apiSettings) {
        this.apiSettings = apiSettings;
    }


    public  Single<List<Position>> getPositions() {
        return apiSettings.getPositions();
    }
    public  Single<Position> getPositionByImei(String imei) {
        return apiSettings.getPositionByImei(imei);
    }
    public Single<Void> AddPosition(Position position){
        return apiSettings.AddPosition(position);
    }
    public Single<Position> updatePosition(Position position){
        return apiSettings.updatePosition(position);
    }

}
