package com.khambhatpragati.adapter;

/**
 * Created by bhagvatee1.gupta on 03-03-2017.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
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
import com.khambhatpragati.activity.ProfileActivity;
import com.khambhatpragati.helper.CustomVolleyRequest;
import com.khambhatpragati.listeners.ItemClickListener;
import com.khambhatpragati.model.BrotherModel;
import com.khambhatpragati.model.response.OTPVerificationResponse;
import com.khambhatpragati.model.response.UserDirectoryBean;
import com.khambhatpragati.parser.JSONParser;
import com.khambhatpragati.preference.UserPreference;
import com.khambhatpragati.utils.DialogUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.khambhatpragati.constants.URLs.DELETEBROTHER_URL;
import static com.khambhatpragati.constants.URLs.GET_MEMBERPROFILE_URL;

public class BrotherAdapter extends RecyclerView.Adapter<BrotherAdapter.ViewHolder> {

    private static final String TAG = BrotherAdapter.class.getSimpleName();
    private Context mContext;
    private String groupId;
    private ArrayList<BrotherModel> listOfUsers;
    private ArrayList<BrotherModel> filterList;
    private ItemClickListener clickListener;
    Context context;

    public BrotherAdapter(ArrayList<BrotherModel> listOfUsers,Context mContext) {
        this.mContext = mContext;
        this.groupId = groupId;
        this.listOfUsers = listOfUsers;
        this.filterList = new ArrayList<BrotherModel>();
        // we copy the original list to the filter list and use it for setting row values
        this.filterList.addAll(this.listOfUsers);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_brother, parent, false);
        context = parent.getContext();
        return new ViewHolder(itemView);
    }

    @RequiresApi(api =Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final BrotherModel BrotherBean = filterList.get(position);
        holder.tvUserName.setText(BrotherBean.getName());
        holder.tvMobileNo.setText(BrotherBean.getBrother_id());


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES. KITKAT ) {
            if(position %4==0) {
                holder.viewColor.setBackgroundColor(context.getColor(R.color.colorAccent));
            } else if(position %4==1) {
                holder.viewColor.setBackgroundColor(context.getColor(R.color.colorRed));
            } else if(position %4==2) {
                holder.viewColor.setBackgroundColor(context.getColor(R.color.colorGreen));
            } else if(position %4==3) {
                holder.viewColor.setBackgroundColor(context.getColor(R.color.color_orange));
            }
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBrother(v.getContext(),holder.getAdapterPosition(),holder,BrotherBean);

            }
        });
    }
    public void deleteBrother(final Context ctx,final int pos,final ViewHolder holder,BrotherModel model) {
        final ProgressDialog progress = new ProgressDialog(ctx);
        progress.setMessage("Please Wait....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

        Map<String, Object> params = new HashMap<>();
        params.put("mobile_no", model.getMobile_no());

        Log.d("Params", "Testing" + params);

        RequestQueue queue = Volley.newRequestQueue(ctx);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, DELETEBROTHER_URL,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject json) {
                        try {
                            Log.d("TAG", "JSON POST Response = " + json.toString());

                            if (!json.getBoolean("error")) {
                                filterList.remove(holder.getAdapterPosition());
                                notifyDataSetChanged();

                                ((Activity)context).finish();
                                Intent intent1=new Intent(ctx,ProfileActivity.class);
                                context.startActivity(intent1);
                            }else{                                //ProgressDialogView.hideProgressDialog();
                                DialogUtil.alertWithOkButton(ctx, "No data from server in DeleteBrother API");
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
                        DialogUtil.alertWithOkButton(ctx, error.getMessage());
                        progress.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            DialogUtil.alertWithOkButton(ctx, "Network timeout error");
                        } else if (error instanceof AuthFailureError) {
                            DialogUtil.alertWithOkButton(ctx, "There was an authentication failure while performing a Request.");
                        } else if (error instanceof ServerError) {
                            DialogUtil.alertWithOkButton(ctx, "ServerError");
                        } else if (error instanceof NetworkError) {
                            DialogUtil.alertWithOkButton(ctx, "NetworkError");
                        } else if (error instanceof ParseError) {
                            DialogUtil.alertWithOkButton(ctx, "ParseError, Server's response could not be parsed.");
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
    public int getItemCount() {
        //return listOfUsers.size();
        return (null != filterList ? filterList.size() : 0);
    }

    // Do Search...
    public void filter(final String text) {
        // Searching could be complex..so we will dispatch it to a different thread...
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Clear the filter list
                filterList.clear();
                // If there is no search value, then ic_add all original list items to filter list
                if (TextUtils.isEmpty(text)) {
                    filterList.addAll(listOfUsers);
                } else {
                    // Iterate in the original List and ic_add it to filter list...
                    for (BrotherModel item : listOfUsers) {
                        if (item.name.toLowerCase().contains(text.toLowerCase())) {
                            // Adding Matched items
                            filterList.add(item);
                        }
                    }
                }

                // Set on UI Thread
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Notify the List that the DataSet has changed...
                        notifyDataSetChanged();
                    }
                });

            }
        }).start();

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvUserName;
        private TextView tvMobileNo;
        private ImageView delete;
        private String mobileNo;
        private LinearLayout viewColor;

        public ViewHolder(View view) {
            super(view);
            tvUserName = (TextView) view.findViewById(R.id.tvUserName);
            tvMobileNo = (TextView) view.findViewById(R.id.tvMobileNo);
            delete = (ImageView) view.findViewById(R.id.delete);
            viewColor = (LinearLayout) view.findViewById(R.id.viewColor);
            view.setOnClickListener(this);
        }

        public void setItem(String item) {
            mobileNo = item;
        }

        @Override
        public void onClick(View view) {
            //Log.d(TAG, "onClick " + getPosition() + " mobileNo: " + mobileNo);
            if (clickListener != null)
                clickListener.onClick(view, getAdapterPosition(), mobileNo);
        }
    }
}