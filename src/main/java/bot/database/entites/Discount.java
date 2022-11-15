package bot.database.entites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "акционные_тарифы")
public class Discount {

    @Id
    @Column(name = "порядковый_номер_акции")
    private long ID;
    @Column(name = "id_тарифа_для_акции")
    private int tariff_id;
    @Column(name = "цена_по_акции")
    private int price;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public int getTariff_id() {
        return tariff_id;
    }

    public void setTariff_id(int tariff_id) {
        this.tariff_id = tariff_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
