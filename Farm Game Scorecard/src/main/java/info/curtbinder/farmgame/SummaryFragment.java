/*
 * Copyright (c) 2014 by Curt Binder (http://curtbinder.info)
 *
 * This work is licensed under the Creative Commons
 * Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-sa/4.0/deed.en_US
 */

package info.curtbinder.farmgame;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;

import info.curtbinder.farmgame.db.PlayersTable;
import info.curtbinder.farmgame.db.ScoreProvider;

/**
 * Created by binder on 2/26/14
 */
 public class SummaryFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String[] FROM = { PlayersTable.COL_ID,
                                            PlayersTable.COL_NAME,
                                            PlayersTable.COL_PLAYERID,
                                            PlayersTable.COL_TOTAL };

    public static SummaryFragment newInstance() {
        SummaryFragment fragment = new SummaryFragment();
        return fragment;
    }

    public SummaryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_summary, container, false);
        // do something with the layout
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {

        Loader<Cursor> loader = null;
        Uri content = Uri.parse(ScoreProvider.CONTENT_URI + "/" + ScoreProvider.PATH_PLAYERS);
        if ( i == 0 ) {
            loader = new CursorLoader(getActivity(), content, FROM,
                    PlayersTable.COL_GAMEID + "=? and " + PlayersTable.COL_TOTAL + ">?",
                    new String[] {Long.toString(((GameActivity)getActivity()).getGameId()),
                    "0"},
                    PlayersTable.COL_TOTAL + " DESC");
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        ListAdapter adapter = getListAdapter();
        if ( adapter == null || !(adapter instanceof CursorAdapter ) ) {
            adapter = new SummaryListCursorAdapter(getActivity(), cursor, 0);
            setListAdapter(adapter);
        } else {
            ((CursorAdapter) adapter).swapCursor(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
