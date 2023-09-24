package in.orangeapp.realestate;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import in.orangeapp.util.API;
import in.orangeapp.util.JsonUtils;
import in.orangeapp.util.Constant;
import in.mobsandgeeks.saripaar.Rule;
import in.mobsandgeeks.saripaar.Validator;
import in.mobsandgeeks.saripaar.annotation.Email;
import in.mobsandgeeks.saripaar.annotation.Required;
import in.mobsandgeeks.saripaar.annotation.TextRule;
import in.orangeapp.realestate.R;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class ContactUsActivity extends AppCompatActivity implements Validator.ValidationListener {

    Toolbar toolbar;

    @Required(order = 1)
    @TextRule(order = 2, minLength = 3, maxLength = 35, trim = true, message = "Enter Valid Full Name")
    EditText edtFullName;

    @Required(order = 3)
    @Email(order = 4, message = "Please Check and Enter a valid Email Address")
    EditText edtEmail;

    @TextRule(order = 5, message = "Enter valid Phone Number", minLength = 0, maxLength = 14)
    EditText edtMobile;

    EditText edtSubject;
    EditText edtDescription;
    Button btnSubmit;
    String strFullname, strEmail, strMobi, strSubject, strDescription, strMessage;
    private Validator validator;
    JsonUtils jsonUtils;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.menu_contact_us));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        jsonUtils = new JsonUtils(this);
        jsonUtils.forceRTLIfSupported(getWindow());

        edtFullName = findViewById(R.id.edt_name);
        edtEmail = findViewById(R.id.edt_email);
        edtMobile = findViewById(R.id.edt_phone);
        edtSubject = findViewById(R.id.edt_subject);
        edtDescription = findViewById(R.id.edt_description);
        btnSubmit = findViewById(R.id.btn_submit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                validator.validateAsync();
            }
        });

        validator = new Validator(this);
        validator.setValidationListener(this);

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

    @Override
    public void onValidationSucceeded() {
        strFullname = edtFullName.getText().toString().replace(" ", "%20");
        strEmail = edtEmail.getText().toString();
        strSubject = edtSubject.getText().toString().replace(" ", "%20");
        strDescription = edtDescription.getText().toString().replace(" ", "%20");
        strMobi = edtMobile.getText().toString();

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(ContactUsActivity.this));
        jsObj.addProperty("method_name", "contact_us");
        jsObj.addProperty("name", strFullname);
        jsObj.addProperty("email", strEmail);
        jsObj.addProperty("phone", strMobi);
        jsObj.addProperty("subject", strSubject);
        jsObj.addProperty("message", strDescription);
        if (JsonUtils.isNetworkAvailable(ContactUsActivity.this)) {
            new MyTaskContact(API.toBase64(jsObj.toString())).execute(Constant.API_URL);
         }

    }

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        String message = failedRule.getFailureMessage();
        if (failedView instanceof EditText) {
            failedView.requestFocus();
            ((EditText) failedView).setError(message);
        } else {
            Toast.makeText(this, "Record Not Saved", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class MyTaskContact extends AsyncTask<String, Void, String> {

        ProgressDialog pDialog;

        String base64;

        private MyTaskContact(String base64) {
            this.base64 = base64;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ContactUsActivity.this);
            pDialog.setMessage(getString(R.string.loading_title));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0], base64);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (null != pDialog && pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (null == result || result.length() == 0) {
                showToast(getString(R.string.nodata));
            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
                    JSONObject objJson;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        strMessage = objJson.getString(Constant.MSG);
                        Constant.GET_SUCCESS_MSG = objJson.getInt(Constant.SUCCESS);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setResult();
            }
        }
    }

    public void setResult() {

        if (Constant.GET_SUCCESS_MSG == 0) {
            edtEmail.setText("");
            edtEmail.requestFocus();
            showToast(strMessage);
        } else {
            showToast(strMessage);
            finish();
        }
    }

    public void showToast(String msg) {
        Toast.makeText(ContactUsActivity.this, msg, Toast.LENGTH_LONG).show();
    }
}
