package com.nrda.database;

public class DatabaseRoutemodel {

    public int _row_id;
    public String route_id;
    public String latitude;
    public String longitude;

    public DatabaseRoutemodel() {
    }

    // constructor
    public DatabaseRoutemodel(int row_id, String route_id, String latitude, String longitude) {
        this._row_id = row_id;
        this.route_id = route_id;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    // constructor
    public DatabaseRoutemodel(String route_id, String latitude, String longitude) {
        this.route_id = route_id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getRow_id() {
	    return this._row_id;
    }
    public void setRow_id(int row_id) {
	    this._row_id = row_id;
    }


    public String getLatitude() {
	    return this.latitude;
    }
    public void setLatitude(String mlatitude) {
	    this.latitude = mlatitude;
    }


    public String getLongitude() {
	    return this.longitude;
    }
    public void setLongitude(String mlatitude) {
	    this.longitude = mlatitude;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }
}