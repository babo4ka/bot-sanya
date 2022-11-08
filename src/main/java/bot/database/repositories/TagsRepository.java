package bot.database.repositories;

import bot.database.entites.Tags;
import org.springframework.data.repository.CrudRepository;

public interface TagsRepository extends CrudRepository<Tags, Long> {
}
