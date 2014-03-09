package info.curtbinder.farmgame.db;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

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
                // TODO delete all games
                // delete from GamesTable;
                // delete from PlayersTable;
                break;
            case CODE_GAMES_ID:
                // TODO delete a specific game
                // delete from GamesTable where gameid = ID
                // delete from PlayersTable where gameid = ID
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
                /*
                TODO perform bulk insert in Players table of the 6 players
                and their default values
                */
                path = PATH_GAMES;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(path + "/" + id);
    }

    @Override
    public boolean onCreate() {
        data = new MainDatabase(getContext());
        return ((data == null) ? false : true);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
