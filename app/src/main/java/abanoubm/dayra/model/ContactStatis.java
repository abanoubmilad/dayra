package abanoubm.dayra.model;

public class ContactStatis extends ContactID {
    private int days;

    public ContactStatis(String id, String name, int days, String picDir) {
        super(id, name, picDir);
        this.days = days;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

}
