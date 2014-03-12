/*
 * Copyright (c) 2014 by Curt Binder (http://curtbinder.info)
 *
 * This work is licensed under the Creative Commons
 * Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-sa/4.0/deed.en_US
 */

package info.curtbinder.farmgame.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by binder on 2/26/14.
 */
public class MainDatabase extends SQLiteOpenHelper {

    public static final String DB_NAME = "farmgames.db";
    public static final int DB_VERSION = 2;

    public MainDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        GamesTable.onCreate(sqLiteDatabase);
        PlayersTable.onCreate(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        GamesTable.onUpgrade(sqLiteDatabase, i, i2);
        PlayersTable.onUpgrade(sqLiteDatabase, i, i2);
    }
}
