package in.orangeapp.adapter;

import static com.android.volley.Request.Method.POST;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.orangeapp.item.AdvertModel;
import in.orangeapp.item.StateModel;
import in.orangeapp.realestate.R;
import in.orangeapp.util.Constant;
import in.orangeapp.util.YourPreference;

public class MultipleStateAdapter extends RecyclerView.Adapter<MultipleStateAdapter.MyViewHolder> {
    Context context;
    List<StateModel> stateList;
    ProgressDialog dialog;

    public MultipleStateAdapter(ArrayList<StateModel> statelist, Context context) {
        this.stateList = statelist;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_state_name, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.tvName.setText(stateList.get(position).getState_title());
        /*holder.llParent.setOnClickListener(v -> {
            stateList.get(position).setSelected(!stateList.get(position).isSelected());
            notifyItemChanged(position);
        });*/
        holder.cbSelect.setChecked(stateList.get(position).isSelected());
        holder.cbSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            stateList.get(position).setSelected(isChecked);
        });
    }

    public List<StateModel> getSelectedItems() {
        List<StateModel> selectedItems = new ArrayList<>();
        for (StateModel item : stateList) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
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
        return stateList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        CheckBox cbSelect;
        LinearLayout llParent;

        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            cbSelect = view.findViewById(R.id.cbSelect);
            llParent = view.findViewById(R.id.llParent);
           }
    }
}


