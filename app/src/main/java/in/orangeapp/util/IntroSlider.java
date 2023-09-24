package in.orangeapp.util;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import in.orangeapp.realestate.R;
import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;

public class IntroSlider extends AppIntro {

    // we are calling on create method
    // to generate the view for our java file.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setIndicatorColor(getColor(R.color.blackmain), getColor(R.color.gray_light));
        setColorSkipButton(getColor(R.color.blackmain));
        setColorDoneText(getColor(R.color.blackmain));

        // below line is for adding the new slide to our app.
        // we are creating a new instance and inside that
        // we are adding the title, description, image and
        // background color for our slide.
        // below line is use for creating first slide
        addSlide(AppIntroFragment.createInstance("Share", "List here\nPublish anywhere.",
                R.drawable.ic_share_, R.color.white, R.color.blackmain, R.color.blackmain, R.font.lato_bold, R.font.lato_bold));

        addSlide(AppIntroFragment.createInstance("Join", "Join the broker network close more deals.",
                R.drawable.ic_join, R.color.white, R.color.blackmain, R.color.blackmain, R.font.lato_bold, R.font.lato_bold));

        // below line is for creating second slide.
        addSlide(AppIntroFragment.createInstance("Find", "Find trustworthy brokers with right party.",
                R.drawable.ic_find, R.color.white, R.color.blackmain, R.color.blackmain, R.font.lato_bold, R.font.lato_bold));

        // below line is use to create third slide.
        addSlide(AppIntroFragment.createInstance("List", "List your inventory without naming party.",
                R.drawable.ic_list, R.color.white, R.color.blackmain, R.color.blackmain, R.font.lato_bold, R.font.lato_bold));

        addSlide(AppIntroFragment.createInstance("Notified", "Get Notified of relevant information in market.",
                R.drawable.ic_notify, R.color.white, R.color.blackmain, R.color.blackmain, R.font.lato_bold, R.font.lato_bold));
    }

    @Override
    protected void onDonePressed(@Nullable Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }

    @Override
    protected void onSkipPressed(@Nullable Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }
}
