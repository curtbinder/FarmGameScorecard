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

    // Public values to be set with each card
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
        // This function is called by the library to update and set the values from the
        // strings to the text of the TextViews
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
