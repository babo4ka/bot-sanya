package bot.database.entites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "теги_тарифа")
public class Tags_inter {

    @Id
    @Column(name="порядковый_номер_т")
    private long ID;
    @Column(name = "id_тарифа_для_тегов")
    private long tariff_id;

    public long getID() {
        return ID;
    }

    public long getTariff_id() {
        return tariff_id;
    }

    public void setTariff_id(long tariff_id) {
        this.tariff_id = tariff_id;
    }

    public long getTag_id() {
        return tag_id;
    }

    public void setTag_id(long tag_id) {
        this.tag_id = tag_id;
    }

    @Column(name = "id_тега_для_тарифа")
    private long tag_id;
}
