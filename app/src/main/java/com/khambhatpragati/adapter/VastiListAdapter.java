package com.khambhatpragati.adapter;

/**
 * Created by bhagvatee1.gupta on 03-03-2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.khambhatpragati.R;
import com.khambhatpragati.activity.DependentDetailActivity;
import com.khambhatpragati.activity.ProfileActivity;
import com.khambhatpragati.activity.VastiDetailActivity;
import com.khambhatpragati.helper.CustomVolleyRequest;
import com.khambhatpragati.listeners.GroupsItemClickListener;
import com.khambhatpragati.listeners.ItemClickListener;
import com.khambhatpragati.listeners.VastiItemClickListener;
import com.khambhatpragati.model.VastiModel;
import com.khambhatpragati.model.response.GroupsBean;
import com.khambhatpragati.model.response.UserDirectoryBean;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class VastiListAdapter extends RecyclerView.Adapter<VastiListAdapter.ViewHolder> {

    private static final String TAG = VastiListAdapter.class.getName();

    private ArrayList<VastiModel> filterList;
    private ArrayList<VastiModel> listOfGroups;
    //private HashMap<String, Integer> unReadMessagesCounter;
    private VastiItemClickListener clickListener;
    Context context;

    public VastiListAdapter(ArrayList<VastiModel> listOfGroups, Context ctx) {
        this.listOfGroups = listOfGroups;
        //this.unReadMessagesCounter = unReadMessagesCounter;

        this.filterList = new ArrayList<VastiModel>();
        // we copy the original list to the filter list and use it for setting row values
        this.filterList.addAll(this.listOfGroups);
        this.context = ctx;
    }

    /*public void setUnReadCounter(HashMap<String, Integer> unReadMessagesCounter) {
        this.unReadMessagesCounter = unReadMessagesCounter;
        this.notifyItemRangeChanged(0, this.unReadMessagesCounter.size());
    }*/

    public void setClickListener(VastiItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_vastilist, parent, false);
        context = parent.getContext();
        return new ViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final VastiModel groupsBean = filterList.get(position);

        if(groupsBean.getMiddle_name()!=null) {
            if (groupsBean.getMiddle_name().equalsIgnoreCase("") ||
                    groupsBean.getMiddle_name().equalsIgnoreCase("null")) {
                holder.tvGroupName.setText(groupsBean.getFirst_name() + " " + groupsBean.getLast_name());
            } else {
                if(groupsBean.getLast_name()!=null) {
                    holder.tvGroupName.setText(groupsBean.getFirst_name() + " " + groupsBean.getMiddle_name()
                            + " " + groupsBean.getLast_name());
                }else{
                    holder.tvGroupName.setText(groupsBean.getFirst_name() + " " + groupsBean.getMiddle_name());
                }
            }
        }else{
            holder.tvGroupName.setText(groupsBean.getFirst_name());
        }
        holder.tvGroupName.setText(groupsBean.getFirst_name() + " " + groupsBean.getMiddle_name() + " " + groupsBean.getLast_name());

        if(!groupsBean.getDependent_count().equalsIgnoreCase("0")) {
            holder.badgeUnreadMsgCount.setVisibility(View.VISIBLE);
            holder.badgeUnreadMsgCount.setText(groupsBean.getDependent_count());
        }else{
            holder.badgeUnreadMsgCount.setVisibility(View.GONE);
            holder.badgeUnreadMsgCount.setText(groupsBean.getDependent_count());
        }

        ImageLoader imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        if(!groupsBean.getPhoto_link().equalsIgnoreCase("NULL")
                && groupsBean.getPhoto_link().startsWith("http"))
        imageLoader.get(groupsBean.getPhoto_link(),
                ImageLoader.getImageListener(holder.profile, R.mipmap.noimage, R.mipmap.profile));

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
        holder.viewColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), VastiDetailActivity.class);
                i.putExtra("vasti_detail",  groupsBean);
                v.getContext().startActivity(i);
            }
        });

        holder.setItem(groupsBean);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return (null != filterList ? filterList.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvGroupName;
        private LinearLayout viewColor;
        private TextView badgeUnreadMsgCount;
        //private Drawable badgeBackground;
        private VastiModel groupsBean;
        CircleImageView profile;

        public ViewHolder(View view) {
            super(view);
            tvGroupName = (TextView) view.findViewById(R.id.tvGroupName);
            viewColor = (LinearLayout) view.findViewById(R.id.viewColor);
            badgeUnreadMsgCount = (TextView) view.findViewById(R.id.badgeUnreadMsgCount);
            profile = (CircleImageView) view.findViewById(R.id.imgProfile);
            //badgeBackground = badgeUnreadMsgCount.getBackground();
            view.setOnClickListener(this);
            setIsRecyclable(false);
        }

        public void setItem(VastiModel groupsBeanItem) {
            groupsBean = groupsBeanItem;
        }

        @Override
        public void onClick(View view) {
            //Log.d(TAG, "onClick " + getPosition() + " groupsBean: " + groupsBean.getMandalName());
            if (clickListener != null)
                clickListener.onClick(view, getAdapterPosition(), groupsBean);
        }
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
                    filterList.addAll(listOfGroups);
                } else {
                    // Iterate in the original List and ic_add it to filter list...
                    for (VastiModel item : listOfGroups) {
                        String strItem="";
                        if(item.getMiddle_name()!=null) {
                            if (item.getMiddle_name().equalsIgnoreCase("") ||
                                    item.getMiddle_name().equalsIgnoreCase("null")) {
                                strItem = item.getFirst_name() + " " + item.getLast_name();
                            } else {
                                if(item.getLast_name()!=null) {
                                    strItem = item.getFirst_name() + " " + item.getMiddle_name()
                                            + " " + item.getLast_name();
                                }else{
                                    strItem = item.getFirst_name() + " " + item.getMiddle_name();
                                }
                            }
                        }else{
                            strItem = item.getFirst_name();
                        }
                        if (strItem.toLowerCase().contains(text.toLowerCase())
                            || item.getMobile_no().contains(text.toLowerCase())) {
                            // Adding Matched items
                            filterList.add(item);
                        }
                    }
                }

                // Set on UI Thread
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Notify the List that the DataSet has changed...
                        notifyDataSetChanged();
                    }
                });

            }
        }).start();

    }

}