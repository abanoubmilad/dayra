package abanoubm.dayra.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import abanoubm.dayra.R;
import abanoubm.dayra.main.Utility;

public class Search extends AppCompatActivity {
    private ViewPager mPager;

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private static final int NUM_PAGES = 3;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return new FragmentSearchInfo();
            else if (position == 1)
                return new FragmentSearchDates();
            else
                return new FragmentSearchBirthdays();

        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    private int mCurrentTab = 0;
    private TextView subHead2;
    private final int[] subHeads2 = new int[]{
            R.string.subhead_search_info,
            R.string.subhead_search_dates,
            R.string.subhead_search_bdays
    };
    private ImageView[] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_search);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));


        buttons = new ImageView[]{
                (ImageView) findViewById(R.id.img1),
                (ImageView) findViewById(R.id.img2),
                (ImageView) findViewById(R.id.img3),
        };

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));

        subHead2 = (TextView) findViewById(R.id.subhead2);
        subHead2.setText(subHeads2[0]);

        buttons[0].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCurrentTab != 0)
                    mPager.setCurrentItem(0);

            }
        });
        buttons[1].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCurrentTab != 1)
                    mPager.setCurrentItem(1);
            }
        });
        buttons[2].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCurrentTab != 2)
                    mPager.setCurrentItem(2);

            }
        });

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                fireTab(position);
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

        subHead2.setText(subHeads2[changedTagCursor]);


    }
}
