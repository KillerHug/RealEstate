package in.orangeapp.realestate;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import in.orangeapp.adapter.FullImageAdapter;
import in.orangeapp.util.Constant;
import in.orangeapp.util.LinePagerIndicatorDecoration;
import in.orangeapp.realestate.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActivityShowProperties extends AppCompatActivity {

    private final List<String> imageList = new ArrayList<>();
    FullImageAdapter mAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_properties);
        Intent intent = getIntent();
        imageList.addAll(Constant.images);
        int pos = intent.getIntExtra("pos", 0);
        //imageList.addAll((Collection<? extends String>) getIntent().getSerializableExtra("data"));
        recyclerView = findViewById(R.id.recycler_view);
        //recyclerView.setBackgroundColor(Color.parseColor("#000000"));
        mAdapter = new FullImageAdapter(imageList, this);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        // add pager behavior
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new LinePagerIndicatorDecoration());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        Objects.requireNonNull(recyclerView.getLayoutManager()).scrollToPosition(pos);

        //imageList.clear();

        //imageList.addAll(imageList);

        mAdapter.notifyDataSetChanged();
    }
}
