package com.example.testret.Models;

import android.location.Location;

public class MyLocationUpdateEventBus {
    private Location location;

    public MyLocationUpdateEventBus(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
