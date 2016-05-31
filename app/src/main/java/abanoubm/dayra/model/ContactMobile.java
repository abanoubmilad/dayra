package abanoubm.dayra.model;

public class ContactMobile extends ContactID {
    private String mobile;
    private boolean isSelected;

    public ContactMobile(String id, String name, String picDir, String mobile) {
        super(id, name, picDir);
        this.mobile = mobile;
        this.isSelected = false;
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
