package in.orangeapp.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import in.orangeapp.adapter.PropertyAdapter;
import in.orangeapp.item.ItemProperty;
import in.orangeapp.util.API;
import in.orangeapp.util.Constant;
import in.orangeapp.util.Events;
import in.orangeapp.util.GlobalBus;
import in.orangeapp.util.ItemOffsetDecoration;
import in.orangeapp.util.JsonUtils;
import in.orangeapp.realestate.MyApplication;
import in.orangeapp.realestate.R;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MyPropertiesFragment extends Fragment {

    ArrayList<ItemProperty> mListItem;
    public RecyclerView recyclerView;
    PropertyAdapter adapter;
    private ProgressBar progressBar;
    private LinearLayout lyt_not_found;
    MyApplication myApplication;
    int j = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.row_recyclerview_home, container, false);
        GlobalBus.getBus().register(this);
        mListItem = new ArrayList<>();
        myApplication=MyApplication.getInstance();
        lyt_not_found = rootView.findViewById(R.id.lyt_not_found);
        progressBar = rootView.findViewById(R.id.progressBar);
        recyclerView = rootView.findViewById(R.id.vertical_courses_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(requireActivity(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(requireActivity()));
        jsObj.addProperty("method_name", "user_property");
        jsObj.addProperty("user_id", myApplication.getUserId());
        if (JsonUtils.isNetworkAvailable(requireActivity())) {
            new getLatest(API.toBase64(jsObj.toString())).execute(Constant.API_URL);
        }

        return rootView;
    }

    @SuppressLint("StaticFieldLeak")
    private class getLatest extends AsyncTask<String, Void, String> {

        String base64;

        private getLatest(String base64) {
            this.base64 = base64;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0], base64);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            showProgress(false);
            if (null == result || result.length() == 0) {
                lyt_not_found.setVisibility(View.VISIBLE);
            } else {
                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
                    JSONObject objJson;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        ItemProperty objItem = new ItemProperty();
                        objItem.setPId(objJson.getString(Constant.PROPERTY_ID));
                        objItem.setPropertyName(objJson.getString(Constant.PROPERTY_TITLE));
                        objItem.setPropertyThumbnailB(objJson.getString(Constant.PROPERTY_IMAGE));
                        objItem.setRateAvg(objJson.getString(Constant.PROPERTY_RATE));
                        objItem.setPropertyPrice(objJson.getString(Constant.PROPERTY_PRICE));
                        objItem.setPropertyBed(objJson.getString(Constant.PROPERTY_BED));
                        objItem.setPropertyBath(objJson.getString(Constant.PROPERTY_BATH));
                        objItem.setPropertyArea(objJson.getString(Constant.PROPERTY_AREA));
                        objItem.setPropertyAddress(objJson.getString(Constant.PROPERTY_ADDRESS));
                        objItem.setPropertyPurpose(objJson.getString(Constant.PROPERTY_PURPOSE));
                        objItem.setpropertyTotalRate(objJson.getString(Constant.PROPERTY_TOTAL_RATE));
                        objItem.setFavourite(objJson.getBoolean(Constant.FAV_TAG));
                        if (i % 2 == 0) {
                            objItem.setRight(true);
                        }
                        if (Constant.SAVE_ADS_NATIVE_ON_OFF.equals("true")) {
                            if (j % Integer.parseInt(Constant.SAVE_NATIVE_CLICK_OTHER) == 0) {
                                mListItem.add( null);
                                j++;
                            }
                        }
                        mListItem.add(objItem);
                        j++;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                displayData();
            }
        }
    }

    private void displayData() {
        if (getActivity() != null) {
        adapter = new PropertyAdapter(getActivity(), mListItem);
        recyclerView.setAdapter(adapter);

        if (adapter.getItemCount() == 0) {
            lyt_not_found.setVisibility(View.VISIBLE);
        } else {
            lyt_not_found.setVisibility(View.GONE);
        }}
    }

    private void showProgress(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            lyt_not_found.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe
    public void getFavData(Events.FavReal saveFav) {
        for (int i = 0; i < mListItem.size(); i++) {
            if (mListItem.get(i) != null) {
                if (mListItem.get(i).getPId().equals(saveFav.getReId())) {
                    mListItem.get(i).setFavourite(saveFav.isFav());
                    adapter.notifyItemChanged(i);
                }
            }
        }
    }
}
