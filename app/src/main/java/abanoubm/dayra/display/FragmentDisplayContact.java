package abanoubm.dayra.display;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import abanoubm.dayra.R;

public class FragmentDisplayContact extends Fragment {
    private int id = -1;
    int current = 0;
    private ImageView infoImage, connImage, locImage, daysImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            id = arguments.getInt("id");
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

        infoImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (current != 0) {
                    current = 0;
                    infoImage.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    connImage.setBackgroundColor(0);
                    locImage.setBackgroundColor(0);
                    daysImage.setBackgroundColor(0);

                    Bundle arguments = new Bundle();
                    arguments.putInt("id",id);

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
                    connImage.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    locImage.setBackgroundColor(0);
                    daysImage.setBackgroundColor(0);

                    Bundle arguments = new Bundle();
                    arguments.putInt("id",id);

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
                    locImage.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    daysImage.setBackgroundColor(0);

                    Bundle arguments = new Bundle();
                    arguments.putInt("id",id);

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
                    daysImage.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                    Bundle arguments = new Bundle();
                    arguments.putInt("id",id);

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
            arguments.putInt("id",id);

            FragmentDisplayContactInfo fragment = new FragmentDisplayContactInfo();
            fragment.setArguments(arguments);

            getChildFragmentManager().beginTransaction()
                    .replace(R.id.display_contact_fragment, fragment)
                    .commit();

        }
        return root;
    }
}