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

import info.curtbinder.farmgame.db.PlayersTable;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardCursorAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;

/**
 * Created by binder on 3/11/14.
 */
public class SummaryListCardCursorAdapter extends CardCursorAdapter {

    private String playerFormat;

    public SummaryListCardCursorAdapter(Context context, Cursor c, int flags) {
        super( context, c, flags );
        playerFormat = context.getResources().getString(R.string.title_player_format);

    }

    @Override
    protected Card getCardFromCursor(Cursor cursor) {
        SummaryCard card = new SummaryCard(super.getContext());
        if ( cursor.getPosition() == 0 ) {
            WinnerHeader h = new WinnerHeader(super.getContext());
            card.addCardHeader(h);
            CardThumbnail t = new CardThumbnail(super.getContext());
            t.setDrawableResource(R.drawable.trophy);
            card.addCardThumbnail(t);
        }
        setCardFromCursor(card, cursor);
        return card;
    }

    private void setCardFromCursor(SummaryCard card, Cursor c) {
        int total = c.getInt(c.getColumnIndex(PlayersTable.COL_TOTAL));
        int playerId = c.getInt(c.getColumnIndex(PlayersTable.COL_PLAYERID));
        String name = c.getString(c.getColumnIndex(PlayersTable.COL_NAME));
        String defaultName = String.format(playerFormat, playerId);
        if ( name == null ) {
            name = defaultName;
        }
        card.title = name;
        card.subTitle = defaultName;
        card.value = GameActivity.getCurrencyFormattedString(total);
    }
}
