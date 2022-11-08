package bot.database.entites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="дополнительное_тарифа")
public class Extra_inter {

    @Id
    @Column(name="порядковый_номер_д")
    private long ID;
    @Column(name = "id_тарифа_для_дополнительно")
    private long tariff_id;
    @Column(name = "id_дополнительно_для_тарифа")
    private long extra_id;

    public long getID() {
        return ID;
    }


    public long getTariff_id() {
        return tariff_id;
    }

    public void setTariff_id(long tariff_id) {
        this.tariff_id = tariff_id;
    }

    public long getExtra_id() {
        return extra_id;
    }

    public void setExtra_id(long extra_id) {
        this.extra_id = extra_id;
    }
}
