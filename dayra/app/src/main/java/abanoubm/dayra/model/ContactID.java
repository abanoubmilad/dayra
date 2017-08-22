package abanoubm.dayra.model;

import android.graphics.Bitmap;

public class ContactID {
    private String id;
    private String name;
    private byte [] photo;

    public ContactID(String id, String name, byte [] photo) {
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

    public byte [] getPhoto() {
        return photo;
    }


    public void setPhoto(byte [] photo) {
        this.photo = photo;
    }
}
