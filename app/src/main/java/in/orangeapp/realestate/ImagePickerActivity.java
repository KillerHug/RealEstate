package in.orangeapp.realestate;

import static com.android.volley.Request.Method.POST;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import in.orangeapp.util.Constant;
import in.orangeapp.realestate.R;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class ImagePickerActivity extends AppCompatActivity {
    CropImageView cropImageView;
    Bitmap cropped;
    private ProgressDialog pDialog;
    ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);
        cropImageView = findViewById(R.id.cropImageView);

        String uri = Objects.requireNonNull(getIntent().getExtras()).getString("uri");
        cropImageView.setImageUriAsync(Uri.parse(uri));
        progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Uploading please wait..");
        progressdialog.setCancelable(false);

    }

    public void backButton(View view) {
        onBackPressed();
    }

    public void continueCrop(View view) {

        progressdialog.show();

        cropped = cropImageView.getCroppedImage();

//        SendImage(getStringImage(cropped));
        new ChangeFileResolution().execute(cropped);
//        SendImage(getFileSize(cropped));
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
    }

    private void SendImage(String s) {
        String url = Constant.SERVER_URLMAIN + "imgupload";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        Map<String, String> params = new HashMap<>();
        params.put("file", s);
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest stringRequest = new JsonObjectRequest(POST, url, parameters,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject obj = new JSONObject(String.valueOf(response));
                            Toast.makeText(ImagePickerActivity.this.getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_SHORT).show();
                            String r_code = obj.getString("imgurl");

                            Log.e("TAG", "SendImage: " + r_code);
//                            tempImage.setVisibility(View.VISIBLE);
//                            Glide.with(ImagePickerActivity.this).load(r_code).into(tempImage);

                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("url", r_code);
                            ImagePickerActivity.this.setResult(Activity.RESULT_OK, resultIntent);
                            ImagePickerActivity.this.finish();

                        } catch (Exception e) {
                            Log.e("add_advert Exception", "" + e.getMessage());
                            e.printStackTrace();
                        } finally {
                            progressdialog.dismiss();
                        }
                    }
                }, error -> {
            Toast.makeText(this, "image upload error.", Toast.LENGTH_SHORT).show();
            progressdialog.dismiss();
        });
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

//    compress image code

    public String getFilename() {
        File file = new File(getExternalCacheDir().getPath(), "RealState/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".png");
        return uriSting;

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    //    image upload new API
    class ChangeFileResolution extends AsyncTask<Bitmap, String, String> {


        @Override
        protected String doInBackground(Bitmap... bitmaps) {
            Bitmap photo = bitmaps[0];
            Bitmap scaledBitmap = null;
            String fName = getFilename();
            File thumbFile = new File(fName);
            if (!thumbFile.exists()) {
                try {
                    thumbFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            String image_path = "";

            try {
                FileOutputStream stream = new FileOutputStream(thumbFile);
                photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                stream.flush();
                stream.close();
                image_path = thumbFile.getAbsolutePath();
            } catch (Exception e) {
                Log.e("TAG", "continueCrop:  " + e);
                e.printStackTrace();
            }
            Log.e("TAG", "continueCrop:  image: " + image_path);
            Log.e("TAG", "continueCrop:  size: " + thumbFile.length() / 1000 + " KB");
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(thumbFile.getAbsolutePath(), options);
            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;
            Log.e("TAG", "continueCrop:  dimensions: before" + actualHeight + "," + actualWidth);
//      max Height and width values of the compressed image is taken as 816x612

            /*float maxHeight = 816.0f;
            float maxWidth = 612.0f;*/
            float maxHeight = 612.0f;
            float maxWidth = 459.0f;
//          float maxHeight = 408.0f;
//          float maxWidth = 306.0f;
            float imgRatio = actualWidth / actualHeight;
            float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;

                }
            }

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
            options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            try {
//          load the bitmap from its path
                bmp = BitmapFactory.decodeFile(image_path, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();

            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
            ExifInterface exif;
            try {
                exif = new ExifInterface(image_path);

                int orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, 0);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                    Log.d("EXIF", "Exif: " + orientation);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                        scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                        true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            FileOutputStream out = null;
            String filename = getFilename();
            try {
                out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
                scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Log.e("TAG", "continueCrop: after image name" + filename);
            BitmapFactory.Options options1 = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filename, options1);
            int actualHeight1 = options.outHeight;
            int actualWidth1 = options.outWidth;
            Log.e("TAG", "continueCrop: after size: " + new File(filename).length() / 1000 + " KB");
            Log.e("TAG", "continueCrop:  dimensions: after" + actualHeight1 + "," + actualWidth1);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            SendImage(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            pDialog.setProgress(Integer.parseInt(values[0]));
        }
    }
}
