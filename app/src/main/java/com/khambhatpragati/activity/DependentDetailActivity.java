package com.khambhatpragati.activity;

import android.Manifest;
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
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.khambhatpragati.model.DependentModel;
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
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.khambhatpragati.activity.DashboardActivity.adCounter;
import static com.khambhatpragati.activity.DashboardActivity.adsImageUrls;
import static com.khambhatpragati.activity.DashboardActivity.isAdsTicking;
import static com.khambhatpragati.activity.DashboardActivity.isShownOnce;
import static com.khambhatpragati.constants.Keys.KEY_GROUPS_ID;
import static com.khambhatpragati.constants.URLs.ADDBROTHER_URL;
import static com.khambhatpragati.constants.URLs.ADDDEPENDENT_URL;
import static com.khambhatpragati.constants.URLs.ADDMEMBERIMAGE_URL;
import static com.khambhatpragati.constants.URLs.DELETEDEPENDENT_URL;
import static com.khambhatpragati.constants.URLs.GET_MEMBERPROFILE_URL;
import static com.khambhatpragati.constants.URLs.UPDATEDEPENDENT_URL;
import static com.khambhatpragati.constants.URLs.UPDATEMEMBER_URL;

public class DependentDetailActivity extends AppCompatActivity {

    Context context;
    private int mYear, mMonth, mDay;
    private int mYear1, mMonth1, mDay1;
    Calendar c1,c2;

    String strRelation="",strBloodGroup="",strMarritalStatus="",strQualification="",strMembershipStatus="",
            strBusiness="",strGender="",strBirthDate="";
    Spinner spnRelation,spnBloodGroup,spnMarritalStatus,spnQualification,spnMembershipStatus,spnBusiness;
    List<SpinnerModel> listRelation,listBloodGroup,listMarritalStatus,listQualification,listMembershipStatus,listBusiness;
    ArrayAdapter<SpinnerModel> arrayAdapterRelation, arrayAdapterBloodGroup,arrayAdapterMarritalStatus,
            arrayAdapterQualification,arrayAdapterMembershipStatus,arrayAdapterBusiness;
    EditText edtMemNo,edtFirstName, edtDOB,edtMobile,edtEmail,edtPostGraduate,edtOthers,edtHusbandDesc,edtEngineers;
    TextView tvMemno,tvFirstName,tvRelation,tvGender,txtID,
            tvDOB,tvBloodGroup,tvMarritalStatus,tvMembershipStatus,tvQualification,tvProfession,txtEmailHeading,
            tvHusbandID,tvHusbandName,tvHusbandDesc,tvHusbandQualification, tvMobile,tvEmail,txtMobileHeading;
    Button btnUpdate;
    EditText edtHusbandName;
    EditText edtHusbandQualification;
    EditText edtHusbandID;
    RelativeLayout relativeLayout;
    RadioGroup rgGender,rgGraduate,rgBusiness,rgServices;
    RadioButton rbMale,rbFemale,rbCommerce,rbArts,rbScience,rbManufacturing,rbStokist,rbPublic,rbPrivate;
    ImageView btnImageUpload,imgProfile,imgCalDOB,imgSearchHusband,imgCancelHusband;
    LinearLayout imgDelete,imgEdit,lvView,lvEdit,lvHusband,lvHusbandHeading,lvHusbandDetail;
    private Toolbar toolbar;
    File f;
    String Path;
    private static final int PERMISSION_REQUEST_CODE = 1;
    Bitmap bm;
    String ba1;
    DependentModel dependent;
    private ImageLoader imageLoader;
    ImageView imageView;
    private String strAdWeblink="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dependent);
        context = DependentDetailActivity.this;
        initToolBar();
        Intent i = getIntent();
        if(!getIntent().getStringExtra("From").equalsIgnoreCase("Add"))
            dependent = (DependentModel)i.getSerializableExtra("dependent_detail");
        boolean isPermission = checkPermission();
        if (isPermission == true) {
        } else {
            requestPermission();
        }
        initView();
    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add Dependent");
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

        relativeLayout = findViewById(R.id.relativeLayout);
        tvMemno = findViewById(R.id.tvMemNo);
        tvFirstName = findViewById(R.id.tvFirstName);
        tvGender = findViewById(R.id.tvGender);
        txtID = findViewById(R.id.txtID);
        tvDOB = findViewById(R.id.tvDOB);
        tvRelation = findViewById(R.id.tvRelation);
        tvBloodGroup = findViewById(R.id.tvBloodGroup);
        tvMarritalStatus = findViewById(R.id.tvMarrital);
        tvMembershipStatus = findViewById(R.id.tvMembershipStatus);
        tvQualification = findViewById(R.id.tvQualification);
        tvProfession = findViewById(R.id.tvBusiness);
        tvMobile = findViewById(R.id.tvMobile);
        tvHusbandQualification = findViewById(R.id.tvHusbandQualification);
        tvHusbandName = findViewById(R.id.tvHusbandName);
        tvHusbandID = findViewById(R.id.tvHusbandID);
        tvHusbandDesc = findViewById(R.id.tvHusbandDesc);
        tvEmail = findViewById(R.id.tvEmail);
        txtEmailHeading = findViewById(R.id.txtEmailHeading);
        txtMobileHeading = findViewById(R.id.txtMobileHeading);

        edtMemNo = findViewById(R.id.edtMemNo);
        edtFirstName = findViewById(R.id.edtFirstName);
        edtDOB = findViewById(R.id.edtDOB);
        edtMobile = findViewById(R.id.edtMobile);
        edtEmail = findViewById(R.id.edtEmail);
        edtPostGraduate = findViewById(R.id.edtPostGraduate);
        edtOthers = findViewById(R.id.edtOthers);
        edtHusbandID = findViewById(R.id.edtHusbandMemNo);
        edtHusbandName = findViewById(R.id.edtHusbandName);
        edtHusbandDesc = findViewById(R.id.edtHusbandDesc);
        edtHusbandQualification = findViewById(R.id.edtHusbandQualification);

        edtEngineers = findViewById(R.id.edtEngineers);

        rgServices = findViewById(R.id.rgServices);
        rbPrivate = findViewById(R.id.radioPrivate);
        rbPublic = findViewById(R.id.radioPublic);

        rgGender = findViewById(R.id.rgGender);
        rgGraduate = findViewById(R.id.rgGraduate);
        rgBusiness = findViewById(R.id.rgBusiness);

        rbMale = findViewById(R.id.radioMale);
        rbFemale = findViewById(R.id.radioFeMale);
        rbCommerce = findViewById(R.id.radioCommerce);
        rbArts = findViewById(R.id.radioArts);
        rbScience = findViewById(R.id.radioScience);
        rbManufacturing = findViewById(R.id.radioManufacturing);
        rbStokist = findViewById(R.id.radioStokist);

        btnImageUpload = findViewById(R.id.btnImageUpload);
        imgProfile = findViewById(R.id.imgProfile);
        imgCancelHusband = findViewById(R.id.imgCancelHusband);
        imgCalDOB = findViewById(R.id.imgCal);

        imgDelete = findViewById(R.id.imgDelete);
        imgEdit = findViewById(R.id.imgEdit);

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
                if(getAge(edtDOB.getText().toString())<12){
                    edtEmail.setVisibility(View.GONE);
                    edtMobile.setVisibility(View.GONE);
                    txtMobileHeading.setVisibility(View.GONE);
                    txtEmailHeading.setVisibility(View.GONE);
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

        imgCancelHusband.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtHusbandID.setText("");
                edtHusbandName.setText("");
                edtHusbandDesc.setText("");
                edtHusbandQualification.setText("");
            }
        });

        btnUpdate = findViewById(R.id.btnOk);
        lvView = findViewById(R.id.viewLV);
        lvEdit = findViewById(R.id.editLV);
        lvHusband = findViewById(R.id.husbandLV);
        lvHusbandHeading = findViewById(R.id.lvHusbandHeading);
        lvHusbandDetail = findViewById(R.id.lvHusbandDetail);
        imgSearchHusband = findViewById(R.id.imgSearchHusband);
        imgSearchHusband.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DependentDetailActivity.this, SelectMemberIDActivity.class);
                i.putExtra(KEY_GROUPS_ID, "");
                i.putExtra("callingFrom", "Husband");
                startActivityForResult(i,3030);
                overridePendingTransition(R.anim.slide_up, R.anim.stay);
            }
        });

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDependent();
            }
        });
        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvView.setVisibility(View.GONE);
                imgEdit.setVisibility(View.GONE);
                btnImageUpload.setVisibility(View.VISIBLE);
                imgProfile.setClickable(true);
                btnUpdate.setVisibility(View.VISIBLE);
                lvEdit.setVisibility(View.VISIBLE);
            }
        });
        spnRelation = (Spinner) findViewById(R.id.spnRelation);
        listRelation = new ArrayList<>();
        listRelation.add(new SpinnerModel("1","Son"));
        listRelation.add(new SpinnerModel("2","Daughter"));
        listRelation.add(new SpinnerModel("3","Wife"));
        listRelation.add(new SpinnerModel("4","Brother"));
        listRelation.add(new SpinnerModel("5","Sister"));
        arrayAdapterRelation = new ArrayAdapter<SpinnerModel>(context, android.R.layout.simple_spinner_item, listRelation) {
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
        arrayAdapterRelation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnRelation.setAdapter(arrayAdapterRelation);
        spnRelation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strRelation = listRelation.get(position).toString();

                if((strRelation.equalsIgnoreCase("Daughter") || strRelation.equalsIgnoreCase("Sister"))
                        && (strMarritalStatus.equalsIgnoreCase("Married"))) {
                    lvHusband.setVisibility(View.VISIBLE);
                }else{
                    lvHusband.setVisibility(View.GONE);
                }
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
                if((strRelation.equalsIgnoreCase("Daughter") || strRelation.equalsIgnoreCase("Sister"))
                        && (strMarritalStatus.equalsIgnoreCase("Married"))) {
                    lvHusband.setVisibility(View.VISIBLE);
                }else{
                    lvHusband.setVisibility(View.GONE);
                }
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
                    edtPostGraduate.setVisibility(View.GONE);
                    edtEngineers.setVisibility(View.GONE);
                    edtOthers.setVisibility(View.GONE);
                    rgBusiness.setVisibility(View.GONE);
                    rgServices.setVisibility(View.GONE);
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

        if(getIntent().getStringExtra("From").equalsIgnoreCase("Add")){
            lvView.setVisibility(View.GONE);
            imgEdit.setVisibility(View.GONE);
            imgDelete.setVisibility(View.GONE);
            edtMemNo.setVisibility(View.GONE);
            txtID.setVisibility(View.GONE);
            btnImageUpload.setVisibility(View.VISIBLE);
            imgProfile.setClickable(true);
            btnUpdate.setVisibility(View.VISIBLE);
            lvEdit.setVisibility(View.VISIBLE);
        }else {
            lvView.setVisibility(View.VISIBLE);
            imgEdit.setVisibility(View.VISIBLE);
            imgDelete.setVisibility(View.VISIBLE);
            edtMemNo.setVisibility(View.VISIBLE);
            txtID.setVisibility(View.VISIBLE);
            imgProfile.setClickable(false);
            btnImageUpload.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.GONE);
            lvEdit.setVisibility(View.GONE);

            getDependentDetail();
        }
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean error = false;
                int selectedGenderId = rgGender.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                RadioButton radioButton = (RadioButton) findViewById(selectedGenderId);
                if (radioButton == null) {
                    strGender = "";
                } else {
                    strGender = radioButton.getText().toString();
                }
                final String regexStr = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}$";
                String regexPincode = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$";

                if (edtFirstName.getText().toString().equalsIgnoreCase("")) {
                    edtFirstName.setError("Please enter First name");
                    edtFirstName.requestFocus();
                    error = true;
                }else if (edtDOB.getText().toString().equalsIgnoreCase("")) {
                    edtDOB.setError("Please Select Birth Date");
                    edtDOB.requestFocus();
                    error = true;
                }else if (strGender.equalsIgnoreCase("")) {
                    Toast.makeText(context, "Please Select Gender", Toast.LENGTH_SHORT).show();
                    rgGender.requestFocus();
                    error = true;
                }/*else if (strGender.equalsIgnoreCase("Female")) {
                    Toast.makeText(context, "Female member should be added as dependent", Toast.LENGTH_SHORT).show();
                    rgGender.requestFocus();
                    error = true;
                }*/ else if (strBloodGroup.equalsIgnoreCase("")) {
                    Toast.makeText(context, "Please Select Blood group", Toast.LENGTH_SHORT).show();
                    spnBloodGroup.requestFocus();
                    error = true;
                } else if (strMarritalStatus.equalsIgnoreCase("")) {
                    Toast.makeText(context, "Please Select marrital status", Toast.LENGTH_SHORT).show();
                    spnMarritalStatus.requestFocus();
                    error = true;
                }else if(getAge(edtDOB.getText().toString())>=18) {
                    if (edtMobile.getText().toString().equalsIgnoreCase("")) {
                        edtMobile.setError("Please enter mobile number");
                        edtMobile.requestFocus();
                        error = true;
                    } else if (edtMobile.getText().toString().length() > 0
                            && !edtMobile.getText().toString().matches(regexStr)) {
                        edtMobile.setError("Please enter valid mobile no.");
                        edtMobile.setFocusableInTouchMode(true);
                        edtMobile.requestFocus();
                        error = true;
                    } else if (edtMobile.getText().toString().length() > 0
                            && edtMobile.getText().toString().length() < 10) {
                        edtMobile.setError("Please Enter Valid Mobile No");
                        edtMobile.setFocusableInTouchMode(true);
                        edtMobile.requestFocus();
                        error = true;
                    }
                }
                if (error != true) {
                    if(getIntent().getStringExtra("From").equalsIgnoreCase("Add")){
                        addDependent();
                    }else {
                        updateDependent();
                    }
                }
            }
        });

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!isAdsTicking) {
                    if (adsImageUrls.size() > 0) {
                        if (adCounter >= adsImageUrls.size())
                            adCounter = 0;
                        Intent i = new Intent(DependentDetailActivity.this, FullScreenAdActivity.class);
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
                    Intent i = new Intent(DependentDetailActivity.this, FullScreenAdActivity.class);
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
                    Intent intent = new Intent(DependentDetailActivity.this, WebviewActivity.class);
                    intent.putExtra("strValue", strAdWeblink);
                    startActivity(intent);
                }
            }
        });
        getBottomAds();
    }
    private void getBottomAds() {
        final ProgressDialog progress = new ProgressDialog(DependentDetailActivity.this);
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
                                        imageLoader = CustomVolleyRequest.getInstance(DependentDetailActivity.this).getImageLoader();
                                        imageLoader.get(adsResponse.getData().get(i).getFull_path(), ImageLoader.getImageListener(imageView, R.mipmap.noimage, android.R.drawable.ic_dialog_alert));
                                    }
                                }
                            }

                        } else {
                            //ProgressDialogView.hideProgressDialog();
                            imageView.setVisibility(View.GONE);
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

    public void getDependentDetail() {

        if(dependent!=null) {
            ImageLoader imageLoader = CustomVolleyRequest.getInstance(DependentDetailActivity.this).getImageLoader();
            String uri="";
            if(dependent.getPhoto_link().contains("http://")) {
                if(dependent.getPhoto_link().trim().contains("/NULL")){
                    ;
                }else
                imageLoader.get(dependent.getPhoto_link(), ImageLoader.getImageListener(imgProfile, R.mipmap.noimage, R.mipmap.profile));
            }
            else {
                if (dependent.getPhoto_link().trim().equalsIgnoreCase("NULL")) {
                    ;
                } else {
                    if (dependent.getPhoto_link().trim().startsWith("http://"))
                        imageLoader.get(dependent.getPhoto_link(), ImageLoader.getImageListener(imgProfile, R.mipmap.noimage, R.mipmap.profile));
                    else
                        imageLoader.get("http://khambhatpragati.com/uploads/member_images/" +
                                dependent.getPhoto_link(), ImageLoader.getImageListener(imgProfile, R.mipmap.noimage, R.mipmap.profile));
                }
            }
            try {
                BitmapDrawable drawable = (BitmapDrawable) imgProfile.getDrawable();
                Bitmap bmp = drawable.getBitmap();
                if (bmp.getWidth() > bmp.getHeight()) {
                    Bitmap bMapRotate=null;
                    Matrix mat=new Matrix();
                    mat.postRotate(90);
                    bMapRotate = Bitmap.createBitmap(bmp, 0, 0,bmp.getWidth(),bmp.getHeight(), mat, true);
                    imgProfile.setImageBitmap(bMapRotate);
                } else {
                    imgProfile.setImageBitmap(bmp);
                }

            } catch (Exception e) {

            }

            if (!dependent.getDependent_id().equalsIgnoreCase("null")) {
                tvMemno.setText(dependent.getDependent_id());
                edtMemNo.setText(dependent.getDependent_id());
            }
            if (!dependent.getFirst_name().equalsIgnoreCase("null")) {
                tvFirstName.setText(dependent.getFirst_name());
                edtFirstName.setText(dependent.getFirst_name());
            }
            if (!dependent.getGender().equalsIgnoreCase("null")) {

                if (dependent.getGender().equalsIgnoreCase("1")
                        || dependent.getGender().equalsIgnoreCase("Male")) {
                    rbMale.setChecked(true);
                    rbFemale.setChecked(false);
                    strGender = "Male";
                } else if (dependent.getGender().equalsIgnoreCase("2")
                        || dependent.getGender().equalsIgnoreCase("FeMale")) {
                    rbMale.setChecked(false);
                    rbFemale.setChecked(true);
                    strGender = "Female";
                }

                tvGender.setText(strGender);
            }
            if (!dependent.getDate_of_birth().equalsIgnoreCase("null")) {
                tvDOB.setText(dependent.getDate_of_birth());
                edtDOB.setText(dependent.getDate_of_birth());
            }
            if (!dependent.getRelation_with_member().equalsIgnoreCase("null")) {
                if (dependent.getRelation_with_member().equalsIgnoreCase("1")
                        || dependent.getRelation_with_member().equalsIgnoreCase("Son")) {
                    spnRelation.setSelection(0);
                    strRelation = "Son";
                } else if (dependent.getRelation_with_member().equalsIgnoreCase("2")
                        || dependent.getRelation_with_member().equalsIgnoreCase("Daughter")) {
                    spnRelation.setSelection(1);
                    strRelation = "Daughter";
                } else if (dependent.getRelation_with_member().equalsIgnoreCase("3")
                        || dependent.getRelation_with_member().equalsIgnoreCase("Wife")) {
                    spnRelation.setSelection(2);
                    strRelation = "Wife";
                } else if (dependent.getRelation_with_member().equalsIgnoreCase("4")
                        || dependent.getRelation_with_member().equalsIgnoreCase("Brother")) {
                    spnRelation.setSelection(3);
                    strRelation = "Brother";
                } else if (dependent.getRelation_with_member().equalsIgnoreCase("5")
                        || dependent.getRelation_with_member().equalsIgnoreCase("Sister")) {
                    spnRelation.setSelection(4);
                    strRelation = "Sister";
                }
                tvRelation.setText(strRelation);
            }
            if (!dependent.getBlood_group().equalsIgnoreCase("null")) {
                if (dependent.getBlood_group().equalsIgnoreCase("1")
                        || dependent.getBlood_group().equalsIgnoreCase("A+")) {
                    spnBloodGroup.setSelection(0);
                    strBloodGroup = "A+";
                } else if (dependent.getBlood_group().equalsIgnoreCase("2")
                        || dependent.getBlood_group().equalsIgnoreCase("A-")) {
                    spnBloodGroup.setSelection(1);
                    strBloodGroup = "A-";
                } else if (dependent.getBlood_group().equalsIgnoreCase("3")
                        || dependent.getBlood_group().equalsIgnoreCase("B+")) {
                    spnBloodGroup.setSelection(2);
                    strBloodGroup = "B+";
                } else if (dependent.getBlood_group().equalsIgnoreCase("4")
                        || dependent.getBlood_group().equalsIgnoreCase("B-")) {
                    spnBloodGroup.setSelection(3);
                    strBloodGroup = "B-";
                } else if (dependent.getBlood_group().equalsIgnoreCase("5")
                        || dependent.getBlood_group().equalsIgnoreCase("AB+")) {
                    spnBloodGroup.setSelection(4);
                    strBloodGroup = "AB+";
                } else if (dependent.getBlood_group().equalsIgnoreCase("6")
                        || dependent.getBlood_group().equalsIgnoreCase("AB-")) {
                    spnBloodGroup.setSelection(5);
                    strBloodGroup = "AB-";
                } else if (dependent.getBlood_group().equalsIgnoreCase("7")
                        || dependent.getBlood_group().equalsIgnoreCase("O+")) {
                    spnBloodGroup.setSelection(6);
                    strBloodGroup = "O+";
                } else if (dependent.getBlood_group().equalsIgnoreCase("8")
                        || dependent.getBlood_group().equalsIgnoreCase("O-")) {
                    spnBloodGroup.setSelection(7);
                    strBloodGroup = "O-";
                }
                tvBloodGroup.setText(strBloodGroup);
            }
            if (!dependent.getMarital_status().equalsIgnoreCase("null")) {
                if (dependent.getMarital_status().equalsIgnoreCase("1")
                        || dependent.getMarital_status().equalsIgnoreCase("Married")) {
                    spnMarritalStatus.setSelection(0);
                    strMarritalStatus = "Married";
                    lvHusbandDetail.setVisibility(View.VISIBLE);
                    lvHusbandHeading.setVisibility(View.VISIBLE);
                } else if (dependent.getMarital_status().equalsIgnoreCase("2")
                        || dependent.getMarital_status().equalsIgnoreCase("0")
                        || dependent.getMarital_status().equalsIgnoreCase("UnMarried")) {
                    spnMarritalStatus.setSelection(1);
                    strMarritalStatus = "Unmarried";
                    lvHusbandDetail.setVisibility(View.GONE);
                    lvHusbandHeading.setVisibility(View.GONE);
                }
                tvMarritalStatus.setText(strMarritalStatus);
            }
            if (!dependent.getQualification().equalsIgnoreCase("null")) {
                if (dependent.getQualification().equalsIgnoreCase("1")
                        || dependent.getQualification().equalsIgnoreCase("Student")) {
                    spnQualification.setSelection(0);
                    strQualification = "Student";
                } else if (dependent.getQualification().equalsIgnoreCase("2")
                        || dependent.getQualification().equalsIgnoreCase("10th")) {
                    spnQualification.setSelection(1);
                    strQualification = "10th";
                } else if (dependent.getQualification().equalsIgnoreCase("3")
                        || dependent.getQualification().equalsIgnoreCase("12th")) {
                    spnQualification.setSelection(2);
                    strQualification = "12th";
                }else if (dependent.getQualification().equalsIgnoreCase("4")
                        || dependent.getQualification().equalsIgnoreCase("Diploma")) {
                    spnQualification.setSelection(3);
                    strQualification = "Diploma";
                } else if (dependent.getQualification().equalsIgnoreCase("5")
                        || dependent.getQualification().contains("Graduate")) {
                    spnQualification.setSelection(4);
                    rgGraduate.setVisibility(View.VISIBLE);
                    edtOthers.setVisibility(View.GONE);
                    String strServices = dependent.getQualification().substring(dependent.getQualification().indexOf("-")+1,
                            dependent.getQualification().length());
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
                } else if (dependent.getQualification().equalsIgnoreCase("6")
                        || dependent.getQualification().contains("Post-Graduate")
                        || dependent.getQualification().contains("PostGraduate")
                        || dependent.getQualification().contains("Post Graduate")) {
                    spnQualification.setSelection(5);
                    edtPostGraduate.setVisibility(View.VISIBLE);
                    rgGraduate.setVisibility(View.GONE);

                    edtPostGraduate.setText(dependent.getQualification().substring(dependent.getQualification().indexOf("-")+1,
                            dependent.getQualification().length()));

                    strQualification="Post-Graduate - "+edtPostGraduate.getText().toString();

                } else if (dependent.getQualification().equalsIgnoreCase("7")
                        || dependent.getQualification().equalsIgnoreCase("Professional")) {
                    spnQualification.setSelection(6);
                    strQualification = "Professional";
                }
                tvQualification.setText(strQualification);
            }
            if (!dependent.getProfession().equalsIgnoreCase("null")) {
                if (dependent.getProfession().equalsIgnoreCase("1")
                        || dependent.getProfession().equalsIgnoreCase("Doctor")) {
                    spnBusiness.setSelection(0);
                    strBusiness = "Doctor";
                } else if (dependent.getProfession().equalsIgnoreCase("2")
                        || dependent.getProfession().equalsIgnoreCase("Lawyer")) {
                    spnBusiness.setSelection(1);
                    strBusiness = "Lawyer";
                } else if (dependent.getProfession().equalsIgnoreCase("3")
                        || dependent.getProfession().equalsIgnoreCase("Architects")) {
                    spnBusiness.setSelection(2);
                    strBusiness = "Architects";
                } else if (dependent.getProfession().equalsIgnoreCase("4")
                        || dependent.getProfession().equalsIgnoreCase("CA/CS/ICWA")) {
                    spnBusiness.setSelection(3);
                    strBusiness = "CA/CS/ICWA";
                } else if (dependent.getProfession().equalsIgnoreCase("5")
                        || dependent.getProfession().equalsIgnoreCase("MBA")) {
                    spnBusiness.setSelection(4);
                    strBusiness = "MBA";
                } else if (dependent.getProfession().equalsIgnoreCase("6")
                        || dependent.getProfession().equalsIgnoreCase("Teacher/Professor")) {
                    spnBusiness.setSelection(5);
                    strBusiness = "Teacher/Professor";
                } else if (dependent.getProfession().equalsIgnoreCase("7")
                        || dependent.getProfession().equalsIgnoreCase("Interior Designer")) {
                    spnBusiness.setSelection(6);
                    strBusiness = "Interior Designer";
                } else if (dependent.getProfession().equalsIgnoreCase("8")
                        || dependent.getProfession().equalsIgnoreCase("Fashion Designer")) {
                    spnBusiness.setSelection(7);
                    strBusiness = "Fashion Designer";
                } else if (dependent.getProfession().equalsIgnoreCase("9")
                        || dependent.getProfession().contains("Engineer")) {
                    spnBusiness.setSelection(8);
                    edtEngineers.setVisibility(View.VISIBLE);
                    rgServices.setVisibility(View.GONE);
                    edtOthers.setVisibility(View.GONE);
                    rgBusiness.setVisibility(View.GONE);

                    edtEngineers.setText(dependent.getProfession().substring(dependent.getProfession().indexOf("-")+1,
                            dependent.getProfession().length()));
                    strBusiness="Engineer - "+edtEngineers.getText().toString();
                } else if (dependent.getProfession().equalsIgnoreCase("10")
                        || dependent.getProfession().equalsIgnoreCase("Civil engineer")) {
                    spnBusiness.setSelection(9);
                    strBusiness = "Civil engineer";
                } else if (dependent.getProfession().equalsIgnoreCase("11")
                        || dependent.getProfession().contains("Business")) {
                    spnBusiness.setSelection(10);
                    rgBusiness.setVisibility(View.VISIBLE);
                    rgServices.setVisibility(View.GONE);
                    edtOthers.setVisibility(View.GONE);
                    edtEngineers.setVisibility(View.GONE);
                    String strServices = dependent.getProfession().substring(dependent.getProfession().indexOf("-")+1,
                            dependent.getProfession().length());
                    if(strServices.trim().equalsIgnoreCase("Trading / Stockist")) {
                        rbStokist.setChecked(true);
                        rbManufacturing.setChecked(false);
                    }
                    else if(strServices.trim().equalsIgnoreCase("Manufacturing")) {
                        rbManufacturing.setChecked(true);
                        rbStokist.setChecked(false);
                    }
                    strBusiness="Business - "+strServices.trim();
                } else if (dependent.getProfession().equalsIgnoreCase("12")
                        || dependent.getProfession().contains("Service")) {
                    spnBusiness.setSelection(11);
                    rgServices.setVisibility(View.VISIBLE);
                    rgBusiness.setVisibility(View.GONE);
                    edtOthers.setVisibility(View.GONE);
                    edtEngineers.setVisibility(View.GONE);

                    String strServices = dependent.getProfession().substring(dependent.getProfession().indexOf("-")+1,
                            dependent.getProfession().length());
                    if(strServices.trim().equalsIgnoreCase("Public")) {
                        rbPublic.setChecked(true);
                        rbPrivate.setChecked(false);
                    }
                    else if(strServices.trim().equalsIgnoreCase("Private")) {
                        rbPrivate.setChecked(true);
                        rbPublic.setChecked(false);
                    }
                    strBusiness="Service - "+strServices.trim();

                } else if (dependent.getProfession().equalsIgnoreCase("13")
                        || dependent.getProfession().equalsIgnoreCase("Housewife")) {
                    spnBusiness.setSelection(12);
                    strBusiness = "Housewife";
                } else if (dependent.getProfession().equalsIgnoreCase("14")
                        || dependent.getProfession().equalsIgnoreCase("Retired")) {
                    spnBusiness.setSelection(13);
                    strBusiness = "Retired";
                } else if (dependent.getProfession().equalsIgnoreCase("15")
                        || dependent.getProfession().contains("Other")) {
                    spnBusiness.setSelection(14);
                    edtOthers.setVisibility(View.VISIBLE);
                    rgServices.setVisibility(View.GONE);
                    rgBusiness.setVisibility(View.GONE);
                    edtEngineers.setVisibility(View.GONE);

                    edtOthers.setText(dependent.getProfession().substring(dependent.getProfession().indexOf("-")+1,
                            dependent.getProfession().length()));
                    strBusiness="other - "+edtOthers.getText().toString();
                }
                tvProfession.setText(strBusiness);
            }
            if (!dependent.getMobile_no().equalsIgnoreCase("null")
                    &&!dependent.getMobile_no().equalsIgnoreCase("0")) {
                tvMobile.setText(dependent.getMobile_no());
                edtMobile.setText(dependent.getMobile_no());
            }
            if (!dependent.getEmail().equalsIgnoreCase("null")) {
                tvEmail.setText(dependent.getEmail());
                edtEmail.setText(dependent.getEmail());
            }
            if (!dependent.getMembership_status().equalsIgnoreCase("null")) {
                if (dependent.getMembership_status().equalsIgnoreCase("1")
                        || dependent.getMembership_status().equalsIgnoreCase("Active")) {
                    spnMembershipStatus.setSelection(0);
                    strMembershipStatus = "Active";
                } else if (dependent.getMembership_status().equalsIgnoreCase("0")
                        || dependent.getMembership_status().equalsIgnoreCase("2")
                        || dependent.getMembership_status().equalsIgnoreCase("InActive")
                        || dependent.getMembership_status().equalsIgnoreCase("In Active")
                        || dependent.getMembership_status().equalsIgnoreCase("In-Active")) {
                    spnMembershipStatus.setSelection(1);
                    strMembershipStatus = "In-Active";
                }
                tvMembershipStatus.setText(strMembershipStatus);
            }
            if (!dependent.getHusband_member_id().equalsIgnoreCase("null")) {
                tvHusbandID.setText(dependent.getHusband_member_id());
                edtHusbandID.setText(dependent.getHusband_member_id());
            }
            if (!dependent.getHusband_description().equalsIgnoreCase("null")) {
                tvHusbandDesc.setText(dependent.getHusband_description());
                edtHusbandDesc.setText(dependent.getHusband_description());
            }
            if (!dependent.getHusband_name().equalsIgnoreCase("null")) {
                tvHusbandName.setText(dependent.getHusband_name());
                edtHusbandName.setText(dependent.getHusband_name());
            }
            if (!dependent.getHusband_qualification().equalsIgnoreCase("null")) {
                tvHusbandQualification.setText(dependent.getHusband_qualification());
                edtHusbandQualification.setText(dependent.getHusband_qualification());
            }
        }else{
            DialogUtil.alertWithOkButton(DependentDetailActivity.this,"Dependent data not found");
        }
    }

    public void updateDependent() {
        final ProgressDialog progress = new ProgressDialog(DependentDetailActivity.this);
        progress.setMessage("Please Wait....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

        Map<String, Object> params = new HashMap<>();
        params.put("first_name", edtFirstName.getText().toString());
        if(dependent!=null)
            params.put("parent_member_id", dependent.getParent_member_id());
        else
            params.put("parent_member_id", UserPreference.getInstance(DependentDetailActivity.this).getPreference().getMemberID());
        params.put("gender", strGender);
        params.put("date_of_birth", edtDOB.getText().toString());
        if(strMarritalStatus.equalsIgnoreCase("Married"))
            params.put("marital_status", "1");
        else
            params.put("marital_status", "2");
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
        if(strMembershipStatus.equalsIgnoreCase("Active"))
            params.put("membership_status","1" );
        else
            params.put("membership_status", "0");
        params.put("relation_with_member", strRelation);
        params.put("record_no", dependent.getRecord_no());
        params.put("mobile_no", edtMobile.getText().toString());
        params.put("email", edtEmail.getText().toString());
        params.put("husband_member_id", edtHusbandID.getText().toString());
        params.put("husband_name", edtHusbandName.getText().toString());
        params.put("husband_description", edtHusbandDesc.getText().toString());
        params.put("husband_qualification", edtHusbandQualification.getText().toString());

        Log.d("Params", "Testing" + params);

        RequestQueue queue = Volley.newRequestQueue(DependentDetailActivity.this);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, UPDATEDEPENDENT_URL,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject json) {
                        try {
                            Log.d("TAG", "JSON POST Response = " + json.toString());

                            if (!json.getBoolean("error")) {
                                if (Path != null) {
                                    float size = getImageSize(context,Uri.parse(Path)) / 1024;
                                    if(size <= 1){
                                        if(size<=-1)
                                            DialogUtil.alertWithOkButton(context,"Invalid Image!!");
                                        else
                                            updateImage(json.getJSONObject("data").getString("record_no"));
                                    } else
                                        DialogUtil.alertWithOkButton(context,"Please select image or lower size upto 1MB");

                                } else {
                                    DialogUtil.showToastShort(context,json.getString("message"));
                                    Intent i = new Intent(DependentDetailActivity.this, DependentListActivity.class);
                                    startActivity(i);
                                    finish();
                                    overridePendingTransition(R.anim.slide_up, R.anim.stay);
                                }
                            } else {
                                DialogUtil.showSnackBarWithAction(relativeLayout, "Error in updateDependent");
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
                        DialogUtil.showSnackBarWithAction(relativeLayout, error.getMessage());
                        progress.dismiss();
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

    public void deleteDependent() {
        final ProgressDialog progress = new ProgressDialog(DependentDetailActivity.this);
        progress.setMessage("Please Wait....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

        Map<String, Object> params = new HashMap<>();
        params.put("record_no", dependent.getRecord_no());

        Log.d("Params", "Testing" + params);

        RequestQueue queue = Volley.newRequestQueue(DependentDetailActivity.this);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, DELETEDEPENDENT_URL,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject json) {
                        try {
                            Log.d("TAG", "JSON POST Response = " + json.toString());

                            if (!json.getBoolean("error")) {
                                DialogUtil.showToastShort(context,json.getString("message"));
                                Intent i = new Intent(DependentDetailActivity.this, DependentListActivity.class);
                                startActivity(i);
                                finish();
                                overridePendingTransition(R.anim.slide_up, R.anim.stay);

                            } else {
                                DialogUtil.showSnackBarWithAction(relativeLayout, "Error in deleteDependent");
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

    public void addDependent() {
        final ProgressDialog progress = new ProgressDialog(DependentDetailActivity.this);
        progress.setMessage("Please Wait....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();


        Map<String, Object> params = new HashMap<>();
        params.put("first_name", edtFirstName.getText().toString());
        params.put("parent_member_id", UserPreference.getInstance(DependentDetailActivity.this).getPreference().getMemberID());
        params.put("gender", strGender);
        params.put("date_of_birth", edtDOB.getText().toString());
        if(strMarritalStatus.equalsIgnoreCase("Married"))
            params.put("marital_status", "1");
        else
            params.put("marital_status", "2");
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
        if(strMembershipStatus.equalsIgnoreCase("Active"))
            params.put("membership_status","1" );
        else
            params.put("membership_status", "0");

        params.put("relation_with_member", strRelation);
        /*if(edtMobile.getText().toString().equalsIgnoreCase(""))
            params.put("mobile_no", "9999999999");
        else*/
            params.put("mobile_no", edtMobile.getText().toString());
        params.put("email", edtEmail.getText().toString());
        params.put("husband_member_id", edtHusbandID.getText().toString());
        params.put("husband_name", edtHusbandName.getText().toString());
        params.put("husband_description", edtHusbandDesc.getText().toString());
        params.put("husband_qualification", edtHusbandQualification.getText().toString());

        Log.d("Params", "Testing" + params);

        RequestQueue queue = Volley.newRequestQueue(DependentDetailActivity.this);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, ADDDEPENDENT_URL,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject json) {
                        try {
                            Log.d("TAG", "JSON POST Response = " + json.toString());

                            if (!json.getBoolean("error")) {
                                if (Path != null) {
                                    float size = getImageSize(context,Uri.parse(Path));
                                    if(size > 0 && size < 1){
                                        if(size<=0)
                                            DialogUtil.alertWithOkButton(context,"Invalid Image!!");
                                        else
                                            DialogUtil.alertWithOkButton(context,"Please select image or lower size upto 1MB");
                                    }else
                                        updateImage(json.getJSONObject("data").getString("record_no"));
                                } else {
                                    Intent i = new Intent(DependentDetailActivity.this, DependentListActivity.class);
                                    startActivity(i);
                                    finish();
                                    overridePendingTransition(R.anim.slide_up, R.anim.stay);
                                }
                            } else {
                                DialogUtil.showSnackBarWithAction(relativeLayout, "Error in addDependent");
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
        }
        return 0;*/
        File file = new File(uri.getPath());
        long length = file.length();
        length = length/1024;
        System.out.println("File Path : " + file.getPath() + ", File size : " + length +" KB");
        return length;
    }
    public void updateImage(String recordNo) {
        final ProgressDialog progress = new ProgressDialog(DependentDetailActivity.this);
        progress.setMessage("Uploading Image please Wait....");
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
        params.put("record_no", recordNo);
        params.put("is_dependent", "yes");
        params.put("member_id", UserPreference.getInstance(DependentDetailActivity.this).getPreference().getMemberID());
        if (Path != null) {
            params.put("photo_link", ba1);
        } else {
            params.put("photo_link", "");
        }

        Log.d("Params", "Testing" + params);

        RequestQueue queue = Volley.newRequestQueue(DependentDetailActivity.this);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, ADDMEMBERIMAGE_URL,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject json) {
                        try {
                            Log.d("TAG", "JSON POST Response = " + json.toString());

                            if (!json.getBoolean("error")) {
                                DialogUtil.showToastShort(context,json.getString("message"));
                                Intent i = new Intent(DependentDetailActivity.this, DependentListActivity.class);
                                startActivity(i);
                                finish();
                                overridePendingTransition(R.anim.slide_up, R.anim.stay);
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
        if (ActivityCompat.shouldShowRequestPermissionRationale(DependentDetailActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                && ActivityCompat.shouldShowRequestPermissionRationale(DependentDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        } else {
            ActivityCompat.requestPermissions(DependentDetailActivity.this, new String[]{
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
                    Toast.makeText(DependentDetailActivity.this, "Access Denied, App may not run properly./अनुमतियां अस्वीकृत, ऐप ठीक से नहीं चलेंगे", Toast.LENGTH_LONG).show();
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
        if (requestCode == 3030) {
            if(resultCode == RESULT_OK) {
                if(data.hasExtra("bName")){
                    edtHusbandID.setText(data.getStringExtra("bID"));
                    edtHusbandName.setText(data.getStringExtra("bMobile"));

                    edtHusbandID.setFocusable(false);
                    edtHusbandID.setClickable(false);

                    edtHusbandName.setFocusable(false);
                    edtHusbandName.setClickable(false);
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
                        if (bmp.getWidth() > bmp.getHeight()) {
                            Bitmap bMapRotate=null;
                            Matrix mat=new Matrix();
                            mat.postRotate(90);
                            bMapRotate = Bitmap.createBitmap(bmp, 0, 0,bmp.getWidth(),bmp.getHeight(), mat, true);
                            imgProfile.setImageBitmap(bMapRotate);
                        } else {
                            imgProfile.setImageBitmap(bmp);
                        }
//                        imgProfile.setImageBitmap(bmp);
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
                        if (bmp.getWidth() > bmp.getHeight()) {
                            Bitmap bMapRotate=null;
                            Matrix mat=new Matrix();
                            mat.postRotate(90);
                            bMapRotate = Bitmap.createBitmap(bmp, 0, 0,bmp.getWidth(),bmp.getHeight(), mat, true);
                            imgProfile.setImageBitmap(bMapRotate);
                        } else {
                            imgProfile.setImageBitmap(bmp);
                        }

//                        imgProfile.setImageBitmap(bmp);
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
                Bitmap bmp = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
                Path = "null";
                Path = picturePath;

                Log.w("imagepath->gallery", picturePath + "");

                if (bmp.getWidth() > bmp.getHeight()) {
                    Bitmap bMapRotate=null;
                    Matrix mat=new Matrix();
                    mat.postRotate(90);
                    bMapRotate = Bitmap.createBitmap(bmp, 0, 0,bmp.getWidth(),bmp.getHeight(), mat, true);
                    imgProfile.setImageBitmap(bMapRotate);
                } else {
                    imgProfile.setImageBitmap(bmp);
                }
                //imgProfile.setImageBitmap(thumbnail);
                //imgProfile.setImageBitmap(ExifUtils.rotateBitmap(picturePath, decodeSampledBitmap(new File(picturePath), 200, 200)));

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