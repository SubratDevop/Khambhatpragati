package com.khambhatpragati.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.khambhatpragati.R;
import com.khambhatpragati.adapter.BrotherAdapter;
import com.khambhatpragati.constants.URLs;
import com.khambhatpragati.helper.CustomVolleyRequest;
import com.khambhatpragati.helper.ExifUtils;
import com.khambhatpragati.model.BrotherModel;
import com.khambhatpragati.model.SpinnerModel;
import com.khambhatpragati.model.response.AdvertismentResponse;
import com.khambhatpragati.model.response.OTPVerificationResponse;
import com.khambhatpragati.parser.JSONParser;
import com.khambhatpragati.preference.UserPreference;
import com.khambhatpragati.utils.DialogUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.khambhatpragati.activity.DashboardActivity.adCounter;
import static com.khambhatpragati.activity.DashboardActivity.adsImageUrls;
import static com.khambhatpragati.activity.DashboardActivity.isAdsTicking;
import static com.khambhatpragati.activity.DashboardActivity.isShownOnce;
import static com.khambhatpragati.constants.Keys.KEY_GROUPS_ID;
import static com.khambhatpragati.constants.URLs.ADDBROTHER_URL;
import static com.khambhatpragati.constants.URLs.ADDMEMBERIMAGE_URL;
import static com.khambhatpragati.constants.URLs.DELETEBROTHER_URL;
import static com.khambhatpragati.constants.URLs.GET_MEMBERPROFILE_URL;
import static com.khambhatpragati.constants.URLs.UPDATEMEMBER_URL;

public class ProfileActivity extends AppCompatActivity {

    Context context;
    private int mYear, mMonth, mDay;
    private int mYear1, mMonth1, mDay1;
    Calendar c1,c2;

    private RecyclerView recyclerBrother;
    BrotherAdapter adapter;
    private ArrayList<BrotherModel> BrotherModelList;
    String strMemStatus="",strBloodGroup="",strMarritalStatus="",strQualification="",strMembershipStatus="",
            strBusiness="",strGender="",strMandal="",strBirthDate="",strParentID="",strCurrentDate="",strBroAge="";
    Spinner spnMemStatus,spnBloodGroup,spnMarritalStatus,spnQualification,spnMembershipStatus,spnBusiness;
    List<SpinnerModel> listMemStatus,listBloodGroup,listMarritalStatus,listQualification,listMembershipStatus,listBusiness;
    ArrayAdapter<SpinnerModel> arrayAdapterMemStatus, arrayAdapterBloodGroup,arrayAdapterMarritalStatus,
            arrayAdapterQualification,arrayAdapterMembershipStatus,arrayAdapterBusiness;
    EditText edtMemNo,edtFirstName,edtMiddleName,edtLastName,edtKnownOriginal,edtParentMemNo,edtGrandFatherName,
            edtDOB,edtFlatNoBuilding,edtResidenseArea,edtNearestStation,edtCity,edtPincode,edtKhambatAddress,
            edtMobile,edtEmail,edtPostGraduate,edtOthers,edtEngineers;
    TextView tvMemno,tvFirstName,tvMiddleName,tvLastName,tvKnownLastName,tvGrandfather,tvParentMemno,tvGender,
            tvDOB,tvMemStatus,tvBloodGroup,tvMarritalStatus,tvMembershipStatus,tvQualification,tvProfession,tvBrotherlist,
            tvMandal,tvFlatNo,tvResidenceArea,tvNearestStn,tvCity,tvPicode,tvMobile,tvEmail,tvKhambat;
    Button btnUpdate;
    EditText edtBroDOB;
    EditText edtBroName;
    EditText edtBroMobile;
    EditText edtBroID;
    MaterialDialog dialog;
    RelativeLayout relativeLayout;
    RadioGroup rgGender,rgMandal,rgGraduate,rgBusiness,rgServices;
    RadioButton rbMale,rbFemale,rbYes,rbNo,rbCommerce,rbArts,rbScience,rbManufacturing,rbStokist,rbPublic,rbPrivate;
    ImageView btnImageUpload,imgProfile,imgCalDOB,imgAddBrother,imgSearchParent,imgCancelParent;
    LinearLayout imgDependent,imgEdit,lvView,lvEdit;
    private Toolbar toolbar;
    File f;
    String Path;
    private static final int PERMISSION_REQUEST_CODE = 1;
    Bitmap bm;
    String ba1;
    private ImageLoader imageLoader;
    ImageView imageView;
    private String strAdWeblink="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = ProfileActivity.this;
        initToolBar();
        boolean isPermission = checkPermission();
        if (isPermission == true) {
        } else {
            requestPermission();
        }
        initView();
    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My Profile");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }

    private void initView() {

        BrotherModelList = new ArrayList<>();
        recyclerBrother = (RecyclerView) findViewById(R.id.recyclerSelf);
        initializeAdapterSelf();

        relativeLayout = findViewById(R.id.relativeLayout);
        tvMemno = findViewById(R.id.tvMemNo);
        tvFirstName = findViewById(R.id.tvFirstName);
        tvMiddleName = findViewById(R.id.tvMiddleName);
        tvLastName = findViewById(R.id.tvLastName);
        tvKnownLastName = findViewById(R.id.tvLastKnown);
        tvGrandfather = findViewById(R.id.tvGrandFatherName);
        tvParentMemno = findViewById(R.id.tvparentMemno);
        tvGender = findViewById(R.id.tvGender);
        tvDOB = findViewById(R.id.tvDOB);
        tvMemStatus = findViewById(R.id.tvMemStatus);
        tvBloodGroup = findViewById(R.id.tvBloodGroup);
        tvMarritalStatus = findViewById(R.id.tvMarrital);
        tvMembershipStatus = findViewById(R.id.tvMembershipStatus);
        tvQualification = findViewById(R.id.tvQualification);
        tvProfession = findViewById(R.id.tvBusiness);
        tvBrotherlist = findViewById(R.id.tvBrothers);
        tvMandal = findViewById(R.id.tvMandal);
        tvFlatNo = findViewById(R.id.tvFlatno);
        tvResidenceArea = findViewById(R.id.tvRArea);
        tvNearestStn = findViewById(R.id.tvNearestStn);
        tvCity = findViewById(R.id.tvCity);
        tvPicode = findViewById(R.id.tvPincode);
        tvMobile = findViewById(R.id.tvMobile);
        tvEmail = findViewById(R.id.tvEmail);
        tvKhambat = findViewById(R.id.tvKhambatAddress);

        edtMemNo = findViewById(R.id.edtMemNo);
        edtFirstName = findViewById(R.id.edtFirstName);
        edtMiddleName = findViewById(R.id.edtMiddleName);
        edtLastName = findViewById(R.id.edtLastName);
        edtKnownOriginal = findViewById(R.id.edtKnownOriginal);
        edtParentMemNo = findViewById(R.id.edtParentMemNo);
        edtGrandFatherName = findViewById(R.id.edtGrandFatherName);
        edtDOB = findViewById(R.id.edtDOB);
        edtFlatNoBuilding = findViewById(R.id.edtFlatNoBuilding);
        edtResidenseArea = findViewById(R.id.edtResidenseArea);
        edtNearestStation = findViewById(R.id.edtNearestStation);
        edtCity = findViewById(R.id.edtCity);
        edtPincode = findViewById(R.id.edtPincode);
        edtKhambatAddress = findViewById(R.id.edtKhambatAddress);
        edtMobile = findViewById(R.id.edtMobile);
        edtEmail = findViewById(R.id.edtEmail);
        edtPostGraduate = findViewById(R.id.edtPostGraduate);
        edtOthers = findViewById(R.id.edtOthers);
        edtEngineers = findViewById(R.id.edtEngineers);

        rgServices = findViewById(R.id.rgServices);
        rgGender = findViewById(R.id.rgGender);
        rgMandal = findViewById(R.id.rbMandal);
        rgGraduate = findViewById(R.id.rgGraduate);
        rgBusiness = findViewById(R.id.rgBusiness);

        rbPrivate = findViewById(R.id.radioPrivate);
        rbPublic = findViewById(R.id.radioPublic);
        rbMale = findViewById(R.id.radioMale);
        rbFemale = findViewById(R.id.radioFeMale);
        rbYes = findViewById(R.id.radioYes);
        rbNo = findViewById(R.id.radioNo);
        rbCommerce = findViewById(R.id.radioCommerce);
        rbArts = findViewById(R.id.radioArts);
        rbScience = findViewById(R.id.radioScience);
        rbManufacturing = findViewById(R.id.radioManufacturing);
        rbStokist = findViewById(R.id.radioStokist);

        btnImageUpload = findViewById(R.id.btnImageUpload);
        imgProfile = findViewById(R.id.imgProfile);
        imgCancelParent = findViewById(R.id.imgCancelParent);
        imgAddBrother = findViewById(R.id.imgAddBrother);
        imgCalDOB = findViewById(R.id.imgCal);

        imgDependent = findViewById(R.id.imgDependent);
        imgEdit = findViewById(R.id.imgEdit);

/*
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        String strMDay = "";
        String strMMonth = "";
        if (mDay < 10) {
            strMDay = "0" + mDay;
        } else {
            strMDay = mDay + "";
        }
        if (mMonth + 1 < 10) {
            strMMonth = "0" + String.valueOf(mMonth + 1);
        } else {
            strMMonth = String.valueOf(mMonth + 1);
        }
        strCurrentDate = strMDay + "/" + strMMonth + "/" + mYear;

        strBirthDate = mYear +"/"+ strMMonth + "/" + strMDay;
        edtDOB.setText(strCurrentDate);
*/

        edtDOB.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtDOB.getText().length() > 0) {
                    edtDOB.setError(null);
                }
            }
        });

        imgCalDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c1 = Calendar.getInstance();
                mYear = c1.get(Calendar.YEAR);
                mMonth = c1.get(Calendar.MONTH);
                mDay = c1.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String monthString = String.valueOf(monthOfYear + 1);
                                String dayString = String.valueOf(dayOfMonth);
                                if (monthString.length() == 1)
                                    monthString = "0" + monthString;
                                if (dayOfMonth <= 9)
                                    dayString = "0" + dayOfMonth;

                                edtDOB.setText(dayString + "/" + (monthString) + "/" + year);
                                strBirthDate = year +"/"+ monthString + "/" + dayString;

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btnImageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Add Photo!");
                builder.setItems(options,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                if (options[id].equals("Take Photo")) {
                                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                                        Uri photoURI = null;
                                        try {
                                            photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".my.package.name.provider", createImageFile());
                                            //f = new File(Environment.getExternalStorageDirectory(), Path);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                        startActivityForResult(intent, 1);
                                    } else {
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                                        startActivityForResult(intent, 1);

                                    }
                                } else if (options[id].equals("Choose from Gallery")) {
                                    Log.w(">>>>>>>>>>>>>>", "gallery intent1");
                                    Intent intent = new Intent(Intent.ACTION_PICK,
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    Log.w(">>>>>>>>>>>>>>", "gallery intent2");
                                    startActivityForResult(intent, 2);
                                    Log.w(">>>>>>>>>>>>>>", "gallery intent3");
                                } else if (options[id].equals("Cancel")) {
                                    dialog.dismiss();
                                }
                            }
                        });
                builder.show();
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Add Photo!");
                builder.setItems(options,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                if (options[id].equals("Take Photo")) {
                                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                                        Uri photoURI = null;
                                        try {
                                            photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".my.package.name.provider", createImageFile());
                                            //f = new File(Environment.getExternalStorageDirectory(), Path);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                        startActivityForResult(intent, 1);
                                    } else {
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                                        startActivityForResult(intent, 1);

                                    }
                                } else if (options[id].equals("Choose from Gallery")) {
                                    Log.w(">>>>>>>>>>>>>>", "gallery intent1");
                                    Intent intent = new Intent(Intent.ACTION_PICK,
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    Log.w(">>>>>>>>>>>>>>", "gallery intent2");
                                    startActivityForResult(intent, 2);
                                    Log.w(">>>>>>>>>>>>>>", "gallery intent3");
                                } else if (options[id].equals("Cancel")) {
                                    dialog.dismiss();
                                }
                            }
                        });
                builder.show();
            }
        });

        imgCancelParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtParentMemNo.setText("");
            }
        });

        btnUpdate = findViewById(R.id.btnOk);
        lvView = findViewById(R.id.viewLV);
        lvEdit = findViewById(R.id.editLV);
        imgSearchParent = findViewById(R.id.imgSearchParent);
        imgSearchParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, SelectMemberIDActivity.class);
                i.putExtra(KEY_GROUPS_ID, "");
                i.putExtra("callingFrom", "Parent");
                startActivityForResult(i,2020);
                overridePendingTransition(R.anim.slide_up, R.anim.stay);
            }
        });

        imgDependent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, DependentListActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slide_up, R.anim.stay);
            }
        });
        imgProfile.setClickable(false);
        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvView.setVisibility(View.GONE);
                imgEdit.setVisibility(View.GONE);
                recyclerBrother.getAdapter().notifyDataSetChanged();
                btnImageUpload.setVisibility(View.VISIBLE);
                imgProfile.setClickable(true);
                btnUpdate.setVisibility(View.VISIBLE);
                lvEdit.setVisibility(View.VISIBLE);
                spnMembershipStatus.setEnabled(false);
                spnMemStatus.setEnabled(false);
            }
        });
        spnMemStatus = (Spinner) findViewById(R.id.spnMemStatus);
        listMemStatus = new ArrayList<>();
        listMemStatus.add(new SpinnerModel("1","Life-Member"));
        listMemStatus.add(new SpinnerModel("2","Expired"));
        arrayAdapterMemStatus = new ArrayAdapter<SpinnerModel>(context, android.R.layout.simple_spinner_item, listMemStatus) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setGravity(Gravity.LEFT);
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
                //tv.setTextColor(Color.RED);
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setGravity(Gravity.LEFT);
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.BLACK);
                    ;
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        arrayAdapterMemStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMemStatus.setAdapter(arrayAdapterMemStatus);
        spnMemStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strMemStatus = listMemStatus.get(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        spnMembershipStatus = (Spinner) findViewById(R.id.spnMembership);
        listMembershipStatus = new ArrayList<>();
        listMembershipStatus.add(new SpinnerModel("1","Active"));
        listMembershipStatus.add(new SpinnerModel("0","In-Active"));
        arrayAdapterMembershipStatus = new ArrayAdapter<SpinnerModel>(context, android.R.layout.simple_spinner_item, listMembershipStatus) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setGravity(Gravity.LEFT);
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
                //tv.setTextColor(Color.RED);
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setGravity(Gravity.LEFT);
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.BLACK);
                    ;
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        arrayAdapterMembershipStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMembershipStatus.setAdapter(arrayAdapterMembershipStatus);
        spnMembershipStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strMembershipStatus = listMembershipStatus.get(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spnMarritalStatus = (Spinner) findViewById(R.id.spnMaritalStatus);
        listMarritalStatus = new ArrayList<>();
        listMarritalStatus.add(new SpinnerModel("1","Married"));
        listMarritalStatus.add(new SpinnerModel("2","Unmarried"));
        arrayAdapterMarritalStatus = new ArrayAdapter<SpinnerModel>(context, android.R.layout.simple_spinner_item, listMarritalStatus) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setGravity(Gravity.LEFT);
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
                //tv.setTextColor(Color.RED);
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setGravity(Gravity.LEFT);
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.BLACK);
                    ;
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        arrayAdapterMarritalStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMarritalStatus.setAdapter(arrayAdapterMarritalStatus);
        spnMarritalStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strMarritalStatus = listMarritalStatus.get(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spnBloodGroup = (Spinner) findViewById(R.id.spnBloodGroup);
        listBloodGroup = new ArrayList<>();
        listBloodGroup.add(new SpinnerModel("1","A+"));
        listBloodGroup.add(new SpinnerModel("2","A-"));
        listBloodGroup.add(new SpinnerModel("3","B+"));
        listBloodGroup.add(new SpinnerModel("4","B-"));
        listBloodGroup.add(new SpinnerModel("5","AB+"));
        listBloodGroup.add(new SpinnerModel("6","AB-"));
        listBloodGroup.add(new SpinnerModel("7","O+"));
        listBloodGroup.add(new SpinnerModel("8","O-"));
        arrayAdapterBloodGroup = new ArrayAdapter<SpinnerModel>(context, android.R.layout.simple_spinner_item, listBloodGroup) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setGravity(Gravity.LEFT);
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
                //tv.setTextColor(Color.RED);
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setGravity(Gravity.LEFT);
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.BLACK);
                    ;
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        arrayAdapterBloodGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnBloodGroup.setAdapter(arrayAdapterBloodGroup);
        spnBloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strBloodGroup = listBloodGroup.get(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spnQualification = (Spinner) findViewById(R.id.spnQualification);
        listQualification = new ArrayList<>();
        listQualification.add(new SpinnerModel("1","Student"));
        listQualification.add(new SpinnerModel("2","10th"));
        listQualification.add(new SpinnerModel("3","12th"));
        listQualification.add(new SpinnerModel("4","Diploma"));
        listQualification.add(new SpinnerModel("5","Graduate"));
        listQualification.add(new SpinnerModel("6","Post-Graduate"));
        listQualification.add(new SpinnerModel("7","Professional"));
        arrayAdapterQualification = new ArrayAdapter<SpinnerModel>(context, android.R.layout.simple_spinner_item, listQualification) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setGravity(Gravity.LEFT);
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
                //tv.setTextColor(Color.RED);
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setGravity(Gravity.LEFT);
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.BLACK);
                    ;
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        arrayAdapterQualification.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnQualification.setAdapter(arrayAdapterQualification);
        spnQualification.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strQualification = listQualification.get(position).toString();
                if(strQualification.equalsIgnoreCase("Graduate")){
                    rgGraduate.setVisibility(View.VISIBLE);
                    spnBusiness.setVisibility(View.VISIBLE);
                    edtPostGraduate.setVisibility(View.GONE);
                    int selectedGenderId = rgGraduate.getCheckedRadioButtonId();
                    // find the radiobutton by returned id
                    RadioButton radioButton = (RadioButton) findViewById(selectedGenderId);
                    if (radioButton == null) {
                        //strQualification = "";
                    } else {
                        strQualification = "Graduate - "+radioButton.getText().toString();
                    }
                }else if(strQualification.equalsIgnoreCase("Post-Graduate")){
                    rgGraduate.setVisibility(View.GONE);
                    spnBusiness.setVisibility(View.VISIBLE);
                    edtPostGraduate.setVisibility(View.VISIBLE);
                    strQualification = "Post-Graduate"+edtPostGraduate.getText().toString();
                }else if(strQualification.equalsIgnoreCase("Student")){
                    spnBusiness.setVisibility(View.GONE);
                    rgGraduate.setVisibility(View.GONE);
                    rgBusiness.setVisibility(View.GONE);
                    rgServices.setVisibility(View.GONE);
                    edtPostGraduate.setVisibility(View.GONE);
                    edtEngineers.setVisibility(View.GONE);
                    edtOthers.setVisibility(View.GONE);
                    strBusiness = "";
                }else{
                    spnBusiness.setVisibility(View.VISIBLE);
                    rgGraduate.setVisibility(View.GONE);
                    edtPostGraduate.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        spnBusiness = (Spinner) findViewById(R.id.spnProfession);
        listBusiness = new ArrayList<>();
        listBusiness.add(new SpinnerModel("1","Doctor"));
        listBusiness.add(new SpinnerModel("2","Lawyer"));
        listBusiness.add(new SpinnerModel("3","Architects"));
        listBusiness.add(new SpinnerModel("4","CA/CS/ICWA"));
        listBusiness.add(new SpinnerModel("5","MBA"));
        listBusiness.add(new SpinnerModel("6","Teacher/Professor"));
        listBusiness.add(new SpinnerModel("7","Interior Designer"));
        listBusiness.add(new SpinnerModel("8","Fashion Designer"));
        listBusiness.add(new SpinnerModel("9","Engineer"));
        listBusiness.add(new SpinnerModel("10","Civil Engineer"));
        listBusiness.add(new SpinnerModel("11","Business"));
        listBusiness.add(new SpinnerModel("12","Service"));
        listBusiness.add(new SpinnerModel("13","Housewife"));
        listBusiness.add(new SpinnerModel("14","Retired"));
        listBusiness.add(new SpinnerModel("15","Other"));
        arrayAdapterBusiness = new ArrayAdapter<SpinnerModel>(context, android.R.layout.simple_spinner_item, listBusiness) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setGravity(Gravity.LEFT);
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
                //tv.setTextColor(Color.RED);
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setGravity(Gravity.LEFT);
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.BLACK);
                    ;
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        arrayAdapterBusiness.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnBusiness.setAdapter(arrayAdapterBusiness);
        spnBusiness.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strBusiness = listBusiness.get(position).toString();

                if(strBusiness.equalsIgnoreCase("Business")){
                    rgBusiness.setVisibility(View.VISIBLE);
                    rgServices.setVisibility(View.GONE);
                    edtOthers.setVisibility(View.GONE);
                    edtEngineers.setVisibility(View.GONE);
                    int selectedId = rgBusiness.getCheckedRadioButtonId();
                    // find the radiobutton by returned id
                    RadioButton radioButton = (RadioButton) findViewById(selectedId);
                    if (radioButton == null) {
                        //strBusiness = "";
                    } else {
                        strBusiness = "Business - "+radioButton.getText().toString();
                    }
                }else if(strBusiness.equalsIgnoreCase("Service")){
                    rgBusiness.setVisibility(View.GONE);
                    rgServices.setVisibility(View.VISIBLE);
                    edtOthers.setVisibility(View.GONE);
                    edtEngineers.setVisibility(View.GONE);
                    int selectedId = rgServices.getCheckedRadioButtonId();
                    // find the radiobutton by returned id
                    RadioButton radioButton = (RadioButton) findViewById(selectedId);

                    if (radioButton == null) {
                        //strBusiness = "";
                    } else {
                        strBusiness = "Service - "+radioButton.getText().toString();
                    }
                }else if(strBusiness.equalsIgnoreCase("Other")){
                    rgBusiness.setVisibility(View.GONE);
                    edtOthers.setVisibility(View.VISIBLE);
                    rgServices.setVisibility(View.GONE);
                    edtEngineers.setVisibility(View.GONE);
                    strBusiness = "Other - "+edtOthers.getText().toString();
                }else if(strBusiness.equalsIgnoreCase("Engineer")){
                    rgBusiness.setVisibility(View.GONE);
                    rgServices.setVisibility(View.GONE);
                    edtOthers.setVisibility(View.GONE);
                    edtEngineers.setVisibility(View.VISIBLE);
                    strBusiness = "Engineer - "+edtEngineers.getText().toString();
                }else{
                    rgBusiness.setVisibility(View.GONE);
                    edtOthers.setVisibility(View.GONE);
                    rgServices.setVisibility(View.GONE);
                    edtEngineers.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        rgServices.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb=(RadioButton)findViewById(checkedId);
                strBusiness ="Services - "+ rb.getText().toString();
            }
        });
        rgBusiness.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb=(RadioButton)findViewById(checkedId);
                strBusiness = "Business - "+ rb.getText().toString();
            }
        });
        rgGraduate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb=(RadioButton)findViewById(checkedId);
                strQualification = "Graduate - "+ rb.getText().toString();
            }
        });

        imgAddBrother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBrother();
            }
        });
        getMemberProfile();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedGenderId = rgGender.getCheckedRadioButtonId();
                // find the radiobutton by returned id
//                RadioButton radioButton = (RadioButton) findViewById(selectedGenderId);
//                if (radioButton == null) {
//                    strGender = "";
//                } else {
//                    strGender = radioButton.getText().toString();
//                }
//
//                int selectedMadalId = rgMandal.getCheckedRadioButtonId();
//                // find the radiobutton by returned id
//                RadioButton radioButtonMandal = (RadioButton) findViewById(selectedMadalId);
//                if (radioButtonMandal == null) {
//                    strMandal = "";
//                } else {
//                    strMandal = radioButtonMandal.getText().toString();
//                }

//                boolean error = false;
//                final String regexStr = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}$";
//                String regexPincode = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$";
//
//                if (edtFirstName.getText().toString().equalsIgnoreCase("")) {
//                    edtFirstName.setError("Please enter First name");
//                    edtFirstName.requestFocus();
//                    error = true;
//                }else if (edtMiddleName.getText().toString().equalsIgnoreCase("")) {
//                    edtMiddleName.setError("Please enter Middle name");
//                    edtMiddleName.requestFocus();
//                    error = true;
//                }else if (edtLastName.getText().toString().equalsIgnoreCase("")) {
//                    edtLastName.setError("Please enter Last name");
//                    edtLastName.requestFocus();
//                    error = true;
//                }else if (edtDOB.getText().toString().equalsIgnoreCase("")) {
//                    edtDOB.setError("Please Select Birth Date");
//                    edtDOB.requestFocus();
//                    error = true;
//                }else if (strGender.equalsIgnoreCase("")) {
//                    Toast.makeText(context, "Please Select Gender", Toast.LENGTH_SHORT).show();
//                    rgGender.requestFocus();
//                    error = true;
//                }else if (strGender.equalsIgnoreCase("Female")) {
//                    Toast.makeText(context, "Female member should be added as dependent", Toast.LENGTH_SHORT).show();
//                    rgGender.requestFocus();
//                    error = true;
//                } else if (strMandal.equalsIgnoreCase("")) {
//                    Toast.makeText(context, "Please Select Are you in Mandal", Toast.LENGTH_SHORT).show();
//                    rgMandal.requestFocus();
//                    error = true;
//                } else if (strBloodGroup.equalsIgnoreCase("")) {
//                    Toast.makeText(context, "Please Select Blood group", Toast.LENGTH_SHORT).show();
//                    spnBloodGroup.requestFocus();
//                    error = true;
//                } else if (strMarritalStatus.equalsIgnoreCase("")) {
//                    Toast.makeText(context, "Please Select marrital status", Toast.LENGTH_SHORT).show();
//                    spnMarritalStatus.requestFocus();
//                    error = true;
//                } else if (edtPincode.getText().toString().equalsIgnoreCase("")) {
//                    edtPincode.setError("Please enter pin code");
//                    edtPincode.requestFocus();
//                    error = true;
//                } else if (edtPincode.getText().toString().length() > 0
//                        && edtPincode.getText().toString().length() < 6) {
//                    edtPincode.requestFocus();
//                    edtPincode.setError("Please enter valid pincode");
//                    error = true;
//                } else if (!edtPincode.getText().toString().matches(regexPincode)) {
//                    edtPincode.requestFocus();
//                    edtPincode.setError("Please enter valid pincode");
//                    error = true;
//                } else if (edtCity.getText().toString().equalsIgnoreCase("")) {
//                    edtCity.setError("Please enter city");
//                    edtCity.requestFocus();
//                    error = true;
//                }else if (edtResidenseArea.getText().toString().equalsIgnoreCase("")) {
//                    edtResidenseArea.setError("Please enter Residense Area");
//                    edtResidenseArea.requestFocus();
//                    error = true;
//                }else if (edtNearestStation.getText().toString().equalsIgnoreCase("")) {
//                    edtNearestStation.setError("Please enter Nearest Station");
//                    edtNearestStation.requestFocus();
//                    error = true;
//                }else if (edtMobile.getText().toString().equalsIgnoreCase("")) {
//                    edtMobile.setError("Please enter mobile number");
//                    edtMobile.requestFocus();
//                    error = true;
//                } else if (edtMobile.getText().toString().length() > 0
//                        && !edtMobile.getText().toString().matches(regexStr)) {
//                    edtMobile.setError("Please enter valid mobile no.");
//                    edtMobile.setFocusableInTouchMode(true);
//                    edtMobile.requestFocus();
//                    error = true;
//                } else if (edtMobile.getText().toString().length() > 0
//                        && edtMobile.getText().toString().length() < 10) {
//                    edtMobile.setError("Please Enter Valid Mobile No");
//                    edtMobile.setFocusableInTouchMode(true);
//                    edtMobile.requestFocus();
//                    error = true;
//                }

//                if (error != true) {
//                    updateMember();
//                }
                updateMember();
            }
        });

//        final Timer timer = new Timer ();
//        TimerTask hourlyTask = new TimerTask () {
//            @Override
//            public void run () {
//                if (adsImageUrls.size() > 0) {
//                    if (adCounter >= adsImageUrls.size())
//                        adCounter = 0;
//                    Intent i = new Intent(ProfileActivity.this, FullScreenAdActivity.class);
//                    i.putExtra("adURL", adsImageUrls.get(adCounter).getFull_path());
//                    startActivity(i);
//                }
//            }
//        };
//
//// schedule the task to run starting now and then every hour...
//        timer.schedule (hourlyTask, 0l, 1000*1*60);
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!isAdsTicking) {
                    if (adsImageUrls.size() > 0) {
                        if (adCounter >= adsImageUrls.size())
                            adCounter = 0;
                        Intent i = new Intent(ProfileActivity.this, FullScreenAdActivity.class);
                        i.putExtra("adURL", adsImageUrls.get(adCounter).getFull_path());
                        startActivity(i);
                    }
                }
            }
        }, 1500);*/
        if(!isAdsTicking ) {
            if(!isShownOnce) {
                if (adsImageUrls.size() > 0) {
                    if (adCounter >= adsImageUrls.size())
                        adCounter = 0;
                    Intent i = new Intent(ProfileActivity.this, FullScreenAdActivity.class);
                    i.putExtra("adURL", adsImageUrls.get(adCounter).getFull_path());
                    i.putExtra("adWebLink", adsImageUrls.get(adCounter).getWebsite_url());
                    i.putExtra("adTIMEOUT", adsImageUrls.get(adCounter).getDisplay_time());
                    startActivity(i);
                }
                isShownOnce=true;
                isAdsTicking=true;
            }
        }

        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!strAdWeblink.equalsIgnoreCase("") && !strAdWeblink.equalsIgnoreCase("null")) {
                    Intent intent = new Intent(ProfileActivity.this, WebviewActivity.class);
                    intent.putExtra("strValue", strAdWeblink);
                    startActivity(intent);
                }
            }
        });
        getBottomAds();
    }

    private void getBottomAds() {
        final ProgressDialog progress = new ProgressDialog(ProfileActivity.this);
        progress.setMessage("Please Wait....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

        AQuery aQuery = new AQuery(getApplicationContext());
        Map<String, Object> params = new HashMap<>();
        params.put("advertisement_type","BottomAd");
        params.put("screen_type","UserProfile Screen");

        Log.d("Params", "Testing" + params);

        aQuery.ajax(URLs.GET_ADS_URL, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                Log.d("Checking", "url: " + url + " , json: " + json);
                try {
                    if (json.toString().contains("data")) {
                        AdvertismentResponse adsResponse = JSONParser.parseAdsResponse(json.toString());
                        if (adsResponse.isStatus().equalsIgnoreCase("200")) {
                            Date todayDate = Calendar.getInstance().getTime();
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            String todayString = formatter.format(todayDate);
                            for(int i=0;i<adsResponse.getData().size();i++){
                                if(formatter.parse(adsResponse.getData().get(i).getEnd_date()).after(formatter.parse(todayString))
                                        || formatter.parse(adsResponse.getData().get(i).getEnd_date()).equals(formatter.parse(todayString))) {
                                    if(adsResponse.getData().get(i).getAdvertisement_type().equalsIgnoreCase("BottomAd")
                                            && adsResponse.getData().get(i).getScreen_type().equalsIgnoreCase("UserProfile Screen")) {
                                        imageView.setVisibility(View.VISIBLE);
                                        strAdWeblink=adsResponse.getData().get(i).getWebsite_url();
                                        imageLoader = CustomVolleyRequest.getInstance(ProfileActivity.this).getImageLoader();
                                        imageLoader.get(adsResponse.getData().get(i).getFull_path(), ImageLoader.getImageListener(imageView, R.mipmap.noimage, android.R.drawable.ic_dialog_alert));
                                    }
                                }
                            }

                        } else {
                            imageView.setVisibility(View.GONE);
                            //ProgressDialogView.hideProgressDialog();
                            DialogUtil.showSnackBarWithAction(relativeLayout, adsResponse.getMessage());
                        }
                    }
                    progress.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    progress.dismiss();
                }
            }
        }.method(AQuery.METHOD_POST));

    }

    public void showBrother(){
        dialog = new MaterialDialog.Builder(ProfileActivity.this)
                        .customView(R.layout.dialog_addbrother, true)
                        .cancelable(true)
                        .build();
        edtBroDOB =  (EditText) dialog.findViewById(R.id.edtDOB);
        edtBroName =  (EditText) dialog.findViewById(R.id.edtMemberName);
        edtBroMobile =  (EditText) dialog.findViewById(R.id.edtMemberContact);
        edtBroID =  (EditText) dialog.findViewById(R.id.edtBroMemNo);
        ImageView imgBroSearch =  (ImageView) dialog.findViewById(R.id.imgSearchBro);
        ImageView imgBroCal =  (ImageView) dialog.findViewById(R.id.imgBroCal);
        ImageView imgCancelBro = (ImageView) dialog.findViewById(R.id.imgCancelBro);

        edtBroDOB.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtBroDOB.getText().length() > 0) {
                    edtBroDOB.setError(null);
                }
            }
        });

        imgBroCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c2 = Calendar.getInstance();
                mYear1 = c2.get(Calendar.YEAR);
                mMonth1 = c2.get(Calendar.MONTH);
                mDay1 = c2.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String monthString = String.valueOf(monthOfYear + 1);
                                String dayString = String.valueOf(dayOfMonth);
                                if (monthString.length() == 1)
                                    monthString = "0" + monthString;
                                if (dayOfMonth <= 9)
                                    dayString = "0" + dayOfMonth;

                                edtBroDOB.setText(dayString + "/" + (monthString) + "/" + year);
                                strBroAge = year +"/"+ monthString + "/" + dayString;

                            }
                        }, mYear1, mMonth1, mDay1);
                datePickerDialog.show();
            }
        });

        imgCancelBro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtBroID.setText("");
                edtBroName.setText("");
                edtBroMobile.setText("");
                edtBroDOB.setText("");
            }
        });

        imgBroSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, SelectMemberIDActivity.class);
                i.putExtra(KEY_GROUPS_ID, "");
                i.putExtra("callingFrom", "Brother");
                startActivityForResult(i,2121);
                overridePendingTransition(R.anim.slide_up, R.anim.stay);
            }
        });

        Button btnAdd = (Button) dialog.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getAge(edtBroDOB.getText().toString())<18){
                    DialogUtil.alertWithOkButton(context,"Brother below 18 should be added as dependent.");
                }else {
                    boolean error = false;
                    final String regexStr = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}$";
                    if (edtBroID.getText().toString().equalsIgnoreCase("")
                            && (!edtBroName.getText().toString().equalsIgnoreCase("")
                            && !edtBroDOB.getText().toString().equalsIgnoreCase("")
                            && !edtBroMobile.getText().toString().equalsIgnoreCase(""))) {
                        if (edtBroMobile.getText().toString().length() > 0
                                && !edtBroMobile.getText().toString().matches(regexStr)) {
                            edtBroMobile.setError("Please enter valid mobile no.");
                            edtBroMobile.setFocusableInTouchMode(true);
                            edtBroMobile.requestFocus();
                            error = true;
                        } else if (edtBroMobile.getText().toString().length() > 0
                                && edtBroMobile.getText().toString().length() < 10) {
                            edtBroMobile.setError("Please Enter Valid Mobile No");
                            edtBroMobile.setFocusableInTouchMode(true);
                            edtBroMobile.requestFocus();
                            error = true;
                        }
                        else {
                            error = false;
                        }
                    } else if (edtBroID.getText().toString().equalsIgnoreCase("")) {
                        edtBroID.setError("Please select Brother ID");
                        edtBroID.requestFocus();
                        error = true;
                    } else if (edtBroName.getText().toString().equalsIgnoreCase("")) {
                        edtBroName.setError("Please enter Brother name");
                        edtBroName.requestFocus();
                        error = true;
                    } else if (edtBroDOB.getText().toString().equalsIgnoreCase("")) {
                        edtBroDOB.setError("Please select Age");
                        edtBroDOB.requestFocus();
                        error = true;
                    }else if (edtBroMobile.getText().toString().equalsIgnoreCase("")) {
                        edtBroMobile.setError("Please enter mobile number");
                        edtBroMobile.requestFocus();
                        error = true;
                    } else if (edtBroMobile.getText().toString().length() > 0
                            && !edtBroMobile.getText().toString().matches(regexStr)) {
                        edtBroMobile.setError("Please enter valid mobile no.");
                        edtBroMobile.setFocusableInTouchMode(true);
                        edtBroMobile.requestFocus();
                        error = true;
                    } else if (edtBroMobile.getText().toString().length() > 0
                            && edtBroMobile.getText().toString().length() < 10) {
                        edtBroMobile.setError("Please Enter Valid Mobile No");
                        edtBroMobile.setFocusableInTouchMode(true);
                        edtBroMobile.requestFocus();
                        error = true;
                    }

                    if (error != true) {
                        addBrother(view.getContext());
                    }
                }
            }
        });
        dialog.show();
    }
    public void addBrother(final Context ctx) {
        final ProgressDialog progress = new ProgressDialog(ctx);
        progress.setMessage("Please Wait....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

        Map<String, Object> params = new HashMap<>();
        params.put("mobile_no", edtBroMobile.getText().toString());
        params.put("date_of_birth", edtBroDOB.getText().toString());
        params.put("parent_member_id", UserPreference.getInstance(ctx).getPreference().getMemberID());
        params.put("name", edtBroName.getText().toString());

        Log.d("Params", "Testing" + params);

        RequestQueue queue = Volley.newRequestQueue(ctx);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, ADDBROTHER_URL,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject json) {
                        try {
                            Log.d("TAG", "JSON POST Response = " + json.toString());

                            if (!json.getBoolean("error")) {
                                dialog.dismiss();
                                DialogUtil.showToastShort(context,"Brother added");
                                recreate();
                            }else{                                //ProgressDialogView.hideProgressDialog();
                                DialogUtil.alertWithOkButton(ctx, json.getString("message"));
                            }

                            progress.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            progress.dismiss();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG", "Volley Error in Response = " + error.getMessage());
                        progress.dismiss();
                        DialogUtil.alertWithOkButton(ctx, error.getMessage());
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            DialogUtil.alertWithOkButton(ctx, "Network timeout error");
                        } else if (error instanceof AuthFailureError) {
                            DialogUtil.alertWithOkButton(ctx, "There was an authentication failure while performing a Request.");
                        } else if (error instanceof ServerError) {
                            DialogUtil.alertWithOkButton(ctx, "ServerError");
                        } else if (error instanceof NetworkError) {
                            DialogUtil.alertWithOkButton(ctx, "NetworkError");
                        } else if (error instanceof ParseError) {
                            DialogUtil.alertWithOkButton(ctx, "ParseError, Server's response could not be parsed.");
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonRequest);
    }

    public static int getAge(String date) {

        int age = 0;

        SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy");
        try {
            Date date1 = df.parse(date);
            Calendar now = Calendar.getInstance();
            Calendar dob = Calendar.getInstance();
            dob.setTime(date1);
            if (dob.after(now)) {
                throw new IllegalArgumentException("Can't be born in the future");
            }
            int year1 = now.get(Calendar.YEAR);
            int year2 = dob.get(Calendar.YEAR);
            age = year1 - year2;
            int month1 = now.get(Calendar.MONTH);
            int month2 = dob.get(Calendar.MONTH);
            if (month2 > month1) {
                age--;
            } else if (month1 == month2) {
                int day1 = now.get(Calendar.DAY_OF_MONTH);
                int day2 = dob.get(Calendar.DAY_OF_MONTH);
                if (day2 > day1) {
                    age--;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return age;
    }
    public void getMemberProfile() {
        final ProgressDialog progress = new ProgressDialog(ProfileActivity.this);
        progress.setMessage("Please Wait....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

        Map<String, Object> params = new HashMap<>();
        params.put("member_id", UserPreference.getInstance(ProfileActivity.this).getPreference().getMemberID());
        params.put("mobile_no", UserPreference.getInstance(ProfileActivity.this).getPreference().getMobileNo());

        Log.d("Params", "Testing" + params);

        RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, GET_MEMBERPROFILE_URL,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject json) {
                       try {
                           Log.d("TAG", "JSON POST Response = " + json.toString());

                           if (json.toString().contains("data")) {
                                OTPVerificationResponse otpResponse = JSONParser.parseOTPVerificationResponse(json.toString());
                                if (!otpResponse.isStatus()) {

                                    JSONArray j = json.getJSONArray("data");
                                    JSONObject jObj = j.getJSONObject(0);
                                    ImageLoader imageLoader = CustomVolleyRequest.getInstance(ProfileActivity.this).getImageLoader();
                                    if(jObj.getString("imageUrl").trim().equalsIgnoreCase("NULL")){
                                        ;
                                    }else {
                                        if(jObj.getString("imageUrl").trim().startsWith("http://"))
                                            imageLoader.get(jObj.getString("imageUrl"), ImageLoader.getImageListener(imgProfile, R.mipmap.noimage, R.mipmap.profile));
                                        else
                                            imageLoader.get("http://khambhatpragati.com/uploads/member_images/"+jObj.getString("imageUrl"), ImageLoader.getImageListener(imgProfile, R.mipmap.noimage, R.mipmap.profile));
                                    }if(!jObj.getString("memberID").equalsIgnoreCase("null")) {
                                        tvMemno.setText(jObj.getString("memberID"));
                                        edtMemNo.setText(jObj.getString("memberID"));
                                    }
                                    if(!jObj.getString("firstName").equalsIgnoreCase("null")) {
                                        tvFirstName.setText(jObj.getString("firstName"));
                                        edtFirstName.setText(jObj.getString("firstName"));
                                    }
                                    if(!jObj.getString("middleName").equalsIgnoreCase("null")) {
                                        tvMiddleName.setText(jObj.getString("middleName"));
                                        edtMiddleName.setText(jObj.getString("middleName"));
                                    }
                                    if(!jObj.getString("lastName").equalsIgnoreCase("null")) {
                                        tvLastName.setText(jObj.getString("lastName"));
                                        edtLastName.setText(jObj.getString("lastName"));
                                    }
                                    if(!jObj.getString("lastKnownName").equalsIgnoreCase("null")) {
                                        tvKnownLastName.setText(jObj.getString("lastKnownName"));
                                        edtKnownOriginal.setText(jObj.getString("lastKnownName"));
                                    }
                                    if(!jObj.getString("parentMemberId").equalsIgnoreCase("null")) {
                                        tvParentMemno.setText(jObj.getString("parentMemberId"));
                                        edtParentMemNo.setText(jObj.getString("parentMemberId"));
                                    }
                                    if(!jObj.getString("grandFatherName").equalsIgnoreCase("null")) {
                                        tvGrandfather.setText(jObj.getString("grandFatherName"));
                                        edtGrandFatherName.setText(jObj.getString("grandFatherName"));
                                    }
                                    if(!jObj.getString("gender").equalsIgnoreCase("null")) {

                                        if(jObj.getString("gender").equalsIgnoreCase("1")
                                                ||jObj.getString("gender").equalsIgnoreCase("Male")){
                                            rbMale.setChecked(true);
                                            rbFemale.setChecked(false);
                                            strGender = "Male";
                                        }else if(jObj.getString("gender").equalsIgnoreCase("2")
                                                ||jObj.getString("gender").equalsIgnoreCase("FeMale")){
                                            rbMale.setChecked(false);
                                            rbFemale.setChecked(true);
                                            strGender = "Female";
                                        }
                                        tvGender.setText(strGender);
                                    }
                                    if(!jObj.getString("dateOfBirth").equalsIgnoreCase("null")) {
                                        tvDOB.setText(jObj.getString("dateOfBirth"));
                                        edtDOB.setText(jObj.getString("dateOfBirth"));
                                    }
                                    if(!jObj.getString("memberStatus").equalsIgnoreCase("null")) {
                                        if(jObj.getString("memberStatus").equalsIgnoreCase("1")
                                                ||jObj.getString("memberStatus").equalsIgnoreCase("Life-Member")
                                                ||jObj.getString("memberStatus").equalsIgnoreCase("Alive")){
                                            spnMemStatus.setSelection(0);
                                            strMemStatus="Life-Member";
                                        }else if(jObj.getString("memberStatus").equalsIgnoreCase("2")
                                                ||jObj.getString("memberStatus").equalsIgnoreCase("0")
                                                ||jObj.getString("memberStatus").equalsIgnoreCase("Expired")){
                                            spnMemStatus.setSelection(1);
                                            strMemStatus="Expired";
                                        }
                                        tvMemStatus.setText(strMemStatus);
                                    }
                                    if(!jObj.getString("committeeMember").equalsIgnoreCase("null")) {
                                        if(jObj.getString("committeeMember").equalsIgnoreCase("1")
                                                ||jObj.getString("committeeMember").equalsIgnoreCase("Yes")){
                                            rbYes.setChecked(true);
                                            rbNo.setChecked(false);
                                            strMandal="Yes";
                                        }else if(jObj.getString("committeeMember").equalsIgnoreCase("2")
                                                ||jObj.getString("committeeMember").equalsIgnoreCase("No")
                                                ||jObj.getString("committeeMember").equalsIgnoreCase("0")){
                                            rbYes.setChecked(false);
                                            rbNo.setChecked(true);
                                            strMandal="No";
                                        }
                                        tvMandal.setText(strMandal);
                                    }
                                    if(!jObj.getString("bloodGroup").equalsIgnoreCase("null")) {
                                        if(jObj.getString("bloodGroup").equalsIgnoreCase("1")
                                                ||jObj.getString("bloodGroup").equalsIgnoreCase("A+")){
                                            spnBloodGroup.setSelection(0);
                                            strBloodGroup="A+";
                                        }else if(jObj.getString("bloodGroup").equalsIgnoreCase("2")
                                                ||jObj.getString("bloodGroup").equalsIgnoreCase("A-")){
                                            spnBloodGroup.setSelection(1);
                                            strBloodGroup="A-";
                                        }else if(jObj.getString("bloodGroup").equalsIgnoreCase("3")
                                                ||jObj.getString("bloodGroup").equalsIgnoreCase("B+")){
                                            spnBloodGroup.setSelection(2);
                                            strBloodGroup="B+";
                                        }else if(jObj.getString("bloodGroup").equalsIgnoreCase("4")
                                                ||jObj.getString("bloodGroup").equalsIgnoreCase("B-")){
                                            spnBloodGroup.setSelection(3);
                                            strBloodGroup="B-";
                                        }else if(jObj.getString("bloodGroup").equalsIgnoreCase("5")
                                                ||jObj.getString("bloodGroup").equalsIgnoreCase("AB+")){
                                            spnBloodGroup.setSelection(4);
                                            strBloodGroup="AB+";
                                        }else if(jObj.getString("bloodGroup").equalsIgnoreCase("6")
                                                ||jObj.getString("bloodGroup").equalsIgnoreCase("AB-")){
                                            spnBloodGroup.setSelection(5);
                                            strBloodGroup="AB-";
                                        }else if(jObj.getString("bloodGroup").equalsIgnoreCase("7")
                                                ||jObj.getString("bloodGroup").equalsIgnoreCase("O+")){
                                            spnBloodGroup.setSelection(6);
                                            strBloodGroup="O+";
                                        }else if(jObj.getString("bloodGroup").equalsIgnoreCase("8")
                                                ||jObj.getString("bloodGroup").equalsIgnoreCase("O-")){
                                            spnBloodGroup.setSelection(7);
                                            strBloodGroup="O-";
                                        }
                                        tvBloodGroup.setText(strBloodGroup);
                                    }
                                    if(!jObj.getString("maritalStatus").equalsIgnoreCase("null")) {
                                        if(jObj.getString("maritalStatus").equalsIgnoreCase("1")
                                                ||jObj.getString("maritalStatus").equalsIgnoreCase("Married")){
                                            spnMarritalStatus.setSelection(0);
                                            strMarritalStatus="Married";
                                        }else if(jObj.getString("maritalStatus").equalsIgnoreCase("2")
                                                || jObj.getString("maritalStatus").equalsIgnoreCase("0")
                                                ||jObj.getString("maritalStatus").equalsIgnoreCase("UnMarried")){
                                            spnMarritalStatus.setSelection(1);
                                            strMarritalStatus="Unmarried";
                                        }
                                        tvMarritalStatus.setText(strMarritalStatus);
                                    }
                                    if(!jObj.getString("qualification").equalsIgnoreCase("null")) {
                                        if(jObj.getString("qualification").equalsIgnoreCase("1")
                                                ||jObj.getString("qualification").equalsIgnoreCase("Student")){
                                            spnQualification.setSelection(0);
                                            strQualification="Student";
                                        }else if(jObj.getString("qualification").equalsIgnoreCase("2")
                                                ||jObj.getString("qualification").equalsIgnoreCase("10th")){
                                            spnQualification.setSelection(1);
                                            strQualification="10th";
                                        }else if(jObj.getString("qualification").equalsIgnoreCase("3")
                                                ||jObj.getString("qualification").equalsIgnoreCase("12th")){
                                            spnQualification.setSelection(2);
                                            strQualification="12th";
                                        }else if(jObj.getString("qualification").equalsIgnoreCase("4")
                                                ||jObj.getString("qualification").contains("Diploma")){
                                            spnQualification.setSelection(3);
                                            strQualification="Diploma";
                                        }else if(jObj.getString("qualification").equalsIgnoreCase("5")
                                                ||jObj.getString("qualification").contains("Graduate")){
                                            spnQualification.setSelection(4);
                                            rgGraduate.setVisibility(View.VISIBLE);
                                            edtOthers.setVisibility(View.GONE);
                                            String strServices = jObj.getString("qualification").substring(jObj.getString("qualification").indexOf("-")+1,
                                                    jObj.getString("qualification").length());
                                            if(strServices.trim().equalsIgnoreCase("Science")) {
                                                rbScience.setChecked(true);
                                                rbArts.setChecked(false);
                                                rbCommerce.setChecked(false);
                                            } else if(strServices.trim().equalsIgnoreCase("Arts")) {
                                                rbArts.setChecked(true);
                                                rbCommerce.setChecked(false);
                                                rbScience.setChecked(false);
                                            } else if(strServices.trim().equalsIgnoreCase("Commerce")) {
                                                rbCommerce.setChecked(true);
                                                rbScience.setChecked(false);
                                                rbArts.setChecked(false);
                                            }
                                            strQualification="Graduate - "+strServices.trim();
                                        }else if(jObj.getString("qualification").equalsIgnoreCase("6")
                                                ||jObj.getString("qualification").contains("Post-Graduate")
                                                ||jObj.getString("qualification").contains("PostGraduate")
                                                ||jObj.getString("qualification").contains("Post Graduate")){
                                            spnQualification.setSelection(5);
                                            edtPostGraduate.setVisibility(View.VISIBLE);
                                            rgGraduate.setVisibility(View.GONE);

                                            edtPostGraduate.setText(jObj.getString("qualification").substring(jObj.getString("qualification").indexOf("-")+1,
                                                    jObj.getString("qualification").length()));

                                            strQualification="Post-Graduate - "+edtPostGraduate.getText().toString();

                                        }else if(jObj.getString("qualification").equalsIgnoreCase("7")
                                                ||jObj.getString("qualification").equalsIgnoreCase("Professional")){
                                            spnQualification.setSelection(6);
                                            strQualification="Professional";
                                        }
                                        tvQualification.setText(strQualification);
                                    }
                                    if(!jObj.getString("profession").equalsIgnoreCase("null")) {
                                        if(jObj.getString("profession").equalsIgnoreCase("1")
                                                ||jObj.getString("profession").equalsIgnoreCase("Doctor")){
                                            spnBusiness.setSelection(0);
                                            strBusiness="Doctor";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("2")
                                                ||jObj.getString("profession").equalsIgnoreCase("Lawyer")){
                                            spnBusiness.setSelection(1);
                                            strBusiness="Lawyer";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("3")
                                                ||jObj.getString("profession").equalsIgnoreCase("Architects")){
                                            spnBusiness.setSelection(2);
                                            strBusiness="Architects";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("4")
                                                ||jObj.getString("profession").equalsIgnoreCase("CA/CS/ICWA")){
                                            spnBusiness.setSelection(3);
                                            strBusiness="CA/CS/ICWA";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("5")
                                                ||jObj.getString("profession").equalsIgnoreCase("MBA")){
                                            spnBusiness.setSelection(4);
                                            strBusiness="MBA";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("6")
                                                ||jObj.getString("profession").equalsIgnoreCase("Teacher/Professor")){
                                            spnBusiness.setSelection(5);
                                            strBusiness="Teacher/Professor";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("7")
                                                ||jObj.getString("profession").equalsIgnoreCase("Interior Designer")){
                                            spnBusiness.setSelection(6);
                                            strBusiness="Interior Designer";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("8")
                                                ||jObj.getString("profession").equalsIgnoreCase("Fashion Designer")){
                                            spnBusiness.setSelection(7);
                                            strBusiness="Fashion Designer";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("9")
                                                ||jObj.getString("profession").contains("Engineer")){
                                            spnBusiness.setSelection(8);
                                            edtEngineers.setVisibility(View.VISIBLE);
                                            rgServices.setVisibility(View.GONE);
                                            edtOthers.setVisibility(View.GONE);
                                            rgBusiness.setVisibility(View.GONE);

                                            edtEngineers.setText(jObj.getString("profession").substring(jObj.getString("profession").indexOf("-")+1,
                                                    jObj.getString("profession").length()));
                                            strBusiness="Engineer - "+edtEngineers.getText().toString();
                                        }else if(jObj.getString("profession").equalsIgnoreCase("10")
                                                ||jObj.getString("profession").equalsIgnoreCase("Civil engineer")){
                                            spnBusiness.setSelection(9);
                                            strBusiness="Civil engineer";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("11")
                                                ||jObj.getString("profession").contains("Business")){
                                            spnBusiness.setSelection(10);

                                            rgBusiness.setVisibility(View.VISIBLE);
                                            rgServices.setVisibility(View.GONE);
                                            edtOthers.setVisibility(View.GONE);
                                            edtEngineers.setVisibility(View.GONE);
                                            String strServices = jObj.getString("profession").substring(jObj.getString("profession").indexOf("-")+1,
                                                    jObj.getString("profession").length());
                                            if(strServices.trim().equalsIgnoreCase("Trading / Stockist")) {
                                                rbStokist.setChecked(true);
                                                rbManufacturing.setChecked(false);
                                            }
                                            else if(strServices.trim().equalsIgnoreCase("Manufacturing")) {
                                                rbManufacturing.setChecked(true);
                                                rbStokist.setChecked(false);
                                            }
                                            strBusiness="Business - "+strServices.trim();
                                        }else if(jObj.getString("profession").equalsIgnoreCase("12")
                                                ||jObj.getString("profession").contains("Service")){
                                            spnBusiness.setSelection(11);
                                            rgServices.setVisibility(View.VISIBLE);
                                            rgBusiness.setVisibility(View.GONE);
                                            edtOthers.setVisibility(View.GONE);
                                            edtEngineers.setVisibility(View.GONE);

                                            String strServices = jObj.getString("profession").substring(jObj.getString("profession").indexOf("-")+1,
                                                    jObj.getString("profession").length());
                                            if(strServices.trim().equalsIgnoreCase("Public")) {
                                                rbPublic.setChecked(true);
                                                rbPrivate.setChecked(false);
                                            }
                                            else if(strServices.trim().equalsIgnoreCase("Private")) {
                                                rbPrivate.setChecked(true);
                                                rbPublic.setChecked(false);
                                            }
                                            strBusiness="Service - "+strServices.trim();
                                        }else if(jObj.getString("profession").equalsIgnoreCase("13")
                                                ||jObj.getString("profession").equalsIgnoreCase("Housewife")){
                                            spnBusiness.setSelection(12);
                                            strBusiness="Housewife";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("14")
                                                ||jObj.getString("profession").equalsIgnoreCase("Retired")){
                                            spnBusiness.setSelection(13);
                                            strBusiness="Retired";
                                        }else if(jObj.getString("profession").equalsIgnoreCase("15")
                                                ||jObj.getString("profession").contains("Other -")){
                                            spnBusiness.setSelection(14);
                                            edtOthers.setVisibility(View.VISIBLE);
                                            rgServices.setVisibility(View.GONE);
                                            rgBusiness.setVisibility(View.GONE);
                                            edtEngineers.setVisibility(View.GONE);

                                            edtOthers.setText(jObj.getString("profession").substring(jObj.getString("profession").indexOf("-")+1,
                                                    jObj.getString("profession").length()));
                                            strBusiness="other - "+edtOthers.getText().toString();
                                        }
                                        tvProfession.setText(strBusiness);
                                    }
                                    if(!jObj.getString("flatNoBuilding").equalsIgnoreCase("null")) {
                                        tvFlatNo.setText(jObj.getString("flatNoBuilding"));
                                        edtFlatNoBuilding.setText(jObj.getString("flatNoBuilding"));
                                    }
                                    if(!jObj.getString("residenceArea").equalsIgnoreCase("null")) {
                                        tvResidenceArea.setText(jObj.getString("residenceArea"));
                                        edtResidenseArea.setText(jObj.getString("residenceArea"));
                                    }
                                    if(!jObj.getString("nearestStation").equalsIgnoreCase("null")) {
                                        tvNearestStn.setText(jObj.getString("nearestStation"));
                                        edtNearestStation.setText(jObj.getString("nearestStation"));
                                    }
                                    if(!jObj.getString("city").equalsIgnoreCase("null")) {
                                        tvCity.setText(jObj.getString("city"));
                                        edtCity.setText(jObj.getString("city"));
                                    }
                                    if(!jObj.getString("pincode").equalsIgnoreCase("null")) {
                                        tvPicode.setText(jObj.getString("pincode"));
                                        edtPincode.setText(jObj.getString("pincode"));
                                    }
                                    if(!jObj.getString("khambhatAddress").equalsIgnoreCase("null")) {
                                        tvKhambat.setText(jObj.getString("khambhatAddress"));
                                        edtKhambatAddress.setText(jObj.getString("khambhatAddress"));
                                    }
                                    if(!jObj.getString("mobileNo").equalsIgnoreCase("null")) {
                                        tvMobile.setText(jObj.getString("mobileNo"));
                                        edtMobile.setText(jObj.getString("mobileNo"));
                                    }
                                    if(!jObj.getString("email").equalsIgnoreCase("null")) {
                                        tvEmail.setText(jObj.getString("email"));
                                        edtEmail.setText(jObj.getString("email"));
                                    }
                                    if(!jObj.getString("membershipStatus").equalsIgnoreCase("null")) {
                                        if(jObj.getString("membershipStatus").equalsIgnoreCase("1")
                                                ||jObj.getString("membershipStatus").equalsIgnoreCase("Active")){
                                            spnMembershipStatus.setSelection(0);
                                            strMembershipStatus="Active";
                                        }else if(jObj.getString("membershipStatus").equalsIgnoreCase("0")
                                                ||jObj.getString("membershipStatus").equalsIgnoreCase("2")
                                                ||jObj.getString("membershipStatus").equalsIgnoreCase("InActive")
                                                ||jObj.getString("membershipStatus").equalsIgnoreCase("In Active")
                                                ||jObj.getString("membershipStatus").equalsIgnoreCase("In-Active")){
                                            spnMembershipStatus.setSelection(1);
                                            strMembershipStatus="In-Active";
                                        }
                                        tvMembershipStatus.setText(strMembershipStatus);
                                    }
                                    JSONArray jBro = jObj.getJSONArray("brotherInfo");
                                    String strBrotherList="";
                                    BrotherModelList.clear();
                                    for(int i=0;i<jBro.length();i++){
                                        if(strBrotherList.equalsIgnoreCase(""))
                                            strBrotherList=jBro.getJSONObject(i).getString("name") +
                                                    " - " + jBro.getJSONObject(i).getString("brother_id");
                                        else
                                            strBrotherList += " , "+jBro.getJSONObject(i).getString("name") +
                                                    " - " + jBro.getJSONObject(i).getString("brother_id");

                                        BrotherModel b = new BrotherModel();
                                        b.setRecord_no(jBro.getJSONObject(i).getString("record_no"));
                                        b.setParent_member_id(jBro.getJSONObject(i).getString("parent_member_id"));
                                        b.setName(jBro.getJSONObject(i).getString("name"));
                                        b.setDate_of_birth(jBro.getJSONObject(i).getString("date_of_birth"));
                                        b.setMobile_no(jBro.getJSONObject(i).getString("mobile_no"));
                                        b.setBrother_id(jBro.getJSONObject(i).getString("brother_id"));
                                        BrotherModelList.add(b);
                                    }

                                    adapter.notifyDataSetChanged();
                                    recyclerBrother.getAdapter().notifyDataSetChanged();
                                    initializeAdapterSelf();
                                    tvBrotherlist.setText(strBrotherList);
                                } else {
                                    //ProgressDialogView.hideProgressDialog();
                                    DialogUtil.showSnackBarWithAction(relativeLayout, otpResponse.getMessage());
                                }
                            }
                            progress.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            progress.dismiss();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG", "Volley Error in Response = " + error.getMessage());
                        progress.dismiss();
                        DialogUtil.showSnackBarWithAction(relativeLayout, error.getMessage());
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            DialogUtil.showSnackBarWithAction(relativeLayout, "Network timeout error");
                        } else if (error instanceof AuthFailureError) {
                            DialogUtil.showSnackBarWithAction(relativeLayout, "There was an authentication failure while performing a Request.");
                        } else if (error instanceof ServerError) {
                            DialogUtil.showSnackBarWithAction(relativeLayout, "ServerError");
                        } else if (error instanceof NetworkError) {
                            DialogUtil.showSnackBarWithAction(relativeLayout, "NetworkError");
                        } else if (error instanceof ParseError) {
                            DialogUtil.showSnackBarWithAction(relativeLayout, "ParseError, Server's response could not be parsed.");
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonRequest);
    }

    public void updateMember() {
        final ProgressDialog progress = new ProgressDialog(ProfileActivity.this);
        progress.setMessage("Please Wait....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

        Map<String, Object> params = new HashMap<>();
        params.put("first_name", edtFirstName.getText().toString());
        params.put("last_name", edtLastName.getText().toString());
        params.put("middle_name", edtMiddleName.getText().toString());
        params.put("last_known_name", edtKnownOriginal.getText().toString());
        params.put("parent_member_id", edtParentMemNo.getText().toString());
        params.put("grand_father_name", edtGrandFatherName.getText().toString());
        params.put("gender", strGender);
        params.put("date_of_birth", edtDOB.getText().toString());

        if(strMandal.equalsIgnoreCase("Yes"))
            params.put("is_committee_member", "1");
        else
            params.put("is_committee_member", "0");

        if(strMemStatus.equalsIgnoreCase("Alive")
            || strMemStatus.equalsIgnoreCase("Life-Member"))
            params.put("member_status", "1");
        else
            params.put("member_status", "0");

        if(strMembershipStatus.equalsIgnoreCase("Active"))
            params.put("membership_status", "1");
        else
            params.put("membership_status", "0");

        if(strMarritalStatus.equalsIgnoreCase("Married"))
            params.put("marital_status", "1");
        else
            params.put("marital_status", "0");

        params.put("blood_group", strBloodGroup);

        if(edtPostGraduate.getText().toString().equalsIgnoreCase(""))
            params.put("qualification", strQualification);
        else
            params.put("qualification", "Post-Graduate - " +edtPostGraduate.getText().toString());
        if(strQualification.equalsIgnoreCase("Student"))
            params.put("profession", "");
        else {
            if (edtOthers.getText().toString().equalsIgnoreCase(""))
                params.put("profession", strBusiness);
            else
                params.put("profession", "Others - " + edtOthers.getText().toString());

            if (edtEngineers.getText().toString().equalsIgnoreCase(""))
                params.put("profession", strBusiness);
            else
                params.put("profession", "Engineer - " + edtEngineers.getText().toString());
        }
        params.put("flat_no_building", edtFlatNoBuilding.getText().toString());
        params.put("residence_area", edtResidenseArea.getText().toString());
        params.put("nearest_station", edtNearestStation.getText().toString());
        params.put("residence_zone", "-");
        params.put("city", edtCity.getText().toString());
        params.put("pincode", edtPincode.getText().toString());
        params.put("kambhat_address", edtKhambatAddress.getText().toString());
        params.put("record_no", UserPreference.getInstance(ProfileActivity.this).getPreference().getRecordId());
        params.put("mobile_no", edtMobile.getText().toString());
        params.put("email", edtEmail.getText().toString());

        Log.d("Params", "Testing" + params);

        RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, UPDATEMEMBER_URL,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject json) {
                        try {
                            Log.d("TAG", "JSON POST Response = " + json.toString());

                            if (json.toString().contains("data")) {
                                if (Path != null) {
                                    float size = getImageSize(context,Uri.parse(Path)) / 1024;
                                    if(size <= 1){
                                        if(size<=-1)
                                        DialogUtil.alertWithOkButton(context,"Invalid Image!!");
                                        else
                                            updateImage();
                                    } else
                                        DialogUtil.alertWithOkButton(context,"Please select image or lower size upto 1MB");

                                } else {
                                    recreate();
                                }
                            } else {
                                DialogUtil.showSnackBarWithAction(relativeLayout, "Error in updateMember");
                            }
                            progress.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            progress.dismiss();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG", "Volley Error in Response = " + error.getMessage());
                        progress.dismiss();
                        DialogUtil.showSnackBarWithAction(relativeLayout, error.getMessage());
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            DialogUtil.showSnackBarWithAction(relativeLayout, "Network timeout error");
                        } else if (error instanceof AuthFailureError) {
                            DialogUtil.showSnackBarWithAction(relativeLayout, "There was an authentication failure while performing a Request.");
                        } else if (error instanceof ServerError) {
                            DialogUtil.showSnackBarWithAction(relativeLayout, "ServerError");
                        } else if (error instanceof NetworkError) {
                            DialogUtil.showSnackBarWithAction(relativeLayout, "NetworkError");
                        } else if (error instanceof ParseError) {
                            DialogUtil.showSnackBarWithAction(relativeLayout, "ParseError, Server's response could not be parsed.");
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonRequest);
    }
    public static long getImageSize(Context context, Uri uri) {
        /*Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
            cursor.moveToFirst();
            float imageSize = cursor.getLong(sizeIndex);
            cursor.close();
            return imageSize; // returns size in bytes
        }*/
        File file = new File(uri.getPath());
        long length = file.length();
        length = length/1024;
        System.out.println("File Path : " + file.getPath() + ", File size : " + length +" KB");
        return length;
    }
    public void updateImage() {
        final ProgressDialog progress = new ProgressDialog(ProfileActivity.this);
        progress.setMessage("Please Wait....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bm = BitmapFactory.decodeFile(Path);
            if (bm != null) {
                bm.compress(Bitmap.CompressFormat.JPEG, 75, bos);
                byte[] data = bos.toByteArray();
                ba1 = Base64.encodeToString(data, Base64.DEFAULT);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, Object> params = new HashMap<>();
        params.put("record_no", UserPreference.getInstance(ProfileActivity.this).getPreference().getRecordId());
        params.put("is_dependent", "no");
        params.put("member_id", UserPreference.getInstance(ProfileActivity.this).getPreference().getMemberID());
        if (Path != null) {
            params.put("photo_link", ba1);
        } else {
            params.put("photo_link", "");
        }

        Log.d("Params", "Testing" + params);

        RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, ADDMEMBERIMAGE_URL,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject json) {
                        try {
                            Log.d("TAG", "JSON POST Response = " + json.toString());

                            if (json.toString().contains("data")) {
                                recreate();
                            } else {
                                //ProgressDialogView.hideProgressDialog();
                                DialogUtil.showSnackBarWithAction(relativeLayout, "Error in addImage");
                            }
                            progress.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            progress.dismiss();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG", "Volley Error in Response = " + error.getMessage());
                        progress.dismiss();
                        DialogUtil.showSnackBarWithAction(relativeLayout, error.getMessage());
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            DialogUtil.showSnackBarWithAction(relativeLayout, "Network timeout error");
                        } else if (error instanceof AuthFailureError) {
                            DialogUtil.showSnackBarWithAction(relativeLayout, "There was an authentication failure while performing a Request.");
                        } else if (error instanceof ServerError) {
                            DialogUtil.showSnackBarWithAction(relativeLayout, "ServerError");
                        } else if (error instanceof NetworkError) {
                            DialogUtil.showSnackBarWithAction(relativeLayout, "NetworkError");
                        } else if (error instanceof ParseError) {
                            DialogUtil.showSnackBarWithAction(relativeLayout, "ParseError, Server's response could not be parsed.");
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonRequest);
    }

    private void initializeAdapterSelf(){
        adapter = new BrotherAdapter(BrotherModelList, ProfileActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerBrother.setLayoutManager(mLayoutManager);
        recyclerBrother.setItemAnimator(new DefaultItemAnimator());
        recyclerBrother.setAdapter(adapter);
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private boolean requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                && ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        } else {
            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(ProfileActivity.this, "Access Denied, App may not run properly./अनुमतियां अस्वीकृत, ऐप ठीक से नहीं चलेंगे", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        //Path = "";
        //Path = "file:" + image.getAbsolutePath();
        Path = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Log.w(">>>>>>>>>>>>>>", "on activity Result");
        Log.w(">>>>>>>>>>>>>>", "requestCode" + requestCode);
        if (requestCode == 2020) {
            if(resultCode == RESULT_OK) {
                if(data.hasExtra("pID")){
                    edtParentMemNo.setText(data.getStringExtra("pID"));
                }
            }
        }
        if (requestCode == 2121) {
            if(resultCode == RESULT_OK) {
                if(data.hasExtra("bName")){
                    edtBroID.setText(data.getStringExtra("bID"));
                    edtBroMobile.setText(data.getStringExtra("bMobile"));
                    edtBroName.setText(data.getStringExtra("bName"));

                    edtBroID.setEnabled(false);
                    edtBroID.setFocusable(false);
                    edtBroID.setClickable(false);

                    edtBroMobile.setEnabled(false);
                    edtBroMobile.setFocusable(false);
                    edtBroMobile.setClickable(false);

                    edtBroName.setEnabled(false);
                    edtBroName.setFocusable(false);
                    edtBroName.setClickable(false);
                }
            }
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                try {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                        Uri imageUri = Uri.parse(Path);
                        File file = new File(imageUri.getPath());
                        FileInputStream in = new FileInputStream(file);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 1;
                        //Path = f.getAbsolutePath();

                        Log.w("##############", Path);
                        Bitmap bmp = BitmapFactory.decodeStream(in, null, options);
                        imgProfile.setImageBitmap(bmp);
                        //imgProfile.setImageBitmap(ExifUtils.rotateBitmap(Path, decodeSampledBitmap(new File(Path), 200, 200)));

                        imgProfile.setEnabled(true);
                    } else {
                        FileInputStream in = new FileInputStream(f);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 1;
                        Path = f.getAbsolutePath();

                        Log.w("##############", Path);
                        Bitmap bmp = BitmapFactory.decodeStream(in, null, options);
                        //imgProfile.setImageBitmap(ExifUtils.rotateBitmap(Path, decodeSampledBitmap(new File(Path), 200, 200)));

                        imgProfile.setImageBitmap(bmp);
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
            if (requestCode == 2) {
                Log.w(">>>>>>>>>>>>>>", "on result if condition");
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = context.getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                //Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));//todo
                File image = new File(picturePath);
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inSampleSize = 1;
                Bitmap thumbnail = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
                Path = "null";
                Path = picturePath;

                Log.w("imagepath->gallery", picturePath + "");
                imgProfile.setImageBitmap(thumbnail);
                //imgProfile.setImageBitmap(ExifUtils.rotateBitmap(Path, decodeSampledBitmap(new File(Path), 200, 200)));

                imgProfile.setEnabled(true);

            }
        }
    }
    public Bitmap decodeSampledBitmap(File res, int reqWidth, int reqHeight) {
        if (res != null) {
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            try {
                FileInputStream stream2 = new FileInputStream(res);

                BitmapFactory.decodeStream(stream2, null, options);

                stream2.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Calculate inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            o2.inJustDecodeBounds = false;
            FileInputStream stream = null;
            try {
                stream = new FileInputStream(res);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeStream(stream, null, o2);
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        } else
            return null;
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}