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
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import info.curtbinder.farmgame.db.GamesTable;
import info.curtbinder.farmgame.db.ScoreProvider;

/**
 * Created by binder on 3/15/14.
 */
public class LoadGameDialogFragment extends DialogFragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private ListView lv;
    private ImageButton btnDelete;
    private TextView tvTitle;
    private long gameId;
    private String gameDate;
    private String gameName;

    private static final String[] FROM = { GamesTable.COL_ID,
                                           GamesTable.COL_DATE,
                                           GamesTable.COL_NAME };
    private static final int[] TO = {   0,
                                        R.id.textHistoryDate,
                                        R.id.textHistoryName };

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Loader<Cursor> loader = null;
        Uri content = Uri.parse(ScoreProvider.CONTENT_URI + "/" + ScoreProvider.PATH_GAMES);
        if ( i == 0 ) {
            loader = new CursorLoader(getActivity(), content, FROM, null, null,
                    GamesTable.COL_ID + " DESC");
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        ListAdapter adapter = lv.getAdapter();
        if ( adapter == null || !(adapter instanceof CursorAdapter) ){
            adapter = new SimpleCursorAdapter(getActivity(),
                    R.layout.item_history, cursor, FROM, TO, 0);
            lv.setAdapter(adapter);
        } else {
            ((CursorAdapter) adapter).swapCursor(cursor);
        }

        // Update the title and delete button based on the number of items
        if ( lv.getCount() == 0 ) {
            btnDelete.setVisibility(View.GONE);
            tvTitle.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

    public interface LoadGameDialogListener {
        public void onDialogLoadGameClick(DialogFragment dlg);
        public void onDialogLoadGameDeleteGames();
    }

    LoadGameDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // verify the host activity implements the callbacks
        try {
            mListener = (LoadGameDialogListener) activity;
        } catch ( ClassCastException e ) {
            throw new ClassCastException(activity.toString() + " must implement" +
                    " LoadGameDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_dialog_history, null);
        lv = (ListView) v.findViewById(R.id.listGames);
        setupListView(v);
        getLoaderManager().initLoader(0, null, this);
        btnDelete = (ImageButton) v.findViewById(R.id.btnDelete);
        setupImageButton();
        tvTitle = (TextView) v.findViewById(R.id.textLoadTitle);
        builder.setView(v);
        return builder.create();
    }

    private void setupListView(View rootView) {
        lv.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                gameId = id;
                gameDate = ((TextView) view.findViewById(R.id.textHistoryDate)).getText().toString();
                gameName = ((TextView) view.findViewById(R.id.textHistoryName)).getText().toString();
                // call the listener function
                mListener.onDialogLoadGameClick(LoadGameDialogFragment.this);
            }
        });
        lv.setEmptyView(rootView.findViewById(R.id.textEmptyList));
    }

    private void setupImageButton() {
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // prompt to delete the history
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.message_prompt_delete_all);
                builder.setNegativeButton(R.string.label_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton(R.string.label_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getDialog().dismiss();
                        dialogInterface.dismiss();
                        mListener.onDialogLoadGameDeleteGames();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public long getGameId() { return gameId; }
    public String getGameDate() { return gameDate; }
    public String getGameName() { return gameName; }
}
