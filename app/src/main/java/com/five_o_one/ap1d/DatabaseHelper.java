package com.five_o_one.ap1d;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

/**
 * Created by Choco〜bourbon on 24-Nov-17.
 */

public class DatabaseHelper extends SQLiteOpenHelper{
    //-----Variables------
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "db";

    private final Context context;
    private static SQLiteDatabase db;
    private static DatabaseHelper sInstance;

    //<---Start of DB fields-->
    private static final String TABLE_NAME = "map";
    public final static String COLUMN_ID = "ID";
    public final static String COLUMN_Name = "Name";
    public final static String COLUMN_Details = "Details";
    public final static String COLUMN_ImageUrl = "ImageUrl";
    public final static String[] ALL_COLUMNS = new String[]{COLUMN_Name, COLUMN_Details,COLUMN_ImageUrl};


    //Use getInstance to initialize instead of this
    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context);
        }
        return sInstance;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onCreate(final SQLiteDatabase db) {
        //Unique auto-increment id possibly needed
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_NAME +
                "(" +
                COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                COLUMN_Name + " TEXT," +
                COLUMN_Details + " TEXT," +
                COLUMN_ImageUrl + " INTEGER" +
                ")";

        db.execSQL(CREATE_USERS_TABLE);

        //initData(db);
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle("Please Wait");
        pd.setMessage("Adding Location(s)");
        pd.show();
        new AsyncTask<String, Integer, Exception>() {
            @Override
            protected Exception doInBackground(String... strings) {
                try {
                    initData(db);
                }
                catch (Exception e){
                    return e;
                }
                return null;
            }
            @Override
            protected void onPostExecute(Exception result) {
                pd.dismiss();
                if (result==null) Toast.makeText(context, "Locations successfully added", Toast.LENGTH_SHORT).show();
                else Toast.makeText(context, "Failed to add locations", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    public void addLocations(final List<LocationData> dataList, SQLiteDatabase db) { // Add event into database /true = success /false = error
        // Create and/or open the database for writing
        if (db==null) db = getWritableDatabase();
        db.beginTransaction();
//        final ProgressDialog pd = new ProgressDialog(con);
//        pd.setTitle("Please Wait");
//        pd.setMessage("Adding Location(s)");
//        pd.show();

        Boolean status=true;
        try {
            for (LocationData data: dataList) {
                ContentValues values = new ContentValues();

                values.put(COLUMN_Name,data.getName());
                values.put(COLUMN_Details,data.getDetails());
                values.put(COLUMN_ImageUrl,data.getImageUrl());

                db.insertOrThrow(TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            status=false;
        } finally {
            db.endTransaction();
//            pd.dismiss();
        }
    }


    List<LocationData> getDataList(){
        db = getReadableDatabase();
        List<LocationData> datalist=new ArrayList();
        Cursor c= db.query(TABLE_NAME, null, null, null, null, null, null);
        if (c != null) {
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                LocationData data=new LocationData();
                data.setName(c.getString(c.getColumnIndex(COLUMN_Name)));
                data.setDetails(c.getString(c.getColumnIndex(COLUMN_Details)));
                data.setImageUrl(c.getInt(c.getColumnIndex(COLUMN_ImageUrl)));
                Log.v("data:",data.toString());
//                Bundle temp2 = new Bundle();
//                for (String COL:ALL_COLUMNS_USER_ENTER) {
//                    temp2.putString(COL,c.getString(c.getColumnIndex(COL)));
//                }
                datalist.add(data);
            }
            c.close();
        }
        return datalist;
    }

    //Default Initial Values
    static final String[] locationNames={"Marina Barrage","Esplanade","Treetop Walk",
            "Maxwell Walk Hawker Center","Marina Bay Sans","Vivo City","Singapore Zoo",
            "Buddha Tooth Relic Temple", "Resort Word Sentosa","Marina Barrage"};
    static final String[] locationDetails={"Detail0","Detail1","Detail2","Detail3","Detail4","Detail5","","","",""};


    private void initData(SQLiteDatabase db){
        TypedArray ar = context.getResources().obtainTypedArray(R.array.locationImages);
        int len = ar.length();
        int[] imgIds = new int[len];
        List<LocationData> dataList=new ArrayList<LocationData>();
        for (int i = 0; i < len; i++) {
            dataList.add(new LocationData(locationNames[i],locationDetails[i],ar.getResourceId(i,0)));
        }
        ar.recycle();
        sInstance.addLocations(dataList,db);
    }
}
