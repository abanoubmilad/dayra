package abanoubm.dayra.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
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
        ViewHolder holder;

        ContactSort contact = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_contact_sort, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.class_year = (TextView) convertView.findViewById(R.id.class_year);
            holder.study_work = (TextView) convertView.findViewById(R.id.study_work);
            holder.street = (TextView) convertView.findViewById(R.id.street);
            holder.site = (TextView) convertView.findViewById(R.id.site);
            holder.conf_father = (TextView) convertView.findViewById(R.id.conf_father);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (contact.getPicDir().length() != 0
                && new File(contact.getPicDir()).exists()) {
            holder.img.setImageBitmap(ThumbnailUtils.extractThumbnail(
                    BitmapFactory.decodeFile(contact.getPicDir()), 100, 100));
        } else {
            holder.img.setImageResource(R.mipmap.def);
        }
        holder.name.setText(contact
                .getName());
        holder.class_year.setText(contact
                .getClassYear());
        holder.study_work.setText(contact
                .getStudyWork());
        holder.street.setText(contact
                .getStreet());
        holder.site.setText(contact
                .getSite());
        holder.conf_father.setText(contact
                .getPriest());
        return convertView;
    }

    private static class ViewHolder {
        TextView name, class_year, study_work, street, site, conf_father;
        ImageView img;
    }
}