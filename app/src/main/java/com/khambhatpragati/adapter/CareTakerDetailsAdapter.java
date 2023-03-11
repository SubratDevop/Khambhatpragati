package com.khambhatpragati.adapter;

/**
 * Created by bhagvatee1.gupta on 03-03-2017.
 */

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khambhatpragati.R;
import com.khambhatpragati.listeners.ItemClickListener;
import com.khambhatpragati.model.response.CategoryBean;

import java.util.ArrayList;

public class CareTakerDetailsAdapter extends RecyclerView.Adapter<CareTakerDetailsAdapter.ViewHolder> {

    private static final String TAG = CareTakerDetailsAdapter.class.getName();

    private ArrayList<CategoryBean> listOfCategory;
    private ItemClickListener clickListener;

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvCareTakerName;
        private TextView tvLocationName;
        private TextView tvCareTakerMobileNo;
        private String mobileNo;

        public ViewHolder(View view) {
            super(view);
            tvCareTakerName = (TextView) view.findViewById(R.id.tvCareTakerName);
            tvLocationName = (TextView) view.findViewById(R.id.tvLocationName);
            tvCareTakerMobileNo = (TextView) view.findViewById(R.id.tvCareTakerMobileNo);
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

    public CareTakerDetailsAdapter(ArrayList<CategoryBean> listOfCategory) {
        this.listOfCategory = listOfCategory;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_care_taker_details, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CategoryBean categoryBean = listOfCategory.get(position);
        holder.tvCareTakerName.setText(categoryBean.getCareTakerName());
        holder.tvLocationName.setText(categoryBean.getLocationName());
        holder.tvCareTakerMobileNo.setText(categoryBean.getMobileNumber());
        holder.setItem(categoryBean.getMobileNumber());
    }

    @Override
    public int getItemCount() {
        return listOfCategory.size();
    }
}