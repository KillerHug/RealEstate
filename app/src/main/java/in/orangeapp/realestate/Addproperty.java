package in.orangeapp.realestate;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;
import static in.orangeapp.util.Constant.SERVER_URLMAIN;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import in.orangeapp.util.YourPreference;
import in.orangeapp.adapter.ShortImageAdapter;
import in.orangeapp.realestate.R;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.refactor.library.SmoothCheckBox;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

//      rented property pr ek condition lgani h & floor space index (FSI)
public class Addproperty extends AppCompatActivity {
    private ActivityResultContracts.RequestMultiplePermissions multiplePermissionsContract;
    private ActivityResultLauncher<String[]> multiplePermissionLauncher;

    Toolbar toolbar;
    SpinnerDialog searchableSpinner;
    SpinnerDialog searchableSpinner2;
    SpinnerDialog searchableSpinner3;
    SpinnerDialog searchableSpinner4;
    SpinnerDialog floortypespinner;
    SpinnerDialog lifttypespinner;
    SpinnerDialog bhktypespinner;
    SpinnerDialog parkingtyprspinner;
    SpinnerDialog furnishingtypespinner;
    SpinnerDialog sellertypespinner;
    SpinnerDialog rentedspinner;
    ArrayList<String> statelist = new ArrayList<String>();
    ArrayList<String> state_id_list = new ArrayList<String>();
    ArrayList<String> districtlist = new ArrayList<String>();
    MaterialButton submit;
    ProgressDialog dialog;
    RadioButton paymentType;
    String amountType = "";
    RadioGroup radioGroup;
    ArrayList<String> propertytype = new ArrayList<String>();

    RadioButton sale_radio, rent_lease, residential, commercial, other, new_constrution_radio, old_radio, new_constrution, under_const_radio, rbLakhs, rbCrores;
    String main_type_str = "Sale", subtype_str = "Residential", cons_type = "New Construction", cons_status = "Ready to move in";
    String usps = "";

    View old_line;
    ImageView property_type_arraw;
    LinearLayout rented_property_layout, floor_type_layout, bhk_layout, lifttype_layout, furnishing_type_layout, parking_type_layout;
    TextView parkfacing_txt, main_road_txt, cornert_txt, two_side_txt, three_side_txt, swimingpool_txt, highway_txt, atrium_txt, vastu_txt;
    EditText property_type, seller_name, seller_mobile_edt, locality, plot_area, howold, distrcit, state, edt_Address, direction, price_edt, edt_description;
    RadioGroup main_type, sub_type, construotion_type, consturt_status;
    EditText carpet_area, suitable_for, lease_start_date, lease_to, lock_inperiod, rent_permonth, roi, total_investment, scheme, Licence, land_area, road_Width, total_time_for_payment, price_per_sqrft;
    EditText floor_type, bhk, seller_type, lifttype, parking_type, furnishing_type, rented_property, lease_period;
    SmoothCheckBox parkfacing_check, main_road_check, corner_property_check, two_side_check, three_side_check, swimingpool_check, highway_check, atrium_check, vastu_check;
    LinearLayout seller_type_layout, floor_villa_layout;

    String villa_floor_type = "";
    CheckBox stilt_check, ground_check, ug_floor_check, first_floor_check, second_floor_check, third_floor_check;
    String property_id = "";

    TextView add_prop_image;

    RecyclerView image_recyclerview;

    List<String> images;

    ShortImageAdapter shortImageAdapter;
    String[] PERMISSIONS = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE};
    TextView tvCount;
    ImageView ivDistrictArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproperty);
        init_views();
        multiplePermissionsContract = new ActivityResultContracts.RequestMultiplePermissions();
        multiplePermissionLauncher = registerForActivityResult(multiplePermissionsContract, new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                if (result.containsValue(false)) {
                    multiplePermissionLauncher.launch(PERMISSIONS);
                } else {
                    if (images.size() < 8)
                        addImage();
                    else
                        Toast.makeText(Addproperty.this, "You can not add more than 8 images", Toast.LENGTH_SHORT).show();
                }
            }
        });


        images = new ArrayList<>();
        shortImageAdapter = new ShortImageAdapter(images, this);
        image_recyclerview.setAdapter(shortImageAdapter);

        add_prop_image = findViewById(R.id.add_prop_image);

        add_prop_image.setOnClickListener(view -> {
            askPermissions(multiplePermissionLauncher);
        });
        ivDistrictArrow.setEnabled(false);
        distrcit.setEnabled(false);
        String from = getIntent().getExtras().getString("from");
        if (from.equalsIgnoreCase("edit")) {
            if (getIntent().getExtras().getString("img_string") != null && !getIntent().getExtras().getString("img_string").equals("")) {
                images.addAll(Arrays.asList(getIntent().getExtras().getString("img_string").split(",")));
                image_recyclerview.setVisibility(View.VISIBLE);
                shortImageAdapter.notifyDataSetChanged();
            }

            submit.setText("Update");
            property_type.setEnabled(false);
            property_type_arraw.setEnabled(false);
            String statee = getIntent().getExtras().getString("state");
            property_id = getIntent().getExtras().getString("property_id");
            String district = getIntent().getExtras().getString("district");
            String roii = getIntent().getExtras().getString("roi");
            String address = getIntent().getExtras().getString("address");
            String old_property_year = getIntent().getExtras().getString("old_property_year");
            String new_constrution_status = getIntent().getExtras().getString("new_constrution_status");
            String rent_pm = getIntent().getExtras().getString("rent_pm");
            String leasePeriod = getIntent().getExtras().getString("leasePeriod");
            String lease_start_datee = getIntent().getExtras().getString("lease_start_date");
            String lock_inperiodd = getIntent().getExtras().getString("lock_inperiod");
            String lease_too = getIntent().getExtras().getString("lease_to");
            String rented_property_type = getIntent().getExtras().getString("rented_property_type");
            String suitable_forr = getIntent().getExtras().getString("suitable_for");
            String description = getIntent().getExtras().getString("description");
            String parking_typee = getIntent().getExtras().getString("parking_type");
            String lift_type = getIntent().getExtras().getString("lift_type");
            String floor_typee = getIntent().getExtras().getString("floor_type");
            String carpet_areaa = getIntent().getExtras().getString("carpet_area");
            String furnishing_typee = getIntent().getExtras().getString("furnishing_type");
            String usp = getIntent().getExtras().getString("usp");
            String bhkk = getIntent().getExtras().getString("bhk");
            String road_width = getIntent().getExtras().getString("road_width");
            String p_feature = getIntent().getExtras().getString("p_feature");
            String location = getIntent().getExtras().getString("location");
            String sub_type = getIntent().getExtras().getString("sub_type");
            String main_type = getIntent().getExtras().getString("main_type");
            String price_psqft = getIntent().getExtras().getString("price_psqft");
            String property_typee = getIntent().getExtras().getString("property_type");
            String licence = getIntent().getExtras().getString("licence");
            String total_timefp = getIntent().getExtras().getString("total_timefp");
            String schemee = getIntent().getExtras().getString("scheme");
            String construction_type = getIntent().getExtras().getString("construction_type");
            String land_areaa = getIntent().getExtras().getString("land_area");
            String plotarea = getIntent().getExtras().getString("plotarea");
            String sellername = getIntent().getExtras().getString("sellername");
            String sellertype = getIntent().getExtras().getString("sellertype");
            String sellermobile = getIntent().getExtras().getString("sellermobile");
            String price = getIntent().getExtras().getString("price");
            String payment_type = getIntent().getExtras().getString("payment_type");
            rbLakhs.setChecked(false);
            rbCrores.setChecked(false);
            if (payment_type.equalsIgnoreCase("Lakhs")) {
                rbLakhs.setChecked(true);
            } else if (payment_type.equalsIgnoreCase("Crores")) {
                rbCrores.setChecked(true);
            }
            sale_radio.setEnabled(false);
            rent_lease.setEnabled(false);
            residential.setEnabled(false);
            commercial.setEnabled(false);
            other.setEnabled(false);
            //new_constrution_radio.setEnabled(false);
            if (usp.contains("Park Facing")) {
                parkfacing_check.setChecked(true);
            }

            if (usp.contains("Main Road Facing")) {
                main_road_check.setChecked(true);
            }

            if (usp.contains("Corner Property")) {
                corner_property_check.setChecked(true);
            }

            if (usp.contains("Two Side Open property")) {
                two_side_check.setChecked(true);
            }

            if (usp.contains("Three Side Open property")) {
                three_side_check.setChecked(true);
            }

            if (usp.contains("Swimming Pool Facing")) {
                swimingpool_check.setChecked(true);
            }

            if (usp.contains("Highway Facing")) {
                highway_check.setChecked(true);
            }

            if (usp.contains("Atrium Facing")) {
                atrium_check.setChecked(true);
            }

            if (usp.contains("Vastu Compliant Property")) {
                vastu_check.setChecked(true);
            }


            if (main_type.equalsIgnoreCase("Sale")) {
                sale_radio.setChecked(true);
                main_type_str = "Sale";
                radioGroup.setVisibility(View.VISIBLE);
            } else {
                rent_lease.setChecked(true);
                radioGroup.setVisibility(View.GONE);
                main_type_str = "rent/lease";
            }
            if (sub_type.equalsIgnoreCase("Residential")) {
                residential.setChecked(true);
            } else if (sub_type.equalsIgnoreCase("commercial")) {
                commercial.setChecked(true);
            } else {
                other.setChecked(true);
            }
            if (construction_type.equalsIgnoreCase("New Construction")) {
                new_constrution_radio.setChecked(true);
            } else {
                old_radio.setChecked(true);
            }

            if (new_constrution_status.equalsIgnoreCase("Ready to move in")) {
                new_constrution.setChecked(true);
            } else {
                under_const_radio.setChecked(true);
            }

            state.setText(statee);
            distrcit.setText(district);
            seller_name.setText(sellername);
            seller_mobile_edt.setText(sellermobile);
            seller_type.setText(sellertype);
            locality.setText(location);
            edt_Address.setText(address);
            property_type.setText(property_typee);
            howold.setText(old_property_year);
            plot_area.setText(plotarea);
            carpet_area.setText(carpet_areaa);
            suitable_for.setText(suitable_forr);
            lease_start_date.setText(lease_start_datee);
            lease_period.setText(leasePeriod);
            lease_to.setText(lease_too);
            lock_inperiod.setText(lock_inperiodd);
            rent_permonth.setText(rent_pm);
            roi.setText(roii);
            //total_investment.setText(tot);
            rented_property.setText(rented_property_type);
            scheme.setText(schemee);
            Licence.setText(licence);
            land_area.setText(land_areaa);
            road_Width.setText(road_width);
            total_time_for_payment.setText(total_timefp);
            price_per_sqrft.setText(price_psqft);

            floor_type.setText(floor_typee);
            bhk.setText(bhkk);
            direction.setText(p_feature);
            lifttype.setText(lift_type);
            parking_type.setText(parking_typee);
            price_edt.setText(price);
            edt_description.setText(description);
            furnishing_type.setText(furnishing_typee);
            property_type_conditions(property_typee);

            if (property_typee.equalsIgnoreCase("Villa/House")) {
                if (floor_typee.contains("Stilt")) {
                    stilt_check.setChecked(true);
                }
                if (floor_typee.contains("Ground Floor")) {
                    ground_check.setChecked(true);
                }
                if (floor_typee.contains("Upper Ground Floor")) {
                    ug_floor_check.setChecked(true);
                }
                if (floor_typee.contains("First Floor")) {
                    first_floor_check.setChecked(true);
                }
                if (floor_typee.contains("Second Floor")) {
                    second_floor_check.setChecked(true);
                }
                if (floor_typee.contains("Third Floor")) {
                    third_floor_check.setChecked(true);
                }
            }

        }
        state_distric_spinner();
        getstate();
        toolbar.setTitle("List Property");
        setSupportActionBar(toolbar);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usps = "";
                if (parkfacing_check.isChecked()) {
                    if (usps.equalsIgnoreCase("")) {
                        usps = parkfacing_txt.getText().toString();
                    } else {
                        usps = usps + ", " + parkfacing_txt.getText().toString();

                    }
                }
                if (main_road_check.isChecked()) {
                    if (usps.equalsIgnoreCase("")) {
                        usps = main_road_txt.getText().toString();
                    } else {
                        usps = usps + ", " + main_road_txt.getText().toString();

                    }
                }
                if (corner_property_check.isChecked()) {
                    if (usps.equalsIgnoreCase("")) {
                        usps = cornert_txt.getText().toString();
                    } else {
                        usps = usps + ", " + cornert_txt.getText().toString();

                    }
                }
                if (two_side_check.isChecked()) {
                    if (usps.equalsIgnoreCase("")) {
                        usps = two_side_txt.getText().toString();
                    } else {
                        usps = usps + ", " + two_side_txt.getText().toString();

                    }
                }
                if (three_side_check.isChecked()) {
                    if (usps.equalsIgnoreCase("")) {
                        usps = three_side_txt.getText().toString();
                    } else {
                        usps = usps + ", " + three_side_txt.getText().toString();

                    }
                }

                if (swimingpool_check.isChecked()) {
                    if (usps.equalsIgnoreCase("")) {
                        usps = swimingpool_txt.getText().toString();
                    } else {
                        usps = usps + ", " + swimingpool_txt.getText().toString();

                    }
                }

                if (highway_check.isChecked()) {
                    if (usps.equalsIgnoreCase("")) {
                        usps = highway_txt.getText().toString();
                    } else {
                        usps = usps + ", " + highway_txt.getText().toString();

                    }
                }

                if (atrium_check.isChecked()) {
                    if (usps.equalsIgnoreCase("")) {
                        usps = atrium_txt.getText().toString();
                    } else {
                        usps = usps + ", " + atrium_txt.getText().toString();

                    }
                }
                if (vastu_check.isChecked()) {
                    if (usps.equalsIgnoreCase("")) {
                        usps = vastu_txt.getText().toString();
                    } else {
                        usps = usps + ", " + vastu_txt.getText().toString();

                    }
                }


                if (stilt_check.isChecked()) {
                    if (villa_floor_type.equalsIgnoreCase("")) {
                        villa_floor_type = stilt_check.getText().toString();
                    } else {
                        villa_floor_type = villa_floor_type + ", " + stilt_check.getText().toString();

                    }
                }

                if (ground_check.isChecked()) {
                    if (villa_floor_type.equalsIgnoreCase("")) {
                        villa_floor_type = ground_check.getText().toString();
                    } else {
                        villa_floor_type = villa_floor_type + ", " + ground_check.getText().toString();

                    }
                }

                if (ug_floor_check.isChecked()) {
                    if (villa_floor_type.equalsIgnoreCase("")) {
                        villa_floor_type = ug_floor_check.getText().toString();
                    } else {
                        villa_floor_type = villa_floor_type + ", " + ug_floor_check.getText().toString();

                    }
                }

                if (first_floor_check.isChecked()) {
                    if (villa_floor_type.equalsIgnoreCase("")) {
                        villa_floor_type = first_floor_check.getText().toString();
                    } else {
                        villa_floor_type = villa_floor_type + ", " + first_floor_check.getText().toString();

                    }
                }

                if (second_floor_check.isChecked()) {
                    if (villa_floor_type.equalsIgnoreCase("")) {
                        villa_floor_type = second_floor_check.getText().toString();
                    } else {
                        villa_floor_type = villa_floor_type + ", " + second_floor_check.getText().toString();

                    }
                }

                if (third_floor_check.isChecked()) {
                    if (villa_floor_type.equalsIgnoreCase("")) {
                        villa_floor_type = third_floor_check.getText().toString();
                    } else {
                        villa_floor_type = villa_floor_type + ", " + third_floor_check.getText().toString();

                    }
                }
//-----------------------------------------------------

//                checkValidation();
//-------------------------------------------------
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
                } else if (suitable_for.getVisibility() == View.VISIBLE && suitable_for.getText().toString().equalsIgnoreCase("")) {
                    suitable_for.setError("*Required");
                } else if (floor_type_layout.getVisibility() == View.VISIBLE && floor_type.getText().toString().equalsIgnoreCase("")) {
                    floor_type.setError("*Required");
                } else if (bhk_layout.getVisibility() == View.VISIBLE && bhk.getText().toString().equalsIgnoreCase("")) {
                    bhk.setError("*Required");
                } else if (lease_period.getVisibility() == View.VISIBLE && lease_period.getText().toString().equalsIgnoreCase("")) {
                    lease_period.setError("*Required");
                } else if (lease_to.getVisibility() == View.VISIBLE && lease_to.getText().toString().equalsIgnoreCase("")) {
                    lease_to.setError("*Required");
                } else if (lock_inperiod.getVisibility() == View.VISIBLE && lock_inperiod.getText().toString().equalsIgnoreCase("")) {
                    lock_inperiod.setError("*Required");
                } else if (rent_permonth.getVisibility() == View.VISIBLE && rent_permonth.getText().toString().equalsIgnoreCase("")) {
                    rent_permonth.setError("*Required");
                } else if (roi.getVisibility() == View.VISIBLE && roi.getText().toString().equalsIgnoreCase("")) {
                    roi.setError("*Required");
                } else if (rented_property_layout.getVisibility() == View.VISIBLE && rented_property.getText().toString().equalsIgnoreCase("")) {
                    rented_property.setError("*Required");
                } else if (scheme.getVisibility() == View.VISIBLE && scheme.getText().toString().equalsIgnoreCase("")) {
                    scheme.setError("*Required");
                } else if (Licence.getVisibility() == View.VISIBLE && Licence.getText().toString().equalsIgnoreCase("")) {
                    Licence.setError("*Required");
                } else if (land_area.getVisibility() == View.VISIBLE && land_area.getText().toString().equalsIgnoreCase("")) {
                    land_area.setError("*Required");
                } else if (road_Width.getVisibility() == View.VISIBLE && road_Width.getText().toString().equalsIgnoreCase("")) {
                    road_Width.setError("*Required");
                } else if (total_time_for_payment.getVisibility() == View.VISIBLE && total_time_for_payment.getText().toString().equalsIgnoreCase("")) {
                    total_time_for_payment.setError("*Required");
                } else if (price_per_sqrft.getVisibility() == View.VISIBLE && price_per_sqrft.getText().toString().equalsIgnoreCase("")) {
                    price_per_sqrft.setError("*Required");
                } else if (direction.getVisibility() == View.VISIBLE && direction.getText().toString().equalsIgnoreCase("")) {
                    direction.setError("*Required");
                } else if (lifttype_layout.getVisibility() == View.VISIBLE && lifttype.getText().toString().equalsIgnoreCase("")) {
                    lifttype.setError("*Required");
                } else if (parking_type_layout.getVisibility() == View.VISIBLE && parking_type.getText().toString().equalsIgnoreCase("")) {
                    parking_type.setError("*Required");
                } else if (furnishing_type_layout.getVisibility() == View.VISIBLE && furnishing_type.getText().toString().equalsIgnoreCase("")) {
                    furnishing_type.setError("*Required");
                }
                /*else if (direction.getText().toString().equalsIgnoreCase("")) {
                    direction.setError("*Required");
                }*/
                else if (price_edt.getText().toString().equalsIgnoreCase("")) {
                    price_edt.setError("*Required");

                } else if (usps.equalsIgnoreCase("")) {
                    Toast.makeText(Addproperty.this, "Choose Atleast two USP", Toast.LENGTH_SHORT).show();
                } else if (radioGroup.getCheckedRadioButtonId() == -1 && main_type_str.equalsIgnoreCase("Sale")) {
                    Toast.makeText(Addproperty.this, "Amount Type Not Selected", Toast.LENGTH_SHORT).show();
                } else {
                    if (main_type_str.equalsIgnoreCase("Sale")) {
                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        paymentType = findViewById(selectedId);
                        amountType = paymentType.getText().toString();
                    } else amountType = "";
                    if (from.equalsIgnoreCase("edit")) {
                        update_property();
                    } else {
                        add_property();
                    }
                }
            }
        });


        main_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.sale_radio:
                        main_type_str = "Sale";
                        radioGroup.setVisibility(View.VISIBLE);
                        price_edt.setHint("Asking Price");
                        break;
                    case R.id.rent_lease:
                        radioGroup.setVisibility(View.GONE);
                        main_type_str = "rent/lease";
                        price_edt.setHint("Asking Price ( Rupees/Month ) ");

                        break;

                }
            }

        });
        sub_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
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
            }

        });
        construotion_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.new_constrution_radio:
                        // Toast.makeText(Addproperty.this, "text", Toast.LENGTH_SHORT).show();
                        cons_type = "New Construction";
                        cons_status = "Ready to move in";
                        consturt_status.setVisibility(View.VISIBLE);
                        old_line.setVisibility(View.GONE);
                        howold.setVisibility(View.GONE);
                        howold.setText("");
                        break;
                    case R.id.old_radio:
                        cons_type = "old";
                        //Toast.makeText(Addproperty.this, "hello", Toast.LENGTH_SHORT).show();
                        cons_status = "";
                        old_line.setVisibility(View.VISIBLE);
                        howold.setVisibility(View.VISIBLE);
                        consturt_status.setVisibility(View.GONE);
                        break;

                }
            }

        });
        consturt_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.new_constrution:
                        cons_status = "Ready to move";
                        break;
                    case R.id.under_const_radio:
                        cons_status = "Under Constrution";
                        break;

                }
            }

        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

//        add property edit text

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

    }

    private void init_views() {
        toolbar = findViewById(R.id.toolbar);
        seller_type = findViewById(R.id.seller_type);
        seller_type_layout = findViewById(R.id.seller_type_layout);
        parkfacing_txt = findViewById(R.id.parkfacing_txt);
        floor_type_layout = findViewById(R.id.floor_type_layout);
        furnishing_type_layout = findViewById(R.id.furnishing_type_layout);
        floor_villa_layout = findViewById(R.id.floor_villa_layout);
        parking_type_layout = findViewById(R.id.parking_type_layout);
        lifttype_layout = findViewById(R.id.lifttype_layout);
        bhk_layout = findViewById(R.id.bhk_layout);
        main_road_txt = findViewById(R.id.main_road_txt);
        cornert_txt = findViewById(R.id.cornert_txt);
        howold = findViewById(R.id.howold);
        plot_area = findViewById(R.id.plot_area);
        rented_property_layout = findViewById(R.id.rented_property_layout);
        old_line = findViewById(R.id.old_line);
        consturt_status = findViewById(R.id.consturt_status);
        edt_Address = findViewById(R.id.edt_Address);
        price_edt = findViewById(R.id.price_edt);
        submit = findViewById(R.id.submit);
        carpet_area = findViewById(R.id.carpet_area);
        suitable_for = findViewById(R.id.suitable_for);
        lease_start_date = findViewById(R.id.lease_start_date);
        lease_to = findViewById(R.id.lease_to);
        lock_inperiod = findViewById(R.id.lock_inperiod);
        rent_permonth = findViewById(R.id.rent_permonth);
        roi = findViewById(R.id.roi);
        total_investment = findViewById(R.id.total_investment);
        scheme = findViewById(R.id.scheme);
        Licence = findViewById(R.id.Licence);
        land_area = findViewById(R.id.land_area);
        road_Width = findViewById(R.id.road_Width);
        total_time_for_payment = findViewById(R.id.total_time_for_payment);
        price_per_sqrft = findViewById(R.id.price_per_sqrft);
        floor_type = findViewById(R.id.floor_type);
        bhk = findViewById(R.id.bhk);
        lifttype = findViewById(R.id.lifttype);
        parking_type = findViewById(R.id.parking_type);
        furnishing_type = findViewById(R.id.furnishing_type);
        rented_property = findViewById(R.id.rented_property);
        lease_period = findViewById(R.id.lease_period);
        two_side_txt = findViewById(R.id.two_side_txt);
        three_side_txt = findViewById(R.id.three_side_txt);
        edt_description = findViewById(R.id.edt_description);
        swimingpool_txt = findViewById(R.id.swimingpool_txt);
        highway_txt = findViewById(R.id.highway_txt);
        atrium_txt = findViewById(R.id.atrium_txt);
        vastu_txt = findViewById(R.id.vastu_txt);
        property_type = findViewById(R.id.property_type);
        seller_name = findViewById(R.id.seller_name);
        seller_mobile_edt = findViewById(R.id.seller_mobile_edt);
        locality = findViewById(R.id.locality);
        distrcit = findViewById(R.id.distrcit);
        state = findViewById(R.id.state);
        direction = findViewById(R.id.direction);
        main_type = findViewById(R.id.main_type);
        sub_type = findViewById(R.id.sub_type);
        construotion_type = findViewById(R.id.construotion_type);
        parkfacing_check = findViewById(R.id.parkfacing_check);
        main_road_check = findViewById(R.id.main_road_check);
        corner_property_check = findViewById(R.id.corner_property_check);
        two_side_check = findViewById(R.id.two_side_check);
        three_side_check = findViewById(R.id.three_side_check);
        swimingpool_check = findViewById(R.id.swimingpool_check);
        highway_check = findViewById(R.id.highway_check);
        atrium_check = findViewById(R.id.atrium_check);
        vastu_check = findViewById(R.id.vastu_check);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        ivDistrictArrow = findViewById(R.id.ivDistrictArrow);
        stilt_check = findViewById(R.id.stilt_check);
        sale_radio = findViewById(R.id.sale_radio);
        residential = findViewById(R.id.residential);
        ground_check = findViewById(R.id.ground_check);
        under_const_radio = findViewById(R.id.under_const_radio);
        property_type_arraw = findViewById(R.id.property_type_arraw);
        rent_lease = findViewById(R.id.rent_lease);
        new_constrution = findViewById(R.id.new_constrution);
        old_radio = findViewById(R.id.old_radio);
        other = findViewById(R.id.other);
        tvCount = findViewById(R.id.tvCount);
        new_constrution_radio = findViewById(R.id.new_constrution_radio);
        ug_floor_check = findViewById(R.id.ug_floor_check);
        first_floor_check = findViewById(R.id.first_floor_check);
        second_floor_check = findViewById(R.id.second_floor_check);
        third_floor_check = findViewById(R.id.third_floor_check);
        commercial = findViewById(R.id.commercial);
        image_recyclerview = findViewById(R.id.image_recyclerview);
        rbCrores = findViewById(R.id.radioCrore);
        rbLakhs = findViewById(R.id.radioLakh);
    }

    private void state_distric_spinner() {


        ArrayList<String> usertypelist = new ArrayList<String>();
        usertypelist.add("North Facing");
        usertypelist.add("East Facing");
        usertypelist.add("West Facing");
        usertypelist.add("South Facing");
        usertypelist.add("North East Facing");
        usertypelist.add("South East Facing");
        usertypelist.add("North West Facing");
        usertypelist.add("South West Facing");


        ArrayList<String> bhklist = new ArrayList<String>();
        bhklist.add("1 BHK");
        bhklist.add("2 BHK");
        bhklist.add("3 BHK");
        bhklist.add("4 BHK");
        bhklist.add("5 BHK");
        bhklist.add("6 BHK");

        ArrayList<String> liftlist = new ArrayList<String>();
        liftlist.add("With Lift");
        liftlist.add("Without Lift");
        liftlist.add("With Exclusive Lift");

        ArrayList<String> parkinglist = new ArrayList<String>();
        parkinglist.add("With Parking");
        parkinglist.add("Without Parking");

        ArrayList<String> furnishedlist = new ArrayList<String>();
        furnishedlist.add("Furnished");
        furnishedlist.add("UnFurnished");
        furnishedlist.add("Fully-Furnished");
        furnishedlist.add("Semi-Furnished");

        setspinner_floortype();


        ArrayList<String> sellertype = new ArrayList<String>();
        sellertype.add("Real Estate Agent");
        sellertype.add("Builder");
        sellertype.add("Supervisor");
        sellertype.add("Owner");
        sellertype.add("User");
        //  searchableSpinner2.setSpinnerListItems(usertypelist);
        sellertypespinner = new SpinnerDialog(Addproperty.this, sellertype, "Select Seller type", "Cancel");// With No Animation


        sellertypespinner.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String selectedString, int position) {
                seller_type.setError(null);
                seller_type.setText(selectedString);

            }
        });

        ArrayList<String> rentedlist = new ArrayList<String>();
        rentedlist.add("Hotel");
        rentedlist.add("Multiplex");
        rentedlist.add("Bank/ATM");
        rentedlist.add("MNC");
        rentedlist.add("Brand");
        rentedlist.add("Guest House/PG");
        rentedlist.add("Shop/SCO");
        rentedlist.add("Office");
        rentedlist.add("Warehouse");
        rentedlist.add("Others");
        //  searchableSpinner2.setSpinnerListItems(usertypelist);
        rentedspinner = new SpinnerDialog(Addproperty.this, rentedlist, "Select Seller type", "Cancel");// With No Animation


        rentedspinner.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String selectedString, int position) {
                rented_property.setError(null);
                rented_property.setText(selectedString);

            }
        });

        rented_property_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rentedspinner.showSpinerDialog();
            }
        });
        rented_property.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rentedspinner.showSpinerDialog();
            }
        });

        //searchableSpinner2.setSpinnerListItems(usertypelist);
        searchableSpinner2 = new SpinnerDialog(Addproperty.this, usertypelist, "Select Facing", "Cancel");// With No Animation
        bhktypespinner = new SpinnerDialog(Addproperty.this, bhklist, "Select BHK", "Cancel");// With No Animation
        lifttypespinner = new SpinnerDialog(Addproperty.this, liftlist, "Select Lift type", "Cancel");// With No Animation
        parkingtyprspinner = new SpinnerDialog(Addproperty.this, parkinglist, "Select Parking type", "Cancel");// With No Animation
        furnishingtypespinner = new SpinnerDialog(Addproperty.this, furnishedlist, "Select Furnished type", "Cancel");// With No Animation

        searchableSpinner2.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                direction.setError(null);
                direction.setText(item);
            }
        });


        bhktypespinner.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                bhk.setError(null);
                bhk.setText(item);
            }
        });


        lifttypespinner.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                lifttype.setError(null);
                lifttype.setText(item);
            }
        });


        parkingtyprspinner.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                parking_type.setError(null);
                parking_type.setText(item);
            }
        });


        furnishingtypespinner.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                furnishing_type.setError(null);
                furnishing_type.setText(item);
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
        seller_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sellertypespinner.showSpinerDialog();

            }
        });
        seller_type_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sellertypespinner.showSpinerDialog();

            }
        });
        furnishing_type_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                furnishingtypespinner.showSpinerDialog();

            }
        });

        furnishing_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                furnishingtypespinner.showSpinerDialog();

            }
        });

        lifttype_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lifttypespinner.showSpinerDialog();

            }
        });
        lifttype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lifttypespinner.showSpinerDialog();

            }
        });
        parking_type_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                parkingtyprspinner.showSpinerDialog();

            }
        });
        parking_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                parkingtyprspinner.showSpinerDialog();

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
        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchableSpinner2.showSpinerDialog();
            }
        });

        floor_type_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                floortypespinner.showSpinerDialog();
            }
        });
        floor_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                floortypespinner.showSpinerDialog();
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
                        propertytype.add("Rented Property");

                    } else {
                        propertytype.add("Farm House");
                        propertytype.add("Agriculture land");
                        propertytype.add("Floor Space Index (FSI)");

                    }
                    searchableSpinner4 = new SpinnerDialog(Addproperty.this, propertytype, "Select Property Type", "Cancel");// With No Animation

                    searchableSpinner4.bindOnSpinerListener(new OnSpinerItemClick() {
                        @Override
                        public void onClick(String selectedString, int position) {
                            property_type.setError(null);
                            property_type.setText(selectedString);

                            property_type_conditions(selectedString);

                        }
                    });

                    searchableSpinner4.showSpinerDialog();
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(Addproperty.this).create();
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

    private void property_type_conditions(String selectedString) {

        ArrayList<String> floortypelist = new ArrayList<String>();

        if (selectedString.equalsIgnoreCase("Villa/House")) {
            construotion_type.setVisibility(View.VISIBLE);
            consturt_status.setVisibility(View.VISIBLE);
            plot_area.setVisibility(View.VISIBLE);
            carpet_area.setVisibility(View.VISIBLE);
            // floor_type_layout.setVisibility(View.VISIBLE);
            floor_villa_layout.setVisibility(View.VISIBLE);
            floortypelist.add("Stilt");
            floortypelist.add("Ground Floor");
            floortypelist.add("Upper Ground Floor");
            floortypelist.add("First Floor");
            floortypelist.add("Second Floor");
            floortypelist.add("Third Floor");

        }
        if (selectedString.equalsIgnoreCase("Plot/Land")) {
            plot_area.setVisibility(View.VISIBLE);
            lifttype_layout.setVisibility(View.GONE);
            parking_type_layout.setVisibility(View.GONE);
            furnishing_type_layout.setVisibility(View.GONE);
            construotion_type.setVisibility(View.GONE);
            consturt_status.setVisibility(View.GONE);
            bhk_layout.setVisibility(View.GONE);
            floor_type_layout.setVisibility(View.GONE);
            carpet_area.setVisibility(View.GONE);


        }
        if (selectedString.equalsIgnoreCase("Builder Floor")) {
            plot_area.setVisibility(View.VISIBLE);
            construotion_type.setVisibility(View.VISIBLE);
            consturt_status.setVisibility(View.VISIBLE);
            bhk_layout.setVisibility(View.VISIBLE);
            floor_type_layout.setVisibility(View.VISIBLE);
            carpet_area.setVisibility(View.VISIBLE);

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
            plot_area.setVisibility(View.VISIBLE);
            construotion_type.setVisibility(View.VISIBLE);
            consturt_status.setVisibility(View.VISIBLE);
            bhk_layout.setVisibility(View.VISIBLE);
            floor_type_layout.setVisibility(View.VISIBLE);
            carpet_area.setVisibility(View.VISIBLE);

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
            construotion_type.setVisibility(View.VISIBLE);
            consturt_status.setVisibility(View.VISIBLE);
            bhk_layout.setVisibility(View.VISIBLE);
            floor_type_layout.setVisibility(View.VISIBLE);
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
            floortypelist.add("Pent House");

        }
        if (selectedString.equalsIgnoreCase("Shop Cum Office (SCO)")) {
            construotion_type.setVisibility(View.VISIBLE);
            consturt_status.setVisibility(View.VISIBLE);
            plot_area.setVisibility(View.VISIBLE);
            carpet_area.setVisibility(View.VISIBLE);
        }
        if (selectedString.equalsIgnoreCase("Basement")) {
            construotion_type.setVisibility(View.VISIBLE);
            consturt_status.setVisibility(View.VISIBLE);
            suitable_for.setVisibility(View.VISIBLE);
            carpet_area.setVisibility(View.VISIBLE);
        }
        if (selectedString.equalsIgnoreCase("Farm House")) {
            construotion_type.setVisibility(View.VISIBLE);
            consturt_status.setVisibility(View.VISIBLE);
            plot_area.setVisibility(View.VISIBLE);
            carpet_area.setVisibility(View.VISIBLE);
            bhk_layout.setVisibility(View.VISIBLE);
        }
        if (selectedString.equalsIgnoreCase("Agriculture Land")) {
            plot_area.setVisibility(View.VISIBLE);
            lifttype_layout.setVisibility(View.GONE);
            parking_type_layout.setVisibility(View.GONE);
            furnishing_type_layout.setVisibility(View.GONE);
        }
        if (selectedString.equalsIgnoreCase("Commercial Land")) {
            suitable_for.setVisibility(View.VISIBLE);
            lifttype_layout.setVisibility(View.GONE);
            parking_type_layout.setVisibility(View.GONE);
            furnishing_type_layout.setVisibility(View.GONE);

            plot_area.setVisibility(View.VISIBLE);
        }
        if (selectedString.equalsIgnoreCase("Factory/Building/Warehouse")) {
            suitable_for.setVisibility(View.VISIBLE);
            lifttype_layout.setVisibility(View.GONE);
            parking_type_layout.setVisibility(View.GONE);
            furnishing_type_layout.setVisibility(View.GONE);

            plot_area.setVisibility(View.VISIBLE);
            carpet_area.setVisibility(View.VISIBLE);
        }

        if (selectedString.equalsIgnoreCase("Shop/Showroom") || selectedString.equalsIgnoreCase("Office Space")) {
            construotion_type.setVisibility(View.VISIBLE);
            consturt_status.setVisibility(View.VISIBLE);
            suitable_for.setVisibility(View.VISIBLE);
            floor_type_layout.setVisibility(View.VISIBLE);
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

        if (selectedString.equalsIgnoreCase("Rented property")) {
            carpet_area.setVisibility(View.VISIBLE);
            rented_property_layout.setVisibility(View.VISIBLE);
            lease_start_date.setVisibility(View.VISIBLE);
            lease_to.setVisibility(View.VISIBLE);
            lease_period.setVisibility(View.VISIBLE);
            lock_inperiod.setVisibility(View.VISIBLE);
            rent_permonth.setVisibility(View.VISIBLE);
            roi.setVisibility(View.VISIBLE);
        }
        if (selectedString.equalsIgnoreCase("Floor Space Index (FSI)")) {

            scheme.setVisibility(View.VISIBLE);
            Licence.setVisibility(View.VISIBLE);
            land_area.setVisibility(View.VISIBLE);
            road_Width.setVisibility(View.VISIBLE);
            price_per_sqrft.setVisibility(View.VISIBLE);
            total_time_for_payment.setVisibility(View.VISIBLE);
            lifttype_layout.setVisibility(View.GONE);
            furnishing_type_layout.setVisibility(View.GONE);
            parking_type_layout.setVisibility(View.GONE);

        }
        floortypespinner = new SpinnerDialog(Addproperty.this, floortypelist, "Select Floor Type", "Cancel");// With No Animation
        floortypespinner.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                floor_type.setError(null);
                floor_type.setText(item);
            }
        });
    }

    private void setspinner_floortype() {


    }

    private void getstate() {

        dialog = new ProgressDialog(Addproperty.this);
        dialog.setMessage("Fetching your details.");
        dialog.show();


        String url = SERVER_URLMAIN + "states";
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

                        // searchableSpinner.setSpinnerListItems(statelist);
                        searchableSpinner = new SpinnerDialog(Addproperty.this, statelist, "Select State", "Cancel");// With No Animation
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

        dialog = new ProgressDialog(Addproperty.this);
        dialog.setMessage("Fetching your details.");
        dialog.show();
        districtlist.clear();
        String url = SERVER_URLMAIN + "district_list";
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
                        ivDistrictArrow.setEnabled(true);
                        distrcit.setEnabled(true);
                        // searchableSpinner3.setSpinnerListItems(districtlist);
                        searchableSpinner3 = new SpinnerDialog(Addproperty.this, districtlist, "Select District", "Cancel");// With No Animation

                        searchableSpinner3.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, int position) {
                                distrcit.setError(null);
                                distrcit.setText(item);
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

    private void add_property() {


        dialog = new ProgressDialog(Addproperty.this);
        dialog.setMessage("Loading...");
        dialog.show();

        String floor_typee;
        if (property_type.getText().toString().equalsIgnoreCase("Villa/House")) {
            floor_typee = villa_floor_type;
        } else {
            floor_typee = floor_type.getText().toString();

        }
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String user_id = yourPrefrence.getData("user_id");


        String url = SERVER_URLMAIN + "add_property";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        Map<String, String> params = new HashMap();
        params.put("user_id", user_id);
        params.put("main_type", main_type_str);
        params.put("sub_type", subtype_str);
        params.put("property_type", property_type.getText().toString());
        params.put("seller_name", seller_name.getText().toString());
        params.put("seller_mobile", seller_mobile_edt.getText().toString());
        params.put("state", state.getText().toString());
        params.put("district", distrcit.getText().toString());
        params.put("location", locality.getText().toString());
        params.put("address", edt_Address.getText().toString());
        params.put("construction_type", cons_type);
        params.put("p_feature", direction.getText().toString());
        params.put("usp", usps);
        params.put("price", price_edt.getText().toString());
        params.put("description", edt_description.getText().toString());
        params.put("new_constrution_status", cons_status);
        params.put("old_property_year", howold.getText().toString());
        params.put("plot_area", plot_area.getText().toString());
        params.put("carpet_area", carpet_area.getText().toString());
        params.put("suitable_for", suitable_for.getText().toString());
        params.put("lease_start_date", lease_start_date.getText().toString());
        params.put("lease_to", lease_to.getText().toString());
        params.put("lock_inperiod", lock_inperiod.getText().toString());
        params.put("rent_pm", rent_permonth.getText().toString());
        params.put("roi", roi.getText().toString());
        params.put("total_investment", total_investment.getText().toString());
        params.put("scheme", scheme.getText().toString());
        params.put("licence", Licence.getText().toString());
        params.put("land_area", land_area.getText().toString());
        params.put("road_width", road_Width.getText().toString());
        params.put("total_timefp", total_time_for_payment.getText().toString());
        params.put("price_psqft", price_per_sqrft.getText().toString());
        params.put("floor_type", floor_typee);
        params.put("bhk", bhk.getText().toString());
        params.put("lift_type", lifttype.getText().toString());
        params.put("parking_type", parking_type.getText().toString());
        params.put("furnishing_type", furnishing_type.getText().toString());
        params.put("seller_type", seller_type.getText().toString());
        params.put("rented_property_type", rented_property.getText().toString());
        params.put("lease_period", lease_period.getText().toString());
        params.put("img_list", TextUtils.join(",", images));
        params.put("payment_type", amountType);
        params.put("post_type", "Available");

        JSONObject parameters = new JSONObject(params);
        Log.i("add_property parameter]", "" + parameters);

        JsonObjectRequest stringRequest = new JsonObjectRequest(POST, url, parameters, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                dialog.dismiss();
                Log.i("add_property Response", "" + response);

                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    //  Toast.makeText(getApplicationContext(), ""+obj.getString("message"), Toast.LENGTH_SHORT).show();

                    String r_code = obj.getString("status");

                    if (r_code.equalsIgnoreCase("1")) {
                        opendialog("Property Added Successfully");
                    }

                } catch (Exception e) {
                    Log.i("add_property Exception", "" + e);

                    e.printStackTrace();

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

                Log.i("add_property Error", "" + error);

            }
        });
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void update_property() {


        dialog = new ProgressDialog(Addproperty.this);
        dialog.setMessage("Loading...");
        dialog.show();

        String floor_typee;
        if (property_type.getText().toString().equalsIgnoreCase("Villa/House")) {
            floor_typee = villa_floor_type;
        } else {
            floor_typee = floor_type.getText().toString();

        }
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String user_id = yourPrefrence.getData("user_id");


        String url = SERVER_URLMAIN + "edit_property";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        Map<String, String> params = new HashMap();
        params.put("user_id", user_id);
        params.put("main_type", main_type_str);
        params.put("sub_type", subtype_str);
        params.put("property_type", property_type.getText().toString());
        params.put("seller_name", seller_name.getText().toString());
        params.put("seller_mobile", seller_mobile_edt.getText().toString());
        params.put("state", state.getText().toString());
        params.put("district", distrcit.getText().toString());
        params.put("location", locality.getText().toString());
        params.put("address", edt_Address.getText().toString());
        params.put("construction_type", cons_type);
        params.put("p_feature", direction.getText().toString());
        params.put("usp", usps);
        params.put("price", price_edt.getText().toString());
        params.put("description", edt_description.getText().toString());
        params.put("new_constrution_status", cons_status);
        params.put("old_property_year", howold.getText().toString());
        params.put("plot_area", plot_area.getText().toString());
        params.put("carpet_area", carpet_area.getText().toString());
        params.put("suitable_for", suitable_for.getText().toString());
        params.put("lease_start_date", lease_start_date.getText().toString());
        params.put("lease_to", lease_to.getText().toString());
        params.put("lock_inperiod", lock_inperiod.getText().toString());
        params.put("rent_pm", rent_permonth.getText().toString());
        params.put("roi", roi.getText().toString());
        params.put("total_investment", total_investment.getText().toString());
        params.put("scheme", scheme.getText().toString());
        params.put("licence", Licence.getText().toString());
        params.put("land_area", land_area.getText().toString());
        params.put("road_width", road_Width.getText().toString());
        params.put("total_timefp", total_time_for_payment.getText().toString());
        params.put("price_psqft", price_per_sqrft.getText().toString());
        params.put("floor_type", floor_typee);
        params.put("bhk", bhk.getText().toString());
        params.put("lift_type", lifttype.getText().toString());
        params.put("parking_type", parking_type.getText().toString());
        params.put("furnishing_type", furnishing_type.getText().toString());
        params.put("seller_type", seller_type.getText().toString());
        params.put("rented_property_type", rented_property.getText().toString());
        params.put("lease_period", lease_period.getText().toString());
        params.put("prop_id", property_id);
        params.put("payment_type", amountType);
        params.put("img_list", TextUtils.join(",", images));
        params.put("post_type", "Available");
        JSONObject parameters = new JSONObject(params);
        Log.i("add_property parameter]", "" + parameters);

        JsonObjectRequest stringRequest = new JsonObjectRequest(POST, url, parameters, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                dialog.dismiss();
                Log.i("add_property Response", "" + response);

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
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

                Log.i("add_property Error", "" + error);

            }
        });


        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, 1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
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
                Intent intent = new Intent(Addproperty.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        deleteDialog.show();
        deleteDialog.setCancelable(false);
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
                propertytype.add("Rented Property");

            } else {
                propertytype.add("Farm House");
                propertytype.add("Agriculture land");
                propertytype.add("Floor Space Index (FSI)");

            }
            // searchableSpinner4.setSpinnerListItems(propertytype);
            searchableSpinner4 = new SpinnerDialog(Addproperty.this, propertytype, "Select Property Type", "Cancel");// With No Animation
            searchableSpinner4.bindOnSpinerListener(new OnSpinerItemClick() {
                @Override
                public void onClick(String selectedString, int position) {
                    property_type.setError(null);
                    property_type.setText(selectedString);

                    ArrayList<String> floortypelist = new ArrayList<String>();

                    if (selectedString.equalsIgnoreCase("Villa/House")) {
                        construotion_type.setVisibility(View.VISIBLE);
                        consturt_status.setVisibility(View.VISIBLE);
                        plot_area.setVisibility(View.VISIBLE);
                        carpet_area.setVisibility(View.VISIBLE);
                        // floor_type_layout.setVisibility(View.VISIBLE);
                        floor_villa_layout.setVisibility(View.VISIBLE);


                    }
                    if (selectedString.equalsIgnoreCase("Plot/Land")) {
                        plot_area.setVisibility(View.VISIBLE);
                        lifttype_layout.setVisibility(View.GONE);
                        parking_type_layout.setVisibility(View.GONE);
                        furnishing_type_layout.setVisibility(View.GONE);
                        construotion_type.setVisibility(View.GONE);
                        consturt_status.setVisibility(View.GONE);
                        bhk_layout.setVisibility(View.GONE);
                        floor_type_layout.setVisibility(View.GONE);
                        carpet_area.setVisibility(View.GONE);


                    }
                    if (selectedString.equalsIgnoreCase("Builder Floor")) {
                        plot_area.setVisibility(View.VISIBLE);
                        construotion_type.setVisibility(View.VISIBLE);
                        consturt_status.setVisibility(View.VISIBLE);
                        bhk_layout.setVisibility(View.VISIBLE);
                        floor_type_layout.setVisibility(View.VISIBLE);
                        carpet_area.setVisibility(View.VISIBLE);

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
                        plot_area.setVisibility(View.VISIBLE);
                        construotion_type.setVisibility(View.VISIBLE);
                        consturt_status.setVisibility(View.VISIBLE);
                        bhk_layout.setVisibility(View.VISIBLE);
                        floor_type_layout.setVisibility(View.VISIBLE);
                        carpet_area.setVisibility(View.VISIBLE);

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
                        construotion_type.setVisibility(View.VISIBLE);
                        consturt_status.setVisibility(View.VISIBLE);
                        bhk_layout.setVisibility(View.VISIBLE);
                        floor_type_layout.setVisibility(View.VISIBLE);
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
                        floortypelist.add("Pent House");

                    }
                    if (selectedString.equalsIgnoreCase("Shop Cum Office (SCO)")) {
                        construotion_type.setVisibility(View.VISIBLE);
                        consturt_status.setVisibility(View.VISIBLE);
                        plot_area.setVisibility(View.VISIBLE);
                        carpet_area.setVisibility(View.VISIBLE);
                    }
                    if (selectedString.equalsIgnoreCase("Basement")) {
                        construotion_type.setVisibility(View.VISIBLE);
                        consturt_status.setVisibility(View.VISIBLE);
                        suitable_for.setVisibility(View.VISIBLE);
                        carpet_area.setVisibility(View.VISIBLE);
                    }
                    if (selectedString.equalsIgnoreCase("Farm House")) {
                        construotion_type.setVisibility(View.VISIBLE);
                        consturt_status.setVisibility(View.VISIBLE);
                        plot_area.setVisibility(View.VISIBLE);
                        carpet_area.setVisibility(View.VISIBLE);
                        bhk_layout.setVisibility(View.VISIBLE);
                    }
                    if (selectedString.equalsIgnoreCase("Agriculture Land")) {
                        plot_area.setVisibility(View.VISIBLE);
                        lifttype_layout.setVisibility(View.GONE);
                        parking_type_layout.setVisibility(View.GONE);
                        furnishing_type_layout.setVisibility(View.GONE);
                    }
                    if (selectedString.equalsIgnoreCase("Commercial Land")) {
                        suitable_for.setVisibility(View.VISIBLE);
                        lifttype_layout.setVisibility(View.GONE);
                        parking_type_layout.setVisibility(View.GONE);
                        furnishing_type_layout.setVisibility(View.GONE);

                        plot_area.setVisibility(View.VISIBLE);
                    }
                    if (selectedString.equalsIgnoreCase("Factory/Building/Warehouse")) {
                        suitable_for.setVisibility(View.VISIBLE);
                        lifttype_layout.setVisibility(View.GONE);
                        parking_type_layout.setVisibility(View.GONE);
                        furnishing_type_layout.setVisibility(View.GONE);

                        plot_area.setVisibility(View.VISIBLE);
                        carpet_area.setVisibility(View.VISIBLE);
                    }

                    if (selectedString.equalsIgnoreCase("Shop/Showroom") || selectedString.equalsIgnoreCase("Office Space")) {
                        construotion_type.setVisibility(View.VISIBLE);
                        consturt_status.setVisibility(View.VISIBLE);
                        suitable_for.setVisibility(View.VISIBLE);
                        floor_type_layout.setVisibility(View.VISIBLE);
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

                    if (selectedString.equalsIgnoreCase("Rented property")) {
                        rented_property_layout.setVisibility(View.VISIBLE);
                        lease_start_date.setVisibility(View.VISIBLE);
                        lease_to.setVisibility(View.VISIBLE);
                        lease_period.setVisibility(View.VISIBLE);
                        lock_inperiod.setVisibility(View.VISIBLE);
                        rent_permonth.setVisibility(View.VISIBLE);
                        total_investment.setVisibility(View.VISIBLE);
                        roi.setVisibility(View.VISIBLE);
                        carpet_area.setVisibility(View.VISIBLE);

                    }
                    if (selectedString.equalsIgnoreCase("Floor Space Index (FSI)")) {

                        scheme.setVisibility(View.VISIBLE);
                        Licence.setVisibility(View.VISIBLE);
                        land_area.setVisibility(View.VISIBLE);
                        road_Width.setVisibility(View.VISIBLE);
                        price_per_sqrft.setVisibility(View.VISIBLE);
                        total_time_for_payment.setVisibility(View.VISIBLE);
                        lifttype.setVisibility(View.GONE);
                        furnishing_type.setVisibility(View.GONE);
                        parking_type.setVisibility(View.GONE);

                    }
                    floortypespinner = new SpinnerDialog(Addproperty.this, floortypelist, "Select Floor Type", "Cancel");// With No Animation
                    floortypespinner.bindOnSpinerListener(new OnSpinerItemClick() {
                        @Override
                        public void onClick(String item, int position) {
                            floor_type.setError(null);
                            floor_type.setText(item);
                        }
                    });

                }
            });

            searchableSpinner4.showSpinerDialog();
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(Addproperty.this).create();
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

    public void directionfacing(View view) {
        searchableSpinner2.showSpinerDialog();

    }

    public void seller_type(View view) {
        sellertypespinner.showSpinerDialog();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Log.e("TAG", "onActivityResult: " + data.getStringExtra("url"));
                    images.add(data.getStringExtra("url"));
                    image_recyclerview.setVisibility(View.VISIBLE);
                    shortImageAdapter.notifyDataSetChanged();
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Intent i = new Intent(Addproperty.this, ImagePickerActivity.class);
                    i.putExtra("uri", Objects.requireNonNull(data.getData()).toString());
                    startActivityForResult(i, 0);
                }
                break;
        }

    }

    public void addImage() {

        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, 1);
    }

    private void askPermissions(ActivityResultLauncher<String[]> multiplePermissionLauncher) {
        if (!hasPermissions(PERMISSIONS)) {
            multiplePermissionLauncher.launch(PERMISSIONS);
        } else {
            Log.d("PERMISSIONS", "All permissions are already granted");
            if (images.size() < 8)
                addImage();
            else
                Toast.makeText(Addproperty.this, "You can not add more than 8 images", Toast.LENGTH_SHORT).show();
        }
    }

    private Boolean hasPermissions(String[] permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d("PERMISSIONS", "Permission is not granted: $permission");
                    return false;
                }
                Log.d("PERMISSIONS", "Permission already granted: $permission");
            }
            return true;
        }
        return false;
    }

    private boolean checkValidation() {
//        Residential
        if (property_type.getText().toString().equalsIgnoreCase("Villa/House") ||
                property_type.getText().toString().equalsIgnoreCase("Ploat/Land")) {
            if (plot_area.getText().toString().equalsIgnoreCase("")) {
                plot_area.setError("*Required");
                return false;
            } else if (carpet_area.getText().toString().equalsIgnoreCase("")) {
                plot_area.setError(null);
                carpet_area.setError("*Required");
                return false;
            } else if (lifttype.getText().toString().equalsIgnoreCase("")) {
                carpet_area.setError(null);
                lifttype.setError("*Required");
                return false;
            } else if (parking_type.getText().toString().equalsIgnoreCase("")) {
                lifttype.setError(null);
                parking_type.setError("*Required");
                return false;
            } else if (furnishing_type.getText().toString().equalsIgnoreCase("")) {
                parking_type.setError(null);
                furnishing_type.setError("*Required");
                return false;
            } else {
                furnishing_type.setError(null);
            }
        } else if (property_type.getText().toString().equalsIgnoreCase("Ploat/Land")) {
            if (plot_area.getText().toString().equalsIgnoreCase("")) {
                plot_area.setError("*Required");
                return false;
            } else if (direction.getText().toString().equalsIgnoreCase("")) {
                plot_area.setError(null);
                direction.setError("*Required");
                return false;
            } else {
                direction.setError(null);
            }
        } else if (property_type.getText().toString().equalsIgnoreCase("Builder Floor")
                || property_type.getText().toString().equalsIgnoreCase("Duplex")) {
            if (plot_area.getText().toString().equalsIgnoreCase("")) {
                plot_area.setError("*Required");
                return false;
            } else if (carpet_area.getText().toString().equalsIgnoreCase("")) {
                plot_area.setError(null);
                carpet_area.setError("*Required");
                return false;
            } else if (floor_type.getText().toString().equalsIgnoreCase("")) {
                carpet_area.setError(null);
                floor_type.setError("*Required");
                return false;
            } else if (bhk.getText().toString().equalsIgnoreCase("")) {
                floor_type.setError(null);
                bhk.setError("*Required");
                return false;
            } else if (direction.getText().toString().equalsIgnoreCase("")) {
                bhk.setError(null);
                direction.setError("*Required");
                return false;
            } else if (lifttype.getText().toString().equalsIgnoreCase("")) {
                direction.setError(null);
                lifttype.setError("*Required");
                return false;
            } else if (parking_type.getText().toString().equalsIgnoreCase("")) {
                lifttype.setError(null);
                parking_type.setError("*Required");
                return false;
            } else if (furnishing_type.getText().toString().equalsIgnoreCase("")) {
                parking_type.setError(null);
                furnishing_type.setError("*Required");
                return false;
            } else {
                furnishing_type.setError(null);
            }
        } else if (property_type.getText().toString().equalsIgnoreCase("Flat")) {
            if (carpet_area.getText().toString().equalsIgnoreCase("")) {
                carpet_area.setError("*Required");
                return false;
            } else if (floor_type.getText().toString().equalsIgnoreCase("")) {
                carpet_area.setError(null);
                floor_type.setError("*Required");
                return false;
            } else if (bhk.getText().toString().equalsIgnoreCase("")) {
                floor_type.setError(null);
                bhk.setError("*Required");
                return false;
            } else if (direction.getText().toString().equalsIgnoreCase("")) {
                bhk.setError(null);
                direction.setError("*Required");
                return false;
            } else if (lifttype.getText().toString().equalsIgnoreCase("")) {
                direction.setError(null);
                lifttype.setError("*Required");
                return false;
            } else if (parking_type.getText().toString().equalsIgnoreCase("")) {
                lifttype.setError(null);
                parking_type.setError("*Required");
                return false;
            } else if (furnishing_type.getText().toString().equalsIgnoreCase("")) {
                parking_type.setError(null);
                furnishing_type.setError("*Required");
                return false;
            } else {
                furnishing_type.setError(null);
            }
        }
        //        Commercial
        else if (property_type.getText().toString().equalsIgnoreCase("Shop/Showroom") ||
                property_type.getText().toString().equalsIgnoreCase("Office Space")) {
            if (carpet_area.getText().toString().equalsIgnoreCase("")) {
                carpet_area.setError("*Required");
                return false;
            } else if (suitable_for.getText().toString().equalsIgnoreCase("")) {
                carpet_area.setError(null);
                suitable_for.setError("*Required");
                return false;
            } else if (floor_type.getText().toString().equalsIgnoreCase("")) {
                suitable_for.setError(null);
                floor_type.setError("*Required");
                return false;
            } else if (direction.getText().toString().equalsIgnoreCase("")) {
                floor_type.setError(null);
                direction.setError("*Required");
                return false;
            } else if (lifttype.getText().toString().equalsIgnoreCase("")) {
                direction.setError(null);
                lifttype.setError("*Required");
                return false;
            } else if (parking_type.getText().toString().equalsIgnoreCase("")) {
                lifttype.setError(null);
                parking_type.setError("*Required");
                return false;
            } else if (furnishing_type.getText().toString().equalsIgnoreCase("")) {
                parking_type.setError(null);
                furnishing_type.setError("*Required");
                return false;
            } else {
                furnishing_type.setError(null);
            }
        } else if (property_type.getText().toString().equalsIgnoreCase("Shop Cum Office(SCO")) {
            if (plot_area.getText().toString().equalsIgnoreCase("")) {
                plot_area.setError("*Required");
                return false;
            } else if (carpet_area.getText().toString().equalsIgnoreCase("")) {
                plot_area.setError(null);
                carpet_area.setError("*Required");
                return false;
            } else if (direction.getText().toString().equalsIgnoreCase("")) {
                carpet_area.setError(null);
                direction.setError("*Required");
                return false;
            } else if (lifttype.getText().toString().equalsIgnoreCase("")) {
                direction.setError(null);
                lifttype.setError("*Required");
                return false;
            } else if (parking_type.getText().toString().equalsIgnoreCase("")) {
                lifttype.setError(null);
                parking_type.setError("*Required");
                return false;
            } else if (furnishing_type.getText().toString().equalsIgnoreCase("")) {
                parking_type.setError(null);
                furnishing_type.setError("*Required");
                return false;
            } else {
                furnishing_type.setError(null);
            }
        } else if (property_type.getText().toString().equalsIgnoreCase("Basement")) {
            if (carpet_area.getText().toString().equalsIgnoreCase("")) {
                carpet_area.setError("*Required");
                return false;
            } else if (suitable_for.getText().toString().equalsIgnoreCase("")) {
                carpet_area.setError(null);
                suitable_for.setError("*Required");
                return false;
            } else if (direction.getText().toString().equalsIgnoreCase("")) {
                suitable_for.setError(null);
                direction.setError("*Required");
                return false;
            } else if (lifttype.getText().toString().equalsIgnoreCase("")) {
                direction.setError(null);
                lifttype.setError("*Required");
                return false;
            } else if (parking_type.getText().toString().equalsIgnoreCase("")) {
                lifttype.setError(null);
                parking_type.setError("*Required");
                return false;
            } else if (furnishing_type.getText().toString().equalsIgnoreCase("")) {
                parking_type.setError(null);
                furnishing_type.setError("*Required");
                return false;
            } else {
                furnishing_type.setError(null);
            }
        } else if (property_type.getText().toString().equalsIgnoreCase("Commercial Land")) {
            if (plot_area.getText().toString().equalsIgnoreCase("")) {
                plot_area.setError("*Required");
                return false;
            } else if (suitable_for.getText().toString().equalsIgnoreCase("")) {
                plot_area.setError(null);
                suitable_for.setError("*Required");
                return false;
            } else if (direction.getText().toString().equalsIgnoreCase("")) {
                suitable_for.setError(null);
                direction.setError("*Required");
                return false;
            } else {
                direction.setError(null);
            }
        } else if (property_type.getText().toString().equalsIgnoreCase("Factory/Building/Warehouse")) {
            if (plot_area.getText().toString().equalsIgnoreCase("")) {
                plot_area.setError("*Required");
                return false;
            } else if (carpet_area.getText().toString().equalsIgnoreCase("")) {
                plot_area.setError(null);
                carpet_area.setError("*Required");
                return false;
            } else if (suitable_for.getText().toString().equalsIgnoreCase("")) {
                carpet_area.setError(null);
                suitable_for.setError("*Required");
                return false;
            } else if (direction.getText().toString().equalsIgnoreCase("")) {
                suitable_for.setError(null);
                direction.setError("*Required");
                return false;
            } else {
                direction.setError(null);
            }
        } else if (property_type.getText().toString().equalsIgnoreCase("Rented Property")) {
            if (carpet_area.getText().toString().equalsIgnoreCase("")) {
                carpet_area.setError("*Required");
                return false;
            } else if (lease_start_date.getText().toString().equalsIgnoreCase("")) {
                carpet_area.setError(null);
                lease_start_date.setError("*Required");
                return false;
            } else if (lease_period.getText().toString().equalsIgnoreCase("")) {
                lease_start_date.setError(null);
                lease_period.setError("*Required");
                return false;
            } else if (lease_to.getText().toString().equalsIgnoreCase("")) {
                lease_period.setError(null);
                lease_to.setError("*Required");
                return false;
            } else if (lock_inperiod.getText().toString().equalsIgnoreCase("")) {
                lease_to.setError(null);
                lock_inperiod.setError("*Required");
                return false;
            } else if (rent_permonth.getText().toString().equalsIgnoreCase("")) {
                lock_inperiod.setError(null);
                rent_permonth.setError("*Required");
                return false;
            } else if (roi.getText().toString().equalsIgnoreCase("")) {
                rent_permonth.setError(null);
                roi.setError("*Required");
                return false;
            } else if (rented_property.getText().toString().equalsIgnoreCase("")) {
                roi.setError(null);
                rented_property.setError("*Required");
                return false;
            } else if (direction.getText().toString().equalsIgnoreCase("")) {
                rented_property.setError(null);
                direction.setError("*Required");
                return false;
            } else if (lifttype.getText().toString().equalsIgnoreCase("")) {
                direction.setError(null);
                lifttype.setError("*Required");
                return false;
            } else if (parking_type.getText().toString().equalsIgnoreCase("")) {
                lifttype.setError(null);
                parking_type.setError("*Required");
                return false;
            } else if (furnishing_type.getText().toString().equalsIgnoreCase("")) {
                parking_type.setError(null);
                furnishing_type.setError("*Required");
                return false;
            } else {
                furnishing_type.setError(null);
            }
        }
//        other
        else if (property_type.getText().toString().equalsIgnoreCase("Farm House")) {
            if (plot_area.getText().toString().equalsIgnoreCase("")) {
                plot_area.setError("*Required");
                return false;
            } else if (carpet_area.getText().toString().equalsIgnoreCase("")) {
                plot_area.setError(null);
                carpet_area.setError("*Required");
                return false;
            } else if (bhk.getText().toString().equalsIgnoreCase("")) {
                carpet_area.setError(null);
                bhk.setError("*Required");
                return false;
            } else if (direction.getText().toString().equalsIgnoreCase("")) {
                bhk.setError(null);
                direction.setError("*Required");
                return false;
            } else if (lifttype.getText().toString().equalsIgnoreCase("")) {
                direction.setError(null);
                lifttype.setError("*Required");
                return false;
            } else if (parking_type.getText().toString().equalsIgnoreCase("")) {
                lifttype.setError(null);
                parking_type.setError("*Required");
                return false;
            } else if (furnishing_type.getText().toString().equalsIgnoreCase("")) {
                parking_type.setError(null);
                furnishing_type.setError("*Required");
                return false;
            } else {
                furnishing_type.setError(null);
            }
        } else if (property_type.getText().toString().equalsIgnoreCase("Agriculture land")) {
            if (plot_area.getText().toString().equalsIgnoreCase("")) {
                plot_area.setError("*Required");
                return false;
            } else if (direction.getText().toString().equalsIgnoreCase("")) {
                plot_area.setError(null);
                direction.setError("*Required");
                return false;
            } else {
                direction.setError(null);
            }
        } else if (property_type.getText().toString().equalsIgnoreCase("Floor Space Index (FSI)")) {
            if (scheme.getText().toString().equalsIgnoreCase("")) {
                scheme.setError("*Required");
                return false;
            } else if (Licence.getText().toString().equalsIgnoreCase("")) {
                scheme.setError(null);
                Licence.setError("*Required");
                return false;
            } else if (land_area.getText().toString().equalsIgnoreCase("")) {
                Licence.setError(null);
                land_area.setError("*Required");
                return false;
            } else if (road_Width.getText().toString().equalsIgnoreCase("")) {
                land_area.setError(null);
                road_Width.setError("*Required");
                return false;
            } else if (total_time_for_payment.getText().toString().equalsIgnoreCase("")) {
                road_Width.setError(null);
                total_time_for_payment.setError("*Required");
                return false;
            } else if (price_per_sqrft.getText().toString().equalsIgnoreCase("")) {
                total_time_for_payment.setError(null);
                price_per_sqrft.setError("*Required");
                return false;
            } else if (direction.getText().toString().equalsIgnoreCase("")) {
                price_per_sqrft.setError(null);
                direction.setError("*Required");
                return false;
            } else {
                direction.setError(null);
            }
        }

        return true;
    }
}

/*else if (seller_name.getText().toString().equalsIgnoreCase("")) {
                    seller_name.setError("*Required");return false;

                } else if (seller_mobile_edt.getText().toString().equalsIgnoreCase("")) {
                    seller_mobile_edt.setError("*Required");return false;

                } */