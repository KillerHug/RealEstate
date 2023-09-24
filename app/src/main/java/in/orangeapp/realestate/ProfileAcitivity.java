package in.orangeapp.realestate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import in.orangeapp.util.Constant;
import in.orangeapp.util.YourPreference;
import in.orangeapp.realestate.R;

import com.github.dhaval2404.imagepicker.ImagePicker;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class ProfileAcitivity extends AppCompatActivity {

    TextView state, disctrict, id, save, uploadvisitingtxt, name, username, seller_type;
    ProgressDialog dialog;
    ImageView imgmain, visiting_img, image_advert;
    String visting = "", profile = "", advert = "";
    TextView createsubadmin, mysubsubmit;
    Toolbar toolbar;
    LinearLayout team_layout;
    Button create_advertising,active_advertising;
    String company_firmm;
    int type = 0;
    TextView submit;
    ImageView remove_photo, remove_visiting;
    LinearLayout llAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_acitivity);
        state = findViewById(R.id.state);
        createsubadmin = findViewById(R.id.createsubadmin);
        mysubsubmit = findViewById(R.id.mysubsubmit);
        remove_photo = findViewById(R.id.remove_photo);
        remove_visiting = findViewById(R.id.remove_visiting);
        team_layout = findViewById(R.id.team_layout);
        create_advertising = findViewById(R.id.btnCreateAds);
        active_advertising = findViewById(R.id.btnActiveAds);
        visiting_img = findViewById(R.id.visiting_img);
        disctrict = findViewById(R.id.disctrict);
        id = findViewById(R.id.id);
        save = findViewById(R.id.save);
        name = findViewById(R.id.name);
        imgmain = findViewById(R.id.imgmain);
        username = findViewById(R.id.username);
        seller_type = findViewById(R.id.seller_type);
        uploadvisitingtxt = findViewById(R.id.uploadvisitingtxt);
        toolbar = findViewById(R.id.toolbar);
        llAds = findViewById(R.id.llAds);
        toolbar.setTitle("My Profile");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getprofile();

        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String user_id = yourPrefrence.getData("user_id");
        if (user_id.equalsIgnoreCase("39")) {
            llAds.setVisibility(View.VISIBLE);
        }else llAds.setVisibility(View.GONE);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadphoto("save");

            }
        });

        createsubadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                String user_id = yourPrefrence.getData("user_id");
                Intent intent = new Intent(ProfileAcitivity.this, SignUpActivity.class);
                intent.putExtra("from", "0");
                intent.putExtra("parent", user_id);
                intent.putExtra("firm", company_firmm);
                intent.putExtra("type", seller_type.getText().toString());
                startActivity(intent);
            }
        });
        remove_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile = "blank";
                uploadphoto("remove");

            }
        });

        remove_visiting.setOnClickListener(view -> {
            visting = "blank";
            uploadphoto("remove");
        });

        mysubsubmit.setOnClickListener(view -> {

            Intent intent = new Intent(ProfileAcitivity.this, MySubAdmin.class);
            startActivity(intent);
        });

        create_advertising.setOnClickListener(view -> {
            startActivity(new Intent(ProfileAcitivity.this, CreateAdvertisementActivity.class));
        });
        active_advertising.setOnClickListener(view -> {
            startActivity(new Intent(ProfileAcitivity.this, AdsActivity.class));
        });
    }

    private void opendialog() {


        final Dialog dialog = new Dialog(ProfileAcitivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.advertising);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        EditText title = dialog.findViewById(R.id.title);
        TextView uploadimage = dialog.findViewById(R.id.uploadimage);
        submit = dialog.findViewById(R.id.submit);
        image_advert = dialog.findViewById(R.id.image_advert);

        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = 2;

                ImagePicker.with(ProfileAcitivity.this)
                        .crop()
                        //Crop image(Optional), Check Customization for more option
                        .galleryOnly()
                        .start();

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                add_advert(title.getText().toString());


            }
        });

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void getprofile() {


        dialog = new ProgressDialog(ProfileAcitivity.this);
        dialog.setMessage("Fetching your details.");
        dialog.show();

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
                    Toast.makeText(getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_SHORT).show();

                    String r_code = obj.getString("status");

                    if (r_code.equalsIgnoreCase("1")) {
                        String user_id = obj.optString("user_id");
                        String namee = obj.optString("name");
                        String statee = obj.optString("state");
                        String districtt = obj.optString("district");
                        company_firmm = obj.optString("company_firm");
                        String typee = obj.optString("type");
                        String profile_photo = obj.optString("profile_photo");
                        String visiting_card = obj.optString("visiting_card");
                        String parent_id = obj.optString("parent_id");
                        name.setText(namee);
                        state.setText(statee);
                        disctrict.setText(districtt);
                        username.setText(company_firmm);
                        seller_type.setText(typee);
                        id.setText("USER ID : " + user_id);
                        if (!parent_id.equalsIgnoreCase("0")) {
                            team_layout.setVisibility(View.GONE);
                        }


                        if (profile_photo.equalsIgnoreCase("")) {
                            remove_photo.setVisibility(View.GONE);
                        } else {
                            remove_photo.setVisibility(View.VISIBLE);

                        }
                        Glide.with(ProfileAcitivity.this)
                                .load(Constant.IMAGE_PATH + profile_photo)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.ic_home_avatar_photoicon)
                                .into(imgmain);

                        if (visiting_card.equalsIgnoreCase("")) {
                            remove_visiting.setVisibility(View.GONE);
                        } else {
                            remove_visiting.setVisibility(View.VISIBLE);

                            Glide.with(ProfileAcitivity.this)
                                    .load(Constant.IMAGE_PATH + visiting_card)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.ic_home_avatar_photoicon)
                                    .into(visiting_img);
                            visiting_img.setVisibility(View.VISIBLE);
                            uploadvisitingtxt.setVisibility(View.GONE);
                        }


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

    private void uploadphoto(String type) {


        dialog = new ProgressDialog(ProfileAcitivity.this);
        dialog.setMessage("Fetching your details.");
        dialog.show();

        String url = Constant.SERVER_URLMAIN + "user_profile_update";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String user_id = yourPrefrence.getData("user_id");


        Map<String, String> params = new HashMap();
        params.put("user_id", user_id);
        params.put("profile_photo", profile);
        params.put("visiting_card", visting);
        JSONObject parameters = new JSONObject(params);

        Log.i("uploadphoto Pararmenter", "" + parameters);

        JsonObjectRequest stringRequest = new JsonObjectRequest(POST, url, parameters, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                Log.i("uploadphoto Response", "" + response);

                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));

                    String r_code = obj.getString("status");

                    if (r_code.equalsIgnoreCase("1")) {
                        if (type.equalsIgnoreCase("save")) {
                            Toast.makeText(getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_SHORT).show();

                            Intent profile = new Intent(ProfileAcitivity.this, MainActivity.class);
                            startActivity(profile);
                            finish();

                        } else {
                            Toast.makeText(getApplicationContext(), "Profile Removed", Toast.LENGTH_SHORT).show();

                            Intent profile = new Intent(ProfileAcitivity.this, MainActivity.class);
                            startActivity(profile);
                            finish();
                        }


                    }

                } catch (Exception e) {
                    Log.i("uploadphoto Exception", "" + e);

                    e.printStackTrace();

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

                Log.i("uploadphoto Error", "" + error);

            }
        });
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);


    }

    private void add_advert(String s) {


        dialog = new ProgressDialog(ProfileAcitivity.this);
        dialog.setMessage("Loading...");
        dialog.show();

        String url = Constant.SERVER_URLMAIN + "add_advert";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String user_id = yourPrefrence.getData("user_id");


        Map<String, String> params = new HashMap();
        params.put("title", s);
        params.put("image", advert);
        JSONObject parameters = new JSONObject(params);

        Log.i("add_advert Pararmenter", "" + parameters);

        JsonObjectRequest stringRequest = new JsonObjectRequest(POST, url, parameters, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                Log.i("add_advert Response", "" + response);

                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    Toast.makeText(getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_SHORT).show();

                    String r_code = obj.getString("status");

                    if (r_code.equalsIgnoreCase("1")) {
                        Intent profile = new Intent(ProfileAcitivity.this, MainActivity.class);
                        startActivity(profile);
                        finish();


                    }

                } catch (Exception e) {
                    Log.i("add_advert Exception", "" + e);

                    e.printStackTrace();

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

                Log.i("uploadphoto Error", "" + error);

            }
        });
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    public void updateprofile(View view) {
//        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, 0);

        type = 0;
        ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .galleryOnly()
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start();


    }

    public void visitingcard(View view) {
        type = 1;

        ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .galleryOnly()
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        //adhar upload
        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                if (type == 0) {
                    save.setVisibility(View.VISIBLE);

                    imgmain.setImageBitmap(bitmap);
                    profile = ConvertBitmapToString(getResizedBitmap(bitmap, 700));

                } else if (type == 2) {
                    submit.setVisibility(View.VISIBLE);
                    image_advert.setImageBitmap(bitmap);
                    advert = ConvertBitmapToString(getResizedBitmap(bitmap, 700));

                } else {
                    save.setVisibility(View.VISIBLE);

                    visiting_img.setImageBitmap(bitmap);
                    uploadvisitingtxt.setVisibility(View.GONE);
                    visiting_img.setVisibility(View.VISIBLE);
                    visting = ConvertBitmapToString(bitmap);

                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
//        if (data != null && requestCode == 0) {
//            if (resultCode == RESULT_OK) {
//                Uri targetUri = data.getData();
//                Bitmap bitmap;
//                try {
//                    save.setVisibility(View.VISIBLE);
//                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
//                    float aspectRatio = bitmap.getWidth() / (float) bitmap.getHeight();
//                    int width = 580;
//                    int height = Math.round(width / aspectRatio);
//
//                    //Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
//
//                    ByteArrayOutputStream out = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, out);
//
//                    imgmain.setImageBitmap(bitmap);
//
//                    profile = ConvertBitmapToString(getResizedBitmap(bitmap, 700));
////                    Updateprofile();
//
//                } catch (FileNotFoundException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }
//        if (data != null && requestCode == 1) {
//            if (resultCode == RESULT_OK) {
//                Uri targetUri = data.getData();
//                Bitmap bitmap;
//                try {
//                    save.setVisibility(View.VISIBLE);
//
//                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
//                    float aspectRatio = bitmap.getWidth() / (float) bitmap.getHeight();
//                    int width = 580;
//                    int height = Math.round(width / aspectRatio);
//
//                    // Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
//                    visiting_img.setImageBitmap(bitmap);
//                    uploadvisitingtxt.setVisibility(View.GONE);
//                    visiting_img.setVisibility(View.VISIBLE);
//                    ByteArrayOutputStream out = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, out);
//
//                    visting = ConvertBitmapToString(getResizedBitmap(bitmap, 700));
//
//                } catch (FileNotFoundException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }

    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private String ConvertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

        byte[] imageBytes = baos.toByteArray();

        String base64String = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

        return base64String;
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

}