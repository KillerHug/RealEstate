package in.orangeapp.realestate;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;
import static in.orangeapp.util.Constant.SERVER_URLMAIN;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.orangeapp.adapter.MultipleCityAdapter;
import in.orangeapp.item.CityModel;
import in.orangeapp.item.StateModel;


public class MultipleSelectCityFragment extends DialogFragment {
    Context context;
    ArrayList<String> statelist = new ArrayList();
    ArrayList<String> stateIdList = new ArrayList();
    RecyclerView rvState;
    ImageView close;
    Button btnSubmit;
    MultipleCityAdapter adapter;
    OnCityClickListener onCityClickListener;
    Spinner spinner_state;
    ProgressDialog dialog;
    ArrayList<CityModel> districtlist = new ArrayList<CityModel>();

    public MultipleSelectCityFragment(CreateAdvertisementActivity createAdvertisementActivity) {
        context = createAdvertisementActivity;
        onCityClickListener = (OnCityClickListener) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(requireContext(), R.style.DialogFragmentTheme);
        dialog.setContentView(R.layout.fragment_multiple_select_city_dialog);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(layoutParams);
        rvState = dialog.findViewById(R.id.rvStateList);
        spinner_state = dialog.findViewById(R.id.spinner_state);
        close = dialog.findViewById(R.id.ivClose);
        btnSubmit = dialog.findViewById(R.id.btnSubmit);
        close.setOnClickListener(v -> dismiss());
        btnSubmit.setOnClickListener(v -> {
            onCityClickListener.onCitySelected(adapter.getSelectedItems());
            dismiss();
        });
        adapter = new MultipleCityAdapter(districtlist, context);
        rvState.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rvState.setAdapter(adapter);
        getstate();
        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    getdistrict(stateIdList.get(position-1));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return dialog;
    }

    private void getstate() {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Fetching your details.");
        dialog.show();
        String url = SERVER_URLMAIN + "states";
        statelist.add("Select State");
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest stringRequest = new JsonObjectRequest(GET, url, null, response -> {
            dialog.dismiss();
            Log.i("states Response", "" + response);
            try {
                JSONObject obj = new JSONObject(String.valueOf(response));
                String r_code = obj.getString("status");
                if (r_code.equalsIgnoreCase("1")) {
                    JSONArray jsonArray = obj.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String state_id = jsonObject.getString("state_id");
                        String state_title = jsonObject.getString("state_title");
                        statelist.add(state_title);
                        stateIdList.add(state_id);
                    }
                }
                spinner_state.setAdapter(getAdapter(statelist));
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

    interface OnCityClickListener {
        void onCitySelected(List<CityModel> selectedItems);
    }

    private ArrayAdapter getAdapter(ArrayList<String> spinner_items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), R.layout.item_spinner_layout) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(R.id.tv_title);
                textView.setText(getItem(position));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = view.findViewById(R.id.tv_title);
                textView.setText(getItem(position));
                return view;
            }
        };

        adapter.addAll(spinner_items);
        return adapter;
    }

    private void getdistrict(String s) {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Fetching your details.");
        dialog.show();
        districtlist.clear();
        String url = SERVER_URLMAIN + "district_list";
        RequestQueue requestQueue = Volley.newRequestQueue(context);

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
                    String r_code = obj.getString("status");
                    if (r_code.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            String district_id = jsonObject2.getString("districtid");
                            String district_title = jsonObject2.getString("district_title");
                            String district_desc = jsonObject2.getString("district_description");
                            districtlist.add(new CityModel(district_id,district_title,district_desc));
                        }
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    Log.i("district_list Exception", "" + e);
                    e.printStackTrace();
                }
            }
        }, error -> {
            dialog.dismiss();
            Log.i("district_list Error", "" + error);
        });
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);

    }
}
