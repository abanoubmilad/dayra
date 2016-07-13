package abanoubm.dayra.main;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import abanoubm.dayra.R;
import abanoubm.dayra.adapters.MenuItemAdapter;
import abanoubm.dayra.operations.AddToDayra;
import abanoubm.dayra.operations.DivideDayra;

public class FragmentHomeSettings extends Fragment {

    private MenuItemAdapter mMenuItemAdapter;

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
                        renameDB();
                        break;
                    case 2:
                        startActivity(new Intent(getActivity(),
                                AddToDayra.class));
                        break;
                    case 3:
                        startActivity(new Intent(getActivity(),
                                DivideDayra.class));
                        break;
                    case 4:
                        deleteDB();
                        break;

                }
            }
        });
        return root;
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
}
