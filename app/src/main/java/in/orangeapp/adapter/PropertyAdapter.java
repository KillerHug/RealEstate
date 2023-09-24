package in.orangeapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.orangeapp.item.ItemProperty;
import in.orangeapp.util.FavClickListener;
import in.orangeapp.util.FavUnFav;
import in.orangeapp.util.JsonUtils;
import in.orangeapp.realestate.MyApplication;
import in.orangeapp.realestate.R;
import in.orangeapp.realestate.SignInActivity;
import in.orangeapp.util.Constant;
import in.orangeapp.util.PopUpAds;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.google.ads.mediation.admob.AdMobAdapter;
import in.google.android.ads.nativetemplates.NativeTemplateStyle;
import in.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.ixidev.gdpr.GDPRChecker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class PropertyAdapter extends RecyclerView.Adapter {

    private Activity activity;
    private ArrayList<ItemProperty> dataList;
    private final int VIEW_ITEM_RIGHT = 1;
    private final int VIEW_ITEM_LEFT = 0;
    private final int VIEW_TYPE_Ad = 2;

    public PropertyAdapter(Activity activity, ArrayList<ItemProperty> dataList) {
        this.activity = activity;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM_RIGHT) {
            View view = LayoutInflater.from(activity).inflate(R.layout.row_estate_item, parent, false);
            return new ViewHolder(view);
        } else if (viewType == VIEW_ITEM_LEFT) {
            View v = LayoutInflater.from(activity).inflate(R.layout.row_estate_item_left, parent, false);
            return new SecondViewHolder(v);
        } else if (viewType == VIEW_TYPE_Ad) {
            View view = LayoutInflater.from(activity).inflate(R.layout.admob_adapter, parent, false);
            return new AdOption(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        if (holder.getItemViewType() == VIEW_ITEM_RIGHT) {

            final ViewHolder viewHolder = (ViewHolder) holder;
            final ItemProperty singleItem = dataList.get(position);
            viewHolder.text.setText(singleItem.getPropertyName());
            viewHolder.textPrice.setText(activity.getString(R.string.currency_symbol) + singleItem.getPropertyPrice());
            viewHolder.textAddress.setText(singleItem.getPropertyAddress());
            Picasso.get().load(singleItem.getPropertyThumbnailB()).placeholder(R.drawable.header_top_logo).into(viewHolder.image);

            if (singleItem.isFavourite()) {
                viewHolder.ic_home_fav.setImageResource(R.drawable.ic_fav_hover);
            } else {
                viewHolder.ic_home_fav.setImageResource(R.drawable.ic_fav);
            }

            viewHolder.ic_home_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (MyApplication.getInstance().getIsLogin()) {
                        if (JsonUtils.isNetworkAvailable(activity)) {
                            FavClickListener saveClickListener = new FavClickListener() {
                                @Override
                                public void onItemClick(boolean isSave, String message) {
                                    if (isSave) {
                                        viewHolder.ic_home_fav.setImageResource(R.drawable.ic_fav_hover);
                                    } else {
                                        viewHolder.ic_home_fav.setImageResource(R.drawable.ic_fav);
                                    }
                                }
                            };
                            new FavUnFav(activity).userFav(singleItem.getPId(), saveClickListener);
                        } else {
                            Toast.makeText(activity, activity.getString(R.string.network_msg), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(activity, activity.getString(R.string.login_msg), Toast.LENGTH_SHORT).show();
                        Intent intentLogin = new Intent(activity, SignInActivity.class);
                        intentLogin.putExtra("isfromdetail", true);
                        activity.startActivity(intentLogin);
                    }
                }
            });

            viewHolder.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopUpAds.ShowInterstitialAds(activity, singleItem.getPId());
                }
            });

            viewHolder.txtPurpose.setText(singleItem.getPropertyPurpose());
            if (activity.getResources().getString(R.string.isRTL).equals("true")) {
                viewHolder.txtPurpose.setBackgroundResource(singleItem.getPropertyPurpose().equals("Rent") ? R.drawable.rent_right_button : R.drawable.sale_right_button);
            } else {
                viewHolder.txtPurpose.setBackgroundResource(singleItem.getPropertyPurpose().equals("Rent") ? R.drawable.rent_left_button : R.drawable.sale_left_button);
            }


        } else if (holder.getItemViewType() == VIEW_ITEM_LEFT) {

            final SecondViewHolder holder2 = (SecondViewHolder) holder;
            final ItemProperty singleItem = dataList.get(position);
            holder2.text.setText(singleItem.getPropertyName());
            holder2.textPrice.setText(activity.getString(R.string.currency_symbol) + singleItem.getPropertyPrice());
            holder2.textAddress.setText(singleItem.getPropertyAddress());
            Picasso.get().load(singleItem.getPropertyThumbnailB()).placeholder(R.drawable.header_top_logo).into(holder2.image);

            if (singleItem.isFavourite()) {
                holder2.ic_home_fav.setImageResource(R.drawable.ic_fav_hover);
            } else {
                holder2.ic_home_fav.setImageResource(R.drawable.ic_fav);
            }

            holder2.ic_home_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (MyApplication.getInstance().getIsLogin()) {
                        if (JsonUtils.isNetworkAvailable(activity)) {
                            FavClickListener saveClickListener = new FavClickListener() {
                                @Override
                                public void onItemClick(boolean isSave, String message) {
                                    if (isSave) {
                                        holder2.ic_home_fav.setImageResource(R.drawable.ic_fav_hover);
                                    } else {
                                        holder2.ic_home_fav.setImageResource(R.drawable.ic_fav);
                                    }
                                }
                            };
                            new FavUnFav(activity).userFav(singleItem.getPId(), saveClickListener);
                        } else {
                            Toast.makeText(activity, activity.getString(R.string.network_msg), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(activity, activity.getString(R.string.login_msg), Toast.LENGTH_SHORT).show();
                        Intent intentLogin = new Intent(activity, SignInActivity.class);
                        intentLogin.putExtra("isfromdetail", true);
                        activity.startActivity(intentLogin);
                    }
                }
            });

            holder2.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopUpAds.ShowInterstitialAds(activity, singleItem.getPId());
                }
            });

            holder2.txtPurpose.setText(singleItem.getPropertyPurpose());
            if (activity.getResources().getString(R.string.isRTL).equals("true")) {
                holder2.txtPurpose.setBackgroundResource(singleItem.getPropertyPurpose().equals("Rent") ? R.drawable.rent_left_button : R.drawable.sale_left_button);
            } else {
                holder2.txtPurpose.setBackgroundResource(singleItem.getPropertyPurpose().equals("Rent") ? R.drawable.rent_right_button : R.drawable.sale_right_button);
            }

        } else if (holder.getItemViewType() == VIEW_TYPE_Ad) {

            final AdOption adOption = (AdOption) holder;
            if (Constant.SAVE_ADS_NATIVE_ON_OFF.equals("true")) {
                if (Constant.SAVE_NATIVE_TYPE.equals("admob")) {

                    if (adOption.linearLayout.getChildCount() == 0) {

                        View view = activity.getLayoutInflater().inflate(R.layout.admob_ad, null, true);

                        final TemplateView templateView = view.findViewById(R.id.my_template);
                        if (templateView.getParent() != null) {
                            ((ViewGroup) templateView.getParent()).removeView(templateView);
                        }
                        adOption.linearLayout.addView(templateView);
                        AdLoader adLoader = new AdLoader.Builder(activity, Constant.SAVE_NATIVE_ID)
                                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                                    @Override
                                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                                        NativeTemplateStyle styles = new
                                                NativeTemplateStyle.Builder()
                                                .build();

                                        templateView.setStyles(styles);
                                        templateView.setNativeAd(unifiedNativeAd);

                                    }
                                })
                                .build();

                        GDPRChecker.Request request = GDPRChecker.getRequest();
                        AdRequest.Builder builder = new AdRequest.Builder();
                        if (request == GDPRChecker.Request.NON_PERSONALIZED) {
                            Bundle extras = new Bundle();
                            extras.putString("npa", "1");
                            builder.addNetworkExtrasBundle(AdMobAdapter.class, extras);
                        }
                        adLoader.loadAd(builder.build());
                    }

                } else {
                    if (adOption.linearLayout.getChildCount() == 0) {

                        LayoutInflater inflater = LayoutInflater.from(activity);
                        LinearLayout adView = (LinearLayout) inflater.inflate(R.layout.native_ad_layout, adOption.linearLayout, false);

                        adOption.linearLayout.addView(adView);

                        // Add the AdOptionsView
                        final LinearLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);

                        // Create native UI using the ad metadata.
                        final TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
                        final MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
                        final TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
                        final TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
                        final TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
                        final Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

                        final NativeAd nativeAd = new NativeAd(activity, Constant.SAVE_NATIVE_ID);
                        //AdSettings.addTestDevice("1035dc69-0d11-45c5-bfaf-8f7f7e76e42a");
                        NativeAdListener nativeAdListener = new NativeAdListener() {
                            @Override
                            public void onMediaDownloaded(Ad ad) {
                                Log.d("status_data", "MediaDownloaded" + " " + ad.toString());
                            }

                            @Override
                            public void onError(Ad ad, AdError adError) {
                                Toast.makeText(activity, adError.toString(), Toast.LENGTH_SHORT).show();
                                Log.d("status_data", "error" + " " + adError.toString());
                            }

                            @Override
                            public void onAdLoaded(Ad ad) {
                                // Race condition, load() called again before last ad was displayed
                                if (nativeAd == null || nativeAd != ad) {
                                    return;
                                }
                                // Inflate Native Ad into Container
                                Log.d("status_data", "on load" + " " + ad.toString());

                                NativeAdLayout nativeAdLayout = new NativeAdLayout(activity);
                                AdOptionsView adOptionsView = new AdOptionsView(activity, nativeAd, nativeAdLayout);
                                adChoicesContainer.removeAllViews();
                                adChoicesContainer.addView(adOptionsView, 0);

                                // Set the Text.
                                nativeAdTitle.setText(nativeAd.getAdvertiserName());
                                nativeAdBody.setText(nativeAd.getAdBodyText());
                                nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
                                nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
                                nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
                                sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

                                // Create a list of clickable views
                                List<View> clickableViews = new ArrayList<>();
                                clickableViews.add(nativeAdTitle);
                                clickableViews.add(nativeAdCallToAction);

                                // Register the Title and CTA button to listen for clicks.
                                nativeAd.registerViewForInteraction(
                                        adOption.linearLayout,
                                        nativeAdMedia,
                                        clickableViews);

                            }

                            @Override
                            public void onAdClicked(Ad ad) {
                                Log.d("status_data", "AdClicked" + " " + ad.toString());
                            }

                            @Override
                            public void onLoggingImpression(Ad ad) {
                                Log.d("status_data", "Impression" + " " + ad.toString());
                            }
                        };
                        // Request an ad
                        nativeAd.loadAd(nativeAd.buildLoadAdConfig().withAdListener(nativeAdListener).build());
                    }
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (dataList.get(position) != null) {
            if (dataList.get(position).isRight()) {
                return VIEW_ITEM_RIGHT;
            } else {
                return VIEW_ITEM_LEFT;
            }
        } else {
            return VIEW_TYPE_Ad;
        }
    }

    public static class SecondViewHolder extends RecyclerView.ViewHolder {
        public ImageView image, ic_home_fav;
        private TextView text, textPrice, textAddress,  txtPurpose;
        private RelativeLayout lyt_parent;

        public SecondViewHolder(View v) {
            super(v);
            image = itemView.findViewById(R.id.image);
            text = itemView.findViewById(R.id.text);
            textPrice = itemView.findViewById(R.id.textPrice);
            textAddress = itemView.findViewById(R.id.textAddress);
            txtPurpose = itemView.findViewById(R.id.textPurpose);
            lyt_parent = itemView.findViewById(R.id.rootLayout);
            ic_home_fav = itemView.findViewById(R.id.ic_home_fav);

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image, ic_home_fav;
        private TextView text, textPrice, textAddress,txtPurpose;
        private RelativeLayout lyt_parent;

        public ViewHolder(View itemView) {
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


    public class AdOption extends RecyclerView.ViewHolder {

        private LinearLayout linearLayout;

        public AdOption(View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.adView_admob);

        }

    }
}
