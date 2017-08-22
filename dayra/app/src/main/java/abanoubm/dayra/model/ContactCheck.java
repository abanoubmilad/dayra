package abanoubm.dayra.model;

import android.graphics.Bitmap;

public class ContactCheck extends ContactID {
    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public ContactCheck(String id, String name, byte [] photo, boolean checked) {
        super(id, name, photo);
        this.checked = checked;
    }

}
