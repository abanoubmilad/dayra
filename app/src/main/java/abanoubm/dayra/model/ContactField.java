package abanoubm.dayra.model;

import android.graphics.Bitmap;

public class ContactField extends ContactID {
    private String field;

    public ContactField(String id, String name, String field, Bitmap photo) {
        super(id, name, photo);
        this.field = field;
    }

    public String getField() {
        return field;
    }

}
