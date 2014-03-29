/*
 * Copyright (c) 2014 by Curt Binder (http://curtbinder.info)
 *
 * This work is licensed under the Creative Commons
 * Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-sa/4.0/deed.en_US
 */

package info.curtbinder.farmgame;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.text.DateFormat;
import java.util.Date;

import info.curtbinder.farmgame.db.GamesTable;
import info.curtbinder.farmgame.db.ScoreProvider;

/**
 * Created by binder on 2/26/14
 */
public class MainActivity extends Activity implements
        Button.OnClickListener,
        LoadGameDialogFragment.LoadGameDialogListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.buttonNewGame:
                startNewGame();
                break;
            case R.id.buttonHistory:
                loadSavedGame();
                break;
            case R.id.buttonInfo:
                About.displayAbout(this);
                break;
            default:
                return;
        }
    }

    private String getCurrentDate() {
        return DateFormat.
                getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(new Date());
    }

    private void startNewGame() {
        String gameDate = getCurrentDate();
        long gameId = createNewGame(gameDate);
        // launch a new_shadow game, pass in gameid to the activity
        launchGame(gameId, gameDate, "");
    }

    private long createNewGame(String gameDate) {
        // create new_shadow game in GamesTable with current date and time
        // return the gameID
        ContentValues cv = new ContentValues();
        cv.put(GamesTable.COL_DATE, gameDate);
        Uri newGameUri = getContentResolver().insert(
                Uri.parse(ScoreProvider.CONTENT_URI + "/" + ScoreProvider.PATH_GAMES),
                cv);
        long gameId = Long.parseLong(newGameUri.getLastPathSegment());
        return gameId;
    }

    private void launchGame(long gameId, String gameDate, String gameName) {
        Intent i = new Intent(MainActivity.this, GameActivity.class);
        i.putExtra(GameActivity.GAMEID, gameId);
        i.putExtra(GameActivity.GAMEDATE, gameDate);
        i.putExtra(GameActivity.GAMENAME, gameName);
        startActivity(i);
    }

    private void loadSavedGame() {
        DialogFragment dlg = new LoadGameDialogFragment();
        dlg.show(getFragmentManager(), "LoadGameDialogFragment");
    }

    @Override
    public void onDialogLoadGameClick(DialogFragment dlg) {
        // launch the game with the gameId and gameDate from the history_text
        dlg.dismiss();
        long gameId = ((LoadGameDialogFragment) dlg).getGameId();
        String gameDate = ((LoadGameDialogFragment) dlg).getGameDate();
        String gameName = ((LoadGameDialogFragment) dlg).getGameName();
        launchGame(gameId, gameDate, gameName);
    }

    @Override
    public void onDialogLoadGameDeleteGames() {
        Uri uri =
                Uri.parse(ScoreProvider.CONTENT_URI + "/"
                        + ScoreProvider.PATH_GAMES);
        getContentResolver().delete(uri, null, null);
    }
}
