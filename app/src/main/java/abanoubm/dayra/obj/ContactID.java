package abanoubm.dayra.obj;

public class ContactID {
    private int id;
    private String name;
    private String picDir;

    public ContactID(int id, String name, String picDir) {
        super();
        this.id = id;
        this.name = name;
        this.picDir = picDir;
    }

    public ContactID() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPicDir() {
        return picDir;
    }

    public void setPicDir(String picDir) {
        this.picDir = picDir;
    }

    public void setName(String name) {
        this.name = name;
    }

}
