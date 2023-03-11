package com.khambhatpragati.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static com.khambhatpragati.activity.DashboardActivity.adCounter;
import static com.khambhatpragati.activity.DashboardActivity.adsImageUrls;
import static com.khambhatpragati.activity.DashboardActivity.isAdsTicking;
import static com.khambhatpragati.activity.DashboardActivity.isShownOnce;
import static com.khambhatpragati.constants.Keys.KEY_DAY_TIMESTAMP;
import static com.khambhatpragati.constants.Keys.KEY_MESSAGES_TITLE;
import static com.khambhatpragati.constants.Keys.KEY_MESSAGES_DESCRIPTION;
import static com.khambhatpragati.constants.Keys.KEY_MESSAGES_POSTED_BY;

import com.android.volley.toolbox.ImageLoader;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.khambhatpragati.R;
import com.khambhatpragati.constants.URLs;
import com.khambhatpragati.helper.CustomVolleyRequest;
import com.khambhatpragati.model.response.AdvertismentResponse;
import com.khambhatpragati.parser.JSONParser;
import com.khambhatpragati.utils.DialogUtil;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MessageDescriptionActivity extends AppCompatActivity {

    private static final String TAG = MessageDescriptionActivity.class.getSimpleName();

    private Toolbar toolbar;
    private TextView tvMessageTitle;
    private TextView tvMessageDesc;
    //private TextView tvMessageDescHyperLink;
    private TextView tvMessagePostedBy;
    private TextView tvMessagePostedDate;
    private ImageLoader imageLoader;
    ImageView imageView;
    RelativeLayout relativeLayout;
    private String strAdWeblink="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_description);
        initToolBar();
        initView();
    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.activity_title_messages_desc));
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
        tvMessageTitle = (TextView) findViewById(R.id.tvMessageTitle);
        tvMessageDesc = (TextView) findViewById(R.id.tvMessageDesc);
        //tvMessageDescHyperLink = (TextView) findViewById(R.id.tvMessageDescHyperlink);
        tvMessagePostedBy = (TextView) findViewById(R.id.tvMessagePostedBy);
        tvMessagePostedDate = (TextView) findViewById(R.id.tvMessagePostedDate);
        relativeLayout =  findViewById(R.id.relativeLayout);

       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (adsImageUrls.size() > 0) {
                    if (adCounter >= adsImageUrls.size())
                        adCounter = 0;
                    Intent i = new Intent(MessageDescriptionActivity.this, FullScreenAdActivity.class);
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
                    Intent i = new Intent(MessageDescriptionActivity.this, FullScreenAdActivity.class);
                    i.putExtra("adURL", adsImageUrls.get(adCounter).getFull_path());
                    i.putExtra("adWebLink", adsImageUrls.get(adCounter).getWebsite_url());
                    i.putExtra("adTIMEOUT", adsImageUrls.get(adCounter).getDisplay_time());
                    startActivity(i);
                }
                isShownOnce=true;
                isAdsTicking=true;
            }
        }

        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tvMessageTitle.setText(extras.getString(KEY_MESSAGES_TITLE));
            tvMessageDesc.setText(extras.getString(KEY_MESSAGES_DESCRIPTION));

            Linkify.addLinks(tvMessageDesc, Linkify.ALL);
//chetana for hyperlinks
//                SpannableString ss = new SpannableString(extras.getString("HYPERLINK"));
//                SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
//                stringBuilder.append(ss);
//                stringBuilder.setSpan(new ClickableSpan() {
//                                          @Override
//                                          public void onClick(View widget) {
//                                              if(extras.getString("HYPERLINK").contains(":")) {
//                                              Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(extras.getString("HYPERLINK")));
//                                              startActivity(browse);
//                                              }else{
//                                                  Intent intent = new Intent(Intent.ACTION_DIAL);
//                                                  intent.setData(Uri.parse("tel:"+extras.getString("HYPERLINK")));
//                                                  startActivity(intent);
//                                              }
//                                          }
//                                      }, 0,
//                        ss.length(),
//                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                tvMessageDescHyperLink.setText(ss);
//                tvMessageDescHyperLink.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if(extras.getString("HYPERLINK").contains(":")){
//
//                        }else{
//                            Intent intent = new Intent(Intent.ACTION_DIAL);
//                            intent.setData(Uri.parse("tel:"+extras.getString("HYPERLINK")));
//                            startActivity(intent);
//                        }
//                    }
//                });
//                tvMessageDescHyperLink.setMovementMethod(LinkMovementMethod.getInstance());
//                tvMessageDescHyperLink.setHighlightColor(Color.TRANSPARENT);

            tvMessagePostedBy.setText("Posted by "+extras.getString(KEY_MESSAGES_POSTED_BY));
            tvMessagePostedDate.setText(extras.getString("KEY_DATETIME"));
        } else {
            tvMessageDesc.setText("Message discription not found!");
        }
        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!strAdWeblink.equalsIgnoreCase("") && !strAdWeblink.equalsIgnoreCase("null")) {
                    Intent intent = new Intent(MessageDescriptionActivity.this, WebviewActivity.class);
                    intent.putExtra("strValue", strAdWeblink);
                    startActivity(intent);
                }
            }
        });
        getBottomAds();
    }
    private void getBottomAds() {
        final ProgressDialog progress = new ProgressDialog(MessageDescriptionActivity.this);
        progress.setMessage("Please Wait....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

        AQuery aQuery = new AQuery(getApplicationContext());
        Map<String, Object> params = new HashMap<>();
        params.put("advertisement_type","BottomAd");
        params.put("screen_type","MessageDetail Screen");

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
                                            && adsResponse.getData().get(i).getScreen_type().equalsIgnoreCase("MessageDetail Screen")) {
                                        imageView.setVisibility(View.VISIBLE);
                                        strAdWeblink=adsResponse.getData().get(i).getWebsite_url();
                                        imageLoader = CustomVolleyRequest.getInstance(MessageDescriptionActivity.this).getImageLoader();
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

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }
}