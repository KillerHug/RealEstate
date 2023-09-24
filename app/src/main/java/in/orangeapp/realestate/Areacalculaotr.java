package in.orangeapp.realestate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;

import in.orangeapp.realestate.R;

public class Areacalculaotr extends AppCompatActivity {

    EditText yards, feet, meters, acre;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_areacalculaotr);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Area Calculator");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        yards = findViewById(R.id.yards);
        feet = findViewById(R.id.feet);
        meters = findViewById(R.id.meters);
        acre = findViewById(R.id.acre);

        yards.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
                if (yards.hasFocus()) {
                    if (!s.toString().equalsIgnoreCase("")) {

                        Float value = Float.parseFloat(s.toString());

                        feet.setText("" + value * 9);
                        meters.setText("" + value / 1.196);
                        acre.setText("" + value / 4840);


                    } else {
                        feet.setText("");
                        meters.setText("");
                        acre.setText("");

                    }
                }
            }
        });

        feet.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub



            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (feet.hasFocus()) {

                    if (!s.toString().equalsIgnoreCase("")) {


                        Float value = Float.parseFloat(s.toString());

                        yards.setText("" + value / 9);
                        meters.setText(String.format("%.5f", value / 10.764));
                        String vl = String.format("%.5f", value / 43560);

                        acre.setText(vl);


                    } else {
                        yards.setText("");
                        meters.setText("");
                        acre.setText("");

                    }
                }
                // TODO Auto-generated method stub
            }
        });

        meters.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
                if (meters.hasFocus()) {

                    if (!s.toString().equalsIgnoreCase("")) {


                        Float value = Float.parseFloat(s.toString());

                        yards.setText("" + value * 1.196);
                        feet.setText("" + value * 10.764);
                        acre.setText("" + value / 4047);


                    } else {
                        yards.setText("");
                        feet.setText("");
                        acre.setText("");

                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

        acre.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {


                if (acre.hasFocus()) {
                    if (!s.toString().equalsIgnoreCase("")) {


                        Float value = Float.parseFloat(s.toString());

                        yards.setText("" + value * 4840);
                        feet.setText("" + value * 43560);
                        meters.setText("" + value * 4047);
                    }

                 else {
                    yards.setText("");
                    feet.setText("");
                    meters.setText("");

                }
            }

                // TODO Auto-generated method stub
            }
        });
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