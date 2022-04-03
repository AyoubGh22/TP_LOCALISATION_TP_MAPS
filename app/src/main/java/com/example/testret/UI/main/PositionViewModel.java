package com.example.testret.UI.main;

import android.annotation.SuppressLint;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.testret.API.Repository;
import com.example.testret.Models.Position;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

@SuppressLint("CheckResult")
public class PositionViewModel extends ViewModel {
    private final Repository repository;

    @ViewModelInject
    public PositionViewModel(Repository repository) {
        this.repository = repository;
    }

    private final MutableLiveData<List<Position>> positionMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Position> myPositionMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Void> addPositionMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Position> updatePositionMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<List<Position>> getPositionMutableLiveData() {
        return positionMutableLiveData;
    }

    public MutableLiveData<Void> getAddPositionMutableLiveData() {
        return addPositionMutableLiveData;
    }

    public MutableLiveData<Position> getMyPositionMutableLiveData() {
        return myPositionMutableLiveData;
    }

    public MutableLiveData<Position> getUpdatePositionMutableLiveData() {
        return updatePositionMutableLiveData;
    }

    public void getPositions() {
        repository.getPositions()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(positionMutableLiveData::setValue, Throwable::printStackTrace);

    }
    public void getPositionByImei(String imei) {
        repository.getPositionByImei(imei)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(myPositionMutableLiveData::setValue, Throwable::printStackTrace);

    }

    public void AddPosition(Position position) {
        repository.AddPosition(position)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(addPositionMutableLiveData::setValue, Throwable::printStackTrace);


    }
    public void updatePosition(Position position) {
        repository.updatePosition(position)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(updatePositionMutableLiveData::setValue, Throwable::printStackTrace);
    }

}
