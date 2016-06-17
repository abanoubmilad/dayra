package abanoubm.dayra.display;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import abanoubm.dayra.R;
import abanoubm.dayra.adapters.ContactsDisplayListAdapter;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.model.ContactID;
import abanoubm.dayra.model.ContactSort;

public class FragmentDisplayContacts extends Fragment {
    private ListView lv;
    private ArrayList<ContactSort> list;
    private int previousPosition = 0;
    private boolean isDualMode = false;
    private ContactsDisplayListAdapter mAdapter;

    private class GetAllTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(getActivity());
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            list = DB.getInstant(getActivity()).getContactsDisplayList();
            return null;
        }

        @Override
        protected void onPostExecute(Void att) {
            mAdapter.clear();
            mAdapter.addAll(list);
            if (list.size() == 0) {
                getActivity().finish();
                Toast.makeText(getActivity(),
                        R.string.msg_no_contacts, Toast.LENGTH_SHORT).show();
            } else {
                if (previousPosition < list.size())
                    lv.setSelection(previousPosition);
                if (isDualMode) {
                    lv.performItemClick(lv.findViewWithTag(mAdapter.getItem(previousPosition)),
                            previousPosition, mAdapter.getItemId(previousPosition));
                }
            }
            pBar.dismiss();

        }
    }

    private class SortTask extends AsyncTask<Integer, Void, Void> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(getActivity());
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected Void doInBackground(Integer... params) {
            switch (params[0]) {
                // name
                case 0:
                    Collections.sort(list, new Comparator<ContactSort>() {
                        @Override
                        public int compare(ContactSort lhs, ContactSort rhs) {
                            return lhs.getName().compareTo(rhs.getName());
                        }
                    });
                    break;
                // class/year
                case 1:
                    Collections.sort(list, new Comparator<ContactSort>() {
                        @Override
                        public int compare(ContactSort lhs, ContactSort rhs) {
                            return lhs.getClassYear().compareTo(rhs.getClassYear());
                        }
                    });
                    break;
                // study/work
                case 2:
                    Collections.sort(list, new Comparator<ContactSort>() {
                        @Override
                        public int compare(ContactSort lhs, ContactSort rhs) {
                            return lhs.getStudyWork().compareTo(rhs.getStudyWork());
                        }
                    });
                    break;
                // site
                case 3:
                    Collections.sort(list, new Comparator<ContactSort>() {
                        @Override
                        public int compare(ContactSort lhs, ContactSort rhs) {
                            return lhs.getSite().compareTo(rhs.getSite());
                        }
                    });
                    break;
                // street
                case 4:
                    Collections.sort(list, new Comparator<ContactSort>() {
                        @Override
                        public int compare(ContactSort lhs, ContactSort rhs) {
                            return lhs.getStreet().compareTo(rhs.getStreet());
                        }
                    });
                    break;
                // confession father
                case 5:
                    Collections.sort(list, new Comparator<ContactSort>() {
                        @Override
                        public int compare(ContactSort lhs, ContactSort rhs) {
                            return lhs.getPriest().compareTo(rhs.getPriest());
                        }
                    });
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mAdapter.clear();
            mAdapter.addAll(list);
            previousPosition = 0;
            if (list.size() == 0) {
                getActivity().finish();
                Toast.makeText(getActivity(),
                        R.string.msg_no_contacts, Toast.LENGTH_SHORT).show();
            } else {
                if (isDualMode) {
                    lv.performItemClick(lv.findViewWithTag(mAdapter.getItem(previousPosition)),
                            previousPosition, mAdapter.getItemId(previousPosition));
                }
            }
            pBar.dismiss();

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        new GetAllTask().execute();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            isDualMode = arguments.getBoolean("isdualmode");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_display_contacts, container, false);

        Spinner spin = (Spinner) root.findViewById(R.id.spin);
        spin.setAdapter(new ArrayAdapter<>(getActivity(),
                R.layout.item_string, getResources().getTextArray(R.array.sort_menu)));

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                new SortTask().execute(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        lv = (ListView) root.findViewById(R.id.list);
        mAdapter = new ContactsDisplayListAdapter(getActivity(), new ArrayList<ContactSort>(0));
        lv.setAdapter(mAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1,
                                    int position, long arg3) {
                if (isDualMode)
                    mAdapter.setSelectedIndex(position);

                previousPosition = lv.getFirstVisiblePosition();

                ((CallBack) getActivity()).notify(((ContactID) parent
                        .getItemAtPosition(position)).getId());
            }
        });
        return root;
    }

}
