package bot.database.entites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "сервисы_тарифа")
public class Service_inter {

    @Id
    @Column(name="порядковый_номер_с")
    private long ID;
    @Column(name = "id_тарифа_для_сервиса")
    private long tariff_id;
    @Column(name = "id_сервиса_для_тарифа")
    private long service_id;

    public long getID() {
        return ID;
    }

    public long getTariff_id() {
        return tariff_id;
    }

    public void setTariff_id(long tariff_id) {
        this.tariff_id = tariff_id;
    }

    public long getService_id() {
        return service_id;
    }

    public void setService_id(long service_id) {
        this.service_id = service_id;
    }
}
