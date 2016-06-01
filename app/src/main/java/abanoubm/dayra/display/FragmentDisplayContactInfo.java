package abanoubm.dayra.display;


import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import abanoubm.dayra.R;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.model.ContactData;

public class FragmentDisplayContactInfo extends Fragment {

    private TextView dis_name, dis_address, dis_bday, dis_comm, dis_email,
            dis_lphone, dis_mobile1, dis_mobile2, dis_mobile3, dis_priest,
            dis_class_year, dis_study_work, dis_street, dis_site;

    private ImageView img;
    private ContactData contactData;
    private String id = "-1";

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
            pBar.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            contactData = DB.getInstant(getActivity()).getAttendantData(id);
            return null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            id = arguments.getString("id");
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

        new GetTask().execute();

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

        return root;

    }

    private void setFields() {

        dis_name.setText(contactData.getName());
        dis_address.setText(contactData.getAddress());
        dis_comm.setText(contactData.getComm());
        dis_email.setText(contactData.getEmail());
        dis_lphone.setText(contactData.getLandPhone());

        dis_mobile1.setText(contactData.getMobile1());
        dis_mobile2.setText(contactData.getMobile2());
        dis_mobile3.setText(contactData.getMobile3());

        dis_priest.setText(contactData.getPriest());

        dis_class_year.setText(contactData.getClassYear());
        dis_study_work.setText(contactData.getStudyWork());
        dis_street.setText(contactData.getStreet());
        dis_site.setText(contactData.getSite());


        dis_bday.setText(contactData.getBirthDay());


        if (contactData.getPicDir().length() == 0
                || !new File(contactData.getPicDir()).exists())
            img.setImageResource(R.mipmap.def);
        else {

            img.setImageBitmap(ThumbnailUtils.extractThumbnail(
                    BitmapFactory.decodeFile(contactData.getPicDir()), 250, 250));
        }

    }

}
