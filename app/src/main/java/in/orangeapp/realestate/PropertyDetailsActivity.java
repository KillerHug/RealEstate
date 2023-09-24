package in.orangeapp.realestate;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import in.orangeapp.fragment.AmenitiesFragment;
import in.orangeapp.fragment.GalleryFragment;
import in.orangeapp.item.ItemProperty;
import in.orangeapp.util.API;
import in.orangeapp.util.BannerAds;
import in.orangeapp.util.Constant;
import in.orangeapp.util.FavClickListener;
import in.orangeapp.util.FavUnFav;
import in.orangeapp.util.JsonUtils;
import in.orangeapp.realestate.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class PropertyDetailsActivity extends AppCompatActivity {

    ImageView imageFloor, imageMap, imageCall;
    TextView txtName, txtAddress, txtPrice, txtBed, txtBath, txtArea, txtPhone, txtAmenities;
    WebView webView;
    Toolbar toolbar;
    ScrollView mScrollView;
    ProgressBar mProgressBar;
    ItemProperty objBean;
    String Id;
    ArrayList<String> mGallery, mAmenities;
    private FragmentManager fragmentManager;
    Menu menu;
    View view, view1;
    JsonUtils jsonUtils;
    LinearLayout adLayout;
    boolean iswhichscreen;
    ImageView image_fur, image_very;
    TextView textFur, textVery;
    MyApplication myApplication;
    LinearLayout lay_bed, lay_bath, lay_feet, lay_verify, lay_semi;
    View view_bed, view_bath, view_feet, view_verify;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estate_details);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setTitle(getString(R.string.property_details));
        myApplication = MyApplication.getInstance();
        jsonUtils = new JsonUtils(this);
        jsonUtils.forceRTLIfSupported(getWindow());

        Intent i = getIntent();
        Id = i.getStringExtra("Id");

        fragmentManager = getSupportFragmentManager();

        objBean = new ItemProperty();
        mGallery = new ArrayList<>();
        mAmenities = new ArrayList<>();

        image_fur = findViewById(R.id.image_fur);
        textFur = findViewById(R.id.textFur);
        image_very = findViewById(R.id.image_very);
        textVery = findViewById(R.id.textVery);
        imageFloor = findViewById(R.id.image_floor);
        imageMap = findViewById(R.id.imageMap);
        imageCall = findViewById(R.id.imageCall);
        txtName = findViewById(R.id.text);
        txtAddress = findViewById(R.id.textAddress);
        txtPrice = findViewById(R.id.textPrice);
        txtBed = findViewById(R.id.textBed);
        txtBath = findViewById(R.id.textBath);
        txtArea = findViewById(R.id.textSquare);
        txtPhone = findViewById(R.id.textPhone);
        txtAmenities = findViewById(R.id.txtAmenities);
        view = findViewById(R.id.viewAmenities);
        view1 = findViewById(R.id.viewAmenities1);
        webView = findViewById(R.id.property_description);

        mScrollView = findViewById(R.id.scrollView);
        mProgressBar = findViewById(R.id.progressBar1);
        webView.setBackgroundColor(Color.TRANSPARENT);

        adLayout = findViewById(R.id.adview);
        Intent intent = getIntent();
        iswhichscreen = intent.getBooleanExtra("isNotification", false);
        BannerAds.showBannerAds(PropertyDetailsActivity.this, adLayout);

        lay_bed = findViewById(R.id.lay_bed);
        lay_bath = findViewById(R.id.lay_bath);
        lay_feet = findViewById(R.id.lay_feet);
        lay_verify = findViewById(R.id.lay_verify);
        lay_semi = findViewById(R.id.lay_semi);
        view_bed = findViewById(R.id.view_bed);
        view_bath = findViewById(R.id.view_bath);
        view_feet = findViewById(R.id.view_feet);
        view_verify = findViewById(R.id.view_verify);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(PropertyDetailsActivity.this));
        jsObj.addProperty("method_name", "properties_single");
        jsObj.addProperty("property_id", Id);
        jsObj.addProperty("user_id", MyApplication.getInstance().getUserId());
        if (JsonUtils.isNetworkAvailable(PropertyDetailsActivity.this)) {
            new getProperty(API.toBase64(jsObj.toString())).execute(Constant.API_URL);
        }

        imageMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String geoUri = "http://maps.google.com/maps?q=loc:" + objBean.getPropertyMapLatitude() + "," + objBean.getPropertyMapLongitude() + " (" + objBean.getPropertyName() + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                startActivity(intent);
            }
        });

        imageCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + objBean.getPropertyPhone()));
                startActivity(callIntent);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class getProperty extends AsyncTask<String, Void, String> {

        String base64;

        private getProperty(String base64) {
            this.base64 = base64;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            mScrollView.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0], base64);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mProgressBar.setVisibility(View.GONE);
            mScrollView.setVisibility(View.VISIBLE);
            if (null == result || result.length() == 0) {
                showToast(getString(R.string.nodata));
            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
                    JSONObject objJson;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        objBean.setPId(objJson.getString(Constant.PROPERTY_ID));
                        objBean.setPropertyName(objJson.getString(Constant.PROPERTY_TITLE));
                        objBean.setPropertyThumbnailB(objJson.getString(Constant.PROPERTY_IMAGE));
                        objBean.setRateAvg(objJson.getString(Constant.PROPERTY_RATE));
                        objBean.setPropertyPrice(objJson.getString(Constant.PROPERTY_PRICE));
                        objBean.setPropertyBed(objJson.getString(Constant.PROPERTY_BED));
                        objBean.setPropertyBath(objJson.getString(Constant.PROPERTY_BATH));
                        objBean.setPropertyArea(objJson.getString(Constant.PROPERTY_AREA));
                        objBean.setPropertyAddress(objJson.getString(Constant.PROPERTY_ADDRESS));
                        objBean.setPropertyPhone(objJson.getString(Constant.PROPERTY_PHONE));
                        objBean.setPropertyDescription(objJson.getString(Constant.PROPERTY_DESC));
                        objBean.setPropertyFloorPlan(objJson.getString(Constant.PROPERTY_FLOOR_PLAN));
                        objBean.setPropertyAmenities(objJson.getString(Constant.PROPERTY_AMENITIES));
                        objBean.setPropertyPurpose(objJson.getString(Constant.PROPERTY_PURPOSE));
                        objBean.setPropertyMapLatitude(objJson.getString(Constant.PROPERTY_LATITUDE));
                        objBean.setPropertyMapLongitude(objJson.getString(Constant.PROPERTY_LONGITUDE));
                        objBean.setPropertyVery(objJson.getString(Constant.PROPERTY_VERY));
                        objBean.setPropertyFur(objJson.getString(Constant.PROPERTY_FUR));
                        objBean.setpropertyTotalRate(objJson.getString(Constant.PROPERTY_TOTAL_RATE));
                        objBean.setFavourite(objJson.getBoolean(Constant.FAV_TAG));

                        JSONArray jsonArrayGallery = objJson.getJSONArray(Constant.GALLERY_ARRAY_NAME);
                        if (jsonArrayGallery.length() != 0) {
                            for (int j = 0; j < jsonArrayGallery.length(); j++) {
                                JSONObject objChild = jsonArrayGallery.getJSONObject(j);
                                if (!objChild.has(Constant.SUCCESS)) {
                                    mGallery.add(objChild.getString(Constant.GALLERY_IMAGE_NAME));
                                } else {
                                    mGallery.add(objJson.getString(Constant.PROPERTY_IMAGE));
                                }

                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setResult();
            }
        }
    }

    private void setResult() {

        txtName.setText(objBean.getPropertyName());
        txtAddress.setText(objBean.getPropertyAddress());
        txtBath.setText(objBean.getPropertyBath() + getString(R.string.bed_bath2));
        txtBed.setText(objBean.getPropertyBed() + getString(R.string.bed_bath));
        txtArea.setText(objBean.getPropertyArea());
        txtPhone.setText(objBean.getPropertyPhone());
        txtPrice.setText(getString(R.string.currency_symbol) + objBean.getPropertyPrice());

        if (objBean.getPropertyBed().isEmpty()) {
            lay_bed.setVisibility(View.GONE);
            view_bed.setVisibility(View.GONE);
        } else {
            lay_bed.setVisibility(View.VISIBLE);
            view_bed.setVisibility(View.VISIBLE);
        }

        if (objBean.getPropertyBath().isEmpty()) {
            lay_bath.setVisibility(View.GONE);
            view_bath.setVisibility(View.GONE);
        } else {
            lay_bath.setVisibility(View.VISIBLE);
            view_bath.setVisibility(View.VISIBLE);
        }

        if (objBean.getPropertyArea().isEmpty()) {
            lay_feet.setVisibility(View.GONE);
            view_feet.setVisibility(View.GONE);
        } else {
            lay_feet.setVisibility(View.VISIBLE);
            view_feet.setVisibility(View.VISIBLE);
        }

        if (objBean.getPropertyVery().isEmpty()) {
            lay_verify.setVisibility(View.GONE);
            view_verify.setVisibility(View.GONE);
        } else {
            lay_verify.setVisibility(View.VISIBLE);
            view_verify.setVisibility(View.VISIBLE);
        }

        if (objBean.getPropertyFur().isEmpty()) {
            lay_semi.setVisibility(View.GONE);
        } else {
            lay_semi.setVisibility(View.VISIBLE);
        }


        if (!objBean.getPropertyAmenities().isEmpty())
            mAmenities = new ArrayList<>(Arrays.asList(objBean.getPropertyAmenities().split(",")));

        Picasso.get().load(objBean.getPropertyFloorPlan()).placeholder(R.drawable.header_top_logo).into(imageFloor);

        imageFloor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PropertyDetailsActivity.this, FloorImageActivity.class);
                intent.putExtra("ImageF", objBean.getPropertyFloorPlan());
                startActivity(intent);

            }
        });

        String mimeType = "text/html";
        String encoding = "utf-8";
        String htmlText = objBean.getPropertyDescription();

        String text = "<html><head>"
                + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/custom.ttf\")}body{font-family: MyFont;color: #868686;text-align:left;font-size:14px;margin-left:0px;line-height:1.8}"
                + "</style></head>"
                + "<body>"
                + htmlText
                + "</body></html>";

        webView.loadDataWithBaseURL(null, text, mimeType, encoding, null);

        if (objBean.getPropertyFur().equals(getString(R.string.detail_un_semi))) {
            image_fur.setImageResource(R.drawable.ic_unfurnished);
            image_fur.setBackgroundResource(R.drawable.circle_gray_detail);
            textFur.setText(getString(R.string.detail_un_semi));

        } else if (objBean.getPropertyFur().equals(getString(R.string.detail_fur))) {
            image_fur.setImageResource(R.drawable.ic_furnished);
            image_fur.setBackgroundResource(R.drawable.circle_green_detail);
            textFur.setText(getString(R.string.detail_fur));
        } else if (objBean.getPropertyFur().equals(getString(R.string.detail_semi))) {
            image_fur.setImageResource(R.drawable.ic_semi_furnished);
            image_fur.setBackgroundResource(R.drawable.circle_orange_detail);
            textFur.setText(getString(R.string.detail_semi));
        }

        if (objBean.getPropertyVery().equals("1"))//verify
        {
            image_very.setBackgroundResource(R.drawable.circle_green_detail);
            image_very.setImageResource(R.drawable.ic_verified_properties);
            textVery.setText(getString(R.string.detail_very));
        } else {
            image_very.setBackgroundResource(R.drawable.circle_gray_detail);
            image_very.setImageResource(R.drawable.ic_non_verified_properties);
            textVery.setText(getString(R.string.detail_un_very));
        }

        if (!mGallery.isEmpty()) {
            GalleryFragment sliderFragment = GalleryFragment.newInstance(mGallery);
            fragmentManager.beginTransaction().replace(R.id.ContainerGallery, sliderFragment).commit();
        }

        if (!objBean.getPropertyAmenities().isEmpty()) {
            AmenitiesFragment amenitiesFragment = AmenitiesFragment.newInstance(mAmenities);
            fragmentManager.beginTransaction().replace(R.id.ContainerAmenities, amenitiesFragment).commit();
        } else {
            txtAmenities.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
        }

        if (menu != null) {
            if (objBean.isFavourite()) {
                menu.findItem(R.id.menu_bookmark).setIcon(R.drawable.ic_bookmark_white_24dp);
            } else {
                menu.findItem(R.id.menu_bookmark).setIcon(R.drawable.ic_bookmark_border_white_24dp);
            }
        }
    }

    public void showToast(String msg) {
        Toast.makeText(PropertyDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_property, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_bookmark:
                if (MyApplication.getInstance().getIsLogin()) {
                    if (JsonUtils.isNetworkAvailable(PropertyDetailsActivity.this)) {
                        FavClickListener saveClickListener = new FavClickListener() {
                            @Override
                            public void onItemClick(boolean isSave, String message) {
                                if (isSave) {
                                    menu.getItem(0).setIcon(R.drawable.ic_bookmark_white_24dp);
                                } else {
                                    menu.getItem(0).setIcon(R.drawable.ic_bookmark_border_white_24dp);
                                }
                            }
                        };
                        new FavUnFav(PropertyDetailsActivity.this).userFav(objBean.getPId(), saveClickListener);
                    } else {
                        Toast.makeText(PropertyDetailsActivity.this, getString(R.string.network_msg), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PropertyDetailsActivity.this, getString(R.string.login_msg), Toast.LENGTH_SHORT).show();
                    Intent intentLogin = new Intent(PropertyDetailsActivity.this, SignInActivity.class);
                    intentLogin.putExtra("isfromdetail", true);
                    startActivity(intentLogin);
                }
                break;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }
}
