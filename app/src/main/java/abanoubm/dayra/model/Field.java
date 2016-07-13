package abanoubm.dayra.model;

import android.graphics.Bitmap;

public class Field {

    private String name;
    private Bitmap photo;
    private String phone;
    private String day;

    public Field(String name, Bitmap photo, String phone, String day) {
        this.name = name;
        this.photo = photo;
        this.phone = phone;
        this.day = day;
    }

    public String getName() {
        return name;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public String getPhone() {
        return phone;
    }

    public String getDay() {
        return day;
    }
}
