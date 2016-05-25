package abanoubm.dayra.display;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;

import abanoubm.dayra.R;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.obj.ContactData;
import abanoubm.dayra.opr.EditContact;

public class FragmentDisplayContact extends Fragment {

    private TextView dis_name, dis_address, dis_bday, dis_comm, dis_email,
            dis_lphone, dis_mobile1, dis_mobile2, dis_mobile3, dis_priest,
            dis_class_year, dis_study_work, dis_street, dis_site,
            dis_last_visit, dis_last_attend;

    CalendarView calendarView;
    private ImageView img;
    private ContactData attData;
    private int id=-1;

    private class GetTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(getActivity());
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            setFields();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

            String [] days =attData.getAttendDates().split(";");
            for (int i = 0; i < days.length; i++) {
                try {
                    calendarView.setDate(dateFormat.parse(days[i]).getTime());
                }catch (Exception e){}
            }
            pBar.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            attData = DB.getInstance(getActivity(),
                    getActivity().getSharedPreferences("login", Context.MODE_PRIVATE)
                            .getString("dbname", "")).getAttendantData(id);
            return null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            id = arguments.getInt("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_display_contact, container, false);

        calendarView = (CalendarView) root.findViewById(R.id.calendarView);

        dis_name = (TextView) root.findViewById(R.id.dis_name);
        dis_address = (TextView) root.findViewById(R.id.dis_address);

        dis_bday = (TextView) root.findViewById(R.id.dis_bday);
        dis_last_visit = (TextView) root.findViewById(R.id.dis_lvisit);

        dis_comm = (TextView) root.findViewById(R.id.dis_comm);
        dis_email = (TextView) root.findViewById(R.id.dis_email);

        dis_lphone = (TextView) root.findViewById(R.id.dis_lphone);
        dis_mobile1 = (TextView) root.findViewById(R.id.dis_mobile1);
        dis_mobile2 = (TextView) root.findViewById(R.id.dis_mobile2);
        dis_mobile3 = (TextView) root.findViewById(R.id.dis_mobile3);

        dis_priest = (TextView) root.findViewById(R.id.dis_priest);
        img = (ImageView) root.findViewById(R.id.pic_view);
        dis_last_attend = (TextView) root.findViewById(R.id.dis_last_attend);

        dis_class_year = (TextView) root.findViewById(R.id.dis_class_year);
        dis_study_work = (TextView) root.findViewById(R.id.dis_study_work);
        dis_street = (TextView) root.findViewById(R.id.dis_street);
        dis_site = (TextView) root.findViewById(R.id.dis_site);

        new GetTask().execute();

        root.findViewById(R.id.map_btn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(),
                                ContactMap.class);
                        intent.putExtra("lon", attData.getMapLng());
                        intent.putExtra("lat", attData.getMapLat());
                        intent.putExtra("zoom", attData.getMapZoom());
                        intent.putExtra("readonly", true);
                        startActivity(intent);
                    }
                });


        root.findViewById(R.id.dis_last_attend)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                getActivity());
                        builder.setTitle(R.string.label_attendance_dates);
                        builder.setItems(attData
                                .getAttendDates().split(";"), null);
                        builder.create().show();
                    }
                });

        root.findViewById(R.id.call_btn1)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_DIAL, Uri
                                .fromParts("tel", dis_mobile1.getText()
                                        .toString(), null)));
                    }
                });
        root.findViewById(R.id.msg_btn1)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(Intent.ACTION_SENDTO, Uri
                                .parse("smsto:"
                                        + dis_mobile1.getText().toString())));
                    }
                });
        root.findViewById(R.id.call_btn2)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_DIAL, Uri
                                .fromParts("tel", dis_mobile2.getText()
                                        .toString(), null)));
                    }
                });
        root.findViewById(R.id.msg_btn2)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(Intent.ACTION_SENDTO, Uri
                                .parse("smsto:"
                                        + dis_mobile2.getText().toString())));
                    }
                });
        root.findViewById(R.id.call_btn3)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_DIAL, Uri
                                .fromParts("tel", dis_mobile3.getText()
                                        .toString(), null)));
                    }
                });
        root.findViewById(R.id.msg_btn3)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(Intent.ACTION_SENDTO, Uri
                                .parse("smsto:"
                                        + dis_mobile3.getText().toString())));
                    }
                });
        root.findViewById(R.id.call_btn4)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_DIAL, Uri
                                .fromParts("tel", dis_lphone.getText()
                                        .toString(), null)));
                    }
                });

        root.findViewById(R.id.back)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        getActivity().finish();
                    }
                });
        root.findViewById(R.id.edit)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        getActivity().finish();
                        startActivity(new Intent(getActivity(),
                                EditContact.class).putExtra("id",
                                attData.getId()));
                    }
                });

        root.findViewById(R.id.btn_connections)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(getActivity(),
                                DisplayConnections.class).putExtra("id",
                                attData.getId()).putExtra("name",
                                attData.getName()));

                    }
                });
        return root;

    }

    private void setFields() {

        dis_name.setText(attData.getName());
        dis_address.setText(attData.getAddress());
        dis_comm.setText(attData.getComm());
        dis_email.setText(attData.getEmail());
        dis_lphone.setText(attData.getLandPhone());

        dis_mobile1.setText(attData.getMobile1());
        dis_mobile2.setText(attData.getMobile2());
        dis_mobile3.setText(attData.getMobile3());

        dis_priest.setText(attData.getPriest());

        dis_class_year.setText(attData.getClassYear());
        dis_study_work.setText(attData.getStudyWork());
        dis_street.setText(attData.getStreet());
        dis_site.setText(attData.getSite());

        dis_last_attend.setText(attData.getLastAttend());

        dis_bday.setText(attData.getBirthDay());

        dis_last_visit.setText(attData.getLastVisit());

        if (attData.getPicDir().length() == 0
                || !new File(attData.getPicDir()).exists())
            img.setImageResource(R.mipmap.def);
        else {

            img.setImageBitmap(ThumbnailUtils.extractThumbnail(
                    BitmapFactory.decodeFile(attData.getPicDir()), 250, 250));
        }

    }

}
