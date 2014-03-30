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
            // The first card is the winner, so we add in the header and thumbnail
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
