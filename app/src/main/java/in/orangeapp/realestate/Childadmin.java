package in.orangeapp.realestate;

import static com.android.volley.Request.Method.POST;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import in.orangeapp.adapter.SubadminAdapter;
import in.orangeapp.item.SubadminModel;
import in.orangeapp.util.YourPreference;
import in.orangeapp.util.Constant;
import in.orangeapp.realestate.R;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Childadmin extends AppCompatActivity {
    Toolbar toolbar;
    ProgressDialog dialog;
    RecyclerView recyclerView;
    TextView name_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_childadmin);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        name_txt = findViewById(R.id.name_txt);
        toolbar.setTitle("My SubAdmin");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        String name= getIntent().getExtras().getString("name");
        name_txt.setText(name);
        user_list();
    }
    private void user_list() {
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String user_id= getIntent().getExtras().getString("parent_id");

        dialog = new ProgressDialog(Childadmin.this);
        dialog.setMessage("Loading...");
        dialog.show();

        String url = Constant.SERVER_URLMAIN + "user_list";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        Map<String, String> params = new HashMap();
        params.put("parent_id",user_id);
        params.put("from","child");
        JSONObject parameters = new JSONObject(params);
        Log.i("user_list par]",""+parameters);

        JsonObjectRequest stringRequest = new JsonObjectRequest(POST, url, parameters, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("user_list Res",""+response);
                dialog.dismiss();

                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    //  Toast.makeText(getApplicationContext(), ""+obj.getString("message"), Toast.LENGTH_SHORT).show();

                    String r_code = obj.getString("status");

                    if (r_code.equalsIgnoreCase("1")) {

                        try {

                            List<SubadminModel> trasactionlist = new GsonBuilder().create().fromJson(obj.getJSONArray("data").toString(), new TypeToken<List<SubadminModel>>() {
                            }.getType());

                            SubadminAdapter newsAdapter = new SubadminAdapter(trasactionlist, Childadmin.this,"child");
                            recyclerView.setLayoutManager(new LinearLayoutManager(Childadmin.this, LinearLayoutManager.VERTICAL, false));
                            recyclerView.setAdapter(newsAdapter);
                            newsAdapter.notifyDataSetChanged();


                        } catch (Exception e) {
                            Log.i("Transaction exception", "" + e);
                            e.printStackTrace();
                        }

                    }

                } catch (Exception e) {
                    Log.i("search_property Exc",""+e);

                    e.printStackTrace();

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

                Log.i("search_property Error",""+error);

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

}