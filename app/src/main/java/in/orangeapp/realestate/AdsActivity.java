package in.orangeapp.realestate;

import static com.android.volley.Request.Method.POST;
import static in.orangeapp.util.Constant.SERVER_URLMAIN;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.orangeapp.adapter.AllAdsDisplayAdapter;
import in.orangeapp.item.AdsDataModel;
import in.orangeapp.item.Property_model;

public class AdsActivity extends AppCompatActivity implements AllAdsDisplayAdapter.AllAdsListener {

    RecyclerView rvAds;
    ProgressDialog dialog;
    List<AdsDataModel> adsDataModelList;
    AllAdsDisplayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);
        adsDataModelList = new ArrayList<>();

        rvAds = findViewById(R.id.rvAds);
        adapter = new AllAdsDisplayAdapter(adsDataModelList, this);
        rvAds.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllAds();
    }

    private void getAllAds() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Fetching your details.");
        dialog.show();
        String url = SERVER_URLMAIN + "show_advert";
        HashMap<String, Integer> params = new HashMap<>();
        params.put("page", 1);
        params.put("limit", 1000);
        JSONObject parameters = new JSONObject(params);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest stringRequest = new JsonObjectRequest(POST, url, parameters, response -> {
            dialog.dismiss();
            Log.i("states Response", "" + response);
            try {
                JSONObject obj = new JSONObject(String.valueOf(response));
                int r_code = obj.getInt("status");
                if (r_code == 1) {
                    adsDataModelList.clear();
                    adsDataModelList.addAll(new GsonBuilder().create().fromJson(obj.getJSONArray("data").toString(),
                            new TypeToken<List<AdsDataModel>>() {
                    }.getType()));
                    adapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                Log.i("states Exception", "" + e);
                e.printStackTrace();
            }
        }, error -> {
            dialog.dismiss();
            Log.i("state Error", "" + error);
        });
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onDeleteAds(int id) {
        String url = SERVER_URLMAIN + "del_advert";
        HashMap<String, Integer> params = new HashMap<>();
        params.put("id", id);
        JSONObject parameters = new JSONObject(params);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest stringRequest = new JsonObjectRequest(POST, url, parameters, response -> {
            dialog.dismiss();
            Log.i("states Response", "" + response);
            try {
                JSONObject obj = new JSONObject(String.valueOf(response));
                int r_code = obj.getInt("status");
                if (r_code == 1) {
                    getAllAds();
                    Toast.makeText(this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.i("states Exception", "" + e);
                e.printStackTrace();
            }
        }, error -> {
            dialog.dismiss();
            Log.i("state Error", "" + error);
        });
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }
}