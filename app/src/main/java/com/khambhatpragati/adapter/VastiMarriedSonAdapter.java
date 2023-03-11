package com.khambhatpragati.adapter;

/**
 * Created by bhagvatee1.gupta on 03-03-2017.
 */

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.khambhatpragati.R;
import com.khambhatpragati.helper.CustomVolleyRequest;
import com.khambhatpragati.listeners.ItemClickListener;
import com.khambhatpragati.model.DependentModel;
import com.khambhatpragati.model.MarriedSonModel;

import java.util.ArrayList;

public class VastiMarriedSonAdapter extends RecyclerView.Adapter<VastiMarriedSonAdapter.ViewHolder> {

    private static final String TAG = VastiMarriedSonAdapter.class.getSimpleName();
    private Context mContext;
    private String groupId;
    private ArrayList<MarriedSonModel> listOfUsers;
    private ArrayList<MarriedSonModel> filterList;
    private ItemClickListener clickListener;
    Context context;
    int currentPosition = -1;

    public VastiMarriedSonAdapter(ArrayList<MarriedSonModel> listOfUsers, Context mContext) {
        this.mContext = mContext;
        this.groupId = groupId;
        this.listOfUsers = listOfUsers;
        this.filterList = new ArrayList<MarriedSonModel>();
        // we copy the original list to the filter list and use it for setting row values
        this.filterList.addAll(this.listOfUsers);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_vastimarriedson, parent, false);
        context = parent.getContext();
        return new ViewHolder(itemView);
    }

    @RequiresApi(api =Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MarriedSonModel BrotherBean = filterList.get(position);
        holder.tvUserName.setText(BrotherBean.getFirst_name());
        if(BrotherBean.getMarritalStatus().equalsIgnoreCase("1"))
            holder.tvMobileNo.setText("Married");
        if(BrotherBean.getMarritalStatus().equalsIgnoreCase("0")
            || BrotherBean.getMarritalStatus().equalsIgnoreCase("2"))
            holder.tvMobileNo.setText("UnMarried");

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
        //if the position is equals to the item position which is to be expanded
        if (currentPosition == position) {
            //creating an animation
            Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);

            //toggling visibility
            holder.viewLV.setVisibility(View.VISIBLE);
            //adding sliding effect
            holder.viewLV.startAnimation(slideDown);

            ImageLoader imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
            if(BrotherBean.getPhoto_link().trim().equalsIgnoreCase("NULL")) {
            ;
            } else{
                if(BrotherBean.getPhoto_link().trim().startsWith("http://"))
                    imageLoader.get(BrotherBean.getPhoto_link(), ImageLoader.getImageListener(holder.imgProfile, R.mipmap.noimage, R.mipmap.profile));
                else
                    imageLoader.get("http://khambhatpragati.com/uploads/member_images/" + BrotherBean.getPhoto_link(), ImageLoader.getImageListener(holder.imgProfile, R.mipmap.noimage, R.mipmap.profile));
            }

            if (!BrotherBean.getMember_id().equalsIgnoreCase("null")) {
                holder.tvMemno.setText(BrotherBean.getMember_id());
            }
            if (!BrotherBean.getFirst_name().equalsIgnoreCase("null")) {
                holder.tvFirstName.setText(BrotherBean.getFirst_name());
            }
            if (!BrotherBean.getMiddle_name().equalsIgnoreCase("null")) {
                holder.tvMiddleName.setText(BrotherBean.getMiddle_name());
            }
            if (!BrotherBean.getMobile_no().equalsIgnoreCase("null")) {
                holder.tvMobile.setText(BrotherBean.getMobile_no());
            }
            if (!BrotherBean.getEmail().equalsIgnoreCase("null")) {
                holder.tvEmail.setText(BrotherBean.getEmail());
            }
            if (!BrotherBean.getLast_name().equalsIgnoreCase("null")) {
                holder.tvLastName.setText(BrotherBean.getLast_name());
            }
        }
        holder.viewColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.viewLV.getVisibility()==View.VISIBLE){
                    holder.viewLV.setVisibility(View.GONE);
                    //creating an animation
                    Animation slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
                    //adding sliding effect
                    holder.viewLV.startAnimation(slideUp);
                }else {
                    currentPosition = position;
                    notifyDataSetChanged();
                }

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
                    for (MarriedSonModel item : listOfUsers) {
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
        private LinearLayout viewLV;
        ImageView imgProfile;
        TextView tvMemno,tvFirstName,tvMiddleName,tvLastName,tvMobile,tvEmail;

        public ViewHolder(View view) {
            super(view);
            tvUserName = (TextView) view.findViewById(R.id.tvUserName);
            tvMobileNo = (TextView) view.findViewById(R.id.tvMobileNo);
            viewColor = (LinearLayout) view.findViewById(R.id.viewColor);
            viewLV = (LinearLayout) view.findViewById(R.id.viewLV);
            imgProfile = (ImageView) view.findViewById(R.id.imgProfile);
            tvMemno = (TextView) view.findViewById(R.id.tvMemNo);
            tvFirstName = (TextView) view.findViewById(R.id.tvFirstName);
            tvMobile = (TextView) view.findViewById(R.id.tvMobile);
            tvEmail = (TextView) view.findViewById(R.id.tvEmail);
            tvMiddleName = (TextView) view.findViewById(R.id.tvMiddleName);
            tvLastName = (TextView) view.findViewById(R.id.tvLastName);

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