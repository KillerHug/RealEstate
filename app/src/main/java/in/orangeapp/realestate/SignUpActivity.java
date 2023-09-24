package in.orangeapp.realestate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import in.orangeapp.util.YourPreference;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import in.orangeapp.realestate.R;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;
import static in.orangeapp.util.Constant.SERVER_URLMAIN;

public class SignUpActivity extends AppCompatActivity {

    Button btnSignUp;
    ProgressDialog dialog;

    SpinnerDialog searchableSpinner;
    SpinnerDialog searchableSpinner2;
    SpinnerDialog searchableSpinner3;
    ArrayList<String> statelist = new ArrayList<String>();
    ArrayList<String> state_id_list = new ArrayList<String>();
    ArrayList<String> districtlist = new ArrayList<String>();
    LinearLayout company_layout, login_parent, llPassword;
    ImageView districtdrop;
    String from;

    String user_id = "",parentId="";
    TextView txtLogin, text_sign_title;
    LinearLayout iama_layout, spinner_layout, otp_laout;
    ImageView imaarrow, statearrow;
    View imaline;
    EditText user_type, otp_edt, companyname, state, distrcit, editText_name_register, editText_email_register,
            editText_phoneNo_register;

    Spinner spinner_active;
    Button send_otp, verify_otp, resend_otp;
    TextInputEditText editText_password_register;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        btnSignUp = findViewById(R.id.button_submit);
        otp_edt = findViewById(R.id.otp_edt);
        resend_otp = findViewById(R.id.resend_otp);
        otp_laout = findViewById(R.id.otp_laout);
        verify_otp = findViewById(R.id.verify_otp);
        spinner_active = findViewById(R.id.spinner_active);
        imaarrow = findViewById(R.id.imaarrow);
        imaline = findViewById(R.id.imaline);
        statearrow = findViewById(R.id.statearrow);
        text_sign_title = findViewById(R.id.text_sign_title);
        login_parent = findViewById(R.id.login_parent);
        llPassword = findViewById(R.id.llPassword);
        iama_layout = findViewById(R.id.iama_layout);
        send_otp = findViewById(R.id.send_otp);
        company_layout = findViewById(R.id.company_layout);
        spinner_layout = findViewById(R.id.spinner_layout);
        companyname = findViewById(R.id.companyname);
        user_type = findViewById(R.id.user_type);
        districtdrop = findViewById(R.id.districtdrop);
        state = findViewById(R.id.state);
        txtLogin = findViewById(R.id.textView_login_register);
        distrcit = findViewById(R.id.distrcit);
        editText_name_register = findViewById(R.id.editText_name_register);
        editText_email_register = findViewById(R.id.editText_email_register);
        editText_phoneNo_register = findViewById(R.id.editText_phoneNo_register);
        editText_password_register = findViewById(R.id.editText_password_register);

        editText_password_register.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                if (s.toString().length() > 5) {
//                    btnSignUp.setVisibility(View.VISIBLE);
//                }else {
//                    btnSignUp.setVisibility(View.GONE);
//                }
            }
        });
        from = getIntent().getExtras().getString("from");

        if (from.equalsIgnoreCase("0")) {
            String firm = getIntent().getExtras().getString("firm");
            String type = getIntent().getExtras().getString("type");
            text_sign_title.setText("Add Team Member");
            iama_layout.setVisibility(View.GONE);
            imaline.setVisibility(View.GONE);
            companyname.setEnabled(true);
//            companyname.setEnabled(false);
            companyname.setText(firm);
            user_type.setText(type);
            editText_phoneNo_register.setHint("Official Mobile Number");
        }

        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user_type.getText().toString().equalsIgnoreCase("")) {
                    user_type.setError("*Required");
                } else if (state.getText().toString().equalsIgnoreCase("")) {
                    state.setError("*Required");
                } else if (distrcit.getText().toString().equalsIgnoreCase("")) {
                    distrcit.setError("*Required");
                } else if (editText_name_register.getText().toString().equalsIgnoreCase("")) {
                    editText_name_register.setError("*Required");
                } else if (editText_email_register.getText().toString().equalsIgnoreCase("")) {
                    editText_email_register.setError("*Required");
                } else if (editText_phoneNo_register.getText().toString().equalsIgnoreCase("")) {
                    editText_phoneNo_register.setError("*Required");
                } else if (company_layout.getVisibility() == View.VISIBLE && companyname.getText().toString().equalsIgnoreCase("")) {
                    companyname.setError("*Required");
                } else {
                    sendotp_api(editText_phoneNo_register.getText().toString());
                }
            }
        });

        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText_phoneNo_register.getText().toString().length() != 10) {
                    editText_phoneNo_register.setError("*Required");
                } else {
                    sendotp_api(editText_phoneNo_register.getText().toString());
                }

            }
        });
        verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (otp_edt.getText().toString().length() != 4) {
                    otp_edt.setError("*Required 4 digit OTP");
                } else {
                    verify_otp_api(editText_phoneNo_register.getText().toString(), otp_edt.getText().toString());
                }

            }
        });
        if (from.equalsIgnoreCase("edit")) {
            btnSignUp.setText("Update");
            btnSignUp.setVisibility(View.VISIBLE);
            text_sign_title.setText("Update");
            login_parent.setVisibility(View.GONE);
            send_otp.setVisibility(View.GONE);
            editText_phoneNo_register.setClickable(false);
            editText_phoneNo_register.setFocusable(false);
            // editText_phoneNo_register.setHint("Official Mobile Number");
            spinner_layout.setVisibility(View.VISIBLE);
            String mobile = getIntent().getExtras().getString("mobile");
            String email = getIntent().getExtras().getString("email");
            String password = getIntent().getExtras().getString("password");
            String type = getIntent().getExtras().getString("type");
            String statee = getIntent().getExtras().getString("state");
            String district = getIntent().getExtras().getString("district");
            String name = getIntent().getExtras().getString("name");
            String company = getIntent().getExtras().getString("company");
            String blocked = getIntent().getExtras().getString("blocked");
            parentId = getIntent().getExtras().getString("parent");
            user_id = getIntent().getExtras().getString("user_id");
            YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
            String user_id_main = yourPrefrence.getData("user_id");

            if (user_id_main.equalsIgnoreCase("39")) {
                if (user_id.equalsIgnoreCase("39")) {
                    spinner_layout.setVisibility(View.GONE);
                }
            } else {
                if (parentId.equalsIgnoreCase("0")) {
                    spinner_layout.setVisibility(View.GONE);
                }
            }
            if (blocked.equalsIgnoreCase("1")) {
                spinner_active.setSelection(1);
            }
            if (type.equalsIgnoreCase("owner") || type.equalsIgnoreCase("user")) {
                company_layout.setVisibility(View.GONE);
            } else {
                company_layout.setVisibility(View.VISIBLE);
            }
            user_type.setText(type);
            user_type.setEnabled(false);
            imaarrow.setEnabled(false);

            state.setText(statee);
            state.setEnabled(false);
            statearrow.setEnabled(false);
            distrcit.setText(district);
            distrcit.setEnabled(false);
            districtdrop.setEnabled(false);
            editText_name_register.setText(name);
            editText_name_register.setEnabled(true);

            companyname.setText(company);
            companyname.setEnabled(true);

            editText_email_register.setText(email);
            editText_phoneNo_register.setText(mobile);
            editText_password_register.setText(password);
            llPassword.setVisibility(View.VISIBLE);


        }


        state_distric_spinner();

        getstate();


        btnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // validator.validateAsync();
                if (from.equalsIgnoreCase("0")) {

                    if (state.getText().toString().equalsIgnoreCase("")) {
                        state.setError("*Required");
                    } else if (distrcit.getText().toString().equalsIgnoreCase("")) {
                        distrcit.setError("*Required");
                    } else if (editText_name_register.getText().toString().equalsIgnoreCase("")) {
                        editText_name_register.setError("*Required");
                    } else if (editText_email_register.getText().toString().equalsIgnoreCase("")) {
                        editText_email_register.setError("*Required");
                    } else if (editText_phoneNo_register.getText().toString().equalsIgnoreCase("")) {
                        editText_phoneNo_register.setError("*Required");
                    } else if (editText_password_register.getText().toString().equalsIgnoreCase("")) {
                        editText_password_register.setError("*Required");
                    } else {
                        userSignupRequest();

                    }
                } else if (from.equalsIgnoreCase("edit")) {
                    if (editText_email_register.getText().toString().equalsIgnoreCase("")) {
                        editText_email_register.setError("*Required");
                    } else if (editText_phoneNo_register.getText().toString().equalsIgnoreCase("")) {
                        editText_phoneNo_register.setError("*Required");
                    } else if (editText_password_register.getText().toString().equalsIgnoreCase("")) {
                        editText_password_register.setError("*Required");
                    } else {
                        upxate_user();
                    }

                } else {
                    if (user_type.getText().toString().equalsIgnoreCase("")) {
                        user_type.setError("*Required");
                    } else if (state.getText().toString().equalsIgnoreCase("")) {
                        state.setError("*Required");
                    } else if (distrcit.getText().toString().equalsIgnoreCase("")) {
                        distrcit.setError("*Required");
                    } else if (editText_name_register.getText().toString().equalsIgnoreCase("")) {
                        editText_name_register.setError("*Required");
                    } else if (editText_email_register.getText().toString().equalsIgnoreCase("")) {
                        editText_email_register.setError("*Required");
                    } else if (editText_phoneNo_register.getText().toString().equalsIgnoreCase("")) {
                        editText_phoneNo_register.setError("*Required");
                    } else if (editText_password_register.getText().toString().equalsIgnoreCase("")) {
                        editText_password_register.setError("*Required");
                    } else {
                        userSignupRequest();

                    }
                }


            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


    }

    private void state_distric_spinner() {

        ArrayList<String> usertypelist = new ArrayList<String>();
        usertypelist.add("Real Estate Agent");
        usertypelist.add("Builder");
        usertypelist.add("Supervisor");
        usertypelist.add("Owner");
        usertypelist.add("User");
        //  searchableSpinner2.setSpinnerListItems(usertypelist);
        searchableSpinner2 = new SpinnerDialog(SignUpActivity.this, usertypelist, "Select User type", "Cancel");// With No Animation


        searchableSpinner2.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String selectedString, int position) {
                user_type.setText(selectedString);
                if (selectedString.equalsIgnoreCase("Real Estate Agent") || selectedString.equalsIgnoreCase("Builder")
                        || selectedString.equalsIgnoreCase("Supervisor")) {
                    company_layout.setVisibility(View.VISIBLE);
                } else {
                    company_layout.setVisibility(View.GONE);

                }
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

        user_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchableSpinner2.showSpinerDialog();
            }
        });


    }

    private void getstate() {
        dialog = new ProgressDialog(SignUpActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();

        String url = SERVER_URLMAIN + "states";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        Map<String, String> params = new HashMap();
        JsonObjectRequest stringRequest = new JsonObjectRequest(GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                Log.i("states Response", "" + response);

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

                        searchableSpinner = new SpinnerDialog(SignUpActivity.this, statelist, "Select State", "Cancel");// With No Animation
                        searchableSpinner.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String selectedString, int position) {
                                state.setText(selectedString);

                                getdistrict(state_id_list.get(position));
                            }
                        });


                    }

                } catch (Exception e) {
                    dialog.dismiss();
                    Log.i("states Exception", "" + e);

                    e.printStackTrace();

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("user_signup Error", "" + error);

            }
        });
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);


    }

    private void getdistrict(String s) {

        districtlist.clear();
        dialog = new ProgressDialog(SignUpActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();

        String url = SERVER_URLMAIN + "district_list";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        Map<String, String> params = new HashMap();
        params.put("state_id", s);
        JSONObject parameters = new JSONObject(params);
        Log.i("district parameter", "" + parameters);

        JsonObjectRequest stringRequest = new JsonObjectRequest(POST, url, parameters, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                Log.i("district_list Response", "" + response);

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
                        districtdrop.setClickable(true);
                        searchableSpinner3 = new SpinnerDialog(SignUpActivity.this, districtlist, "Select District", "Cancel");// With No Animation

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

    public void showToast(String msg) {
        Toast.makeText(SignUpActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    private void userSignupRequest() {


        dialog = new ProgressDialog(SignUpActivity.this);
        dialog.setMessage("Adding your details.");
        dialog.show();

        String parent = "";
        String url = SERVER_URLMAIN + "user_signup";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        if (from.equalsIgnoreCase("0")) {

            parent = getIntent().getExtras().getString("parent");
        } else {

        }
        Map<String, String> params = new HashMap();
        params.put("parent_id", parent);
        params.put("name", editText_name_register.getText().toString());
        params.put("email", editText_email_register.getText().toString());
        params.put("password", editText_password_register.getText().toString());
        params.put("mobile", editText_phoneNo_register.getText().toString());
        params.put("state", state.getText().toString());
        params.put("district", distrcit.getText().toString());
        params.put("type", user_type.getText().toString());
        params.put("company_firm", companyname.getText().toString());
        JSONObject parameters = new JSONObject(params);

        Log.i("user_signup Pararmenter", "" + parameters);

        JsonObjectRequest stringRequest = new JsonObjectRequest(POST, url, parameters, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                Log.i("user_signup Response", "" + response);

                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    Toast.makeText(getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_SHORT).show();

                    String r_code = obj.getString("status");

                    if (r_code.equalsIgnoreCase("1")) {

                        if (from.equalsIgnoreCase("0")) {
                            Intent intent = new Intent(getApplicationContext(), ProfileAcitivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            String user_id = obj.optString("user_id");
                            String name = obj.optString("name");
                            String state = obj.optString("state");
                            String district = obj.optString("district");
                            String company_firm = obj.optString("company_firm");
                            String type = obj.optString("type");

                            YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                            yourPrefrence.saveData("signup", "1");
                            yourPrefrence.saveData("name", name);
                            yourPrefrence.saveData("user_id", user_id);
                            yourPrefrence.saveData("state", state);
                            yourPrefrence.saveData("district", district);
                            yourPrefrence.saveData("company_firm", company_firm);
                            yourPrefrence.saveData("type", type);


                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }

                    }

                } catch (Exception e) {
                    Log.i("user_signup Exception", "" + e);

                    e.printStackTrace();

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

                Log.i("user_signup Error", "" + error);

            }
        });
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);


    }

    private void sendotp_api(String mobile) {


        dialog = new ProgressDialog(SignUpActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();

        String url = SERVER_URLMAIN + "send_otp";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        Map<String, String> params = new HashMap();
        params.put("mobile", mobile);
        JSONObject parameters = new JSONObject(params);

        Log.i("send_otp Pararmenter", "" + parameters);

        JsonObjectRequest stringRequest = new JsonObjectRequest(POST, url, parameters, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                Log.i("send_otp Response", "" + response);

                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    Toast.makeText(getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_SHORT).show();

                    String r_code = obj.getString("status");

                    if (r_code.equalsIgnoreCase("1")) {

                        new CountDownTimer(60000, 1000) {

                            public void onTick(long millisUntilFinished) {
                                resend_otp.setEnabled(false);
                                resend_otp.setText(millisUntilFinished / 1000 + " sec");
                                //here you can have your logic to set text to edittext
                            }

                            public void onFinish() {
                                resend_otp.setEnabled(true);
                                resend_otp.setText("Resend OTP");
                            }

                        }.start();
                        verify_otp.setVisibility(View.VISIBLE);
                        otp_laout.setVisibility(View.VISIBLE);
                        resend_otp.setVisibility(View.VISIBLE);
                        send_otp.setVisibility(View.GONE);

                    }

                } catch (Exception e) {
                    Log.i("user_signup Exception", "" + e);

                    e.printStackTrace();

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

                Log.i("user_signup Error", "" + error);

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);


    }


    private void verify_otp_api(String mobile, String otp) {


        dialog = new ProgressDialog(SignUpActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();

        String url = SERVER_URLMAIN + "otp_verify";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        Map<String, String> params = new HashMap();
        params.put("mobile", mobile);
        params.put("otp", otp);
        JSONObject parameters = new JSONObject(params);

        Log.i("otp_verify Pararmenter", "" + parameters);

        JsonObjectRequest stringRequest = new JsonObjectRequest(POST, url, parameters, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                Log.i("otp_verify Response", "" + response);
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    Toast.makeText(getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_SHORT).show();

                    String r_code = obj.getString("status");

                    if (r_code.equalsIgnoreCase("1")) {

                        llPassword.setVisibility(View.VISIBLE);
                        btnSignUp.setVisibility(View.VISIBLE);
                        otp_laout.setVisibility(View.GONE);
                        send_otp.setVisibility(View.GONE);
                        verify_otp.setVisibility(View.GONE);
                        resend_otp.setVisibility(View.GONE);

                    }

                } catch (Exception e) {
                    Log.i("user_signup Exception", "" + e);

                    e.printStackTrace();

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

                Log.i("user_signup Error", "" + error);

            }
        });
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);


    }

    private void upxate_user() {


        dialog = new ProgressDialog(SignUpActivity.this);
        dialog.setMessage("Adding your details.");
        dialog.show();

        String parent = "";
        String url = SERVER_URLMAIN + "edit_user";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        Map<String, String> params = new HashMap();
        params.put("parent_id", parentId);
        params.put("name", editText_name_register.getText().toString());
        params.put("email", editText_email_register.getText().toString());
        params.put("password", editText_password_register.getText().toString());
        params.put("mobile", editText_phoneNo_register.getText().toString());
        params.put("state", state.getText().toString());
        params.put("district", distrcit.getText().toString());
        params.put("type", user_type.getText().toString());
        params.put("company_firm", companyname.getText().toString());
        params.put("user_id", user_id);
        params.put("blocked", "" + spinner_active.getSelectedItemPosition());
        JSONObject parameters = new JSONObject(params);

        Log.i("user_signup Pararmenter", "" + parameters);

        JsonObjectRequest stringRequest = new JsonObjectRequest(POST, url, parameters, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                Log.i("user_signup Response", "" + response);

                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    Toast.makeText(getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_SHORT).show();

                    String r_code = obj.getString("status");

                    if (r_code.equalsIgnoreCase("1")) {
                        Intent intent = new Intent(getApplicationContext(), ProfileAcitivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }

                } catch (Exception e) {
                    Log.i("user_signup Exception", "" + e);

                    e.printStackTrace();

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

                Log.i("user_signup Error", "" + error);

            }
        });
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);


    }

    public void state(View view) {
        searchableSpinner.showSpinerDialog();

    }

    public void district(View view) {
        try {
            searchableSpinner3.showSpinerDialog();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void iama(View view) {
        searchableSpinner2.showSpinerDialog();

    }
}
