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

import it.gmariotti.cardslib.library.internal.CardHeader;

/**
 * Created by binder on 3/29/14.
 */
public class WinnerHeader extends CardHeader {

    public WinnerHeader(Context context) {
        super(context, R.layout.winner_card_header_inner);
    }

    // Since the header will not change, there's no reason to perform extra function calls
    // The text is hard coded in the layout with a string resource.
    // The text could easily be updated using this function call and then removing the value
    // from the layout.
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
