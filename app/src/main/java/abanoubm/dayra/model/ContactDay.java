package abanoubm.dayra.model;

import android.graphics.Bitmap;

public class ContactDay extends ContactID {
    private String day;

    public ContactDay(String id, String name, String day, Bitmap photo) {
        super(id, name, photo);
        this.day = day;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

}
