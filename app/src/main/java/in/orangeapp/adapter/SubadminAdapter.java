package in.orangeapp.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import in.orangeapp.item.SubadminModel;
import in.orangeapp.realestate.SignUpActivity;
import in.orangeapp.realestate.Childadmin;
import in.orangeapp.realestate.R;
import in.orangeapp.realestate.SignInActivity;
import in.orangeapp.util.YourPreference;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.POST;
import static in.orangeapp.util.Constant.SERVER_URLMAIN;

public class SubadminAdapter extends RecyclerView.Adapter<SubadminAdapter.MyViewHolder> {
    Context context;
    List<SubadminModel> subcat;
    ProgressDialog dialog;
    String from;

    public SubadminAdapter(List<SubadminModel> subcat, Context context,String from) {
        this.subcat = subcat;
        this.context = context;
        this.from = from;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subadmin_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.state.setText(subcat.get(position).getState());
        holder.disctrict.setText(subcat.get(position).getDistrict());
        holder.name.setText(subcat.get(position).getName());
        holder.email.setText(subcat.get(position).getEmail());
        holder.mobile.setText(subcat.get(position).getMobile());
        holder.total_property.setText("Total Listing : "+subcat.get(position).getTotal_property());
        holder.password.setText("Passowrd : "+subcat.get(position).getPassword());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Are you sure?");
                builder.setMessage("All Data Associated with this Team Member will be lost permanently and cannot be retrived.");

                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog

                        dialog.dismiss();

                    }
                });

                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteuser(subcat.get(position).getUser_id(),subcat.get(position).getParent_id());

                        removeAt(position);
                        dialog.dismiss();
                        // Do nothing
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, SignUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("from","edit");
                intent.putExtra("mobile",subcat.get(position).getMobile());
                intent.putExtra("email",subcat.get(position).getEmail());
                intent.putExtra("password",subcat.get(position).getPassword());
                intent.putExtra("type",subcat.get(position).getType());
                intent.putExtra("state",subcat.get(position).getState());
                intent.putExtra("district",subcat.get(position).getDistrict());
                intent.putExtra("name",subcat.get(position).getName());
                intent.putExtra("company",subcat.get(position).getCompany_firm());
                intent.putExtra("user_id",subcat.get(position).getUser_id());
                intent.putExtra("blocked",subcat.get(position).getBlocked());
                intent.putExtra("parent",subcat.get(position).getParent_id());
                context.startActivity(intent);

            }
        });
        YourPreference yourPrefrence = YourPreference.getInstance(context);
        String user_id= yourPrefrence.getData("user_id");

        if (user_id.equalsIgnoreCase("39")){


            if (from.equalsIgnoreCase("subadmin")) {
                holder.main_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, Childadmin.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("parent_id", subcat.get(position).getUser_id());
                        intent.putExtra("name", subcat.get(position).getName());
                        context.startActivity(intent);

                    }
                });
            }
        }

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

    public class MyViewHolder extends RecyclerView.ViewHolder {
         ImageView imgTypeOfSale,fav;
         LinearLayout main_layout;
         TextView state,disctrict,name,mobile,password,edit,delete,total_property,email;
         public MyViewHolder(View view) {
            super(view);
             state=view.findViewById(R.id.state);
             disctrict=view.findViewById(R.id.disctrict);
             name=view.findViewById(R.id.name);
             mobile=view.findViewById(R.id.mobile);
             password=view.findViewById(R.id.password);
             edit=view.findViewById(R.id.edit);
             delete=view.findViewById(R.id.delete);
             main_layout=view.findViewById(R.id.main_layout);
             total_property=view.findViewById(R.id.total_property);
             email=view.findViewById(R.id.email);


        }
    }

    private void deleteuser(String id,String parent_id ) {

        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");
        dialog.show();

        String url = SERVER_URLMAIN + "delete_user";
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        Map<String, String> params = new HashMap();
        params.put("id",id);
        params.put("type","user");
        params.put("parent_id",parent_id);
        JSONObject parameters = new JSONObject(params);
        Log.i("user_list par]",""+parameters);

        JsonObjectRequest stringRequest = new JsonObjectRequest(POST, url, parameters, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("user_list Res",""+response);
                dialog.dismiss();

                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    //  Toast.makeText(getApplicationContext(), ""+obj.getString("message"), Toast.LENGTH_SHORT).show();

                    String r_code = obj.getString("status");

                    if (r_code.equalsIgnoreCase("1")) {

                        YourPreference yourPrefrence = YourPreference.getInstance(context);
                        String user_id_main= yourPrefrence.getData("user_id");

                        if (id.equalsIgnoreCase(user_id_main))
                        {
                             yourPrefrence.saveData("signup", "");

                            Intent intent_login = new Intent(context, SignInActivity.class);
                            intent_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent_login);
                            ((Activity)context).finish();
                        }
                    }

                } catch (Exception e) {
                    Log.i("search_property Exc",""+e);

                    e.printStackTrace();

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

                Log.i("search_property Error",""+error);

            }
        });
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);



    }




}


