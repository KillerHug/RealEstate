package in.orangeapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import in.orangeapp.item.ItemType;
import in.orangeapp.realestate.R;

import java.util.ArrayList;

/**
 * Created by laxmi.
 */
public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ItemRowHolder> {

    public ArrayList<ItemType> dataList;
    private Context mContext;

    public FilterAdapter(Context context, ArrayList<ItemType> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_filter_item, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemType singleItem = dataList.get(position);

        holder.radioButtonType.setText(singleItem.getTypeName());
        holder.radioButtonType.setTag(position);
        //holder.radioButtonType.setTag(R.id.filter_type, singleItem);
        holder.radioButtonType.setChecked(singleItem.isSelected());
        holder.radioButtonType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer pos = (Integer) holder.radioButtonType.getTag();
                if (dataList.get(pos).isSelected()) {
                    dataList.get(pos).setSelected(false);
                } else {
                    dataList.get(pos).setSelected(true);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        private TextView text;
        private RelativeLayout relativeLayout;
        CheckBox radioButtonType;

        private ItemRowHolder(View itemView) {
            super(itemView);

            relativeLayout = itemView.findViewById(R.id.rootLayout);
            radioButtonType = itemView.findViewById(R.id.filter_type);

        }
    }
}
