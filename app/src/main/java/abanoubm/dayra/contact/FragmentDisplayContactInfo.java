package abanoubm.dayra.contact;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import abanoubm.dayra.R;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.model.ContactData;

public class FragmentDisplayContactInfo extends Fragment {
    private static final String ARG_ID = "id";

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
                        startActivity(new Intent(Intent.ACTION_CALL, Uri
                                .fromParts("tel", dis_mobile1.getText()
                                        .toString(), null)));
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
                        startActivity(new Intent(Intent.ACTION_CALL, Uri
                                .fromParts("tel", dis_mobile2.getText()
                                        .toString(), null)));
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
                        startActivity(new Intent(Intent.ACTION_CALL, Uri
                                .fromParts("tel", dis_mobile3.getText()
                                        .toString(), null)));
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
                        startActivity(new Intent(Intent.ACTION_CALL, Uri
                                .fromParts("tel", dis_lphone.getText()
                                        .toString(), null)));
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
            img.setImageBitmap(contactData.getPhoto());
        else
            img.setImageResource(R.mipmap.def);


    }

}
