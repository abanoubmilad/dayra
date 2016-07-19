package abanoubm.dayra.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.model.ContactDisplayList;

public class ContactsDisplayListAdapter extends Adapter<ContactDisplayList> {

    private int selected = -1;

    public ContactsDisplayListAdapter(Context context, ArrayList<ContactDisplayList> contacts) {
        super(context, 0, contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        ContactDisplayList contact = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_display_contact_list, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.class_year = (TextView) convertView.findViewById(R.id.class_year);
            holder.study_work = (TextView) convertView.findViewById(R.id.study_work);
            holder.street = (TextView) convertView.findViewById(R.id.street);
            holder.site = (TextView) convertView.findViewById(R.id.site);
            holder.conf_father = (TextView) convertView.findViewById(R.id.conf_father);
            holder.root = convertView.findViewById(R.id.root);
            holder.home = (TextView) convertView.findViewById(R.id.home);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (contact.getPhoto() != null)
            holder.img.setImageBitmap(contact.getPhoto());
        else
            holder.img.setImageResource(R.mipmap.def);

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
        holder.home.setText(contact
                .getHome());

        if (selected == position)
            holder.root.setBackgroundColor(
                    ContextCompat.getColor(getContext(), R.color.colorAccent));
        else
            holder.root.setBackgroundResource(R.drawable.dynamic_bg);

        return convertView;
    }

    private static class ViewHolder {
        TextView name, class_year, study_work, street, site, conf_father, home;
        ImageView img;
        View root;
    }

    public void setSelectedIndex(int pos) {
        selected = pos;
        notifyDataSetChanged();
    }
}