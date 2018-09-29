package com.nrda.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseRoute extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 6;

    // Database Name
    private static final String DATABASE_NAME = "nrda_route_db";

    // Contacts table name
    private static final String TABLE_UP_ROUTE = "up_route";
    private static final String TABLE_SMART_CARD = "smart_card";
    //private static final String TABLE_DOWN_ROUTE = "down_route";

    // Table Columns names
    private static final String KEY_ID = "row_id";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_ROUTE_ID = "route_id";

    private static final String KEY_LONGITUDE = "longitude";

    private static final String KEY_TAGID = "tag_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_RECHARGE_TYPE = "recharge_type";
    private static final String KEY_RECHARGE_AMOUNT = "recharge_amount";
    private static final String KEY_CARD_TYPE = "card_type";
    private static final String KEY_COLOR = "color";
    private static final String KEY_SMART_CARD_CODE = "smart_card_code";
    private static final String KEY_VALIDITY_DATE = "validity_date";

    private final ArrayList<DatabaseSmartCardDetailModel> smartcardrecord_list = new ArrayList<DatabaseSmartCardDetailModel>();
    private final ArrayList<DatabaseSmartCardDetailModel> smartcardrecord_selected_list = new ArrayList<DatabaseSmartCardDetailModel>();
    private final ArrayList<DatabaseRoutemodel> touchlist_list = new ArrayList<DatabaseRoutemodel>();

    public DatabaseRoute(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_UPROUTE_TABLE = "CREATE TABLE " + TABLE_UP_ROUTE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_ROUTE_ID + " TEXT,"
                + KEY_LATITUDE + " TEXT,"
                + KEY_LONGITUDE + " TEXT" +
                ")";
        db.execSQL(CREATE_UPROUTE_TABLE);

        String CREATE_SMARTCARD_TABLE = "CREATE TABLE " + TABLE_SMART_CARD + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TAGID + " TEXT,"
                + KEY_NAME + " TEXT,"
                + KEY_MOBILE + " TEXT,"
                + KEY_RECHARGE_TYPE + " TEXT,"
                + KEY_RECHARGE_AMOUNT + " TEXT,"
                + KEY_CARD_TYPE + " TEXT,"
                + KEY_COLOR + " TEXT,"
                + KEY_VALIDITY_DATE + " TEXT,"
                + KEY_SMART_CARD_CODE + " TEXT" +
                ")";
        db.execSQL(CREATE_SMARTCARD_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UP_ROUTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SMART_CARD);

        onCreate(db);
    }

    public void insertUPRoute(DatabaseRoutemodel contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ROUTE_ID, contact.getRoute_id());
        values.put(KEY_LATITUDE, contact.getLatitude());
        values.put(KEY_LONGITUDE, contact.getLongitude());
        // Inserting Row
        db.insert(TABLE_UP_ROUTE, null, values);
        db.close(); // Closing database connection
    }

    public void insertSmartCardDetail(DatabaseSmartCardDetailModel smartCard) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_TAGID, smartCard.getTag_id());
        values.put(KEY_NAME, smartCard.getName());
        values.put(KEY_MOBILE, smartCard.getMobile());
        values.put(KEY_RECHARGE_TYPE, smartCard.getRecharge_type());
        values.put(KEY_RECHARGE_AMOUNT, smartCard.getRecharge_amount());
        values.put(KEY_CARD_TYPE, smartCard.getCard_type());
        values.put(KEY_COLOR, smartCard.getColor());
        values.put(KEY_SMART_CARD_CODE, smartCard.getSmartcard_code());
        values.put(KEY_VALIDITY_DATE, smartCard.getValidity());
        // Inserting Row
        db.insert(TABLE_SMART_CARD, null, values);
        db.close(); // Closing database connection
    }

   // Search if tagId is available or not
    public Boolean searchTagId(String tagId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + TABLE_SMART_CARD +" Where tag_id ='" + tagId + "' ", null);
        Boolean rowExists;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()){
            do {
                rowExists = true;
            } while (cursor.moveToNext());
        }else{
            rowExists = false;
        }
        // return contact list
        return rowExists;
    }

    // Getting All Product
    public ArrayList<DatabaseRoutemodel> Get_AllUPRoute() {
        try {
            touchlist_list.clear();
            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_UP_ROUTE;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    DatabaseRoutemodel contact = new DatabaseRoutemodel();
                    contact.setRow_id(Integer.parseInt(cursor.getString(0)));
                    contact.setRoute_id(cursor.getString(1));
                    contact.setLatitude(cursor.getString(2));
                    contact.setLongitude(cursor.getString(3));
                    //Adding contact to list
                    touchlist_list.add(contact);
                } while (cursor.moveToNext());
            }
            // return contact list
            cursor.close();
            db.close();
            return touchlist_list;
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("all_contact", "" + e);
        }

        return touchlist_list;
    }

    public ArrayList<DatabaseRoutemodel> GetRoutebyId(String route_id) {
        try {
            touchlist_list.clear();
            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_UP_ROUTE +" WHERE "+ KEY_ROUTE_ID + "=" +route_id;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    DatabaseRoutemodel contact = new DatabaseRoutemodel();
                    contact.setRow_id(Integer.parseInt(cursor.getString(0)));
                    contact.setRoute_id(cursor.getString(1));
                    contact.setLatitude(cursor.getString(2));
                    contact.setLongitude(cursor.getString(3));
                    //Adding contact to list
                    touchlist_list.add(contact);
                } while (cursor.moveToNext());
            }
            // return contact list
            cursor.close();
            db.close();
            return touchlist_list;
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("all_contact", "" + e);
        }

        return touchlist_list;
    }

    // Getting All smartCardData
    public ArrayList<DatabaseSmartCardDetailModel> Get_AllRecord() {
        try {
            smartcardrecord_list.clear();
            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_SMART_CARD;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    DatabaseSmartCardDetailModel contact = new DatabaseSmartCardDetailModel();
                    contact.setTag_id(cursor.getString(1));
                    contact.setName(cursor.getString(2));
                    contact.setMobile(cursor.getString(3));
                    contact.setRecharge_type(cursor.getString(4));
                    contact.setRecharge_amount(cursor.getString(5));
                    contact.setCard_type(cursor.getString(6));
                    contact.setColor(cursor.getString(7));
                    contact.setValidity(cursor.getString(8));
                    contact.setSmartcard_code(cursor.getString(9));
                    //Adding contact to list
                    smartcardrecord_list.add(contact);
                } while (cursor.moveToNext());
            }
            // return contact list
            cursor.close();
            db.close();
            return smartcardrecord_list;
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("all_contact", "" + e);
        }

        return smartcardrecord_list;
    }

    public ArrayList<DatabaseSmartCardDetailModel> Get_SelectedRecord(String tagId) {
        try {
            smartcardrecord_selected_list.clear();
            // Select All Query

            String selectQuery = "SELECT  * FROM " + TABLE_SMART_CARD + " WHERE "+ KEY_TAGID +" = '"+tagId+"'";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    DatabaseSmartCardDetailModel contact = new DatabaseSmartCardDetailModel();
                    contact.setTag_id(cursor.getString(1));
                    contact.setName(cursor.getString(2));
                    contact.setMobile(cursor.getString(3));
                    contact.setRecharge_type(cursor.getString(4));
                    contact.setRecharge_amount(cursor.getString(5));
                    contact.setCard_type(cursor.getString(6));
                    contact.setColor(cursor.getString(7));
                    contact.setValidity(cursor.getString(8));
                    contact.setSmartcard_code(cursor.getString(9));
                    //Adding contact to list
                    smartcardrecord_selected_list.add(contact);
                } while (cursor.moveToNext());
            }
            // return contact list
            cursor.close();
            db.close();
            return smartcardrecord_selected_list;
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("all_contact", "" + e);
        }

        return smartcardrecord_selected_list;
    }

    public ArrayList<HashMap<String,String>> GetRouteby_Id(String route_id) {
        ArrayList<HashMap<String,String>> getdata = new ArrayList<>();
        try {
            touchlist_list.clear();
            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_UP_ROUTE +" WHERE "+ KEY_ROUTE_ID + "=" +route_id;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("latitude", cursor.getString(2));
                    map.put("longitude", cursor.getString(3));
                    //Adding contact to list
                    getdata.add(map);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return getdata;
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("all_contact", "" + e);
        }

        return getdata;
    }

    // Getting contacts Count
    public int Get_UPRouteCount() {
        int count = 0;
        String countQuery = "SELECT  * FROM " + TABLE_UP_ROUTE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();

        db.close();
        return count;
    }

    public LatLng GetLatlng(String lat, String lng) {

        LatLng latLng = null;
        try {
            // Select All Query
            //String selectQuery = "SELECT up_route.*, ROUND( 3956 * acos( cos( radians('"+lat+"') ) * cos( radians(latitude) ) * cos( radians(longitude) - radians('"+lng+"') ) + sin( radians('"+lat+"') ) * sin( radians(latitude) ) ) ,8) as distance FROM up_route where latitude !=0 and longitude !=0 order by distance ASC Limit 1";
            String selectQuery = "SELECT row_id, latitude, longitude, distance(Double.parseDouble(latitude), Double.parseDouble(longitude), '"+Double.parseDouble(lat)+"', '"+Double.parseDouble(lng)+"') AS distance FROM up_route where latitude !=0 and longitude !=0 order by distance ASC Limit 1";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    DatabaseRoutemodel contact = new DatabaseRoutemodel();
                    double latitude = Double.parseDouble(cursor.getString(2));
                    double longitude = Double.parseDouble(cursor.getString(3));

                    latLng = new LatLng(latitude, longitude);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

            return latLng;
        }catch (Exception e) {
            // TODO: handle exception
            Log.e("all_contact", "" + e);
        }
        return latLng;
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public ArrayList<String> Get_RouteId(){
        ArrayList<String> routeArr = new ArrayList<>();
        try {
            touchlist_list.clear();
            // Select All Query
            String selectQuery = "SELECT DISTINCT " + KEY_ROUTE_ID + " FROM " + TABLE_UP_ROUTE;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    DatabaseRoutemodel contact = new DatabaseRoutemodel();

                    routeArr.add(cursor.getString(0));
                    //Adding contact to list
                    touchlist_list.add(contact);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
            return routeArr;
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("all_contact", "" + e);
        }

        return routeArr;
    }

    public void deleteAll() {

        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_UP_ROUTE, null, null);

        db.close();
    }
}
