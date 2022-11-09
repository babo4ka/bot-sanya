package bot.database.entites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "сервисы")
public class Service {

    @Id
    @Column(name="id_сервиса")
    private long ID;

    @Column(name = "название_сервиса")
    private String name;
    @Column(name = "значение_сервиса")
    private String value;

    @Column(name ="эмодзи")
    private String emoji;

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public long getID() {
        return ID;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
