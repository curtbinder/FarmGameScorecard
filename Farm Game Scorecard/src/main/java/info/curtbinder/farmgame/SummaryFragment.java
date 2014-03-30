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

package info.curtbinder.farmgame;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;

import info.curtbinder.farmgame.db.PlayersTable;
import info.curtbinder.farmgame.db.ScoreProvider;
import it.gmariotti.cardslib.library.internal.CardHeader;

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
            adapter = new SummaryListCardCursorAdapter(getActivity(), cursor, 0);
            setListAdapter(adapter);
        } else {
            ((CursorAdapter) adapter).swapCursor(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
