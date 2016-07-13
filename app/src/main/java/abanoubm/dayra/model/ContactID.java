package abanoubm.dayra.model;

import android.graphics.Bitmap;

public class ContactID {
    private String id;
    private String name;
    private Bitmap photo;

    public ContactID(String id, String name, Bitmap photo) {
        this.id = id;
        this.name = name;
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Bitmap getPhoto() {
        return photo;
    }


    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }
}
