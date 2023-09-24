package in.orangeapp.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import in.orangeapp.item.AdvertModel;
import in.orangeapp.util.Constant;
import in.orangeapp.util.YourPreference;
import in.orangeapp.realestate.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class AdvertAdapter extends RecyclerView.Adapter<AdvertAdapter.MyViewHolder> {
    Context context;
    List<AdvertModel> subcat;
    ProgressDialog dialog;

    public AdvertAdapter(List<AdvertModel> subcat, Context context) {
        this.subcat = subcat;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.advert_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.title.setText(subcat.get(position).getTitle());

        Glide.with(context)
                .load(Constant.IMAGE_PATH + subcat.get(position).getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image);
        YourPreference yourPrefrence = YourPreference.getInstance(context);
        String user_id = yourPrefrence.getData("user_id");
        if (user_id.equalsIgnoreCase("39")) {
            holder.delete.setVisibility(View.VISIBLE);
        }

        if (subcat.get(position).getTitle().equalsIgnoreCase("")) {
            holder.header_card.setVisibility(View.GONE);
        }


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Are you sure?");
                builder.setMessage("This Advertisement will be lost permanently and cannot be retrived.");

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
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public void removeAt(int position) {
        subcat.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, subcat.size());
    }

    @Override
    public int getItemCount() {
        return subcat.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image, fav;
        TextView title, disctrict, name, mobile, password, edit;
        ImageView delete;
        LinearLayout header_card;
        RelativeLayout delete_layout;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            image = view.findViewById(R.id.image);
            delete = view.findViewById(R.id.delete);
            header_card = view.findViewById(R.id.header_card);
            delete_layout = view.findViewById(R.id.delete_layout);


        }
    }

    private void deleteuser(String id) {

        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");
        dialog.show();

        String url = Constant.SERVER_URLMAIN + "delete_advert";
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        Map<String, String> params = new HashMap();
        params.put("id", id);
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


}


