package in.orangeapp.realestate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import in.orangeapp.realestate.R;
import com.google.android.material.button.MaterialButton;

public class ROIcalculator extends AppCompatActivity {

    Toolbar toolbar;

    MaterialButton button_submit,button_reset;
    EditText purchase_price,sellingprice,ov_expences,hli_year;
    TextView total_result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roicalculator);
        toolbar = findViewById(R.id.toolbar);
        purchase_price = findViewById(R.id.purchase_price);
        sellingprice = findViewById(R.id.sellingprice);
        button_submit = findViewById(R.id.button_submit);
        ov_expences = findViewById(R.id.ov_expences);
        total_result = findViewById(R.id.total_result);
        hli_year = findViewById(R.id.hli_year);
        button_reset = findViewById(R.id.button_reset);
        toolbar.setTitle("ROI Calculator");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

       button_submit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               try {
                   float purchase_value = Float.parseFloat(purchase_price.getText().toString());
                   float selling_value = Float.parseFloat(sellingprice.getText().toString());
                   if (ov_expences.getText().toString().equalsIgnoreCase("")) {
                       ov_expences.setText("0");
                   }

                   float ov_value = Float.parseFloat(ov_expences.getText().toString());
                   float hli_value = Float.parseFloat(hli_year.getText().toString());

                   float purchasingvalkue = purchase_value + ov_value;
                   float value = selling_value - purchasingvalkue;
                   float result = value / purchasingvalkue;
                   float final_result = result * 100;
                   float final_result2 = final_result / hli_value;
                   //Toast.makeText(ROIcalculator.this, ""+final_result, Toast.LENGTH_SHORT).show();

                   total_result.setText("Total ROI = " + String.format("%.2f", final_result2) + " %");
               }catch(Exception e)
               {
                   e.printStackTrace();
               }
           }
       });

        button_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                purchase_price.setText("");
                sellingprice.setText("");
                ov_expences.setText("");
                hli_year.setText("");
                total_result.setText("Total ROI = 0");
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