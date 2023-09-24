package in.orangeapp.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import in.orangeapp.realestate.R;

import java.util.List;

public class ShortImageAdapter extends RecyclerView.Adapter<ShortImageAdapter.MyViewHolder> {

    List<String> imageList;
    Context context;

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView remove_image;
        ProgressBar progressBar;

        MyViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
            remove_image = view.findViewById(R.id.remove_image);
            progressBar = view.findViewById(R.id.ivProgressBar);
        }
    }

    public ShortImageAdapter(List<String> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_add_property_image, parent, false);

        return new MyViewHolder(itemView);
    }

    public void remove(int index) {
        imageList.remove(index);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String image = imageList.get(position);

        /*Glide.with(context).load(image)
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);*/
        holder.progressBar.setVisibility(View.VISIBLE);
        Glide.with(context)
                .load(image)
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).into(holder.imageView);

        holder.remove_image.setOnClickListener((view) -> {
            remove(position);
        });

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
}
