package abanoubm.dayra.model;

public class ContactMobile extends ContactID {
    private String mobile;
    private boolean isSelected;
    private boolean isExisted;

    public boolean isExisted() {
        return isExisted;
    }


    public ContactMobile(String id, String name, String picDir, String mobile, boolean isExisted) {
        super(id, name, picDir);
        this.mobile = mobile;
        this.isSelected = false;
        this.isExisted = isExisted;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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
