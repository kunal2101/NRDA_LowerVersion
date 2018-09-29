package com.nrda.utils;

/**
 * Created by Diwash Kumar Choudhary on 3/10/2017.
 */
public class Nrda_Url_Conts {

    public static final String BASE_URL = "http://61.0.175.99/nrdamobileservice/mobileservice.asmx/";
    public static final String BASE_URL_two = "http://61.0.175.99/ETMService/NEWETM.asmx/";

    public static final String FARE_CAL                     =  BASE_URL+ "getFareEstimationDetails";
    public static final String ALLSHELTERDETAIL             = "getAllShelterDetails";
    public static final String GETROUTEINFO                 = "getRouteInfo";
    public static final String GETSHELTERDETAIL             = "getShelterDetails";
    public static final String GETBUSTIMINGDETAILS          = "getBusTimingDetails";
    public static final String GETETAROUTEDETAIL            = "getETARouteDetails";
    public static final String GETROUTEETADETAILBYRID       = "getRouteETADetailsbyRID";
    public static final String GETROUTEMAPIMAGE             = "getRouteMap";
    public static final String GETNEARBUS_STOP              = "getNearestShelter";
    public static final String GETSHELTERETADETAILS         = "getShelterETADetails";
    public static final String GETROUTETRIPDETAILBYRID      = "getTripDetailsbyRID";
    public static final String GETETATRIPWISEDETAILS        = "getETATripWiseDetails";
    public static final String ALLVEHICLELIVETRACKING       = "allvehicleliveTracking";
    public static final String GETALLSHELTERLIST            = "getAllShelterDetails";
    public static final String GETROUTEPOINT                = "getRoutePoints";
    public static final String GETROUTEPISINFO              = "getPISDetails";
    public static final String GETSHELTERBYRANGE            = "getNearestShelterByRange";
    public static final String GETLIVETRACKBYROUTE          = "allvehicleliveTrackingroute";
    public static final String GETREALTIMEPISNEW            = "pisdetails";
    public static final String GETFEEDBACK                  = "feedback";
    public static final String GETROUTEINFORMATION          = "routeinfoapi";

//smart card
    public static final String GETOTP                       = "GenerateOTP";
    public static final String GETREADOTP                   = "VerifyOTP";
    public static final String GETCARDDETAIL                = "Getcarddetails";

    // Location updates intervals
    public static int UPDATE_INTERVAL = 3000; // 3 sec
    public static int FATEST_INTERVAL = 3000; // 5 sec
    public static int DISPLACEMENT = 10; // 10 meters
}

