package com.example.chargepointapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.chargepointapplication.Models.ChargePoint;
import com.example.chargepointapplication.Models.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "chargepoint_db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_ROLE = "role";

    private static final String TABLE_CHARGE_POINTS = "chargepoints";
    private static final String COLUMN_REFERENCE_ID = "referenceID";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_TOWN = "town";
    private static final String COLUMN_COUNTY = "county";
    private static final String COLUMN_POSTCODE = "postcode";
    private static final String COLUMN_CHARGE_DEVICE_STATUS = "chargeDeviceStatus";
    private static final String COLUMN_CONNECTOR_ID = "connectorID";
    private static final String COLUMN_CONNECTOR_TYPE = "connectorType";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_EMAIL + " TEXT PRIMARY KEY,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_ROLE + " TEXT)";

        String CREATE_CHARGEPOINTS_TABLE = "CREATE TABLE " + TABLE_CHARGE_POINTS + "("
                + COLUMN_REFERENCE_ID + " TEXT PRIMARY KEY,"
                + COLUMN_LATITUDE + " DOUBLE,"
                + COLUMN_LONGITUDE + " DOUBLE,"
                + COLUMN_TOWN + " TEXT,"
                + COLUMN_COUNTY + " TEXT,"
                + COLUMN_POSTCODE + " TEXT,"
                + COLUMN_CHARGE_DEVICE_STATUS + " TEXT,"
                + COLUMN_CONNECTOR_ID + " TEXT,"
                + COLUMN_CONNECTOR_TYPE + " TEXT)";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_CHARGEPOINTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHARGE_POINTS);
        onCreate(db);
    }

    //This would add user into the database.
    public void addUser(User user) {

        //  get an instance of database
        SQLiteDatabase db = this.getWritableDatabase();

        //  get the values which needs to be replaced
        ContentValues values = new ContentValues();
        //  update the values
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_ROLE, user.getRole());

        //  inserts the replaced data into the database
        db.insert(TABLE_USERS, null, values);
        db.close();  // database closing
    }

    public boolean validateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_EMAIL, COLUMN_PASSWORD};
        Cursor cursor = db.query(TABLE_USERS, columns, COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{email, password}, null, null, null);

        boolean userExists = cursor.getCount() > 0;
        cursor.close();
        return userExists;
    }

    public String getUserRole(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ROLE};
        Cursor cursor = db.query(TABLE_USERS, columns, COLUMN_EMAIL + "=?",
                new String[]{email}, null, null, null);
        String role = "";
        if (cursor != null && cursor.moveToFirst()) {
            role = cursor.getString(cursor.getColumnIndex(COLUMN_ROLE));
        }
        cursor.close();
        return role;
    }

    public void addChargePoint(ChargePoint chargePoint) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_REFERENCE_ID, chargePoint.getReferenceID());
        values.put(COLUMN_LATITUDE, chargePoint.getLatitude());
        values.put(COLUMN_LONGITUDE, chargePoint.getLongitude());
        values.put(COLUMN_TOWN, chargePoint.getTown());
        values.put(COLUMN_COUNTY, chargePoint.getCounty());
        values.put(COLUMN_POSTCODE, chargePoint.getPostcode());
        values.put(COLUMN_CHARGE_DEVICE_STATUS, chargePoint.getChargeDeviceStatus());
        values.put(COLUMN_CONNECTOR_ID, chargePoint.getConnectorID());
        values.put(COLUMN_CONNECTOR_TYPE, chargePoint.getConnectorType());

        db.insert(TABLE_CHARGE_POINTS, null, values);
        db.close();
    }

    public List<ChargePoint> getAllChargePoints() {
        List<ChargePoint> chargePoints = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_CHARGE_POINTS;

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                ChargePoint chargePoint = new ChargePoint(
                        cursor.getString(cursor.getColumnIndex(COLUMN_REFERENCE_ID)),
                        cursor.getDouble(cursor.getColumnIndex(COLUMN_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGITUDE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_TOWN)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_COUNTY)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_POSTCODE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CHARGE_DEVICE_STATUS)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CONNECTOR_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CONNECTOR_TYPE))
                );
                chargePoints.add(chargePoint);
            }
            cursor.close();
        }
        return chargePoints;
    }
    public void deleteChargePoint(String referenceID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CHARGE_POINTS, COLUMN_REFERENCE_ID + "=?", new String[]{referenceID});
        db.close();
    }
}

