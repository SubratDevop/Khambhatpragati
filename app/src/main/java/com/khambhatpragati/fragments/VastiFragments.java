package com.khambhatpragati.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
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
import com.khambhatpragati.R;
import com.khambhatpragati.activity.DependentListActivity;
import com.khambhatpragati.adapter.DependentListAdapter;
import com.khambhatpragati.adapter.VastiBrotherAdapter;
import com.khambhatpragati.adapter.VastiDependentAdapter;
import com.khambhatpragati.adapter.VastiMarriedSonAdapter;
import com.khambhatpragati.helper.CustomVolleyRequest;
import com.khambhatpragati.model.BrotherModel;
import com.khambhatpragati.model.DependentModel;
import com.khambhatpragati.model.MarriedSonModel;
import com.khambhatpragati.model.VastiModel;
import com.khambhatpragati.model.response.OTPVerificationResponse;
import com.khambhatpragati.parser.JSONParser;
import com.khambhatpragati.preference.UserPreference;
import com.khambhatpragati.utils.DialogUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.khambhatpragati.constants.URLs.GET_BROTHERDATA_URL;
import static com.khambhatpragati.constants.URLs.GET_DEPENDENTLIST_URL;
import static com.khambhatpragati.constants.URLs.GET_MARRIEDSONDATA_URL;
import static com.khambhatpragati.constants.URLs.GET_MEMBERPROFILE_URL;

public class VastiFragments extends Fragment {

    Context context;
    private RecyclerView recyclerView;
    private VastiDependentAdapter mDependentAdapter;
    private VastiBrotherAdapter mBrotherAdapter;
    private VastiMarriedSonAdapter mMarriedSonAdapter;
    private ArrayList<DependentModel> dependentList = new ArrayList<DependentModel>();
    private ArrayList<MarriedSonModel> mSonList = new ArrayList<MarriedSonModel>();
    private ArrayList<BrotherModel> brotherList = new ArrayList<BrotherModel>();

    LinearLayout relativeLayout;
    LinearLayout profileLV;
    VastiModel vastiModel;
    String titleText="";

    public VastiFragments(VastiModel v,String s) {
        this.vastiModel = v;
        this.titleText=s;
    }
    public static VastiFragments newInstance(VastiModel v,String s) {
        VastiFragments fragment = new VastiFragments(v,s);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.vasti_fragment, container, false);
        context = getActivity();

        profileLV = view.findViewById(R.id.recyclerLV);
        relativeLayout = view.findViewById(R.id.relativeLayout);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        initializeAdapterDependent();
        initializeAdapterBrother();
        initializeAdapterMarriedSons();

        if(titleText.equalsIgnoreCase("FAMILY")) {
            getDependentList();
        }else if(titleText.equalsIgnoreCase("SONS")) {
            getMarriedSonList();
        }else if(titleText.equalsIgnoreCase("BROTHER")) {
            getBrotherList();
        }
        return view;
    }
    private void initializeAdapterDependent(){
        mDependentAdapter = new VastiDependentAdapter(dependentList, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mDependentAdapter);
    }
    private void initializeAdapterBrother(){
        mBrotherAdapter = new VastiBrotherAdapter(brotherList, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mBrotherAdapter);
    }
    private void initializeAdapterMarriedSons(){
        mMarriedSonAdapter = new VastiMarriedSonAdapter(mSonList, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mMarriedSonAdapter);
    }

    private void getDependentList() {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

        Map<String, Object> params = new HashMap<>();
        params.put("mobile_no", vastiModel.getMobile_no());

        Log.d("Params", "Testing" + params);

        RequestQueue queue = Volley.newRequestQueue(context);
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
                                mDependentAdapter.notifyDataSetChanged();
                                recyclerView.getAdapter().notifyDataSetChanged();
                                initializeAdapterDependent();
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

    private void getMarriedSonList() {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, GET_MARRIEDSONDATA_URL+"/"+vastiModel.getMember_id(),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject json) {
                        try {
                            Log.d("TAG", "JSON POST Response = " + json.toString());
                            if (json.toString().contains("data")) {
                                mSonList.clear();
                                for(int i=0;i<json.getJSONArray("data").length();i++){
                                    MarriedSonModel d = new MarriedSonModel();
                                    d.setRecord_no(json.getJSONArray("data").getJSONObject(i).getString("record_no"));
                                    d.setMember_id(json.getJSONArray("data").getJSONObject(i).getString("member_id"));
                                    d.setFirst_name(json.getJSONArray("data").getJSONObject(i).getString("first_name"));
                                    d.setMiddle_name(json.getJSONArray("data").getJSONObject(i).getString("middle_name"));
                                    d.setLast_name(json.getJSONArray("data").getJSONObject(i).getString("last_name"));
                                    d.setMobile_no(json.getJSONArray("data").getJSONObject(i).getString("mobile_no"));
                                    d.setEmail(json.getJSONArray("data").getJSONObject(i).getString("email"));
                                    d.setMarritalStatus(json.getJSONArray("data").getJSONObject(i).getString("marital_status"));
                                    d.setPhoto_link(json.getJSONArray("data").getJSONObject(i).getString("photo_link"));

                                    mSonList.add(d);
                                }
                                mMarriedSonAdapter.notifyDataSetChanged();
                                recyclerView.getAdapter().notifyDataSetChanged();
                                initializeAdapterMarriedSons();
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
    private void getBrotherList() {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, GET_BROTHERDATA_URL+"/"+vastiModel.getMember_id(),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject json) {
                        try {
                            Log.d("TAG", "JSON POST Response = " + json.toString());
                            if (json.toString().contains("data")) {
                                brotherList.clear();
                                for(int i=0;i<json.getJSONArray("data").length();i++){
                                    BrotherModel d = new BrotherModel();
                                    d.setRecord_no(json.getJSONArray("data").getJSONObject(i).getString("record_no"));
                                    d.setParent_member_id(json.getJSONArray("data").getJSONObject(i).getString("parent_member_id"));
                                    d.setName(json.getJSONArray("data").getJSONObject(i).getString("name"));
                                    d.setMobile_no(json.getJSONArray("data").getJSONObject(i).getString("mobile_no"));
                                    d.setDate_of_birth(json.getJSONArray("data").getJSONObject(i).getString("date_of_birth"));
                                    d.setBrother_id(json.getJSONArray("data").getJSONObject(i).getString("brother_id"));

                                    brotherList.add(d);
                                }
                                mBrotherAdapter.notifyDataSetChanged();
                                recyclerView.getAdapter().notifyDataSetChanged();
                                initializeAdapterBrother();
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