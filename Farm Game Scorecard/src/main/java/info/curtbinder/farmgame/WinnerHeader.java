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

import it.gmariotti.cardslib.library.internal.CardHeader;

/**
 * Created by binder on 3/29/14.
 */
public class WinnerHeader extends CardHeader {

    public WinnerHeader(Context context) {
        super(context, R.layout.winner_card_header_inner);
    }

//    @Override
//    public void setupInnerViewElements(ViewGroup parent, View view) {
//        if ( view != null ) {
//            TextView tv = (TextView) view.findViewById(R.id.textHeaderTitle);
//            if ( tv != null ) {
//                tv.setText("Winner Winner");
//            }
//        }
//    }
}
