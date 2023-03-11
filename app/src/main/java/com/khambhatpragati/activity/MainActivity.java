package com.khambhatpragati.activity;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.khambhatpragati.R;
import com.khambhatpragati.application.Globals;
import com.khambhatpragati.helper.PreferencesManager;
import com.khambhatpragati.mvp.view.MVPView;
import com.khambhatpragati.utils.DialogUtil;
import com.khambhatpragati.view.ProgressDialogView;

import static com.khambhatpragati.constants.Keys.KEY_CATEGORY_RESPONSE;
import static com.khambhatpragati.constants.Keys.KEY_IS_LOGGED_IN;

public class MainActivity extends AppCompatActivity implements MVPView {

    private RelativeLayout relativeLayout;
    private EditText etCategory;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        addListenerOnWidgets();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Globals.getInstance().getSelectedCategoryName() != null) {
            etCategory.setText(Globals.getInstance().getSelectedCategoryName());
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    private void initView() {
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        etCategory = (EditText) findViewById(R.id.etCategory);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
    }

    private void addListenerOnWidgets() {
        etCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_up, R.anim.stay);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Globals.getInstance().getSelectedCategoryName() != null) {
                    Intent i = new Intent(MainActivity.this, CareTakerDetailsActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_up, R.anim.stay);
                } else {
                    DialogUtil.showSnackBar(relativeLayout, getString(R.string.msg_enter_category));
                }
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu); //your file name
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(final MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_sync:
//                CategoryPresenter presenter = new CategoryPresenterImpl(this);
//                presenter.getAllCategory(MainActivity.this);
//                return true;
//            case R.id.menu_logout:
//                showLogoutAlert();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    private void showLogoutAlert() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
        builder1.setMessage(R.string.msg_logout_alert);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PreferencesManager.put(getApplicationContext(), KEY_IS_LOGGED_IN, false);
                        Intent i = new Intent(MainActivity.this, MobileVerificationActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_down, R.anim.stay);
                        finish();
                        DialogUtil.showToastLong(getApplicationContext(), getString(R.string.msg_logout_success));
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public void onStartProgress() {
        ProgressDialogView.showProgressDialog(MainActivity.this);
    }

    @Override
    public void onSuccess(String response) {
        ProgressDialogView.hideProgressDialog();
        if (response != null) {
            PreferencesManager.put(MainActivity.this, KEY_CATEGORY_RESPONSE, response);
        } else {
            DialogUtil.showSnackBar(relativeLayout, getString(R.string.msg_sync_failed));
        }
    }

    @Override
    public void onError(String errorMessage) {
        ProgressDialogView.hideProgressDialog();
        DialogUtil.showSnackBarWithAction(relativeLayout, errorMessage);
    }

    @Override
    public void onInternetConnectionError(boolean isOnline) {
        DialogUtil.showSnackBarWithAction(relativeLayout, getString(R.string.msg_no_internet));
    }
}