package bot.database.repositories;

import bot.database.entites.Service;
import org.springframework.data.repository.CrudRepository;

public interface ServiceRepository extends CrudRepository<Service, Long> {
}
