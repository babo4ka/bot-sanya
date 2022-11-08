package bot.database.entites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "оборудование")
public class Equip {

    @Id
    @Column(name="id_оборудования")
    private long ID;

    @Column(name = "значение_оборудования")
    private String value;

    public long getID() {
        return ID;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
