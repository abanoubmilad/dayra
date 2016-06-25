package abanoubm.dayra.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.model.DayCheck;

public class DayCheckAdapter extends ArrayAdapter<DayCheck> {

    public DayCheckAdapter(Context context, ArrayList<DayCheck> contacts) {
        super(context, 0, contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        DayCheck day = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_day_check, parent, false);
            holder = new ViewHolder();
            holder.day = (TextView) convertView.findViewById(R.id.day);
            holder.root = convertView.findViewById(R.id.root);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.day.setText(day.getDay());
        holder.root.setBackgroundColor(
                ContextCompat.getColor(getContext(),day.isChecked()?R.color.hotgreen: R.color.red));


        return convertView;
    }

    private static class ViewHolder {
        TextView day;
        View root;
    }
}