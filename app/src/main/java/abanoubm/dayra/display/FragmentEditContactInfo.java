package abanoubm.dayra.display;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import abanoubm.dayra.R;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.ContactData;

public class FragmentEditContactInfo extends Fragment {
    private static final String ARG_ID = "id";
    private String id;

    private ImageView img;

    private ContactData contactData;

    private static final int TAKE_IMG = 2;
    private static final int BROWSE_IMG = 1;

    private Uri fileUri;
    private Bitmap photo = null;

    private EditText name, address, comm, email,
            lphone, mobile1, mobile2, mobile3;
    private TextView date;

    private static EditText[] optionsInput;

    private static final String[] optionsTags = {
            DB.CONTACT_CLASS_YEAR,
            DB.CONTACT_STUDY_WORK,
            DB.CONTACT_ST,
            DB.CONTACT_SITE,
            DB.CONTACT_SUPERVISOR
    };
    private static final int[] optionsChoose = {
            R.string.label_choose_class_year,
            R.string.label_choose_study_work,
            R.string.label_choose_street,
            R.string.label_choose_site,
            R.string.label_choose_supervisor
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_contact_info, container, false);
        optionsInput = new EditText[]{
                (EditText) root.findViewById(R.id.edit_class_year),
                (EditText) root.findViewById(R.id.edit_study_work),
                (EditText) root.findViewById(R.id.edit_street),
                (EditText) root.findViewById(R.id.edit_site),
                (EditText) root.findViewById(R.id.edit_priest)
        };

        name = (EditText) root.findViewById(R.id.edit_name);
        address = (EditText) root.findViewById(R.id.edit_address);

        date = (TextView) root.findViewById(R.id.date);

        comm = (EditText) root.findViewById(R.id.edit_comm);
        email = (EditText) root.findViewById(R.id.edit_email);


        lphone = (EditText) root.findViewById(R.id.edit_lphone);
        mobile1 = (EditText) root.findViewById(R.id.edit_mobile1);
        mobile2 = (EditText) root.findViewById(R.id.edit_mobile2);
        mobile3 = (EditText) root.findViewById(R.id.edit_mobile3);

        img = (ImageView) root.findViewById(R.id.pic_view);

        new GetTask().execute();

        final DatePickerDialog picker_bday;
        Calendar cal = Calendar.getInstance();
        picker_bday = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                date.setText(Utility.produceDate(dayOfMonth, monthOfYear + 1, year));
            }

        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));

        root.findViewById(R.id.pick_date)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        picker_bday.show();

                    }
                });

        img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                editImage();
            }
        });

        root.findViewById(R.id.backImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(getActivity());
                final View view = li.inflate(R.layout.dialogue_back, null, false);
                final AlertDialog ad = new AlertDialog.Builder(getActivity())
                        .setCancelable(true).create();
                ad.setView(view, 0, 0, 0, 0);
                ad.show();
                view.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ad.dismiss();
                    }
                });
                view.findViewById(R.id.yesBtn).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        getActivity().finish();
                        ad.dismiss();

                    }
                });


            }
        });
        root.findViewById(R.id.deleteImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(getActivity());
                final View view = li.inflate(R.layout.dialogue_delete, null, false);
                final AlertDialog ad = new AlertDialog.Builder(getActivity())
                        .setCancelable(true).create();
                ad.setView(view, 0, 0, 0, 0);
                ad.show();
                view.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ad.dismiss();
                    }
                });
                view.findViewById(R.id.yesBtn).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {


                        new DeleteTask().execute();
                        ad.dismiss();

                    }
                });


            }
        });
        root.findViewById(R.id.resetImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(getActivity());
                final View view = li.inflate(R.layout.dialogue_reset, null, false);
                final AlertDialog ad = new AlertDialog.Builder(getActivity())
                        .setCancelable(true).create();
                ad.setView(view, 0, 0, 0, 0);
                ad.show();
                view.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ad.dismiss();
                    }
                });
                view.findViewById(R.id.yesBtn).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        reset();
                        ad.dismiss();

                    }
                });


            }
        });
        root.findViewById(R.id.saveImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EditTask().execute(
                        name.getText().toString().trim(),
                        address.getText().toString().trim(),
                        date.getText().toString().trim(),
                        comm.getText().toString().trim(),
                        email.getText().toString().trim(),
                        lphone.getText().toString().trim(),
                        mobile1.getText().toString().trim(),
                        mobile2.getText().toString().trim(),
                        mobile3.getText().toString().trim(),
                        optionsInput[0].getText().toString().trim(),
                        optionsInput[1].getText().toString().trim(),
                        optionsInput[2].getText().toString().trim(),
                        optionsInput[3].getText().toString().trim(),
                        optionsInput[4].getText().toString().trim()
                );
            }
        });

        root.findViewById(R.id.spin_site)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        new GetOptionsListTask().execute(3);
                    }
                });

        root.findViewById(R.id.spin_street)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        new GetOptionsListTask().execute(2);
                    }
                });
        root.findViewById(R.id.spin_class_year)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        new GetOptionsListTask().execute(0);

                    }
                });
        root.findViewById(R.id.spin_priest)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        new GetOptionsListTask().execute(4);

                    }
                });
        root.findViewById(R.id.spin_study_work)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        new GetOptionsListTask().execute(1);

                    }
                });
        return root;
    }


    private class EditTask extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog pBar;
        private int msgSource;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(getActivity());
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {

            pBar.dismiss();
            Toast.makeText(getActivity(), msgSource,
                    Toast.LENGTH_SHORT).show();
//            if (result) {
            //    finish();
//                startActivity(new Intent(getActivity(),
//                        DisplayContact.class).putExtra("id", contactData.getId()));
//            }

        }

        @Override
        protected Boolean doInBackground(String... params) {
            DB dbm = DB.getInstant(getActivity());
            String check = dbm.getNameId(params[0]);
            if (!Utility.isName(params[0])) {
                msgSource = R.string.err_msg_invalid_name;
            } else if (!check.equals("-1") && !check.equals(contactData.getId())) {
                msgSource = R.string.err_msg_duplicate_name;
            } else if (params[4].length() != 0 && !Utility.isEmail(params[4])) {
                msgSource = R.string.err_msg_email;
            } else if (params[12].length() != 0 && !Utility.isSiteName(params[12])) {
                msgSource = R.string.err_msg_site;
            } else {


                ContentValues values = new ContentValues();
                values.put(DB.CONTACT_NAME, params[0]);
                values.put(DB.CONTACT_SUPERVISOR, params[13]);
                values.put(DB.CONTACT_NOTES, params[3]);
                values.put(DB.CONTACT_BDAY, params[2]);
                values.put(DB.CONTACT_EMAIL, params[4]);
                values.put(DB.CONTACT_MOB1, params[6]);
                values.put(DB.CONTACT_MOB2, params[7]);
                values.put(DB.CONTACT_MOB3, params[8]);
                values.put(DB.CONTACT_LPHONE, params[5]);
                values.put(DB.CONTACT_ADDR, params[1]);
                values.put(DB.CONTACT_ST, params[11]);
                values.put(DB.CONTACT_SITE, params[12]);
                values.put(DB.CONTACT_STUDY_WORK, params[10]);
                values.put(DB.CONTACT_CLASS_YEAR, params[9]);

                dbm.updateContact(values, Utility.getBytes(photo), contactData.getId());
                msgSource = R.string.msg_updated;
                return true;
            }
            return false;

        }
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

    private class GetOptionsListTask extends
            AsyncTask<Integer, Void, ArrayList<String>> {
        private ProgressDialog pBar;
        private int position;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(getActivity());
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected ArrayList<String> doInBackground(Integer... params) {
            position = params[0];
            return DB.getInstant(getActivity()).getOptionsList(optionsTags[position]);
        }

        @Override
        protected void onPostExecute(final ArrayList<String> result) {
            pBar.dismiss();
            if (result.size() == 0) {
                Toast.makeText(getActivity(),
                        R.string.msg_no_available_entries, Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    getActivity());
            builder.setTitle(getResources().getString(optionsChoose[position]));
            builder.setItems(
                    result.toArray(new String[result.size()]),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            optionsInput[position].setText(result.get(which));

                        }

                    });
            builder.create().show();
        }
    }


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
            reset();
            pBar.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            contactData = DB.getInstant(getActivity()).getContactInfo(id);
            return null;
        }
    }

    private void reset() {

        name.setText(contactData.getName());
        address.setText(contactData.getAddress());
        comm.setText(contactData.getComm());
        email.setText(contactData.getEmail());
        lphone.setText(contactData.getLandPhone());

        mobile1.setText(contactData.getMobile1());
        mobile2.setText(contactData.getMobile2());
        mobile3.setText(contactData.getMobile3());

        optionsInput[0].setText(contactData.getClassYear());
        optionsInput[1].setText(contactData.getStudyWork());
        optionsInput[2].setText(contactData.getStreet());
        optionsInput[3].setText(contactData.getSite());
        optionsInput[4].setText(contactData.getPriest());

        date.setText(contactData.getBirthDay());

        photo = contactData.getPhoto();
        if (photo != null)
            img.setImageBitmap(photo);
        else
            img.setImageResource(R.mipmap.def);

    }

    private void editImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.label_choose_photo);
        builder.setItems(
                getResources().getStringArray(
                        R.array.photo_menu),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent galleryIntent = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(galleryIntent, BROWSE_IMG);
                        } else if (which == 1) {
                            captureImage();

                        } else {
                            photo = null;
                            img.setImageResource(R.mipmap.def);

                        }
                    }

                });
        builder.create().show();

    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String path;
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/";
        } else {
            path = android.os.Environment.getDataDirectory().getAbsolutePath()
                    + "/";
        }

        path += "img_"
                + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(new Date()) + ".jpg";
        fileUri = Uri.fromFile(new File(path));

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        startActivityForResult(intent, TAKE_IMG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == TAKE_IMG) {

                photo = Utility.getBitmap(fileUri.getPath());
                if (photo != null)
                    img.setImageBitmap(photo);

            } else if (requestCode == BROWSE_IMG) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                photo = Utility.getBitmap(
                        cursor.getString(cursor.getColumnIndex(filePathColumn[0])));
                if (photo != null)
                    img.setImageBitmap(photo);
                cursor.close();
            }
        }
    }

}