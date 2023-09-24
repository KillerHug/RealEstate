package in.orangeapp.item;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdsDataModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("adv_type")
    @Expose
    private String advType;
    @SerializedName("editorial")
    @Expose
    private String editorial;
    @SerializedName("sponsored")
    @Expose
    private String sponsored;
    @SerializedName("sponsored_geo_type")
    @Expose
    private String sponsoredGeoType;
    @SerializedName("geo_id")
    @Expose
    private String geoId;
    @SerializedName("geo_name")
    @Expose
    private List<String> geoName;
    @SerializedName("filepath")
    @Expose
    private String filepath;
    @SerializedName("user_account")
    @Expose
    private String userAccount;
    @SerializedName("adv_ext_type")
    @Expose
    private String advExtType;
    @SerializedName("adv_ratio")
    @Expose
    private String advRatio;
    @SerializedName("cta_color")
    @Expose
    private String ctaColor;
    @SerializedName("cta_link")
    @Expose
    private String ctaLink;
    @SerializedName("no_of_display")
    @Expose
    private String noOfDisplay;
    @SerializedName("cta_type")
    @Expose
    private String ctaType;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("datetime")
    @Expose
    private String datetime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdvType() {
        return advType;
    }

    public void setAdvType(String advType) {
        this.advType = advType;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getSponsored() {
        return sponsored;
    }

    public void setSponsored(String sponsored) {
        this.sponsored = sponsored;
    }

    public String getSponsoredGeoType() {
        return sponsoredGeoType;
    }

    public void setSponsoredGeoType(String sponsoredGeoType) {
        this.sponsoredGeoType = sponsoredGeoType;
    }

    public String getGeoId() {
        return geoId;
    }

    public void setGeoId(String geoId) {
        this.geoId = geoId;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getAdvExtType() {
        return advExtType;
    }

    public void setAdvExtType(String advExtType) {
        this.advExtType = advExtType;
    }

    public String getAdvRatio() {
        return advRatio;
    }

    public void setAdvRatio(String advRatio) {
        this.advRatio = advRatio;
    }

    public String getCtaLink() {
        return ctaLink;
    }

    public void setCtaLink(String ctaLink) {
        this.ctaLink = ctaLink;
    }

    public String getNoOfDisplay() {
        return noOfDisplay;
    }

    public void setNoOfDisplay(String noOfDisplay) {
        this.noOfDisplay = noOfDisplay;
    }

    public String getCtaType() {
        return ctaType;
    }

    public void setCtaType(String ctaType) {
        this.ctaType = ctaType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public List<String> getGeoName() {
        return geoName;
    }

    public void setGeoName(List<String> geoName) {
        this.geoName = geoName;
    }

    public String getCtaColor() {
        return ctaColor;
    }

    public void setCtaColor(String ctaColor) {
        this.ctaColor = ctaColor;
    }
}