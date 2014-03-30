/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 by Curt Binder (http://curtbinder.info)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package info.curtbinder.farmgame.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by binder on 2/26/14.
 */
public class GamesTable {

    public static final String TABLE_NAME = "games";

    public static final String COL_ID = "_id";
    public static final String COL_DATE = "creation";
    public static final String COL_NAME = "name";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_DATE + " TEXT NOT NULL, " + COL_NAME + " TEXT);";

    // create table if not exists games (id integer primary key autoincrement
    //    , creation text not null, name text);

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static void onCreate(SQLiteDatabase db) {
        //Log.d("GamesTable", "Create: " + CREATE_TABLE);
        db.execSQL( CREATE_TABLE );
    }

    public static void onUpgrade (
            SQLiteDatabase db,
            int oldVersion,
            int newVersion ) {
        // initially, just drop tables and create new_shadow ones
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
