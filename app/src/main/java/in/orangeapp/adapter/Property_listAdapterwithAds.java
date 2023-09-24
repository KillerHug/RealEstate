package in.orangeapp.adapter;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.android.volley.Request.Method.POST;
import static in.orangeapp.util.Constant.API_URL;
import static in.orangeapp.util.Constant.IMAGE_PATH;
import static in.orangeapp.util.Constant.SERVER_URLMAIN;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import in.orangeapp.item.Property_model;
import in.orangeapp.realestate.Addproperty;
import in.orangeapp.realestate.ActivityShowProperties;
import in.orangeapp.util.YourPreference;
import in.orangeapp.realestate.R;
import in.orangeapp.util.Constant;

import com.google.android.material.button.MaterialButton;
import com.vipul.hp_hp.library.Layout_to_Image;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Property_listAdapterwithAds extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<Property_model> subcat;
    ProgressDialog dialog;
    String s, name, mobile;

    Layout_to_Image layout_to_image;  //Create Object of Layout_to_Image Class
    private String districtt, namee, company_firmm, statee;
    private static final int PROPERTY_LAYOUT = 0;
    private static final int ADS_LAYOUT = 1;
    OnCTAButtonClick onCTAButtonClick;

    public Property_listAdapterwithAds(List<Property_model> subcat, Context context, String s, String name, String mobile,
                                       String districtt, String namee, String company_firmm, String statee, OnCTAButtonClick listener) {
        this.subcat = subcat;
        this.context = context;
        this.name = name;
        this.mobile = mobile;
        this.s = s;
        this.districtt = districtt;
        this.namee = namee;
        this.company_firmm = company_firmm;
        this.statee = statee;
        onCTAButtonClick = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
//        if (viewType == -1) {
//            itemView = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.item_start_home, parent, false);
//            return new FirstItemViewHolder(itemView);
//        }
        RecyclerView.ViewHolder viewHolder;
        if (viewType == 0) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.proprty_lis_row, parent, false);
            viewHolder = new PropertyViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.advert_row_new, parent, false);
            viewHolder = new MyViewHolder(itemView);
        }
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, @SuppressLint("RecyclerView") final int position) {
        /*if (position == 0) {
            FirstItemViewHolder holder = (FirstItemViewHolder) holder1;

            holder.name.setText(namee);
            holder.location.setText(districtt + ", " + statee);
            if (company_firmm.equalsIgnoreCase("")) {

                holder.firm_name.setText("");
                holder.firm_name.setVisibility(View.GONE);

            } else {
                holder.firm_name.setText(company_firmm);
            }
            holder.add_property.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(holder.itemView.getContext(), Addproperty.class);
                    intent.putExtra("from", "add");
                    holder.itemView.getContext().startActivity(intent);
                }
            });
            holder.btn_postRequirement.setOnClickListener(v -> {
                Intent intent = new Intent(holder.itemView.getContext(), AddRequirementActivity.class);
                intent.putExtra("from", "add");
                holder.itemView.getContext().startActivity(intent);
            });

            holder.search_prop.setOnClickListener(v -> {
                Intent intent = new Intent(holder.itemView.getContext(), SearchProperty.class);
                holder.itemView.getContext().startActivity(intent);
            });
        } else {*/
//        Log.e("TAG", "onBindViewHolder: " + subcat.get(position).getImg_list());
        if (holder1.getItemViewType() == 0) {
            PropertyViewHolder holder = (PropertyViewHolder) holder1;

            holder.share.setVisibility(View.GONE);
            holder.shareWhatsUp.setVisibility(View.GONE);
            if (subcat.get(position).getImg_list() == null || subcat.get(position).getImg_list().equals("")) {
                holder.showImages.setVisibility(View.GONE);
            } else {
                holder.showImages.setVisibility(View.VISIBLE);
            }

            holder.showImages.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Constant.images.clear();
                    String[] list = subcat.get(position).getImg_list().split(",");
                    Constant.images.addAll(Arrays.asList(list));
                    holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), ActivityShowProperties.class));

                }
            });

            holder.property_id.setText("Prop Id: " + subcat.get(position).getId());

            holder.date.setText("Posted On " + subcat.get(position).getCreated_at());

            YourPreference yourPrefrence = YourPreference.getInstance(context);
            String user_id = yourPrefrence.getData("user_id");

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            final View popupView = layoutInflater.inflate(R.layout.popup_dropdown, null);
            final PopupWindow popupWindow = new PopupWindow(popupView, 200, LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView edit = popupView.findViewById(R.id.edit);
            TextView delete = popupView.findViewById(R.id.delete);
            TextView share = popupView.findViewById(R.id.share);

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Are you sure?");

                    builder.setPositiveButton("No", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
                            dialog.dismiss();

                        }
                    });

                    builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteuser(subcat.get(position).getId());

                            removeAt(position);
                            dialog.dismiss();
                            // Do nothing
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                }
            });

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();


                }
            });
            holder.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (popupWindow.isShowing())
                        popupWindow.dismiss();
                    else
                        popupWindow.showAsDropDown(holder.menu, 0, 10);

                }
            });
            if (s.equalsIgnoreCase("1")) {

                try {
                    if (subcat.get(position).getUploader_id().equalsIgnoreCase(user_id)) {
                        holder.edit_layout.setVisibility(View.VISIBLE);
                        share.setVisibility(View.GONE);
                    } else {

                        if (user_id.equalsIgnoreCase("39")) {
                            holder.edit_layout.setVisibility(View.VISIBLE);

                        } else {
                            holder.edit_layout.setVisibility(View.GONE);
                            edit.setVisibility(View.GONE);
                            delete.setVisibility(View.GONE);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
//                    holder.share.setVisibility(View.VISIBLE);
                edit.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);

            }


            String plotarea = subcat.get(position).getPlot_area();
            String land_area = subcat.get(position).getLand_area();
            String construction_type = subcat.get(position).getConstruction_type();
            String scheme = subcat.get(position).getScheme();
            String licence = subcat.get(position).getLicence();
            String total_timefp = subcat.get(position).getTotal_timefp();
            String property_type = subcat.get(position).getProperty_type();
            String price_psqft = subcat.get(position).getPrice_psqft();
            String main_type = subcat.get(position).getMain_type();
            String sub_type = subcat.get(position).getSub_type();
            String state = subcat.get(position).getState();
            String district = subcat.get(position).getDistrict();
            String location = subcat.get(position).getLocation();
            String p_feature = subcat.get(position).getP_feature();
            String road_width = subcat.get(position).getRoad_width();
            String bhk = subcat.get(position).getBhk();
            String usp = subcat.get(position).getUsp();
            String furnishing_type = subcat.get(position).getFurnishing_type();
            String carpet_area = subcat.get(position).getCarpet_area();
            String floor_type = subcat.get(position).getFloor_type();
            String lift_type = subcat.get(position).getLift_type();
            String parking_type = subcat.get(position).getParking_type();
            String description = subcat.get(position).getDescription();
            String suitable_for = subcat.get(position).getSuitable_for();
            String rented_property_type = subcat.get(position).getRented_property_type();
            String lease_to = subcat.get(position).getLease_to();
            String lease_period = subcat.get(position).getLease_period();
            String lock_inperiod = subcat.get(position).getLock_inperiod();
            String lease_start_date = subcat.get(position).getLease_start_date();
            String leasePeriod = subcat.get(position).getLease_period();
            String rent_pm = subcat.get(position).getRent_pm();
            String new_constrution_status = subcat.get(position).getNew_constrution_status();
            String old_property_year = subcat.get(position).getOld_property_year();
            String address = subcat.get(position).getAddress();
            String uploader_name = subcat.get(position).getUploader_name();
            String uploader_number = subcat.get(position).getUploader_number();
            String uploader_v_card = subcat.get(position).getUploader_v_card();
            String uploader_type = subcat.get(position).getUploader_type();
            String roi = subcat.get(position).getRoi();
            String postType = subcat.get(position).getPost_type();
            if (postType.length() < 1)
                postType = " Available ";

            String finalConstruction_type = construction_type;

            holder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bitmap bitmap = generateBitmap(holder.rootLayout);
                    //  layout_to_image=new Layout_to_Image(context,holder.rootLayout);
                    //  Bitmap  bitmap=layout_to_image.convert_layout();
                    onClickApp("com.whatsapp", bitmap);
                }
            });

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();

                    Intent intent = new Intent(context, Addproperty.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("from", "edit");
                    intent.putExtra("state", subcat.get(position).getState());
                    intent.putExtra("district", subcat.get(position).getDistrict());
                    intent.putExtra("roi", roi);
                    intent.putExtra("uploader_type", uploader_type);
                    intent.putExtra("uploader_v_card", uploader_v_card);
                    intent.putExtra("uploader_number", uploader_number);
                    intent.putExtra("uploader_name", uploader_name);
                    intent.putExtra("address", address);
                    intent.putExtra("old_property_year", old_property_year);
                    intent.putExtra("new_constrution_status", new_constrution_status);
                    intent.putExtra("rent_pm", rent_pm);
                    intent.putExtra("leasePeriod", leasePeriod);
                    intent.putExtra("lease_start_date", lease_start_date);
                    intent.putExtra("lock_inperiod", lock_inperiod);
                    intent.putExtra("lease_to", lease_to);
                    intent.putExtra("rented_property_type", rented_property_type);
                    intent.putExtra("suitable_for", suitable_for);
                    intent.putExtra("description", description);
                    intent.putExtra("parking_type", parking_type);
                    intent.putExtra("lift_type", lift_type);
                    intent.putExtra("floor_type", floor_type);
                    intent.putExtra("carpet_area", carpet_area);
                    intent.putExtra("furnishing_type", furnishing_type);
                    intent.putExtra("usp", usp);
                    intent.putExtra("bhk", bhk);
                    intent.putExtra("road_width", road_width);
                    intent.putExtra("p_feature", p_feature);
                    intent.putExtra("location", location);
                    intent.putExtra("sub_type", sub_type);
                    intent.putExtra("main_type", main_type);
                    intent.putExtra("price_psqft", price_psqft);
                    intent.putExtra("property_type", property_type);
                    intent.putExtra("licence", licence);
                    intent.putExtra("total_timefp", total_timefp);
                    intent.putExtra("scheme", scheme);
                    intent.putExtra("construction_type", finalConstruction_type);
                    intent.putExtra("land_area", land_area);
                    intent.putExtra("plotarea", plotarea);
                    intent.putExtra("sellername", subcat.get(position).getSeller_name());
                    intent.putExtra("sellermobile", subcat.get(position).getSeller_mobile());
                    intent.putExtra("sellertype", subcat.get(position).getSeller_type());
                    intent.putExtra("price", subcat.get(position).getPrice());
                    intent.putExtra("property_id", subcat.get(position).getId());
                    intent.putExtra("img_string", subcat.get(position).getImg_list());
                    intent.putExtra("payment_type", subcat.get(position).getPayment_type());
                    context.startActivity(intent);

                }
            });

            String location2;
            String hairSpace = context.getString(R.string.hair_space);

            if (main_type.equalsIgnoreCase("rent/lease")) {
                holder.price.setText(subcat.get(position).getPrice() + " Rupees/Month");

            } else {
                holder.price.setText(subcat.get(position).getPrice() + " " + subcat.get(position).getPayment_type());

            }


            if (s.equalsIgnoreCase("0")) {

                holder.seller_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + uploader_number));
                        context.startActivity(intent);

                    }
                });
                holder.uplodede_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holder.visitingcard.getVisibility() == View.VISIBLE) {
                            holder.visitingcard.setVisibility(View.GONE);
                        } else {
                            holder.visitingcard.setVisibility(View.VISIBLE);
                        }

                    }
                });

                holder.seller_detail.setText(Html.fromHtml("For more details kindly contact : <b>" + uploader_type + "</b> " + uploader_name + "-<font color=\"#0000FF\">" + uploader_number + "</font>"));
                location2 = "in " + location;
                if (uploader_type.equalsIgnoreCase("Real Estate Agent") || uploader_type.equalsIgnoreCase("Builder") || uploader_type.equalsIgnoreCase("Supervisor")) {
                    holder.uplodede_name.setText(subcat.get(position).getUploader_company());


                } else {
                    holder.uplodede_name.setText(subcat.get(position).getUploader_name());

                }
                Glide.with(context)
                        .load(IMAGE_PATH + uploader_v_card)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.visiting_card)
                        .into(holder.visitingcard);

            } else {
                holder.uplodede_name.setText(subcat.get(position).getUploader_name());

                holder.seller_detail.setText(Html.fromHtml("For more details kindly contact : <b>" + subcat.get(position).getSeller_type() + "</b> ( " + subcat.get(position).getSeller_name() + " - " + "<font color=\"#FF018EFF\">" + subcat.get(position).getSeller_mobile() + "</font> )"));
                location2 = "at " + address + " in " + location;
                holder.visitingcard.setVisibility(View.GONE);
            }
            if (construction_type.equalsIgnoreCase("New Construction")) {
                construction_type = new_constrution_status;
            } else {
                construction_type = "Approx " + old_property_year + " Years old constructed";

            }

            if (postType.trim().equalsIgnoreCase("Available")) {
                holder.tvAskingPrice.setText("Asking Price:-");
                holder.description.setVisibility(View.VISIBLE);
                if (property_type.equalsIgnoreCase("Villa/House")) {
                    holder.heading.setText(Html.fromHtml("<font color=\"#4CAF50\">" + postType + "</font>" + " " + plotarea + " Sq.Yds. " + "<font color=\"#01B0F0\">" + property_type + "</font>" + " for " + main_type + " at " + location
                            + " in " + district + ", " + state));


                    holder.description.setText(Html.fromHtml(p_feature + ", " + usp + ", " + plotarea + " Sq.Yds. " + construction_type + " , " + furnishing_type
                            + " " + property_type + " having a total carpet area of " + carpet_area + " Sq.Ft. built-up on " + floor_type + " " + lift_type
                            + ", " + parking_type + " available for " + main_type + " " + location2 + "."));


                } else if (property_type.equalsIgnoreCase("Plot/Land")) {
                    holder.heading.setText(Html.fromHtml("<font color=\"#4CAF50\">" + postType + "</font>" + " " + plotarea + " Sq.Yds. " + sub_type + " <font color=\"#00B04F\">" + property_type + "</font>" + " for " + main_type + " at " + location
                            + " in " + district + ", " + state));


                    holder.description.setText(p_feature + ", " + usp + ", " + plotarea + " Sq.Yds. " + sub_type + " " + property_type + " available for " + main_type + " " + location2 + ".");

                } else if (property_type.equalsIgnoreCase("Commercial Land")) {
                    holder.heading.setText(Html.fromHtml("<font color=\"#4CAF50\">" + postType + "</font>" + " " + plotarea + " Sq.Yds. " + " <font color=\"#76923B\">" + property_type + "</font>" + " for " + main_type + " at " + location
                            + " in " + district + ", " + state));


                    holder.description.setText(Html.fromHtml(p_feature + ", " + usp + ", " + property_type + " having total land area of " + plotarea + " Sq.Yds. suitable for <b>" + suitable_for + "</b> available for " + main_type + " " + location2 + "."));

                } else if (property_type.equalsIgnoreCase("Builder Floor")) {

                    holder.heading.setText(Html.fromHtml("<font color=\"#4CAF50\">" + postType + " </font>" + " " +
                            plotarea + " Sq.Yds. " + bhk + ", " + construction_type + " Independent " +
                            " <font color=\"#E36C08\">" + property_type + "</font>" + " for " + main_type + " at " + location
                            + " in " + district + ", " + state));


                    holder.description.setText(p_feature + ", " + usp + ", " + plotarea + " Sq.Yds. " + bhk + ", " + construction_type + " , " + furnishing_type
                            + " " + property_type + " having a total carpet area of " + carpet_area + " Sq.Ft. " + lift_type
                            + ", " + parking_type + " at " + floor_type + " available for " + main_type + " " + location2 + "."
                    );


                } else if (property_type.equalsIgnoreCase("Flat")) {
                    holder.heading.setText(Html.fromHtml("<font color=\"#4CAF50\">" + postType + "</font>" + " " + bhk + ", " + " <font color=\"#31849B\">" + property_type + "</font>" + " for " + main_type + " at " + location
                            + " in " + district + ", " + state));


                    holder.description.setText(p_feature + ", " + usp + ", " + bhk + ", " + construction_type + " , " + furnishing_type
                            + " " + property_type + " having a total carpet area of " + carpet_area + " Sq.Ft. " + lift_type
                            + ", " + parking_type + " at " + floor_type + " available for " + main_type + " " + location2 + "."
                    );

                } else if (property_type.equalsIgnoreCase("Duplex")) {
                    holder.heading.setText(Html.fromHtml("<font color=\"#4CAF50\">" + postType + "</font>" + " " + bhk + ", " + plotarea + " Sq.Yds. " + " <font color=\"#FF9900\">" + property_type + "</font>" + " for " + main_type + " at " + location
                            + " in " + district + ", " + state));


                    holder.description.setText(p_feature + ", " + usp + ", " + bhk + ", " + plotarea + " Sq.Yds. " + construction_type + " , " + furnishing_type
                            + " " + property_type + " having a total carpet area of " + carpet_area + " Sq.Ft. " + lift_type
                            + ", " + parking_type + " at " + floor_type + " available for " + main_type + " " + location2 + "."
                    );

                } else if (property_type.equalsIgnoreCase("Office Space")) {
                    holder.heading.setText(Html.fromHtml("<font color=\"#4CAF50\">" + postType + "</font>" + " " + carpet_area + " Sq.Ft. Commercial " + " <font color=\"#FF0100\">" + property_type + "</font>" + " for " + main_type + " at " + location
                            + " in " + district + ", " + state));


                    holder.description.setText(Html.fromHtml(p_feature + ", " + usp + ", " + construction_type + " , " + furnishing_type
                            + " " + property_type + " having a total carpet area of " + carpet_area + " Sq.Ft. " + lift_type
                            + ", " + parking_type + " Suitable for <b>" + suitable_for + " </b>available at " + floor_type + " available for " + main_type + " " + location2 + "."
                    ));

                } else if (property_type.equalsIgnoreCase("Shop/Showroom")) {
                    holder.heading.setText(Html.fromHtml("<font color=\"#4CAF50\">" + postType + "</font>" + " " + carpet_area + " Sq.Ft. Commercial " + " <font color=\"#E26C09\">" + property_type + "</font>" + " for " + main_type + " at " + location
                            + " in " + district + ", " + state));


                    holder.description.setText(Html.fromHtml(p_feature + ", " + usp + ", " + construction_type + " , " + furnishing_type
                            + " " + property_type + " having a total carpet area of " + carpet_area + " Sq.Ft. " + lift_type
                            + ", " + parking_type + " Suitable for <b>" + suitable_for + "</b> available at " + floor_type + " available for " + main_type + " " + location2 + "."
                    ));

                } else if (property_type.equalsIgnoreCase("Shop Cum Office (SCO)")) {
                    holder.heading.setText(Html.fromHtml("<font color=\"#4CAF50\">" + postType + "</font>" + " " + plotarea + " Sq.Yds. " + construction_type + ", " + " <font color=\"#FF3399\">" + property_type + "</font>" + " for " + main_type + " at " + location
                            + " in " + district + ", " + state));
                    holder.description.setText(p_feature + ", " + usp + ", " + plotarea + " Sq.Yds. " + construction_type + " , " + furnishing_type
                            + " " + property_type + " having a total carpet area of " + carpet_area + " Sq.Ft. " + lift_type
                            + ", " + parking_type + " available for " + main_type + " " + location2 + "."
                    );

                } else if (property_type.equalsIgnoreCase("Basement")) {
                    holder.heading.setText(Html.fromHtml("<font color=\"#4CAF50\">" + postType + "</font>" + " " + carpet_area + " Sq.Ft. " + construction_type + ", Commercial " + " <font color=\"#E26C09\">" + property_type + "</font>" + " for " + main_type + " at " + location
                            + " in " + district + ", " + state));
                    holder.description.setText(Html.fromHtml(p_feature + ", " + usp + ", " + carpet_area + " Sq.Ft. " + construction_type + " , " + furnishing_type
                            + " " + property_type + " having a total carpet area of " + carpet_area + " Sq.Ft. Suitable for <b>" + suitable_for + "</b> " + lift_type
                            + ", " + parking_type + " available for " + main_type + " " + location2 + "."
                    ));

                } else if (property_type.equalsIgnoreCase("Factory/Building/Warehouse")) {
                    holder.heading.setText(Html.fromHtml("<font color=\"#4CAF50\">" + postType + "</font>" + " " + plotarea + " Sq.Yds. " + construction_type + " " + " <font color=\"#92D050\">" + property_type + "</font>" + " for " + main_type + " at " + location
                            + " in " + district + ", " + state));
                    holder.description.setText(Html.fromHtml(p_feature + ", " + usp + ", " + plotarea + " Sq.Yds. " + construction_type + ", " + property_type + " having a total carpet area of " + carpet_area + " Sq.Ft. Suitable for <b>" + suitable_for + "</b> at " + parking_type + " available for " + main_type + " " + location2 + "."

                    ));

                } else if (property_type.equalsIgnoreCase("Rented property")) {
                    holder.heading.setText(Html.fromHtml("<font color=\"#4CAF50\">" + postType + "</font>" + " " + carpet_area + " Sq.Ft. " + construction_type + " " + rented_property_type + " as Pre-" + " <font color=\"#C0504D\">" + property_type + "</font>" + " for " + main_type + " at " + location
                            + " in " + district + ", " + state));
                    holder.description.setText(p_feature + ", " + usp + ", " + construction_type + " ," + furnishing_type + " , Pre-" + property_type + " having a total carpet area of " + carpet_area + " Sq.Ft. " + lift_type + ", " + parking_type + " leased to " + lease_to + " with a total lease period of " + lease_period + " years having a lock in period of " + lock_inperiod +
                            " years. Lease started from " + lease_start_date + " with a monthly rental of " + rent_pm + " available for " + main_type + " " + location2 + ". \n \n Return of Investment (ROI): " + roi + " percent");

                } else if (property_type.equalsIgnoreCase("Farm House")) {
                    holder.heading.setText(Html.fromHtml("<font color=\"#4CAF50\">" + postType + "</font>" + " " + bhk + ", " + " <font color=\"#00AF50\">" + property_type + "</font>" + " for " + main_type + " at " + location
                            + " in " + district + ", " + state));


                    holder.description.setText(p_feature + ", " + usp + ", " + bhk + ", " + carpet_area + " Sq.Ft. " + construction_type + " , " + furnishing_type
                            + " " + property_type + " having a total Land area of " + plotarea + " Sq.Yds. " + lift_type
                            + " " + parking_type + " for " + main_type + " " + location2 + "."
                    );

                } else if (property_type.equalsIgnoreCase("Agriculture Land")) {
                    holder.heading.setText(Html.fromHtml("<font color=\"#4CAF50\">" + postType + "</font>" + " " + plotarea + " Sq.Yds. " + " <font color=\"#00AF50\">" + property_type + "</font>" + " for " + main_type + " at " + location
                            + " in " + district + ", " + state));


                    holder.description.setText(p_feature + ", " + usp + ", " + plotarea + " Sq.Yds. " + property_type + " available for " + main_type + " " + location2 + "."
                    );
                } else if (property_type.equalsIgnoreCase("Floor Space Index (FSI)")) {
                    holder.heading.setText(Html.fromHtml("<font color=\"#4CAF50\">" + postType + "</font>" + " " + land_area + " Acers, " + " <font color=\"#A71F71\">" + property_type + "</font>" + " for " + main_type + " at " + location
                            + " in " + district + ", " + state));


                    holder.description.setText(Html.fromHtml(p_feature + ", " + usp + ", " + land_area + " Acers." + property_type + " on main road having width " + road_width + " meters available for " + main_type + " at a price Rs. <b>" + price_psqft + "</b> per Sq.Ft. at " + location2 + ".<br> <br>Scheme: "
                            + scheme + "<br>Licence: " + licence + "<br> Total Time For Payment: " + total_timefp + " Months"
                    ));
                }
            } else {
                holder.description.setVisibility(View.GONE);
                holder.tvAskingPrice.setText("Budget:-");
                if (property_type.equalsIgnoreCase("Villa/House")) {
                    holder.heading.setText(Html.fromHtml(
                            "<font color=\"#FF0000\">" + postType + "</font>"
                                    + " " + plotarea + " Sq.Yds. "
                                    + "<font color=\"#01B0F0\">" + property_type + "</font>"
                                    + " for " + main_type + " at " + location
                                    + " in " + district + ", " + state));
                    //holder.description.setText(Html.fromHtml(p_feature + ", " + usp + ", " + plotarea + " Sq.Yds. " + construction_type + " , " + furnishing_type+ " " + property_type + " having a total carpet area of " + carpet_area + " Sq.Ft. built-up on " + floor_type + " " + lift_type+ ", " + parking_type + " available for " + main_type + " " + location2 + "."));
                } else if (property_type.equalsIgnoreCase("Plot/Land")) {
                    holder.heading.setText(Html.fromHtml(
                            "<font color=\"#FF0000\">" + postType + "</font>"
                                    + " " + plotarea + " Sq.Yds. "
                                    + sub_type + " <font color=\"#00B04F\">"
                                    + property_type + "</font>" + " for "
                                    + main_type + " at " + location
                                    + " in " + district + ", " + state));
                    //holder.description.setText(p_feature + ", " + usp + ", " + " " + plotarea + " Sq.Yds. " + sub_type + " " + property_type + " available for " + main_type + " " + location2 + ".");
                } else if (property_type.equalsIgnoreCase("Commercial Land")) {
                    holder.heading.setText(Html.fromHtml("<font color=\"#FF0000\">" + postType +
                            "</font>" + " " + plotarea + " Sq.Yds. " + "<font color=\"#76923B\">" +
                            property_type + "</font>" + " for " + main_type + " at " + location
                            + " in " + district + ", " + state));
                    //holder.description.setText(Html.fromHtml(p_feature + ", " + usp + ", " + property_type + " having total land area of " + plotarea + " Sq.Yds. suitable for <b>" + suitable_for + "</b> available for " + main_type + " " + location2 + "."));
                } else if (property_type.equalsIgnoreCase("Builder Floor")) {
                    holder.heading.setText(Html.fromHtml("<font color=\"#FF0000\">" + postType + "</font>" + " "
                            + plotarea + " Sq.Yds. " + bhk + "," + " Independent " +
                            " <font color=\"#E36C08\">" + property_type + "</font>" + /*" at " + floor_type +*/ " for " + main_type + " at " + location
                            + " in " + district + ", " + state));
                    //holder.description.setText(p_feature + ", " + usp + ", " + plotarea + " Sq.Yds. " + bhk + ", " + construction_type + " , " + furnishing_type+ " " + property_type + " having a total carpet area of " + carpet_area + " Sq.Ft. " + lift_type + ", " + parking_type + " at " + floor_type + " available for " + main_type + " " + location2 + ".");
                } else if (property_type.equalsIgnoreCase("Flat")) {
                    holder.heading.setText(Html.fromHtml("<font color=\"#FF0000\">" + postType + "</font>" + " " + bhk + ", "
                            + " <font color=\"#31849B\">" + property_type + "</font>" + " for " + main_type + " at " + location
                            + " in " + district + ", " + state));
                    //holder.description.setText(p_feature + ", " + usp + ", " + bhk + ", " + construction_type + " , " + furnishing_type+ " " + property_type + " having a total carpet area of " + carpet_area + " Sq.Ft. " + lift_type + ", " + parking_type + " at " + floor_type + " available for " + main_type + " " + location2 + ".");
                } else if (property_type.equalsIgnoreCase("Duplex")) {
                    holder.heading.setText(Html.fromHtml("<font color=\"#FF0000\">" + postType + "</font>" + " " + bhk +
                            " <font color=\"#FF9900\">" + property_type +
                            "</font>" + " for " + main_type + " at " + location
                            + " in " + district + ", " + state));
                    //holder.description.setText(p_feature + ", " + usp + ", " + bhk + ", " + plotarea + " Sq.Yds. " + ", " + construction_type + " , " + furnishing_type+ " " + property_type + " having a total carpet area of " + carpet_area + " Sq.Ft. " + lift_type + ", " + parking_type + " at " + floor_type + " available for " + main_type + " " + location2 + ".");
                } else if (property_type.equalsIgnoreCase("Office Space")) {
                    holder.heading.setText(Html.fromHtml("<font color=\"#FF0000\">" + postType + "</font>" + " " +
                            carpet_area + " Sq.Ft. Commercial " + " <font color=\"#FF0100\">" + property_type + "</font>" +
                            " for " + main_type + " at " + location
                            + " in " + district + ", " + state));
                    //holder.description.setText(Html.fromHtml(p_feature + ", " + usp + ", " + construction_type + " , " + furnishing_type+ " " + property_type + " having a total carpet area of " + carpet_area + " Sq.Ft. " + lift_type + ", " + parking_type + " Suitable for <b>" + suitable_for + " </b>available at " + floor_type + " available for " + main_type + " " + location2 + "."));
                } else if (property_type.equalsIgnoreCase("Shop/Showroom")) {


                    holder.heading.setText(Html.fromHtml("<font color=\"#FF0000\">" + postType + "</font>"
                            + " " + carpet_area + " Sq.Ft. Commercial " + " <font color=\"#E26C09\">"
                            + property_type + "</font>" + " for " + main_type + " at " + location
                            + " in " + district + ", " + state));
                    //holder.description.setText(Html.fromHtml(p_feature + ", " + usp + ", " + construction_type + " , " + furnishing_type+ " " + property_type + " having a total carpet area of " + carpet_area + " Sq.Ft. " + lift_type + ", " + parking_type + " Suitable for <b>" + suitable_for + "</b> available at " + floor_type + " available for " + main_type + " " + location2 + "."));

                } else if (property_type.equalsIgnoreCase("Shop Cum Office (SCO)")) {
                    holder.heading.setText(Html.fromHtml("<font color=\"#FF0000\">" + postType +
                            "</font>" + " " + plotarea + " Sq.Yds. " +
                            "<font color=\"#FF3399\">" + property_type + "</font>" + " for " + main_type + " at " + location
                            + " in " + district + ", " + state));
                    //holder.description.setText(p_feature + ", " + usp + ", " + plotarea + " Sq.Yds. , " + construction_type + " , " + furnishing_type+ " " + property_type + " having a total carpet area of " + carpet_area + " Sq.Ft. " + lift_type + ", " + parking_type + " available for " + main_type + " " + location2 + ".");

                } else if (property_type.equalsIgnoreCase("Basement")) {
                    holder.heading.setText(Html.fromHtml("<font color=\"#FF0000\">" +
                            postType + "</font>" + " " + carpet_area + " Sq.Ft. " + "Commercial " + " <font color=\"#E26C09\">" +
                            property_type + "</font>" + " for " + main_type + " at " + location
                            + " in " + district + ", " + state));
                    //holder.description.setText(Html.fromHtml(p_feature + ", " + usp + ", " + carpet_area + " Sq.Ft. " + construction_type + " , " + furnishing_type+ " " + property_type + " having a total carpet area of " + carpet_area + " Sq.Ft. Suitable for <b>" + suitable_for + "</b> " + lift_type + ", " + parking_type + " available for " + main_type + " " + location2 + "."));

                } else if (property_type.equalsIgnoreCase("Factory/Building/Warehouse")) {
                    holder.heading.setText(Html.fromHtml("<font color=\"#FF0000\">" + postType + "</font>" + " " +
                            plotarea + " Sq.Yds. " + "<font color=\"#92D050\">" +
                            property_type + "</font>" + " for " + main_type + " at " + location
                            + " in " + district + ", " + state));
                    //holder.description.setText(Html.fromHtml(p_feature + ", " + usp + ", " + plotarea + " Sq.Yds. " + construction_type + ", " + property_type + " having a total carpet area of " + carpet_area + " Sq.Ft. Suitable for <b>" + suitable_for + "</b> at " + parking_type + " available for " + main_type + " " + location2 + "."));

                } else if (property_type.equalsIgnoreCase("Rented property")) {
                    holder.heading.setText(Html.fromHtml("<font color=\"#FF0000\">" + postType + "</font>" + " " + carpet_area + " Sq.Ft. " + construction_type + " " + rented_property_type + " as Pre-" + " <font color=\"#C0504D\">" + property_type + "</font>" + " for " + main_type + " at " + location
                            + " in " + district + ", " + state));
                    //holder.description.setText(p_feature + ", " + usp + ", " + construction_type + " ," + furnishing_type + " , Pre-" + property_type + " having a total carpet area of " + carpet_area + " Sq.Ft., " + lift_type + ", " + parking_type + " leased to " + lease_to + " with a total lease period of " + lease_period + " years having a lock in period of " + lock_inperiod +" years. Lease started from " + lease_start_date + " with a monthly rental of " + rent_pm + " available for " + main_type + " " + location2 + ". \n \n Return of Investment (ROI): " + roi + " percent");

                } else if (property_type.equalsIgnoreCase("Farm House")) {
                    holder.heading.setText(Html.fromHtml("<font color=\"#FF0000\">" +
                            postType + "</font>" + " " + plotarea + " Sq.Yds. " + bhk + ", " + "<font color=\"#00AF50\">" +
                            property_type + "</font>" + " for " + main_type + " at " + location
                            + " in " + district + ", " + state));


                    //holder.description.setText(p_feature + ", " + usp + ", " + bhk + ", " + carpet_area + " Sq.Ft. " + construction_type + " , " + furnishing_type                        + " " + property_type + " having a total Land area of " + plotarea + " Sq.Yds. " + lift_type                        + " " + parking_type + " for " + main_type + " " + location2 + "."                );

                } else if (property_type.equalsIgnoreCase("Agriculture Land")) {
                    holder.heading.setText(Html.fromHtml("<font color=\"#FF0000\">" +
                            postType + "</font>" + " " + plotarea + " Sq.Yds. " + "<font color=\"#00AF50\">" +
                            property_type + "</font>" + " for " + main_type + " at " + location
                            + " in " + district + ", " + state));


                    //holder.description.setText(p_feature + ", " + usp + ", " + plotarea + " Sq.Yds. " + property_type + " available for " + main_type + " " + location2 + ".");
                }
            }
            if (description.equalsIgnoreCase("")) {
                holder.note.setVisibility(View.GONE);
            } else {
                holder.note.setVisibility(View.VISIBLE);
                holder.note.setText(Html.fromHtml("<b>Note:-</b> " + description));
            }

            if (s.equalsIgnoreCase("1")) {
                holder.fav.setVisibility(View.GONE);
            }
            if (subcat.get(position).getFavourite().equalsIgnoreCase("1")) {
                holder.fav.setImageResource(R.drawable.ic_fav_hover);

            } else {
                holder.fav.setImageResource(R.drawable.ic_favourite);

            }
            holder.fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    makefav(subcat.get(position).getId(), position);
                    if (subcat.get(position).getFavourite().equalsIgnoreCase("1")) {
                        holder.fav.setImageResource(R.drawable.ic_favourite);
                        if (name.equalsIgnoreCase("favourite")) {
                            removeAt(position);
                        }
                    } else {
                        holder.fav.setImageResource(R.drawable.ic_fav_hover);
                    }
                }
            });
        } else {
            MyViewHolder holder = (MyViewHolder) holder1;
            holder.title.setText(subcat.get(position).getCta_type());
            Glide.with(context)
                    .load(subcat.get(position).getFilepath())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.image);
            Glide.with(context)
                    .load(IMAGE_PATH + subcat.get(position).getUser_detail().getProfile_photo())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.profileImage);
            YourPreference yourPrefrence = YourPreference.getInstance(context);
            String user_id = yourPrefrence.getData("user_id");
            if (subcat.get(position).getTitle() != null && subcat.get(position).getTitle().equalsIgnoreCase("")) {
                holder.header_card.setVisibility(View.GONE);
            }

            if (subcat.get(position).getCta_color() != null && !subcat.get(position).getCta_color().isEmpty()) {
                holder.header_card.setBackgroundColor(Integer.parseInt(subcat.get(position).getCta_color()));
                holder.tvUserName.setTextColor(Integer.parseInt(subcat.get(position).getCta_color()));
            }
            holder.header_card.setOnClickListener(v -> {
                onCTAButtonClick.onCTAClick(subcat.get(position).getCta_type(), subcat.get(position).getCta_link());
            });
            if (subcat.get(position).getAdv_ext_type().equalsIgnoreCase("Image")) {
                holder.videoView.setVisibility(View.GONE);
                holder.ivPlay.setVisibility(View.GONE);
                holder.image.setVisibility(View.VISIBLE);
            } else {
                Log.e("TAG", "onBindViewHolder: file path---"+subcat.get(position).getFilepath() );
                holder.videoView.setVisibility(View.VISIBLE);
                holder.ivPlay.setVisibility(View.VISIBLE);
                holder.image.setVisibility(View.GONE);
                if (!subcat.get(position).getFilepath().isEmpty()) {
                    Uri uri = Uri.parse(subcat.get(position).getFilepath());
                    holder.videoView.setVideoURI(uri);
                    holder.ivPlay.setOnClickListener(v -> {
                        if (holder.videoView.isPlaying()) {
                            holder.videoView.pause();
                        } else {
                            holder.videoView.start();
                        }
                    });
                }
            }
//            holder.delete.setOnClickListener(view -> {
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle("Are you sure?");
//                builder.setMessage("This Advertisement will be lost permanently and cannot be retrived.");
//                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                builder.setNegativeButton("Yes", (dialog, which) -> {
//                    deleteAd(subcat.get(position).getId());
//                    removeAt(position);
//                    dialog.dismiss();
//                });
//
//                AlertDialog alert = builder.create();
//                alert.show();
//
//            });
            holder.tvUserName.setText(subcat.get(position).getUser_detail().getName());
            holder.tvAdsType.setText(subcat.get(position).getAdv_type());
        }
    }

    private void deleteAd(String id) {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");
        dialog.show();
        String url = SERVER_URLMAIN + "delete_advert";
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        Map<String, String> params = new HashMap();
        params.put("id", id);
        JSONObject parameters = new JSONObject(params);
        Log.i("user_list par]", "" + parameters);

        JsonObjectRequest stringRequest = new JsonObjectRequest(POST, url, parameters, response -> {
            Log.i("user_list Res", "" + response);
            dialog.dismiss();

            try {
                JSONObject obj = new JSONObject(String.valueOf(response));
                //  Toast.makeText(getApplicationContext(), ""+obj.getString("message"), Toast.LENGTH_SHORT).show();

                String r_code = obj.getString("status");

                if (r_code.equalsIgnoreCase("1")) {

                    try {


                    } catch (Exception e) {
                        Log.i("Transaction exception", "" + e);
                        e.printStackTrace();
                    }

                }

            } catch (Exception e) {
                Log.i("search_property Exc", "" + e);

                e.printStackTrace();

            }
        }, error -> {
            dialog.dismiss();

            Log.i("search_property Error", "" + error);

        });
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }


    public void removeAt(int position) {
        subcat.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, subcat.size());
    }

    private void deleteuser(String id) {

        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");
        dialog.show();

        String url = SERVER_URLMAIN + "delete_user";
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        Map<String, String> params = new HashMap();
        params.put("id", id);
        params.put("type", "property");
        JSONObject parameters = new JSONObject(params);
        Log.i("user_list par]", "" + parameters);

        JsonObjectRequest stringRequest = new JsonObjectRequest(POST, url, parameters, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("user_list Res", "" + response);
                dialog.dismiss();

                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    //  Toast.makeText(getApplicationContext(), ""+obj.getString("message"), Toast.LENGTH_SHORT).show();

                    String r_code = obj.getString("status");

                    if (r_code.equalsIgnoreCase("1")) {

                        try {


                        } catch (Exception e) {
                            Log.i("Transaction exception", "" + e);
                            e.printStackTrace();
                        }

                    }

                } catch (Exception e) {
                    Log.i("search_property Exc", "" + e);

                    e.printStackTrace();

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

                Log.i("search_property Error", "" + error);

            }
        });
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);


    }


    @Override
    public int getItemCount() {
        return subcat.size();
    }

    public static class PropertyViewHolder extends RecyclerView.ViewHolder {
        ImageView visitingcard, fav, menu, share, shareWhatsUp;
        LinearLayout edit_layout;
        ImageView delete, edit;
        LinearLayout rootLayout;
        ImageView showImages;
        AppCompatTextView property_id, date, heading, uplodede_name, description, price, seller_detail, note, tvAskingPrice;

        public PropertyViewHolder(View view) {
            super(view);
            property_id = view.findViewById(R.id.property_id);
            rootLayout = view.findViewById(R.id.rootLayout);
            tvAskingPrice = view.findViewById(R.id.tvAskingPrice);
            share = view.findViewById(R.id.share);
            shareWhatsUp = view.findViewById(R.id.shareWhatsUp);
            heading = view.findViewById(R.id.heading);
            delete = view.findViewById(R.id.delete);
            description = view.findViewById(R.id.description);
            price = view.findViewById(R.id.price);
            seller_detail = view.findViewById(R.id.seller_detail);
            fav = view.findViewById(R.id.fav);
            note = view.findViewById(R.id.note);
            edit_layout = view.findViewById(R.id.edit_layout);
            edit = view.findViewById(R.id.edit);
            date = view.findViewById(R.id.date);
            visitingcard = view.findViewById(R.id.visitingcard);
            uplodede_name = view.findViewById(R.id.uplodede_name);
            menu = view.findViewById(R.id.menu);
            showImages = view.findViewById(R.id.show_image);
            //  imgTypeOfSale = view.findViewById(R.id.imgTypeOfSale);


        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image, fav;
        TextView title, disctrict, name, mobile, password, edit, tvUserName, tvAdsType;
        ImageView /*delete,*/ profileImage,ivPlay;
        LinearLayout header_card;
        private VideoView videoView;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            image = view.findViewById(R.id.image);
            header_card = view.findViewById(R.id.header_card);
            videoView = view.findViewById(R.id.idVideoView);
            tvAdsType = view.findViewById(R.id.tvAdsType);
            tvUserName = view.findViewById(R.id.tvUserName);
            ivPlay = view.findViewById(R.id.ivPlay);
            profileImage = view.findViewById(R.id.profileImage);
        }
    }

    public static class FirstItemViewHolder extends RecyclerView.ViewHolder {
        TextView name, firm_name, location;
        ImageView add_property, search_prop;
        MaterialButton btn_postRequirement;

        public FirstItemViewHolder(View rootView) {
            super(rootView);
            add_property = rootView.findViewById(R.id.add_property);
            btn_postRequirement = rootView.findViewById(R.id.btnPostRequirement);
            search_prop = rootView.findViewById(R.id.search_prop);
            firm_name = rootView.findViewById(R.id.firm_name);
            location = rootView.findViewById(R.id.location);
            name = rootView.findViewById(R.id.name);
        }
    }

    private void makefav(String prop_id, int position) {


        dialog = new ProgressDialog(context);
        dialog.setMessage("Fetching your details.");
        dialog.show();

        String url = SERVER_URLMAIN + "make_favourite_unfavourite";
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        YourPreference yourPrefrence = YourPreference.getInstance(context);
        String user_id = yourPrefrence.getData("user_id");


        Map<String, String> params = new HashMap();
        params.put("user_id", user_id);
        params.put("property_id", prop_id);
        JSONObject parameters = new JSONObject(params);

        Log.i("user_signup Pararmenter", "" + parameters);

        JsonObjectRequest stringRequest = new JsonObjectRequest(POST, url, parameters, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                Log.i("user_signup Response", "" + response);

                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
//                    Toast.makeText(context, "" + obj.getString("fav"), Toast.LENGTH_SHORT).show();

                    String r_code = obj.getString("status");

                    if (r_code.equalsIgnoreCase("1")) {
                        if (obj.getString("fav").equalsIgnoreCase("1")) {
                            Toast.makeText(context, "Favorite Add Successfully", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(context, "Favorite Removed Successfully", Toast.LENGTH_SHORT).show();
                        subcat.get(position).setFavourite(obj.getString("fav"));
                        notifyItemChanged(position);
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
        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, 1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );

        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);


    }

    private Bitmap generateBitmap(LinearLayout view) {
        //Provide it with a layout params. It should necessarily be wrapping the
        //content as we not really going to have a parent for it.
//        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT));
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        );
//        params.setMargins(70, 70, 70, 70);
//        view.setLayoutParams(params);

        //Pre-measure the view so that height and width don't remain null.
//        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//

        //Assign a size and position to the view and all of its descendants
//        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        //Create the bitmap
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(),
                Bitmap.Config.ARGB_8888);
        //Create a canvas with the specified bitmap to draw into
        Canvas c = new Canvas(bitmap);

        //Render this view (and all of its children) to the given Canvas
        view.draw(c);
        return bitmap;
    }


    public void onClickApp(String pack, Bitmap bitmap) {
        PackageManager pm = context.getPackageManager();
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            //  String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "Title", null);

            String path = MediaStore.Images.Media.insertImage(
                    context.getContentResolver(), bitmap, "IMG_" + System.currentTimeMillis(), null
            );
            Uri imageUri = Uri.parse(path);

//            @SuppressWarnings("unused")
//            PackageInfo info = pm.getPackageInfo(pack, PackageManager.GET_META_DATA);

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("image/*");
            waIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            waIntent.putExtra(Intent.EXTRA_TEXT, "");
            context.startActivity(Intent.createChooser(waIntent, "Share with"));
        } catch (Exception e) {
            Log.e("Error on sharing", e + " ");
            Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public int getItemViewType(int position) {
        return subcat.get(position).getViewType();
    }

    public interface OnCTAButtonClick {
        void onCTAClick(String cta_type, String cta_link);
    }
}