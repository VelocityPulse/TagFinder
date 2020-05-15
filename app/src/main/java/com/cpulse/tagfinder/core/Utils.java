package com.cpulse.tagfinder.core;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.cpulse.tagfinder.R;

public class Utils {

    public static void hideKeyboard(Activity iActivity) {
        InputMethodManager lImm = (InputMethodManager) iActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View lView = iActivity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (lView == null)
            lView = new View(iActivity);
        lImm.hideSoftInputFromWindow(lView.getWindowToken(), 0);
    }

    public static void hideKeyboard(Activity iActivity, View iView) {
        InputMethodManager lImm = (InputMethodManager) iActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        if (iView == null)
            iView = new View(iActivity);
        lImm.hideSoftInputFromWindow(iView.getWindowToken(), 0);
    }

    public static void showLongToast(final Activity iActivity, final String iToast) {
        iActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(iActivity, iToast, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void showToast(final Activity iActivity, final String iToast) {
        iActivity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(iActivity, iToast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static boolean isNullOrEmpty(String iStr) {
        return iStr == null || iStr.trim().isEmpty();
    }

    public static AlertDialog createProgressDialog(Context iContext, final AlertDialog.OnClickListener iOnClickCancelListener) {
        final AlertDialog.Builder lProgressDialog;
        lProgressDialog = new AlertDialog.Builder(iContext);
        lProgressDialog.setView(R.layout.loading_view);
        lProgressDialog.setCancelable(false);
        lProgressDialog.setNegativeButton("Cancel", iOnClickCancelListener);

        return lProgressDialog.create();
    }

    public static AlertDialog createProgressDialogNoButton(Context iContext) {
        final AlertDialog.Builder lProgressDialog;
        lProgressDialog = new AlertDialog.Builder(iContext);
        lProgressDialog.setView(R.layout.loading_view);
        lProgressDialog.setCancelable(false);

        return lProgressDialog.create();
    }

    public static AlertDialog createProgressDialog(Context iContext,
                                                   String iTitle,
                                                   final AlertDialog.OnClickListener iOnClickCancelListener) {
        final AlertDialog.Builder lProgressDialog;
        lProgressDialog = new AlertDialog.Builder(iContext);
        lProgressDialog.setView(R.layout.loading_view);
        lProgressDialog.setTitle(iTitle);
        lProgressDialog.setCancelable(false);
        lProgressDialog.setNegativeButton("Cancel", iOnClickCancelListener);

        return lProgressDialog.create();
    }

    public static AlertDialog createProgressDialog(Context iContext,
                                                   String iTitle,
                                                   String iButtonText,
                                                   final AlertDialog.OnClickListener iOnClickCancelListener) {
        final AlertDialog.Builder lProgressDialog;
        lProgressDialog = new AlertDialog.Builder(iContext);
        lProgressDialog.setView(R.layout.loading_view);
        lProgressDialog.setTitle(iTitle);
        lProgressDialog.setCancelable(false);
        lProgressDialog.setNegativeButton(iButtonText, iOnClickCancelListener);

        return lProgressDialog.create();
    }
}