package abanoubm.dayra.contacts;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import abanoubm.dayra.R;
import abanoubm.dayra.contact.DisplayContact;
import abanoubm.dayra.contact.FragmentDisplayContact;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Main;
import abanoubm.dayra.main.Utility;

public class DisplayContacts extends AppCompatActivity implements CallBack {

    private boolean dualMode;
    private static final String ARG_ID = "id";
    private static final String ARG_DUAL_MODE = "dual";
    private DrawerLayout nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Utility.getArabicLang(getApplicationContext()) == 1) {
            Utility.setArabicLang(getApplicationContext(), 2);

            Locale myLocale = new Locale("ar");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);

            finish();
            startActivity(new Intent(getIntent()));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_display_contacts);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
       // ((TextView) findViewById(R.id.subhead2)).setText(R.string.subhead_display_contacts);

        nav = (DrawerLayout)findViewById(R.id.drawer_layout);
        findViewById(R.id.nav_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

    nav.openDrawer(Gravity.RIGHT);


            }
        });
        findViewById(R.id.nav_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

onBackPressed();

            }
        });
        dualMode = findViewById(R.id.display_contacts_fragment_dual) != null;

        if (savedInstanceState == null) {
            Bundle args = new Bundle();
            args.putBoolean(ARG_DUAL_MODE, dualMode);
            FragmentDisplayContacts fragment = new FragmentDisplayContacts();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.display_contacts_fragment, fragment)
                    .commit();



            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.menu_fragment, new FragmentMenu())
                    .commit();
        }


    }

    @Override
    public void notify(String id) {
        if (dualMode) {
            Bundle args = new Bundle();
            args.putString(ARG_ID, id);
            args.putBoolean(ARG_DUAL_MODE, true);

            FragmentDisplayContact fragment = new FragmentDisplayContact();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.display_contact_fragment, fragment)
                    .commit();

        } else {
            startActivity(new Intent(this, DisplayContact.class).putExtra(ARG_ID, id));
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DB.getInstant(getApplicationContext()).closeDB();
        Utility.clearLogin(getApplicationContext());
        Intent intent = new Intent(getApplicationContext(), Main.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Utility.getArabicLang(getApplicationContext()) != 0)
            Utility.setArabicLang(getApplicationContext(), 1);
    }

    public void closeNav(){
        nav.closeDrawers();

    }

}
