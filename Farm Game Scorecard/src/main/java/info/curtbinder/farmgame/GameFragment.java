package info.curtbinder.farmgame;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by binder on 2/26/14.
 */
public class GameFragment extends Fragment {

    private int sectionNumber = 0;
    private static final int MAX_VALUES = 10;

    private EditText values;



    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static GameFragment newInstance(int sectionNumber) {
        GameFragment fragment = new GameFragment(sectionNumber);
        return fragment;
    }

    public GameFragment(int sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);
        // do something with the layout
        findViews();
        updateTextLabels();
        addListeners();
        return rootView;
    }

    private void findViews() {

    }

    private void updateTextLabels() {

    }

    private void addListeners() {

    }
}
