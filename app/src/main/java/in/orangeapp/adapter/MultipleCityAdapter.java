package in.orangeapp.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.orangeapp.item.CityModel;
import in.orangeapp.item.StateModel;
import in.orangeapp.realestate.R;

public class MultipleCityAdapter extends RecyclerView.Adapter<MultipleCityAdapter.MyViewHolder> {
    Context context;
    List<CityModel> cityList;
    ProgressDialog dialog;

    public MultipleCityAdapter(ArrayList<CityModel> statelist, Context context) {
        this.cityList = statelist;
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
        holder.tvName.setText(cityList.get(position).getDistrict_title());
        holder.cbSelect.setChecked(cityList.get(position).isSelected());
        holder.cbSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cityList.get(position).setSelected(isChecked);
        });
    }

    public List<CityModel> getSelectedItems() {
        List<CityModel> selectedItems = new ArrayList<>();
        for (CityModel item : cityList) {
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
        return cityList.size();
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


