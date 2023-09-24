package in.orangeapp.realestate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import in.orangeapp.adapter.PropertyAdapter;
import in.orangeapp.item.ItemProperty;
import in.orangeapp.util.API;
import in.orangeapp.util.BannerAds;
import in.orangeapp.util.Constant;
import in.orangeapp.util.Events;
import in.orangeapp.util.GlobalBus;
import in.orangeapp.util.ItemOffsetDecoration;
import in.orangeapp.util.JsonUtils;
import in.orangeapp.realestate.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class AdvanceSearchActivity extends AppCompatActivity {

    ArrayList<ItemProperty> mListItem;
    public RecyclerView recyclerView;
    PropertyAdapter adapter;
    private ProgressBar progressBar;
    private LinearLayout lyt_not_found;
    String typeMax,typeMin, typeId, typeVerify,typeFur;
    Toolbar toolbar;
    JsonUtils jsonUtils;
    LinearLayout adLayout;
    int j = 1;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_item);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.menu_search));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        GlobalBus.getBus().register(this);
        Intent intent = getIntent();
        jsonUtils = new JsonUtils(this);
        jsonUtils.forceRTLIfSupported(getWindow());

        typeMax = intent.getStringExtra("PriceMax");
        typeId = intent.getStringExtra("TypeId");
        typeMin = intent.getStringExtra("PriceMin");
        typeVerify = intent.getStringExtra("Verify");
        typeFur = intent.getStringExtra("Furnishing");

        mListItem = new ArrayList<>();

        lyt_not_found = findViewById(R.id.lyt_not_found);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.vertical_courses_list);
        adLayout = findViewById(R.id.adview);
        BannerAds.showBannerAds(AdvanceSearchActivity.this, adLayout);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(AdvanceSearchActivity.this, 1));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(AdvanceSearchActivity.this, R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(AdvanceSearchActivity.this));
        jsObj.addProperty("method_name", "all_properties");
        jsObj.addProperty("verified", typeVerify);
        jsObj.addProperty("price_min", typeMin);
        jsObj.addProperty("price_max", typeMax);
        jsObj.addProperty("furnishing", typeFur);
        jsObj.addProperty("type_id", typeId);
        jsObj.addProperty("user_id", MyApplication.getInstance().getUserId());
        if (JsonUtils.isNetworkAvailable(AdvanceSearchActivity.this)) {
            new getSearch(API.toBase64(jsObj.toString())).execute(Constant.API_URL);
        }

    }

    @SuppressLint("StaticFieldLeak")
    private class getSearch extends AsyncTask<String, Void, String> {

        String base64;

        private getSearch(String base64) {
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
        adapter = new PropertyAdapter(AdvanceSearchActivity.this, mListItem);
        recyclerView.setAdapter(adapter);

        if (adapter.getItemCount() == 0) {
            lyt_not_found.setVisibility(View.VISIBLE);
        } else {
            lyt_not_found.setVisibility(View.GONE);
        }
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
