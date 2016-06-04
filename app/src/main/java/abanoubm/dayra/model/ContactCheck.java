package abanoubm.dayra.model;

public class ContactCheck extends ContactID {
    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public ContactCheck(String id, String name, String picDir, boolean checked) {
        super(id, name, picDir);
        this.checked = checked;
    }

}
