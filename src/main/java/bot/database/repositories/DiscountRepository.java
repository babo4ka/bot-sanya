package bot.database.repositories;

import bot.database.entites.Discount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface DiscountRepository extends CrudRepository<Discount, Long> {
    @Query(value = "delete from акционные_тарифы where id_тарифа_для_акции = :tariff_id",
    nativeQuery = true)
    void deleteByTariffId(@Param("tariff_id") long tariff_id);
}
