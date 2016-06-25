package abanoubm.dayra.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import abanoubm.dayra.R;

public class StringAdapter extends Adapter<String> {

    public StringAdapter(Context context, ArrayList<String> strs) {
        super(context, 0, strs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_string, parent, false);
            holder = new ViewHolder();
            holder.item = (TextView) convertView.findViewById(R.id.item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.item.setText(getItem(position));


        return convertView;
    }

    private static class ViewHolder {
        TextView item;
    }
}