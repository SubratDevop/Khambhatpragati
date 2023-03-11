 package com.khambhatpragati.adapter;

/**
 * Created by bhagvatee1.gupta on 03-03-2017.
 */

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.khambhatpragati.R;
import com.khambhatpragati.listeners.MessagesItemClickListener;
import com.khambhatpragati.model.response.MessagesBean;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    private static final String TAG = MessagesAdapter.class.getName();

    private ArrayList<MessagesBean> listOfMessages;
    private MessagesItemClickListener clickListener;
    Context context;

    public MessagesAdapter(ArrayList<MessagesBean> listOfMessages) {
        this.listOfMessages = listOfMessages;
    }

    public void setClickListener(MessagesItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_messages, parent, false);
        context = parent.getContext();
        return new ViewHolder(itemView);
    }

    @RequiresApi (api =Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MessagesBean allMessagesBean = listOfMessages.get(position);
        holder.tvMessage.setText(allMessagesBean.getTitle());

        holder.tvDateTime.setText(allMessagesBean.getModifiedDateTime());


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
        holder.setItem(allMessagesBean);
    }



    @Override
    public int getItemCount() {
        return listOfMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvMessage;
        private TextView tvDateTime;
        private MessagesBean messagesBean;
        private LinearLayout viewColor;

        public ViewHolder(View view) {
            super(view);

            viewColor = (LinearLayout) view.findViewById(R.id.viewColor);
            tvMessage = (TextView) view.findViewById(R.id.tvMessage);
            tvDateTime = (TextView) view.findViewById(R.id.tvDateTime);
            view.setOnClickListener(this);
        }

        public void setItem(MessagesBean messagesBeanItem) {
            messagesBean = messagesBeanItem;
        }

        @Override
        public void onClick(View view) {
            //Log.d(TAG, "onClick " + getPosition() + " messagesBean: " + messagesBean);
            if (clickListener != null)
                clickListener.onClick(view, getAdapterPosition(), messagesBean);
        }
    }
}