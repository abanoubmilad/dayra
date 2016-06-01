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
import abanoubm.dayra.operations.EditContact;

public class FragmentDisplayContactDetails extends Fragment {
    private String id = "-1";
    private int current = 0;
    private ImageView infoImage, connImage, locImage, daysImage;

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

        infoImage = (ImageView) root.findViewById(R.id.infoImage);
        connImage = (ImageView) root.findViewById(R.id.connImage);
        locImage = (ImageView) root.findViewById(R.id.locImage);
        daysImage = (ImageView) root.findViewById(R.id.daysImage);

        root.findViewById(R.id.editImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditContact.class).putExtra("id", id));
            }
        });
        infoImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (current != 0) {
                    current = 0;
                    infoImage.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                    connImage.setBackgroundColor(0);
                    locImage.setBackgroundColor(0);
                    daysImage.setBackgroundColor(0);

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
        connImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (current != 1) {
                    current = 1;
                    infoImage.setBackgroundColor(0);
                    connImage.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                    locImage.setBackgroundColor(0);
                    daysImage.setBackgroundColor(0);

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
        locImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (current != 2) {
                    current = 2;
                    infoImage.setBackgroundColor(0);
                    connImage.setBackgroundColor(0);
                    locImage.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                    daysImage.setBackgroundColor(0);

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
        daysImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (current != 3) {
                    current = 3;
                    infoImage.setBackgroundColor(0);
                    connImage.setBackgroundColor(0);
                    locImage.setBackgroundColor(0);
                    daysImage.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));

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