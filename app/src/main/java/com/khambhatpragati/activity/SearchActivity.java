package com.khambhatpragati.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.khambhatpragati.R;
import com.khambhatpragati.application.Globals;
import com.khambhatpragati.helper.PreferencesManager;
import com.khambhatpragati.model.response.CategoryBean;
import com.khambhatpragati.parser.JSONParser;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import static com.khambhatpragati.constants.Keys.KEY_CATEGORY_RESPONSE;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = SearchActivity.class.getName();

    private AutoCompleteTextView autoCompleteCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView() {
        autoCompleteCategory = (AutoCompleteTextView) findViewById(R.id.autoCompleteCategory);
        loadCategory();
        addListenerOnWidgets();
    }

    private void loadCategory() {
        ArrayList<String> listOfCategoryName = new ArrayList<>();
        ArrayList<CategoryBean> listOfCategory = JSONParser.parseCategoryResponse(PreferencesManager.getString(SearchActivity.this, KEY_CATEGORY_RESPONSE));
        if (listOfCategory != null && listOfCategory != null && listOfCategory.size() > 0) {
            for (CategoryBean category : listOfCategory) {
                listOfCategoryName.add(category.getCategoryName());
            }
        }

        listOfCategoryName = new ArrayList<String>(new LinkedHashSet<String>(listOfCategoryName));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfCategoryName);
        autoCompleteCategory.setAdapter(adapter);
    }

    private void addListenerOnWidgets() {

        autoCompleteCategory.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                autoCompleteCategory.showDropDown();
                autoCompleteCategory.requestFocus();
                return false;
            }
        });

        autoCompleteCategory.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                autoCompleteCategory.showDropDown();
                autoCompleteCategory.requestFocus();
            }
        });

        autoCompleteCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = (String) parent.getItemAtPosition(position);
                //Log.d(TAG, "selectedCategory = " + selectedCategory);
                Globals.getInstance().setSelectedCategoryName(selectedCategory);
                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_down);
            }
        });
    }
}
