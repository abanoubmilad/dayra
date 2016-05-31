package abanoubm.dayra.model;

public class ContactID {
    private String id;
    private String name;
    private String picDir;

    public ContactID(String id, String name, String picDir) {
        super();
        this.id = id;
        this.name = name;
        this.picDir = picDir;
    }

    public ContactID() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
