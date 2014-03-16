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
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by binder on 3/15/14.
 */
public class About {

    private static final String ASSET_ABOUT = "about.txt";

    public static void displayAbout(Activity a) {
        final AlertDialog.Builder bld = new AlertDialog.Builder(a);
        bld.setCancelable(false);
        bld.setTitle(a.getString(R.string.action_about));
        LayoutInflater inf =
                (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inf.inflate(R.layout.dialog_about, null);
        WebView wv = (WebView) layout.findViewById(R.id.aboutText);
        wv.loadDataWithBaseURL(null, readAboutText(a), "text/html", "utf-8", null);
        bld.setView(layout);
        bld.setPositiveButton(a.getString(R.string.label_close),
                new DialogInterface.OnClickListener() {

                    public void onClick(
                            DialogInterface dialog,
                            int which) {
                        dialog.dismiss();
                    }
                }
        );
        bld.create().show();
    }

    private static String readAboutText(Context a) {
        BufferedReader in = null;
        StringBuilder buf;
        try {
            in =
                    new BufferedReader(new InputStreamReader(a.getAssets()
                            .open(ASSET_ABOUT)));
            buf = new StringBuilder(8192);
            String line;
            while ((line = in.readLine()) != null)
                buf.append(line).append('\n');
        } catch (IOException e) {
            buf = new StringBuilder("");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return buf.toString();
    }
}
