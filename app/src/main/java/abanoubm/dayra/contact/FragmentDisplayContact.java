package abanoubm.dayra.contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import abanoubm.dayra.R;

public class FragmentDisplayContact extends Fragment {
    private String id;
    private int current = 0;
    private boolean dualMode;
    private static final String ARG_ID = "id";
    private static final String ARG_DUAL_MODE = "dual";

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

        root.findViewById(R.id.subhead1).setVisibility(View.GONE);
        final TextView subhead2 = ((TextView) root.findViewById(R.id.subhead2));

        final ImageView[] buttons = new ImageView[]{
                (ImageView) root.findViewById(R.id.img1),
                (ImageView) root.findViewById(R.id.img2),
                (ImageView) root.findViewById(R.id.img3),
                (ImageView) root.findViewById(R.id.img4),
                (ImageView) root.findViewById(R.id.img5)
        };


        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(ARG_ID, id);

            FragmentDisplayContactInfo fragment = new FragmentDisplayContactInfo();
            fragment.setArguments(arguments);

            subhead2.setText(R.string.subhead_display_info);

            getChildFragmentManager().beginTransaction()
                    .replace(R.id.display_contact_fragment, fragment)
                    .commit();

        }

        buttons[0].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (current != 0) {
                    buttons[current].setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.grey));
                    current = 0;
                    buttons[0].setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));

                    Bundle arguments = new Bundle();
                    arguments.putString(ARG_ID, id);

                    subhead2.setText(R.string.subhead_display_info);

                    FragmentDisplayContactInfo fragment = new FragmentDisplayContactInfo();
                    fragment.setArguments(arguments);

                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.display_contact_fragment, fragment)
                            .commit();
                }

            }
        });
        buttons[1].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (current != 1) {
                    buttons[current].setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.grey));
                    current = 1;
                    buttons[1].setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));

                    subhead2.setText(R.string.subhead_display_day);

                    Bundle arguments = new Bundle();
                    arguments.putString(ARG_ID, id);

                    FragmentDisplayContactDay fragment = new FragmentDisplayContactDay();
                    fragment.setArguments(arguments);

                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.display_contact_fragment, fragment)
                            .commit();
                }
            }
        });
        buttons[2].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (current != 2) {
                    buttons[current].setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.grey));
                    current = 2;
                    buttons[2].setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));


                    subhead2.setText(R.string.subhead_display_connections);

                    Bundle arguments = new Bundle();
                    arguments.putString(ARG_ID, id);

                    FragmentDisplayContactConnection fragment = new FragmentDisplayContactConnection();
                    fragment.setArguments(arguments);

                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.display_contact_fragment, fragment)
                            .commit();
                }

            }
        });

        buttons[3].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (current != 3) {
                    buttons[current].setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.grey));
                    current = 3;
                    buttons[3].setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));


                    subhead2.setText(R.string.subhead_display_map);

                    Bundle arguments = new Bundle();
                    arguments.putString(ARG_ID, id);

                    FragmentDisplayContactMap fragment = new FragmentDisplayContactMap();
                    fragment.setArguments(arguments);

                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.display_contact_fragment, fragment)
                            .commit();
                }

            }
        });

        buttons[4].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (current != 4) {
                    buttons[current].setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.grey));
                    current = 4;
                    buttons[4].setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                    startActivity(new Intent(getActivity(), EditContact.class).putExtra(ARG_ID, id));
                    if(dualMode){
                        buttons[0].performClick();
                    }else {
                        getActivity().finish();
                    }
                }

            }
        });


        return root;
    }
}