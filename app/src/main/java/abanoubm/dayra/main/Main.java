package abanoubm.dayra.main;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
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
import java.util.Locale;

import abanoubm.dayra.BuildConfig;
import abanoubm.dayra.R;
import abanoubm.dayra.adapters.MenuItemAdapter;

public class Main extends Activity {
    private static final int IMPORT_DB = 1;
    private MenuItemAdapter mMenuItemAdapter;

    private final int IMPORT_REQUEST = 700, SUPPORT_REQUEST = 800, FOLDER_REQUEST = 900;

    private class CheckSupportTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Void result) {

        }

        @Override
        protected Void doInBackground(Void... params) {
            ContactHelper.checkDayraSupport(getContentResolver(), getApplicationContext());
            return null;

        }
    }

    private class SignTask extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {

            pBar = new ProgressDialog(Main.this);
            pBar.setMessage(getResources().getString(R.string.label_loading));
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            pBar.dismiss();
            if (result) {
                startActivity(new Intent(getApplicationContext(), Home.class));
                finish();
            } else
                Toast.makeText(getApplicationContext(), R.string.err_msg_open,
                        Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            if (!DB.isDBExists(getApplicationContext(), params[0]))
                return false;
            else {
                Utility.makeLogin(getApplicationContext(), params[0]);

                return true;
            }
        }
    }

    private class RegisterTask extends AsyncTask<String, Void, Void> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(Main.this);
            pBar.setMessage(getResources().getString(R.string.label_loading));
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            pBar.dismiss();
            Toast.makeText(getApplicationContext(),
                    R.string.msg_dayra_created, Toast.LENGTH_SHORT)
                    .show();
            startActivity(new Intent(getApplicationContext(), Home.class));
            finish();
        }

        @Override
        protected Void doInBackground(String... params) {
            Utility.makeLogin(getApplicationContext(), params[0]);
            DB.getInstant(getApplicationContext());
            return null;
        }
    }

    private class ImportDayraFileTask extends AsyncTask<String, Void, Integer> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {

            pBar = new ProgressDialog(Main.this);
            pBar.setMessage(getResources().getString(R.string.label_loading));
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected void onPostExecute(Integer result) {
            pBar.dismiss();
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT)
                    .show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            DBAdder db = new DBAdder(getApplicationContext(), params[1]);
            if (db.checkDB()) {
                db.close();
                // create empty db with that name
                DB.getInstant(getApplicationContext(),
                        params[0]).closeDB();
                try {

                    FileInputStream inStream = new FileInputStream(
                            params[1]);
                    FileOutputStream outStream = new FileOutputStream(getDatabasePath(params[0]));

                    FileChannel inChannel = inStream.getChannel();
                    FileChannel outChannel = outStream.getChannel();
                    inChannel.transferTo(0, inChannel.size(), outChannel);
                    inStream.close();
                    outStream.close();
                    return R.string.msg_dayra_imported;
                } catch (Exception e) {
                    File file = getDatabasePath(params[0]);
                    if (file != null && file.exists()) {
                        file.delete();
                    }
                    return R.string.err_msg_import;

                }

            } else {
                db.close();
                return R.string.err_msg_invalid_file;
            }


        }

    }

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
        setContentView(R.layout.act_main);
        ((TextView) findViewById(R.id.subhead1)).setText(R.string.app_name);
        ((TextView) findViewById(R.id.subhead2)).setText(BuildConfig.VERSION_NAME);

        ((TextView) findViewById(R.id.footer)).setText("dayra " + BuildConfig.VERSION_NAME + " @" + new SimpleDateFormat(
                "yyyy", Locale.getDefault())
                .format(new Date()) + " Abanoub M.");

        if (!Utility.getDayraName(getApplicationContext()).equals("")) {
            startActivity(new Intent(getApplicationContext(), Home.class));
            finish();
        }


        ListView lv = (ListView) findViewById(R.id.home_list);

        mMenuItemAdapter = new MenuItemAdapter(getApplicationContext(),
                new ArrayList<>(Arrays.asList(getResources()
                        .getStringArray(R.array.sign_menu))), 1);
        lv.setAdapter(mMenuItemAdapter);

        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:
                        sign();
                        break;
                    case 1:
                        register();
                        break;
                    case 2:
                        if (Build.VERSION.SDK_INT < 23 ||
                                ContextCompat.checkSelfPermission(Main.this,
                                        Manifest.permission.READ_EXTERNAL_STORAGE)
                                        == PackageManager.PERMISSION_GRANTED) {
                            importDB();
                        } else {
                            ActivityCompat.requestPermissions(Main.this,
                                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                    IMPORT_REQUEST);
                        }
                        break;
                    case 3:
                        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri
                                .parse("https://drive.google.com/file/d/0B1rNCm5K9cvwVXJTTzNqSFdrVk0/view")));
                        break;
                    case 4:
                        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri
                                .parse("https://drive.google.com/open?id=1flSRdoiIT_hNd96Kxz3Ww3EhXDLZ45FhwFJ2hF9vl7g")));
                        break;
                    case 5: {

                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                Main.this);
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
                                            Utility.setArabicLang(getApplicationContext(), 0);
                                        } else {
                                            temp = "ar";
                                            Utility.setArabicLang(getApplicationContext(), 2);
                                        }
                                        Locale myLocale = new Locale(temp);
                                        Resources res = getResources();
                                        DisplayMetrics dm = res.getDisplayMetrics();
                                        Configuration conf = res.getConfiguration();
                                        conf.locale = myLocale;
                                        res.updateConfiguration(conf, dm);

                                        finish();
                                        startActivity(new Intent(getIntent()));
                                    }

                                });
                        builder.create().show();

                    }
                    break;
                    case 6:
                        try {
                            getPackageManager().getPackageInfo(
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

                    case 7:
                        if (Build.VERSION.SDK_INT < 23 ||
                                ContextCompat.checkSelfPermission(Main.this,
                                        Manifest.permission.WRITE_CONTACTS)
                                        == PackageManager.PERMISSION_GRANTED) {
                            new CheckSupportTask().execute();

                        } else {
                            ActivityCompat.requestPermissions(Main.this,
                                    new String[]{android.Manifest.permission.WRITE_CONTACTS},
                                    SUPPORT_REQUEST);
                        }
                        try {
                            getPackageManager().getPackageInfo(
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
                    case 8:
                        Uri uri = Uri.parse("market://details?id=" + getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        try {
                            startActivity(goToMarket);
                        } catch (Exception e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                        }
                        break;
                    case 9:
                        if (Build.VERSION.SDK_INT < 23 ||
                                ContextCompat.checkSelfPermission(Main.this,
                                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        == PackageManager.PERMISSION_GRANTED) {
                            Intent intent = new Intent(Intent.ACTION_VIEW).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.setDataAndType(Uri.fromFile(new File(Utility.getDayraFolder())), "*/*");
                            startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_NEW_TASK));
                        } else {
                            ActivityCompat.requestPermissions(Main.this,
                                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    FOLDER_REQUEST);
                        }
                        break;
                }
            }
        });

    }

    private void importDB() {
        Intent intentImport = new Intent(Intent.ACTION_GET_CONTENT);
        intentImport.setDataAndType(Uri.fromFile(new File(Utility.getDayraFolder())),
                "application/octet-stream");

        if (getApplicationContext().getPackageManager()
                .queryIntentActivities(intentImport, 0).size() > 0) {
            startActivityForResult(intentImport, IMPORT_DB);
        } else {
            Toast.makeText(getApplicationContext(), R.string.err_msg_explorer,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMPORT_DB) {
                String path = Utility.getRealPath(getApplicationContext(), data.getData());
                String dbname = path.substring(path.lastIndexOf("/") + 1);
                if (Utility.isInvlaidDBName(dbname))
                    Toast.makeText(getApplicationContext(),
                            R.string.err_msg_dayra_name, Toast.LENGTH_SHORT)
                            .show();
                else if (DB.isDBExists(getApplicationContext(), dbname))
                    Toast.makeText(getApplicationContext(),
                            R.string.err_msg_duplicate_dayra, Toast.LENGTH_SHORT)
                            .show();
                else
                    new ImportDayraFileTask().execute(dbname, path);


            }

        }

    }

    private void register() {
        LayoutInflater li = LayoutInflater.from(getApplicationContext());
        final View view = li.inflate(R.layout.dialogue_create, null, false);
        final AlertDialog ad = new AlertDialog.Builder(Main.this)
                .setCancelable(true).create();
        ad.setView(view, 0, 0, 0, 0);
        ad.show();

        view.findViewById(R.id.yesBtn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String str = ((EditText)
                        view.findViewById(R.id.input)).getText().toString().trim();

                if (Utility.isInvlaidDBName(str)) {
                    Toast.makeText(getApplicationContext(),
                            R.string.err_msg_dayra_name, Toast.LENGTH_SHORT)
                            .show();
                } else {

                    if (DB.isDBExists(getApplicationContext(), str)) {
                        Toast.makeText(getApplicationContext(),
                                R.string.err_msg_duplicate_dayra,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        new RegisterTask().execute(str);
                        ad.dismiss();

                    }
                }
            }
        });

    }

    private void sign() {
        String inpath;
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            inpath = getApplicationContext().getApplicationInfo().dataDir
                    + "/databases/";
        } else {
            inpath = "/data/data/" + getApplicationContext().getPackageName()
                    + "/databases/";
        }
        File folder = new File(inpath);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null || listOfFiles.length == 0) {
            Toast.makeText(getApplicationContext(), R.string.msg_no_dayra,
                    Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<String> names = new ArrayList<>(listOfFiles.length);
            for (File file : listOfFiles) {
                if (!file.getName().contains("journal"))
                    names.add(file.getName());
            }

            LayoutInflater li = LayoutInflater.from(getApplicationContext());
            View signView = li.inflate(R.layout.dialogue_signin, null, false);
            final AlertDialog ad = new AlertDialog.Builder(Main.this)
                    .setCancelable(true).create();
            ad.setView(signView, 0, 0, 0, 0);
            ad.show();

            ListView nameslv = (ListView) signView
                    .findViewById(R.id.databases_lv);
            nameslv.setAdapter(new ArrayAdapter<>(
                    getApplicationContext(), R.layout.item_string,
                    R.id.item, names));

            nameslv.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    String str = (String) parent.getItemAtPosition(position);
                    new SignTask().execute(str);
                    ad.dismiss();
                }
            });

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMenuItemAdapter.recycleIcons();
        if (Utility.getArabicLang(getApplicationContext()) != 0)
            Utility.setArabicLang(getApplicationContext(), 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FOLDER_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_VIEW).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.setDataAndType(Uri.fromFile(new File(Utility.getDayraFolder())), "*/*");
                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK));
            }

        } else if (requestCode == IMPORT_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                importDB();

        } else if (requestCode == SUPPORT_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                new CheckSupportTask().execute();

        }
    }

}