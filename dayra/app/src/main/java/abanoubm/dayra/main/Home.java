package abanoubm.dayra.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import abanoubm.dayra.BuildConfig;
import abanoubm.dayra.R;

public class Home extends FragmentActivity {
    private ViewPager mPager;

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private static final int NUM_PAGES = 3;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return new FragmentHomeMain();
            else if (position == 1)
                return new FragmentHomeIO();
            else
                return new FragmentHomeSettings();

        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }


    private int mCurrentTab = 1;
    private TextView subHead2;
    private ImageView[] buttons;
    private final int[] subHeads2 = new int[]{
            R.string.label_home_main,
            R.string.label_home_out,
            R.string.label_home_settings};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Utility.getArabicLang(getApplicationContext()) == 1) {
            Utility.setArabicLang(getApplicationContext(), 2);

            Locale myLocale = new Locale("ar");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);

            finish();
            startActivity(new Intent(getIntent()));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home);

        ((TextView) findViewById(R.id.footer)).setText("dayra "+ BuildConfig.VERSION_NAME+" @"+new SimpleDateFormat(
                "yyyy", Locale.getDefault())
                .format(new Date())+" Abanoub M.");
        findViewById(R.id.nav_back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                buttons[mCurrentTab].setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.lightgrey));
                buttons[0].setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                DB.getInstant(getApplicationContext()).closeDB();
                Utility.clearLogin(getApplicationContext());
                Intent intent = new Intent(getApplicationContext(), Main.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));

        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        buttons = new ImageView[]{
                (ImageView) findViewById(R.id.img1),
                (ImageView) findViewById(R.id.img2),
                (ImageView) findViewById(R.id.img3),
                (ImageView) findViewById(R.id.img4)
        };
        subHead2 = (TextView) findViewById(R.id.subhead2);
        subHead2.setText(subHeads2[0]);

        buttons[0].setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                buttons[mCurrentTab].setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.lightgrey));
                buttons[0].setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                DB.getInstant(getApplicationContext()).closeDB();
                Utility.clearLogin(getApplicationContext());
                Intent intent = new Intent(getApplicationContext(), Main.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        buttons[1].setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCurrentTab != 1)
                    mPager.setCurrentItem(0);


            }
        });
        buttons[2].setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCurrentTab != 2)
                    mPager.setCurrentItem(1);


            }
        });
        buttons[3].setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCurrentTab != 3)
                    mPager.setCurrentItem(2);

            }
        });

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                fireTab(position+1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void fireTab(int changedTagCursor) {

        buttons[mCurrentTab].setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.lightgrey));
        mCurrentTab = changedTagCursor;
        buttons[changedTagCursor].setBackgroundColor(ContextCompat.getColor(this, R.color.white));

        subHead2.setText(subHeads2[changedTagCursor-1]);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Utility.getArabicLang(getApplicationContext()) != 0)
            Utility.setArabicLang(getApplicationContext(), 1);
    }
}
