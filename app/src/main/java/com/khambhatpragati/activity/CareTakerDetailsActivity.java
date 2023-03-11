package com.khambhatpragati.activity;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import static com.khambhatpragati.constants.Keys.KEY_CATEGORY_RESPONSE;

import com.khambhatpragati.R;
import com.khambhatpragati.adapter.CareTakerDetailsAdapter;
import com.khambhatpragati.application.Globals;
import com.khambhatpragati.helper.PreferencesManager;
import com.khambhatpragati.listeners.ItemClickListener;
import com.khambhatpragati.model.response.CategoryBean;
import com.khambhatpragati.parser.JSONParser;

import java.util.ArrayList;

public class CareTakerDetailsActivity extends AppCompatActivity implements ItemClickListener {

    private static final String TAG = CareTakerDetailsActivity.class.getSimpleName();
    private TextView tvSelectedCategory;
    private RecyclerView recyclerView;
    private CareTakerDetailsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_taker_details);
        initView();
    }

    private void initView() {
        tvSelectedCategory = (TextView) findViewById(R.id.tvSelectedCategory);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        ArrayList<CategoryBean> listOfCategoryData = new ArrayList<>();
        ArrayList<CategoryBean> listOfCategory = JSONParser.parseCategoryResponse(PreferencesManager.getString(CareTakerDetailsActivity.this, KEY_CATEGORY_RESPONSE));
        //Log.d(TAG, "listOfCategory.size() = " + listOfCategory.size());
        if (listOfCategory != null && listOfCategory.size() > 0) {
            for (CategoryBean category : listOfCategory) {
                if (category != null && category.getCategoryName().equals(Globals.getInstance().getSelectedCategoryName())) {
                    listOfCategoryData.add(category);
                }
            }
        }

        tvSelectedCategory.setText(Globals.getInstance().getSelectedCategoryName());
        mAdapter = new CareTakerDetailsAdapter(listOfCategoryData);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.setClickListener(this);
    }

    @Override
    public void onClick(View view, int position, String mobileNo) {
//        Log.d(TAG, "position = " + position);
//        Log.d(TAG, "mobileNo = " + mobileNo);
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + mobileNo));
        startActivity(intent);
    }
}
