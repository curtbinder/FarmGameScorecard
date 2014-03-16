/*
 * Copyright (c) 2014 by Curt Binder (http://curtbinder.info)
 *
 * This work is licensed under the Creative Commons
 * Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-sa/4.0/deed.en_US
 */

package info.curtbinder.farmgame;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import info.curtbinder.farmgame.db.PlayersTable;

/**
 * Created by binder on 3/11/14.
 */
public class SummaryListCursorAdapter extends CursorAdapter {

    private final int LAYOUT = R.layout.item_player;
    private String playerFormat;

    static class ViewHolder {
        TextView tvTitle;
        TextView tvSubTitle;
        TextView tvValue;
    }

    public SummaryListCursorAdapter ( Context context, Cursor c, int flags ) {
        super( context, c, flags );
        playerFormat = context.getResources().getString(R.string.title_player_format);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(LAYOUT, viewGroup, false);
        ViewHolder vh = new ViewHolder();
        vh.tvTitle = (TextView) v.findViewById(R.id.textTitle);
        vh.tvSubTitle = (TextView) v.findViewById(R.id.textSubTitle);
        vh.tvValue = (TextView) v.findViewById(R.id.textValue);
        setViews(vh, cursor);
        v.setTag(vh);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ViewHolder vh = (ViewHolder) view.getTag();
        setViews(vh, cursor);
    }

    private void setViews(ViewHolder v, Cursor c) {
        int total = c.getInt(c.getColumnIndex(PlayersTable.COL_TOTAL));
        int playerId = c.getInt(c.getColumnIndex(PlayersTable.COL_PLAYERID));
        String name = c.getString(c.getColumnIndex(PlayersTable.COL_NAME));
        String defaultName = String.format(playerFormat, playerId);
        if ( name == null ) {
            name = defaultName;
        }
        v.tvTitle.setText(name);
        v.tvSubTitle.setText(defaultName);
        v.tvValue.setText(GameActivity.getCurrencyFormattedString(total));
    }
}
