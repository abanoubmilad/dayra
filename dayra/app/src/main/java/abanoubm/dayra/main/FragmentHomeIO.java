package abanoubm.dayra.main;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
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
import abanoubm.dayra.operations.ExportGDrive;
import abanoubm.dayra.operations.SendSMS;

public class FragmentHomeIO extends Fragment {

    private MenuItemAdapter mMenuItemAdapter;
    private final int COPY_PHONE_REQUEST = 1400,
            COPY_DAYRA_REQUEST = 1500, EXPORT_ATTENDANCE_REQUEST = 1600,
            FOLDER_REQUEST = 1700, EXPORT_INFO_TABLE_REQUEST = 1800,
            EXPORT_INFO_REPORT_REQUEST = 1900, EXPORT_FILE_REQUEST = 2000,
            EXPORT_EXCEL_REQUEST = 2100, GDRIVE_REQUEST = 2200;

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
                    case 2:
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
                    case 3:
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
                    case 4:
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
                    case 5:
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
                    case 6:
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

                    case 7:
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
                    case 8:
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
                    case 9:
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

        }


    }
}
