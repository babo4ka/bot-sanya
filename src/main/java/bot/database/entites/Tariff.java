package bot.database.entites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "тарифы")
public class Tariff {

    @Id
    @Column(name="id_тарифа")
    private long ID;
    @Column(name = "название_тарифа")
    private String name;
    @Column(name = "цена")
    private int price;

    public long getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    @Column(name = "краткое_описание")
    private String shortDesc;
}
