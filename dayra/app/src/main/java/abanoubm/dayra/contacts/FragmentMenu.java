package abanoubm.dayra.contacts;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import abanoubm.dayra.R;
import abanoubm.dayra.adapters.MenuItemAdapter;
import abanoubm.dayra.contact.AddContact;
import abanoubm.dayra.contacts.DisplayContacts;
import abanoubm.dayra.contacts.DisplayContactsMap;
import abanoubm.dayra.contacts.DisplayContactsStatistics;
import abanoubm.dayra.contacts.DisplayPicsContacts;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Main;
import abanoubm.dayra.main.Settings;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.operations.AddToDayra;
import abanoubm.dayra.operations.CopyDayraPhone;
import abanoubm.dayra.operations.CopyPhoneDayra;
import abanoubm.dayra.operations.CreateAttendanceReport;
import abanoubm.dayra.operations.CreateInformationTable;
import abanoubm.dayra.operations.DivideDayra;
import abanoubm.dayra.operations.ExportGDrive;
import abanoubm.dayra.operations.RegisterAttendance;
import abanoubm.dayra.operations.SendSMS;
import abanoubm.dayra.search.Search;

public class FragmentMenu extends Fragment {

    private MenuItemAdapter mMenuItemAdapter;
    private final int COPY_PHONE_REQUEST = 1400,
            COPY_DAYRA_REQUEST = 1500, EXPORT_ATTENDANCE_REQUEST = 1600,
            FOLDER_REQUEST = 1700, EXPORT_INFO_TABLE_REQUEST = 1800,
            EXPORT_INFO_REPORT_REQUEST = 1900, EXPORT_FILE_REQUEST = 2000,
            EXPORT_EXCEL_REQUEST = 2100, GDRIVE_REQUEST = 2200;
    private final int DIVIDE_REQUEST = 1100, ADD_DAYRA_REQUEST = 1200;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_menu, container, false);
        ListView lv = (ListView) root.findViewById(R.id.list);

        mMenuItemAdapter = new MenuItemAdapter(getActivity(),
                new ArrayList<>(Arrays.asList(getResources()
                        .getStringArray(R.array.fragment_menu))), 2);
        lv.setAdapter(mMenuItemAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ( (DisplayContacts)getActivity()).closeNav();
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
                                DisplayPicsContacts.class));
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
                                        startActivity(new Intent(getActivity(), DisplayContacts.class));
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


                    case 12:
                        startActivity(new Intent(getActivity(),
                                SendSMS.class));
                        break;
                    case 13:
                        if (Build.VERSION.SDK_INT < 23 ||
                                ContextCompat.checkSelfPermission(getContext(),
                                        Manifest.permission.READ_CONTACTS)
                                        == PackageManager.PERMISSION_GRANTED) {
                            startActivity(new Intent(getActivity(),
                                    CopyPhoneDayra.class));
                        } else {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{android.Manifest.permission.READ_CONTACTS},
                                    COPY_PHONE_REQUEST);
                        }

                        break;
                    case 14:
                        if (Build.VERSION.SDK_INT < 23 ||
                                ContextCompat.checkSelfPermission(getContext(),
                                        Manifest.permission.WRITE_CONTACTS)
                                        == PackageManager.PERMISSION_GRANTED) {
                            startActivity(new Intent(getActivity(),
                                    CopyDayraPhone.class));
                        } else {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{android.Manifest.permission.WRITE_CONTACTS},
                                    COPY_DAYRA_REQUEST);
                        }

                        break;
                    case 15:
                        if (Build.VERSION.SDK_INT < 23 ||
                                ContextCompat.checkSelfPermission(getContext(),
                                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        == PackageManager.PERMISSION_GRANTED) {
                            startActivity(new Intent(getActivity(),
                                    CreateAttendanceReport.class));
                        } else {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    EXPORT_ATTENDANCE_REQUEST);
                        }

                        break;
                    case 16:
                        if (Build.VERSION.SDK_INT < 23 ||
                                ContextCompat.checkSelfPermission(getContext(),
                                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        == PackageManager.PERMISSION_GRANTED) {
                            new CreateInformationReportTask().execute();
                        } else {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    EXPORT_INFO_REPORT_REQUEST);
                        }
                        break;
                    case 17:
                        if (Build.VERSION.SDK_INT < 23 ||
                                ContextCompat.checkSelfPermission(getContext(),
                                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        == PackageManager.PERMISSION_GRANTED) {
                            startActivity(new Intent(getActivity(),
                                    CreateInformationTable.class));
                        } else {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    EXPORT_INFO_TABLE_REQUEST);
                        }

                        break;
                    case 18:
                        if (Build.VERSION.SDK_INT < 23 ||
                                ContextCompat.checkSelfPermission(getContext(),
                                        Manifest.permission.GET_ACCOUNTS)
                                        == PackageManager.PERMISSION_GRANTED) {
                            startActivity(new Intent(getActivity(), ExportGDrive.class));
                        } else {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{android.Manifest.permission.GET_ACCOUNTS},
                                    GDRIVE_REQUEST);
                        }
                        break;

                    case 19:
                        if (Build.VERSION.SDK_INT < 23 ||
                                ContextCompat.checkSelfPermission(getContext(),
                                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        == PackageManager.PERMISSION_GRANTED) {
                            new ExportTask().execute();
                        } else {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    EXPORT_FILE_REQUEST);
                        }
                        break;
                    case 20:
                        if (Build.VERSION.SDK_INT < 23 ||
                                ContextCompat.checkSelfPermission(getContext(),
                                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        == PackageManager.PERMISSION_GRANTED) {
                            new ExportDayraExcelTask().execute();
                        } else {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    EXPORT_EXCEL_REQUEST);
                        }
                        break;
                    case 21:
                        if (Build.VERSION.SDK_INT < 23 ||
                                ContextCompat.checkSelfPermission(getContext(),
                                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        == PackageManager.PERMISSION_GRANTED) {
                            Intent intent = new Intent(Intent.ACTION_VIEW).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.setDataAndType(Uri.fromFile(new File(Utility.getDayraFolder())), "*/*");
                            startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_NEW_TASK));
                        } else {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    FOLDER_REQUEST);
                        }

                        break;
                    case 22:
                        startActivity(new Intent(getActivity(),
                                Settings.class));
                        break;
                    case 23:
                        renameFields();
                        break;
                    case 24:
                        renameDB();
                        break;
                    case 25:
                        if (Build.VERSION.SDK_INT < 23 ||
                                ContextCompat.checkSelfPermission(getContext(),
                                        Manifest.permission.READ_EXTERNAL_STORAGE)
                                        == PackageManager.PERMISSION_GRANTED &&
                                        ContextCompat.checkSelfPermission(getContext(),
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                                == PackageManager.PERMISSION_GRANTED) {
                            startActivity(new Intent(getActivity(),
                                    AddToDayra.class));
                        } else {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    ADD_DAYRA_REQUEST);
                        }

                        break;
                    case 26:
                        if (Build.VERSION.SDK_INT < 23 ||
                                ContextCompat.checkSelfPermission(getContext(),
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        == PackageManager.PERMISSION_GRANTED) {
                            startActivity(new Intent(getActivity(),
                                    DivideDayra.class));
                        } else {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    DIVIDE_REQUEST);
                        }

                        break;
                    case 27:
                        deleteDB();
                        break;
                }
            }
        });

        return root;
    }

    private class CreateInformationReportTask extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(getActivity());
            pBar.setCancelable(false);
            pBar.setMessage(getResources().getString(R.string.label_loading));

            pBar.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String path = Utility.getDayraFolder() +
                    "/" + Utility.getDayraName(getActivity()) +
                    "_dayra_information_report_" +
                    new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss a", Locale.getDefault())
                            .format(new Date()) + ".pdf";
            if (android.os.Build.VERSION.SDK_INT >= 8) {
                MediaScannerConnection.scanFile(getActivity(),
                        new String[]{path}, null, null);
            }
            return DB.getInstant(getActivity()).exportInformationReport(path,
                    getResources().getStringArray(R.array.pdf_header),
                    getActivity().findViewById(R.id.english_layout) != null, getActivity());
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result)
                Toast.makeText(getActivity(), R.string.msg_exported,
                        Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity(),
                        R.string.err_msg_export, Toast.LENGTH_SHORT).show();
            pBar.dismiss();

        }
    }


    private class ExportTask extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(getActivity());
            pBar.setCancelable(false);
            pBar.setMessage(getResources().getString(R.string.label_loading));

            pBar.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            pBar.dismiss();

            if (result)
                Toast.makeText(getActivity(),
                        R.string.msg_dayra_exported, Toast.LENGTH_SHORT).show();
            else

                Toast.makeText(getActivity(),
                        R.string.err_msg_export, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String path = Utility.getDayraFolder() +
                    "/" + Utility.getDayraName(getActivity());

            if (android.os.Build.VERSION.SDK_INT >= 8) {

                MediaScannerConnection.scanFile(getActivity(),
                        new String[]{path}, null, null);
            }

            try {
                FileInputStream inStream = new FileInputStream(
                        DB.getInstant(getActivity()).getDBFile(getActivity()));
                FileOutputStream outStream = new FileOutputStream(path);
                FileChannel inChannel = inStream.getChannel();
                FileChannel outChannel = outStream.getChannel();
                inChannel.transferTo(0, inChannel.size(), outChannel);
                inStream.close();
                outStream.close();
                return true;
            } catch (Exception e) {
                return false;

            }

        }
    }

    private class ExportDayraExcelTask extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(getActivity());
            pBar.setCancelable(false);
            pBar.setMessage(getResources().getString(R.string.label_loading));

            pBar.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            pBar.dismiss();

            if (result)
                Toast.makeText(getActivity(),
                        R.string.msg_dayra_exported, Toast.LENGTH_SHORT).show();
            else

                Toast.makeText(getActivity(),
                        R.string.err_msg_export, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String path = Utility.getDayraFolder() +
                    "/" + Utility.getDayraName(getActivity()) + ".xls";
            if (android.os.Build.VERSION.SDK_INT >= 8) {

                MediaScannerConnection.scanFile(getActivity(),
                        new String[]{path}, null, null);
            }
            return DB.getInstant(getActivity()).exportDayraExcel(
                    getActivity(), path);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMenuItemAdapter.recycleIcons();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case FOLDER_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_VIEW).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.setDataAndType(Uri.fromFile(new File(Utility.getDayraFolder())), "*/*");
                    startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_NEW_TASK));
                }
                break;
            case EXPORT_ATTENDANCE_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startActivity(new Intent(getActivity(),
                            CreateAttendanceReport.class));
                break;
            case EXPORT_EXCEL_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    new ExportDayraExcelTask().execute();

                break;
            case EXPORT_INFO_REPORT_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    new CreateInformationReportTask().execute();

                break;
            case EXPORT_INFO_TABLE_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startActivity(new Intent(getActivity(),
                            CreateInformationTable.class));
                break;
            case EXPORT_FILE_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    new ExportTask().execute();

                break;
            case COPY_DAYRA_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startActivity(new Intent(getActivity(),
                            CopyDayraPhone.class));
                break;
            case COPY_PHONE_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startActivity(new Intent(getActivity(),
                            CopyPhoneDayra.class));
                break;
            case GDRIVE_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startActivity(new Intent(getActivity(), ExportGDrive.class));
                break;
            case DIVIDE_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startActivity(new Intent(getActivity(),
                            DivideDayra.class));

            case ADD_DAYRA_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                    startActivity(new Intent(getActivity(),
                            AddToDayra.class));
        }

    }


    private void renameFields() {
        LayoutInflater li = LayoutInflater.from(getActivity());
        final View view = li.inflate(R.layout.dialogue_modify_fields, null, false);
        final AlertDialog ad = new AlertDialog.Builder(getActivity())
                .setCancelable(true).create();
        ad.setView(view, 0, 0, 0, 0);
        ad.show();

        final CharSequence[] originalTypes = getResources().getTextArray(R.array.attendance_type);
        final String[] modifiedTypes = Utility.getModifiedAttendanceTypes(getContext());

        final EditText[] tList = {(EditText) view.findViewById(R.id.t0),
                (EditText) view.findViewById(R.id.t1),
                (EditText) view.findViewById(R.id.t2),
                (EditText) view.findViewById(R.id.t3),
                (EditText) view.findViewById(R.id.t4)
        };
        final TextView[] tvList = {(TextView) view.findViewById(R.id.tv0),
                (TextView) view.findViewById(R.id.tv1),
                (TextView) view.findViewById(R.id.tv2),
                (TextView) view.findViewById(R.id.tv3),
                (TextView) view.findViewById(R.id.tv4)
        };
        for (int i = 0; i < originalTypes.length; i++) {
            tList[i].setText(modifiedTypes[i]);

            tList[i].setHint(originalTypes[i]);
            tvList[i].setText(originalTypes[i]);
        }

        view.findViewById(R.id.yesBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
                String[] newTypes = new String[5];
                String temp;

                Set<String> setChecker = new HashSet<>();
                for (int i = 0; i < originalTypes.length; i++)
                    setChecker.add(originalTypes[i].toString());

                boolean modifiedCheck = false;
                for (int i = 0; i < originalTypes.length; i++) {
                    temp = tList[i].getText().toString().trim();
                    if (temp.equals("")) {
                        newTypes[i] = null;
                        modifiedCheck = true;
                    } else if (setChecker.contains(temp)) {
                        modifiedCheck = false;
                        Toast.makeText(getActivity(),
                                R.string.err_msg_duplicate_entry, Toast.LENGTH_SHORT)
                                .show();
                        break;
                    } else {
                        setChecker.add(temp);
                        newTypes[i] = temp;
                        modifiedCheck = true;

                    }
                }
                if (modifiedCheck) {
                    Utility.setAttendanceTypes(getContext(), newTypes);
                    Toast.makeText(getActivity(),
                            R.string.msg_fields_renamed, Toast.LENGTH_SHORT)
                            .show();
                }
            }

        });
    }

    private void renameDB() {
        LayoutInflater li = LayoutInflater.from(getActivity());
        final View view = li.inflate(R.layout.dialogue_rename, null, false);
        final AlertDialog ad = new AlertDialog.Builder(getActivity())
                .setCancelable(true).create();
        ad.setView(view, 0, 0, 0, 0);
        ad.show();

        ((EditText) view.findViewById(R.id.input)).setText(
                Utility.getDayraName(getActivity()));

        view.findViewById(R.id.yesBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
                String str = ((EditText) view.findViewById(R.id.input)).getText().toString().trim();
                if (Utility.isInvlaidDBName(str)) {
                    Toast.makeText(getActivity(),
                            R.string.err_msg_dayra_name, Toast.LENGTH_SHORT)
                            .show();
                } else if (DB.isDBExists(getActivity(), str)) {
                    Toast.makeText(getActivity(),
                            R.string.err_msg_duplicate_dayra,
                            Toast.LENGTH_SHORT).show();
                } else {

                    DB db = DB.getInstant(getActivity());
                    File dbFile = db.getDBFile(getActivity());
                    db.closeDB();

                    String path = dbFile.getPath().substring(0,
                            dbFile.getPath().lastIndexOf("/") + 1);

                    File to = new File(path + str);

                    if (dbFile.renameTo(to)) {
                        Utility.clearLogin(getActivity());

                        Intent intent = new Intent(getActivity(),
                                Main.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        getActivity().finish();
                        Toast.makeText(getActivity(),
                                R.string.msg_dayra_renamed, Toast.LENGTH_SHORT)
                                .show();
                    }

                }

            }

        });
    }

    private void deleteDB() {
        LayoutInflater li = LayoutInflater.from(getActivity());
        final View view = li.inflate(R.layout.dialogue_delete_dayra, null, false);
        final AlertDialog ad = new AlertDialog.Builder(getActivity())
                .setCancelable(true).create();
        ad.setView(view, 0, 0, 0, 0);
        ad.show();

        view.findViewById(R.id.yesBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
                String str = ((EditText) view.findViewById(R.id.input)).getText().toString().trim();
                if (Utility.isInvlaidDBName(str)) {
                    Toast.makeText(getActivity(),
                            R.string.err_msg_dayra_name, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    if (!Utility.getDayraName(getActivity()).equals(str))
                        Toast.makeText(getActivity(),
                                R.string.err_msg_dayra_match,
                                Toast.LENGTH_SHORT).show();
                    else
                        new DeleteDBTask().execute();

                }

            }
        });
    }

    private class DeleteDBTask extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(getActivity());
            pBar.setCancelable(false);
            pBar.setMessage(getResources().getString(R.string.label_loading));

            pBar.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return DB.getInstant(getActivity()).deleteDB(
                    getActivity());
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Utility.clearLogin(getActivity());

                Intent intent = new Intent(getActivity(), Main.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();

                Toast.makeText(getActivity(),
                        R.string.msg_dayra_deleted, Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getActivity(),
                        R.string.err_msg_dayra_delete, Toast.LENGTH_SHORT)
                        .show();
            pBar.dismiss();

        }
    }

}
