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
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by binder on 3/15/14.
 */
public class RenameDialogFragment extends DialogFragment {

    private EditText editNewName;

    // if an activity is going to call/load this fragment, it must implement
    // these functions to handle callbacks
    public interface RenameDialogListener {
        public void onDialogRenameClick(DialogFragment dlg);
    }

    RenameDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // verify the host activity implements the callbacks
        try {
            mListener = (RenameDialogListener) activity;
        } catch ( ClassCastException e ) {
            throw new ClassCastException(activity.toString() + " must implement" +
                    " RenameDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_dialog_rename, null);
        editNewName = (EditText) v.findViewById(R.id.editNewName);
        builder.setView(v);
        builder.setPositiveButton(R.string.action_rename, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mListener.onDialogRenameClick(RenameDialogFragment.this);
            }
        });
        builder.setNegativeButton(R.string.label_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //dialog.cancel();
                RenameDialogFragment.this.getDialog().cancel();
            }
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public String getNewName() {
        return editNewName.getText().toString();
    }
}
