package com.five_o_one.ap1d;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Choco〜bourbon on 24-Nov-17.
 */

public class DatabaseHelper extends SQLiteOpenHelper{
    //-----Variables------
    public static final int DB_VERSION = 1;
    private static final String DB_NAME = "db";

    private final Context context;
    protected static SQLiteDatabase db;
    private static DatabaseHelper sInstance;

    private SharedPreferences prefs;

    //<---Start of DB fields-->
    private static final String TABLE_NAME = "map";
    public final static String COLUMN_ID = "ID";
    public final static String COLUMN_FID = "FID";
    public final static String COLUMN_EventType = "EventType"; //if ==100xxxx admin else unique event/timetable
    public final static String COLUMN_Event = "Event";
    public final static String[] ALL_COLUMNS_USER_ENTER = new String[]{COLUMN_EventType, COLUMN_Event};


    //Use getInstance to initialize instead of this
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Unique auto-increment id possibly needed
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_NAME +
                "(" +
                COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                COLUMN_FID + " TEXT," +
                COLUMN_EventType + " TEXT," +
                ")";

        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

//    public void addLocalEvents(final List<Event> events, final Context con) { // Add event into database /true = success /false = error
//
//        // Create and/or open the database for writing
//        db = getWritableDatabase();
//        db.beginTransaction();
//        final ProgressDialog pd = new ProgressDialog(con);
//        pd.setTitle("Please Wait");
//        pd.setMessage("Adding Event(s)");
//        pd.show();
//
//        Boolean status=true;
//        try {
//            for (Event e: events) {
//                Bundle data=e.getBundle();
//                ContentValues values = new ContentValues();
//                for (int m = 0; ALL_COLUMNS_USER_ENTER.length > m; m++) {
//                    if (data.getString(ALL_COLUMNS_USER_ENTER[m]) != null) {
//                        values.put(ALL_COLUMNS_USER_ENTER[m], data.getString(ALL_COLUMNS_USER_ENTER[m]));
//                    }
//                }
//                db.insertOrThrow(TABLE_NAME, null, values);
//            }
//            db.setTransactionSuccessful();
//        } catch (Exception e) {
//            status=false;
//        } finally {
//            db.endTransaction();
//            pd.dismiss();
//            if (status) Toast.makeText(con, "Event successfully added", Toast.LENGTH_SHORT).show();
//            else Toast.makeText(con, "Failed to add event", Toast.LENGTH_SHORT).show();
//        }
//    }
}
