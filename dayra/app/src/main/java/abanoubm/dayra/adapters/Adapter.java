package abanoubm.dayra.adapters;

import android.content.Context;
import android.os.Build;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class Adapter<T> extends ArrayAdapter<T> {

    public Adapter(Context context, int resource, ArrayList<T> entries) {
        super(context, resource, entries);
    }

    public void clearThenAddAll(ArrayList<T> list) {
        setNotifyOnChange(false);
        clear();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            super.addAll(list);
        } else {
            for (T element : list)
                super.add(element);

        }
        setNotifyOnChange(true);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<T> list) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            super.addAll(list);
        } else {
            setNotifyOnChange(false);
            for (T element : list)
                super.add(element);
            setNotifyOnChange(true);
            notifyDataSetChanged();
        }
    }
}