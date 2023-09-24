package in.orangeapp.realestate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;


import in.orangeapp.realestate.R;

public class Generaldocuments extends AppCompatActivity {
    ProgressDialog dialog;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generaldocuments);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("General Documents");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


    }


    public void draft_agreement(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/file/d/1xHjfAOuOHWpJCM_5R--ohR0w24mELbLw/view?usp=sharing")));

    }

    public void police(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/file/d/1XRXcrhILKjFHoxQko0t0Yvham6GzFbw6/view?usp=sharing")));

    }

    public void payment_recipet(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/file/d/1_cKeoMntc_OkstZX-Jb_rmugu2HPTnm5/view?usp=sharing")));


    }

    public void rent_agreemnt(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/file/d/1ysPBBLYoayb1_N600e_S3utY2rIAtWRR/view?usp=sharing")));


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