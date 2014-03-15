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
import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

import info.curtbinder.farmgame.db.PlayersTable;
import info.curtbinder.farmgame.db.ScoreProvider;

/**
 * Created by binder on 2/26/14
 */
 public class GameFragment extends Fragment {

    private int playerId = 0;

    private int[] values = new int[Items.values().length];
    private String name;
    private TextView tvTotalAmount;
    private EditText editName;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static GameFragment newInstance(int sectionNumber) {
        return new GameFragment(sectionNumber);
    }

    public GameFragment(int playerId) {
        this.playerId = playerId;
    }

    private void loadData() {
        Uri content = Uri.parse(ScoreProvider.CONTENT_URI + "/" +
                                ScoreProvider.PATH_PLAYERS + "/" + playerId);
        Cursor c = getActivity().getContentResolver().query(content, null,
                PlayersTable.COL_GAMEID + "=?",
                new String[]{Long.toString(((GameActivity) getActivity()).getGameId())}, null);
        if ( c.moveToFirst() ) {
            for ( int i = 0; i < values.length; i++ ) {
                int v = c.getInt(c.getColumnIndex(PlayersTable.getColumnFromIndex(i)));
                values[i] = v;
            }
            name = c.getString(c.getColumnIndex(PlayersTable.COL_NAME));
        }
        c.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);
        // do something with the layout
        loadData();
        findAndUpdateViews(rootView);
        updateDisplayAmount();
        return rootView;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//    }

    private void findAndUpdateViews(View v) {
        tvTotalAmount = (TextView) v.findViewById(R.id.textTotal);
        updateLabelsAndAddListener(v, R.id.item_hay, Items.HAY);
        updateLabelsAndAddListener(v, R.id.item_grain, Items.GRAIN);
        updateLabelsAndAddListener(v, R.id.item_fruit, Items.FRUIT);
        updateLabelsAndAddListener(v, R.id.item_livestock, Items.LIVESTOCK);
        updateLabelsAndAddListener(v, R.id.item_tractor, Items.TRACTOR);
        updateLabelsAndAddListener(v, R.id.item_harvester, Items.HARVESTOR);
        updateLabelsAndAddListener(v, R.id.item_toppenish, Items.TOPPENISH);
        updateLabelsAndAddListener(v, R.id.item_cascade, Items.CASCADE);
        updateLabelsAndAddListener(v, R.id.item_rattlesnake, Items.RATTLESNAKE);
        updateLabelsAndAddListener(v, R.id.item_ahtanum, Items.AHTANUM);
        updateLabelsAndAddListener(v, R.id.item_p10k, Items.P10K);
        updateLabelsAndAddListener(v, R.id.item_p5k, Items.P5K);
        updateLabelsAndAddListener(v, R.id.item_p1k, Items.P1K);
        updateLabelsAndAddListener(v, R.id.item_10000, Items.B10000);
        updateLabelsAndAddListener(v, R.id.item_5000, Items.B5000);
        updateLabelsAndAddListener(v, R.id.item_1000, Items.B1000);
        updateLabelsAndAddListener(v, R.id.item_500, Items.B500);
        updateLabelsAndAddListener(v, R.id.item_100, Items.B100);
    }

    private void updateLabelsAndAddListener(View v, int fieldId, Items index) {
        editName = (EditText) v.findViewById(R.id.editPlayerName);
        if ( name != null ) {
            if ( ! name.isEmpty() ) {
                editName.setText(name);
            }
        }
        editName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean bHasFocus) {
                if ( !bHasFocus ) {
                    // edit box has lost focus, let's update the name
                    name = ((EditText)view).getText().toString();
                    updatePlayerNameInTable();
                }
            }
        });
        int pos = index.ordinal();
        RelativeLayout l = (RelativeLayout) v.findViewById(fieldId);
        EditText t = (EditText) l.findViewById(R.id.editValue);
        String s = "";
        if ( values[pos] > 0 ) {
            s = Integer.toString(values[pos]);
        }
        t.setText(s);
        t.addTextChangedListener(new TextChangeListener(pos));
        TextView tv = (TextView) l.findViewById(R.id.textTitle);
        tv.setText(((GameActivity) getActivity()).fieldTitles[pos]);
        tv = (TextView) l.findViewById(R.id.textSubTitle);
        tv.setText(((GameActivity) getActivity()).fieldSubTitles[pos]);
    }

    protected void updateDisplayAmount() {
//        Log.d("Game", "Player " + playerId + ") updateDisplayAmount");
        int total = 0;
        for ( int i = 0; i < values.length; i++ ) {
            total += values[i];
        }
        updateItemValueInTable(PlayersTable.COL_TOTAL, total);
        tvTotalAmount.setText(GameActivity.getCurrencyFormattedString(total));
    }

    protected void updatePlayerNameInTable() {
        //Log.d("Game", "updatePlayerNameInTable");
        ContentValues cv = new ContentValues();
        cv.put(PlayersTable.COL_NAME, name);
        updatePlayersTable(cv);
    }

    protected void updateItemValueInTable(String column, int value) {
        //Log.d("Game", "updateItemValueInTable");
        ContentValues cv = new ContentValues();
        cv.put(column, value);
        updatePlayersTable(cv);
    }

    protected void updatePlayersTable(ContentValues cv) {
//        Log.d("Game", "Player " + playerId + ") updatePlayersTable: " + cv.toString());
        Uri uri = Uri.parse(ScoreProvider.CONTENT_URI + "/" +
                            ScoreProvider.PATH_PLAYERS);
        getActivity().getContentResolver().update(uri, cv,
                PlayersTable.COL_GAMEID + "=? and " + PlayersTable.COL_PLAYERID + "=?",
                new String[]{Long.toString(((GameActivity) getActivity()).getGameId()),
                        Integer.toString(playerId)}
        );
    }

    private class TextChangeListener implements TextWatcher {
        private int index;
        private int fieldValue;

        public TextChangeListener(int index) {
            this.index = index;
            this.fieldValue = ((GameActivity) getActivity()).fieldValues[index];
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String s = editable.toString();
            int total = 0;
            if ( ! s.isEmpty() ) {
                int qty = Integer.parseInt(s);
                total = fieldValue * qty;
            }

            // store the value computed from the quantity
            values[index] = total;
            updateItemValueInTable(PlayersTable.getColumnFromIndex(index), total);
            // update the value in the database
            updateDisplayAmount();
        }
    }
}
