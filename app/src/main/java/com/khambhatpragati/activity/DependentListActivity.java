package com.khambhatpragati.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.khambhatpragati.R;
import com.khambhatpragati.adapter.BrotherAdapter;
import com.khambhatpragati.adapter.DependentListAdapter;
import com.khambhatpragati.adapter.UserDirectoryAdapter;
import com.khambhatpragati.application.Globals;
import com.khambhatpragati.constants.URLs;
import com.khambhatpragati.db.DBHelper;
import com.khambhatpragati.helper.CustomVolleyRequest;
import com.khambhatpragati.listeners.ItemClickListener;
import com.khambhatpragati.model.BrotherModel;
import com.khambhatpragati.model.DependentModel;
import com.khambhatpragati.model.response.AdvertismentResponse;
import com.khambhatpragati.model.response.UserDirectoryBean;
import com.khambhatpragati.parser.JSONParser;
import com.khambhatpragati.preference.UserPreference;
import com.khambhatpragati.utils.AppUtil;
import com.khambhatpragati.utils.DialogUtil;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.khambhatpragati.activity.DashboardActivity.adCounter;
import static com.khambhatpragati.activity.DashboardActivity.adsImageUrls;
import static com.khambhatpragati.activity.DashboardActivity.isAdsTicking;
import static com.khambhatpragati.activity.DashboardActivity.isShownOnce;
import static com.khambhatpragati.constants.Keys.KEY_ACCESS_LEVEL;
import static com.khambhatpragati.constants.Keys.KEY_GROUPS_ID;
import static com.khambhatpragati.constants.URLs.GET_DEPENDENTLIST_URL;
import static com.khambhatpragati.constants.URLs.GET_MEMBERPROFILE_URL;

public class DependentListActivity extends AppCompatActivity implements ItemClickListener {

    private static final String TAG = DependentListActivity.class.getSimpleName();
    private RelativeLayout relativeLayout;
    private Toolbar toolbar;
    ImageView imgAddDependent;
    private RecyclerView recyclerView;
    private DependentListAdapter mAdapter;
    private ArrayList<DependentModel> dependentList = new ArrayList<DependentModel>();

    private ImageLoader imageLoader;
    ImageView imageView;
    private String strAdWeblink="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dependentlist);

        initToolBar();
        initView();
    }

    @Override
    protected void onResume() {
        getDependentList();
        super.onResume();
    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My Dependents");
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }
    private void initializeAdapter(){
        mAdapter = new DependentListAdapter(dependentList, DependentListActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }
    private void initView() {
        imgAddDependent =  findViewById(R.id.addDependent);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        initializeAdapter();

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (adsImageUrls.size() > 0) {
                    if (adCounter >= adsImageUrls.size())
                        adCounter = 0;
                    Intent i = new Intent(DependentListActivity.this, FullScreenAdActivity.class);
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
                    Intent i = new Intent(DependentListActivity.this, FullScreenAdActivity.class);
                    i.putExtra("adURL", adsImageUrls.get(adCounter).getFull_path());
                    i.putExtra("adWebLink", adsImageUrls.get(adCounter).getWebsite_url());
                    i.putExtra("adTIMEOUT", adsImageUrls.get(adCounter).getDisplay_time());
                    startActivity(i);
                }
                isShownOnce=true;
                isAdsTicking=true;
            }
        }

        imgAddDependent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DependentListActivity.this, DependentDetailActivity.class);
                i.putExtra("dependent_detail","");
                i.putExtra("From","Add");
                startActivity(i);
                overridePendingTransition(R.anim.slide_up, R.anim.stay);
            }
        });
        getDependentList();
        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!strAdWeblink.equalsIgnoreCase("") && !strAdWeblink.equalsIgnoreCase("null")) {
                    Intent intent = new Intent(DependentListActivity.this, WebviewActivity.class);
                    intent.putExtra("strValue", strAdWeblink);
                    startActivity(intent);
                }
            }
        });
        getBottomAds();
    }
    private void getBottomAds() {
        final ProgressDialog progress = new ProgressDialog(DependentListActivity.this);
        progress.setMessage("Please Wait....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

        AQuery aQuery = new AQuery(getApplicationContext());
        Map<String, Object> params = new HashMap<>();
        params.put("advertisement_type","BottomAd");
        params.put("screen_type","UserProfile Screen");

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
                                            && adsResponse.getData().get(i).getScreen_type().equalsIgnoreCase("UserProfile Screen")) {
                                        imageView.setVisibility(View.VISIBLE);
                                        strAdWeblink=adsResponse.getData().get(i).getWebsite_url();
                                        imageLoader = CustomVolleyRequest.getInstance(DependentListActivity.this).getImageLoader();
                                        if(!adsResponse.getData().get(i).getFull_path().equalsIgnoreCase("NULL")
                                                && adsResponse.getData().get(i).getFull_path().startsWith("http"))
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

    private void getDependentList() {
        final ProgressDialog progress = new ProgressDialog(DependentListActivity.this);
        progress.setMessage("Please Wait....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

        Map<String, Object> params = new HashMap<>();
        params.put("mobile_no", UserPreference.getInstance(DependentListActivity.this).getPreference().getMobileNo());

        Log.d("Params", "Testing" + params);

        RequestQueue queue = Volley.newRequestQueue(DependentListActivity.this);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, GET_DEPENDENTLIST_URL,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject json) {
                        try {
                            Log.d("TAG", "JSON POST Response = " + json.toString());
                            if (json.toString().contains("data")) {
                                dependentList.clear();
                                for(int i=0;i<json.getJSONArray("data").length();i++){
                                    DependentModel d = new DependentModel();
                                    d.setRecord_no(json.getJSONArray("data").getJSONObject(i).getString("record_no"));
                                    d.setParent_member_id(json.getJSONArray("data").getJSONObject(i).getString("parent_member_id"));
                                    d.setFirst_name(json.getJSONArray("data").getJSONObject(i).getString("first_name"));
                                    d.setRelation_with_member(json.getJSONArray("data").getJSONObject(i).getString("relation_with_member"));
                                    d.setGender(json.getJSONArray("data").getJSONObject(i).getString("gender"));
                                    d.setDate_of_birth(json.getJSONArray("data").getJSONObject(i).getString("date_of_birth"));
                                    d.setMarital_status(json.getJSONArray("data").getJSONObject(i).getString("marital_status"));
                                    d.setBlood_group(json.getJSONArray("data").getJSONObject(i).getString("blood_group"));
                                    d.setQualification(json.getJSONArray("data").getJSONObject(i).getString("qualification"));
                                    d.setProfession(json.getJSONArray("data").getJSONObject(i).getString("profession"));
                                    d.setMobile_no(json.getJSONArray("data").getJSONObject(i).getString("mobile_no"));
                                    d.setEmail(json.getJSONArray("data").getJSONObject(i).getString("email"));
                                    d.setPhoto_link(json.getJSONArray("data").getJSONObject(i).getString("photo_link"));
                                    d.setHusband_member_id(json.getJSONArray("data").getJSONObject(i).getString("husband_member_id"));
                                    d.setHusband_name(json.getJSONArray("data").getJSONObject(i).getString("husband_name"));
                                    d.setHusband_description(json.getJSONArray("data").getJSONObject(i).getString("husband_description"));
                                    d.setHusband_qualification(json.getJSONArray("data").getJSONObject(i).getString("husband_qualification"));
                                    d.setMembership_status(json.getJSONArray("data").getJSONObject(i).getString("membership_status"));
                                    d.setCreated_date(json.getJSONArray("data").getJSONObject(i).getString("created_date"));
                                    d.setModified_date(json.getJSONArray("data").getJSONObject(i).getString("modified_date"));
                                    d.setDependent_id(json.getJSONArray("data").getJSONObject(i).getString("dependent_id"));

                                    dependentList.add(d);
                                }
                                mAdapter.notifyDataSetChanged();
                                recyclerView.getAdapter().notifyDataSetChanged();
                                initializeAdapter();
                            } else {
                                //ProgressDialogView.hideProgressDialog();
                                DialogUtil.showSnackBarWithAction(relativeLayout, json.getString("message"));
                            }

                            progress.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            progress.dismiss();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG", "Volley Error in Response = " + error.getMessage());
                        progress.dismiss();
                        DialogUtil.showSnackBarWithAction(relativeLayout, error.getMessage());
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            DialogUtil.showSnackBarWithAction(relativeLayout, "Network timeout error");
                        } else if (error instanceof AuthFailureError) {
                            DialogUtil.showSnackBarWithAction(relativeLayout, "There was an authentication failure while performing a Request.");
                        } else if (error instanceof ServerError) {
                            DialogUtil.showSnackBarWithAction(relativeLayout, "ServerError");
                        } else if (error instanceof NetworkError) {
                            DialogUtil.showSnackBarWithAction(relativeLayout, "NetworkError");
                        } else if (error instanceof ParseError) {
                            DialogUtil.showSnackBarWithAction(relativeLayout, "ParseError, Server's response could not be parsed.");
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonRequest);
    }

    @Override
    public void onClick(View view, int position, String mobileNo) {
//        Log.d(TAG, "position = " + position);
//        Log.d(TAG, "mobileNo = " + mobileNo);
//        Intent intent = new Intent(Intent.ACTION_DIAL);
//        intent.setData(Uri.parse("tel:" + mobileNo));
//        startActivity(intent);
    }
}
