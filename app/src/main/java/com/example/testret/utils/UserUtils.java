package com.example.testret.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.text.TextUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

@SuppressLint("SimpleDateFormat")
public class UserUtils {
    public static String getAddressFromLocation(Context context, Location location) {
        StringBuilder result = new StringBuilder();
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addressList != null && addressList.size() > 0) {
                if (addressList.get(0).getLocality() != null && !TextUtils.isEmpty(addressList.get(0).getLocality())) {
                    result.append(addressList.get(0).getLocality());
                } else if (addressList.get(0).getSubAdminArea() != null && !TextUtils.isEmpty(addressList.get(0).getSubAdminArea())) {
                    result.append(addressList.get(0).getSubAdminArea());
                } else if (addressList.get(0).getAdminArea() != null && !TextUtils.isEmpty(addressList.get(0).getAdminArea())) {
                    result.append(addressList.get(0).getAdminArea());
                } else {
                    result.append(addressList.get(0).getCountryName());
                }
                result.append(addressList.get(0).getCountryCode());
            }
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return result.toString();
        }
    }

    @SuppressLint("DefaultLocale")
    public static String getDegreeToCelsius(float Kelvin) {
        double result = (Kelvin - 273.15);
        return String.format("%.0f", result);
    }

    public static String getDateDDMMFromNumber(double currentTemp) {
        String date = new SimpleDateFormat("dd.MMM.yyyy")
                .format(new java.util.Date((long) currentTemp * 1000));
        return date;
    }

    public static String getDateHHMMFromNumber(double currentTemp) {
        String date = new SimpleDateFormat("HH:mm")
                .format(new java.util.Date((long) currentTemp * 1000));
        return date;
    }
}
