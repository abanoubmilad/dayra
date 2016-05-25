package abanoubm.dayra.obj;

public class ContactConnection extends ContactID {
    private boolean isCon;

    public boolean isCon() {
        return isCon;
    }

    public void setCon(boolean isCon) {
        this.isCon = isCon;
    }

    public ContactConnection(int id, String name, String picDir, boolean isCon) {
        super(id, name, picDir);
        this.isCon = isCon;
    }

}
