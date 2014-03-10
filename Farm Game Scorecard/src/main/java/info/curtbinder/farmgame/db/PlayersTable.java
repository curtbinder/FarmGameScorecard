package info.curtbinder.farmgame.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by binder on 2/26/14.
 */
public class PlayersTable {

    public static final String TABLE_NAME = "players";

    public static final String COL_ID = "id";
    public static final String COL_GAMEID = "gameid";
    public static final String COL_PLAYERID = "playerid";
    public static final String COL_NAME = "name";
    public static final String COL_HAY = "hay";
    public static final String COL_GRAIN = "grain";
    public static final String COL_FRUIT = "fruit";
    public static final String COL_LIVESTOCK = "livestock";
    public static final String COL_TRACTOR = "tractor";
    public static final String COL_HARVESTOR = "harvestor";
    public static final String COL_TOPPENISH = "toppenish";
    public static final String COL_CASCADE = "cascade";
    public static final String COL_RATTLESNAKE = "rattlesnake";
    public static final String COL_AHTANUM = "ahtanum";
    public static final String COL_P10K = "p10k";
    public static final String COL_P5K = "p5k";
    public static final String COL_P1K = "p1k";
    public static final String COL_10000 = "10000";
    public static final String COL_5000 = "5000";
    public static final String COL_1000 = "1000";
    public static final String COL_500 = "500";
    public static final String COL_100 = "100";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_GAMEID + " INTEGER, " +
            COL_PLAYERID + " INTEGER, " + COL_NAME + " TEXT, " + COL_HAY + " INTEGER, " +
            COL_GRAIN + " INTEGER, " + COL_FRUIT + " INTEGER, " + COL_LIVESTOCK + " INTEGER, " +
            COL_TRACTOR + " INTEGER, " + COL_HARVESTOR + " INTEGER, " + COL_TOPPENISH + " INTEGER, " +
            COL_CASCADE + " INTEGER, " + COL_RATTLESNAKE + " INTEGER, " + COL_AHTANUM + " INTEGER, " +
            COL_P10K + " INTEGER, " + COL_P5K + " INTEGER, " + COL_P1K + " INTEGER, " +
            COL_10000 + " INTEGER, " + COL_5000 + " INTEGER, " + COL_1000 + " INTEGER, " +
            COL_500 + " INTEGER, " + COL_100 + " INTEGER);";

    /*
    CREATE TABLE IF NOT EXISTS players (id INTEGER PRIMARY KEY AUTOINCREMENT,
    gameid INTEGER, playerid INTEGER, name TEXT, hay INTEGER, grain INTEGER,
    fruit INTEGER, livestock INTEGER, tractor INTEGER, harvestor INTEGER,
    toppenish INTEGER, cascade INTEGER, rattlesnake INTEGER, ahtanum INTEGER,
    p10k INTEGER, p5k INTEGER, p1k INTEGER, 10000 INTEGER, 5000 INTEGER,
    1000 INTEGER, 500 INTEGER, 100 INTEGER);
     */
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static void onCreate(SQLiteDatabase db) {
        Log.d("PlayersTable", "Create: " + CREATE_TABLE);
        //db.execSQL( CREATE_TABLE );
    }

    public static void onUpgrade (
            SQLiteDatabase db,
            int oldVersion,
            int newVersion ) {
        // initially, just drop tables and create new ones
        dropTable( db );
        onCreate( db );
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL( DROP_TABLE );
    }

    public static void onDowngrade (
            SQLiteDatabase db,
            int oldVersion,
            int newVersion ) {
        dropTable( db );
    }
}
