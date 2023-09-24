package in.orangeapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("ALL")
public class MediaPicker {
    private static final String TYPE_IMAGE = "image/*";
    private static final String TYPE_VIDEO = "video/*";
    private static final String[] MIME_TYPES_IMAGE = {"image/jpeg", "image/jpg", "image/png"};
    private static final String[] MIME_TYPES_ = {"image/jpeg", "image/jpg", "image/png"};

    private CropImageView.CropShape shape = CropImageView.CropShape.OVAL;
    private Point point = new Point(1, 1);
    private File mediaFile;
    /**
     * Activity object that will be used while calling startActivityForResult(). Activity then will
     * receive the callbacks to its own onActivityResult() and is responsible of calling the
     * onActivityResult() of the MediaPicker for handling result and being notified.
     */
    private Activity context;
    /**
     * Fragment object that will be used while calling startActivityForResult(). Fragment then will
     * receive the callbacks to its own onActivityResult() and is responsible of calling the
     * onActivityResult() of the MediaPicker for handling result and being notified.
     */
    private Fragment fragment;
    private ImagePickerListener imagePickerListener;
    private VideoPickerListener videoPickerListener;
    private VideoErrorListener videoErrorListener;
    private int videoDuration = 10;
    private boolean
            enableCropper = false, allowAttachment = false, onlyImageGallery = false;

    public MediaPicker(@NonNull Activity activity) {
        this.context = activity;
//        cacheDirectory = FileUtils.getAppCacheDirectoryPath(activity);
    }

    public void setImagePickerListener(@NonNull ImagePickerListener imagePickerListener) {
        this.imagePickerListener = imagePickerListener;
    }

    public void setVideoErrorListener(@NonNull VideoErrorListener videoErrorListener) {
        this.videoErrorListener = videoErrorListener;
    }

    public void setVideoPickerListener(VideoPickerListener videoPickerListener) {
        this.videoPickerListener = videoPickerListener;
    }

    public void enableCropper(boolean enableCropper) {
        this.enableCropper = enableCropper;
    }

    /**
     * Removes all listeners and references
     */
    public void clear() {
        this.imagePickerListener = null;
        this.context = null;
        this.fragment = null;
        this.mediaFile = null;
    }

    public void show() {
        if (FileUtils.isLowStorage(context)) {
            Toast.makeText(context, "Media picker message please free up space", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void setCropShape(CropImageView.CropShape shape) {
        this.shape = shape;
        if (shape == CropImageView.CropShape.OVAL) {
            point = new Point(1, 1);
        } else {
            point = new Point(16, 9);
        }
    }

    /**
     * Handles the result of events that the Activity or Fragment receives on its own
     * onActivityResult(). This method must be called inside the onActivityResult()
     * of the container Activity or Fragment.
     */
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case 101:
                if (data != null && data.getData() != null) {
                    String imagePath = getPathFromGallery(context, data.getData());
                    if (imagePath != null) {
                        mediaFile = new File(imagePath);
                        /*if (enableCropper) {
                            if (fragment == null) {
                                CropImage.activity(Uri.fromFile(mediaFile))
                                        .setCropShape(shape)
                                        .setAspectRatio(point.x, point.y)
                                        .start(context);
                            } else
                                CropImage.activity(Uri.fromFile(mediaFile))
                                        .setCropShape(shape)
                                        .setAspectRatio(point.x, point.y)
                                        .start(fragment.getContext(), fragment);
                        } else {*/
                        imagePickerListener.onImageSelectedFromPicker(mediaFile);
//                        }
                    }
                }
                break;

            case 102:
                if (data != null && data.getData() != null && !FileUtils.isGooglePhotosUri(data.getData())) {
                    String videoPath = FileUtils.getPath(context, data.getData());
                    int maxSize = 0;
                    maxSize = 1024 * 30;
                    mediaFile = new File(videoPath);
                    long fileSizeInBytes = mediaFile.length();
                    long fileSizeInKB = fileSizeInBytes / 1024;
                    long fileSizeInMB = fileSizeInKB / 1024;
                    if (videoPath != null) {
                        if (FileUtils.getFileSizeKiloBytes(new File(videoPath)) > maxSize) {
                            videoErrorListener.onVideoSelectedError();
                            return;
                        }
                        videoPickerListener.onVideoSelectedFromPicker(mediaFile);
                    }
                }
                break;

            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                String imagePath = result.getUri().getPath();
                if (imagePath != null) {
                    mediaFile = new File(imagePath);
                    imagePickerListener.onImageSelectedFromPicker(mediaFile);
                }
        }
    }

    public void openGallery(@NonNull final Uri uri, final int requestCode) {
        checkListener();
        Intent intent = new Intent(Intent.ACTION_PICK, uri);
        try {
            if (requestCode == 101) {
                intent.setType(TYPE_IMAGE);
                intent.putExtra(Intent.EXTRA_MIME_TYPES, MIME_TYPES_IMAGE);
            } else {
                intent.setType(TYPE_VIDEO);
            }
            if (fragment == null)
                context.startActivityForResult(intent, requestCode);
            else
                fragment.startActivityForResult(intent, requestCode);
        } catch (Exception ex) {
            Log.e("TAG", "openGallery: " + ex.getMessage());
        }
    }

    @Nullable
    public File getMediaFile() {
        return mediaFile;
    }

    @Nullable
    private String getPathFromGallery(@NonNull final Context context,
                                      @NonNull final Uri imageUri) {
        String imagePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(imageUri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(projection[0]);
            imagePath = cursor.getString(columnIndex);
            cursor.close();
        }
        return imagePath;
    }

    @Nullable
    private File createFile(@NonNull final String directory, @NonNull final String fileName,
                            @NonNull final String fileSuffix) throws IOException {
        File file = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File storageDir = new File(directory);
            if (!storageDir.mkdirs()) {
                if (!storageDir.exists()) {
                    return null;
                }
            }
            file = File.createTempFile(fileName, fileSuffix, storageDir);
        }
        return file;
    }

    /**
     * Revoke access permission for the content URI to the specified package otherwise the permission won't be
     * revoked until the device restarts.
     */
    private void revokeUriPermission() {
        context.revokeUriPermission(FileProvider.getUriForFile(context,
                        context.getPackageName() + ".provider", mediaFile),
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }

    private void checkListener() {
        if (imagePickerListener == null) {
            throw new RuntimeException("ImagePickerListener must be set before calling openImageCamera() or openImageGallery()");
        }

        if (videoPickerListener == null) {
            throw new RuntimeException("VideoPickerListener must be set before calling openVideoCamera() or openVideoGallery()");
        }
    }

    public interface ImagePickerListener {
        void onImageSelectedFromPicker(@NonNull File imageFile);
    }

    public interface VideoPickerListener {
        void onVideoSelectedFromPicker(@NonNull File videoFile);
    }

    public interface VideoErrorListener {
        void onVideoSelectedError();
    }
}