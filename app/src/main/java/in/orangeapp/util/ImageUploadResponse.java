package in.orangeapp.util;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageUploadResponse {

    @Expose
    @SerializedName("imgurl")
    String imgurl;

    @Expose
    @SerializedName("message")
    String message;

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @NonNull
    @Override
    public String toString() {
        return "ImageUploadResponse{" +
                "imgurl='" + imgurl + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
