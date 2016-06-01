package abanoubm.dayra.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import abanoubm.dayra.R;
import abanoubm.dayra.adapters.MenuItemAdapter;

public class Main extends Activity {
    private static final int IMPORT_DB = 1;
    private static final int IMPORT_EXCEL = 2;
    private ProgressDialog pBar;
    private MenuItemAdapter mMenuItemAdapter;

    private class SignTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            pBar.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            pBar.dismiss();
            if (result)
                startActivity(new Intent(getApplicationContext(), Home.class));
            else
                Toast.makeText(getApplicationContext(), R.string.err_msg_open,
                        Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            if (!DB.isDBExists(getApplicationContext(), params[0]))
                return false;
            else {
                getSharedPreferences("login",
                        Context.MODE_PRIVATE).edit()
                        .putString("dbname", params[0]).commit();
                return true;
            }
        }
    }

    private class RegisterTask extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            pBar.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            pBar.dismiss();
            Toast.makeText(getApplicationContext(),
                    R.string.msg_dayra_created, Toast.LENGTH_SHORT)
                    .show();
            startActivity(new Intent(getApplicationContext(), Home.class));

        }

        @Override
        protected Void doInBackground(String... params) {
            getSharedPreferences("login",
                    Context.MODE_PRIVATE).edit()
                    .putString("dbname", params[0]).commit();
            DB.getInstant(getApplicationContext());
            return null;
        }
    }

    private class ImportTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
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
            ExternalDB edbm = ExternalDB.getInstance(
                    getApplicationContext(), params[0], params[1]);
            if (edbm.checkDB()) {
                String inpath;
                if (android.os.Build.VERSION.SDK_INT >= 17) {
                    inpath = getApplicationContext().getApplicationInfo().dataDir
                            + "/databases/";
                } else {
                    inpath = "/data/data/"
                            + getApplicationContext().getPackageName()
                            + "/databases/";
                }
                inpath += params[0];
                try {
                    FileInputStream inStream = new FileInputStream(
                            params[1]);
                    FileOutputStream outStream = new FileOutputStream(
                            new File(inpath));
                    FileChannel inChannel = inStream.getChannel();
                    FileChannel outChannel = outStream.getChannel();
                    inChannel.transferTo(0, inChannel.size(), outChannel);
                    inStream.close();
                    outStream.close();

                    return R.string.msg_dayra_imported;

                } catch (Exception e) {
                    return R.string.err_msg_import;

                }

            } else
                return R.string.err_msg_invalid_file;

        }

    }

    private class ImportExcelTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
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
            getSharedPreferences("login",
                    Context.MODE_PRIVATE).edit()
                    .putString("dbname", params[0]).commit();
            DB dbm = DB.getInstant(getApplicationContext());
            getSharedPreferences("login",
                    Context.MODE_PRIVATE).edit()
                    .putString("dbname", null).commit();
            if (dbm.ImportDayraExcel(getApplicationContext(), params[1]))
                return R.string.msg_dayra_imported;
            dbm.deleteDB(getApplicationContext());
            return R.string.err_msg_invalid_file;


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPref = getSharedPreferences("lang",
                Context.MODE_PRIVATE);
        int arRange = sharedPref.getInt("ar", 0);
        if (arRange == 1) {
            SharedPreferences.Editor editor = getSharedPreferences("lang",
                    Context.MODE_PRIVATE).edit();
            editor.putInt("ar", 2);
            editor.commit();

            Locale myLocale = new Locale("ar");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);

            Intent refresh = new Intent(getApplicationContext(), Main.class);
            finish();
            startActivity(refresh);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        ((TextView) findViewById(R.id.subhead1)).setText(R.string.app_name);

        sharedPref = getSharedPreferences("login", Context.MODE_PRIVATE);
        if (sharedPref.getBoolean("checked", false)
                && DB.isDBExists(getApplicationContext(),
                sharedPref.getString("dbname", ""))) {
            startActivity(new Intent(getApplicationContext(), Home.class));
            finish();
        }


        pBar = new ProgressDialog(Main.this);
        pBar.setMessage(getResources().getString(R.string.label_loading));
        pBar.setCancelable(false);
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
                        importDB();
                        break;
                    case 3:
                        importExcel();
                        break;
                    case 4:
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri
                                .parse("https://drive.google.com/file/d/0B1rNCm5K9cvwVXJTTzNqSFdrVk0/view"));
                        startActivity(i);
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
                                            SharedPreferences.Editor editor = getSharedPreferences(
                                                    "lang", Context.MODE_PRIVATE)
                                                    .edit();
                                            editor.putInt("ar", 0);
                                            editor.commit();
                                        } else {
                                            temp = "ar";
                                            SharedPreferences.Editor editor = getSharedPreferences(
                                                    "lang", Context.MODE_PRIVATE)
                                                    .edit();
                                            editor.putInt("ar", 2);
                                            editor.commit();
                                        }
                                        Locale myLocale = new Locale(temp);
                                        Resources res = getResources();
                                        DisplayMetrics dm = res.getDisplayMetrics();
                                        Configuration conf = res.getConfiguration();
                                        conf.locale = myLocale;
                                        res.updateConfiguration(conf, dm);

                                        Intent refresh = new Intent(
                                                getApplicationContext(), Main.class);
                                        finish();
                                        startActivity(refresh);
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
                                    .parse("fb://page/453595434816965")));
                        } catch (Exception e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri
                                    .parse("https://www.facebook.com/dayraapp")));
                        }
                        break;

                    case 7:
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

    private void importDB() {
        Intent intentImport = new Intent(Intent.ACTION_GET_CONTENT);
        intentImport.setType("file/*");
        PackageManager manager = getApplicationContext().getPackageManager();
        List<ResolveInfo> infos = manager
                .queryIntentActivities(intentImport, 0);

        if (infos.size() > 0) {
            startActivityForResult(intentImport, IMPORT_DB);
        } else {
            Toast.makeText(getApplicationContext(), R.string.err_msg_explorer,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void importExcel() {
        Intent intentImport = new Intent(Intent.ACTION_GET_CONTENT);
        intentImport.setType("file/*");
        PackageManager manager = getApplicationContext().getPackageManager();
        List<ResolveInfo> infos = manager
                .queryIntentActivities(intentImport, 0);

        if (infos.size() > 0) {
            startActivityForResult(intentImport, IMPORT_EXCEL);
        } else {
            Toast.makeText(getApplicationContext(), R.string.err_msg_explorer,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMPORT_DB) {
                Uri uri = data.getData();
                if (uri != null && new File(uri.getPath()).exists()) {
                    String path = uri.getPath();
                    String dbname = path.substring(path.lastIndexOf("/") + 1);
                    if (!Utility.isDBName(dbname)) {
                        Toast.makeText(getApplicationContext(),
                                R.string.err_msg_dayra_name, Toast.LENGTH_SHORT)
                                .show();
                    } else if (DB.isDBExists(getApplicationContext(), dbname))
                        Toast.makeText(getApplicationContext(),
                                R.string.err_msg_duplicate_dayra, Toast.LENGTH_SHORT)
                                .show();
                    else {
                        new ImportTask().execute(dbname, path);
                    }

                }

            } else if (requestCode == IMPORT_EXCEL) {
                Uri uri = data.getData();
                if (uri != null && new File(uri.getPath()).exists()) {
                    String path = uri.getPath();
                    String dbname = path.substring(path.lastIndexOf("/") + 1)
                            .replace(".xls", "");
                    if (!Utility.isDBName(dbname)) {
                        Toast.makeText(getApplicationContext(),
                                R.string.err_msg_dayra_name, Toast.LENGTH_SHORT)
                                .show();
                    } else if (DB.isDBExists(getApplicationContext(), dbname)) {
                        Toast.makeText(getApplicationContext(),
                                R.string.err_msg_duplicate_dayra, Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        new ImportExcelTask().execute(dbname, path);
                    }

                }

            }

        }
    }

    private void register() {
        LayoutInflater li = LayoutInflater.from(getApplicationContext());
        View regView = li.inflate(R.layout.signup, null);
        final AlertDialog ad = new AlertDialog.Builder(Main.this)
                .setCancelable(true).create();
        ad.setView(regView, 0, 0, 0, 0);
        ad.show();
        TextView reg = (TextView) regView.findViewById(R.id.btnreg);
        TextView back = (TextView) regView.findViewById(R.id.back);
        final EditText name = (EditText) regView.findViewById(R.id.sign_name);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
        reg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String namestr = name.getText().toString().trim();

                if (!Utility.isDBName(namestr)) {
                    Toast.makeText(getApplicationContext(),
                            R.string.err_msg_dayra_name, Toast.LENGTH_SHORT)
                            .show();
                } else {

                    if (DB.isDBExists(getApplicationContext(), namestr)) {
                        Toast.makeText(getApplicationContext(),
                                R.string.err_msg_duplicate_dayra,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        new RegisterTask().execute(namestr);
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
                if (!file.getName().contains("-journal"))
                    names.add(file.getName());
            }

            LayoutInflater li = LayoutInflater.from(getApplicationContext());
            View signView = li.inflate(R.layout.signin, null);
            final AlertDialog ad = new AlertDialog.Builder(Main.this)
                    .setCancelable(true).create();
            ad.setView(signView, 0, 0, 0, 0);
            ad.show();

            ListView nameslv = (ListView) signView
                    .findViewById(R.id.databases_lv);
            nameslv.setAdapter(new ArrayAdapter<>(
                    getApplicationContext(), R.layout.item_dbs_menu,
                    R.id.item, names));
            TextView back = (TextView) signView.findViewById(R.id.back);

            back.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.dismiss();
                }
            });
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
        mMenuItemAdapter.recycleIcons();
        super.onDestroy();
        SharedPreferences sharedPref = getSharedPreferences("lang",
                Context.MODE_PRIVATE);
        int arRange = sharedPref.getInt("ar", 0);
        if (arRange != 0) {
            SharedPreferences.Editor editor = getSharedPreferences("lang",
                    Context.MODE_PRIVATE).edit();
            editor.putInt("ar", 1);
            editor.commit();
        }
    }

}