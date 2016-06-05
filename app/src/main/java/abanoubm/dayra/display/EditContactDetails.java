package abanoubm.dayra.display;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import abanoubm.dayra.R;
import abanoubm.dayra.main.Utility;

public class EditContactDetails extends ActionBarActivity {
    private String id = "-1";
    private int current = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_edit_contact);

        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        ((TextView) findViewById(R.id.subhead2)).setText(R.string.subhead_edit_contact);
        final ImageView[] buttons = new ImageView[]{
                (ImageView) findViewById(R.id.img1),
                (ImageView) findViewById(R.id.img2),
                (ImageView) findViewById(R.id.img3),
                (ImageView) findViewById(R.id.img4)
        };
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString("id", getIntent().getStringExtra("id"));

            FragmentEditContactInfo fragment = new FragmentEditContactInfo();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, fragment)
                    .commit();

        }

        buttons[0].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (current != 0) {
                    buttons[current].setBackgroundColor(0);
                    current = 0;
                    buttons[0].setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));

                    Bundle arguments = new Bundle();
                    arguments.putString("id", id);

                    FragmentEditContactInfo fragment = new FragmentEditContactInfo();
                    fragment.setArguments(arguments);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment, fragment)
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
                    buttons[1].setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorAccent));

                    Bundle arguments = new Bundle();
                    arguments.putString("id", id);

                    FragmentEditContactDay fragment = new FragmentEditContactDay();
                    fragment.setArguments(arguments);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment, fragment)
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
                    buttons[2].setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));


                    Bundle arguments = new Bundle();
                    arguments.putString("id", id);

                    FragmentEditContactConnection fragment = new FragmentEditContactConnection();
                    fragment.setArguments(arguments);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment, fragment)
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
                    buttons[3].setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));


                    Bundle arguments = new Bundle();
                    arguments.putString("id", id);

                    FragmentEditContactMap fragment = new FragmentEditContactMap();
                    fragment.setArguments(arguments);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment, fragment)
                            .commit();
                }

            }
        });

    }


}
