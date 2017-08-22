package abanoubm.dayra.contact;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import abanoubm.dayra.R;

public class EditContact extends FragmentActivity {
    private ViewPager mPager;

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private static final int NUM_PAGES = 4;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle arguments = new Bundle();
            arguments.putString(ARG_ID, id);
            if (position == 0) {
                FragmentEditContactInfo fragment = new FragmentEditContactInfo();
                fragment.setArguments(arguments);
                return fragment;
            } else if (position == 1) {
                FragmentEditContactDay fragment = new FragmentEditContactDay();
                fragment.setArguments(arguments);
                return fragment;
            } else if (position == 2) {
                FragmentEditContactConnection fragment = new FragmentEditContactConnection();
                fragment.setArguments(arguments);
                return fragment;
            } else {
                FragmentEditContactMap fragment = new FragmentEditContactMap();
                fragment.setArguments(arguments);
                return fragment;
            }

        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    private String id;
    private int mCurrentTab = 0;
    private ImageView[] buttons;
    private TextView subHead2;
    private final int[] subHeads2 = new int[]{
            R.string.subhead_edit_info,
            R.string.subhead_edit_day,
            R.string.subhead_edit_connections,
            R.string.subhead_edit_map};

    private static final String ARG_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_edit_contact);
        findViewById(R.id.nav_back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });
        id = getIntent().getStringExtra(ARG_ID);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));

        subHead2 = ((TextView) findViewById(R.id.header));
        subHead2.setText(subHeads2[0]);


        buttons = new ImageView[]{
                (ImageView) findViewById(R.id.img1),
                (ImageView) findViewById(R.id.img2),
                (ImageView) findViewById(R.id.img3),
                (ImageView) findViewById(R.id.img4)
        };

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

        buttons[3].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCurrentTab != 3)
                    mPager.setCurrentItem(3);

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

    private void fireTab(int changedCurrent) {
        buttons[mCurrentTab].setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
        mCurrentTab = changedCurrent;
        buttons[changedCurrent].setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        subHead2.setText(subHeads2[changedCurrent]);
        ((InputMethodManager) getSystemService(
                INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mPager.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
