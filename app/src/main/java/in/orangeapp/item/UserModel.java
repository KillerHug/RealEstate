package in.orangeapp.item;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel {
    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("password")
    @Expose
    String password;
    @SerializedName("state")
    @Expose
    String state;
    @SerializedName("isVerify")
    @Expose
    String isVerify;
    @SerializedName("otp")
    @Expose
    String otp;
    @SerializedName("device_id")
    @Expose
    String device_id;
    @SerializedName("created_at")
    @Expose
    String created_at;
    @SerializedName("visiting_card")
    @Expose
    String visiting_card;
    @SerializedName("parent_id")
    @Expose
    String parent_id;
    @SerializedName("company_firm")
    @Expose
    String company_firm;
    @SerializedName("profile_photo")
    @Expose
    String profile_photo;
    @SerializedName("mobile")
    @Expose
    String mobile;
    @SerializedName("email")
    @Expose
    String email;
    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("type")
    @Expose
    String type;
    @SerializedName("district")
    @Expose
    String district;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIsVerify() {
        return isVerify;
    }

    public void setIsVerify(String isVerify) {
        this.isVerify = isVerify;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getVisiting_card() {
        return visiting_card;
    }

    public void setVisiting_card(String visiting_card) {
        this.visiting_card = visiting_card;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getCompany_firm() {
        return company_firm;
    }

    public void setCompany_firm(String company_firm) {
        this.company_firm = company_firm;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
