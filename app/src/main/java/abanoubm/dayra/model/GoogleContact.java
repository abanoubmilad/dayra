package abanoubm.dayra.model;

public class GoogleContact {
    private String name;
    private String mobile;
    private boolean isSelected;
    private boolean isExisted;

    public boolean isExisted() {
        return isExisted;
    }

    public GoogleContact(String name, String mobile, boolean isExisted) {
        this.name = name;
        this.mobile = mobile;
        this.isSelected = false;
        this.isExisted = isExisted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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