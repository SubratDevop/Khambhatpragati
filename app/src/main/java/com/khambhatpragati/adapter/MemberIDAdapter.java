package com.khambhatpragati.adapter;

/**
 * Created by bhagvatee1.gupta on 03-03-2017.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.khambhatpragati.R;
import com.khambhatpragati.listeners.ItemClickListener;
import com.khambhatpragati.listeners.MemberIDClickListener;
import com.khambhatpragati.model.response.UserDirectoryBean;
import com.khambhatpragati.preference.UserPreference;

import java.util.ArrayList;

public class MemberIDAdapter extends RecyclerView.Adapter<MemberIDAdapter.ViewHolder> {

    private static final String TAG = MemberIDAdapter.class.getSimpleName();
    private Context mContext;
    private String groupId;
    private ArrayList<UserDirectoryBean> listOfUsers;
    private ArrayList<UserDirectoryBean> filterList;
    private MemberIDClickListener clickListener;
    Context context;

    public MemberIDAdapter(Context mContext, ArrayList<UserDirectoryBean> listOfUsers, String groupId) {
        this.mContext = mContext;
        this.groupId = groupId;
        this.listOfUsers = listOfUsers;
        this.filterList = new ArrayList<UserDirectoryBean>();
        // we copy the original list to the filter list and use it for setting row values
        this.filterList.addAll(this.listOfUsers);
    }

    public void setClickListener(MemberIDClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_member_id, parent, false);
        context = parent.getContext();
        return new ViewHolder(itemView);
    }

    @RequiresApi(api =Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //UserDirectoryBean userDirectoryBean = listOfUsers.get(position);
        UserDirectoryBean userDirectoryBean = filterList.get(position);
        holder.tvUserName.setText(userDirectoryBean.getUserName());
        holder.tvMobileNo.setText("+91 " + userDirectoryBean.getMobileNo());
        holder.tvUserType.setText(userDirectoryBean.getMemberId());

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
        holder.setItem(userDirectoryBean.getMobileNo(),userDirectoryBean.getUserName(),userDirectoryBean.getMemberId());
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
                    for (UserDirectoryBean item : listOfUsers) {
                        if (item.getUserName().toLowerCase().contains(text.toLowerCase())) {
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
        private TextView tvUserType;
        private String mobileNo="",userName="",userID="";
        private LinearLayout viewColor;

        public ViewHolder(View view) {
            super(view);
            tvUserName = (TextView) view.findViewById(R.id.tvUserName);
            tvMobileNo = (TextView) view.findViewById(R.id.tvMobileNo);
            tvUserType = (TextView) view.findViewById(R.id.tvUserType);
            viewColor = (LinearLayout) view.findViewById(R.id.viewColor);
            view.setOnClickListener(this);
        }

        public void setItem(String item,String name,String id) {
            mobileNo = item;
            userName = name;
            userID = id;
        }

        @Override
        public void onClick(View view) {
            //Log.d(TAG, "onClick " + getPosition() + " mobileNo: " + mobileNo);
            if (clickListener != null)
                clickListener.onClick(view, getAdapterPosition(), mobileNo,userName,userID);
        }
    }
}