package in.orangeapp.fragment;

import static com.android.volley.Request.Method.POST;
import static in.orangeapp.util.Constant.SERVER_URLMAIN;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import in.orangeapp.adapter.Property_listAdapterwithAds;
import in.orangeapp.item.Property_model;
import in.orangeapp.realestate.AddRequirementActivity;
import in.orangeapp.realestate.Addproperty;
import in.orangeapp.realestate.SearchProperty;
import in.orangeapp.realestate.R;
import in.orangeapp.util.GlobalBus;
import in.orangeapp.util.YourPreference;

import com.google.android.material.button.MaterialButton;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements Property_listAdapterwithAds.OnCTAButtonClick {

    ProgressDialog dialog;
    RecyclerView advert_recycle;
    TextView name, firm_name, location;
    ImageView add_property, search_prop;
    MaterialButton btn_postRequirement;
    LinearLayout llAddProperty;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        advert_recycle = rootView.findViewById(R.id.advert_recycle);
        llAddProperty = rootView.findViewById(R.id.llAddProperty);
        add_property = rootView.findViewById(R.id.add_property);
        btn_postRequirement = rootView.findViewById(R.id.btnPostRequirement);
        search_prop = rootView.findViewById(R.id.search_prop);
        firm_name = rootView.findViewById(R.id.firm_name);
        location = rootView.findViewById(R.id.location);
        name = rootView.findViewById(R.id.name);
//      getAdvert();
        getprofile();
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Fetching your details.");
        dialog.show();

        return rootView;
    }

    private void setData(String namee, String statee, String districtt, String company_firmm) {
        name.setText(namee);
        location.setText(districtt + ", " + statee);
        if (company_firmm.equalsIgnoreCase("")) {
            firm_name.setText("");
            firm_name.setVisibility(View.GONE);
        } else {
            firm_name.setText(company_firmm);
        }
        add_property.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), Addproperty.class);
            intent.putExtra("from", "add");
            getContext().startActivity(intent);
        });
        btn_postRequirement.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddRequirementActivity.class);
            intent.putExtra("from", "add");
            getContext().startActivity(intent);
        });

        search_prop.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SearchProperty.class);
            getContext().startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        getprofile();
    }
    //    private void getadvert() {
//        YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
//        String user_id = yourPrefrence.getData("user_id");

//
//
//        String url = SERVER_URLMAIN + "advert_list";
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//
//        Map<String, String> params = new HashMap();
//        params.put("parent_id", user_id);
//        JSONObject parameters = new JSONObject(params);
//        Log.i("advert_list par]", "" + parameters);
//
//        JsonObjectRequest stringRequest = new JsonObjectRequest(POST, url, parameters, new com.android.volley.Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.i("advert_list Res", "" + response);
//
//                try {
//                    JSONObject obj = new JSONObject(String.valueOf(response));
//                    //  Toast.makeText(getApplicationContext(), ""+obj.getString("message"), Toast.LENGTH_SHORT).show();
//
//                    String r_code = obj.getString("status");
//
//                    if (r_code.equalsIgnoreCase("1")) {
//
//                        try {
//
//                            List<AdvertModel> trasactionlist = new GsonBuilder().create().fromJson(obj.getJSONArray("data").toString(), new TypeToken<List<AdvertModel>>() {
//                            }.getType());
//
//                            AdvertAdapter newsAdapter = new AdvertAdapter(trasactionlist, getActivity());
//                            advert_recycle.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//                            advert_recycle.setAdapter(newsAdapter);
//                            newsAdapter.notifyDataSetChanged();
//
//                            advert_recycle.setHasFixedSize(true);
//
//
//                        } catch (Exception e) {
//                            Log.i("Transaction exception", "" + e);
//                            e.printStackTrace();
//                        }
//
//                    }
//
//                } catch (Exception e) {
//                    Log.i("search_property Exc", "" + e);
//
//                    e.printStackTrace();
//
//                }
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Log.i("search_property Error", "" + error);
//
//            }
//        });
//        stringRequest.setShouldCache(false);
//        requestQueue.add(stringRequest);
//
//
//    }


    private void getprofile() {

        String url = SERVER_URLMAIN + "myprofile";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
        String user_id = yourPrefrence.getData("user_id");


        Map<String, String> params = new HashMap();
        params.put("user_id", user_id);
        JSONObject parameters = new JSONObject(params);

        Log.i("myprofile Pararmenter", "" + parameters);

        JsonObjectRequest stringRequest = new JsonObjectRequest(POST, url, parameters, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("myprofile Response", "" + response);

                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    //Toast.makeText(getActivity(), "" + obj.getString("message"), Toast.LENGTH_SHORT).show();

                    String r_code = obj.getString("status");


                    if (r_code.equalsIgnoreCase("1")) {
                        String user_id = obj.optString("user_id");
                        String namee = obj.optString("name");
                        String statee = obj.optString("state");
                        String districtt = obj.optString("district");
                        String company_firmm = obj.optString("company_firm");
                        String typee = obj.optString("type");
                        String profile_photo = obj.optString("profile_photo");
                        String visiting_card = obj.optString("visiting_card");
                        getHomeProperties(namee, obj.optString("mobile"), statee, districtt, company_firmm);
                        setData(namee, statee, districtt, company_firmm);
                    }

                } catch (Exception e) {
                    Log.i("user_signup Exception", "" + e);

                    e.printStackTrace();

                }
            }
        }, error -> {
            dialog.dismiss();
            Log.i("user_signup Error", "" + error);
        });
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }


    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GlobalBus.getBus().unregister(this);
    }

    private void getHomeProperties(String namee, String mobile, String statee, String districtt, String company_firmm) {
        YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
        String user_id = yourPrefrence.getData("user_id");

//        dialog = new ProgressDialog(getActivity());
//        dialog.setMessage("Loading...");
//        dialog.show();

        String url = SERVER_URLMAIN + "home_data_new";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        Map<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        JSONObject parameters = new JSONObject(params);
        Log.i("my_property par]", "" + parameters);

        JsonObjectRequest stringRequest = new JsonObjectRequest(POST, url, parameters, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("my_property Res", "" + response);

                dialog.dismiss();

                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    //  Toast.makeText(getApplicationContext(), ""+obj.getString("message"), Toast.LENGTH_SHORT).show();

                    String r_code = obj.getString("status");

                    if (r_code.equalsIgnoreCase("1")) {

                        try {

                            List<Property_model> trasactionlist = new ArrayList<>();
//                            trasactionlist.add(null);
                            trasactionlist.addAll(new GsonBuilder().create().fromJson(obj.getJSONArray("data").toString(), new TypeToken<List<Property_model>>() {
                            }.getType()));

//                            trasactionlist.removeAll(Collections.singletonList(null));

                            Property_listAdapterwithAds newsAdapter = new Property_listAdapterwithAds(trasactionlist, getContext(),
                                    "0", namee, mobile,
                                    districtt, namee, company_firmm, statee, HomeFragment.this);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

                            advert_recycle.setLayoutManager(linearLayoutManager);
                            advert_recycle.setAdapter(newsAdapter);

                            newsAdapter.notifyDataSetChanged();


                        } catch (Exception e) {
                            Log.i("Transaction exception", "" + e);
                            e.printStackTrace();
                        }
                    }


                } catch (Exception e) {
                    Log.i("my_property Exc", "" + e);

                    e.printStackTrace();

                }
            }
        }, error -> {
            dialog.dismiss();
            Log.i("search_property Error", "" + error);
        });
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


    }

    @Override
    public void onCTAClick(String cta_type, String cta_link) {
        switch (cta_type) {
            case "Visit Us":
            case "More Details":
            case "Book Now":
            case "Get Quote":
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(cta_link));
                startActivity(intent);
                return;
            case "Call Us":
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + cta_link));
                if (requireActivity().checkSelfPermission(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(callIntent);
                } else {
                    requestPermissions(new String[]{android.Manifest.permission.CALL_PHONE}, 1);
                }
                return;
            case "WhatsApp Us":
                Intent whatsAppIntent = new Intent(Intent.ACTION_VIEW);
                String url = "https://api.whatsapp.com/send?phone=+91" + cta_link;
                whatsAppIntent.setData(Uri.parse(url));

                // Check if WhatsApp is installed on the device before opening it.
                if (whatsAppIntent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivity(whatsAppIntent);
                } else {
                    Toast.makeText(requireContext(), "WhatsApp Not Install", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }
}

