package in.orangeapp.realestate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import in.orangeapp.util.API;
import in.orangeapp.util.JsonUtils;
import in.orangeapp.util.YourPreference;
import in.orangeapp.All_Calculator;
import in.orangeapp.fragment.FavouriteFragment;
import in.orangeapp.fragment.HomeFragment;
import in.orangeapp.fragment.MyPropertyFragment;
import in.orangeapp.item.ItemAbout;
import in.orangeapp.item.ItemType;
import in.orangeapp.util.Constant;
import in.orangeapp.realestate.R;
import com.google.ads.consent.ConsentForm;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

import static com.android.volley.Request.Method.POST;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private FragmentManager fragmentManager;
    MyApplication MyApp;
    NavigationView navigationView;
    Toolbar toolbar;
    ArrayList<ItemType> mListType;
    ArrayList<String> mPropertyName;
    JsonUtils jsonUtils;
    ArrayList<ItemAbout> mListItem;
    LinearLayout adLayout;
    private ConsentForm form;
    private Location mylocation;
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;
    int versionCode;
    String name, state, district, type, company_firm;
    ImageView header_image;
    RelativeLayout headerlayout;
    ProgressDialog dialog;

    TextView header_name, header_email;
    String[] PERMISSIONS = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE};
    private ActivityResultContracts.RequestMultiplePermissions multiplePermissionsContract;
    private ActivityResultLauncher<String[]> multiplePermissionLauncher;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        multiplePermissionsContract = new ActivityResultContracts.RequestMultiplePermissions();
        multiplePermissionLauncher = registerForActivityResult(multiplePermissionsContract, new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                if (result.containsValue(false)) {
                    multiplePermissionLauncher.launch(PERMISSIONS);
                }
            }
        });
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        toolbar = findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();
        navigationView = findViewById(R.id.navigation_view);
        View hView = navigationView.getHeaderView(0);

        drawerLayout = findViewById(R.id.drawer_layout);
        header_image = hView.findViewById(R.id.header_image);
        headerlayout = hView.findViewById(R.id.headerlayout);

        header_name = hView.findViewById(R.id.header_name);
        header_email = hView.findViewById(R.id.header_email);
        adLayout = findViewById(R.id.adview);
        header_name.setText("hello");
        //  setUpGClient();
        mListType = new ArrayList<>();
        mPropertyName = new ArrayList<>();
        mListItem = new ArrayList<>();
        versionCode = BuildConfig.VERSION_CODE;

        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        name = yourPrefrence.getData("name");
        type = yourPrefrence.getData("type");
        company_firm = yourPrefrence.getData("company_firm");
        state = yourPrefrence.getData("state");
        district = yourPrefrence.getData("district");

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), //Insert your own package name.
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        userprofile();

        HomeFragment homeFragment = new HomeFragment();
        fragmentManager.beginTransaction().replace(R.id.Container, homeFragment).commit();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);


        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.myregister:
                                item.setChecked(true);
                                MyPropertyFragment latestFragment = new MyPropertyFragment();
                                fragmentManager.beginTransaction().replace(R.id.Container, latestFragment).commit();
                                break;
                            case R.id.home:
                                item.setChecked(true);

                                HomeFragment homeFragment = new HomeFragment();
                                fragmentManager.beginTransaction().replace(R.id.Container, homeFragment).commit();

                                break;
                            case R.id.fav:
                                item.setChecked(true);

                                FavouriteFragment favouriteFragment = new FavouriteFragment();
                                fragmentManager.beginTransaction().replace(R.id.Container, favouriteFragment).commit();
                                break;
                        }
                        return false;
                    }
                });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {

//                    case R.id.menu_go_home:
//                        toolbar.setTitle(getString(R.string.menu_home));
//                        HomeFragment homeFragment = new HomeFragment();
//                        fragmentManager.beginTransaction().replace(R.id.Container, homeFragment).commit();
//                        spaceNavigationView.changeCurrentItem(0);
//                        return true;
                    case R.id.menu_go_latest:
                        Intent intent2 = new Intent(getApplicationContext(), Generaldocuments.class);
                        startActivity(intent2);

                        return true;
                    case R.id.menu_go_property:
                        Intent intent5 = new Intent(getApplicationContext(), All_Calculator.class);
                        startActivity(intent5);
                        return true;

                    case R.id.roi_calculator:
                        Intent roi = new Intent(getApplicationContext(), ROIcalculator.class);
                        startActivity(roi);
                        return true;

                    case R.id.affor_calculator:
                        Intent affor = new Intent(getApplicationContext(), Morgagecalculator.class);
                        startActivity(affor);
                        return true;

//                    case R.id.menu_go_favourite:
//                        toolbar.setTitle(getString(R.string.menu_favourite));
//                        FavouriteFragment favouriteFragment = new FavouriteFragment();
//                        fragmentManager.beginTransaction().replace(R.id.Container, favouriteFragment).commit();
//                        spaceNavigationView.changeCurrentItem(2);
//                        return true;
                    case R.id.menu_go_add_properties:

                        Intent intent = new Intent(getApplicationContext(), AboutUsActivity.class);
                        startActivity(intent);

                        return true;
//                    case R.id.menu_go_add_properties:
//                        if (MyApp.getIsLogin()) {
//                            Intent intent = new Intent(MainActivity.this, Addproperty.class);
//                            startActivity(intent);
//                        } else {
//                            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                        return true;

                    case R.id.menu_go_profile:
                        Intent profile = new Intent(MainActivity.this, ProfileAcitivity.class);
                        startActivity(profile);
                        return true;
                    case R.id.menu_go_contact:
                        Intent contact = new Intent(MainActivity.this, Contactus.class);
                        startActivity(contact);
                        return true;
                    case R.id.menu_go_setting:
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_TEXT,
                                "Hi, I have started using Orange App Mobile platform and found it very usefull, you can also download the same from https://play.google.com/store/apps/details?id="
                                        + getApplicationContext().getPackageName());
                        startActivity(Intent.createChooser(i, "Invite Friends"));
                        return true;
//                    case R.id.menu_go_login:
//                        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                        finish();
//                        return true;
                    case R.id.menu_go_logout:
                        Logout();

                        drawerLayout.closeDrawers();
                        return true;

                    default:
                        return true;
                }
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(MainActivity.this));
        jsObj.addProperty("method_name", "get_app_details");
        if (JsonUtils.isNetworkAvailable(MainActivity.this)) {

        } else {
            showToast(getString(R.string.nodata));
        }


    }

    public void showToast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        ExitApp();
    }

    private void ExitApp() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.exit_msg))
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("No", (dialog, which) -> {
                })
                .setNegativeButton("Yes", (dialog, which) -> {
                    finish();
                    // do nothing
                })
                .show();
    }

    private void Logout() {

        final PrettyDialog dialog = new PrettyDialog(MainActivity.this);
        dialog.setTitle(getString(R.string.dialog_logout))
                .setTitleColor(R.color.dialog_text)
                .setMessage(getString(R.string.logout_msg))
                .setMessageColor(R.color.dialog_text)
                .setAnimationEnabled(false)
                .setIcon(R.drawable.pdlg_icon_info, R.color.dialog_color, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        dialog.dismiss();
                    }
                })
                .addButton("YES", R.color.dialog_white_text, R.color.dialog_color, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        dialog.dismiss();

                        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                        yourPrefrence.saveData("signup", "");

                        Intent intent_login = new Intent(MainActivity.this, SignInActivity.class);
                        intent_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent_login);
                        finish();
                    }
                })
                .addButton(getString(R.string.dialog_no), R.color.dialog_white_text, R.color.dialog_color, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        dialog.dismiss();
                    }
                });
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        askPermissions(multiplePermissionLauncher);
        userprofile();
        if (type.equalsIgnoreCase("Real Estate Agent") || type.equalsIgnoreCase("Builder")) {
            toolbar.setTitle(company_firm.toUpperCase() + "\n" + state + " (" + district + ")");

        } else {
            toolbar.setTitle(name.toUpperCase() + "\n" + state + " (" + district + ")");
        }

    }

    private void userprofile() {

        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Fetching your detailsssss.");
        //dialog.show();


        String url = Constant.SERVER_URLMAIN + "myprofile";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String user_id = yourPrefrence.getData("user_id");


        Map<String, String> params = new HashMap();
        params.put("user_id", user_id);
        JSONObject parameters = new JSONObject(params);

        Log.i("user_signup Pararmenter", "" + parameters);

        JsonObjectRequest stringRequest = new JsonObjectRequest(POST, url, parameters, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();

                Log.i("user_signup Response", "" + response);
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    //   Toast.makeText(getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_SHORT).show();

                    String r_code = obj.getString("status");

                    if (r_code.equalsIgnoreCase("1")) {
                        String namee = obj.optString("name");
                        String profile_photo = obj.optString("profile_photo");
                        String visiting_card = obj.optString("visiting_card");
                        String statee = obj.optString("state");
                        String districtt = obj.optString("district");
                        Glide.with(MainActivity.this)
                                .load(Constant.IMAGE_PATH + profile_photo)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.ic_home_avatar_photoicon)
                                .into(header_image);
                        header_name.setText(namee);
                        header_email.setText(districtt + " , " + statee);


                        Glide.with(MainActivity.this)
                                .load(Constant.IMAGE_PATH + visiting_card)
                                .into(new CustomTarget<Drawable>() {
                                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                    @Override
                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                        headerlayout.setBackground(resource);
                                    }

                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {

                                    }
                                });
//                        Glide.with(MainActivity.this)
//                                .load(IMAGE_PATH + visiting_card)
//                                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                .placeholder(R.drawable.ic_home_avatar_photoicon)
//                                .into(visiting_img);


                    }

                } catch (Exception e) {
                    Log.i("user_signup Exception", "" + e);

                    e.printStackTrace();

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

                Log.i("user_signup Error", "" + error);

            }
        });
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }

    private void askPermissions(ActivityResultLauncher<String[]> multiplePermissionLauncher) {
        if (!hasPermissions(PERMISSIONS)) {
            multiplePermissionLauncher.launch(PERMISSIONS);
        }
    }

    private Boolean hasPermissions(String[] permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d("PERMISSIONS", "Permission is not granted: $permission");
                    return false;
                }
                Log.d("PERMISSIONS", "Permission already granted: $permission");
            }
            return true;
        }
        return false;
    }

}
