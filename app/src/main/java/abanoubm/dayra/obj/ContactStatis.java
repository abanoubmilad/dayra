package abanoubm.dayra.obj;

public class ContactStatis extends ContactID {
    private int days;

    public ContactStatis(int id, String name, int days, String picDir) {
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
