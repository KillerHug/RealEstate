package in.orangeapp.realestate;

import static com.android.volley.Request.Method.GET;
import static in.orangeapp.util.Constant.SERVER_URLMAIN;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

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
import java.util.List;

import in.orangeapp.adapter.MultipleStateAdapter;
import in.orangeapp.item.StateModel;


public class MultipleSelectStatesFragment extends DialogFragment {
    Context context;
    ArrayList<StateModel> statelist = new ArrayList();
    RecyclerView rvState;
    ImageView close;
    Button btnSubmit;
    MultipleStateAdapter adapter;
    OnStateClickListener onStateClickListener;
    ProgressDialog dialog;

    public MultipleSelectStatesFragment(CreateAdvertisementActivity createAdvertisementActivity) {
        context = createAdvertisementActivity;
        onStateClickListener = (OnStateClickListener) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(requireContext(), R.style.DialogFragmentTheme);
        dialog.setContentView(R.layout.fragment_multiple_select_state_dialog);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(layoutParams);
        rvState = dialog.findViewById(R.id.rvStateList);
        close = dialog.findViewById(R.id.ivClose);
        btnSubmit = dialog.findViewById(R.id.btnSubmit);
        close.setOnClickListener(v -> dismiss());
        btnSubmit.setOnClickListener(v -> {
            onStateClickListener.onStateSelected(adapter.getSelectedItems());
            dismiss();
        });
        adapter = new MultipleStateAdapter(statelist, context);
        rvState.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rvState.setAdapter(adapter);
        getstate();
        return dialog;
    }

    private void getstate() {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Fetching your details.");
        dialog.show();
        String url = SERVER_URLMAIN + "states";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest stringRequest = new JsonObjectRequest(GET, url, null, response -> {
            Log.i("states Response", "" + response);
            dialog.dismiss();
            try {
                JSONObject obj = new JSONObject(String.valueOf(response));
                String r_code = obj.getString("status");
                if (r_code.equalsIgnoreCase("1")) {
                    JSONArray jsonArray = obj.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String state_id = jsonObject.getString("state_id");
                        String state_title = jsonObject.getString("state_title");
                        statelist.add(new StateModel(state_id, state_title));
                    }
                }
                adapter.notifyDataSetChanged();
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

    interface OnStateClickListener {
        void onStateSelected(List<StateModel> selectedItems);
    }
}
