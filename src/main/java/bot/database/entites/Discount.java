package bot.database.entites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity(name = "акционные_тарифы")
public class Discount {

    @Id
    @Column(name = "порядковый_номер_акции")
    private long ID;
    @Column(name = "id_тарифа_для_акции")
    private int tariffId;
    @Column(name = "цена_по_акции")
    private int price;
    @Column(name = "дата_окончания")
    private Timestamp endDate;

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public int getTariff_id() {
        return tariffId;
    }

    public void setTariff_id(int tariff_id) {
        this.tariffId = tariff_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
