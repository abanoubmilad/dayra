package abanoubm.dayra.main;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import abanoubm.dayra.R;
import abanoubm.dayra.adapters.MenuItemAdapter;
import abanoubm.dayra.operations.AddToDayra;
import abanoubm.dayra.operations.DivideDayra;

public class FragmentHomeSettings extends Fragment {

    private MenuItemAdapter mMenuItemAdapter;
    private final int DIVIDE_REQUEST = 1100, ADD_DAYRA_REQUEST = 1200;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ListView lv = (ListView) root.findViewById(R.id.list);

        mMenuItemAdapter = new MenuItemAdapter(getActivity(),
                new ArrayList<>(Arrays.asList(getResources()
                        .getStringArray(R.array.settings_menu))), 4);

        lv.setAdapter(mMenuItemAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(getActivity(),
                                Settings.class));
                        break;
                    case 1:
                        renameFields();
                        break;
                    case 2:
                        renameDB();
                        break;
                    case 3:
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
                    case 4:
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
                    case 5:
                        deleteDB();
                        break;

                }
            }
        });
        return root;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMenuItemAdapter.recycleIcons();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == DIVIDE_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startActivity(new Intent(getActivity(),
                        DivideDayra.class));

        } else if (requestCode == ADD_DAYRA_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                startActivity(new Intent(getActivity(),
                        AddToDayra.class));
        }
    }
}
