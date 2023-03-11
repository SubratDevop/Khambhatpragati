package com.khambhatpragati.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.ImageLoader;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.khambhatpragati.R;
import com.khambhatpragati.adapter.MessagesAdapter;
import com.khambhatpragati.application.Globals;
import com.khambhatpragati.constants.URLs;
import com.khambhatpragati.helper.CustomVolleyRequest;
import com.khambhatpragati.helper.PreferencesManager;
import com.khambhatpragati.listeners.MessagesItemClickListener;
import com.khambhatpragati.model.response.AdvertismentResponse;
import com.khambhatpragati.model.response.MessagesBean;
import com.khambhatpragati.model.response.GroupsBean;
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
import static com.khambhatpragati.constants.Keys.KEY_GROUPS_ID;
import static com.khambhatpragati.constants.Keys.KEY_MESSAGES_LIST;
import static com.khambhatpragati.constants.Keys.KEY_MESSAGES_POSTED_BY;
import static com.khambhatpragati.constants.Keys.KEY_MESSAGES_TITLE;
import static com.khambhatpragati.constants.Keys.KEY_MESSAGES_DESCRIPTION;
import static com.khambhatpragati.constants.Keys.KEY_ACCESS_LEVEL;

public class MessagesActivity extends AppCompatActivity implements MessagesItemClickListener {

    private static final String TAG = MessagesActivity.class.getSimpleName();

    private RelativeLayout relativeLayout,footer;

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton btnSendMessage;
    private MessagesAdapter mAdapter;
    private GroupsBean groupsBean;
    private ArrayList<MessagesBean> listOfMessages;
    private ImageLoader imageLoader;
    ImageView imageView;
    private String strAdWeblink="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        initView();
        initToolBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.d(TAG, "Globals.getInstance().isMsgSent(): " + Globals.getInstance().isMsgSent());
        if (Globals.getInstance().isMsgSent()) {
            Globals.getInstance().setMsgSent(false);
            finish();
        }
    }

    @SuppressLint("RestrictedApi")
    private void initView() {
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        footer = (RelativeLayout) findViewById(R.id.footer);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        btnSendMessage = (FloatingActionButton) findViewById(R.id.btnSendMessage);
        groupsBean = Globals.getInstance().getGroupsBean();

        listOfMessages = new ArrayList<>();

//        Log.d(TAG, "groupsBean.getAccessLevel() = " + groupsBean.getAccessLevel());

        if (groupsBean.getAccessLevel().equals("2")) {
            btnSendMessage.setVisibility(View.VISIBLE);
        }else{
            btnSendMessage.setVisibility(View.GONE);
        }

        addListenerOnWidgets();
        displayAllMessages();


        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (adsImageUrls.size() > 0) {
                    if (adCounter >= adsImageUrls.size())
                        adCounter = 0;
                    Intent i = new Intent(MessagesActivity.this, FullScreenAdActivity.class);
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
                    Intent i = new Intent(MessagesActivity.this, FullScreenAdActivity.class);
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
                    Intent intent = new Intent(MessagesActivity.this, WebviewActivity.class);
                    intent.putExtra("strValue", strAdWeblink);
                    startActivity(intent);
                }
            }
        });
        getBottomAds();
    }
    private void getBottomAds() {
        final ProgressDialog progress = new ProgressDialog(MessagesActivity.this);
        progress.setMessage("Please Wait....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

        AQuery aQuery = new AQuery(getApplicationContext());
        Map<String, Object> params = new HashMap<>();
        params.put("advertisement_type","BottomAd");
        params.put("screen_type","MandalMessages Screen");

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
                            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                            String todayString = formatter.format(todayDate);

                            for(int i=0;i<adsResponse.getData().size();i++){
                                if(formatter.parse(adsResponse.getData().get(i).getEnd_date()).after(formatter.parse(todayString))
                                        || formatter.parse(adsResponse.getData().get(i).getEnd_date()).equals(formatter.parse(todayString))) {
                                    if(adsResponse.getData().get(i).getAdvertisement_type().equalsIgnoreCase("BottomAd")
                                            && adsResponse.getData().get(i).getScreen_type().equalsIgnoreCase("MandalMessages Screen")) {
                                        imageView.setVisibility(View.VISIBLE);
                                        strAdWeblink=adsResponse.getData().get(i).getWebsite_url();
                                        imageLoader = CustomVolleyRequest.getInstance(MessagesActivity.this).getImageLoader();
                                        if(!adsResponse.getData().get(i).getFull_path().equalsIgnoreCase("NULL")
                                                && adsResponse.getData().get(i).getFull_path().startsWith("http"))
                                        imageLoader.get(adsResponse.getData().get(i).getFull_path(), ImageLoader.getImageListener(imageView, R.mipmap.noimage, android.R.drawable.ic_dialog_alert));
                                    }
                                }
                            }

                        } else {
                            /*RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT
                            );
                            params.setMargins(5, 5, 5, 5);
                            footer.setLayoutParams(params);*/
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

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Announcements");//groupsBean.getMandalName());
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

    private void displayAllMessages() {
        String msgJsonList = PreferencesManager.getString(MessagesActivity.this, KEY_MESSAGES_LIST);
        listOfMessages = JSONParser.parseMessageList(msgJsonList);
        ArrayList<MessagesBean> filteredMsgList = new ArrayList<>();

        if (listOfMessages != null && listOfMessages.size() > 0) {
            for (MessagesBean msgBean : listOfMessages) {
                if (groupsBean.getMandalID().equals(msgBean.getMandalId())) {
                    filteredMsgList.add(msgBean);
                }
            }
        }

        //Log.d(TAG, "filteredMsgList.size() = " + filteredMsgList.size());

        if (filteredMsgList != null && filteredMsgList.size() > 0) {
            ArrayList<MessagesBean> sortedMsgList = AppUtil.sortMsgListByModifiedDate(filteredMsgList);
            if (sortedMsgList != null && sortedMsgList.size() > 0) {
                mAdapter = new MessagesAdapter(sortedMsgList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
                mAdapter.setClickListener(this);
            }
        } else {
            DialogUtil.showSnackBarWithAction(relativeLayout, getString(R.string.msg_msg_not_available_for_group));
        }
    }

    private void addListenerOnWidgets() {
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MessagesActivity.this, SendMessageActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_up, R.anim.stay);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contact, menu); //your file name
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_contact:
                Intent i = new Intent(MessagesActivity.this, UserDirectoryActivity.class);
                i.putExtra(KEY_GROUPS_ID, groupsBean.getMandalID());
                i.putExtra(KEY_ACCESS_LEVEL, groupsBean.getAccessLevel());
                startActivity(i);
                overridePendingTransition(R.anim.slide_up, R.anim.stay);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Messages Listener's onClick
    @Override
    public void onClick(View view, int position, MessagesBean allMessagesBean) {
        //Log.d(TAG, "position = " + position);
        //Log.d(TAG, "groupId = " + allMessagesBean.getTitle());
        Intent i = new Intent(MessagesActivity.this, MessageDescriptionActivity.class);
        i.putExtra(KEY_MESSAGES_TITLE, allMessagesBean.getTitle());
        i.putExtra("KEY_DATETIME", allMessagesBean.getModifiedDateTime());
        //chetana for hyperlinks
        String hyperlink="",text="";
//        if(allMessagesBean.getDescription().contains(":")) {
//            hyperlink = allMessagesBean.getDescription().substring(allMessagesBean.getDescription().indexOf(":") - 4);
//            text = allMessagesBean.getDescription().substring(0, allMessagesBean.getDescription().indexOf(":") - 4);
//            i.putExtra(KEY_MESSAGES_DESCRIPTION, text);
//            i.putExtra("HYPERLINK", hyperlink);
//        }
//        else if(!extractNumber(allMessagesBean.getDescription()).equalsIgnoreCase("")
//        && extractNumber(allMessagesBean.getDescription()).length()>=10){
//            Matcher matcher = Pattern.compile("\\d+").matcher(allMessagesBean.getDescription());
//            matcher.find();
//
//            String number = extractNumber(allMessagesBean.getDescription());
//            text = allMessagesBean.getDescription().substring(0, allMessagesBean.getDescription().indexOf(number.charAt(0)));
//            i.putExtra(KEY_MESSAGES_DESCRIPTION, text);
//            i.putExtra("HYPERLINK", extractNumber(allMessagesBean.getDescription()));
//        }
//        else{
            //i.putExtra("HYPERLINK", hyperlink);
  //      }
        i.putExtra(KEY_MESSAGES_DESCRIPTION, allMessagesBean.getDescription());
        i.putExtra(KEY_MESSAGES_POSTED_BY, allMessagesBean.getUserName());
        startActivity(i);
        overridePendingTransition(R.anim.slide_up, R.anim.stay);
    }

//    public static String extractNumber(final String str) {
//
//        if(str == null || str.isEmpty()) return "";
//
//        StringBuilder sb = new StringBuilder();
//        boolean found = false;
//        for(char c : str.toCharArray()){
//            if(Character.isDigit(c)){
//                sb.append(c);
//                found = true;
//            } else if(found){
//                // If we already found a digit before and this char is not a digit, stop looping
//                break;
//            }
//        }
//
//        return sb.toString();
//    }
}