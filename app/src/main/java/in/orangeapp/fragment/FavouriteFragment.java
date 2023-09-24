package in.orangeapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import in.orangeapp.adapter.Property_listAdapter;
import in.orangeapp.item.Property_model;
import in.orangeapp.util.Constant;
import in.orangeapp.util.YourPreference;
import in.orangeapp.realestate.R;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class FavouriteFragment extends Fragment {
    RecyclerView properties_reycle;
    Toolbar toolbar;

    ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.row_recyclerview_home, container, false);
        properties_reycle = rootView.findViewById(R.id.properties_reycle);
        search_property();


        return rootView;
    }


    private void search_property() {
        YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
        String user_id= yourPrefrence.getData("user_id");

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        String url = Constant.SERVER_URLMAIN + "favlist";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        Map<String, String> params = new HashMap<>();
        params.put("user_id",user_id);
        JSONObject parameters = new JSONObject(params);
        Log.i("my_property par]",""+parameters);

        JsonObjectRequest stringRequest = new JsonObjectRequest(POST, url, parameters, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("my_property Res",""+response);
                dialog.dismiss();

                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    //  Toast.makeText(getApplicationContext(), ""+obj.getString("message"), Toast.LENGTH_SHORT).show();

                    String r_code = obj.getString("status");

                    if (r_code.equalsIgnoreCase("1")) {

                        try {

                            List<Property_model> trasactionlist = new GsonBuilder().create().fromJson(obj.getJSONArray("data").toString(), new TypeToken<List<Property_model>>() {
                            }.getType());

                            Property_listAdapter newsAdapter = new Property_listAdapter(trasactionlist, getContext(),"0","favourite","",1);
                            properties_reycle.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                            properties_reycle.setAdapter(newsAdapter);
                            newsAdapter.notifyDataSetChanged();


                        } catch (Exception e) {
                            Log.i("Transaction exception", "" + e);
                            e.printStackTrace();
                        }
                    }




                } catch (Exception e) {
                    Log.i("my_property Exc",""+e);

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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


    }


}
