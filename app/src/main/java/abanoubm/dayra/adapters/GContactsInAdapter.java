package abanoubm.dayra.adapters;

import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.model.GoogleContact;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class GContactsInAdapter extends ArrayAdapter<GoogleContact> {

	public GContactsInAdapter(Context context,
							  ArrayList<GoogleContact> contacts) {
		super(context, 0, contacts);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		GoogleContact contact = getItem(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.item_gcontact_in, parent, false);
		}


		((TextView) convertView.findViewById(R.id.name)).setText(contact.getName());
		((TextView) convertView.findViewById(R.id.mobile)).setText(contact.getMobile());

		CheckBox flag = (CheckBox) convertView.findViewById(R.id.flag2);
		flag.setChecked(contact.isSelected());

		return convertView;
	}
}