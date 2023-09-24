package in.orangeapp.realestate;

import static com.android.volley.Request.Method.POST;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import in.orangeapp.util.Constant;
import in.orangeapp.util.JsonUtils;
import in.mobsandgeeks.saripaar.Validator;
import in.mobsandgeeks.saripaar.annotation.Email;
import in.mobsandgeeks.saripaar.annotation.Required;
import in.orangeapp.realestate.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class ForgotPasswordActivity extends AppCompatActivity {

    @Required(order = 1)
    @Email(order = 2, message = "Please Check and Enter a valid Email Address")
    EditText edtEmail;

    String strEmail, strMessage;
    private Validator validator;
    Button btnSubmit;
    JsonUtils jsonUtils;
    Toolbar toolbar;
    ProgressDialog dialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.forgot_password));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        jsonUtils = new JsonUtils(this);
        jsonUtils.forceRTLIfSupported(getWindow());

        edtEmail = findViewById(R.id.editText_fp);
        btnSubmit = findViewById(R.id.button_fp);

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
              //  validator.validateAsync();
                forot();
            }
        });


    }

    private void forot() {


        dialog = new ProgressDialog(ForgotPasswordActivity.this);
        dialog.setMessage("Fetching your details.");
        dialog.show();

        String url = Constant.SERVER_URLMAIN + "forget_password";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        Map<String, String> params = new HashMap();
        params.put("email", edtEmail.getText().toString());
         JSONObject parameters = new JSONObject(params);

        Log.i("forget_password p", "" + parameters);

        JsonObjectRequest stringRequest = new JsonObjectRequest(POST, url, parameters, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                Log.i("forget_password Res", "" + response);

                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    Toast.makeText(getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_SHORT).show();

                    String r_code = obj.getString("status");

                    if (r_code.equalsIgnoreCase("1")) {

                        Intent intent = new Intent(ForgotPasswordActivity.this, SignInActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }

                } catch (Exception e) {
                    Log.i("forget_password e", "" + e);

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

}
