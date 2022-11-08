package bot.database.entites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "теги")
public class Tags {

    @Id
    @Column(name="id_тега")
    private long ID;
    @Column(name = "название_тега")
    private String name;

    public long getID() {
        return ID;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
