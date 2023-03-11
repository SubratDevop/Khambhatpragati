package com.khambhatpragati.fragments;

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
import android.view.LayoutInflater;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
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
import com.khambhatpragati.R;
import com.khambhatpragati.activity.DependentListActivity;
import com.khambhatpragati.activity.FullScreenAdActivity;
import com.khambhatpragati.activity.SelectMemberIDActivity;
import com.khambhatpragati.adapter.BrotherAdapter;
import com.khambhatpragati.helper.CustomVolleyRequest;
import com.khambhatpragati.model.BrotherModel;
import com.khambhatpragati.model.SpinnerModel;
import com.khambhatpragati.model.VastiModel;
import com.khambhatpragati.model.response.OTPVerificationResponse;
import com.khambhatpragati.parser.JSONParser;
import com.khambhatpragati.preference.UserPreference;
import com.khambhatpragati.utils.DialogUtil;

import org.json.JSONArray;
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
import static com.khambhatpragati.constants.Keys.KEY_GROUPS_ID;
import static com.khambhatpragati.constants.URLs.ADDBROTHER_URL;
import static com.khambhatpragati.constants.URLs.ADDMEMBERIMAGE_URL;
import static com.khambhatpragati.constants.URLs.GET_MEMBERDATA_URL;
import static com.khambhatpragati.constants.URLs.GET_MEMBERPROFILE_URL;
import static com.khambhatpragati.constants.URLs.GET_PARENTDATA_URL;
import static com.khambhatpragati.constants.URLs.UPDATEMEMBER_URL;

public class VastiProfileFragment extends Fragment {

    Context context;
    TextView tvMemno,tvFirstName,tvMiddleName,tvLastName,tvKnownLastName,tvGrandfather,tvParentMemno,tvGender,
            tvDOB,tvMemStatus,tvBloodGroup,tvMarritalStatus,tvMembershipStatus,tvQualification,tvProfession,tvBrotherlist,
            tvMandal,tvFlatNo,tvResidenceArea,tvNearestStn,tvCity,tvPicode,tvMobile,tvEmail,tvKhambat;
    LinearLayout relativeLayout;
    LinearLayout profileLV;
    VastiModel vastiModel;
    String titleText="";
    private ImageView imgProfile;

    public VastiProfileFragment(VastiModel v,String s) {
        this.vastiModel = v;
        this.titleText=s;
    }
    public static VastiProfileFragment newInstance(VastiModel v,String s) {
        VastiProfileFragment fragment = new VastiProfileFragment(v,s);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.vastiprofile_fragment, container, false);
        context = getActivity();

        profileLV = view.findViewById(R.id.profileLV);
        imgProfile = view.findViewById(R.id.imgProfile);
        relativeLayout = view.findViewById(R.id.relativeLayout);
        tvMemno = view.findViewById(R.id.tvMemNo);
        tvFirstName = view.findViewById(R.id.tvFirstName);
        tvMiddleName = view.findViewById(R.id.tvMiddleName);
        tvLastName = view.findViewById(R.id.tvLastName);
        tvKnownLastName = view.findViewById(R.id.tvLastKnown);
        tvGrandfather = view.findViewById(R.id.tvGrandFatherName);
        tvParentMemno = view.findViewById(R.id.tvparentMemno);
        tvGender = view.findViewById(R.id.tvGender);
        tvDOB = view.findViewById(R.id.tvDOB);
        tvMemStatus = view.findViewById(R.id.tvMemStatus);
        tvBloodGroup = view.findViewById(R.id.tvBloodGroup);
        tvMarritalStatus = view.findViewById(R.id.tvMarrital);
        tvMembershipStatus = view.findViewById(R.id.tvMembershipStatus);
        tvQualification = view.findViewById(R.id.tvQualification);
        tvProfession = view.findViewById(R.id.tvBusiness);
        tvBrotherlist = view.findViewById(R.id.tvBrothers);
        tvMandal = view.findViewById(R.id.tvMandal);
        tvFlatNo = view.findViewById(R.id.tvFlatno);
        tvResidenceArea = view.findViewById(R.id.tvRArea);
        tvNearestStn = view.findViewById(R.id.tvNearestStn);
        tvCity = view.findViewById(R.id.tvCity);
        tvPicode = view.findViewById(R.id.tvPincode);
        tvMobile = view.findViewById(R.id.tvMobile);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvKhambat = view.findViewById(R.id.tvKhambatAddress);

        if(titleText.equalsIgnoreCase("PARENT"))
            getParentData(vastiModel.getParent_member_id());
        else
            getMemberData(vastiModel.getMember_id(),vastiModel.getMobile_no());

        return view;
    }

    public void getMemberData(String id,String mobile) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

/*
        Map<String, Object> params = new HashMap<>();
        params.put("member_id", id);
        params.put("mobile_no", mobile);

        Log.d("Params", "Testing" + params);
*/

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, GET_MEMBERDATA_URL+"/"+id,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject json) {
                        try {
                            Log.d("TAG", "JSON POST Response = " + json.toString());

                            if (json.toString().contains("data")) {
                                OTPVerificationResponse otpResponse = JSONParser.parseOTPVerificationResponse(json.toString());
                                if (!otpResponse.isStatus()) {

                                    //JSONArray j = json.getJSONArray("data");
                                    JSONObject jObj = json.getJSONObject("data");
                                    ImageLoader imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
                                    if(jObj.getString("photo_link").trim().equalsIgnoreCase("NULL")){
                                        ;
                                    }else {
                                        if(jObj.getString("photo_link").trim().startsWith("http://"))
                                            imageLoader.get(jObj.getString("photo_link"), ImageLoader.getImageListener(imgProfile, R.mipmap.noimage, R.mipmap.profile));
                                        else
                                            imageLoader.get("http://khambhatpragati.com/uploads/member_images/"+jObj.getString("photo_link"), ImageLoader.getImageListener(imgProfile, R.mipmap.noimage, R.mipmap.profile));
                                    }
                                    if(!jObj.getString("member_id").equalsIgnoreCase("null")) {
                                        tvMemno.setText(jObj.getString("member_id"));
                                    }
                                    if(!jObj.getString("first_name").equalsIgnoreCase("null")) {
                                        tvFirstName.setText(jObj.getString("first_name"));
                                    }
                                    if(!jObj.getString("middle_name").equalsIgnoreCase("null")) {
                                        tvMiddleName.setText(jObj.getString("middle_name"));
                                    }
                                    if(!jObj.getString("last_name").equalsIgnoreCase("null")) {
                                        tvLastName.setText(jObj.getString("last_name"));
                                    }
                                    if(!jObj.getString("last_known_name").equalsIgnoreCase("null")) {
                                        tvKnownLastName.setText(jObj.getString("last_known_name"));
                                    }
                                    if(!jObj.getString("parent_member_id").equalsIgnoreCase("null")) {
                                        tvParentMemno.setText(jObj.getString("parent_member_id"));
                                    }
                                    if(!jObj.getString("grand_father_name").equalsIgnoreCase("null")) {
                                        tvGrandfather.setText(jObj.getString("grand_father_name"));
                                    }
                                    if(!jObj.getString("gender").equalsIgnoreCase("null")) {

                                        if(jObj.getString("gender").equalsIgnoreCase("1")
                                                ||jObj.getString("gender").equalsIgnoreCase("Male")){
                                            tvGender.setText("Male");
                                        }else if(jObj.getString("gender").equalsIgnoreCase("2")
                                                ||jObj.getString("gender").equalsIgnoreCase("FeMale")){
                                            tvGender.setText("Female");
                                        }

                                    }
                                    if(!jObj.getString("date_of_birth").equalsIgnoreCase("null")) {
                                        tvDOB.setText(jObj.getString("date_of_birth"));
                                    }
                                    if(!jObj.getString("member_status").equalsIgnoreCase("null")) {
                                        String strMemStatus="";
                                        if(jObj.getString("member_status").equalsIgnoreCase("1")
                                                ||jObj.getString("member_status").equalsIgnoreCase("Life-Member")
                                                ||jObj.getString("member_status").equalsIgnoreCase("Alive")){
                                            strMemStatus="Life-Member";
                                        }else if(jObj.getString("member_status").equalsIgnoreCase("2")
                                                ||jObj.getString("member_status").equalsIgnoreCase("0")
                                                ||jObj.getString("member_status").equalsIgnoreCase("Expired")){
                                            strMemStatus="Expired";
                                        }
                                        tvMemStatus.setText(strMemStatus);
                                    }
                                    if(!jObj.getString("is_committee_member").equalsIgnoreCase("null")) {
                                        String strMandal="";
                                        if(jObj.getString("is_committee_member").equalsIgnoreCase("1")
                                                ||jObj.getString("is_committee_member").equalsIgnoreCase("Yes")){
                                            strMandal="Yes";
                                        }else if(jObj.getString("is_committee_member").equalsIgnoreCase("2")
                                                ||jObj.getString("is_committee_member").equalsIgnoreCase("No")
                                                ||jObj.getString("is_committee_member").equalsIgnoreCase("0")){
                                            strMandal="No";
                                        }
                                        tvMandal.setText(strMandal);
                                    }
                                    if(!jObj.getString("blood_group").equalsIgnoreCase("null")) {
                                        String strBloodGroup="";
                                        if(jObj.getString("blood_group").equalsIgnoreCase("1")
                                                ||jObj.getString("blood_group").equalsIgnoreCase("A+")){
                                            strBloodGroup="A+";
                                        }else if(jObj.getString("blood_group").equalsIgnoreCase("2")
                                                ||jObj.getString("blood_group").equalsIgnoreCase("A-")){
                                            strBloodGroup="A-";
                                        }else if(jObj.getString("blood_group").equalsIgnoreCase("3")
                                                ||jObj.getString("blood_group").equalsIgnoreCase("B+")){
                                            strBloodGroup="B+";
                                        }else if(jObj.getString("blood_group").equalsIgnoreCase("4")
                                                ||jObj.getString("blood_group").equalsIgnoreCase("B-")){
                                            strBloodGroup="B-";
                                        }else if(jObj.getString("blood_group").equalsIgnoreCase("5")
                                                ||jObj.getString("blood_group").equalsIgnoreCase("AB+")){
                                            strBloodGroup="AB+";
                                        }else if(jObj.getString("blood_group").equalsIgnoreCase("6")
                                                ||jObj.getString("blood_group").equalsIgnoreCase("AB-")){
                                            strBloodGroup="AB-";
                                        }else if(jObj.getString("blood_group").equalsIgnoreCase("7")
                                                ||jObj.getString("blood_group").equalsIgnoreCase("O+")){
                                            strBloodGroup="O+";
                                        }else if(jObj.getString("blood_group").equalsIgnoreCase("8")
                                                ||jObj.getString("blood_group").equalsIgnoreCase("O-")){
                                            strBloodGroup="O-";
                                        }
                                        tvBloodGroup.setText(strBloodGroup);
                                    }
                                    if(!jObj.getString("marital_status").equalsIgnoreCase("null")) {
                                        String strMarritalStatus="";
                                        if(jObj.getString("marital_status").equalsIgnoreCase("1")
                                                ||jObj.getString("marital_status").equalsIgnoreCase("Married")){
                                            strMarritalStatus="Married";
                                        }else if(jObj.getString("marital_status").equalsIgnoreCase("2")
                                                ||jObj.getString("marital_status").equalsIgnoreCase("UnMarried")){
                                            strMarritalStatus="Unmarried";
                                        }
                                        tvMarritalStatus.setText(strMarritalStatus);
                                    }
                                    if(!jObj.getString("qualification").equalsIgnoreCase("null")) {
                                        String strQualification="";
                                        if(jObj.getString("qualification").equalsIgnoreCase("1")
                                                ||jObj.getString("qualification").equalsIgnoreCase("Student")){
                                            strQualification="Student";
                                        }else if(jObj.getString("qualification").equalsIgnoreCase("2")
                                                ||jObj.getString("qualification").equalsIgnoreCase("10th")){
                                            strQualification="10th";
                                        }else if(jObj.getString("qualification").equalsIgnoreCase("3")
                                                ||jObj.getString("qualification").equalsIgnoreCase("12th")){
                                            strQualification="12th";
                                        }else if(jObj.getString("qualification").equalsIgnoreCase("4")
                                                ||jObj.getString("qualification").equalsIgnoreCase("Graduate")){
                                            strQualification="Graduate";
                                        }else if(jObj.getString("qualification").equalsIgnoreCase("5")
                                                ||jObj.getString("qualification").equalsIgnoreCase("Post-Graduate")
                                                ||jObj.getString("qualification").equalsIgnoreCase("PostGraduate")
                                                ||jObj.getString("qualification").equalsIgnoreCase("Post Graduate")){
                                            strQualification="Post-Graduate";
                                        }else if(jObj.getString("qualification").equalsIgnoreCase("6")
                                                ||jObj.getString("qualification").equalsIgnoreCase("Professional")){
                                            strQualification="Professional";
                                        }
                                        tvQualification.setText(strQualification);
                                    }
                                    if(!jObj.getString("profession").equalsIgnoreCase("null")) {
                                        String strBusiness="";
                                        if(jObj.getString("profession").equalsIgnoreCase("1")
                                                ||jObj.getString("profession").equalsIgnoreCase("Doctor")){
                                            strBusiness="Doctor";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("2")
                                                ||jObj.getString("profession").equalsIgnoreCase("Lawyer")){
                                            strBusiness="Lawyer";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("3")
                                                ||jObj.getString("profession").equalsIgnoreCase("Architects")){
                                            strBusiness="Architects";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("4")
                                                ||jObj.getString("profession").equalsIgnoreCase("CA/CS/ICWA")){
                                            strBusiness="CA/CS/ICWA";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("5")
                                                ||jObj.getString("profession").equalsIgnoreCase("MBA")){
                                            strBusiness="MBA";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("6")
                                                ||jObj.getString("profession").equalsIgnoreCase("Teacher/Professor")){
                                            strBusiness="Teacher/Professor";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("7")
                                                ||jObj.getString("profession").equalsIgnoreCase("Interior Designer")){
                                            strBusiness="Interior Designer";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("8")
                                                ||jObj.getString("profession").equalsIgnoreCase("Fashion Designer")){
                                            strBusiness="Fashion Designer";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("9")
                                                ||jObj.getString("profession").equalsIgnoreCase("Engineer")){
                                            strBusiness="Engineer";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("10")
                                                ||jObj.getString("profession").equalsIgnoreCase("Civil engineer")){
                                            strBusiness="Civil engineer";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("11")
                                                ||jObj.getString("profession").equalsIgnoreCase("Business")){
                                            strBusiness="Business";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("12")
                                                ||jObj.getString("profession").equalsIgnoreCase("Service")){
                                            strBusiness="Service";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("13")
                                                ||jObj.getString("profession").equalsIgnoreCase("Housewife")){
                                            strBusiness="Housewife";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("14")
                                                ||jObj.getString("profession").equalsIgnoreCase("Retired")){
                                            strBusiness="Retired";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("15")
                                                ||jObj.getString("profession").equalsIgnoreCase("Other")){
                                            strBusiness="other";
                                        }
                                        tvProfession.setText(strBusiness);
                                    }
                                    if(!jObj.getString("flat_no_building").equalsIgnoreCase("null")) {
                                        tvFlatNo.setText(jObj.getString("flat_no_building"));
                                    }
                                    if(!jObj.getString("residence_area").equalsIgnoreCase("null")) {
                                        tvResidenceArea.setText(jObj.getString("residence_area"));
                                    }
                                    if(!jObj.getString("nearest_station").equalsIgnoreCase("null")) {
                                        tvNearestStn.setText(jObj.getString("nearest_station"));
                                    }
                                    if(!jObj.getString("city").equalsIgnoreCase("null")) {
                                        tvCity.setText(jObj.getString("city"));
                                    }
                                    if(!jObj.getString("pincode").equalsIgnoreCase("null")) {
                                        tvPicode.setText(jObj.getString("pincode"));
                                    }
                                    if(!jObj.getString("kambhat_address").equalsIgnoreCase("null")) {
                                        tvKhambat.setText(jObj.getString("kambhat_address"));
                                    }
                                    if(!jObj.getString("mobile_no").equalsIgnoreCase("null")) {
                                        tvMobile.setText(jObj.getString("mobile_no"));
                                    }
                                    if(!jObj.getString("email").equalsIgnoreCase("null")) {
                                        tvEmail.setText(jObj.getString("email"));
                                    }
                                    if(!jObj.getString("membership_status").equalsIgnoreCase("null")) {
                                        String strMembershipStatus="";
                                        if(jObj.getString("membership_status").equalsIgnoreCase("1")
                                                ||jObj.getString("membership_status").equalsIgnoreCase("Active")){
                                            strMembershipStatus="Active";
                                        }else if(jObj.getString("membership_status").equalsIgnoreCase("0")
                                                ||jObj.getString("membership_status").equalsIgnoreCase("2")
                                                ||jObj.getString("membership_status").equalsIgnoreCase("InActive")
                                                ||jObj.getString("membership_status").equalsIgnoreCase("In Active")
                                                ||jObj.getString("membership_status").equalsIgnoreCase("In-Active")){
                                            strMembershipStatus="In-Active";
                                        }
                                        tvMembershipStatus.setText(strMembershipStatus);
                                    }
/*                                    JSONArray jBro = jObj.getJSONArray("brotherInfo");
                                    String strBrotherList="";
                                    for(int i=0;i<jBro.length();i++){
                                        if(strBrotherList.equalsIgnoreCase(""))
                                            strBrotherList=jBro.getJSONObject(i).getString("name") +
                                                    " - " + jBro.getJSONObject(i).getString("brother_id");
                                        else
                                            strBrotherList += " , "+jBro.getJSONObject(i).getString("name") +
                                                    " - " + jBro.getJSONObject(i).getString("brother_id");
                                    }
                                    tvBrotherlist.setText(strBrotherList);*/
                                } else {
                                    //ProgressDialogView.hideProgressDialog();
                                    DialogUtil.showSnackBarWithAction(relativeLayout, otpResponse.getMessage());
                                }
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


    public void getMemberProfile(String id,String mobile) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

        Map<String, Object> params = new HashMap<>();
        params.put("member_id", id);
        params.put("mobile_no", mobile);

        Log.d("Params", "Testing" + params);

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, GET_MEMBERPROFILE_URL,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject json) {
                        try {
                            Log.d("TAG", "JSON POST Response = " + json.toString());

                            if (json.toString().contains("data")) {
                                OTPVerificationResponse otpResponse = JSONParser.parseOTPVerificationResponse(json.toString());
                                if (!otpResponse.isStatus()) {

                                    JSONArray j = json.getJSONArray("data");
                                    JSONObject jObj = j.getJSONObject(0);
                                    ImageLoader imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
                                    if(jObj.getString("imageUrl").trim().equalsIgnoreCase("NULL")){
                                        ;
                                    }else {
                                        if(jObj.getString("imageUrl").trim().startsWith("http://"))
                                            imageLoader.get(jObj.getString("imageUrl"), ImageLoader.getImageListener(imgProfile, R.mipmap.noimage, R.mipmap.profile));
                                        else
                                            imageLoader.get("http://khambhatpragati.com/uploads/member_images/"+jObj.getString("imageUrl"), ImageLoader.getImageListener(imgProfile, R.mipmap.noimage, R.mipmap.profile));
                                    }
                                    if(!jObj.getString("memberID").equalsIgnoreCase("null")) {
                                        tvMemno.setText(jObj.getString("memberID"));
                                    }
                                    if(!jObj.getString("firstName").equalsIgnoreCase("null")) {
                                        tvFirstName.setText(jObj.getString("firstName"));
                                    }
                                    if(!jObj.getString("middleName").equalsIgnoreCase("null")) {
                                        tvMiddleName.setText(jObj.getString("middleName"));
                                    }
                                    if(!jObj.getString("lastName").equalsIgnoreCase("null")) {
                                        tvLastName.setText(jObj.getString("lastName"));
                                    }
                                    if(!jObj.getString("lastKnownName").equalsIgnoreCase("null")) {
                                        tvKnownLastName.setText(jObj.getString("lastKnownName"));
                                    }
                                    if(!jObj.getString("parentMemberId").equalsIgnoreCase("null")) {
                                        tvParentMemno.setText(jObj.getString("parentMemberId"));
                                    }
                                    if(!jObj.getString("grandFatherName").equalsIgnoreCase("null")) {
                                        tvGrandfather.setText(jObj.getString("grandFatherName"));
                                    }
                                    if(!jObj.getString("gender").equalsIgnoreCase("null")) {

                                        if(jObj.getString("gender").equalsIgnoreCase("1")
                                                ||jObj.getString("gender").equalsIgnoreCase("Male")){
                                            tvGender.setText("Male");
                                        }else if(jObj.getString("gender").equalsIgnoreCase("2")
                                                ||jObj.getString("gender").equalsIgnoreCase("FeMale")){
                                            tvGender.setText("Female");
                                        }

                                    }
                                    if(!jObj.getString("dateOfBirth").equalsIgnoreCase("null")) {
                                        tvDOB.setText(jObj.getString("dateOfBirth"));
                                    }
                                    if(!jObj.getString("memberStatus").equalsIgnoreCase("null")) {
                                        String strMemStatus="";
                                        if(jObj.getString("memberStatus").equalsIgnoreCase("1")
                                                ||jObj.getString("memberStatus").equalsIgnoreCase("Life-Member")
                                                ||jObj.getString("memberStatus").equalsIgnoreCase("Alive")){
                                            strMemStatus="Life-Member";
                                        }else if(jObj.getString("memberStatus").equalsIgnoreCase("2")
                                                ||jObj.getString("memberStatus").equalsIgnoreCase("0")
                                                ||jObj.getString("memberStatus").equalsIgnoreCase("Expired")){
                                            strMemStatus="Expired";
                                        }
                                        tvMemStatus.setText(strMemStatus);
                                    }
                                    if(!jObj.getString("committeeMember").equalsIgnoreCase("null")) {
                                        String strMandal="";
                                        if(jObj.getString("committeeMember").equalsIgnoreCase("1")
                                                ||jObj.getString("committeeMember").equalsIgnoreCase("Yes")){
                                            strMandal="Yes";
                                        }else if(jObj.getString("committeeMember").equalsIgnoreCase("2")
                                                ||jObj.getString("committeeMember").equalsIgnoreCase("No")
                                                ||jObj.getString("committeeMember").equalsIgnoreCase("0")){
                                            strMandal="No";
                                        }
                                        tvMandal.setText(strMandal);
                                    }
                                    if(!jObj.getString("bloodGroup").equalsIgnoreCase("null")) {
                                        String strBloodGroup="";
                                        if(jObj.getString("bloodGroup").equalsIgnoreCase("1")
                                                ||jObj.getString("bloodGroup").equalsIgnoreCase("A+")){
                                            strBloodGroup="A+";
                                        }else if(jObj.getString("bloodGroup").equalsIgnoreCase("2")
                                                ||jObj.getString("bloodGroup").equalsIgnoreCase("A-")){
                                            strBloodGroup="A-";
                                        }else if(jObj.getString("bloodGroup").equalsIgnoreCase("3")
                                                ||jObj.getString("bloodGroup").equalsIgnoreCase("B+")){
                                            strBloodGroup="B+";
                                        }else if(jObj.getString("bloodGroup").equalsIgnoreCase("4")
                                                ||jObj.getString("bloodGroup").equalsIgnoreCase("B-")){
                                            strBloodGroup="B-";
                                        }else if(jObj.getString("bloodGroup").equalsIgnoreCase("5")
                                                ||jObj.getString("bloodGroup").equalsIgnoreCase("AB+")){
                                            strBloodGroup="AB+";
                                        }else if(jObj.getString("bloodGroup").equalsIgnoreCase("6")
                                                ||jObj.getString("bloodGroup").equalsIgnoreCase("AB-")){
                                            strBloodGroup="AB-";
                                        }else if(jObj.getString("bloodGroup").equalsIgnoreCase("7")
                                                ||jObj.getString("bloodGroup").equalsIgnoreCase("O+")){
                                            strBloodGroup="O+";
                                        }else if(jObj.getString("bloodGroup").equalsIgnoreCase("8")
                                                ||jObj.getString("bloodGroup").equalsIgnoreCase("O-")){
                                            strBloodGroup="O-";
                                        }
                                        tvBloodGroup.setText(strBloodGroup);
                                    }
                                    if(!jObj.getString("maritalStatus").equalsIgnoreCase("null")) {
                                        String strMarritalStatus="";
                                        if(jObj.getString("maritalStatus").equalsIgnoreCase("1")
                                                ||jObj.getString("maritalStatus").equalsIgnoreCase("Married")){
                                            strMarritalStatus="Married";
                                        }else if(jObj.getString("maritalStatus").equalsIgnoreCase("2")
                                                ||jObj.getString("maritalStatus").equalsIgnoreCase("UnMarried")){
                                            strMarritalStatus="Unmarried";
                                        }
                                        tvMarritalStatus.setText(strMarritalStatus);
                                    }
                                    if(!jObj.getString("qualification").equalsIgnoreCase("null")) {
                                        String strQualification="";
                                        if(jObj.getString("qualification").equalsIgnoreCase("1")
                                                ||jObj.getString("qualification").equalsIgnoreCase("Student")){
                                            strQualification="Student";
                                        }else if(jObj.getString("qualification").equalsIgnoreCase("2")
                                                ||jObj.getString("qualification").equalsIgnoreCase("10th")){
                                            strQualification="10th";
                                        }else if(jObj.getString("qualification").equalsIgnoreCase("3")
                                                ||jObj.getString("qualification").equalsIgnoreCase("12th")){
                                            strQualification="12th";
                                        }else if(jObj.getString("qualification").equalsIgnoreCase("4")
                                                ||jObj.getString("qualification").equalsIgnoreCase("Graduate")){
                                            strQualification="Gratuate";
                                        }else if(jObj.getString("qualification").equalsIgnoreCase("5")
                                                ||jObj.getString("qualification").equalsIgnoreCase("Post-Graduate")
                                                ||jObj.getString("qualification").equalsIgnoreCase("PostGraduate")
                                                ||jObj.getString("qualification").equalsIgnoreCase("Post Graduate")){
                                            strQualification="Post-Graduate";
                                        }else if(jObj.getString("qualification").equalsIgnoreCase("6")
                                                ||jObj.getString("qualification").equalsIgnoreCase("Professional")){
                                            strQualification="Professional";
                                        }
                                        tvQualification.setText(strQualification);
                                    }
                                    if(!jObj.getString("profession").equalsIgnoreCase("null")) {
                                        String strBusiness="";
                                        if(jObj.getString("profession").equalsIgnoreCase("1")
                                                ||jObj.getString("profession").equalsIgnoreCase("Doctor")){
                                            strBusiness="Doctor";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("2")
                                                ||jObj.getString("profession").equalsIgnoreCase("Lawyer")){
                                            strBusiness="Lawyer";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("3")
                                                ||jObj.getString("profession").equalsIgnoreCase("Architects")){
                                            strBusiness="Architects";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("4")
                                                ||jObj.getString("profession").equalsIgnoreCase("CA/CS/ICWA")){
                                            strBusiness="CA/CS/ICWA";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("5")
                                                ||jObj.getString("profession").equalsIgnoreCase("MBA")){
                                            strBusiness="MBA";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("6")
                                                ||jObj.getString("profession").equalsIgnoreCase("Teacher/Professor")){
                                            strBusiness="Teacher/Professor";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("7")
                                                ||jObj.getString("profession").equalsIgnoreCase("Interior Designer")){
                                            strBusiness="Interior Designer";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("8")
                                                ||jObj.getString("profession").equalsIgnoreCase("Fashion Designer")){
                                            strBusiness="Fashion Designer";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("9")
                                                ||jObj.getString("profession").equalsIgnoreCase("Engineer")){
                                            strBusiness="Engineer";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("10")
                                                ||jObj.getString("profession").equalsIgnoreCase("Civil engineer")){
                                            strBusiness="Civil engineer";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("11")
                                                ||jObj.getString("profession").equalsIgnoreCase("Business")){
                                            strBusiness="Business";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("12")
                                                ||jObj.getString("profession").equalsIgnoreCase("Service")){
                                            strBusiness="Service";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("13")
                                                ||jObj.getString("profession").equalsIgnoreCase("Housewife")){
                                            strBusiness="Housewife";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("14")
                                                ||jObj.getString("profession").equalsIgnoreCase("Retired")){
                                            strBusiness="Retired";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("15")
                                                ||jObj.getString("profession").equalsIgnoreCase("Other")){
                                            strBusiness="other";
                                        }
                                        tvProfession.setText(strBusiness);
                                    }
                                    if(!jObj.getString("flatNoBuilding").equalsIgnoreCase("null")) {
                                        tvFlatNo.setText(jObj.getString("flatNoBuilding"));
                                    }
                                    if(!jObj.getString("residenceArea").equalsIgnoreCase("null")) {
                                        tvResidenceArea.setText(jObj.getString("residenceArea"));
                                    }
                                    if(!jObj.getString("nearestStation").equalsIgnoreCase("null")) {
                                        tvNearestStn.setText(jObj.getString("nearestStation"));
                                    }
                                    if(!jObj.getString("city").equalsIgnoreCase("null")) {
                                        tvCity.setText(jObj.getString("city"));
                                    }
                                    if(!jObj.getString("pincode").equalsIgnoreCase("null")) {
                                        tvPicode.setText(jObj.getString("pincode"));
                                    }
                                    if(!jObj.getString("khambhatAddress").equalsIgnoreCase("null")) {
                                        tvKhambat.setText(jObj.getString("khambhatAddress"));
                                    }
                                    if(!jObj.getString("mobileNo").equalsIgnoreCase("null")) {
                                        tvMobile.setText(jObj.getString("mobileNo"));
                                    }
                                    if(!jObj.getString("email").equalsIgnoreCase("null")) {
                                        tvEmail.setText(jObj.getString("email"));
                                    }
                                    if(!jObj.getString("membershipStatus").equalsIgnoreCase("null")) {
                                        String strMembershipStatus="";
                                        if(jObj.getString("membershipStatus").equalsIgnoreCase("1")
                                                ||jObj.getString("membershipStatus").equalsIgnoreCase("Active")){
                                            strMembershipStatus="Active";
                                        }else if(jObj.getString("membershipStatus").equalsIgnoreCase("0")
                                                ||jObj.getString("membershipStatus").equalsIgnoreCase("2")
                                                ||jObj.getString("membershipStatus").equalsIgnoreCase("InActive")
                                                ||jObj.getString("membershipStatus").equalsIgnoreCase("In Active")
                                                ||jObj.getString("membershipStatus").equalsIgnoreCase("In-Active")){
                                            strMembershipStatus="In-Active";
                                        }
                                        tvMembershipStatus.setText(strMembershipStatus);
                                    }
                                    JSONArray jBro = jObj.getJSONArray("brotherInfo");
                                    String strBrotherList="";
                                    for(int i=0;i<jBro.length();i++){
                                        if(strBrotherList.equalsIgnoreCase(""))
                                            strBrotherList=jBro.getJSONObject(i).getString("name") +
                                                    " - " + jBro.getJSONObject(i).getString("brother_id");
                                        else
                                            strBrotherList += " , "+jBro.getJSONObject(i).getString("name") +
                                                    " - " + jBro.getJSONObject(i).getString("brother_id");
                                    }
                                    tvBrotherlist.setText(strBrotherList);
                                } else {
                                    //ProgressDialogView.hideProgressDialog();
                                    DialogUtil.showSnackBarWithAction(relativeLayout, otpResponse.getMessage());
                                }
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

    public void getParentData(String id) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, GET_PARENTDATA_URL+"/"+id,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject json) {
                        try {
                            Log.d("TAG", "JSON POST Response = " + json.toString());

                            if (json.toString().contains("data")) {
                                getMemberProfile(json.getJSONObject("data").getString("member_id"),
                                        json.getJSONObject("data").getString("mobile_no"));
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

}