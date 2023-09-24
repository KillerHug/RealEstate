package in.orangeapp.realestate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.orangeapp.realestate.R;
import com.google.android.material.button.MaterialButton;

public class Morgagecalculator extends AppCompatActivity {
    Toolbar toolbar;
    EditText purchase_price, down_payment, annual_interst, lpi_year, additial_fees;
    MaterialButton button_submit, button_reset;
    TextView monthlyEMI,totallpoan_txt,total_nterst;
    LinearLayout cal_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affordabilitycalculator);
        toolbar = findViewById(R.id.toolbar);
        purchase_price = findViewById(R.id.purchase_price);
        down_payment = findViewById(R.id.down_payment);
        cal_layout = findViewById(R.id.cal_layout);
        annual_interst = findViewById(R.id.annual_interst);
        lpi_year = findViewById(R.id.lpi_year);
        additial_fees = findViewById(R.id.additial_fees);
        button_reset = findViewById(R.id.button_reset);
        button_submit = findViewById(R.id.button_submit);
        monthlyEMI = findViewById(R.id.monthlyEMI);
        totallpoan_txt = findViewById(R.id.totallpoan_txt);
        total_nterst = findViewById(R.id.total_nterst);
        toolbar.setTitle("Home Loan Calculator");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                float purchase_pricee = Float.parseFloat(purchase_price.getText().toString());
                float down_paymentt = Float.parseFloat(down_payment.getText().toString());
                float loan_in_period = Float.parseFloat(lpi_year.getText().toString());
                float annual_interstt = Float.parseFloat(annual_interst.getText().toString());
                float additial_feess = Float.parseFloat(additial_fees.getText().toString());

                float total_purchase_pricee=purchase_pricee+additial_feess;
                float loan_amount = total_purchase_pricee - down_paymentt;

                float total_interts = loan_amount * annual_interstt * loan_in_period / 100;
                float total_payble = loan_amount + total_interts;
                float total_Month = loan_in_period * 12;
                float final_value = total_payble / total_Month;


                total_nterst.setText("Total Interst : "+total_interts);
                totallpoan_txt.setText("Loan Amount : "+loan_amount);
                monthlyEMI.setText("Monthly EMI : "+final_value);
                cal_layout.setVisibility(View.VISIBLE);

               // Toast.makeText(Morgagecalculator.this, ""+final_value, Toast.LENGTH_SHORT).show();

            }
        });

        button_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                purchase_price.setText("");
                down_payment.setText("");
                annual_interst.setText("");
                lpi_year.setText("");
                additial_fees.setText("");
                cal_layout.setVisibility(View.GONE);
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