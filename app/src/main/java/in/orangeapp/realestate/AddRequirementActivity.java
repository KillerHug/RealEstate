package in.orangeapp.realestate;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;
import static in.orangeapp.util.Constant.SERVER_URLMAIN;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import in.orangeapp.util.Constant;
import in.orangeapp.util.YourPreference;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import in.orangeapp.realestate.R;

public class AddRequirementActivity extends AppCompatActivity {
    Toolbar toolbar;
    ArrayList<String> propertytype = new ArrayList<String>();
    ProgressDialog dialog;

    SpinnerDialog searchableSpinner;
    SpinnerDialog searchableSpinner3;
    SpinnerDialog searchableSpinner4;
    SpinnerDialog bhktypespinner;
    LinearLayout bhk_layout;
    ArrayList<String> statelist = new ArrayList<String>();
    ArrayList<String> state_id_list = new ArrayList<String>();
    ArrayList<String> districtlist = new ArrayList<String>();
    RadioGroup main_type, sub_type;
    String main_type_str = "purchase", subtype_str = "Residential";
    EditText property_type, locality, distrcit, state, edt_property_id, bhk, plot_area, carpet_area, price_edt, edt_description;
    MaterialButton post;
    String namee;
    String mobile;
    RadioGroup radioGroup;
    String amountType = "";
    RadioButton paymentType;
    TextView tvCount;
    RadioButton rbLakhs, rbCrores, purchase_radio, rent_lease, residential, commercial, other;
    private String prop_id = "";
    ImageView ivDistrictArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_requirement);
        price_edt = findViewById(R.id.price_edt);
        ivDistrictArrow = findViewById(R.id.ivDistrictArrow);
        edt_description = findViewById(R.id.edt_description);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        toolbar = findViewById(R.id.toolbar);
        property_type = findViewById(R.id.property_type);
        bhk_layout = findViewById(R.id.bhk_layout);
        locality = findViewById(R.id.locality);
        bhk = findViewById(R.id.bhk);
        post = findViewById(R.id.post);
        carpet_area = findViewById(R.id.carpet_area);
        plot_area = findViewById(R.id.plot_area);
        edt_property_id = findViewById(R.id.edt_property_id);
        distrcit = findViewById(R.id.distrcit);
        state = findViewById(R.id.state);
        main_type = findViewById(R.id.main_type);
        sub_type = findViewById(R.id.sub_type);
        tvCount = findViewById(R.id.tvCount);
        rbLakhs = findViewById(R.id.radioLakh);
        rbCrores = findViewById(R.id.radioCrore);
        purchase_radio = findViewById(R.id.purchase_radio);
        rent_lease = findViewById(R.id.rent_lease);
        residential = findViewById(R.id.residential);
        commercial = findViewById(R.id.commercial);
        other = findViewById(R.id.other);
        toolbar.setTitle("Requirement");
        ivDistrictArrow.setEnabled(false);
        distrcit.setEnabled(false);

        setSupportActionBar(toolbar);
        edt_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null)
                    tvCount.setText(s.toString().length() + "/125");
            }
        });
        initListener();
        state_distric_spinner();
        getstate();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
//        edit data
        String from = getIntent().getStringExtra("from");
        if (from.equalsIgnoreCase("edit")) {
            post.setText("Update");
            rbLakhs.setChecked(false);
            rbCrores.setChecked(false);
            property_type.setEnabled(false);
            //            intent.putExtra("user_id", user_id);
            String txtPaymentType = getIntent().getStringExtra("payment_type");
            String txtMainType = getIntent().getStringExtra("main_type");
            String txtSubType = getIntent().getStringExtra("sub_type");
            String txtState = getIntent().getStringExtra("state");
            String txtDistrict = getIntent().getStringExtra("district");
            String txtLocation = getIntent().getStringExtra("location");
            String txtPrice = getIntent().getStringExtra("price");
            String txtDescription = getIntent().getStringExtra("description");
            String txtPlotArea = getIntent().getStringExtra("plot_area");
            String txtFloorType = getIntent().getStringExtra("floor_type");
            String txtBhk = getIntent().getStringExtra("bhk");
            String txtCarpetArea = getIntent().getStringExtra("carpet_area");
            String txtPropertyType = getIntent().getStringExtra("property_type");
            prop_id = getIntent().getStringExtra("property_id");
            if (txtPaymentType.equalsIgnoreCase("Lakhs")) {
                rbLakhs.setChecked(true);
            } else if (txtPaymentType.equalsIgnoreCase("Crores")) {
                rbCrores.setChecked(true);
            }
            purchase_radio.setEnabled(false);
            rent_lease.setEnabled(false);
            residential.setEnabled(false);
            commercial.setEnabled(false);
            other.setEnabled(false);
            if (txtMainType.equalsIgnoreCase("purchase")) {
                purchase_radio.setChecked(true);
                main_type_str = "purchase";
                radioGroup.setVisibility(View.VISIBLE);
            } else {
                rent_lease.setChecked(true);
                radioGroup.setVisibility(View.GONE);
                main_type_str = "rent/lease";
            }
            if (txtSubType.equalsIgnoreCase("Residential")) {
                residential.setChecked(true);
            } else if (txtSubType.equalsIgnoreCase("Commercial")) {
                commercial.setChecked(true);
            } else {
                other.setChecked(true);
            }
            property_type.setText(txtPropertyType);
            state.setText(txtState);
            distrcit.setText(txtDistrict);
            locality.setText(txtLocation);
            price_edt.setText(txtPrice);
            edt_description.setText(txtDescription);
            plot_area.setText(txtPlotArea);
            bhk.setText(txtBhk);
            carpet_area.setText(txtCarpetArea);
            property_type_conditions(txtPropertyType);
        }
    }

    private void initListener() {
        post.setOnClickListener(view -> {
            if (property_type.getText().toString().equalsIgnoreCase("")) {
                property_type.setError("*Required");

            } else if (state.getText().toString().equalsIgnoreCase("")) {
                state.setError("*Required");

            } else if (distrcit.getText().toString().equalsIgnoreCase("")) {
                distrcit.setError("*Required");

            } else if (locality.getText().toString().equalsIgnoreCase("")) {
                locality.setError("*Required");
            } else if (plot_area.getVisibility() == View.VISIBLE && plot_area.getText().toString().equalsIgnoreCase("")) {
                plot_area.setError("*Required");
            } else if (carpet_area.getVisibility() == View.VISIBLE && carpet_area.getText().toString().equalsIgnoreCase("")) {
                carpet_area.setError("*Required");
            } else if (bhk_layout.getVisibility() == View.VISIBLE && bhk.getText().toString().equalsIgnoreCase("")) {
                bhk.setError("*Required");
            } else if (price_edt.getText().toString().equalsIgnoreCase("")) {
                price_edt.setError("*Required");
            } else if (radioGroup.getCheckedRadioButtonId() == -1 && main_type_str.equalsIgnoreCase("purchase")) {
                Toast.makeText(AddRequirementActivity.this, "Amount Type Not Selected", Toast.LENGTH_SHORT).show();
            } else {
                if (main_type_str.equalsIgnoreCase("purchase")) {
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    paymentType = findViewById(selectedId);
                    amountType = paymentType.getText().toString();
                } else amountType = "";
                if (getIntent() != null && getIntent().getStringExtra("from").equalsIgnoreCase("edit")) {
                    updatePostRequirement();
                } else {
                    addPostRequirement();
                }
            }
        });
        main_type.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.purchase_radio:
                    main_type_str = "purchase";
                    radioGroup.setVisibility(View.VISIBLE);
                    price_edt.setHint("Budget");
                    break;
                case R.id.rent_lease:
                    main_type_str = "rent/lease";
                    radioGroup.setVisibility(View.GONE);
                    price_edt.setHint("Budget ( Rupees/Month ) ");
                    break;

            }
        });

        sub_type.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.residential:
                    subtype_str = "residential";
                    break;
                case R.id.commercial:
                    subtype_str = "commercial";
                    break;
                case R.id.other:
                    subtype_str = "other";
                    break;

            }
        });
    }

    private void state_distric_spinner() {


        ArrayList<String> bhklist = new ArrayList<String>();
//        bhklist.add("Any");
        bhklist.add("1 BHK");
        bhklist.add("2 BHK");
        bhklist.add("3 BHK");
        bhklist.add("4 BHK");
        bhklist.add("5 BHK");
        bhklist.add("6 BHK");

        ArrayList<String> plot_size_list = new ArrayList<String>();
        plot_size_list.add("Any");
        plot_size_list.add("Less Than 99 Sq. Yds.");
        plot_size_list.add("Between 100 - 199 Sq. Yds.");
        plot_size_list.add("Between 200 - 299 Sq. Yds.");
        plot_size_list.add("Between 300 - 399 Sq. Yds.");
        plot_size_list.add("Between 400 - 499 Sq. Yds.");
        plot_size_list.add("Between 500 - 599 Sq. Yds.");
        plot_size_list.add("Between 600 - 999 Sq. Yds.");
        plot_size_list.add("More Than 1000 Sq. Yds.");

        ArrayList<String> carpet_size_list = new ArrayList<String>();
        carpet_size_list.add("Any");
        carpet_size_list.add("Less Than 99 Sq.Ft.");
        carpet_size_list.add("Between 100 - 249 Sq. Ft.");
        carpet_size_list.add("Between 250 - 499 Sq. Ft.");
        carpet_size_list.add("Between 500 - 999 Sq. Ft.");
        carpet_size_list.add("Between 1000 - 1499 Sq. Ft.");
        carpet_size_list.add("Between 1500 - 1999 Sq. Ft.");
        carpet_size_list.add("Between 2000 - 2499 Sq. Ft.");
        carpet_size_list.add("More Than 2500 Sq. Ft.");

        bhktypespinner = new SpinnerDialog(this, bhklist, "Select BHK", "Cancel");// With No Animation

        bhktypespinner.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                bhk.setError(null);
//                if (position == 0) {
//                    bhk.setHint("Any");
//                    bhk.setText("");
//
//                }
//                else {
                    bhk.setText(item);
//                }
            }
        });

        bhk_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bhktypespinner.showSpinerDialog();

            }
        });
        bhk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bhktypespinner.showSpinerDialog();

            }
        });
        plot_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                plot_sizespinner.showSpinerDialog();

            }
        });
        carpet_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchableSpinner.showSpinerDialog();
            }
        });
        distrcit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchableSpinner3.showSpinerDialog();
            }
        });


        property_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                propertytype.clear();

                if (property_type.getText().toString().equalsIgnoreCase("")) {
                    if (subtype_str.equalsIgnoreCase("residential")) {
                        propertytype.add("Villa/House");
                        propertytype.add("Plot/Land");
                        propertytype.add("Builder Floor");
                        propertytype.add("Flat");
                        propertytype.add("Duplex");

                    } else if (subtype_str.equalsIgnoreCase("commercial")) {
                        propertytype.add("Shop/Showroom");
                        propertytype.add("Office Space");
                        propertytype.add("Shop Cum Office (SCO)");
                        propertytype.add("Basement");
                        propertytype.add("Commercial Land");
                        propertytype.add("Factory/Building/Warehouse");
//                        propertytype.add("Rented Property");

                    } else {
                        propertytype.add("Farm House");
                        propertytype.add("Agriculture land");
//                        propertytype.add("Floor Space Index (FSI)");

                    }
                    searchableSpinner4 = new SpinnerDialog(AddRequirementActivity.this, propertytype, "Select Property Type", "Cancel");// With No Animation

                    searchableSpinner4.bindOnSpinerListener(new OnSpinerItemClick() {
                        @Override
                        public void onClick(String selectedString, int position) {
                            property_type.setError(null);
                            property_type.setText(selectedString);


                            ArrayList<String> floortypelist = new ArrayList<String>();

                            if (selectedString.equalsIgnoreCase("Villa/House")) {
                                plot_area.setVisibility(View.VISIBLE);

                            }
                            if (selectedString.equalsIgnoreCase("Plot/Land")) {
                                plot_area.setVisibility(View.VISIBLE);


                            }
                            if (selectedString.equalsIgnoreCase("Builder Floor")) {
                                plot_area.setVisibility(View.VISIBLE);
                                bhk_layout.setVisibility(View.VISIBLE);
                                floortypelist.add("Any");

                                floortypelist.add("Basement Floor");
                                floortypelist.add("Stilt");
                                floortypelist.add("Lower Ground Floor");
                                floortypelist.add("Ground Floor");
                                floortypelist.add("Upper Ground Floor");
                                floortypelist.add("First Floor");
                                floortypelist.add("Second Floor");
                                floortypelist.add("Third Floor");
                                floortypelist.add("Top Floor With Roof");

                            }
                            if (selectedString.equalsIgnoreCase("Duplex")) {
                                bhk_layout.setVisibility(View.VISIBLE);


                                floortypelist.add("Any");
                                floortypelist.add("Lower Ground Floor & Ground Floor");
                                floortypelist.add("Ground Floor & Upper Ground Floor");
                                floortypelist.add("Upper Ground Floor & First Floor");
                                floortypelist.add("First Floor & Second Floor");
                                floortypelist.add("Second Floor & Third Floor");
                                floortypelist.add("Third Floor & Fourth Floor");
                                floortypelist.add("Fourth Floor & Fifth Floor");

                            }
                            if (selectedString.equalsIgnoreCase("Flat")) {
                                bhk_layout.setVisibility(View.VISIBLE);


                                floortypelist.add("Any");

                                floortypelist.add("Basement Floor");
                                floortypelist.add("Stilt");
                                floortypelist.add("Lower Ground Floor");
                                floortypelist.add("Ground Floor");
                                floortypelist.add("Upper Ground Floor");
                                floortypelist.add("First Floor");
                                floortypelist.add("Second Floor");
                                floortypelist.add("Third Floor");
                                floortypelist.add("Fourth Floor");
                                floortypelist.add("Fifth Floor");
                                floortypelist.add("Sixth Floor");
                                floortypelist.add("Seventh Floor");
                                floortypelist.add("Eight Floor");
                                floortypelist.add("Ninth Floor");
                                floortypelist.add("Tenth Floor");
                                floortypelist.add("Eleventh Floor");
                                floortypelist.add("Twelfth Floor");
                                floortypelist.add("Thirteenth Floor");
                                floortypelist.add("Fourteenth Floor");
                                floortypelist.add("Sixteenth Floor");
                                floortypelist.add("Seveteenth Floor");
                                floortypelist.add("Eighteenth Floor");
                                floortypelist.add("Ninteenth Floor");
                                floortypelist.add("Twentieth Floor");
                                floortypelist.add("Twenty First Floor");
                                floortypelist.add("Twenty Second Floor");
                                floortypelist.add("Twennty Third Floor");
                                floortypelist.add("Twenty Fourth Floor");
                                floortypelist.add("Twenty Fifth Floor");
                                floortypelist.add("Pent House");

                            }
                            if (selectedString.equalsIgnoreCase("Shop Cum Office (SCO)")) {
                                plot_area.setVisibility(View.VISIBLE);
                            }
                            if (selectedString.equalsIgnoreCase("Basement")) {
                                carpet_area.setVisibility(View.VISIBLE);
                            }
                            if (selectedString.equalsIgnoreCase("Farm House")) {
                                plot_area.setVisibility(View.VISIBLE);
                                bhk_layout.setVisibility(View.VISIBLE);
                            }
                            if (selectedString.equalsIgnoreCase("Agriculture Land")) {
                                plot_area.setVisibility(View.VISIBLE);
                            }
                            if (selectedString.equalsIgnoreCase("Commercial Land")) {

                                plot_area.setVisibility(View.VISIBLE);
                            }
                            if (selectedString.equalsIgnoreCase("Factory/Building/Warehouse")) {

                                plot_area.setVisibility(View.VISIBLE);
                            }

                            if (selectedString.equalsIgnoreCase("Shop/Showroom") || selectedString.equalsIgnoreCase("Office Space")) {
                                carpet_area.setVisibility(View.VISIBLE);
                            }

//                            if (selectedString.equalsIgnoreCase("Rented property")) {
//
//                            }
//                            if (selectedString.equalsIgnoreCase("Floor Space Index (FSI)")) {
//
//
//                            }
                        }
                    });

                    searchableSpinner4.showSpinerDialog();
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(AddRequirementActivity.this).create();
                    alertDialog.setTitle("Warning");
                    alertDialog.setMessage("You may loss the filled data");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                    startActivity(getIntent());
                                }
                            });
                    alertDialog.show();
                }
            }
        });

    }

    private void getstate() {

        dialog = new ProgressDialog(AddRequirementActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();


        String url = Constant.SERVER_URLMAIN + "states";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        Map<String, String> params = new HashMap();
        JsonObjectRequest stringRequest = new JsonObjectRequest(GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("states Response", "" + response);
                dialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    //     Toast.makeText(getApplicationContext(), ""+obj.getString("message"), Toast.LENGTH_SHORT).show();

                    String r_code = obj.getString("status");

                    if (r_code.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = obj.getJSONArray("data");


                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            String state_id = jsonObject2.getString("state_id");
                            String state_title = jsonObject2.getString("state_title");
                            statelist.add(state_title);
                            state_id_list.add(state_id);

                        }

                        searchableSpinner = new SpinnerDialog(AddRequirementActivity.this, statelist, "Select State", "Cancel");// With No Animation

                        searchableSpinner.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, int position) {
                                state.setError(null);
                                state.setText(item);

                                getdistrict(state_id_list.get(position));
                            }
                        });
                    }

                } catch (Exception e) {
                    Log.i("states Exception", "" + e);

                    e.printStackTrace();

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

                Log.i("state Error", "" + error);

            }
        });
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);


    }

    private void getdistrict(String s) {
        districtlist.clear();

        dialog = new ProgressDialog(AddRequirementActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();

        String url = Constant.SERVER_URLMAIN + "district_list";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        Map<String, String> params = new HashMap();
        params.put("state_id", s);
        JSONObject parameters = new JSONObject(params);
        Log.i("district parameter]", "" + parameters);

        JsonObjectRequest stringRequest = new JsonObjectRequest(POST, url, parameters, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("district_list Response", "" + response);
                dialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    //  Toast.makeText(getApplicationContext(), ""+obj.getString("message"), Toast.LENGTH_SHORT).show();

                    String r_code = obj.getString("status");

                    if (r_code.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = obj.getJSONArray("data");


                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            String district_title = jsonObject2.getString("district_title");
                            districtlist.add(district_title);

                        }
                        distrcit.setEnabled(true);
                        ivDistrictArrow.setEnabled(true);
                        searchableSpinner3 = new SpinnerDialog(AddRequirementActivity.this, districtlist, "Select District", "Cancel");// With No Animation
                        searchableSpinner3.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String selectedString, int position) {
                                distrcit.setError(null);
                                distrcit.setText(selectedString);
                            }
                        });


                    }

                } catch (Exception e) {
                    Log.i("district_list Exception", "" + e);

                    e.printStackTrace();

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

                Log.i("district_list Error", "" + error);

            }
        });
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    public void property_type(View view) {
        propertytype.clear();

        if (property_type.getText().toString().equalsIgnoreCase("")) {

            propertytype.clear();
            if (subtype_str.equalsIgnoreCase("residential")) {
                propertytype.add("Villa/House");
                propertytype.add("Plot/Land");
                propertytype.add("Builder Floor");
                propertytype.add("Flat");
                propertytype.add("Duplex");

            } else if (subtype_str.equalsIgnoreCase("commercial")) {
                propertytype.add("Shop/Showroom");
                propertytype.add("Office Space");
                propertytype.add("Shop Cum Office (SCO)");
                propertytype.add("Basement");
                propertytype.add("Commercial Land");
                propertytype.add("Factory/Building/Warehouse");
//                propertytype.add("Rented Property");

            } else {
                propertytype.add("Farm House");
                propertytype.add("Agriculture land");
//                propertytype.add("Floor Space Index (FSI)");

            }
            searchableSpinner4 = new SpinnerDialog(AddRequirementActivity.this, propertytype, "Select Property Type", "Cancel");// With No Animation
            searchableSpinner4.bindOnSpinerListener(new OnSpinerItemClick() {
                @Override
                public void onClick(String selectedString, int position) {
                    property_type.setError(null);
                    property_type.setText(selectedString);


                    ArrayList<String> floortypelist = new ArrayList<String>();

                    if (selectedString.equalsIgnoreCase("Villa/House")) {
                        plot_area.setVisibility(View.VISIBLE);

                    }
                    if (selectedString.equalsIgnoreCase("Plot/Land")) {
                        plot_area.setVisibility(View.VISIBLE);


                    }
                    if (selectedString.equalsIgnoreCase("Builder Floor")) {
                        plot_area.setVisibility(View.VISIBLE);
                        bhk_layout.setVisibility(View.VISIBLE);


                        floortypelist.add("Basement Floor");
                        floortypelist.add("Stilt");
                        floortypelist.add("Lower Ground Floor");
                        floortypelist.add("Ground Floor");
                        floortypelist.add("Upper Ground Floor");
                        floortypelist.add("First Floor");
                        floortypelist.add("Second Floor");
                        floortypelist.add("Third Floor");
                        floortypelist.add("Top Floor With Roof");

                    }
                    if (selectedString.equalsIgnoreCase("Duplex")) {
                        bhk_layout.setVisibility(View.VISIBLE);


                        floortypelist.add("Lower Ground Floor & Ground Floor");
                        floortypelist.add("Ground Floor & Upper Ground Floor");
                        floortypelist.add("Upper Ground Floor & First Floor");
                        floortypelist.add("First Floor & Second Floor");
                        floortypelist.add("Second Floor & Third Floor");
                        floortypelist.add("Third Floor & Fourth Floor");
                        floortypelist.add("Fourth Floor & Fifth Floor");

                    }
                    if (selectedString.equalsIgnoreCase("Flat")) {
                        bhk_layout.setVisibility(View.VISIBLE);


                        floortypelist.add("Basement Floor");
                        floortypelist.add("Stilt");
                        floortypelist.add("Lower Ground Floor");
                        floortypelist.add("Ground Floor");
                        floortypelist.add("Upper Ground Floor");
                        floortypelist.add("First Floor");
                        floortypelist.add("Second Floor");
                        floortypelist.add("Third Floor");
                        floortypelist.add("Fourth Floor");
                        floortypelist.add("Fifth Floor");
                        floortypelist.add("Sixth Floor");
                        floortypelist.add("Seventh Floor");
                        floortypelist.add("Eight Floor");
                        floortypelist.add("Ninth Floor");
                        floortypelist.add("Tenth Floor");
                        floortypelist.add("Eleventh Floor");
                        floortypelist.add("Twelfth Floor");
                        floortypelist.add("Thirteenth Floor");
                        floortypelist.add("Fourteenth Floor");
                        floortypelist.add("Sixteenth Floor");
                        floortypelist.add("Seveteenth Floor");
                        floortypelist.add("Eighteenth Floor");
                        floortypelist.add("Ninteenth Floor");
                        floortypelist.add("Twentieth Floor");
                        floortypelist.add("Twenty First Floor");
                        floortypelist.add("Twenty Second Floor");
                        floortypelist.add("Twennty Third Floor");
                        floortypelist.add("Twenty Fourth Floor");
                        floortypelist.add("Twenty Fifth Floor");
                        floortypelist.add("Pent House");

                    }
                    if (selectedString.equalsIgnoreCase("Shop Cum Office (SCO)")) {
                        plot_area.setVisibility(View.VISIBLE);
                    }
                    if (selectedString.equalsIgnoreCase("Basement")) {
                        carpet_area.setVisibility(View.VISIBLE);
                    }
                    if (selectedString.equalsIgnoreCase("Farm House")) {
                        plot_area.setVisibility(View.VISIBLE);
                        bhk_layout.setVisibility(View.VISIBLE);
                    }
                    if (selectedString.equalsIgnoreCase("Agriculture Land")) {
                        plot_area.setVisibility(View.VISIBLE);
                    }
                    if (selectedString.equalsIgnoreCase("Commercial Land")) {

                        plot_area.setVisibility(View.VISIBLE);
                    }
                    if (selectedString.equalsIgnoreCase("Factory/Building/Warehouse")) {

                        plot_area.setVisibility(View.VISIBLE);
                    }

                    if (selectedString.equalsIgnoreCase("Shop/Showroom") || selectedString.equalsIgnoreCase("Office Space")) {
                        carpet_area.setVisibility(View.VISIBLE);
                    }

//                    if (selectedString.equalsIgnoreCase("Rented property")) {
//
//                    }
//                    if (selectedString.equalsIgnoreCase("Floor Space Index (FSI)")) {
//
//                    }
                }
            });

            searchableSpinner4.showSpinerDialog();
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(AddRequirementActivity.this).create();
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("You may loss the filled data");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                            startActivity(getIntent());
                        }
                    });
            alertDialog.show();
        }

    }

    public void state(View view) {
        searchableSpinner.showSpinerDialog();

    }

    public void district(View view) {
        searchableSpinner3.showSpinerDialog();
    }

    private void addPostRequirement() {
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String user_id = yourPrefrence.getData("user_id");

        dialog = new ProgressDialog(AddRequirementActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();

        String url = Constant.SERVER_URLMAIN + "add_requirement";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        Map<String, String> params = new HashMap();
        params.put("user_id", user_id);
//        params.put("property_id", edt_property_id.getText().toString());
        params.put("main_type", main_type_str);
        params.put("sub_type", subtype_str);
        params.put("property_type", property_type.getText().toString());
        params.put("state", state.getText().toString());
        params.put("district", distrcit.getText().toString());
        params.put("location", locality.getText().toString());
        params.put("price", price_edt.getText().toString());
        params.put("description", edt_description.getText().toString());
        params.put("plot_area", plot_area.getText().toString());
        params.put("bhk", bhk.getText().toString());
        params.put("carpet_area", carpet_area.getText().toString());
        params.put("payment_type", amountType);
        params.put("post_type", "Required");
        JSONObject parameters = new JSONObject(params);
        Log.i("search_property par]", "" + parameters);
        JsonObjectRequest stringRequest = new JsonObjectRequest(POST, url, parameters, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("search_property Res", "" + response);
                dialog.dismiss();

                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    //  Toast.makeText(getApplicationContext(), ""+obj.getString("message"), Toast.LENGTH_SHORT).show();

                    String r_code = obj.getString("status");
                    if (r_code.equalsIgnoreCase("1")) {
                        opendialog("Property Updated Successfully");

                    }

                } catch (Exception e) {
                    Log.i("search_property Exc", "" + e);

                    e.printStackTrace();

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

                Log.i("search_property Error", "" + error);

            }
        });
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }

    private void opendialog(String msg) {

        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.sucess, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        //  deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        deleteDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        deleteDialog.setView(deleteDialogView);
        TextView headtext = deleteDialogView.findViewById(R.id.headtext);
        headtext.setText(msg);
        deleteDialogView.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic

                deleteDialog.dismiss();
                Intent intent = new Intent(AddRequirementActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        deleteDialog.show();
        deleteDialog.setCancelable(false);
    }

    private void updatePostRequirement() {
//        add_requirement
        dialog = new ProgressDialog(AddRequirementActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String user_id = yourPrefrence.getData("user_id");


        String url = SERVER_URLMAIN + "edit_requirement";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        Map<String, String> params = new HashMap();
        params.put("user_id", user_id);
        params.put("main_type", main_type_str);
        params.put("sub_type", subtype_str);
        params.put("property_type", property_type.getText().toString());
        params.put("state", state.getText().toString());
        params.put("district", distrcit.getText().toString());
        params.put("location", locality.getText().toString());
        params.put("price", price_edt.getText().toString());
        params.put("description", edt_description.getText().toString());
        params.put("plot_area", plot_area.getText().toString());
        params.put("bhk", bhk.getText().toString());
        params.put("carpet_area", carpet_area.getText().toString());
        params.put("payment_type", amountType);
        params.put("prop_id", prop_id);
        params.put("post_type", "Required");
        JSONObject parameters = new JSONObject(params);
        Log.i("add_requirement parameter]", "" + parameters);
        JsonObjectRequest stringRequest = new JsonObjectRequest(POST, url, parameters, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                Log.i("add_requirement Response", "" + response);
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    //  Toast.makeText(getApplicationContext(), ""+obj.getString("message"), Toast.LENGTH_SHORT).show();
                    String r_code = obj.getString("status");
                    if (r_code.equalsIgnoreCase("1")) {
                        opendialog("Property Updated Successfully");
                    }
                } catch (Exception e) {
                    Log.i("add_property Exception", "" + e);
                    e.printStackTrace();
                }
            }
        }, error -> {
            dialog.dismiss();
            Log.i("add_property Error", "" + error);
        });
        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, 1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }

    private void property_type_conditions(String selectedString) {

        ArrayList<String> floortypelist = new ArrayList<String>();

        if (selectedString.equalsIgnoreCase("Villa/House")) {
            plot_area.setVisibility(View.VISIBLE);
            carpet_area.setVisibility(View.GONE);
            floortypelist.add("Stilt");
            floortypelist.add("Ground Floor");
            floortypelist.add("Upper Ground Floor");
            floortypelist.add("First Floor");
            floortypelist.add("Second Floor");
            floortypelist.add("Third Floor");

        }
        if (selectedString.equalsIgnoreCase("Plot/Land")) {
            plot_area.setVisibility(View.VISIBLE);
            bhk_layout.setVisibility(View.GONE);
            carpet_area.setVisibility(View.GONE);


        }
        if (selectedString.equalsIgnoreCase("Builder Floor")) {
            plot_area.setVisibility(View.VISIBLE);
            bhk_layout.setVisibility(View.VISIBLE);

            carpet_area.setVisibility(View.GONE);

            floortypelist.add("Basement Floor");
            floortypelist.add("Stilt");
            floortypelist.add("Lower Ground Floor");
            floortypelist.add("Ground Floor");
            floortypelist.add("Upper Ground Floor");
            floortypelist.add("First Floor");
            floortypelist.add("Second Floor");
            floortypelist.add("Third Floor");
            floortypelist.add("Top Floor With Roof");

        }
        if (selectedString.equalsIgnoreCase("Duplex")) {
            plot_area.setVisibility(View.GONE);
            bhk_layout.setVisibility(View.VISIBLE);

            carpet_area.setVisibility(View.GONE);

            floortypelist.add("Basement Floor");
            floortypelist.add("Stilt");
            floortypelist.add("Lower Ground Floor & Ground Floor");
            floortypelist.add("Ground Floor & Upper Ground Floor");
            floortypelist.add("Upper Ground Floor & First Floor");
            floortypelist.add("First Floor & Second Floor");
            floortypelist.add("Second Floor & Third Floor");
            floortypelist.add("Third Floor & Fourth Floor");
            floortypelist.add("Fourth Floor & Fifth Floor");

        }
        if (selectedString.equalsIgnoreCase("Flat")) {
            bhk_layout.setVisibility(View.VISIBLE);

            carpet_area.setVisibility(View.GONE);

            floortypelist.add("Basement Floor");
            floortypelist.add("Stilt");
            floortypelist.add("Lower Ground Floor");
            floortypelist.add("Ground Floor");
            floortypelist.add("Upper Ground Floor");
            floortypelist.add("First Floor");
            floortypelist.add("Second Floor");
            floortypelist.add("Third Floor");
            floortypelist.add("Fourth Floor");
            floortypelist.add("Fifth Floor");
            floortypelist.add("Sixth Floor");
            floortypelist.add("Seventh Floor");
            floortypelist.add("Eight Floor");
            floortypelist.add("Ninth Floor");
            floortypelist.add("Tenth Floor");
            floortypelist.add("Eleventh Floor");
            floortypelist.add("Twelfth Floor");
            floortypelist.add("Thirteenth Floor");
            floortypelist.add("Fourteenth Floor");
            floortypelist.add("Sixteenth Floor");
            floortypelist.add("Seveteenth Floor");
            floortypelist.add("Eighteenth Floor");
            floortypelist.add("Ninteenth Floor");
            floortypelist.add("Twentieth Floor");
            floortypelist.add("Twenty First Floor");
            floortypelist.add("Twenty Second Floor");
            floortypelist.add("Twennty Third Floor");
            floortypelist.add("Twenty Fourth Floor");
            floortypelist.add("Twenty Fifth Floor");
            floortypelist.add("Pent House");

        }
        if (selectedString.equalsIgnoreCase("Shop Cum Office (SCO)")) {
            plot_area.setVisibility(View.VISIBLE);
            carpet_area.setVisibility(View.GONE);
        }
        if (selectedString.equalsIgnoreCase("Basement")) {
            carpet_area.setVisibility(View.VISIBLE);
        }
        if (selectedString.equalsIgnoreCase("Farm House")) {

            plot_area.setVisibility(View.VISIBLE);
            carpet_area.setVisibility(View.GONE);
            bhk_layout.setVisibility(View.VISIBLE);
        }
        if (selectedString.equalsIgnoreCase("Agriculture Land")) {
            plot_area.setVisibility(View.VISIBLE);
        }
        if (selectedString.equalsIgnoreCase("Commercial Land")) {
            plot_area.setVisibility(View.VISIBLE);
        }
        if (selectedString.equalsIgnoreCase("Factory/Building/Warehouse")) {
            plot_area.setVisibility(View.VISIBLE);
            carpet_area.setVisibility(View.GONE);
        }

        if (selectedString.equalsIgnoreCase("Shop/Showroom") || selectedString.equalsIgnoreCase("Office Space")) {
            carpet_area.setVisibility(View.VISIBLE);
            floortypelist.add("Basement Floor");
            floortypelist.add("Stilt");
            floortypelist.add("Lower Ground Floor");
            floortypelist.add("Ground Floor");
            floortypelist.add("Upper Ground Floor");
            floortypelist.add("First Floor");
            floortypelist.add("Second Floor");
            floortypelist.add("Third Floor");
            floortypelist.add("Fourth Floor");
            floortypelist.add("Fifth Floor");
            floortypelist.add("Sixth Floor");
            floortypelist.add("Seventh Floor");
            floortypelist.add("Eight Floor");
            floortypelist.add("Ninth Floor");
            floortypelist.add("Tenth Floor");
            floortypelist.add("Eleventh Floor");
            floortypelist.add("Twelfth Floor");
            floortypelist.add("Thirteenth Floor");
            floortypelist.add("Fourteenth Floor");
            floortypelist.add("Sixteenth Floor");
            floortypelist.add("Seveteenth Floor");
            floortypelist.add("Eighteenth Floor");
            floortypelist.add("Ninteenth Floor");
            floortypelist.add("Twentieth Floor");
            floortypelist.add("Twenty First Floor");
            floortypelist.add("Twenty Second Floor");
            floortypelist.add("Twennty Third Floor");
            floortypelist.add("Twenty Fourth Floor");
            floortypelist.add("Twenty Fifth Floor");
        }
    }
}