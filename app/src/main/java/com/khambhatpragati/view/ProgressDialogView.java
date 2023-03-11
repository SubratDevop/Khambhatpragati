package com.khambhatpragati.view;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by COMPAQ on 26-04-2016.
 */
public class ProgressDialogView {

    private static final String TAG = ProgressDialogView.class.getSimpleName();

    private static ProgressDialog progressDialog;

    private static ProgressDialog getInstance() {
        if (null == progressDialog) {
            return progressDialog;
        }
        return progressDialog;
    }

    public static void showProgressDialog(Context context) {
        if (null == progressDialog) {
            getInstance();
        }
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void hideProgressDialog() {
        if (null != progressDialog && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}