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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import info.curtbinder.farmgame.db.PlayersTable;
import info.curtbinder.farmgame.db.ScoreProvider;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.view.CardView;

/**
 * Created by binder on 2/26/14
 */
 public class SummaryFragment extends ListFragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

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
        setHasOptionsMenu(true);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.summary, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_screenshot:
                new TakeScreenshotTask().execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String takeScreenshot() {
        Log.d("SummaryFragment", "Take Screenshot");
        View v = getActivity().findViewById(R.id.pager);
        String msg = "Failed to save";
        if (v != null) {
            Log.d("SummaryFragment", "Found layout");
            Bitmap bm = getBitmap(v);
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            String name = "MyName";
            File img = new File(path+"/"+name+"_screenshot.png");
            Log.d("SummaryFragment", "File: " + img.toString());
            FileOutputStream fout = null;
            try {
                fout = new FileOutputStream(img);
                bm.compress(Bitmap.CompressFormat.PNG, 100, fout);
                fout.flush();
                fout.close();
                msg = "Saved";
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("SummaryFragment", "Null layout");
        }
        return msg;
    }

    private Bitmap getBitmap(View v) {
        return getBitmapDrawingCache(v);
//        return getBitmapCache2(v);
    }

    private Bitmap getBitmapCache2(View v) {
        CardListView l = (CardListView) v.findViewById(android.R.id.list);
        ListAdapter a = l.getAdapter();
        int total_cards = a.getCount();
        int total_width = l.getChildAt(0).getWidth(); // might have to add padding
        int card_one_height = l.getChildAt(0).getHeight();
        int total_height = card_one_height;
        int card_height = l.getChildAt(1).getHeight();
        // all cards are the same height except the first card, take the height
        // times the number of cards minus 1.
        total_height += (card_height * (total_cards-1));
        Log.d("Canvas", "HxW: " + total_height + "x" + total_width);
        Bitmap bm = Bitmap.createBitmap(total_width, total_height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        c.drawColor(Color.WHITE);
        //View tmpView = null;
        SummaryCard sc;
        int top = 0;
        Paint p = new Paint();
        int h;
        for ( int j = 0; j < a.getCount(); j++ ) {
//            Log.d("BMP", "Adapter Count: " + j + " of " + a.getCount());
            sc = (SummaryCard)a.getItem(j);
            View cv = a.getView(j, null, null);
//            CardView cv = sc.getCardView();
            if ( cv == null ) {
                Log.d("Canvas", "Null Card: " + j);
                continue;
            }
            if ( j == 0 ) {
                h = card_one_height;
            } else {
                h = card_height;
            }
            Bitmap b = Bitmap.createBitmap(total_width, h, Bitmap.Config.ARGB_8888);
            Canvas c1 = new Canvas(b);
            cv.layout(0, 0, total_width, h);
            cv.draw(c1);
//            cv.setDrawingCacheEnabled(true);
//            Bitmap b = Bitmap.createBitmap(cv.getDrawingCache());
//            cv.setDrawingCacheEnabled(false);
            c.drawBitmap(b, 0, top, p);
            if ( j == 0 ) {
                top += card_height;
            } else {
                top += card_height;
            }
        }
//        v.draw(c);
        return bm;
    }

    private Bitmap getBitmapDrawingCache(View v) {
        v.setDrawingCacheEnabled(true);
        Bitmap bm = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return bm;
    }

    private class TakeScreenshotTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String result = takeScreenshot();

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
        }
    }
}
