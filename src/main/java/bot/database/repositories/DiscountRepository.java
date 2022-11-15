package bot.database.repositories;

import bot.database.entites.Discount;
import org.springframework.data.repository.CrudRepository;

public interface DiscountRepository extends CrudRepository<Discount, Long> {
}
