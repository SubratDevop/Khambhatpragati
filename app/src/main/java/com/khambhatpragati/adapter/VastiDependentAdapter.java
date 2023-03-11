package com.khambhatpragati.adapter;

/**
 * Created by bhagvatee1.gupta on 03-03-2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.khambhatpragati.R;
import com.khambhatpragati.activity.DependentDetailActivity;
import com.khambhatpragati.helper.CustomVolleyRequest;
import com.khambhatpragati.listeners.ItemClickListener;
import com.khambhatpragati.model.DependentModel;

import java.util.ArrayList;

public class VastiDependentAdapter extends RecyclerView.Adapter<VastiDependentAdapter.ViewHolder> {

    private static final String TAG = VastiDependentAdapter.class.getSimpleName();
    private Context mContext;
    private String groupId;
    private ArrayList<DependentModel> listOfUsers;
    private ArrayList<DependentModel> filterList;
    private ItemClickListener clickListener;
    Context context;
    int currentPosition = -1;

    public VastiDependentAdapter(ArrayList<DependentModel> listOfUsers, Context mContext) {
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_vastidependent, parent, false);
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
        //if the position is equals to the item position which is to be expanded
        if (currentPosition == position) {
            //creating an animation
            Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);

            //toggling visibility
            holder.viewLV.setVisibility(View.VISIBLE);
            //adding sliding effect
            holder.viewLV.startAnimation(slideDown);

            ImageLoader imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
            if(BrotherBean.getPhoto_link().equalsIgnoreCase("NULL")) {// || BrotherBean.getPhoto_link().contains("null")) {
                ;//imageLoader.get(/*"http://khambhatpragati.com/uploads/member_images/" +*/ BrotherBean.getPhoto_link(), ImageLoader.getImageListener(holder.imgProfile, R.mipmap.noimage, R.mipmap.profile));
            }else{
                if(BrotherBean.getPhoto_link().trim().startsWith("http://"))
                    imageLoader.get(BrotherBean.getPhoto_link(), ImageLoader.getImageListener(holder.imgProfile, R.mipmap.noimage, R.mipmap.profile));
                else
                    imageLoader.get("http://khambhatpragati.com/uploads/member_images/" + BrotherBean.getPhoto_link(), ImageLoader.getImageListener(holder.imgProfile, R.mipmap.noimage, R.mipmap.profile));
            }
            if (!BrotherBean.getDependent_id().equalsIgnoreCase("null")) {
                holder.tvMemno.setText(BrotherBean.getDependent_id());
            }
            if (!BrotherBean.getFirst_name().equalsIgnoreCase("null")) {
                holder.tvFirstName.setText(BrotherBean.getFirst_name());
            }
            if (!BrotherBean.getGender().equalsIgnoreCase("null")) {
                String strGender="";
                if (BrotherBean.getGender().equalsIgnoreCase("1")
                        || BrotherBean.getGender().equalsIgnoreCase("Male")) {
                    strGender = "Male";
                } else if (BrotherBean.getGender().equalsIgnoreCase("2")
                        || BrotherBean.getGender().equalsIgnoreCase("FeMale")) {
                    strGender = "Female";
                }
                holder.tvGender.setText(strGender);
            }
            if (!BrotherBean.getDate_of_birth().equalsIgnoreCase("null")) {
                holder.tvDOB.setText(BrotherBean.getDate_of_birth());
            }
            if (!BrotherBean.getRelation_with_member().equalsIgnoreCase("null")) {
                String strRelation="";
                if (BrotherBean.getRelation_with_member().equalsIgnoreCase("1")
                        || BrotherBean.getRelation_with_member().equalsIgnoreCase("Son")) {
                    strRelation = "Son";
                } else if (BrotherBean.getRelation_with_member().equalsIgnoreCase("2")
                        || BrotherBean.getRelation_with_member().equalsIgnoreCase("Daughter")) {
                    strRelation = "Daughter";
                } else if (BrotherBean.getRelation_with_member().equalsIgnoreCase("3")
                        || BrotherBean.getRelation_with_member().equalsIgnoreCase("Wife")) {
                    strRelation = "Wife";
                } else if (BrotherBean.getRelation_with_member().equalsIgnoreCase("4")
                        || BrotherBean.getRelation_with_member().equalsIgnoreCase("Brother")) {
                    strRelation = "Brother";
                } else if (BrotherBean.getRelation_with_member().equalsIgnoreCase("5")
                        || BrotherBean.getRelation_with_member().equalsIgnoreCase("Sister")) {
                    strRelation = "Sister";
                }
                holder.tvRelation.setText(strRelation);
            }
            if (!BrotherBean.getBlood_group().equalsIgnoreCase("null")) {
                String strBloodGroup="";
                if (BrotherBean.getBlood_group().equalsIgnoreCase("1")
                        || BrotherBean.getBlood_group().equalsIgnoreCase("A+")) {
                    strBloodGroup = "A+";
                } else if (BrotherBean.getBlood_group().equalsIgnoreCase("2")
                        || BrotherBean.getBlood_group().equalsIgnoreCase("A-")) {
                    strBloodGroup = "A-";
                } else if (BrotherBean.getBlood_group().equalsIgnoreCase("3")
                        || BrotherBean.getBlood_group().equalsIgnoreCase("B+")) {
                    strBloodGroup = "B+";
                } else if (BrotherBean.getBlood_group().equalsIgnoreCase("4")
                        || BrotherBean.getBlood_group().equalsIgnoreCase("B-")) {
                    strBloodGroup = "B-";
                } else if (BrotherBean.getBlood_group().equalsIgnoreCase("5")
                        || BrotherBean.getBlood_group().equalsIgnoreCase("AB+")) {
                    strBloodGroup = "AB+";
                } else if (BrotherBean.getBlood_group().equalsIgnoreCase("6")
                        || BrotherBean.getBlood_group().equalsIgnoreCase("AB-")) {
                    strBloodGroup = "AB-";
                } else if (BrotherBean.getBlood_group().equalsIgnoreCase("7")
                        || BrotherBean.getBlood_group().equalsIgnoreCase("O+")) {
                    strBloodGroup = "O+";
                } else if (BrotherBean.getBlood_group().equalsIgnoreCase("8")
                        || BrotherBean.getBlood_group().equalsIgnoreCase("O-")) {
                    strBloodGroup = "O-";
                }
                holder.tvBloodGroup.setText(strBloodGroup);
            }
            if (!BrotherBean.getMarital_status().equalsIgnoreCase("null")) {
                String strMarritalStatus="";
                if (BrotherBean.getMarital_status().equalsIgnoreCase("1")
                        || BrotherBean.getMarital_status().equalsIgnoreCase("Married")) {
                    strMarritalStatus = "Married";
                } else if (BrotherBean.getMarital_status().equalsIgnoreCase("2")
                        || BrotherBean.getMarital_status().equalsIgnoreCase("UnMarried")) {
                    strMarritalStatus = "Unmarried";
                }
                holder.tvMarritalStatus.setText(strMarritalStatus);
            }
            if (!BrotherBean.getQualification().equalsIgnoreCase("null")) {
                String strQualification="";
                if (BrotherBean.getQualification().equalsIgnoreCase("1")
                        || BrotherBean.getQualification().equalsIgnoreCase("Student")) {
                    strQualification = "Student";
                } else if (BrotherBean.getQualification().equalsIgnoreCase("2")
                        || BrotherBean.getQualification().equalsIgnoreCase("10th")) {
                    strQualification = "10th";
                } else if (BrotherBean.getQualification().equalsIgnoreCase("3")
                        || BrotherBean.getQualification().equalsIgnoreCase("12th")) {
                    strQualification = "12th";
                } else if (BrotherBean.getQualification().equalsIgnoreCase("4")
                        || BrotherBean.getQualification().equalsIgnoreCase("Graduate")) {
                    strQualification = "Graduate";
                } else if (BrotherBean.getQualification().equalsIgnoreCase("5")
                        || BrotherBean.getQualification().equalsIgnoreCase("Post-Graduate")
                        || BrotherBean.getQualification().equalsIgnoreCase("PostGraduate")
                        || BrotherBean.getQualification().equalsIgnoreCase("Post Graduate")) {
                    strQualification = "Post-Graduate";
                } else if (BrotherBean.getQualification().equalsIgnoreCase("6")
                        || BrotherBean.getQualification().equalsIgnoreCase("Professional")) {
                    strQualification = "Professional";
                }
                holder.tvQualification.setText(strQualification);
            }
            if (!BrotherBean.getProfession().equalsIgnoreCase("null")) {
                String strBusiness="";
                if (BrotherBean.getProfession().equalsIgnoreCase("1")
                        || BrotherBean.getProfession().equalsIgnoreCase("Doctor")) {
                    strBusiness = "Doctor";
                } else if (BrotherBean.getProfession().equalsIgnoreCase("2")
                        || BrotherBean.getProfession().equalsIgnoreCase("Lawyer")) {
                    strBusiness = "Lawyer";
                } else if (BrotherBean.getProfession().equalsIgnoreCase("3")
                        || BrotherBean.getProfession().equalsIgnoreCase("Architects")) {
                    strBusiness = "Architects";
                } else if (BrotherBean.getProfession().equalsIgnoreCase("4")
                        || BrotherBean.getProfession().equalsIgnoreCase("CA/CS/ICWA")) {
                    strBusiness = "CA/CS/ICWA";
                } else if (BrotherBean.getProfession().equalsIgnoreCase("5")
                        || BrotherBean.getProfession().equalsIgnoreCase("MBA")) {
                    strBusiness = "MBA";
                } else if (BrotherBean.getProfession().equalsIgnoreCase("6")
                        || BrotherBean.getProfession().equalsIgnoreCase("Teacher/Professor")) {
                    strBusiness = "Teacher/Professor";
                } else if (BrotherBean.getProfession().equalsIgnoreCase("7")
                        || BrotherBean.getProfession().equalsIgnoreCase("Interior Designer")) {
                    strBusiness = "Interior Designer";
                } else if (BrotherBean.getProfession().equalsIgnoreCase("8")
                        || BrotherBean.getProfession().equalsIgnoreCase("Fashion Designer")) {
                    strBusiness = "Fashion Designer";
                } else if (BrotherBean.getProfession().equalsIgnoreCase("9")
                        || BrotherBean.getProfession().equalsIgnoreCase("Engineer")) {
                    strBusiness = "Engineer";
                } else if (BrotherBean.getProfession().equalsIgnoreCase("10")
                        || BrotherBean.getProfession().equalsIgnoreCase("Civil engineer")) {
                    strBusiness = "Civil engineer";
                } else if (BrotherBean.getProfession().equalsIgnoreCase("11")
                        || BrotherBean.getProfession().equalsIgnoreCase("Business")) {
                    strBusiness = "Business";
                } else if (BrotherBean.getProfession().equalsIgnoreCase("12")
                        || BrotherBean.getProfession().equalsIgnoreCase("Service")) {
                    strBusiness = "Service";
                } else if (BrotherBean.getProfession().equalsIgnoreCase("13")
                        || BrotherBean.getProfession().equalsIgnoreCase("Housewife")) {
                    strBusiness = "Housewife";
                } else if (BrotherBean.getProfession().equalsIgnoreCase("14")
                        || BrotherBean.getProfession().equalsIgnoreCase("Retired")) {
                    strBusiness = "Retired";
                } else if (BrotherBean.getProfession().equalsIgnoreCase("15")
                        || BrotherBean.getProfession().equalsIgnoreCase("Other")) {
                    strBusiness = "other";
                }
                holder.tvProfession.setText(strBusiness);
            }
            if (!BrotherBean.getMobile_no().equalsIgnoreCase("null")) {
                holder.tvMobile.setText(BrotherBean.getMobile_no());
            }
            if (!BrotherBean.getEmail().equalsIgnoreCase("null")) {
                holder.tvEmail.setText(BrotherBean.getEmail());
            }
            if (!BrotherBean.getMembership_status().equalsIgnoreCase("null")) {
                String strMembershipStatus="";
                if (BrotherBean.getMembership_status().equalsIgnoreCase("1")
                        || BrotherBean.getMembership_status().equalsIgnoreCase("Active")) {
                    strMembershipStatus = "Active";
                } else if (BrotherBean.getMembership_status().equalsIgnoreCase("0")
                        || BrotherBean.getMembership_status().equalsIgnoreCase("2")
                        || BrotherBean.getMembership_status().equalsIgnoreCase("InActive")
                        || BrotherBean.getMembership_status().equalsIgnoreCase("In Active")
                        || BrotherBean.getMembership_status().equalsIgnoreCase("In-Active")) {
                    strMembershipStatus = "In-Active";
                }
                holder.tvMembershipStatus.setText(strMembershipStatus);
            }
            if (!BrotherBean.getHusband_member_id().equalsIgnoreCase("null")) {
                holder.tvHusbandID.setText(BrotherBean.getHusband_member_id());
            }
            if (!BrotherBean.getHusband_description().equalsIgnoreCase("null")) {
                holder.tvHusbandDesc.setText(BrotherBean.getHusband_description());
            }
            if (!BrotherBean.getHusband_name().equalsIgnoreCase("null")) {
                holder.tvHusbandName.setText(BrotherBean.getHusband_name());
            }
            if (!BrotherBean.getHusband_qualification().equalsIgnoreCase("null")) {
                holder.tvHusbandQualification.setText(BrotherBean.getHusband_qualification());
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
        private LinearLayout viewLV;
        ImageView imgProfile;
        TextView tvMemno,tvFirstName,tvRelation,tvGender,txtID,
                tvDOB,tvBloodGroup,tvMarritalStatus,tvMembershipStatus,tvQualification,tvProfession,txtEmailHeading,
                tvHusbandID,tvHusbandName,tvHusbandDesc,tvHusbandQualification, tvMobile,tvEmail,txtMobileHeading;

        public ViewHolder(View view) {
            super(view);
            tvUserName = (TextView) view.findViewById(R.id.tvUserName);
            tvMobileNo = (TextView) view.findViewById(R.id.tvMobileNo);
            viewColor = (LinearLayout) view.findViewById(R.id.viewColor);
            viewLV = (LinearLayout) view.findViewById(R.id.viewLV);
            imgProfile = (ImageView) view.findViewById(R.id.imgProfile);
            tvMemno = (TextView) view.findViewById(R.id.tvMemNo);
            tvFirstName = (TextView) view.findViewById(R.id.tvFirstName);
            tvGender = (TextView) view.findViewById(R.id.tvGender);
            txtID = (TextView) view.findViewById(R.id.txtID);
            tvDOB = (TextView) view.findViewById(R.id.tvDOB);
            tvRelation = (TextView) view.findViewById(R.id.tvRelation);
            tvBloodGroup = (TextView) view.findViewById(R.id.tvBloodGroup);
            tvMarritalStatus = (TextView) view.findViewById(R.id.tvMarrital);
            tvMembershipStatus = (TextView) view.findViewById(R.id.tvMembershipStatus);
            tvQualification = (TextView) view.findViewById(R.id.tvQualification);
            tvProfession = (TextView) view.findViewById(R.id.tvBusiness);
            tvMobile = (TextView) view.findViewById(R.id.tvMobile);
            tvHusbandQualification = (TextView) view.findViewById(R.id.tvHusbandQualification);
            tvHusbandName = (TextView) view.findViewById(R.id.tvHusbandName);
            tvHusbandID = (TextView) view.findViewById(R.id.tvHusbandID);
            tvHusbandDesc = (TextView) view.findViewById(R.id.tvHusbandDesc);
            tvEmail = (TextView) view.findViewById(R.id.tvEmail);
            txtEmailHeading = (TextView) view.findViewById(R.id.txtEmailHeading);
            txtMobileHeading = (TextView) view.findViewById(R.id.txtMobileHeading);

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