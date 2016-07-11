package abanoubm.dayra.contact;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import abanoubm.dayra.R;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.model.ContactLocation;

public class EditContact extends FragmentActivity {
    private static final int NUM_PAGES = 4;
    private ViewPager mPager;

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

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
                arguments.putDouble(ARG_LAT, mLocation.getMapLat());
                arguments.putDouble(ARG_LNG, mLocation.getMapLng());
                arguments.putFloat(ARG_ZOM, mLocation.getZoom());
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
    private int current = 0;
    private ImageView[] buttons;
    private ContactLocation mLocation;
    private TextView subHead2;
    private final int[] subHeads2 = new int[]{
            R.string.subhead_edit_info,
            R.string.subhead_edit_day,
            R.string.subhead_edit_connections,
            R.string.subhead_edit_map};

    private static final String ARG_LAT = "lat";
    private static final String ARG_LNG = "lon";
    private static final String ARG_ZOM = "zoom";
    private static final String ARG_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_edit_contact);

        id = getIntent().getStringExtra(ARG_ID);
        mPager = (ViewPager) findViewById(R.id.pager);

        new GetLocationTask().execute();


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
                if (current != 0)
                    mPager.setCurrentItem(0);


            }
        });
        buttons[1].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (current != 1)
                    mPager.setCurrentItem(1);

            }
        });
        buttons[2].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (current != 2)
                    mPager.setCurrentItem(2);


            }
        });

        buttons[3].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (current != 3)
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
        buttons[current].setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
        current = changedCurrent;
        buttons[changedCurrent].setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        subHead2.setText(subHeads2[changedCurrent]);
    }


    private class GetLocationTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(EditContact.this);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            mPager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));
            pBar.dismiss();

        }

        @Override
        protected Void doInBackground(Void... params) {
            mLocation = DB.getInstant(getApplicationContext()).getContactLocation(id);
            return null;
        }
    }
}
