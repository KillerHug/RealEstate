package in.orangeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import in.orangeapp.realestate.Morgagecalculator;
import in.orangeapp.realestate.Areacalculaotr;
import in.orangeapp.realestate.R;
import in.orangeapp.realestate.ROIcalculator;

public class All_Calculator extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_calculator);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Calculators");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
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

    public void area(View view) {
        Intent roi = new Intent(getApplicationContext(), Areacalculaotr.class);

        startActivity(roi);

    }

    public void roi(View view) {
        Intent roi = new Intent(getApplicationContext(), ROIcalculator.class);
        startActivity(roi);

    }

    public void morgage(View view) {
        Intent roi = new Intent(getApplicationContext(), Morgagecalculator.class);
        startActivity(roi);

    }
}