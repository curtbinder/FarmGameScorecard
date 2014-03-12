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
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewGroup;

/**
 * Created by binder on 2/26/14
 */
public class GameActivity extends Activity implements ActionBar.TabListener {

    public static final String GAMEID = "gameid";

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
//        mViewPager.setOffscreenPageLimit(MAX_FRAGS);

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
                Log.d("GameActivity", "create fragment, position:" + position);
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
