package bot.database.entites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "дополнительно")
public class Extra {

    @Id
    @Column(name="id_дополнительно")
    private long ID;

    public long getID() {
        return ID;
    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Column(name = "значение_дополнительно")
    private String value;
}
