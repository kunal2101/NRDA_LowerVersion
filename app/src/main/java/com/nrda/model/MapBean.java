package com.nrda.model;

/**
 * Created by kunalkumar on 31/07/17.
 */

public class MapBean {

    private String name;
    private Double latitude;
    private Double longitude;
    private String vehicleRegNo;
    private String routetype;
    private String routeName;
    private String deviceSpeed;
    private String routeID;
    private String tripdateTime;

    public String getTripdateTime() {
        return tripdateTime;
    }

    public void setTripdateTime(String tripdateTime) {
        this.tripdateTime = tripdateTime;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getDeviceSpeed() {
        return deviceSpeed;
    }

    public void setDeviceSpeed(String deviceSpeed) {
        this.deviceSpeed = deviceSpeed;
    }

    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getVehicleRegNo() {
        return vehicleRegNo;
    }

    public void setVehicleRegNo(String vehicleRegNo) {
        this.vehicleRegNo = vehicleRegNo;
    }

    public String getRoutetype() {
        return routetype;
    }

    public void setRoutetype(String routetype) {
        this.routetype = routetype;
    }
}
