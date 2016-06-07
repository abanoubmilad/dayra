package abanoubm.dayra.operations;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;

public class ExportContacts extends Activity {

    private ArrayList<String> dataHeader;
    private ArrayList<String> dataTag;

    private class ExportTask extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(ExportContacts.this);
            pBar.setCancelable(false);
            pBar.setMessage(getResources().getString(R.string.label_loading));
            pBar.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String path = Utility.getDayraFolder() +
                    "/dayra_data_" +
                    Utility.getDayraName(getApplicationContext()) + ".pdf";
            if (android.os.Build.VERSION.SDK_INT >= 8) {

                MediaScannerConnection.scanFile(getApplicationContext(),
                        new String[]{path}, null, null);
            }
            return DB.getInstant(getApplicationContext()).exportContactsPdf(
                    dataTag, dataHeader, path);

        }

        @Override
        protected void onPostExecute(Boolean result) {
            pBar.dismiss();
            if (result)

                Toast.makeText(getApplicationContext(), R.string.msg_exported,
                        Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(),
                        R.string.err_msg_export, Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_export_contacts);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        ((TextView) findViewById(R.id.subhead2))
                .setText(R.string.subhead_export_pdf);


        final TextView exportBtn;
        final CheckBox numbering, name, photo, mobile1, mobile2, mobile3,
                lphone, addr, bday, notes,
                email, study_work, class_year, site,
                street, supervisor;


        exportBtn = (TextView) findViewById(R.id.export_btn);

        name = (CheckBox) findViewById(R.id.name);
        mobile1 = (CheckBox) findViewById(R.id.mobile1);
        mobile2 = (CheckBox) findViewById(R.id.mobile2);
        mobile3 = (CheckBox) findViewById(R.id.mobile3);
        numbering = (CheckBox) findViewById(R.id.numbering);
        lphone = (CheckBox) findViewById(R.id.lphone);
        addr = (CheckBox) findViewById(R.id.addr);
        bday = (CheckBox) findViewById(R.id.bday);
        photo = (CheckBox) findViewById(R.id.photo);
        notes = (CheckBox) findViewById(R.id.notes);
        email = (CheckBox) findViewById(R.id.email);
        study_work = (CheckBox) findViewById(R.id.study_work);
        class_year = (CheckBox) findViewById(R.id.classyear);
        street = (CheckBox) findViewById(R.id.street);
        site = (CheckBox) findViewById(R.id.site);
        supervisor = (CheckBox) findViewById(R.id.supervisor);

        exportBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Resources temp = getResources();
                dataTag = new ArrayList<>();
                dataHeader = new ArrayList<>();
                if (numbering.isChecked()) {
                    dataTag.add(" ");
                    dataHeader.add(" ");
                }
                if (name.isChecked()) {
                    dataTag.add(DB.CONTACT_NAME);
                    dataHeader.add(temp.getString(R.string.label_name));
                }
                if (photo.isChecked()) {
                    dataTag.add(DB.PHOTO_BLOB);
                    dataHeader.add(temp.getString(R.string.label_photo));
                }
                if (mobile1.isChecked()) {
                    dataTag.add(DB.CONTACT_MOB1);
                    dataHeader.add(temp.getString(R.string.label_mobile1));

                }
                if (class_year.isChecked()) {
                    dataTag.add(DB.CONTACT_CLASS_YEAR);
                    dataHeader.add(temp.getString(R.string.label_class_year));

                }
                if (study_work.isChecked()) {
                    dataTag.add(DB.CONTACT_STUDY_WORK);
                    dataHeader.add(temp.getString(R.string.label_study_work));

                }

                if (site.isChecked()) {
                    dataTag.add(DB.CONTACT_SITE);
                    dataHeader.add(temp.getString(R.string.label_site));

                }
                if (street.isChecked()) {
                    dataTag.add(DB.CONTACT_ST);
                    dataHeader.add(temp.getString(R.string.label_street));

                }
                if (addr.isChecked()) {
                    dataTag.add(DB.CONTACT_ADDR);
                    dataHeader.add(temp.getString(R.string.label_address));

                }
                if (mobile2.isChecked()) {
                    dataTag.add(DB.CONTACT_MOB2);
                    dataHeader.add(temp.getString(R.string.label_mobile2));

                }
                if (mobile3.isChecked()) {
                    dataTag.add(DB.CONTACT_MOB3);
                    dataHeader.add(temp.getString(R.string.label_mobile3));

                }
                if (lphone.isChecked()) {
                    dataTag.add(DB.CONTACT_LPHONE);
                    dataHeader.add(temp.getString(R.string.label_lphone));

                }

                if (email.isChecked()) {
                    dataTag.add(DB.CONTACT_EMAIL);
                    dataHeader.add(temp.getString(R.string.label_email));

                }
                if (notes.isChecked()) {
                    dataTag.add(DB.CONTACT_NOTES);
                    dataHeader.add(temp.getString(R.string.label_notes));

                }
                if (bday.isChecked()) {
                    dataTag.add(DB.CONTACT_BDAY);
                    dataHeader.add(temp.getString(R.string.label_bday));

                }

                if (supervisor.isChecked()) {
                    dataTag.add(DB.CONTACT_SUPERVISOR);
                    dataHeader.add(temp.getString(R.string.label_supervisor));

                }


                if (dataTag.size() == 0)
                    Toast.makeText(getApplicationContext(),
                            R.string.msg_choose_field, Toast.LENGTH_SHORT)
                            .show();
                else
                    new ExportTask().execute();

            }

        });
    }
}
