package bot.database.entites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "оборудование_тарифа")
public class Equip_inter {

    @Id
    @Column(name="порядковый_номер_о")
    private long ID;
    @Column(name = "id_тарифа_для_оборудования")
    private long tariff_id;
    @Column(name = "id_оборудования_для_тарифа")
    private long equip_id;

    public long getID() {
        return ID;
    }


    public long getTariff_id() {
        return tariff_id;
    }

    public void setTariff_id(long tariff_id) {
        this.tariff_id = tariff_id;
    }

    public long getEquip_id() {
        return equip_id;
    }

    public void setEquip_id(long equip_id) {
        this.equip_id = equip_id;
    }

}
