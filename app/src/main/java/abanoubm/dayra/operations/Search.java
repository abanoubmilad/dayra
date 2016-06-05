package abanoubm.dayra.operations;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import abanoubm.dayra.R;
import abanoubm.dayra.main.Utility;

public class Search extends ActionBarActivity {
    private int current = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_search);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));

        final ImageView[] buttons     = new ImageView[]{
                (ImageView) findViewById(R.id.img1),
                (ImageView) findViewById(R.id.img2),
                (ImageView) findViewById(R.id.img3),
        };

        buttons[0].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (current != 0) {
                    buttons[current].setBackgroundColor(0);
                    current = 0;
                    buttons[0].setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                    ((TextView) findViewById(R.id.subhead2))
                            .setText(R.string.subhead_search);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment, new FragmentSearch())
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
                    buttons[1].setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                    ((TextView) findViewById(R.id.subhead2))
                            .setText(R.string.subhead_search_dates);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment, new FragmentSearchDates())
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
                    ((TextView) findViewById(R.id.subhead2))
                            .setText(R.string.subhead_search_bdays);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment, new FragmentSearchBirthdays())
                            .commit();

                }

            }
        });


    }
}
