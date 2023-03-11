package com.khambhatpragati.adapter;

/**
 * Created by bhagvatee1.gupta on 03-03-2017.
 */

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.khambhatpragati.R;
import com.khambhatpragati.listeners.GroupsItemClickListener;
import com.khambhatpragati.model.response.GroupsBean;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupDetailsAdapter extends RecyclerView.Adapter<GroupDetailsAdapter.ViewHolder> {

    private static final String TAG = GroupDetailsAdapter.class.getName();

    private ArrayList<GroupsBean> listOfGroups;
    private HashMap<String, Integer> unReadMessagesCounter;
    private GroupsItemClickListener clickListener;
    Context context;

    public GroupDetailsAdapter(ArrayList<GroupsBean> listOfGroups, HashMap<String, Integer> unReadMessagesCounter) {
        this.listOfGroups = listOfGroups;
        this.unReadMessagesCounter = unReadMessagesCounter;
    }

    public void setUnReadCounter(HashMap<String, Integer> unReadMessagesCounter) {
        this.unReadMessagesCounter = unReadMessagesCounter;
        this.notifyItemRangeChanged(0, this.unReadMessagesCounter.size());
    }

    public void setClickListener(GroupsItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_groups, parent, false);
        context = parent.getContext();
        return new ViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GroupsBean groupsBean = listOfGroups.get(position);
        holder.tvGroupName.setText(groupsBean.getMandalName());
        if (unReadMessagesCounter != null && unReadMessagesCounter.size() > 0) {
            Integer unReadCount = unReadMessagesCounter.get(groupsBean.getMandalID());
            if (unReadCount > 0) {
                holder.badgeUnreadMsgCount.setVisibility(View.VISIBLE);
                holder.badgeUnreadMsgCount.setText(" "+unReadCount.toString()+" ");
            } else {
                holder.badgeUnreadMsgCount.setVisibility(View.INVISIBLE);
                holder.badgeUnreadMsgCount.setText("");
            }
        }
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
        holder.setItem(groupsBean);
    }

    @Override
    public int getItemCount() {
        return listOfGroups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvGroupName;
        private LinearLayout viewColor;
        private TextView badgeUnreadMsgCount;
        //private Drawable badgeBackground;
        private GroupsBean groupsBean;

        public ViewHolder(View view) {
            super(view);
            tvGroupName = (TextView) view.findViewById(R.id.tvGroupName);
            viewColor = (LinearLayout) view.findViewById(R.id.viewColor);
            badgeUnreadMsgCount = (TextView) view.findViewById(R.id.badgeUnreadMsgCount);
            //badgeBackground = badgeUnreadMsgCount.getBackground();
            view.setOnClickListener(this);
        }

        public void setItem(GroupsBean groupsBeanItem) {
            groupsBean = groupsBeanItem;
        }

        @Override
        public void onClick(View view) {
            //Log.d(TAG, "onClick " + getPosition() + " groupsBean: " + groupsBean.getMandalName());
            if (clickListener != null)
                clickListener.onClick(view, getAdapterPosition(), groupsBean);
        }
    }
}