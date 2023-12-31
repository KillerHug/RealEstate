package in.orangeapp.realestate;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import in.orangeapp.adapter.FullGalleryAdapter;
import in.orangeapp.fragment.GalleryFragment;
import in.orangeapp.util.ItemOffsetDecoration;
import in.orangeapp.util.JsonUtils;
import in.orangeapp.realestate.R;
import com.squareup.picasso.Picasso;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class FullGalleryActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    FullGalleryAdapter adapter;
    ImageView imageView;
    int position;
    TextView txtClose;
    JsonUtils jsonUtils;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_gallery);
        recyclerView = findViewById(R.id.rv_gallery);
        imageView = findViewById(R.id.image);
        txtClose = findViewById(R.id.txtClose);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setTitle(getString(R.string.property_gallery));

        jsonUtils = new JsonUtils(this);
        jsonUtils.forceRTLIfSupported(getWindow());

        Intent intent = getIntent();
        position = intent.getIntExtra("Position", 0);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(FullGalleryActivity.this, LinearLayoutManager.HORIZONTAL, false));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(FullGalleryActivity.this, R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);

        Picasso.get().load(GalleryFragment.mList.get(position)).into(imageView);

        adapter = new FullGalleryAdapter(FullGalleryActivity.this, GalleryFragment.mList);
        recyclerView.setAdapter(adapter);

        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(FullGalleryActivity.this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Picasso.get().load(GalleryFragment.mList.get(position)).into(imageView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        private RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }
}
