package info.curtbinder.farmgame;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by binder on 2/26/14.
 */
public class GameFragment extends Fragment {

    private int sectionNumber = 0;

    private int[] values = new int[Items.values().length];
    private TextView tvTotalAmount;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static GameFragment newInstance(int sectionNumber) {
        return new GameFragment(sectionNumber);
    }

    public GameFragment(int sectionNumber) {
        this.sectionNumber = sectionNumber;
        for ( int i = 0; i < values.length; i++ ) {
            values[i] = 0;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);
        // do something with the layout
        findAndUpdateViews(rootView);
        updateDisplayAmount();
        return rootView;
    }

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
        int pos = index.ordinal();
        RelativeLayout l = (RelativeLayout) v.findViewById(fieldId);
        EditText t = (EditText) l.findViewById(R.id.editValue);
        t.setText("");
        t.addTextChangedListener(new TextChangeListener(pos));
        TextView tv = (TextView) l.findViewById(R.id.textTitle);
        tv.setText(((GameActivity)getActivity()).fieldTitles[pos]);
        tv = (TextView) l.findViewById(R.id.textSubTitle);
        tv.setText(((GameActivity)getActivity()).fieldSubTitles[pos]);
    }

    protected void updateDisplayAmount() {
        int total = 0;
        for ( int i : values ) {
            total += values[i];
        }
        NumberFormat nft = NumberFormat.getCurrencyInstance(Locale.getDefault());
        nft.setMaximumFractionDigits(0);
        String s = nft.format(total);
        tvTotalAmount.setText(s);
    }

    private class TextChangeListener implements TextWatcher {
        private int index;
        private int fieldValue;

        public TextChangeListener(int index) {
            this.index = index;
            this.fieldValue = ((GameActivity)getActivity()).fieldValues[index];
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
            updateDisplayAmount();
        }
    }
}
