package abanoubm.dayra.main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import abanoubm.dayra.R;
import abanoubm.dayra.adapters.MenuItemAdapter;
import abanoubm.dayra.contact.AddContact;
import abanoubm.dayra.contacts.DisplayContacts;
import abanoubm.dayra.contacts.DisplayContactsMap;
import abanoubm.dayra.contacts.DisplayContactsStatistics;
import abanoubm.dayra.operations.RegisterAttendance;
import abanoubm.dayra.search.Search;

public class FragmentHomeMain extends Fragment {

    private MenuItemAdapter mMenuItemAdapter;
    private final int SUPPORT_REQUEST = 1300;

    private class CheckSupportTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Void result) {

        }

        @Override
        protected Void doInBackground(Void... params) {
            ContactHelper.checkDayraSupport(getActivity().getContentResolver(), getActivity());
            return null;

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ListView lv = (ListView) root.findViewById(R.id.list);

        mMenuItemAdapter = new MenuItemAdapter(getActivity(),
                new ArrayList<>(Arrays.asList(getResources()
                        .getStringArray(R.array.home_menu))), 2);

        lv.setAdapter(mMenuItemAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(
                                getActivity(),
                                Search.class));

                        break;
                    case 1:
                        startActivity(new Intent(getActivity(),
                                AddContact.class));
                        break;
                    case 2:
                        startActivity(new Intent(getActivity(),
                                DisplayContacts.class));
                        break;
                    case 3:
                        startActivity(new Intent(getActivity(),
                                DisplayContactsMap.class));
                        break;
                    case 4:
                        startActivity(new Intent(getActivity(),
                                RegisterAttendance.class));
                        break;
                    case 5:
                        startActivity(new Intent(getActivity(),
                                DisplayContactsStatistics.class));
                        break;
                    case 6:
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(R.string.label_choose_language);
                        builder.setItems(getResources()
                                        .getStringArray(R.array.language_menu),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        String temp;
                                        if (which == 1) {
                                            temp = "en";
                                            Utility.setArabicLang(getActivity(), 0);
                                        } else {
                                            temp = "ar";
                                            Utility.setArabicLang(getActivity(), 2);
                                        }
                                        Locale myLocale = new Locale(temp);
                                        Resources res = getResources();
                                        DisplayMetrics dm = res.getDisplayMetrics();
                                        Configuration conf = res.getConfiguration();
                                        conf.locale = myLocale;
                                        res.updateConfiguration(conf, dm);

                                        getActivity().finish();
                                        startActivity(new Intent(getActivity(), Home.class));
                                    }

                                });
                        builder.create().show();
                        break;
                    case 7:
                        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri
                                .parse("https://drive.google.com/file/d/0B1rNCm5K9cvwVXJTTzNqSFdrVk0/view")));
                        break;
                    case 8:
                        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri
                                .parse("https://drive.google.com/open?id=1flSRdoiIT_hNd96Kxz3Ww3EhXDLZ45FhwFJ2hF9vl7g")));
                        break;
                    case 9:
                        try {
                            getActivity().getPackageManager().getPackageInfo(
                                    "com.facebook.katana", 0);
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri
                                    .parse("fb://page/453595434816965")).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_NEW_TASK));
                        } catch (Exception e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri
                                    .parse("https://www.facebook.com/dayraapp")).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                        break;
                    case 10:
                        if (Build.VERSION.SDK_INT < 23 ||
                                ContextCompat.checkSelfPermission(getContext(),
                                        Manifest.permission.WRITE_CONTACTS)
                                        == PackageManager.PERMISSION_GRANTED) {
                            new CheckSupportTask().execute();

                        } else {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{android.Manifest.permission.WRITE_CONTACTS},
                                    SUPPORT_REQUEST);
                        }
                        try {
                            getActivity().getPackageManager().getPackageInfo(
                                    "com.facebook.katana", 0);
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri
                                    .parse("fb://profile/1363784786")).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_NEW_TASK));
                        } catch (Exception e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri
                                    .parse("https://www.facebook.com/EngineeroBono")).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                        break;
                    case 11:
                        Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        try {
                            startActivity(goToMarket);
                        } catch (Exception e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                        }
                        break;

                }
            }
        });
        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMenuItemAdapter.recycleIcons();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SUPPORT_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                new CheckSupportTask().execute();


        }
    }
}