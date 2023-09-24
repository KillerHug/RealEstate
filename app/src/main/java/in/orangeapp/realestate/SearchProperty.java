package in.orangeapp.realestate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import in.orangeapp.util.Constant;
import in.orangeapp.util.YourPreference;
import in.orangeapp.realestate.R;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

public class SearchProperty extends AppCompatActivity {

    Toolbar toolbar;
    ArrayList<String> propertytype = new ArrayList<String>();
    ProgressDialog dialog;

    SpinnerDialog searchableSpinner;
    SpinnerDialog searchableSpinner3;
    SpinnerDialog searchableSpinner4;
    SpinnerDialog floortypespinner;
    SpinnerDialog bhktypespinner;
    SpinnerDialog plot_sizespinner;
    SpinnerDialog carpet_sizespinner;
    LinearLayout bhk_layout, floor_type_layout;
    ArrayList<String> statelist = new ArrayList<String>();
    ArrayList<String> state_id_list = new ArrayList<String>();
    ArrayList<String> districtlist = new ArrayList<String>();
    RadioGroup main_type, sub_type;
    String main_type_str = "sale/purchase", subtype_str = "residential";
    EditText property_type, locality, distrcit, state, edt_property_id, bhk, plot_area, carpet_area, floor_type, plot_area_villah;
    MaterialButton search, id_search;
    String namee;
    String mobile;
    String plot_size_string = "", carpet_size_string = "";
    ImageView ivDistrictArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_property);
        toolbar = findViewById(R.id.toolbar);
        property_type = findViewById(R.id.property_type);
        ivDistrictArrow = findViewById(R.id.ivDistrictArrow);
        floor_type = findViewById(R.id.floor_type);
        bhk_layout = findViewById(R.id.bhk_layout);
        floor_type_layout = findViewById(R.id.floor_type_layout);
        locality = findViewById(R.id.locality);
        bhk = findViewById(R.id.bhk);
        search = findViewById(R.id.search);
        carpet_area = findViewById(R.id.carpet_area);
        plot_area = findViewById(R.id.plot_area);
        id_search = findViewById(R.id.id_search);
        edt_property_id = findViewById(R.id.edt_property_id);
        distrcit = findViewById(R.id.distrcit);
        state = findViewById(R.id.state);
        main_type = findViewById(R.id.main_type);
        sub_type = findViewById(R.id.sub_type);
        toolbar.setTitle("Search  Property");
        setSupportActionBar(toolbar);
        ivDistrictArrow.setEnabled(false);
        distrcit.setEnabled(false);
        state_distric_spinner();
        getstate();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (property_type.getText().toString().equalsIgnoreCase("")) {
                    property_type.setError("*Required");

                } else if (state.getText().toString().equalsIgnoreCase("")) {
                    state.setError("*Required");

                } else if (distrcit.getText().toString().equalsIgnoreCase("")) {
                    distrcit.setError("*Required");

                } else if (locality.getText().toString().equalsIgnoreCase("")) {
                    locality.setError("*Required");

                } else {
                    search_property();

                }
            }
        });

        id_search.setOnClickListener(view -> {
            if (edt_property_id.getText().toString().equalsIgnoreCase("")) {
                edt_property_id.setError("*Required");
            } else {
                search_property();
            }
        });
        main_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.sale_purchase_ratio:
                        main_type_str = "sale/purchase";
                        break;
                    case R.id.rent_lease:
                        main_type_str = "rent/lease";
                        break;
                    /*case R.id.purchase_ratio:
                        main_type_str = "purchase";
                        break;*/

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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }


    private void state_distric_spinner() {


        ArrayList<String> bhklist = new ArrayList<String>();
        bhklist.add("Any");
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

        bhktypespinner = new SpinnerDialog(SearchProperty.this, bhklist, "Select BHK", "Cancel");// With No Animation

        bhktypespinner.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                if (position == 0) {
                    bhk.setHint("Any");
                    bhk.setText("");

                } else {
                    bhk.setText(item);
                }

            }
        });

        plot_sizespinner = new SpinnerDialog(SearchProperty.this, plot_size_list, "Select Plot size", "Cancel");// With No Animation

        plot_sizespinner.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                plot_area.setText(item);

                if (position == 0) {
                    plot_size_string = "";
                }
                if (position == 1) {
                    plot_size_string = "0,99";
                }
                if (position == 2) {
                    plot_size_string = "100,199";
                }
                if (position == 3) {
                    plot_size_string = "200,299";
                }
                if (position == 4) {
                    plot_size_string = "300,399";
                }
                if (position == 5) {
                    plot_size_string = "400,499";
                }
                if (position == 6) {
                    plot_size_string = "500,599";
                }
                if (position == 7) {
                    plot_size_string = "600,999";
                }
                if (position == 8) {
                    plot_size_string = "1000,100000000000";
                }


            }
        });
        carpet_sizespinner = new SpinnerDialog(SearchProperty.this, carpet_size_list, "Select Plot size", "Cancel");// With No Animation

        carpet_sizespinner.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                carpet_area.setText(item);

                if (position == 9) {
                    carpet_size_string = "";
                }
                if (position == 1) {
                    carpet_size_string = "0,99";
                }
                if (position == 2) {
                    carpet_size_string = "100,249";
                }
                if (position == 3) {
                    carpet_size_string = "250,499";
                }
                if (position == 4) {
                    carpet_size_string = "500,999";
                }
                if (position == 5) {
                    carpet_size_string = "1000,1499";
                }
                if (position == 6) {
                    carpet_size_string = "1500,1999";
                }
                if (position == 7) {
                    carpet_size_string = "2000,2499";
                }
                if (position == 8) {
                    carpet_size_string = "2500,10000000000000";
                }


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
                plot_sizespinner.showSpinerDialog();

            }
        });
        carpet_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carpet_sizespinner.showSpinerDialog();

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
                        propertytype.add("Rented Property");

                    } else {
                        propertytype.add("Farm House");
                        propertytype.add("Agriculture land");
                        propertytype.add("Floor Space Index (FSI)");

                    }
                    searchableSpinner4 = new SpinnerDialog(SearchProperty.this, propertytype, "Select Property Type", "Cancel");// With No Animation

                    searchableSpinner4.bindOnSpinerListener(new OnSpinerItemClick() {
                        @Override
                        public void onClick(String selectedString, int position) {
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
                                floor_type_layout.setVisibility(View.VISIBLE);
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
                                floor_type_layout.setVisibility(View.VISIBLE);

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
                                floor_type_layout.setVisibility(View.VISIBLE);

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

                            if (selectedString.equalsIgnoreCase("Rented property")) {

                            }
                            if (selectedString.equalsIgnoreCase("Floor Space Index (FSI)")) {


                            }
                            floortypespinner = new SpinnerDialog(SearchProperty.this, floortypelist, "Select Floor Type", "Cancel");// With No Animation
                            floortypespinner.bindOnSpinerListener(new OnSpinerItemClick() {
                                @Override
                                public void onClick(String item, int position) {

                                    if (position == 0) {

                                        floor_type.setHint(item);
                                        floor_type.setText("");

                                    } else {
                                        floor_type.setText(item);

                                    }
                                }
                            });

                        }
                    });

                    searchableSpinner4.showSpinerDialog();
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(SearchProperty.this).create();
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

        dialog = new ProgressDialog(SearchProperty.this);
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

                        searchableSpinner = new SpinnerDialog(SearchProperty.this, statelist, "Select State", "Cancel");// With No Animation

                        searchableSpinner.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, int position) {
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

        dialog = new ProgressDialog(SearchProperty.this);
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
                        searchableSpinner3 = new SpinnerDialog(SearchProperty.this, districtlist, "Select District", "Cancel");// With No Animation
                        searchableSpinner3.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String selectedString, int position) {
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


    private void search_property() {
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String user_id = yourPrefrence.getData("user_id");

        dialog = new ProgressDialog(SearchProperty.this);
        dialog.setMessage("Loading...");
        dialog.show();

        String url = Constant.SERVER_URLMAIN + "search_property";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        Map<String, String> params = new HashMap();
        params.put("user_id", user_id);
        params.put("property_id", edt_property_id.getText().toString());
        params.put("main_type", main_type_str);
        params.put("sub_type", subtype_str);
        params.put("property_type", property_type.getText().toString());
        params.put("state", state.getText().toString());
        params.put("district", distrcit.getText().toString());
        params.put("plot_area", plot_size_string);
        params.put("bhk", bhk.getText().toString());
        params.put("floor_type", floor_type.getText().toString());
        params.put("carpet_area", carpet_size_string);
        params.put("location", locality.getText().toString());
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


                    }
                    Intent intent = new Intent(SearchProperty.this, Propertylist.class);
                    intent.putExtra("response", String.valueOf(response));
                    startActivity(intent);

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
                propertytype.add("Rented Property");

            } else {
                propertytype.add("Farm House");
                propertytype.add("Agriculture land");
                propertytype.add("Floor Space Index (FSI)");

            }
            searchableSpinner4 = new SpinnerDialog(SearchProperty.this, propertytype, "Select Property Type", "Cancel");// With No Animation
            searchableSpinner4.bindOnSpinerListener(new OnSpinerItemClick() {
                @Override
                public void onClick(String selectedString, int position) {
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
                        floor_type_layout.setVisibility(View.VISIBLE);

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
                        floor_type_layout.setVisibility(View.VISIBLE);

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
                        floor_type_layout.setVisibility(View.VISIBLE);

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

                    if (selectedString.equalsIgnoreCase("Rented property")) {

                    }
                    if (selectedString.equalsIgnoreCase("Floor Space Index (FSI)")) {


                    }
                    floortypespinner = new SpinnerDialog(SearchProperty.this, floortypelist, "Select Floor Type", "Cancel");// With No Animation
                    floortypespinner.bindOnSpinerListener(new OnSpinerItemClick() {
                        @Override
                        public void onClick(String item, int position) {
                            if (position == 0) {

                                floor_type.setHint(item);
                                floor_type.setText("");

                            } else {
                                floor_type.setText(item);

                            }
                        }
                    });

                }
            });

            searchableSpinner4.showSpinerDialog();
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(SearchProperty.this).create();
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
}