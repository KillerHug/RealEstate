package in.orangeapp.realestate;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import in.orangeapp.item.ItemAbout;
import in.orangeapp.util.API;
import in.orangeapp.util.JsonUtils;
import in.orangeapp.realestate.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class AboutUsActivity extends AppCompatActivity {

    TextView txtAppName, txtVersion, txtCompany, txtEmail, txtWebsite, txtContact;
    ImageView imgAppLogo;
    ArrayList<ItemAbout> mListItem;
    ScrollView mScrollView;
    ProgressBar mProgressBar;
    WebView webView;
    Toolbar toolbar;
    JsonUtils jsonUtils;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.menu_about));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        jsonUtils = new JsonUtils(this);
        jsonUtils.forceRTLIfSupported(getWindow());

        txtAppName = findViewById(R.id.text_app_name);
        txtVersion = findViewById(R.id.text_version);
        txtCompany = findViewById(R.id.text_company);
        txtEmail = findViewById(R.id.text_email);
        txtWebsite = findViewById(R.id.text_website);
        txtContact = findViewById(R.id.text_contact);
        imgAppLogo = findViewById(R.id.app_logo_about_us);
        webView = findViewById(R.id.webView);

        mScrollView = findViewById(R.id.scrollView);
        mProgressBar = findViewById(R.id.progressBar);

        mListItem = new ArrayList<>();

        setResult();

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(AboutUsActivity.this));
        jsObj.addProperty("method_name", "get_app_details");
        if (JsonUtils.isNetworkAvailable(AboutUsActivity.this)) {
        }

    }


    private void setResult() {

//        ItemAbout itemAbout = mListItem.get(0);
//        txtAppName.setText(itemAbout.getAppName());
//        txtVersion.setText(itemAbout.getAppVersion());
//        txtCompany.setText(itemAbout.getAppAuthor());
//        txtEmail.setText(itemAbout.getAppEmail());
//        txtWebsite.setText(itemAbout.getAppWebsite());
//        txtContact.setText(itemAbout.getAppContact());
//        Picasso.get().load(Constant.IMAGE_PATH + itemAbout.getAppLogo()).into(imgAppLogo);

        String mimeType = "text/html;charset=UTF-8";
        String encoding = "utf-8";
        //   String htmlText = itemAbout.getAppDescription();

        String text = "<html><head>"
                + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/custom.ttf\")}body{font-family:" +
                " MyFont; font-size:14.6px; color: #EDA826;text-align:justify;line-height:1.6}"
                + "</style></head>"
                + "<body style=\"background-color:black;>"
                + " " +

                "<span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:14.6px; color:#000000\"> " +
                "&#147;The Price of doing the same old thing is far higher than the price of change&#148; - Bill Gates </span> " +
                "</div> " +
                "<div class=\"pos\" id=\"_100:124\" style=\"top:124;left:100\"> " +
                "<span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:16.6px; color:#32465b\"> " +
                ".</span> " +
                "</div> " +
                "<div class=\"pos\" id=\"_100:191\" style=\"top:191;left:100\"> " +
                "<span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:16.6px; color:#ffffff\"> " +
                "And here we are with one such unique solution where there is - Innovation you can avail; Values you can trust; Services you can rely and finally Results you can enjoy.</span> " +
                "</div> " +
                "<div class=\"pos\" id=\"_100:221\" style=\"top:221;left:100\"> " +
                "<span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:16.6px; color:#ffffff\"> " +
                " </span>" +
                "</div>" +
                "<div class=\"pos\" id=\"_100:281\" style=\"top:281;left:100\"> " +
                "<span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:16.6px; color:#ffffff\">" +
                "We at <span style=\"font-weight:bold;background-color:white;color:#000000\"> Doorsteps <span style=\" color:#ff0000\"> Realty </span> </span> are firm believer of making Dreams = Reality, offering our platform as user friendly, transparent, easiest, quickest medium of getting closer to your dream property anywhere in India.</span> " +
                "</div>" +
                "<div class=\"pos\" id=\"_100:311\" style=\"top:311;left:100\"> " +
                "<span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:16.6px; color:#ffffff\">" +
                "</span>" +
                "</div>" +
                "<div class=\"pos\" id=\"_100:341\" style=\"top:341;left:100\"> " +
                "<span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:16.6px; color:#ffffff\">" +
                "</span>" +
                "</div>" +
                "<div class=\"pos\" id=\"_100:371\" style=\"top:371;left:100\"> " +
                "<span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:16.6px; color:#ffffff\">" +
                " </span>" +
                "</div>" +
                "<div class=\"pos\" id=\"_100:401\" style=\"top:401;left:100\"> " +
                "<span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:16.6px; color:#ffffff\">" +
                "Living up to its meaning , <span style=\"font-weight:bold;background-color:white;color:#000000\"> Doorsteps<span style=\" color:#ff0000\"> Realty </span>   </span><span style=\" color:#ffffff\">    is the most doable, overall reliable step towards  your  effective  Property  needs.  It  has  to  it  everything  you  need  from conception to reception with keys to unlock your dream property.</span></span> " +
                "</div>" +
                "<div class=\"pos\" id=\"_100:430\" style=\"top:430;left:100\"> " +
                "<span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:16.6px; color:#ffffff\">" +
                "</span> " +
                "</div> " +
                "<div class=\"pos\" id=\"_100:460\" style=\"top:460;left:100\"> " +
                "<span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:16.6px; color:#ffffff\">" +
                "</span> " +
                "</div> " +
                "<div class=\"pos\" id=\"_100:520\" style=\"top:520;left:100\">" +
                "<span id=\"_16.6\" style=\"font-weight:bold;font-style:italic; font-family:Arial; font-size:16.6px; color:#ffffff\"> " +
                "D<span style=\"font-weight:normal\"> = Doable</span></span>" +
                "</div>" +
                "<div class=\"pos\" id=\"_100:550\" style=\"top:550;left:100\">" +
                "<span id=\"_16.6\" style=\"font-weight:bold;font-style:italic; font-family:Arial; font-size:16.6px; color:#ffffff\"> " +
                "OO<span style=\"font-weight:normal\"> = Overall</span></span>" +
                "</div>" +
                "<div class=\"pos\" id=\"_100:580\" style=\"top:580;left:100\"> " +
                "<span id=\"_16.6\" style=\"font-weight:bold;font-style:italic; font-family:Arial; font-size:16.6px; color:#ffffff\"> " +
                "R<span style=\"font-weight:normal\"> = Reliable</span></span> " +
                "</div> " +
                "<div class=\"pos\" id=\"_100:610\" style=\"top:610;left:100\"> " +
                "<span id=\"_16.6\" style=\"font-weight:bold;font-style:italic; font-family:Arial; font-size:16.6px; color:#ffffff\"> " +
                "S<span style=\"font-weight:normal\"> = Step</span></span> " +
                "</div> " +
                "<div class=\"pos\" id=\"_100:640\" style=\"top:640;left:100\"> " +
                "<span id=\"_16.6\" style=\"font-weight:bold;font-style:italic; font-family:Arial; font-size:16.6px; color:#ffffff\"> " +
                "T <span style=\"font-weight:normal\"> = Towards</span></span> " +
                "</div> " +
                "<div class=\"pos\" id=\"_100:670\" style=\"top:670;left:100\"> " +
                "<span id=\"_16.6\" style=\"font-weight:bold;font-style:italic; font-family:Arial; font-size:16.6px; color:#ffffff\"> " +
                "E<span style=\"font-weight:normal\"> = Effective</span></span> " +
                "</div> " +
                "<div class=\"pos\" id=\"_100:700\" style=\"top:700;left:100\"> " +
                "<span id=\"_16.6\" style=\"font-weight:bold;font-style:italic; font-family:Arial; font-size:16.6px; color:#ffffff\"> " +
                "P<span style=\"font-weight:normal\"> = Property </span></span> " +
                "</div> " +
                "<div class=\"pos\" id=\"_100:730\" style=\"top:730;left:100\"> " +
                "<span id=\"_16.6\" style=\"font-weight:bold;font-style:italic; font-family:Arial; font-size:16.6px; color:#ffffff\"> " +
                "S<span style=\"font-weight:normal\"> = Solution</span></span> " +
                "</div> " +
                "<div class=\"pos\" id=\"_100:760\" style=\"top:760;left:100\"> " +
                "<span id=\"_16.6\" style=\"font-weight:bold;font-style:italic; font-family:Arial; font-size:16.6px; color:#ff0000\"> " +
                "REALTY </span> " +
                "</div> " +
                "<div class=\"pos\" id=\"_100:820\" style=\"top:820;left:100\"> " +
                "<span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:16.6px; color:#ffffff\"> " +
                "Coming together is a beginning; keeping together is progress; working together is success. - By Edward Everett Hale<br>Lets come together to begin a new journey, progressing together to find your dream property and thereby create joy and value you deserve.</span> " +
                "</div> " +
                "<div class=\"pos\" id=\"_100:850\" style=\"top:850;left:100\"> " +
                "<span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:16.6px; color:#ffffff\"> " +
                "</span> " +
                "</div> " +
                "<div class=\"pos\" id=\"_100:910\" style=\"top:910;left:100\"> " +
                "<span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:16.6px; color:#ffffff\"> " +
                "</span> " +
                "</div> " +
                "<div class=\"pos\" id=\"_100:940\" style=\"top:940;left:100\"> " +
                "<span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:16.6px; color:#fff\"> " +
                "</span> " +
                "</div>"

                + "</body></html>";

        String htmlText = "<html>\n" +
                "<head>\n" +
                "\t<style type=\"text/css\">\n" +
                "\t@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/custom.ttf\")}body{font-family: MyFont; font-size:14.6px; color: #ffffff;text-align:justify;line-height:1.6}\n" +
                "\t</style>\n" +
                "</head>\n" +
                "<body style=\"background-color:black;\">\n" +
                "                <span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:14.6px; color:#EDA826\">\n" +
                "                &#147;The Price of doing the same old thing is far higher than the price of change&#148; - Bill Gates </span>\n" +
                "                </div>\n" +
                "                <div class=\"pos\" id=\"_100:124\" style=\"top:124;left:100\">\n" +
                "                <span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:14px; color:#32465b\">\n" +
                "                .</span>\n" +
                "                </div>\n" +
                "                <div class=\"pos\" id=\"_100:191\" style=\"top:191;left:100\">\n" +
                "                <span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:14px; color:#ffffff\">\n" +
                "                And here we are <span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:14px; color:#EDA826\"><b>with intent to add colours to your life</b></span> with one such New Path where there is - Innovation you can avail; Values you can trust; Services you can rely and finally<span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:14px; color:#EDA826\"> Fruitful</span> Results you can enjoy.</span>\n" +
                "                </div>\n" +
                "                <div class=\"pos\" id=\"_100:221\" style=\"top:221;left:100\">\n" +
                "                <span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:14px; color:#ffffff\">\n" +
                "                 </span>\n" +
                "                </div>\n" +
                "                <div class=\"pos\" id=\"_100:281\" style=\"top:281;left:100\">\n" +
                "                <span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:14px; color:#ffffff\">\n" +
                "                <span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:14px; color:#EDA826\"><b>&#147;ORANGE - We Love to Share&#148; is here to share our care by Offering - affordable RANGE of properties via</b></span> our platform <span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:14px; color:#EDA826\"><b>which is</b></span> transparent, easiest, quickest medium of getting closer to your dream property anywhere in India thereby making your Dreams = Reality. </span>\n" +
                "                </div>\n" +
                "                <div class=\"pos\" id=\"_100:311\" style=\"top:311;left:100\">\n" +
                "                <span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:14px; color:#ffffff\">\n" +
                "                </span>\n" +
                "                </div>\n" +
                "                <div class=\"pos\" id=\"_100:341\" style=\"top:341;left:100\">\n" +
                "                <span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:14px; color:#ffffff\">\n" +
                "                </span>\n" +
                "                </div>\n" +
                "                <div class=\"pos\" id=\"_100:371\" style=\"top:371;left:100\">\n" +
                "                <span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:14px; color:#ffffff\">\n" +
                "                 </span>\n" +
                "                </div>\n" +
                "                <div class=\"pos\" id=\"_100:401\" style=\"top:401;left:100\">\n" +
                "                <span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:14px; color:#ffffff\">\n" +
                "                Living up to its meaning, <span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:14px; color:#EDA826\"><b>&#147;ORANGE - We Love to Share&#148; is the most Outstanding - RANGE of Properties that we are delighted to share</b></span> towards each of your property needs comprising of everything you require from conception to reception with keys to unlock your dream property.</span>\n" +
                "                </div>\n" +
                "                <div class=\"pos\" id=\"_100:430\" style=\"top:430;left:100\">\n" +
                "                <span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:14px; color:#ffffff\">\n" +
                "                </span>\n" +
                "                </div>\n" +
                "                <div class=\"pos\" id=\"_100:460\" style=\"top:460;left:100\">\n" +
                "                <span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:14px; color:#ffffff\">\n" +
                "                </span>\n" +
                "                </div>\n" +
                "                <div class=\"pos\" id=\"_100:520\" style=\"top:520;left:100\">\n" +
                "                <span id=\"_16.6\" style=\"font-weight:bold;font-style:italic; font-family:Arial; font-size:14px; color:#EDA826\">\n" +
                "                <br>O<span style=\"font-weight:normal;color:#ffffff\"> = Oneness in heart with our customers we Love to Share Outstanding</span></span>\n" +
                "                </div>\n" +
                "                <div class=\"pos\" id=\"_100:550\" style=\"top:550;left:100\">\n" +
                "                <span id=\"_16.6\" style=\"font-weight:bold;font-style:italic; font-family:Arial; font-size:14px; color:#EDA826\">\n" +
                "                R<span style=\"font-weight:normal;color:#ffffff\"> = Reliable</span></span>\n" +
                "                </div>\n" +
                "                <div class=\"pos\" id=\"_100:580\" style=\"top:580;left:100\">\n" +
                "                <span id=\"_16.6\" style=\"font-weight:bold;font-style:italic; font-family:Arial; font-size:14px; color:#EDA826\">\n" +
                "                A<span style=\"font-weight:normal;color:#ffffff\"> = Affordable</span></span>\n" +
                "                </div>\n" +
                "                <div class=\"pos\" id=\"_100:610\" style=\"top:610;left:100\">\n" +
                "                <span id=\"_16.6\" style=\"font-weight:bold;font-style:italic; font-family:Arial; font-size:14px; color:#EDA826\">\n" +
                "                N<span style=\"font-weight:normal;color:#ffffff\"> = Nationwide</span></span>\n" +
                "                </div>\n" +
                "                <div class=\"pos\" id=\"_100:640\" style=\"top:640;left:100\">\n" +
                "                <span id=\"_16.6\" style=\"font-weight:bold;font-style:italic; font-family:Arial; font-size:14px; color:#EDA826\">\n" +
                "                G <span style=\"font-weight:normal;color:#ffffff\"> = Grand/Genuine</span></span>\n" +
                "                </div>\n" +
                "                <div class=\"pos\" id=\"_100:670\" style=\"top:670;left:100\">\n" +
                "                <span id=\"_16.6\" style=\"font-weight:bold;font-style:italic; font-family:Arial; font-size:14px; color:#EDA826\">\n" +
                "                E<span style=\"font-weight:normal;color:#ffffff\"> = Exemplary</span></span>\n" +
                "                </div>\n" +
                "\n" +
                "                <div class=\"pos\" id=\"_100:780\" style=\"top:780;left:100\">\n" +
                "                <span style=\"font-style:italic; font-family:Arial; font-size:14px; color:#ffffff\"><br>properties adding fruitful colours to your Life.</span>\n" +
                "                </div>\n" +
                "                <div class=\"pos\" id=\"_100:800\" style=\"top:800;left:100\">\n" +
                "                <span style=\"font-style:italic; font-family:Arial; font-size:14px; color:#ffffff\"><br>As Edward Everett Hale rightly said:</span>\n" +
                "            </div>\n" +
                "            <div class=\"pos\" id=\"_100:800\" style=\"top:800;left:100\">\n" +
                "                <span id=\"_16.6\" style=\"font-weight:bold;font-style:italic; font-family:Arial; font-size:14px; color:#EDA826\">Coming together is a beginning; </span>\n" +
                "            </div>\n" +
                "            <div class=\"pos\" id=\"_100:800\" style=\"top:800;left:100\">\n" +
                "                <span id=\"_16.6\" style=\"font-weight:bold;font-style:italic; font-family:Arial; font-size:14px; color:#EDA826\">Keeping together is progress; </span>\n" +
                "            </div>\n" +
                "            <div class=\"pos\" id=\"_100:800\" style=\"top:800;left:100\">\n" +
                "                <span id=\"_16.6\" style=\"font-weight:bold;font-style:italic; font-family:Arial; font-size:14px; color:#EDA826\">Working together is success. \n" +
                " </span></div>\n" +
                "              \n" +
                "                <div class=\"pos\" id=\"_100:820\" style=\"top:820;left:100\">\n" +
                "                <span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:14px; color:#ffffff\">\n" +
                "                <br>Lets come together to begin a new journey, progressing together to find your dream property and thereby create joy and value you deserve.</span>\n" +
                "                </div>\n" +
                "                <div class=\"pos\" id=\"_100:850\" style=\"top:850;left:100\">\n" +
                "                <span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:14px; color:#ffffff\">\n" +
                "                </span>\n" +
                "                </div>\n" +
                "                <div class=\"pos\" id=\"_100:910\" style=\"top:910;left:100\">\n" +
                "                <span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:14px; color:#ffffff\">\n" +
                "                </span>\n" +
                "                </div>\n" +
                "                <div class=\"pos\" id=\"_100:940\" style=\"top:940;left:100\">\n" +
                "                <span id=\"_16.6\" style=\"font-style:italic; font-family:Arial; font-size:14px; color:#fff\">\n" +
                "                </span>\n" +
                "                </div></body>\n" +
                "</html>";
        webView.setHorizontalScrollBarEnabled(false);

        webView.loadDataWithBaseURL(null, htmlText, mimeType, encoding, null);
    }


    public void showToast(String msg) {
        Toast.makeText(AboutUsActivity.this, msg, Toast.LENGTH_LONG).show();
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
