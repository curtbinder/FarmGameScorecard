/*
 * Copyright (c) 2014 by Curt Binder (http://curtbinder.info)
 *
 * This work is licensed under the Creative Commons
 * Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-sa/4.0/deed.en_US
 */

package info.curtbinder.farmgame;

import java.text.NumberFormat;
import java.util.Locale;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import info.curtbinder.farmgame.db.GamesTable;
import info.curtbinder.farmgame.db.PlayersTable;
import info.curtbinder.farmgame.db.ScoreProvider;

/**
 * Created by binder on 2/26/14
 */
public class GameActivity extends Activity implements ActionBar.TabListener,
        RenameDialogFragment.RenameDialogListener {

    public static final String GAMEID = "gameid";
    public static final String GAMEDATE= "gamedate";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    private Fragment[] frags;
    private static final int MAX_FRAGS = 7;

    int[] fieldValues;
    String[] fieldTitles;
    String[] fieldSubTitles;
    private long gameId;
    private String gameDate;

    public static String getCurrencyFormattedString(int value) {
        NumberFormat nft = NumberFormat.getCurrencyInstance(Locale.getDefault());
        nft.setMaximumFractionDigits(0);
        return nft.format(value);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        fieldValues = getResources().getIntArray(R.array.itemFieldValues);
        fieldTitles = getResources().getStringArray(R.array.itemTitles);
        fieldSubTitles = getResources().getStringArray(R.array.itemSubTitles);
        Bundle b = getIntent().getExtras();
        if ( b != null ) {
            gameId = b.getLong(GAMEID);
            gameDate = b.getString(GAMEDATE);
        }
        frags = new Fragment[MAX_FRAGS];

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        setActivityTitle("");
    }

    private void setActivityTitle(String title) {
        if ( title.isEmpty() ) {
            title = gameDate;
        }
        setTitle(title);
    }

    public long getGameId() {
        return gameId;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch ( id ) {
            case R.id.action_rename:
                promptToRenameGame();
                break;
            case R.id.action_delete:
                promptToDeleteGame();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void promptToRenameGame() {
        DialogFragment dlg = new RenameDialogFragment();
        dlg.show(getFragmentManager(), "RenameDialogFragment");
    }

    @Override
    public void onDialogRenameClick(DialogFragment dlg) {
        Uri uri = Uri.parse(ScoreProvider.CONTENT_URI + "/" +
                ScoreProvider.PATH_GAMES);
        String newName = ((RenameDialogFragment) dlg).getNewName();
        setActivityTitle(newName);
        ContentValues cv = new ContentValues();
        cv.put(GamesTable.COL_NAME, newName);
        Log.d("GameActivity", "DialogRenameClick: " + cv.toString());
        getContentResolver().update(uri, cv,
                GamesTable.COL_ID + "=?",
                new String[]{Long.toString(gameId)});
    }

    private void promptToDeleteGame() {
        // display alert prompting to delete game
        // if user chooses YES,
        //      finish activity
        //      delete all player entries for game id from players table
        //      delete the game from the games table
        // if user chooses NO,
        //      just return
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.message_prompt_delete);
        builder.setNegativeButton(R.string.label_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton(R.string.label_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                deleteGame();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    protected void deleteGame() {
        // delete the current game
        /*
        String sGameWhere = GamesTable.COL_ID + "=?";
        String[] sWhereCond = { Long.toString(gameId) };
        String sPlayersWhere = PlayersTable.COL_GAMEID + "=?";
        Uri uri_game =
                Uri.parse( ScoreProvider.CONTENT_URI + "/"
                        + ScoreProvider.PATH_GAMES );
        Uri uri_players =
                Uri.parse( ScoreProvider.CONTENT_URI + "/"
                        + ScoreProvider.PATH_PLAYERS );
        getContentResolver().delete( uri_game, sGameWhere, sWhereCond );
        getContentResolver().delete( uri_players, sPlayersWhere, sWhereCond );
        */
        // just delete the game, the provider handles deleting the data from both tables
        Uri uri =
                Uri.parse( ScoreProvider.CONTENT_URI + "/"
                        + ScoreProvider.PATH_GAMES + "/"
                        + Long.toString(gameId));
        getContentResolver().delete( uri, null, null );
        finish();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            if ( frags[position] == null ) {
                if (position == 0) {
                    frags[position] = SummaryFragment.newInstance();
                } else {
                    frags[position] = GameFragment.newInstance(position);
                }
            }
            return frags[position];
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // don't destroy the fragment since we are already saving them in an array
//            super.destroyItem(container, position, object);
        }

        @Override
        public int getCount() {
            // 1 summary page, 6 player pages
            return MAX_FRAGS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // todo update to get title from the fragment
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_summary);
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                    return String.format(l, getString(R.string.title_player_format), position);
            }
            return null;
        }
    }

}
