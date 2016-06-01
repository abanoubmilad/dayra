package abanoubm.dayra.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
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
import abanoubm.dayra.adapters.MenuItemAdapter;
import abanoubm.dayra.display.DisplayContacts;
import abanoubm.dayra.display.DisplayContactsStatistics;
import abanoubm.dayra.display.MapLocations;
import abanoubm.dayra.operations.AddContact;
import abanoubm.dayra.operations.CopyDayraPhone;
import abanoubm.dayra.operations.CopyPhoneDayra;
import abanoubm.dayra.operations.DivideDayra;
import abanoubm.dayra.operations.ExportContacts;
import abanoubm.dayra.operations.RegisterAttendance;
import abanoubm.dayra.operations.ReplaceDayra;
import abanoubm.dayra.operations.Search;
import abanoubm.dayra.operations.SendSMS;

public class Home extends Activity {
    private ProgressDialog pBar;
    private MenuItemAdapter mMenuItemAdapter;
    private ListView lv;
    private int tagCursor;
    private TextView subHead2;
    private ImageView[] buttons;

    public void fireHome1Menu() {
        buttons[tagCursor].setBackgroundColor(0);
        tagCursor = 0;
        buttons[0].setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
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
        buttons = new ImageView[]{
                (ImageView) findViewById(R.id.homeImage1),
                (ImageView) findViewById(R.id.homeImage2),
                (ImageView) findViewById(R.id.ioImage),
                (ImageView) findViewById(R.id.settImage)
        };
        subHead2 = (TextView) findViewById(R.id.subhead2);

        buttons[0].setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (tagCursor != 0)
                    fireHome1Menu();

            }
        });
        buttons[1].setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (tagCursor != 1)
                    fireHome2Menu();

            }
        });
        buttons[2].setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (tagCursor != 2)
                    fireOutMenu();

            }
        });
        buttons[3].setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (tagCursor != 3)
                    fireSettingsMenu();

            }
        });

        lv = (ListView) findViewById(R.id.home_list);
        fireHome2Menu();

    }

    private void fireHome2Menu() {

        buttons[tagCursor].setBackgroundColor(0);
        tagCursor = 1;
        buttons[1].setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));

        subHead2.setText(R.string.label_home_main);

        if (mMenuItemAdapter != null)
            mMenuItemAdapter.recycleIcons();
        mMenuItemAdapter = new MenuItemAdapter(getApplicationContext(),
                new ArrayList<>(Arrays.asList(getResources()
                        .getStringArray(R.array.home_menu))), 2);
        lv.setAdapter(mMenuItemAdapter);

        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(
                                getApplicationContext(),
                                Search.class));

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
                                DisplayContactsStatistics.class));
                        break;
                    case 6:
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri
                                .parse("https://drive.google.com/file/d/0B1rNCm5K9cvwVXJTTzNqSFdrVk0/view"));
                        startActivity(i);
                        break;
                    case 7:
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

        buttons[tagCursor].setBackgroundColor(0);
        tagCursor = 2;
        buttons[2].setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));

        subHead2.setText(R.string.label_home_out);
        if (mMenuItemAdapter != null)
            mMenuItemAdapter.recycleIcons();
        mMenuItemAdapter = new MenuItemAdapter(getApplicationContext(),
                new ArrayList<>(Arrays.asList(getResources()
                        .getStringArray(R.array.out_menu))), 3);
        lv.setAdapter(mMenuItemAdapter);

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(getApplicationContext(),
                                SendSMS.class));
                        break;
                    case 1:
                        startActivity(new Intent(getApplicationContext(),
                                CopyPhoneDayra.class));
                        break;
                    case 2:
                        startActivity(new Intent(getApplicationContext(),
                                CopyDayraPhone.class));
                        break;
                    case 3:
                        new ExportReportTask().execute();
                        break;
                    case 4:
                        startActivity(new Intent(getApplicationContext(),
                                ExportContacts.class));
                        break;
                    case 5:
                        new ExportTask().execute();
                        break;
                    case 6:
                        new ExportDayraExcelTask().execute();
                        break;
                }
            }
        });
    }

    private void fireSettingsMenu() {

        buttons[tagCursor].setBackgroundColor(0);
        tagCursor = 3;
        buttons[3].setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));

        subHead2.setText(R.string.label_home_settings);
        if (mMenuItemAdapter != null)
            mMenuItemAdapter.recycleIcons();
        mMenuItemAdapter = new MenuItemAdapter(getApplicationContext(),
                new ArrayList<>(Arrays.asList(getResources()
                        .getStringArray(R.array.settings_menu))), 4);
        lv.setAdapter(mMenuItemAdapter);

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:
//                        startActivity(new Intent(getApplicationContext(),
//                                Settings.class));
                        break;
                    case 1:
                        startActivity(new Intent(getApplicationContext(),
                                ReplaceDayra.class));
                        break;
                    case 2:
                        startActivity(new Intent(getApplicationContext(),
                                DivideDayra.class));
                        break;
                    case 3:
                        renameDB();
                        break;
                    case 4:
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

                    DB db = DB.getInstant(getApplicationContext());
                    File dbFile = db.getDBFile(getApplicationContext());
                    db.closeDB();

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
                        new String[]{path}, null, null);
            }

            return DB.getInstant(getApplicationContext()).exportReport(path,
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
            return DB.getInstant(getApplicationContext()).deleteDB(
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
            DB dbm = DB.getInstant(getApplicationContext());
            path += sharedPref.getString("dbname", "dayra db");
            if (android.os.Build.VERSION.SDK_INT >= 8) {

                MediaScannerConnection.scanFile(getApplicationContext(),
                        new String[]{path}, null, null);
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
                        new String[]{path}, null, null);
            }
            return DB.getInstant(getApplicationContext()).exportDayraExcel(
                    getApplicationContext(), path);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMenuItemAdapter.recycleIcons();
    }
}
