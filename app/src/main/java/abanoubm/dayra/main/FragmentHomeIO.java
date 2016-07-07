package abanoubm.dayra.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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
import abanoubm.dayra.operations.CopyDayraPhone;
import abanoubm.dayra.operations.CopyPhoneDayra;
import abanoubm.dayra.operations.CreateAttendanceReport;
import abanoubm.dayra.operations.CreateInformationTable;
import abanoubm.dayra.operations.SendSMS;

public class FragmentHomeIO extends Fragment {

    private MenuItemAdapter mMenuItemAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ListView lv = (ListView) root.findViewById(R.id.list);

        mMenuItemAdapter = new MenuItemAdapter(getActivity(),
                new ArrayList<>(Arrays.asList(getResources()
                        .getStringArray(R.array.out_menu))), 3);
        lv.setAdapter(mMenuItemAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(getActivity(),
                                SendSMS.class));
                        break;
                    case 1:
                        startActivity(new Intent(getActivity(),
                                CopyPhoneDayra.class));
                        break;
                    case 2:
                        startActivity(new Intent(getActivity(),
                                CopyDayraPhone.class));
                        break;
                    case 3:
                        startActivity(new Intent(getActivity(),
                                CreateAttendanceReport.class));
                        break;
                    case 4:
                        new CreateInformationReportTask().execute();
                        break;
                    case 5:
                        startActivity(new Intent(getActivity(),
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

}
