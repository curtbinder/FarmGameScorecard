package info.curtbinder.farmgame.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by binder on 2/26/14.
 */
public class GamesTable {

    public static final String TABLE_NAME = "games";

    public static final String COL_ID = "id";
    public static final String COL_DATE = "creation";
    public static final String COL_NAME = "name";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_DATE + " TEXT NOT NULL, " + COL_NAME + " TEXT);";

    // create table if not exists games (id integer primary key autoincrement
    //    , creation text not null, name text);

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static void onCreate(SQLiteDatabase db) {
        Log.d("GamesTable", "Create: " + CREATE_TABLE);
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
