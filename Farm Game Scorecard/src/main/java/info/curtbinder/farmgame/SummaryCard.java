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
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by binder on 3/29/14.
 */
public class SummaryCard extends Card {

    protected TextView tvTitle;
    protected TextView tvSubTitle;
    protected TextView tvValue;

    public String title;
    public String subTitle;
    public String value;

    public SummaryCard(Context context) {
        this(context, R.layout.card_player);
    }

    public SummaryCard(Context context, int innerLayout) {
        super(context, innerLayout);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        tvTitle = (TextView) parent.findViewById(R.id.textTitle);
        tvSubTitle = (TextView) parent.findViewById(R.id.textSubTitle);
        tvValue = (TextView) parent.findViewById(R.id.textValue);

        if ( tvTitle != null ) {
            tvTitle.setText(title);
        }
        if ( tvSubTitle != null ) {
            tvSubTitle.setText(subTitle);
        }
        if ( tvValue != null ) {
            tvValue.setText(value);
        }
    }
}
