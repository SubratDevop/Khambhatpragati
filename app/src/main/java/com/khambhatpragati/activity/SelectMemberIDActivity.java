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

import com.android.volley.toolbox.ImageLoader;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.khambhatpragati.R;
import com.khambhatpragati.adapter.MemberIDAdapter;
import com.khambhatpragati.adapter.UserDirectoryAdapter;
import com.khambhatpragati.application.Globals;
import com.khambhatpragati.constants.URLs;
import com.khambhatpragati.db.DBHelper;
import com.khambhatpragati.helper.CustomVolleyRequest;
import com.khambhatpragati.listeners.MemberIDClickListener;
import com.khambhatpragati.model.response.AdvertismentResponse;
import com.khambhatpragati.model.response.UserDirectoryBean;
import com.khambhatpragati.parser.JSONParser;
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

public class SelectMemberIDActivity extends AppCompatActivity implements MemberIDClickListener {

    private static final String TAG = SelectMemberIDActivity.class.getSimpleName();
    private RelativeLayout relativeLayout;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private MemberIDAdapter mAdapter;
    ImageView imageView;
    private String selectedGroupId;
    private String accessLevel;
    private ImageLoader imageLoader;
    private String strAdWeblink="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_directory);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            selectedGroupId = extras.getString(KEY_GROUPS_ID);
            accessLevel = extras.getString(KEY_ACCESS_LEVEL);
        }
        initToolBar();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Globals.getInstance().isUserAdded()){
            displaySelectedGroupsContacts();
        }
    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(getIntent().getStringExtra("callingFrom").equalsIgnoreCase("Parent"))
            toolbar.setTitle("Select Parent ID");
        else
            toolbar.setTitle("Select Brother ID");
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

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        //Log.d(TAG, "selectedGroupId: " + selectedGroupId);
        if (selectedGroupId.isEmpty()) {
            displayAllContacts();
        } else {
            displaySelectedGroupsContacts();
        }


        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (adsImageUrls.size() > 0) {
                    if (adCounter >= adsImageUrls.size())
                        adCounter = 0;
                    Intent i = new Intent(SelectMemberIDActivity.this, FullScreenAdActivity.class);
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
                    Intent i = new Intent(SelectMemberIDActivity.this, FullScreenAdActivity.class);
                    i.putExtra("adURL", adsImageUrls.get(adCounter).getFull_path());
                    i.putExtra("adWebLink", adsImageUrls.get(adCounter).getWebsite_url());
                    i.putExtra("adTIMEOUT", adsImageUrls.get(adCounter).getDisplay_time());
                    startActivity(i);
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
                    Intent intent = new Intent(SelectMemberIDActivity.this, WebviewActivity.class);
                    intent.putExtra("strValue", strAdWeblink);
                    startActivity(intent);
                }
            }
        });
        getBottomAds();
    }
    private void getBottomAds() {
        final ProgressDialog progress = new ProgressDialog(SelectMemberIDActivity.this);
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
                                        imageLoader = CustomVolleyRequest.getInstance(SelectMemberIDActivity.this).getImageLoader();
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



    private void displayAllContacts() {
        DBHelper dbHelper = new DBHelper(SelectMemberIDActivity.this);
        ArrayList<UserDirectoryBean> listOfUserDirectory = JSONParser.parseUserDirectoryList(dbHelper.selectResponse(URLs.API_NAME_USER_DIRECTORY));

        if (listOfUserDirectory != null && listOfUserDirectory.size() > 0) {
            ArrayList<UserDirectoryBean> sortedUsersList = AppUtil.sortUserDirectoryListByName(getUniqueUsers(listOfUserDirectory));
            mAdapter = new MemberIDAdapter(SelectMemberIDActivity.this, sortedUsersList, selectedGroupId);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            mAdapter.setClickListener(this);
        } else {
            DialogUtil.showSnackBarWithAction(relativeLayout, getString(R.string.msg_users_not_found));
        }
    }

    private void displaySelectedGroupsContacts() {
        DBHelper dbHelper = new DBHelper(SelectMemberIDActivity.this);
        ArrayList<UserDirectoryBean> listOfUserDirectory = JSONParser.parseUserDirectoryList(dbHelper.selectResponse(URLs.API_NAME_USER_DIRECTORY));
        if (listOfUserDirectory != null && listOfUserDirectory.size() > 0) {
            ArrayList<UserDirectoryBean> sortedUsersList = AppUtil.sortUserDirectoryListByName(getAllUsersOfGroup(listOfUserDirectory));
            mAdapter = new MemberIDAdapter(SelectMemberIDActivity.this, sortedUsersList, selectedGroupId);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            mAdapter.setClickListener(this);
        } else {
            DialogUtil.showSnackBarWithAction(relativeLayout, getString(R.string.msg_users_not_found));
        }
    }

    private ArrayList<UserDirectoryBean> getUniqueUsers(ArrayList<UserDirectoryBean> allUsers) {
        ArrayList<UserDirectoryBean> noRepeat = new ArrayList<UserDirectoryBean>();
        for (UserDirectoryBean user : allUsers) {
            boolean isFound = false;
            for (UserDirectoryBean u : noRepeat) {
                //chetana
                if(u!=null) {
                    if(u.getMobileNo()!=null) {
                        if (u.getMobileNo().equals(user.getMobileNo())) {
                            isFound = true;
                        }
                    }
                }
            }

            if (!isFound)
                noRepeat.add(user);
        }
//        Log.d(TAG, "All Users: " + allUsers.size());
//        Log.d(TAG, "Unique Users: " + noRepeat.size());
        return noRepeat;
    }

    private ArrayList<UserDirectoryBean> getAllUsersOfGroup(ArrayList<UserDirectoryBean> allUsers) {
        ArrayList<UserDirectoryBean> listAllUsersOfGroup = new ArrayList<UserDirectoryBean>();
        for (UserDirectoryBean user : allUsers) {
            if (user.getMandalID().equals(selectedGroupId)) {
                listAllUsersOfGroup.add(user);
            }
        }
        return listAllUsersOfGroup;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_directory, menu);

        MenuItem actionAddUser = menu.findItem(R.id.action_add_user);
        actionAddUser.setVisible(false);

        MenuItem actionSearch = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) actionSearch.getActionView();
        //searchView.setQueryHint(Html.fromHtml("<font color = #ffffff>" + "Search Text" + "</font>"));
        EditText searchEditText = (EditText) searchView.findViewById(R.id.search_src_text);
        searchEditText.setTextColor(Color.WHITE);
        searchEditText.setHintTextColor(Color.WHITE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    mAdapter.filter("");
                    //recyclerView.clearTextFilter();
                } else {
                    mAdapter.filter(newText);
                }

                //Log.d(TAG, "newText = " + newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view, int position, String mobileNo, String name, String id) {
//        Log.d(TAG, "position = " + position);
//        Log.d(TAG, "mobileNo = " + mobileNo);
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        if(getIntent().getStringExtra("callingFrom").equalsIgnoreCase("Parent")) {
            intent.putExtra("pMobile", mobileNo);
            intent.putExtra("pName", name);
            intent.putExtra("pID", id);
        }else{
            intent.putExtra("bMobile", mobileNo);
            intent.putExtra("bName", name);
            intent.putExtra("bID", id);
        }
        setResult(RESULT_OK, intent);
        finish();
    }
}
