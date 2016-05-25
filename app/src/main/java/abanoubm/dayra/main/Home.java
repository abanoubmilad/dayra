package abanoubm.dayra.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;

import abanoubm.dayra.R;
import abanoubm.dayra.adapt.MenuItemAdapter;
import abanoubm.dayra.display.DisplayContacts;
import abanoubm.dayra.display.DisplayContactsStatis;
import abanoubm.dayra.display.MapLocations;
import abanoubm.dayra.opr.AddContact;
import abanoubm.dayra.opr.CopyDayraPhone;
import abanoubm.dayra.opr.CopyPhoneDayra;
import abanoubm.dayra.opr.DivideDayra;
import abanoubm.dayra.opr.ExportContacts;
import abanoubm.dayra.opr.RegisterAttendance;
import abanoubm.dayra.opr.ReplaceDayra;
import abanoubm.dayra.opr.SearchBDay;
import abanoubm.dayra.opr.SearchDate;
import abanoubm.dayra.opr.SearchName;
import abanoubm.dayra.opr.SendSMS;

public class Home extends Activity {
    private ProgressDialog pBar;
    private MenuItemAdapter mMenuItemAdapter;
    private ListView lv;
    private int tagCursor;
    private TextView subHead2;
    private ImageView homeImage, ioImage, settImage;

    @Override
    public void onBackPressed() {
        getSharedPreferences("login", Context.MODE_PRIVATE).edit()
                .putString("dbname", null).commit();
        Intent intent = new Intent(getApplicationContext(), Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));

        subHead2 = (TextView) findViewById(R.id.subhead2);
        homeImage = (ImageView) findViewById(R.id.homeImage);
        ioImage = (ImageView) findViewById(R.id.ioImage);
        settImage = (ImageView) findViewById(R.id.settImage);

        homeImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (tagCursor != 1)
                    fireHomeMenu();

            }
        });
        ioImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (tagCursor != 2)
                    fireOutMenu();

            }
        });
        settImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (tagCursor != 3)
                    fireSettingsMenu();

            }
        });

        lv = (ListView) findViewById(R.id.home_list);
        fireHomeMenu();

    }

    private void fireHomeMenu() {
        tagCursor = 1;
        homeImage.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        ioImage.setBackgroundColor(0);
        settImage.setBackgroundColor(0);

        subHead2.setText(R.string.label_home_main);

        if (mMenuItemAdapter != null)
            mMenuItemAdapter.recycleIcons();
        mMenuItemAdapter = new MenuItemAdapter(getApplicationContext(),
                new ArrayList<String>(Arrays.asList(getResources()
                        .getStringArray(R.array.home_menu))), 2);
        lv.setAdapter(mMenuItemAdapter);

        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0: {

                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                Home.this);
                        builder.setTitle(R.string.label_choose_search);
                        builder.setItems((CharSequence[]) getResources()
                                        .getStringArray(R.array.search_menu),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        switch (which) {
                                            case 0:
                                                startActivity(new Intent(
                                                        getApplicationContext(),
                                                        SearchName.class));
                                                break;
                                            case 1:
                                                startActivity(new Intent(
                                                        getApplicationContext(),
                                                        SearchDate.class).putExtra(
                                                        "sf", 1));
                                                break;
                                            case 2:
                                                startActivity(new Intent(
                                                        getApplicationContext(),
                                                        SearchDate.class).putExtra(
                                                        "sf", 3));
                                                break;
                                            case 3:
                                                startActivity(new Intent(
                                                        getApplicationContext(),
                                                        SearchDate.class).putExtra(
                                                        "sf", 2));
                                                break;
                                            case 4:
                                                startActivity(new Intent(
                                                        getApplicationContext(),
                                                        SearchDate.class).putExtra(
                                                        "sf", 4));
                                                break;

                                            default:
                                                startActivity(new Intent(
                                                        getApplicationContext(),
                                                        SearchBDay.class));
                                                break;
                                        }

                                    }

                                });
                        builder.create().show();

                    }
                    break;
                    case 1:
                        startActivity(new Intent(getApplicationContext(),
                                AddContact.class));
                        break;
                    case 2:
                        startActivity(new Intent(getApplicationContext(),
                                DisplayContacts.class));
                        break;
                    case 3:
                        startActivity(new Intent(getApplicationContext(),
                                MapLocations.class));
                        break;
                    case 4:
                        startActivity(new Intent(getApplicationContext(),
                                RegisterAttendance.class));
                        break;
                    case 5:
                        startActivity(new Intent(getApplicationContext(),
                                DisplayContactsStatis.class));
                        break;
                    case 6:
                        startActivity(new Intent(getApplicationContext(),
                                SendSMS.class));
                        break;

                    case 7:
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri
                                .parse("https://drive.google.com/file/d/0B1rNCm5K9cvwVXJTTzNqSFdrVk0/view"));
                        startActivity(i);
                        break;
                    case 8:
                        try {
                            getPackageManager().getPackageInfo(
                                    "com.facebook.katana", 0);
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri
                                    .parse("fb://page/453595434816965")));
                        } catch (Exception e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri
                                    .parse("https://www.facebook.com/dayraapp")));
                        }
                        break;
                    default:
                        try {
                            getPackageManager().getPackageInfo(
                                    "com.facebook.katana", 0);
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri
                                    .parse("fb://profile/1363784786")));
                        } catch (Exception e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri
                                    .parse("https://www.facebook.com/EngineeroBono")));
                        }
                        break;

                }
            }
        });
    }

    private void fireOutMenu() {

        tagCursor = 2;
        ioImage.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        homeImage.setBackgroundColor(0);
        settImage.setBackgroundColor(0);

        subHead2.setText(R.string.label_home_out);
        if (mMenuItemAdapter != null)
            mMenuItemAdapter.recycleIcons();
        mMenuItemAdapter = new MenuItemAdapter(getApplicationContext(),
                new ArrayList<String>(Arrays.asList(getResources()
                        .getStringArray(R.array.out_menu))), 3);
        lv.setAdapter(mMenuItemAdapter);

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {

                    case 0:
                        startActivity(new Intent(getApplicationContext(),
                                CopyPhoneDayra.class));
                        break;
                    case 1:
                        startActivity(new Intent(getApplicationContext(),
                                CopyDayraPhone.class));
                        break;
                    case 2:
                        new ExportReportTask().execute();
                        break;
                    case 3:
                        startActivity(new Intent(getApplicationContext(),
                                ExportContacts.class));
                        break;
                    case 4:
                        new ExportTask().execute();
                        break;
                    case 5:
                        new ExportDayraExcelTask().execute();
                        break;
                }
            }
        });
    }

    private void fireSettingsMenu() {

        tagCursor = 3;
        settImage.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        homeImage.setBackgroundColor(0);
        ioImage.setBackgroundColor(0);

        subHead2.setText(R.string.label_home_settings);
        if (mMenuItemAdapter != null)
            mMenuItemAdapter.recycleIcons();
        mMenuItemAdapter = new MenuItemAdapter(getApplicationContext(),
                new ArrayList<String>(Arrays.asList(getResources()
                        .getStringArray(R.array.settings_menu))), 4);
        lv.setAdapter(mMenuItemAdapter);

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {

                    case 0:
                        startActivity(new Intent(getApplicationContext(),
                                ReplaceDayra.class));
                        break;
                    case 1:
                        startActivity(new Intent(getApplicationContext(),
                                DivideDayra.class));
                        break;
                    case 2:
                        renameDB();
                        break;
                    case 3:
                        deleteDB();
                        break;

                }
            }
        });
    }

    private void renameDB() {
        LayoutInflater li = LayoutInflater.from(getApplicationContext());
        View chooseView = li.inflate(R.layout.confirm_dbname, null);
        final AlertDialog ad = new AlertDialog.Builder(Home.this)
                .setCancelable(true).create();
        ad.setView(chooseView, 0, 0, 0, 0);

        ad.show();
        TextView confirm = (TextView) chooseView.findViewById(R.id.confirmbtn);
        TextView back = (TextView) chooseView.findViewById(R.id.back);
        final EditText name = (EditText) chooseView.findViewById(R.id.name);
        final String nameStr = getSharedPreferences("login",
                Context.MODE_PRIVATE).getString("dbname", "");
        name.setText(nameStr);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
        confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
                String entered = name.getText().toString().trim();
                if (!Utility.isDBName(entered)) {
                    Toast.makeText(getApplicationContext(),
                            R.string.err_msg_dayra_name, Toast.LENGTH_SHORT)
                            .show();
                } else if (!DB.isDBExists(getApplicationContext(), entered)) {
                    File dbFile = DB.getInstance(getApplicationContext(),
                            nameStr).getDBFile(getApplicationContext());
                    String path = dbFile.getPath().substring(0,
                            dbFile.getPath().lastIndexOf("/") + 1);

                    File to = new File(path + entered);

                    if (dbFile.renameTo(to)) {
                        getSharedPreferences("login", Context.MODE_PRIVATE)
                                .edit().putString("dbname", null).commit();
                        Intent intent = new Intent(getApplicationContext(),
                                Main.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(),
                                R.string.msg_dayra_renamed, Toast.LENGTH_SHORT)
                                .show();
                    }

                }

            }

        });
    }

    private void deleteDB() {
        LayoutInflater li = LayoutInflater.from(getApplicationContext());
        View chooseView = li.inflate(R.layout.confirm_dbname, null);
        final AlertDialog ad = new AlertDialog.Builder(Home.this)
                .setCancelable(true).create();
        ad.setView(chooseView, 0, 0, 0, 0);

        ad.show();
        TextView confirm = (TextView) chooseView.findViewById(R.id.confirmbtn);
        TextView back = (TextView) chooseView.findViewById(R.id.back);
        final EditText name = (EditText) chooseView.findViewById(R.id.name);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
        confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
                String namestr = name.getText().toString().trim();
                if (!Utility.isDBName(namestr)) {
                    Toast.makeText(getApplicationContext(),
                            R.string.err_msg_dayra_name, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    if (!getSharedPreferences("login", Context.MODE_PRIVATE)
                            .getString("dbname", "").equals(namestr))
                        Toast.makeText(getApplicationContext(),
                                R.string.err_msg_dayra_match,
                                Toast.LENGTH_SHORT).show();
                    else
                        new DeleteDBTask().execute();

                }

            }
        });
    }

    private class ExportReportTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(Home.this);
            pBar.setCancelable(false);
            pBar.setMessage(getResources().getString(R.string.label_loading));

            pBar.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String path;
            if (android.os.Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED)) {
                path = Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/";
            } else {
                path = android.os.Environment.getDataDirectory()
                        .getAbsolutePath() + "/";
            }
            path += "dayra folder";
            new File(path).mkdirs();
            path += "/";
            path += "dayra_report_"
                    + getSharedPreferences("login", Context.MODE_PRIVATE)
                    .getString("dbname", "data") + ".pdf";
            if (android.os.Build.VERSION.SDK_INT >= 8) {

                MediaScannerConnection.scanFile(getApplicationContext(),
                        new String[]{path.toString()}, null, null);
            }

            return DB.getInstance(
                    getApplicationContext(),
                    getSharedPreferences("login", Context.MODE_PRIVATE)
                            .getString("dbname", "")).exportReport(path,
                    getResources().getStringArray(R.array.excel_header),
                    findViewById(R.id.english_layout) != null);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result)
                Toast.makeText(getApplicationContext(), R.string.msg_exported,
                        Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(),
                        R.string.err_msg_export, Toast.LENGTH_SHORT).show();
            pBar.dismiss();

        }
    }

    private class DeleteDBTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(Home.this);
            pBar.setCancelable(false);
            pBar.setMessage(getResources().getString(R.string.label_loading));

            pBar.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return DB.getInstance(
                    getApplicationContext(),
                    getSharedPreferences("login", Context.MODE_PRIVATE)
                            .getString("dbname", "")).deleteDB(
                    getApplicationContext());
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                getSharedPreferences("login", Context.MODE_PRIVATE).edit()
                        .putString("dbname", null).commit();

                Intent intent = new Intent(getApplicationContext(), Main.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                Toast.makeText(getApplicationContext(),
                        R.string.msg_dayra_deleted, Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getApplicationContext(),
                        R.string.err_msg_dayra_delete, Toast.LENGTH_SHORT)
                        .show();
            pBar.dismiss();

        }
    }

    private class ExportTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(Home.this);
            pBar.setCancelable(false);
            pBar.setMessage(getResources().getString(R.string.label_loading));

            pBar.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            pBar.dismiss();

            if (result)
                Toast.makeText(getApplicationContext(),
                        R.string.msg_dayra_exported, Toast.LENGTH_SHORT).show();
            else

                Toast.makeText(getApplicationContext(),
                        R.string.err_msg_export, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String path;
            if (android.os.Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED)) {
                path = Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/";
            } else {
                path = android.os.Environment.getDataDirectory()
                        .getAbsolutePath() + "/";
            }
            path += "dayra folder";
            new File(path).mkdirs();
            path += "/";

            SharedPreferences sharedPref = getSharedPreferences("login",
                    Context.MODE_PRIVATE);
            DB dbm = DB.getInstance(getApplicationContext(),
                    sharedPref.getString("dbname", ""));
            path += sharedPref.getString("dbname", "dayra db");
            if (android.os.Build.VERSION.SDK_INT >= 8) {

                MediaScannerConnection.scanFile(getApplicationContext(),
                        new String[]{path.toString()}, null, null);
            }

            try {
                FileInputStream inStream = new FileInputStream(
                        dbm.getDBFile(getApplicationContext()));
                FileOutputStream outStream = new FileOutputStream(
                        new File(path));
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

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(Home.this);
            pBar.setCancelable(false);
            pBar.setMessage(getResources().getString(R.string.label_loading));

            pBar.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            pBar.dismiss();

            if (result)
                Toast.makeText(getApplicationContext(),
                        R.string.msg_dayra_exported, Toast.LENGTH_SHORT).show();
            else

                Toast.makeText(getApplicationContext(),
                        R.string.err_msg_export, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String path;
            if (android.os.Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED)) {
                path = Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/";
            } else {
                path = android.os.Environment.getDataDirectory()
                        .getAbsolutePath() + "/";
            }
            path += "dayra folder";
            new File(path).mkdirs();

            path += "/"
                    + getSharedPreferences("login", Context.MODE_PRIVATE)
                    .getString("dbname", "dayra data") + ".xls";
            if (android.os.Build.VERSION.SDK_INT >= 8) {

                MediaScannerConnection.scanFile(getApplicationContext(),
                        new String[]{path.toString()}, null, null);
            }
            return DB.getInstance(
                    getApplicationContext(),
                    getSharedPreferences("login", Context.MODE_PRIVATE)
                            .getString("dbname", "")).exportDayraExcel(
                    getApplicationContext(), path);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMenuItemAdapter.recycleIcons();
    }
}
