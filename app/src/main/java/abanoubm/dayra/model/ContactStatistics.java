package abanoubm.dayra.model;

import android.graphics.Bitmap;

public class ContactStatistics extends ContactID {
    private String minDay, maxDay;
    private int daysCount;

    public ContactStatistics(String id, String name, byte [] photo, String minDay, String maxDay, int daysCount) {
        super(id, name, photo);
        this.minDay = minDay;
        this.maxDay = maxDay;
        this.daysCount = daysCount;
    }

    public String getMinDay() {
        return minDay;
    }


    public String getMaxDay() {
        return maxDay;
    }


    public int getDaysCount() {
        return daysCount;
    }

}
