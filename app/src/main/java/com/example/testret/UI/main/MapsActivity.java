package com.example.testret.UI.main;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

import com.example.testret.Models.MyLocationUpdateEventBus;
import com.example.testret.Models.Position;
import com.example.testret.R;
import com.example.testret.Service.LocationServiceUpdate;
import com.example.testret.databinding.ActivityMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "TAG";
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LocationManager manager;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private PositionViewModel positionViewModel;
    private Location location;

    public static String android_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        positionViewModel = new ViewModelProvider(this).get(PositionViewModel.class);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (isLocationEnabled()) {
            _GetCurrentUserPermission();
        } else {
            buildAlertMessageNoGps();
        }
    }

    public boolean isLocationEnabled() {
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.ActivateGpsRequest))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.okay), (dialog, id) -> startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 333));
        final AlertDialog alert = builder.create();
        alert.show();
        _GetCurrentUserPermission();
    }

    private void _GetCurrentUserPermission() {
        PermissionListener dialogPermissionListener =
                DialogOnDeniedPermissionListener.Builder
                        .withContext(getApplicationContext())
                        .withTitle("Gps permission")
                        .withMessage("Gps permission is needed to get your location")
                        .withButtonText(android.R.string.ok)
                        .build();
        Dexter.withContext(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        init();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        dialogPermissionListener.onPermissionDenied(permissionDeniedResponse);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();
    }


    private void init() {
        _BuildLocationRequest();
        _BuildLocationCallBack();
        _UpdateLocation();
    }

    private void _UpdateLocation() {
        if (fusedLocationProviderClient == null) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            positionViewModel.getPositions();
            subscribe();
        }
    }

    private void _BuildLocationRequest() {
        if (locationRequest == null) {
            locationRequest = new LocationRequest();
            locationRequest.setSmallestDisplacement(10f);
            locationRequest.setInterval(5000);
            locationRequest.setFastestInterval(3000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }
    }

    private void _BuildLocationCallBack() {
        if (locationCallback == null) {
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    Location location = locationResult.getLastLocation();
                    if (location != null)
                        startService(new Intent(getApplicationContext(), LocationServiceUpdate.class));
                }
            };
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        positionViewModel.getPositionMutableLiveData().observe(this,positions -> {
            mMap.clear();
            for (Position p:positions
                 ) {
                mMap.addMarker(new MarkerOptions().position(new LatLng(p.getLatitude(),p.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.drawable.clients)).title(p.getImei()));
                String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(getApplicationContext(), LocationServiceUpdate.class));
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().hasSubscriberForEvent(MyLocationUpdateEventBus.class))
            EventBus.getDefault().removeStickyEvent(MyLocationUpdateEventBus.class);
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void OnMyLocationUpdateEventBus(MyLocationUpdateEventBus myLocationUpdateEventBus){
        location = myLocationUpdateEventBus.getLocation();
        positionViewModel.getPositionByImei(android_id);
        subscribe();
    }

    public void subscribe(){
        if (location!=null){
            Log.d(TAG, "_FindUserLocation: GET");
        positionViewModel.getMyPositionMutableLiveData().observe(this, position -> {
            if (position.getId()== 0){
                Log.d(TAG, "_FindUserLocation: Add");
                positionViewModel.AddPosition(new Position(location.getLatitude(), location.getLongitude(), android_id));}
            else{
                Log.d(TAG, "_FindUserLocation: Update");
                position.setLatitude(location.getLatitude());
                position.setLongitude(location.getLongitude());
                positionViewModel.updatePosition(position);
            }

        });
        positionViewModel.getUpdatePositionMutableLiveData().observe(this, aBoolean ->
        {
            positionViewModel.getPositions();
            Log.d(TAG, "_FindUserLocation: Update Done : "+aBoolean.getId());
        });}
    }
}