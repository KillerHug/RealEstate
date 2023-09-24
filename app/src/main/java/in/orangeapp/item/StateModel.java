package in.orangeapp.item;

import com.google.gson.annotations.SerializedName;

public class StateModel {
    @SerializedName("state_id")
    String state_id;
    @SerializedName("state_title")
    String state_title;
    boolean isSelected=false;

    public StateModel(String state_id, String state_title) {
        this.state_id=state_id;
        this.state_title=state_title;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getState_title() {
        return state_title;
    }

    public void setState_title(String state_title) {
        this.state_title = state_title;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
