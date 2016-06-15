package abanoubm.dayra.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import abanoubm.dayra.R;
import abanoubm.dayra.adapters.MenuItemAdapter;
import abanoubm.dayra.display.AddContactDetails;
import abanoubm.dayra.display.DisplayContacts;
import abanoubm.dayra.display.DisplayContactsMap;
import abanoubm.dayra.display.DisplayContactsStatistics;
import abanoubm.dayra.operations.CopyDayraPhone;
import abanoubm.dayra.operations.CopyPhoneDayra;
import abanoubm.dayra.operations.CreateAttendanceReport;
import abanoubm.dayra.operations.CreateInformationTable;
import abanoubm.dayra.operations.DivideDayra;
import abanoubm.dayra.operations.RegisterAttendance;
import abanoubm.dayra.operations.AddDayraData;
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
        Intent intent = new Intent(getApplicationContext(), Main.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        buttons = new ImageView[]{
                (ImageView) findViewById(R.id.img1),
                (ImageView) findViewById(R.id.img2),
                (ImageView) findViewById(R.id.img3),
                (ImageView) findViewById(R.id.img4)
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
                                AddContactDetails.class));
                        break;
                    case 2:
                        startActivity(new Intent(getApplicationContext(),
                                DisplayContacts.class));
                        break;
                    case 3:
                        startActivity(new Intent(getApplicationContext(),
                                DisplayContactsMap.class));
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
                    case 8:
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
                    case 9:
                        Uri uri = Uri.parse("market://details?id=" + getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(goToMarket);
                        } catch (Exception e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
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
                        startActivity(new Intent(getApplicationContext(),
                                CreateAttendanceReport.class));
                        break;
                    case 4:
                        new CreateInformationReportTask().execute();
                        break;
                    case 5:
                        startActivity(new Intent(getApplicationContext(),
                                CreateInformationTable.class));
                        break;
                    case 6:
                        new ExportTask().execute();
                        break;
                    case 7:
                        new ExportDayraExcelTask().execute();
                        break;
                    case 8:
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(new File(Utility.getDayraFolder())), "*/*");
                        startActivity(intent);
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
                        startActivity(new Intent(getApplicationContext(),
                                Settings.class));
                        break;
                    case 1:
                        renameDB();
                        break;
                    case 2:
                        startActivity(new Intent(getApplicationContext(),
                                AddDayraData.class));
                        break;
                    case 3:
                        startActivity(new Intent(getApplicationContext(),
                                DivideDayra.class));
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
        final View view = li.inflate(R.layout.dialogue_rename, null, false);
        final AlertDialog ad = new AlertDialog.Builder(Home.this)
                .setCancelable(true).create();
        ad.setView(view, 0, 0, 0, 0);
        ad.show();

        ((EditText) view.findViewById(R.id.input)).setText(
                Utility.getDayraName(getApplicationContext()));
        view.findViewById(R.id.cancelBtn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
        view.findViewById(R.id.yesBtn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
                String str = ((EditText) view.findViewById(R.id.input)).getText().toString().trim();
                if (!Utility.isDBName(str)) {
                    Toast.makeText(getApplicationContext(),
                            R.string.err_msg_dayra_name, Toast.LENGTH_SHORT)
                            .show();
                } else if (DB.isDBExists(getApplicationContext(), str)) {
                    Toast.makeText(getApplicationContext(),
                            R.string.err_msg_duplicate_dayra,
                            Toast.LENGTH_SHORT).show();
                } else {

                    DB db = DB.getInstant(getApplicationContext());
                    File dbFile = db.getDBFile(getApplicationContext());
                    db.closeDB();

                    String path = dbFile.getPath().substring(0,
                            dbFile.getPath().lastIndexOf("/") + 1);

                    File to = new File(path + str);

                    if (dbFile.renameTo(to)) {
                        getSharedPreferences("login", Context.MODE_PRIVATE)
                                .edit().putString("dbname", null).apply();
                        Intent intent = new Intent(getApplicationContext(),
                                Main.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
        final View view = li.inflate(R.layout.dialogue_delete_dayra, null, false);
        final AlertDialog ad = new AlertDialog.Builder(Home.this)
                .setCancelable(true).create();
        ad.setView(view, 0, 0, 0, 0);
        ad.show();
        view.findViewById(R.id.cancelBtn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
        view.findViewById(R.id.yesBtn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
                String str = ((EditText) view.findViewById(R.id.input)).getText().toString().trim();
                if (!Utility.isDBName(str)) {
                    Toast.makeText(getApplicationContext(),
                            R.string.err_msg_dayra_name, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    if (!Utility.getDayraName(getApplicationContext()).equals(str))
                        Toast.makeText(getApplicationContext(),
                                R.string.err_msg_dayra_match,
                                Toast.LENGTH_SHORT).show();
                    else
                        new DeleteDBTask().execute();

                }

            }
        });
    }

    private class CreateInformationReportTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(Home.this);
            pBar.setCancelable(false);
            pBar.setMessage(getResources().getString(R.string.label_loading));

            pBar.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String path = Utility.getDayraFolder() +
                    "/" + Utility.getDayraName(getApplicationContext()) +
                    "_dayra_information_report_" +
                    new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss a", Locale.getDefault())
                            .format(new Date()) + ".pdf";
            if (android.os.Build.VERSION.SDK_INT >= 8) {
                MediaScannerConnection.scanFile(getApplicationContext(),
                        new String[]{path}, null, null);
            }
            return DB.getInstant(getApplicationContext()).exportInformationReport(path,
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
                        .putString("dbname", null).apply();

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
            String path = Utility.getDayraFolder() +
                    "/" + Utility.getDayraName(getApplicationContext());

            if (android.os.Build.VERSION.SDK_INT >= 8) {

                MediaScannerConnection.scanFile(getApplicationContext(),
                        new String[]{path}, null, null);
            }

            try {
                FileInputStream inStream = new FileInputStream(
                        DB.getInstant(getApplicationContext()).getDBFile(getApplicationContext()));
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
            String path = Utility.getDayraFolder() +
                    "/" + Utility.getDayraName(getApplicationContext()) + ".xls";
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
