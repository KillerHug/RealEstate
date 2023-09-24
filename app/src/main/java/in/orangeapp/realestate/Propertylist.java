package in.orangeapp.realestate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import in.orangeapp.adapter.Property_listAdapter;
import in.orangeapp.item.Property_model;
import in.orangeapp.realestate.R;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.List;

public class Propertylist extends AppCompatActivity {

    RecyclerView properties_reycle;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propertylist);
        properties_reycle = findViewById(R.id.properties_reycle);
        String response = getIntent().getExtras().getString("response");
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Property List");

        try {
            JSONObject obj = new JSONObject(response);

            List<Property_model> trasactionlist = new GsonBuilder().create().fromJson(obj.getJSONArray("data").toString(), new TypeToken<List<Property_model>>() {
            }.getType());

            Property_listAdapter newsAdapter = new Property_listAdapter(trasactionlist, Propertylist.this, "0", "", "", 2);
            properties_reycle.setLayoutManager(new LinearLayoutManager(Propertylist.this, LinearLayoutManager.VERTICAL, false));
            properties_reycle.setAdapter(newsAdapter);
            newsAdapter.notifyDataSetChanged();


        } catch (Exception e) {
            Log.i("Transaction exception", "" + e);
            e.printStackTrace();
        }
    }


}