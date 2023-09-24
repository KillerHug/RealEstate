package in.orangeapp.realestate;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import in.orangeapp.util.YourPreference;
import in.orangeapp.realestate.R;


public class SplashActivity extends AppCompatActivity {

    boolean mIsBackButtonPressed;
    MyApplication myApplication;
    String str_package;
    private VideoView videoView;
//    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        videoView = findViewById(R.id.idVideoView);
//        imageView = findViewById(R.id.ivSplash);
//        Glide.with(this).load(R.drawable.splash_gif).into(imageView);
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String signup = yourPrefrence.getData("signup");
//        full screen video
   /*     DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)  videoView.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.leftMargin = 0;
        videoView.setLayoutParams(params);

//video play
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash_video_2);
        videoView.setVideoURI(uri);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);
        videoView.setMediaController(mediaController);
        videoView.start();
//        video listener
        videoView.setOnCompletionListener(mp -> {

        });*/

        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent;
            if (signup.equalsIgnoreCase("1")) {
                intent = new Intent(getApplicationContext(), MainActivity.class);
            } else {
                intent = new Intent(getApplicationContext(), SignInActivity.class);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }, 4000);


    }

    public void showToast(String msg) {
        Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        // set the flag to true so the next activity won't start up
        mIsBackButtonPressed = true;
        super.onBackPressed();

    }
}
