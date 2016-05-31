package abanoubm.dayra.model;

public class ContactUpdate extends ContactDay {
    private boolean isSelected;

    public ContactUpdate(String id, String name, String day,
                         boolean isSelected, String picDir) {
        super(id, name, day, picDir);
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

}
