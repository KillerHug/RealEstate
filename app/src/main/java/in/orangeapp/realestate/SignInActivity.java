package in.orangeapp.realestate;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import in.orangeapp.util.Constant;
import in.orangeapp.util.IntroSlider;
import in.orangeapp.util.YourPreference;
import in.orangeapp.realestate.R;
import com.facebook.CallbackManager;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.android.volley.Request.Method.POST;

public class SignInActivity extends AppCompatActivity {

    EditText edtphone;
    ProgressDialog dialog;

    TextInputEditText edtPassword;
    Button btnSingIn, btnSkip;
    //Facebook login
    private CallbackManager callbackManager;
    private static final String EMAIL = "email";
    TextView textSignUp,textView_forget_password_login;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i =new Intent(getApplicationContext(), IntroSlider.class);
        startActivity(i);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_in);

        //facebook button
        callbackManager = CallbackManager.Factory.create();
        edtphone = findViewById(R.id.editText_email_login_activity);
        textView_forget_password_login = findViewById(R.id.textView_forget_password_login);
        edtPassword = findViewById(R.id.editText_password_login_activity);
        btnSingIn = findViewById(R.id.button_login_activity);
        btnSkip = findViewById(R.id.button_skip_login_activity);
        textSignUp = findViewById(R.id.textView_signup_login);

        btnSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view = SignInActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                if (edtphone.getText().toString().length()!=10)
                {
                    edtphone.setError("*Required");
                    edtphone.requestFocus();
                }else if (edtPassword.getText().toString().equalsIgnoreCase(""))
                {
                    edtPassword.setError("*Required");
                    edtphone.requestFocus();
                }else {
                    userSignupRequest();
                }

            }
        });

        textSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("from","1");
                startActivity(intent);
            }
        });

        textView_forget_password_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("from","1");
                startActivity(intent);
            }
        });




    }


    private void userSignupRequest() {


        dialog = new ProgressDialog(SignInActivity.this);
        dialog.setMessage("Fetching your details.");
        dialog.show();

        String url = Constant.SERVER_URLMAIN + "user_login";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        Map<String, String> params = new HashMap();
        params.put("mobile", edtphone.getText().toString());
        params.put("password", edtPassword.getText().toString());
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
                        String user_id = obj.optString("user_id");
                        String name = obj.optString("name");
                        String state = obj.optString("state");
                        String district = obj.optString("district");
                        String company_firm = obj.optString("company_firm");
                        String type = obj.optString("type");

                        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                        yourPrefrence.saveData("signup","1");
                        yourPrefrence.saveData("name",name);
                        yourPrefrence.saveData("user_id",user_id);
                        yourPrefrence.saveData("state",state);
                        yourPrefrence.saveData("district",district);
                        yourPrefrence.saveData("company_firm",company_firm);
                        yourPrefrence.saveData("type",type);

                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


    }
}
