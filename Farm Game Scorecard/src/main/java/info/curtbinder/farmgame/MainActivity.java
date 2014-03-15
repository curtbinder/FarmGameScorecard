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
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import info.curtbinder.farmgame.db.GamesTable;
import info.curtbinder.farmgame.db.ScoreProvider;

/**
 * Created by binder on 2/26/14
 */
public class MainActivity extends Activity implements Button.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        switch ( id ) {
            case R.id.buttonNewGame:
                startNewGame();
            default:
                return;
        }
    }

    private void startNewGame() {
        long gameId = createNewGame();
        addPlayersToGame(gameId);
        // launch a new game, pass in gameid to the activity
        launchNewGame(gameId);
    }

    private long createNewGame() {
        // create new game in GamesTable with current date and time
        // return the gameID
        ContentValues cv = new ContentValues();
        cv.put(GamesTable.COL_DATE, DateFormat.getDateInstance().format(new Date()));
        Uri newGameUri = getContentResolver().insert(
                Uri.parse(ScoreProvider.CONTENT_URI + "/" + ScoreProvider.PATH_GAMES),
                cv);
        long gameId = Long.parseLong(newGameUri.getLastPathSegment());
        Log.d("MainActivity", "New Game ID: " + gameId);
        return gameId;
    }

    private void addPlayersToGame(long gameId) {
        // insert 6 players into PlayersTable with default values
    }

    private void launchNewGame(long gameId) {
        Intent i = new Intent(MainActivity.this, GameActivity.class);
        i.putExtra(GameActivity.GAMEID, gameId);
        startActivity(i);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

}
