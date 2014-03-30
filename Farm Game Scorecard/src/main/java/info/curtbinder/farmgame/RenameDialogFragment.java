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
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
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
