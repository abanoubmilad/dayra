package abanoubm.dayra.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.model.ContactSort;

public class ContactSortAdapter extends ArrayAdapter<ContactSort> {

    public ContactSortAdapter(Context context, ArrayList<ContactSort> contacts) {
        super(context, 0, contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ContactSort contact = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_contact_sort, parent, false);
        }

        ImageView img = (ImageView) convertView.findViewById(R.id.attendantImg);

        if (contact.getPicDir().length() != 0
                && new File(contact.getPicDir()).exists()) {
            img.setImageBitmap(ThumbnailUtils.extractThumbnail(
                    BitmapFactory.decodeFile(contact.getPicDir()), 100, 100));
        } else {
            img.setImageResource(R.mipmap.def);
        }
        ((TextView) convertView.findViewById(R.id.name)).setText(contact
                .getName());
        ((TextView) convertView.findViewById(R.id.class_year)).setText(contact
                .getClassYear());
        ((TextView) convertView.findViewById(R.id.study_work)).setText(contact
                .getStudyWork());
        ((TextView) convertView.findViewById(R.id.street)).setText(contact
                .getStreet());
        ((TextView) convertView.findViewById(R.id.site)).setText(contact
                .getSite());
        ((TextView) convertView.findViewById(R.id.conf_father)).setText(contact
                .getPriest());
        ((TextView) convertView.findViewById(R.id.last_attendance))
                .setText(contact.getLastAttend());
        ((TextView) convertView.findViewById(R.id.last_visit)).setText(contact
                .getLastVisit());
        ((TextView) convertView.findViewById(R.id.bday)).setText(contact
                .getBirthDay());

        return convertView;
    }
}