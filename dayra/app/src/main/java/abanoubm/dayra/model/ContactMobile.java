package abanoubm.dayra.model;

import android.graphics.Bitmap;

public class ContactMobile extends ContactID {
    private String mobile;
    private boolean isSelected;
    private boolean isExisted;

    public boolean isExisted() {
        return isExisted;
    }


    public ContactMobile(String id, String name, byte [] photo, String mobile, boolean isExisted) {
        super(id, name, photo);
        this.mobile = mobile;
        this.isSelected = false;
        this.isExisted = isExisted;
    }

    public String getMobile() {
        return mobile;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void invertSelected() {
        isSelected = !isSelected;
    }
}
