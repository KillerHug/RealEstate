package in.orangeapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import in.orangeapp.item.ItemProperty;
import in.orangeapp.util.FavClickListener;
import in.orangeapp.util.FavUnFav;
import in.orangeapp.util.JsonUtils;
import in.orangeapp.realestate.MyApplication;
import in.orangeapp.realestate.R;
import in.orangeapp.realestate.SignInActivity;
import in.orangeapp.util.PopUpAds;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ItemRowHolder> {

    private ArrayList<ItemProperty> dataList;
    private Activity mContext;

    public HomeAdapter(Activity context, ArrayList<ItemProperty> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_item, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemProperty singleItem = dataList.get(position);
        holder.text.setText(singleItem.getPropertyName());
        holder.textPrice.setText(mContext.getString(R.string.currency_symbol) + singleItem.getPropertyPrice());
        holder.textAddress.setText(singleItem.getPropertyAddress());
         Picasso.get().load(singleItem.getPropertyThumbnailB()).placeholder(R.drawable.header_top_logo).into(holder.image);

        if (singleItem.isFavourite()) {
            holder.ic_home_fav.setImageResource(R.drawable.ic_fav_hover);
        } else {
            holder.ic_home_fav.setImageResource(R.drawable.ic_fav);
        }

        holder.ic_home_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApplication.getInstance().getIsLogin()) {
                    if (JsonUtils.isNetworkAvailable(mContext)) {
                        FavClickListener saveClickListener = new FavClickListener() {
                            @Override
                            public void onItemClick(boolean isSave, String message) {
                                if (isSave) {
                                    holder.ic_home_fav.setImageResource(R.drawable.ic_fav_hover);
                                } else {
                                    holder.ic_home_fav.setImageResource(R.drawable.ic_fav);
                                }
                            }
                        };
                        new FavUnFav(mContext).userFav(singleItem.getPId(), saveClickListener);
                    } else {
                        Toast.makeText(mContext, mContext.getString(R.string.network_msg), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.login_msg), Toast.LENGTH_SHORT).show();
                    Intent intentLogin = new Intent(mContext, SignInActivity.class);
                    intentLogin.putExtra("isfromdetail", true);
                    mContext.startActivity(intentLogin);
                }
            }
        });

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopUpAds.ShowInterstitialAds(mContext, singleItem.getPId());
            }
        });

        holder.txtPurpose.setText(singleItem.getPropertyPurpose());
        if (mContext.getResources().getString(R.string.isRTL).equals("true")) {
            holder.txtPurpose.setBackgroundResource(singleItem.getPropertyPurpose().equals("Rent") ? R.drawable.rent_right_button : R.drawable.sale_right_button);
        } else {
            holder.txtPurpose.setBackgroundResource(singleItem.getPropertyPurpose().equals("Rent") ? R.drawable.rent_left_button : R.drawable.sale_left_button);
        }
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public ImageView image, ic_home_fav;
        private TextView text, textPrice, textAddress, txtPurpose;
        private LinearLayout lyt_parent;

        private ItemRowHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            text = itemView.findViewById(R.id.text);
            textPrice = itemView.findViewById(R.id.textPrice);
            textAddress = itemView.findViewById(R.id.textAddress);
            txtPurpose = itemView.findViewById(R.id.textPurpose);
             lyt_parent = itemView.findViewById(R.id.rootLayout);
            ic_home_fav = itemView.findViewById(R.id.ic_home_fav);
        }
    }
}
