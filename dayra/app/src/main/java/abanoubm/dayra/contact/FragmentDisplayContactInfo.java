package abanoubm.dayra.contact;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import abanoubm.dayra.R;
import abanoubm.dayra.contacts.DisplayContacts;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.ContactData;

public class FragmentDisplayContactInfo extends Fragment {
    private boolean dualMode;
    private static final String ARG_ID = "id";
    private static final String ARG_DUAL_MODE = "dual";

    private TextView dis_name, dis_address, dis_bday, dis_comm, dis_email,
            dis_lphone, dis_mobile1, dis_mobile2, dis_mobile3, dis_priest,
            dis_class_year, dis_study_work, dis_street, dis_site, dis_home;

    private ImageView img;
    private ContactData contactData;
    private String id;
    private ImageView call1, call2, call3, call4, msg1, msg2, msg3;

    private class GetTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Void result) {
            setFields();
        }

        @Override
        protected Void doInBackground(Void... params) {
            contactData = DB.getInstant(getActivity()).getContactInfo(id);
            return null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            id = arguments.getString(ARG_ID);
            dualMode = arguments.getBoolean(ARG_DUAL_MODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_display_contact_info, container, false);

        dis_name = (TextView) root.findViewById(R.id.dis_name);
        dis_address = (TextView) root.findViewById(R.id.dis_address);

        dis_bday = (TextView) root.findViewById(R.id.dis_bday);

        dis_comm = (TextView) root.findViewById(R.id.dis_comm);
        dis_email = (TextView) root.findViewById(R.id.dis_email);

        dis_lphone = (TextView) root.findViewById(R.id.dis_lphone);
        dis_mobile1 = (TextView) root.findViewById(R.id.dis_mobile1);
        dis_mobile2 = (TextView) root.findViewById(R.id.dis_mobile2);
        dis_mobile3 = (TextView) root.findViewById(R.id.dis_mobile3);

        dis_priest = (TextView) root.findViewById(R.id.dis_priest);
        img = (ImageView) root.findViewById(R.id.pic_view);

        dis_class_year = (TextView) root.findViewById(R.id.dis_class_year);
        dis_study_work = (TextView) root.findViewById(R.id.dis_study_work);
        dis_street = (TextView) root.findViewById(R.id.dis_street);
        dis_site = (TextView) root.findViewById(R.id.dis_site);
        dis_home = (TextView) root.findViewById(R.id.dis_home);


        call1 = (ImageView) root.findViewById(R.id.call_btn1);
        call2 = (ImageView) root.findViewById(R.id.call_btn2);
        call3 = (ImageView) root.findViewById(R.id.call_btn3);
        call4 = (ImageView) root.findViewById(R.id.call_btn4);

        msg1 = (ImageView) root.findViewById(R.id.msg_btn1);
        msg2 = (ImageView) root.findViewById(R.id.msg_btn2);
        msg3 = (ImageView) root.findViewById(R.id.msg_btn3);

        new GetTask().execute();

        call1
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT < 23 ||
                                ContextCompat.checkSelfPermission(getContext(),
                                        android.Manifest.permission.CALL_PHONE)
                                        == PackageManager.PERMISSION_GRANTED) {
                            startActivity(new Intent(Intent.ACTION_CALL, Uri
                                    .fromParts("tel", dis_mobile1.getText()
                                            .toString(), null)));
                        } else {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{android.Manifest.permission.CALL_PHONE},
                                    1);
                        }

                    }
                });
        msg1
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(Intent.ACTION_SENDTO, Uri
                                .parse("smsto:"
                                        + dis_mobile1.getText().toString())));
                    }
                });
        call2
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT < 23 ||
                                ContextCompat.checkSelfPermission(getContext(),
                                        android.Manifest.permission.CALL_PHONE)
                                        == PackageManager.PERMISSION_GRANTED) {
                            startActivity(new Intent(Intent.ACTION_CALL, Uri
                                    .fromParts("tel", dis_mobile2.getText()
                                            .toString(), null)));
                        } else {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{android.Manifest.permission.CALL_PHONE},
                                    2);
                        }
                    }
                });
        msg2
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(Intent.ACTION_SENDTO, Uri
                                .parse("smsto:"
                                        + dis_mobile2.getText().toString())));
                    }
                });
        call3
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT < 23 ||
                                ContextCompat.checkSelfPermission(getContext(),
                                        android.Manifest.permission.CALL_PHONE)
                                        == PackageManager.PERMISSION_GRANTED) {
                            startActivity(new Intent(Intent.ACTION_CALL, Uri
                                    .fromParts("tel", dis_mobile3.getText()
                                            .toString(), null)));
                        } else {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{android.Manifest.permission.CALL_PHONE},
                                    3);
                        }
                    }
                });
        msg3
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(Intent.ACTION_SENDTO, Uri
                                .parse("smsto:"
                                        + dis_mobile3.getText().toString())));
                    }
                });
        call4
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT < 23 ||
                                ContextCompat.checkSelfPermission(getContext(),
                                        android.Manifest.permission.CALL_PHONE)
                                        == PackageManager.PERMISSION_GRANTED) {
                            startActivity(new Intent(Intent.ACTION_CALL, Uri
                                    .fromParts("tel", dis_lphone.getText()
                                            .toString(), null)));
                        } else {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{android.Manifest.permission.CALL_PHONE},
                                    4);
                        }
                    }
                });

        root.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(getActivity());
                final View view = li.inflate(R.layout.dialogue_delete, null, false);
                final AlertDialog ad = new AlertDialog.Builder(getActivity())
                        .setCancelable(true).create();
                ad.setView(view, 0, 0, 0, 0);
                ad.show();

                view.findViewById(R.id.yesBtn).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {


                        new DeleteTask().execute();
                        ad.dismiss();

                    }
                });


            }
        });
        return root;

    }

    private void setFields() {

        dis_name.setText(contactData.getName());
        dis_address.setText(contactData.getAddress());
        dis_comm.setText(contactData.getComm());
        dis_email.setText(contactData.getEmail());

        dis_lphone.setText(contactData.getLandPhone());
        if (contactData.getLandPhone().length() != 0) {
            call4.setVisibility(View.VISIBLE);
        }

        dis_mobile1.setText(contactData.getMobile1());
        if (contactData.getMobile1().length() != 0) {
            call1.setVisibility(View.VISIBLE);
            msg1.setVisibility(View.VISIBLE);
        }

        dis_mobile2.setText(contactData.getMobile2());
        if (contactData.getMobile2().length() != 0) {
            call2.setVisibility(View.VISIBLE);
            msg2.setVisibility(View.VISIBLE);
        }

        dis_mobile3.setText(contactData.getMobile3());
        if (contactData.getMobile3().length() != 0) {
            call3.setVisibility(View.VISIBLE);
            msg3.setVisibility(View.VISIBLE);
        }

        dis_priest.setText(contactData.getPriest());
        dis_class_year.setText(contactData.getClassYear());
        dis_study_work.setText(contactData.getStudyWork());
        dis_street.setText(contactData.getStreet());
        dis_site.setText(contactData.getSite());
        dis_home.setText(contactData.getHome());


        dis_bday.setText(contactData.getBirthDay());


        if (contactData.getPhoto() != null)
            img.setImageBitmap(Utility.getBitmap(contactData.getPhoto()));
        else
            img.setImageResource(R.mipmap.def);


    }

    private class DeleteTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {

            pBar = new ProgressDialog(getActivity());
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            getActivity().finish();
            if (dualMode)
                startActivity(new Intent(getContext(), DisplayContacts.class));

            Toast.makeText(getActivity(),
                    R.string.msg_deleted, Toast.LENGTH_SHORT)
                    .show();
            pBar.dismiss();

        }

        @Override
        protected Void doInBackground(Void... params) {
            DB.getInstant(getActivity()).deleteContact(id);
            return null;
        }
    }
}
