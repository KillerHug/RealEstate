package in.orangeapp.realestate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import in.orangeapp.realestate.R;

public class Contactus extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.menu_contact_us));
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

    public void mail1(View view) {

//            Intent intent = new Intent(Intent.ACTION_SENDTO);
//            intent.setData(Uri.parse("mailto:"+"support@doorstepsrealty.com")); // only email apps should handle this
//              if (intent.resolveActivity(getPackageManager()) != null) {
//                startActivity(intent);
//            }
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setType("plain/text")
                .setData(Uri.parse("mailto:enquire@orangeapp.in"))
                .putExtra(Intent.EXTRA_EMAIL, new String[]{"enquire@orangeapp.in"});

        startActivity(intent);
    }

    public void mail2(View view) {

//        Intent intent = new Intent(Intent.ACTION_SENDTO);
//        intent.setData(Uri.parse("mailto:"+"doorstepsrealty@gmail.com")); // only email apps should handle this
//         if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        }
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setType("plain/text")
                .setData(Uri.parse("mailto:marketing@doorstepsrealty.com"))
                .putExtra(Intent.EXTRA_EMAIL, new String[]{"marketing@doorstepsrealty.com"});
        startActivity(intent);
    }

    public void openwhatsapp(View view) {

        String phoneNumberWithCountryCode = "+919911308309";
        startActivity(
                new Intent(Intent.ACTION_VIEW,
                        Uri.parse(
                                String.format("https://api.whatsapp.com/send?phone=%s&text=%s", phoneNumberWithCountryCode, "Hi - I want to suggest..")
                        )
                )
        );
    }
}