package in.orangeapp.realestate;

import static com.android.volley.Request.Method.POST;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.orangeapp.MediaPicker;
import in.orangeapp.item.AdsDataModel;
import in.orangeapp.item.CityModel;
import in.orangeapp.item.StateModel;
import in.orangeapp.util.Constant;

public class CreateAdvertisementActivity extends AppCompatActivity implements
        MultipleSelectStatesFragment.OnStateClickListener,
        MultipleSelectCityFragment.OnCityClickListener,
        ColorPickerDialogFragment.OnColorPickerListener {
    private ActivityResultContracts.RequestMultiplePermissions multiplePermissionsContract;
    private ActivityResultLauncher<String[]> multiplePermissionLauncher;
    Spinner spinner_advertiser, spinner_cta, spinner_file, spinner_editorial, spinner_ratio, spinner_sponsor, spinner_days;
    Button uploadDoc, postUpload;
    String txtSpinnerAdvertiser = "", txtSpinnerCta = "", txtSpinnerFile = "", txtSpinnerEditorial = "", txtSpinnerRatio = "", txtSpinnerSponsor = "";
    String[] PERMISSIONS = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE};
    MediaPicker mediaPicker;
    LinearLayout llSponsored, llEditorial;
    EditText etUserMobile, etCTALink;
    private String imgurl = "";
    ProgressDialog dialog;
    TextView tvColorCode;
    int days = 0;
    List<String> stateIdList = new ArrayList<>();
    TextView tvFilePath, tvSelectedName;
    AdsDataModel model;
    private boolean isClickable = false;
    String[] daysList = new String[101];
    private int mDefaultColor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advertisement);
        mediaPicker = new MediaPicker(this);
        multiplePermissionsContract = new ActivityResultContracts.RequestMultiplePermissions();
        multiplePermissionLauncher = registerForActivityResult(multiplePermissionsContract, new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                if (result.containsValue(false)) {
                    multiplePermissionLauncher.launch(PERMISSIONS);
                } else {
                    chooseImage_Video();
                }
            }
        });
        spinner_advertiser = findViewById(R.id.spinner_advertiser);
        spinner_cta = findViewById(R.id.spinner_cta);
        spinner_file = findViewById(R.id.spinner_type);
        tvFilePath = findViewById(R.id.tvFilePath);
        spinner_sponsor = findViewById(R.id.spinner_sponsor);
        spinner_editorial = findViewById(R.id.spinner_editorial);
        spinner_ratio = findViewById(R.id.spinner_ratio);
        spinner_days = findViewById(R.id.spinner_days);
        tvColorCode = findViewById(R.id.tvColorCode);
        tvSelectedName = findViewById(R.id.tvSelectedName);
        uploadDoc = findViewById(R.id.btnUpload);
        postUpload = findViewById(R.id.btnCreateAds);
        llSponsored = findViewById(R.id.llSponsored);
        llEditorial = findViewById(R.id.llEditorial);
        etUserMobile = findViewById(R.id.etUserMobile);
        etCTALink = findViewById(R.id.etCTALink);
        daysList[0] = "---Select---";
        for (int i = 1; i <= 100; i++) {
            daysList[i] = i + "";
        }
        spinner_days.setAdapter(getAdapter(daysList));
        spinner_advertiser.setAdapter(getAdapter(getResources().getStringArray(R.array.spinner_advertiser)));
        spinner_cta.setAdapter(getAdapter(getResources().getStringArray(R.array.spinner_cta)));
        spinner_file.setAdapter(getAdapter(getResources().getStringArray(R.array.spinner_file)));
        spinner_editorial.setAdapter(getAdapter(getResources().getStringArray(R.array.spinner_location)));
        spinner_ratio.setAdapter(getAdapter(getResources().getStringArray(R.array.spinner_ratio)));
        spinner_sponsor.setAdapter(getAdapter(getResources().getStringArray(R.array.spinner_sponsor)));
        spinnerListener();
        setListener();
        setData();
    }

    private void setData() {
        if (getIntent().getStringExtra("ads_data") != null) {
            postUpload.setText("Update");
            model = new Gson().fromJson(getIntent().getStringExtra("ads_data"), AdsDataModel.class);
            txtSpinnerAdvertiser = model.getAdvType();
            spinner_advertiser.setSelection(Arrays.asList(getResources().getStringArray(R.array.spinner_advertiser)).indexOf(txtSpinnerAdvertiser));
            if (txtSpinnerAdvertiser.equalsIgnoreCase("editorial")) {
                llSponsored.setVisibility(View.GONE);
                llEditorial.setVisibility(View.VISIBLE);
            } else {
                llSponsored.setVisibility(View.VISIBLE);
                llEditorial.setVisibility(View.GONE);
                String[] elements = model.getGeoId().substring(1, model.getGeoId().length() - 1).split(", ");
                stateIdList.addAll(Arrays.asList(elements));
                List<String> txtSelected = model.getGeoName();
                String selectedItem = "";
                switch (txtSelected.size()) {
                    case 0:
                        selectedItem = "";
                        break;
                    case 1:
                        selectedItem = txtSelected.get(0);
                    default:
                        for (int i = 0; i < txtSelected.size() - 1; i++) {
                            selectedItem += txtSelected.get(i);
                            if (i < txtSelected.size() - 2) {
                                selectedItem += ", ";
                            }
                        }
                        selectedItem += " and " + txtSelected.get(txtSelected.size() - 1);
                        break;

                }
                tvSelectedName.setText(selectedItem);
            }

            txtSpinnerEditorial = model.getEditorial();
            spinner_editorial.setSelection(Arrays.asList(getResources().getStringArray(R.array.spinner_location)).indexOf(txtSpinnerEditorial));

            txtSpinnerSponsor = model.getSponsoredGeoType();
            if (txtSpinnerSponsor.equalsIgnoreCase("city") || txtSpinnerSponsor.equalsIgnoreCase("district"))
                txtSpinnerSponsor = "District";
            spinner_sponsor.setSelection(Arrays.asList(getResources().getStringArray(R.array.spinner_sponsor)).indexOf(txtSpinnerSponsor));

            txtSpinnerFile = model.getAdvExtType();
            spinner_file.setSelection(Arrays.asList(getResources().getStringArray(R.array.spinner_file)).indexOf(txtSpinnerFile));

            txtSpinnerRatio = model.getAdvRatio();
            spinner_ratio.setSelection(Arrays.asList(getResources().getStringArray(R.array.spinner_ratio)).indexOf(txtSpinnerRatio));

            txtSpinnerCta = model.getCtaType();
            spinner_cta.setSelection(Arrays.asList(getResources().getStringArray(R.array.spinner_cta)).indexOf(txtSpinnerCta));

            etUserMobile.setText(model.getUserAccount());
            etCTALink.setText(model.getCtaLink());

            days = Integer.parseInt(model.getNoOfDisplay());
            spinner_days.setSelection(Integer.parseInt(daysList[days - 1]));

            imgurl = model.getFilepath();
            tvFilePath.setText(imgurl);
            if (!model.getCtaColor().isEmpty()) {
                mDefaultColor = Integer.parseInt(model.getCtaColor());
                if (model.getCtaColor() != null && !model.getCtaColor().isEmpty()) {
                    tvColorCode.setBackgroundColor(mDefaultColor);
                }
            }
        }
    }

    private void chooseImage_Video() {
        mediaPicker.enableCropper(true);
        if (txtSpinnerFile != null) {
            if (txtSpinnerFile.equalsIgnoreCase("image")) {
                mediaPicker.openGallery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 101);
            } else {
                mediaPicker.openGallery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, 102);
            }
        } else {
            Toast.makeText(this, "Select File Type", Toast.LENGTH_SHORT).show();
        }
    }

    private void spinnerListener() {
        spinner_days.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    days = Integer.parseInt(String.valueOf(spinner_days.getSelectedItem()));
                    Log.e("TAG", "onItemSelected: " + days);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_advertiser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    txtSpinnerAdvertiser = String.valueOf(spinner_advertiser.getSelectedItem());
                    if (txtSpinnerAdvertiser.equals("Sponsored")) {
                        llSponsored.setVisibility(View.VISIBLE);
                        llEditorial.setVisibility(View.GONE);
                    } else {
                        llSponsored.setVisibility(View.GONE);
                        llEditorial.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_cta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    txtSpinnerCta = String.valueOf(spinner_cta.getSelectedItem());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_file.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    txtSpinnerFile = String.valueOf(spinner_file.getSelectedItem());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_ratio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    txtSpinnerRatio = String.valueOf(spinner_ratio.getSelectedItem());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_sponsor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    if (isClickable) {
                        txtSpinnerSponsor = String.valueOf(spinner_sponsor.getSelectedItem());
                        if (txtSpinnerSponsor.equalsIgnoreCase("State")) {
                            MultipleSelectStatesFragment optionsSheetFragment = new MultipleSelectStatesFragment(CreateAdvertisementActivity.this);
                            optionsSheetFragment.show(getSupportFragmentManager(), optionsSheetFragment.getClass().getSimpleName());
                        } else if (txtSpinnerSponsor.equalsIgnoreCase("District")) {
                            MultipleSelectCityFragment optionsSheetFragment = new MultipleSelectCityFragment(CreateAdvertisementActivity.this);
                            optionsSheetFragment.show(getSupportFragmentManager(), optionsSheetFragment.getClass().getSimpleName());
                        }
                        isClickable = false;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_editorial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    txtSpinnerEditorial = String.valueOf(spinner_editorial.getSelectedItem());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setListener() {
        tvColorCode.setOnClickListener(v -> {
            ColorPickerDialogFragment colorPickerDialogFragment = new ColorPickerDialogFragment(CreateAdvertisementActivity.this);
            colorPickerDialogFragment.show(getSupportFragmentManager(), colorPickerDialogFragment.getClass().getSimpleName());
        });

        uploadDoc.setOnClickListener(v -> {
            askPermissions(multiplePermissionLauncher);
        });
        mediaPicker.setImagePickerListener(this::uploadFileOnServer);
        mediaPicker.setVideoPickerListener(this::uploadFileOnServer);
        mediaPicker.setVideoErrorListener(() -> Toast.makeText(CreateAdvertisementActivity.this, "Video Size Greater than 30 MB", Toast.LENGTH_SHORT).show());
        postUpload.setOnClickListener(v -> {
            if (validateOk()) {
                if (postUpload.getText() != "Update") {
                    uploadDataOnServer();
                } else {
                    updateDataOnServer();
                }
            }
        });
        spinner_sponsor.setOnTouchListener((v, event) -> {
            isClickable = true;
            return false;
        });

    }

    private boolean validateOk() {
        if (txtSpinnerAdvertiser.isEmpty()) {
            Toast.makeText(this, "Select Advertiser", Toast.LENGTH_SHORT).show();
            return false;
        } else if (txtSpinnerAdvertiser.equalsIgnoreCase("editorial") && txtSpinnerEditorial.isEmpty()) {
            Toast.makeText(this, "Select Editorial", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etUserMobile.getText().toString().isEmpty()) {
            Toast.makeText(this, "Select User mobile number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (txtSpinnerFile.isEmpty()) {
            Toast.makeText(this, "Select File Type", Toast.LENGTH_SHORT).show();
            return false;
        } else if (txtSpinnerRatio.isEmpty()) {
            Toast.makeText(this, "Select File Ratio Type", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etCTALink.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter CTA link", Toast.LENGTH_SHORT).show();
            return false;
        } else if (imgurl == null || imgurl.isEmpty()) {
            Toast.makeText(this, "Upload Image or video", Toast.LENGTH_SHORT).show();
            return false;
        } else if (txtSpinnerAdvertiser.equalsIgnoreCase("sponsored") && txtSpinnerSponsor.isEmpty()) {
            Toast.makeText(this, "Select sponsor type", Toast.LENGTH_SHORT).show();
            return false;
        } else if (txtSpinnerAdvertiser.equalsIgnoreCase("sponsored") && stateIdList.size() == 0) {
            Toast.makeText(this, "Select State or city Name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (days == 0) {
            Toast.makeText(this, "Enter number of days", Toast.LENGTH_SHORT).show();
            return false;
        } else if (txtSpinnerCta.isEmpty()) {
            Toast.makeText(this, "Select CTA type", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void uploadDataOnServer() {
        String url = Constant.SERVER_URLMAIN + "save_advert";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject parameters = new JSONObject();
        String tempNum = etUserMobile.getText().toString();
        Long mobile = Long.parseLong(tempNum);
        try {
            parameters.put("advert_type", txtSpinnerAdvertiser);
            if (!txtSpinnerAdvertiser.equalsIgnoreCase("Sponsored")) {
                parameters.put("editorial_location", Integer.parseInt(txtSpinnerEditorial));
            }
            if (txtSpinnerAdvertiser.equalsIgnoreCase("Sponsored")) {
                parameters.put("sponsored", 1);
            } else {
                parameters.put("sponsored", 0);
            }
            parameters.put("user_account", mobile);
            parameters.put("adv_ext_type", txtSpinnerFile);
            parameters.put("adv_ratio", txtSpinnerRatio);
            parameters.put("cta_button", "red");
            parameters.put("cta_link", etCTALink.getText().toString().trim());
            parameters.put("filepath", imgurl);
            parameters.put("sponsored_geo_type", txtSpinnerSponsor);
            parameters.put("sponsored_geo_id", stateIdList);
            parameters.put("cta_type", txtSpinnerCta);
            parameters.put("no_of_display", days);
            parameters.put("cta_color", mDefaultColor);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("upload data Pararmenter", "" + parameters);
        JsonObjectRequest stringRequest = new JsonObjectRequest(POST, url, parameters,
                response ->
                {
                    Log.e("uploadphoto Response", "" + response);
                    try {
                        JSONObject obj = new JSONObject(String.valueOf(response));
                        if (obj.getString("status").equals("1")) {
                            Toast.makeText(this, "Add Successfully upload", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ,
                error ->
                        Log.e("uploadphoto Error", "" + error));
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }

    private void updateDataOnServer() {
        String url = Constant.SERVER_URLMAIN + "update_advert";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject parameters = new JSONObject();
        String tempNum = etUserMobile.getText().toString();
        Long mobile = Long.parseLong(tempNum);
        try {
            parameters.put("advert_type", txtSpinnerAdvertiser);
            if (!txtSpinnerAdvertiser.equalsIgnoreCase("Sponsored")) {
                parameters.put("editorial_location", Integer.parseInt(txtSpinnerEditorial));
            }
            if (txtSpinnerAdvertiser.equalsIgnoreCase("Sponsored")) {
                parameters.put("sponsored", 1);
            } else {
                parameters.put("sponsored", 0);
            }
            parameters.put("user_account", mobile);
            parameters.put("id", model.getId());
            parameters.put("adv_ext_type", txtSpinnerFile);
            parameters.put("adv_ratio", txtSpinnerRatio);
            parameters.put("cta_button", "red");
            parameters.put("cta_link", etCTALink.getText().toString().trim());
            parameters.put("filepath", imgurl);
            parameters.put("sponsored_geo_type", txtSpinnerSponsor);
            parameters.put("sponsored_geo_id", stateIdList);
            parameters.put("cta_type", txtSpinnerCta);
            parameters.put("no_of_display", days);
            parameters.put("cta_color", mDefaultColor);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("upload data Pararmenter", "" + parameters);
        JsonObjectRequest stringRequest = new JsonObjectRequest(POST, url, parameters,
                response ->
                {
                    Log.e("uploadphoto Response", "" + response);
                    try {
                        JSONObject obj = new JSONObject(String.valueOf(response));
                        if (obj.getString("status").equals("1")) {
                            Toast.makeText(this, "Update Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ,
                error ->
                        Log.e("uploadphoto Error", "" + error));
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }

    private void uploadFileOnServer(File file) {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait");
        dialog.show();
        String url = Constant.SERVER_URLMAIN + "file_upload";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        Map<String, String> params = new HashMap();
        Log.e("TAG_IMAGE", "uploadFileOnServer: " + fileToBase64(file));
        params.put("file", fileToBase64(file));
        params.put("type", txtSpinnerFile.toLowerCase());
        JSONObject parameters = new JSONObject(params);

        Log.e("uploadphoto Pararmenter", "" + parameters);

        JsonObjectRequest stringRequest = new JsonObjectRequest(POST, url, parameters,
                response ->
                {
                    dialog.dismiss();
                    try {
                        JSONObject obj = new JSONObject(String.valueOf(response));
                        imgurl = obj.getString("imgurl");
                        tvFilePath.setText(imgurl);
                        String message = obj.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("uploadphoto Response", "" + response);
                },
                error ->
                {
                    dialog.dismiss();
                    Log.e("uploadphoto Error", "" + error);
                });
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }

    private void askPermissions(ActivityResultLauncher<String[]> multiplePermissionLauncher) {
        if (!hasPermissions(PERMISSIONS)) {
            multiplePermissionLauncher.launch(PERMISSIONS);
        } else {
            chooseImage_Video();
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


    private ArrayAdapter getAdapter(String[] spinner_items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_spinner_layout) {
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
        /*ArrayAdapter adapter = new ArrayAdapter(this,
                R.layout.item_spinner_layout,
                spinner_items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/
        return adapter;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mediaPicker.onActivityResult(requestCode, resultCode, data);
    }

    public String fileToBase64(File file) {
        try {
            FileInputStream inputStream = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            inputStream.read(bytes);
            inputStream.close();
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onStateSelected(List<StateModel> selectedItems) {
        stateIdList.clear();
        String txtSelected = "";
        for (int i = 0; i < selectedItems.size(); i++) {
            stateIdList.add(selectedItems.get(i).getState_id());
        }
        if (selectedItems.size() == 0) {
            txtSelected = "";
        } else if (selectedItems.size() == 1) {
            txtSelected = selectedItems.get(0).getState_title();
        } else {
            for (int i = 0; i < selectedItems.size() - 1; i++) {
                txtSelected += selectedItems.get(i).getState_title();
                if (i < selectedItems.size() - 2) {
                    txtSelected += ", ";
                }
            }
            txtSelected += " and " + selectedItems.get(selectedItems.size() - 1).getState_title();
        }
        tvSelectedName.setText(txtSelected);
    }

    @Override
    public void onCitySelected(List<CityModel> selectedItems) {
        String txtSelected = "";
        stateIdList.clear();
        for (int i = 0; i < selectedItems.size(); i++) {
            stateIdList.add(selectedItems.get(i).getDistrictid());
        }
        if (selectedItems.size() == 0) {
            txtSelected = "";
        } else if (selectedItems.size() == 1) {
            txtSelected = selectedItems.get(0).getDistrict_title();
        } else {
            for (int i = 0; i < selectedItems.size() - 1; i++) {
                txtSelected += selectedItems.get(i).getDistrict_title();
                if (i < selectedItems.size() - 2) {
                    txtSelected += ", ";
                }
            }
            txtSelected += " and " + selectedItems.get(selectedItems.size() - 1).getDistrict_title();
        }
        tvSelectedName.setText(txtSelected);
    }

    @Override
    public void onColorSelected(int color) {
        mDefaultColor = color;
        tvColorCode.setBackgroundColor(mDefaultColor);
    }
}