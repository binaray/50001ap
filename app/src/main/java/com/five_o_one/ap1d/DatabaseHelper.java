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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

/**
 * Created by Choco〜bourbon on 24-Nov-17.
 *
 * Note when adding new resources to project reset database as image resource ids may change
 * Reset will be required for this case
 *
 */

public class DatabaseHelper extends SQLiteOpenHelper{
    //-----Variables------
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "db";
    private final Context context;
    private static SQLiteDatabase db;
    private static DatabaseHelper sInstance;
//    private DatabaseListener listener;

    //<---Start of DB fields-->
    private static final String TABLE_NAME = "map";
    public final static String COLUMN_ID = "ID";
    public final static String COLUMN_Name = "Name";
    public final static String COLUMN_Details = "Details";
    public final static String COLUMN_Paths = "Paths";
    public final static String COLUMN_ImageUrl = "ImageUrl";
    public final static String COLUMN_Selected = "Selected";

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

    @Override
    public void onCreate(final SQLiteDatabase db) {
        //Unique auto-increment id possibly needed
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_NAME +
                "(" +
                COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                COLUMN_Name + " TEXT," +
                COLUMN_Details + " TEXT," +
                COLUMN_Paths + " TEXT," +
                COLUMN_ImageUrl + " INTEGER," +
                COLUMN_Selected + " INTEGER" +
                ")";

        db.execSQL(CREATE_USERS_TABLE);
        initData(db);
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
        Boolean status=true;
        try {
            final GsonBuilder builder=new GsonBuilder();
            builder.serializeSpecialFloatingPointValues();

            for (LocationData data: dataList) {
                ContentValues values = new ContentValues();
                Gson gson=builder.create();
                String json=gson.toJson(data.getPaths());
                Log.v("json: ",json);

                values.put(COLUMN_Name,data.getName());
                values.put(COLUMN_Details,data.getDetails());
                values.put(COLUMN_Paths,json);
                values.put(COLUMN_ImageUrl,data.getImageUrl());
                values.put(COLUMN_Selected,data.isSelected());

                db.insertOrThrow(TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            status=false;
        } finally {
            db.endTransaction();
        }
    }

    public void updateSelected(LocationData data)
    {
        if (db==null) db = getWritableDatabase();
        db.beginTransaction();
        try{
            ContentValues values = new ContentValues();
            values.put(COLUMN_Selected, Integer.toString(data.isSelected()));
//            values.put(COLUMN_Details,"boboboobobo");
//            db.update(TABLE_NAME, values, COLUMN_ID + "=" + Integer.toString(data.getId()), null);
//            db.update(TABLE_NAME, values, COLUMN_Name + " = ?", new String[] { data.getName() });
            String[] whereArgs = {String.valueOf(data.getId())};
            int success=db.update(TABLE_NAME, values, COLUMN_ID +"=?", whereArgs );
            Log.v("Selections updated", String.valueOf(success)+"with val of"+data.isSelected());
            db.setTransactionSuccessful();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        db.endTransaction();
    }

    List<LocationData> getDataList(){
        db = getReadableDatabase();
        List<LocationData> datalist=new ArrayList();
        Cursor c= db.query(TABLE_NAME, null, null, null, null, null, null);
        if (c != null) {
            final GsonBuilder builder=new GsonBuilder();
            builder.serializeSpecialFloatingPointValues();
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                Gson gson=builder.create();
                LocationData data=new LocationData();
                Path[] paths=gson.fromJson(c.getString(c.getColumnIndex(COLUMN_Paths)),Path[].class);
                data.setId(c.getInt(c.getColumnIndex(COLUMN_ID)));
                data.setName(c.getString(c.getColumnIndex(COLUMN_Name)));
                data.setDetails(c.getString(c.getColumnIndex(COLUMN_Details)));
                data.setPaths(paths);     //not used in MainActivity
                data.setImageUrl(c.getInt(c.getColumnIndex(COLUMN_ImageUrl)));
                data.setSelected(c.getInt(c.getColumnIndex(COLUMN_Selected)));
                Log.v("Location data:",data.toString());
                datalist.add(data);
            }
            c.close();
        }
        return datalist;
    }

    List<LocationData> getSelectedDataList(){
        db = getReadableDatabase();
        List<LocationData> datalist=new ArrayList();
        final GsonBuilder builder=new GsonBuilder();
        builder.serializeSpecialFloatingPointValues();

        final String WHERE0 = "CAST("+COLUMN_ID+" as TEXT) = ?";
        final String[] WHEREARGS0 = new String[] { "1" };
        Cursor c0= db.query(TABLE_NAME, null, WHERE0, WHEREARGS0, null, null, null);
        if (c0 != null) {
            for (c0.moveToFirst(); !c0.isAfterLast(); c0.moveToNext()) {
                Gson gson=builder.create();
                LocationData data=new LocationData();
                Path[] paths=gson.fromJson(c0.getString(c0.getColumnIndex(COLUMN_Paths)),Path[].class);

                data.setName(c0.getString(c0.getColumnIndex(COLUMN_Name)));
                data.setDetails(c0.getString(c0.getColumnIndex(COLUMN_Details)));
                data.setPaths(paths);
                data.setImageUrl(c0.getInt(c0.getColumnIndex(COLUMN_ImageUrl)));
                data.setSelected(c0.getInt(c0.getColumnIndex(COLUMN_Selected)));
                Log.v("Selected data:",data.toString());
                datalist.add(data);
            }
            c0.close();
        }

        final String WHERE = "CAST("+COLUMN_Selected+" as TEXT) = ?";
        final String[] WHEREARGS = new String[] { "1" };
        Cursor c= db.query(TABLE_NAME, null, WHERE, WHEREARGS, null, null, null);
        if (c != null) {
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                Gson gson=builder.create();
                LocationData data=new LocationData();
                Path[] paths=gson.fromJson(c.getString(c.getColumnIndex(COLUMN_Paths)),Path[].class);

                data.setName(c.getString(c.getColumnIndex(COLUMN_Name)));
                data.setDetails(c.getString(c.getColumnIndex(COLUMN_Details)));
                data.setPaths(paths);
                data.setImageUrl(c.getInt(c.getColumnIndex(COLUMN_ImageUrl)));
                data.setSelected(c.getInt(c.getColumnIndex(COLUMN_Selected)));
                Log.v("Selected data:",data.toString());
                datalist.add(data);
            }
            c.close();
        }
        return datalist;
    }


/*-----------------------------------------
* Initialization functions
* ----------------------------------------*/
    //Default Initial Values
    static final String[] locationNames={"Marina Bay Sands","Singapore Flyer","Resort World Sentosa",
            "VivoCity","Buddha Tooth Relic Temple","Singapore Zoo","Marina Barrage",
            "Esplanade-Theatres on the Bay", "TreeTop Walk","Maxwell Food Center"};
    static final String[] locationDetails={"Marina Bay Sands is one of two winning proposals for Singapore's first integrated resorts. The complex is topped by a 340-metre-long (1,120 ft) SkyPark with a capacity of 3,900 people and a 150 m (490 ft) infinity swimming pool, set on top of the world's largest public cantilevered platform.",
            "The Singapore Flyer is a giant Ferris wheel in Singapore. The Flyer has an overall height of 165 metres (541 ft) and was the world's tallest Ferris wheel until the 167.6 m (550 ft) High Roller.",
            "Resorts World Sentosa (Abbreviation: RWS) is an integrated resort on the island of Sentosa, off the southern coast of Singapore. The key attractions include one of Singapore's two casinos, a Universal Studios theme park, Adventure Cove Water Park, and S.E.A. Aquarium, which includes the world's largest oceanarium.",
            "VivoCity (Chinese: 怡丰城) is the largest shopping mall in Singapore. Located in the HarbourFront precinct of Bukit Merah, it was designed by the Japanese architect Toyo Ito. Its name is derived from the word vivacity.",
            "The Buddha Tooth Relic Temple and Museum is a Buddhist temple and museum complex located in the Chinatown district of Singapore.",
            "The Singapore Zoo, formerly known as the Singapore Zoological Gardens and commonly known locally as the Mandai Zoo, occupies 28 hectares (69 acres) on the margins of Upper Seletar Reservoir within Singapore's heavily forested central catchment area.",
            "Built across the mouth of Marina Channel, Marina Barrage (MB) creates Singapore’s 15th reservoir, and the first in the heart of the city.",
            "Esplanade – Theatres on the Bay, also known as the Esplanade Theatre or simply The Esplanade, is a 60,000 square metres (6.0 ha) performing arts centre located in Marina Bay near the mouth of the Singapore River. Named after the nearby Esplanade Park, it consists of a concert hall which seats about 1,600 and a theatre with a capacity of about 2,000 for the performing arts.",
            "The HSBC TreeTop Walk opened to public on 5 November 2004. It connects the two highest points in MacRitchie – Bukit Pierce and Bukit Kalang. At the highest point, the bridge hangs 25 metres from the forest floor.",
            "The Maxwell Food Centre dates back to pre-war days as a fresh food market and food centre. In 1986, it was converted into a food centre, housing hawkers from the vicinity. The present existing hawker centre was renovated in 2001."};


    private void initData(SQLiteDatabase db){
        Path [][] adjmat = new Path[10][10];

        Path infPath = new Path(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
//        locationNames.put(0,"Marina Bay Sands");
        adjmat[0][0] = infPath.adddirection(0,0);
        adjmat[0][1] = new Path(14,0,17,1.83,3,3.22,0,1);
        adjmat[0][2] = new Path(69,0,26,1.18,14,6.96,0,2);
        adjmat[0][3] = new Path(76,0,35,4.03,19,8.50,0,3);
        adjmat[0][4] = new Path(28,0,19,0.88,8,4.98,0,4);
        adjmat[0][5] = new Path(269,0,84,1.96,30,18.40,0,5);
        adjmat[0][6] = new Path(19,0,44,0.87,6,4.76,0,6);
        adjmat[0][7] = new Path(14,0,25,0.77,4,4.43,0,7);
        adjmat[0][8] = new Path(155,0,68,1.37,21,11.30,0,8);
        adjmat[0][9] = new Path(32,0,18,0.77,6,4.90,0,9);

//        locationNames.put(1,"Singapore Flyer");
        adjmat[1][0] = new Path(14,0,17,0.83,6,4.32,1,0);
        adjmat[1][1] = infPath.adddirection(1,1);
        adjmat[1][2] = new Path(81,0,31,1.26,13,7.84,1,2);
        adjmat[1][3] = new Path(88,0,38,4.03,18,9.36,1,3);
        adjmat[1][4] = new Path(39,0,24,0.98,8,4.76,1,4);
        adjmat[1][5] = new Path(264,0,85,1.89,29,18.18,1,5);
        adjmat[1][6] = new Path(29,0,50,0.97,8,5.47,1,6);
        adjmat[1][7] = new Path(12,0,21,0.77,3,3.94,1,7);
        adjmat[1][8] = new Path(151,0,70,1.33,21,10.71,1,8);
        adjmat[1][9] = new Path(38,0,24,0.77,6,5.25,1,9);

//        locationNames.put(2,"Vivo City");
        adjmat[2][0] = new Path(69,0,24,1.18,12,8.30,2,0);
        adjmat[2][1] = new Path(81,0,29,1.26,14,7.96,2,1);
        adjmat[2][2] = infPath.adddirection(2,2);
        adjmat[2][3] = new Path(12,0,10,2.00,9,4.54,2,3);
        adjmat[2][4] = new Path(47,0,18,0.98,11,6.42,2,4);
        adjmat[2][5] = new Path(270,0,85,1.99,31,22.58,2,5);
        adjmat[2][6] = new Path(91,0,55,1.16,13,8.19,2,6);
        adjmat[2][7] = new Path(68,0,31,1.07,13,9.58,2,7);
        adjmat[2][8] = new Path(176,0,72,1.49,26,15.73,2,8);
        adjmat[2][9] = new Path(48,0,17,0.77,11,8.87,2,9);

//        locationNames.put(3,"Resort World Sentosa");
        adjmat[3][0] = new Path(76,0,33,1.18,13,8.74,3,0);
        adjmat[3][1] = new Path(88,0,38,1.26,14,8.40,3,1);
        adjmat[3][2] = new Path(12,0,10,0.00,4,3.22,3,2);
        adjmat[3][3] = infPath.adddirection(3,3);
        adjmat[3][4] = new Path(55,0,27,0.98,12,6.64,3,4);
        adjmat[3][5] = new Path(285,0,92,1.99,32,22.80,3,5);
        adjmat[3][6] = new Path(105,0,63,1.16,19,9.26,3,6);
        adjmat[3][7] = new Path(81,0,34,0.97,19,9.65,3,7);
        adjmat[3][8] = new Path(189,0,78,1.49,32,16.92,3,8);
        adjmat[3][9] = new Path(62,0,25,0.77,17,8.89,3,9);

//        locationNames.put(4, "Buddha Tooth Relic Temple");
        adjmat[4][0] = new Path(28,0,18,0.88,7,5.32,4,0);
        adjmat[4][1] = new Path(39,0,23,0.98,8,4.76,4,1);
        adjmat[4][2] = new Path(47,0,19,0.98,9,4.98,4,2);
        adjmat[4][3] = new Path(55,0,28,3.98,14,6.52,4,3);
        adjmat[4][4] = infPath.adddirection(4,4);
        adjmat[4][5] = new Path(264,0,83,1.91,30,18.40,4,5);
        adjmat[4][6] = new Path(48,0,47,0.87,9,6.17,4,6);
        adjmat[4][7] = new Path(23,0,24,0.77,1,6.67,4,7);
        adjmat[4][8] = new Path(155,0,70,1.33,20,12.48,4,8);
        adjmat[4][9] = new Path(1,0,Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY,1,3.60,4,9);

//        locationNames.put(5, "Zoo");
        adjmat[5][0] = new Path(269,0,86,1.88,32,22.48,5,0);
        adjmat[5][1] = new Path(264,0,87,1.96,29,19.40,5,1);
        adjmat[5][2] = new Path(270,0,86,2.11,32,21.48,5,2);
        adjmat[5][3] = new Path(285,0,96,4.99,36,23.68,5,3);
        adjmat[5][4] = new Path(264,0,84,1.91,30,21.60,5,4);
        adjmat[5][5] = infPath.adddirection(5,5);
        adjmat[5][6] = new Path(298,0,106,1.90,33,24.43,5,6);
        adjmat[5][7] = new Path(273,0,67,1.75,28,19.93,5,7);
        adjmat[5][8] = new Path(183,0,104,1.72,21,14.66,5,8);
        adjmat[5][9] = new Path(287,0,80,1.88,29,22.27,5,9);

//        locationNames.put(6,"Marina Barrage");
        adjmat[6][0] = new Path(19,0,41,0.87,6,5.99,6,0);
        adjmat[6][1] = new Path(27,0,48,0.77,10,7.02,6,1);
        adjmat[6][2] = new Path(90,0,54,1.16,12,8.17,6,2);
        adjmat[6][3] = new Path(103,0,58,4.16,26,10.58,6,3);
        adjmat[6][4] = new Path(49,0,45,0.87,10,6.81,6,4);
        adjmat[6][5] = new Path(300,0,104,1.90,30,23.96,6,5);
        adjmat[6][6] = infPath.adddirection(6,6);
        adjmat[6][7] = new Path(28,0,48,0.87,10,7.30,6,7);
        adjmat[6][8] = new Path(169,0,99,1.49,25,16.11,6,8);
        adjmat[6][9] = new Path(49,0,45,0.77,9,6.80,6,9);

//        locationNames.put(7,"Esplanade");
        adjmat[7][0] = new Path(14,0,25,0.77,5,4.56,7,0);
        adjmat[7][1] = new Path(12,0,21,0.77,4,4.61,7,1);
        adjmat[7][2] = new Path(67,0,32,1.07,10,6.62,7,2);
        adjmat[7][3] = new Path(80,0,33,3.97,21,8.34,7,3);
        adjmat[7][4] = new Path(23,0,24,0.77,4,4.57,7,4);
        adjmat[7][5] = new Path(275,0,76,1.85,27,18.75,7,5);
        adjmat[7][6] = new Path(28,0,49,0.87,8,5.46,7,6);
        adjmat[7][7] = infPath.adddirection(7,7);
        adjmat[7][8] = new Path(145,0,70,1.33,20,11.16,7,8);
        adjmat[7][9] = new Path(25,0,22,0.77,4,4.56,7,9);

//        locationNames.put(8,"Treetop Walk at Macrithie Reservoir");
        adjmat[8][0] = new Path(153,0,64,1.37,23,12.74,8,0);
        adjmat[8][1] = new Path(148,0,66,1.29,21,11.54,8,1);
        adjmat[8][2] = new Path(172,0,71,1.45,27,18.58,8,2);
        adjmat[8][3] = new Path(186,0,78,4.53,38,20.49,8,3);
        adjmat[8][4] = new Path(151,0,68,1.33,23,16.29,8,4);
        adjmat[8][5] = new Path(182,0,100,1.69,18,11.64,8,5);
        adjmat[8][6] = new Path(166,0,98,1.49,28,14.43,8,6);
        adjmat[8][7] = new Path(141,0,69,1.37,20,11.64,8,7);
        adjmat[8][8] = infPath.adddirection(8,8);
        adjmat[8][9] = new Path(152,0,71,1.37,22,16.28,8,9);

//        locationNames.put(9,"Maxwell Road Hawker Center");
        adjmat[9][0] = new Path(33,0,17,0.77,6,5.18,9,0);
        adjmat[9][1] = new Path(37,0,23,0.77,7,5.62,9,1);
        adjmat[9][2] = new Path(48,0,18,0.77,9,5.96,9,2);
        adjmat[9][3] = new Path(61,0,26,3.77,20,7.69,9,3);
        adjmat[9][4] = new Path(1,0,Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY,4,3.60,9,4);
        adjmat[9][5] = new Path(289,0,85,1.91,28,23.15,9,5);
        adjmat[9][6] = new Path(49,0,47,0.77,8,6.05,9,6);
        adjmat[9][7] = new Path(24,0,22,0.77,6,5.26,9,7);
        adjmat[9][8] = new Path(156,0,71,1.37,21,12.68,9,98);
        adjmat[9][9] = infPath.adddirection(9,9);


        TypedArray ar = context.getResources().obtainTypedArray(R.array.locationImages);
        List<LocationData> dataList=new ArrayList<LocationData>();
        for (int i = 0; i < ar.length(); i++) {
            dataList.add(new LocationData(locationNames[i],locationDetails[i],ar.getResourceId(i,0),adjmat[i],0));
        }
        ar.recycle();
        sInstance.addLocations(dataList,db);
    }
}
