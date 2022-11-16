package bot.database.repositories;

import bot.database.entites.Discount;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DiscountRepository extends CrudRepository<Discount, Long> {
    Optional<Discount> findByTariffId(int id);
}
