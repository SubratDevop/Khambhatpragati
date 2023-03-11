package com.khambhatpragati.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.material.tabs.TabLayout;
import com.khambhatpragati.R;
import com.khambhatpragati.constants.URLs;
import com.khambhatpragati.fragments.VastiFragments;
import com.khambhatpragati.fragments.VastiProfileFragment;
import com.khambhatpragati.helper.CustomVolleyRequest;
import com.khambhatpragati.model.DependentModel;
import com.khambhatpragati.model.SpinnerModel;
import com.khambhatpragati.model.VastiModel;
import com.khambhatpragati.model.response.AdvertismentResponse;
import com.khambhatpragati.parser.JSONParser;
import com.khambhatpragati.preference.UserPreference;
import com.khambhatpragati.utils.DialogUtil;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.khambhatpragati.activity.DashboardActivity.adCounter;
import static com.khambhatpragati.activity.DashboardActivity.adsImageUrls;
import static com.khambhatpragati.activity.DashboardActivity.isAdsTicking;
import static com.khambhatpragati.activity.DashboardActivity.isShownOnce;
import static com.khambhatpragati.constants.Keys.KEY_GROUPS_ID;
import static com.khambhatpragati.constants.URLs.ADDDEPENDENT_URL;
import static com.khambhatpragati.constants.URLs.ADDMEMBERIMAGE_URL;
import static com.khambhatpragati.constants.URLs.DELETEDEPENDENT_URL;
import static com.khambhatpragati.constants.URLs.UPDATEDEPENDENT_URL;

public class VastiDetailActivity extends AppCompatActivity {

    Context context;
    RelativeLayout relativeLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    VastiModel vasti;
    private ImageLoader imageLoader;
    ImageView imageView;
    private String strAdWeblink="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vastidetail);
        context = VastiDetailActivity.this;
        initToolBar();
        Intent i = getIntent();
        initView();
    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Member Details");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }

    private void initView() {

        relativeLayout = findViewById(R.id.relativeLayout);
        tabLayout = (TabLayout) findViewById(R.id.tabs_firstAppeal);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        Intent i = getIntent();
        vasti = (VastiModel)i.getSerializableExtra("vasti_detail");
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.addTab(tabLayout.newTab().setText("PROFILE"));
        if(vasti!=null) {

            if(!vasti.getParent_member_id().equalsIgnoreCase("null")
                && !vasti.getParent_member_id().equalsIgnoreCase("")
                && !vasti.getParent_member_id().equalsIgnoreCase("0"))
                tabLayout.addTab(tabLayout.newTab().setText("PARENT"));
            if(!vasti.getDependent_count().equalsIgnoreCase("0")
                && !vasti.getDependent_count().equalsIgnoreCase("null")
                && !vasti.getDependent_count().equalsIgnoreCase(""))
                tabLayout.addTab(tabLayout.newTab().setText("FAMILY"));
            if(!vasti.getMarried_son_count().equalsIgnoreCase("0")
                && !vasti.getMarried_son_count().equalsIgnoreCase("null")
                && !vasti.getMarried_son_count().equalsIgnoreCase(""))
                tabLayout.addTab(tabLayout.newTab().setText("SONS"));
            if(!vasti.getBrother_count().equalsIgnoreCase("0")
                && !vasti.getBrother_count().equalsIgnoreCase("")
                && !vasti.getBrother_count().equalsIgnoreCase("null"))
                tabLayout.addTab(tabLayout.newTab().setText("BROTHER"));
        }
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (adsImageUrls.size() > 0) {
                    if (adCounter >= adsImageUrls.size())
                        adCounter = 0;
                    Intent i = new Intent(VastiDetailActivity.this, FullScreenAdActivity.class);
                    i.putExtra("adURL", adsImageUrls.get(adCounter).getFull_path());
                    startActivity(i);
                }
            }
        }, 1500);*/

        if(!isAdsTicking ) {
            if(!isShownOnce) {
                if (adsImageUrls.size() > 0) {
                    if (adCounter >= adsImageUrls.size())
                        adCounter = 0;
                    Intent intent = new Intent(VastiDetailActivity.this, FullScreenAdActivity.class);
                    intent.putExtra("adURL", adsImageUrls.get(adCounter).getFull_path());
                    i.putExtra("adWebLink", adsImageUrls.get(adCounter).getWebsite_url());
                    intent.putExtra("adTIMEOUT", adsImageUrls.get(adCounter).getDisplay_time());
                    startActivity(intent);
                }
                isShownOnce=true;
                isAdsTicking=true;
            }
        }
        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!strAdWeblink.equalsIgnoreCase("") && !strAdWeblink.equalsIgnoreCase("null")) {
                    Intent intent = new Intent(VastiDetailActivity.this, WebviewActivity.class);
                    intent.putExtra("strValue", strAdWeblink);
                    startActivity(intent);
                }
            }
        });
        getBottomAds();
    }
    private void getBottomAds() {
        final ProgressDialog progress = new ProgressDialog(VastiDetailActivity.this);
        progress.setMessage("Please Wait....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

        AQuery aQuery = new AQuery(getApplicationContext());
        Map<String, Object> params = new HashMap<>();
        params.put("advertisement_type","BottomAd");
        params.put("screen_type","VastiPatrakDetail Screen");

        Log.d("Params", "Testing" + params);

        aQuery.ajax(URLs.GET_ADS_URL, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                Log.d("Checking", "url: " + url + " , json: " + json);
                try {
                    if (json.toString().contains("data")) {
                        AdvertismentResponse adsResponse = JSONParser.parseAdsResponse(json.toString());
                        if (adsResponse.isStatus().equalsIgnoreCase("200")) {
                            Date todayDate = Calendar.getInstance().getTime();
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            String todayString = formatter.format(todayDate);
                            for(int i=0;i<adsResponse.getData().size();i++){
                                if(formatter.parse(adsResponse.getData().get(i).getEnd_date()).after(formatter.parse(todayString))
                                        || formatter.parse(adsResponse.getData().get(i).getEnd_date()).equals(formatter.parse(todayString))) {
                                    if(adsResponse.getData().get(i).getAdvertisement_type().equalsIgnoreCase("BottomAd")
                                            && adsResponse.getData().get(i).getScreen_type().equalsIgnoreCase("VastiPatrakDetail Screen")) {
                                        imageView.setVisibility(View.VISIBLE);
                                        strAdWeblink=adsResponse.getData().get(i).getWebsite_url();
                                        imageLoader = CustomVolleyRequest.getInstance(VastiDetailActivity.this).getImageLoader();
                                        imageLoader.get(adsResponse.getData().get(i).getFull_path(), ImageLoader.getImageListener(imageView, R.mipmap.noimage, android.R.drawable.ic_dialog_alert));
                                    }
                                }
                            }

                        } else {
                            imageView.setVisibility(View.GONE);
                            //ProgressDialogView.hideProgressDialog();
                            DialogUtil.showSnackBarWithAction(relativeLayout, adsResponse.getMessage());
                        }
                    }
                    progress.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    progress.dismiss();
                }
            }
        }.method(AQuery.METHOD_POST));

    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {

        int mNumOfTabs;
        Fragment fragment = null;

        public ViewPagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            if(tabLayout.getTabAt(position).getText().toString().equalsIgnoreCase("PROFILE")
                || tabLayout.getTabAt(position).getText().toString().equalsIgnoreCase("PARENT"))
                fragment = new VastiProfileFragment(vasti,tabLayout.getTabAt(position).getText().toString());
            else
                fragment = new VastiFragments(vasti,tabLayout.getTabAt(position).getText().toString());
            return fragment;

        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(position);
        }
    }

}