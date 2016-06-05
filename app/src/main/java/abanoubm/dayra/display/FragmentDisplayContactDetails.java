package abanoubm.dayra.display;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import abanoubm.dayra.R;

public class FragmentDisplayContactDetails extends Fragment {
    private String id = "-1";
    private int current = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            id = arguments.getString("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_display_contact, container, false);

        final ImageView[] buttons

         = new ImageView[]{
                (ImageView) root.findViewById(R.id.img1),
                (ImageView) root.findViewById(R.id.img2),
                (ImageView) root.findViewById(R.id.img3),
                (ImageView) root.findViewById(R.id.img4),
                (ImageView) root.findViewById(R.id.img5)
        };

        buttons[0].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (current != 0) {
                    buttons[current].setBackgroundColor(0);
                    current = 0;
                    buttons[0].setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));

                    Bundle arguments = new Bundle();
                    arguments.putString("id", id);

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
                    buttons[current].setBackgroundColor(0);
                    current = 1;
                    buttons[1].setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));

                    Bundle arguments = new Bundle();
                    arguments.putString("id", id);

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
                    buttons[current].setBackgroundColor(0);
                    current = 2;
                    buttons[2].setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));


                    Bundle arguments = new Bundle();
                    arguments.putString("id", id);

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
                    buttons[current].setBackgroundColor(0);
                    current = 3;
                    buttons[3].setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));


                    Bundle arguments = new Bundle();
                    arguments.putString("id", id);

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
                    buttons[current].setBackgroundColor(0);
                    current = 4;
                    buttons[4].setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                    startActivity(new Intent(getActivity(), EditContactDetails.class).putExtra("id", id));

                }

            }
        });


        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString("id", id);

            FragmentDisplayContactInfo fragment = new FragmentDisplayContactInfo();
            fragment.setArguments(arguments);

            getChildFragmentManager().beginTransaction()
                    .replace(R.id.display_contact_fragment, fragment)
                    .commit();

        }
        return root;
    }
}