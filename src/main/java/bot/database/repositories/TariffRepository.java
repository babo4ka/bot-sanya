package bot.database.repositories;

import bot.database.entites.Tariff;
import org.springframework.data.repository.CrudRepository;

public interface TariffRepository extends CrudRepository<Tariff, Long> {
}
