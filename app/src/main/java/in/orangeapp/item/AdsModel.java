package in.orangeapp.item;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdsModel {

   @SerializedName("status")
   @Expose
   private Integer status;
   @SerializedName("total_page")
   @Expose
   private Integer totalPage;
   @SerializedName("current_page")
   @Expose
   private Integer currentPage;
   @SerializedName("message")
   @Expose
   private String message;
   @SerializedName("data")
   @Expose
   private List<AdsDataModel> data;

   public Integer getStatus() {
      return status;
   }

   public void setStatus(Integer status) {
      this.status = status;
   }

   public Integer getTotalPage() {
      return totalPage;
   }

   public void setTotalPage(Integer totalPage) {
      this.totalPage = totalPage;
   }

   public Integer getCurrentPage() {
      return currentPage;
   }

   public void setCurrentPage(Integer currentPage) {
      this.currentPage = currentPage;
   }

   public String getMessage() {
      return message;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public List<AdsDataModel> getData() {
      return data;
   }

   public void setData(List<AdsDataModel> data) {
      this.data = data;
   }
}
