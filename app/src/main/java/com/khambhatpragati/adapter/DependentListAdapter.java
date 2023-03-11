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
import androidx.cardview.widget.CardView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.khambhatpragati.R;
import com.khambhatpragati.activity.DependentDetailActivity;
import com.khambhatpragati.activity.DependentListActivity;
import com.khambhatpragati.activity.ProfileActivity;
import com.khambhatpragati.listeners.ItemClickListener;
import com.khambhatpragati.model.BrotherModel;
import com.khambhatpragati.model.DependentModel;
import com.khambhatpragati.utils.DialogUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.khambhatpragati.constants.URLs.DELETEBROTHER_URL;

public class DependentListAdapter extends RecyclerView.Adapter<DependentListAdapter.ViewHolder> {

    private static final String TAG = DependentListAdapter.class.getSimpleName();
    private Context mContext;
    private String groupId;
    private ArrayList<DependentModel> listOfUsers;
    private ArrayList<DependentModel> filterList;
    private ItemClickListener clickListener;
    Context context;

    public DependentListAdapter(ArrayList<DependentModel> listOfUsers, Context mContext) {
        this.mContext = mContext;
        this.groupId = groupId;
        this.listOfUsers = listOfUsers;
        this.filterList = new ArrayList<DependentModel>();
        // we copy the original list to the filter list and use it for setting row values
        this.filterList.addAll(this.listOfUsers);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dependentlist, parent, false);
        context = parent.getContext();
        return new ViewHolder(itemView);
    }

    @RequiresApi(api =Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final DependentModel BrotherBean = filterList.get(position);
        holder.tvUserName.setText(BrotherBean.getFirst_name());
        if(BrotherBean.getRelation_with_member().equalsIgnoreCase("1"))
            holder.tvMobileNo.setText("Son");
        else if(BrotherBean.getRelation_with_member().equalsIgnoreCase("2"))
            holder.tvMobileNo.setText("Daughter");
        else if(BrotherBean.getRelation_with_member().equalsIgnoreCase("3"))
            holder.tvMobileNo.setText("Wife");
        else if(BrotherBean.getRelation_with_member().equalsIgnoreCase("4"))
            holder.tvMobileNo.setText("Brother");
        else if(BrotherBean.getRelation_with_member().equalsIgnoreCase("5"))
            holder.tvMobileNo.setText("Sister");
        else
            holder.tvMobileNo.setText(BrotherBean.getRelation_with_member());

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES. KITKAT ) {
            if(position %4==0) {
                holder.viewColor.setBackgroundColor(context.getColor(R.color.colorAccent));
            } else if(position %4==1) {
                holder.viewColor.setBackgroundColor(context.getColor(R.color.colorRed));
            }  else if(position %4==2) {
                holder.viewColor.setBackgroundColor(context.getColor(R.color.colorGreen));
            } else if(position %4==3) {
                holder.viewColor.setBackgroundColor(context.getColor(R.color.color_orange));
            }
        }
        holder.viewColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), DependentDetailActivity.class);
                i.putExtra("dependent_detail",BrotherBean);
                i.putExtra("From","View");
                v.getContext().startActivity(i);
            }
        });
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
                    for (DependentModel item : listOfUsers) {
                        if (item.first_name.toLowerCase().contains(text.toLowerCase())) {
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
        private String mobileNo;
        private LinearLayout viewColor;

        public ViewHolder(View view) {
            super(view);
            tvUserName = (TextView) view.findViewById(R.id.tvUserName);
            tvMobileNo = (TextView) view.findViewById(R.id.tvMobileNo);
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