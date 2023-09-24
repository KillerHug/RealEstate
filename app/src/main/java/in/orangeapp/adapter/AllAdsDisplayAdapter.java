package in.orangeapp.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.orangeapp.item.AdsDataModel;
import in.orangeapp.realestate.CreateAdvertisementActivity;
import in.orangeapp.realestate.R;

public class AllAdsDisplayAdapter extends RecyclerView.Adapter<AllAdsDisplayAdapter.MyViewHolder> {
    Context context;
    List<AdsDataModel> adsDataModels;
    ProgressDialog dialog;
    AllAdsListener listener;

    public AllAdsDisplayAdapter(List<AdsDataModel> statelist, Context context) {
        this.adsDataModels = statelist;
        this.context = context;
        listener = (AllAdsListener) context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ads_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        AdsDataModel model = adsDataModels.get(position);
        if (model.getAdvType().equals("Editorial")) {
            holder.labelGeoLoc.setVisibility(View.GONE);
            holder.tvGeoLoc.setVisibility(View.GONE);
        } else {
            holder.labelGeoLoc.setVisibility(View.VISIBLE);
            holder.tvGeoLoc.setVisibility(View.VISIBLE);
        }
        Glide.with(context)
                .load(model.getFilepath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_home_avatar_photoicon)
                .into(holder.ivAdsImage);
        holder.adsType.setText(model.getAdvType());
        holder.tvUserNumber.setText(model.getUserAccount());

        String txtSelected = "";
        if (model.getGeoName().size() == 0) {
            txtSelected = "";
        } else if (model.getGeoName().size() == 1) {
            txtSelected = model.getGeoName().get(0);
        } else {
            for (int i = 0; i < model.getGeoName().size() - 1; i++) {
                txtSelected += model.getGeoName().get(i);
                if (i < model.getGeoName().size() - 2) {
                    txtSelected += ", ";
                }
            }
            txtSelected += " and " + model.getGeoName().get(model.getGeoName().size() - 1);
        }
        holder.tvGeoLoc.setText(txtSelected);

        holder.tvBalanceDays.setText(model.getNoOfDisplay());
        holder.tvDOP.setText(model.getDatetime().split(" ")[0]);
        remainingDays(holder.tvRemainingDays, model.getDatetime(), model.getNoOfDisplay());
        holder.ivAdsDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Do you want to delete advt");
            builder.setTitle("Delete !");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                listener.onDeleteAds(Integer.parseInt(model.getId()));
                dialog.dismiss();
            });
            builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                dialog.dismiss();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
        holder.ivAdsEdit.setOnClickListener(v -> {
            context.startActivity(new Intent(context, CreateAdvertisementActivity.class).putExtra("ads_data", new Gson().toJson(model)));
        });
    }

    private void remainingDays(TextView tvRemainingDays, String datetime, String noOfDisplay) {
        long remainingDays = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date originalDate = sdf.parse(datetime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(originalDate);
            calendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(noOfDisplay));
            Date newDate = calendar.getTime();
            long timeDifference = newDate.getTime() - new Date().getTime();
            remainingDays = timeDifference / (24 * 60 * 60 * 1000);
            System.out.println("Remaining Days: " + remainingDays);
        } catch (
                ParseException e) {
            e.printStackTrace();
        }
        if (remainingDays < 0) {
            tvRemainingDays.setText("Expired");
            tvRemainingDays.setTextColor(ContextCompat.getColor(tvRemainingDays.getContext(), R.color.error));
        } else if (remainingDays == 0) {
            tvRemainingDays.setText("Today Expire");
            tvRemainingDays.setTextColor(ContextCompat.getColor(tvRemainingDays.getContext(), R.color.active));
        } else if (remainingDays == 1) {
            tvRemainingDays.setText(remainingDays + " Day");
            tvRemainingDays.setTextColor(ContextCompat.getColor(tvRemainingDays.getContext(), R.color.active));
        } else {
            tvRemainingDays.setText(remainingDays + " Days");
            tvRemainingDays.setTextColor(ContextCompat.getColor(tvRemainingDays.getContext(), R.color.active));
        }
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return adsDataModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAdsImage, ivAdsDelete, ivAdsEdit;
        TextView adsType, tvUserNumber, tvBalanceDays, tvGeoLoc, tvDOP, tvRemainingDays, labelGeoLoc;

        public MyViewHolder(View view) {
            super(view);
            ivAdsImage = view.findViewById(R.id.ivAdsImage);
            adsType = view.findViewById(R.id.tvAdsType);
            tvUserNumber = view.findViewById(R.id.tvUserNumber);
            ivAdsDelete = view.findViewById(R.id.ivAdsDelete);
            ivAdsEdit = view.findViewById(R.id.ivAdsEdit);
            tvBalanceDays = view.findViewById(R.id.tvBalanceDays);
            tvGeoLoc = view.findViewById(R.id.tvGeoLocation);
            tvDOP = view.findViewById(R.id.tvDOP);
            labelGeoLoc = view.findViewById(R.id.labelGeoLoc);
            tvRemainingDays = view.findViewById(R.id.tvRemainingDays);
        }
    }

    public interface AllAdsListener {
        void onDeleteAds(int id);
    }
}


