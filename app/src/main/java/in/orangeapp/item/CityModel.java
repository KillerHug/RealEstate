package in.orangeapp.item;

import com.google.gson.annotations.SerializedName;

public class CityModel {
    @SerializedName("districtid")
    String districtid;
    @SerializedName("district_title")
    String district_title;
    @SerializedName("district_description")
    String district_description;
    boolean isSelected=false;

    public CityModel(String districtid, String district_title, String district_description) {
        this.districtid = districtid;
        this.district_title = district_title;
        this.district_description = district_description;
    }

    public String getDistrictid() {
        return districtid;
    }

    public void setDistrictid(String districtid) {
        this.districtid = districtid;
    }

    public String getDistrict_title() {
        return district_title;
    }

    public void setDistrict_title(String district_title) {
        this.district_title = district_title;
    }

    public String getDistrict_description() {
        return district_description;
    }

    public void setDistrict_description(String district_description) {
        this.district_description = district_description;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
