package abanoubm.dayra.contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import abanoubm.dayra.R;

public class FragmentDisplayContact extends Fragment {
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
                FragmentDisplayContactInfo fragment = new FragmentDisplayContactInfo();
                fragment.setArguments(arguments);
                return fragment;
            } else if (position == 1) {
                FragmentDisplayContactDay fragment = new FragmentDisplayContactDay();
                fragment.setArguments(arguments);
                return fragment;
            } else if (position == 2) {
                FragmentDisplayContactConnection fragment = new FragmentDisplayContactConnection();
                fragment.setArguments(arguments);
                return fragment;
            } else {
                FragmentDisplayContactMap fragment = new FragmentDisplayContactMap();
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
    private boolean dualMode;
    private static final String ARG_ID = "id";
    private static final String ARG_DUAL_MODE = "dual";

    private TextView subHead2;
    private final int[] subHeads2 = new int[]{
            R.string.subhead_display_info,
            R.string.subhead_display_day,
            R.string.subhead_display_connections,
            R.string.subhead_display_map};
    private ImageView[] buttons;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            id = arguments.getString(ARG_ID);
            dualMode = arguments.getBoolean(ARG_DUAL_MODE);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_display_contact, container, false);

        mPager = (ViewPager) root.findViewById(R.id.pager);
        mPager.setAdapter(new ScreenSlidePagerAdapter(getChildFragmentManager()));

        subHead2 = ((TextView) root.findViewById(R.id.header));
        subHead2.setText(R.string.subhead_display_info);

        buttons = new ImageView[]{
                (ImageView) root.findViewById(R.id.img1),
                (ImageView) root.findViewById(R.id.img2),
                (ImageView) root.findViewById(R.id.img3),
                (ImageView) root.findViewById(R.id.img4),
                (ImageView) root.findViewById(R.id.img5)
        };


        buttons[0].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(0);


            }
        });
        buttons[1].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
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

        buttons[4].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (current != 4) {
                    startActivity(new Intent(getActivity(), EditContact.class).putExtra(ARG_ID, id));
                    if (!dualMode)
                        getActivity().finish();

                }

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
        return root;
    }

    private void fireTab(int changedCurrent) {
        buttons[current].setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.grey));
        current = changedCurrent;
        buttons[changedCurrent].setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        subHead2.setText(subHeads2[changedCurrent]);
    }

}