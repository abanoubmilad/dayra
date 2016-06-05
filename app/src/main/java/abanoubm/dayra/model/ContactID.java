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

    public ContactID() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Bitmap getPhoto() {
        return photo;
    }


    public void setName(String name) {
        this.name = name;
    }

}
