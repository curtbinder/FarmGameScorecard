/*
 * Copyright (c) 2014 by Curt Binder (http://curtbinder.info)
 *
 * This work is licensed under the Creative Commons
 * Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-sa/4.0/deed.en_US
 */

package info.curtbinder.farmgame.db;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by binder on 2/26/14
 */
public class ScoreProvider extends ContentProvider {

    private MainDatabase data;

    private static final String CONTENT = ScoreProvider.class.getPackage().getName();
    private static final String CONTENT_MIME_TYPE = "/vnd." + CONTENT + ".";
    public static final Uri CONTENT_URI = Uri.parse("content://" + CONTENT);

    // paths
    public static final String PATH_GAMES = "games";
    public static final String PATH_PLAYERS = "players";

    // mime types
    // all games
    public static final String GAMES_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
            CONTENT_MIME_TYPE + PATH_GAMES;
    // only 1 game
    public static final String GAMES_ID_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
            CONTENT_MIME_TYPE + PATH_GAMES;
    // all players
    public static final String PLAYERS_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
            CONTENT_MIME_TYPE + PATH_PLAYERS;
    // 1 player
    public static final String PLAYERS_ID_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
            CONTENT_MIME_TYPE + PATH_PLAYERS;

    // used for UriMatcher
    private static final int CODE_GAMES = 10;
    private static final int CODE_GAMES_ID = 11;
    private static final int CODE_PLAYERS = 20;
    private static final int CODE_PLAYERS_ID = 21;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(CONTENT, PATH_GAMES, CODE_GAMES);
        sUriMatcher.addURI(CONTENT, PATH_GAMES + "/#", CODE_GAMES_ID);
        sUriMatcher.addURI(CONTENT, PATH_PLAYERS, CODE_PLAYERS);
        sUriMatcher.addURI(CONTENT, PATH_PLAYERS + "/#", CODE_PLAYERS_ID);
    }

    public ScoreProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted = 0;
        SQLiteDatabase db = data.getWritableDatabase();
        // Only delete games from the interface. Players get deleted after the games get deleted
        switch ( sUriMatcher.match(uri) ) {
            case CODE_GAMES:
                // delete from GamesTable;
                rowsDeleted = db.delete(GamesTable.TABLE_NAME, "1", null);
                // delete from PlayersTable;
                rowsDeleted += db.delete(PlayersTable.TABLE_NAME, "1", null);
                break;
            case CODE_GAMES_ID:
                // delete from GamesTable where gameid = ID
                rowsDeleted = db.delete(GamesTable.TABLE_NAME, GamesTable.COL_ID + "=?",
                        new String[] { uri.getLastPathSegment() } );
                // delete from PlayersTable where gameid = ID
                rowsDeleted += db.delete(PlayersTable.TABLE_NAME, PlayersTable.COL_GAMEID + "=?",
                        new String[] { uri.getLastPathSegment() } );
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        int match = sUriMatcher.match(uri);
        switch ( match ) {
            case CODE_GAMES:
                return GAMES_MIME_TYPE;
            case CODE_GAMES_ID:
                return GAMES_ID_MIME_TYPE;
            case CODE_PLAYERS:
                return PLAYERS_MIME_TYPE;
            case CODE_PLAYERS_ID:
                return PLAYERS_ID_MIME_TYPE;
            default:
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = 0;
        String path = null;
        SQLiteDatabase db = data.getWritableDatabase();
        switch ( sUriMatcher.match(uri) ) {
            case CODE_GAMES:
                id = db.insert(GamesTable.TABLE_NAME, null, values);
                insertGamePlayers(db, id);
                path = PATH_GAMES;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(path + "/" + id);
    }

    private ContentValues getDefaultPlayerValues(long gameId, int id) {
        ContentValues cv = new ContentValues();
        cv.put(PlayersTable.COL_GAMEID, gameId);
        cv.put(PlayersTable.COL_PLAYERID, id);
        cv.put(PlayersTable.COL_TOTAL, 0);
        return cv;
    }

    private void insertGamePlayers(SQLiteDatabase db, long gameId) {
        for ( int i = 0; i < 6; i++ ) {
            db.insert(PlayersTable.TABLE_NAME, null, getDefaultPlayerValues(gameId, i+1));
        }
    }

    @Override
    public boolean onCreate() {
        data = new MainDatabase(getContext());
        return ((data == null) ? false : true);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        String table = null;
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch ( sUriMatcher.match(uri) ) {
            case CODE_GAMES:
                // query all games, sorted descending, most recent first
                table = GamesTable.TABLE_NAME;
                break;
            case CODE_GAMES_ID:
                // query 1 game
                table = GamesTable.TABLE_NAME;
                qb.appendWhere(GamesTable.COL_ID + "="
                    + uri.getLastPathSegment());
                break;
            case CODE_PLAYERS:
                // query all players for a specified game
                table = PlayersTable.TABLE_NAME;
                break;
            case CODE_PLAYERS_ID:
                // query 1 player for a specified game
                table = PlayersTable.TABLE_NAME;
                qb.appendWhere(PlayersTable.COL_PLAYERID + "="
                    + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        qb.setTables(table);
        SQLiteDatabase db = data.getWritableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs,
                            null, null, sortOrder, null);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        int rowsUpdated = 0;
        SQLiteDatabase db = data.getWritableDatabase();
        switch ( sUriMatcher.match(uri) ) {
            case CODE_PLAYERS:
                rowsUpdated = db.update(PlayersTable.TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            case CODE_GAMES:
                rowsUpdated = db.update(GamesTable.TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
