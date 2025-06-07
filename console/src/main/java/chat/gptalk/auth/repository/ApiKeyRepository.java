package chat.gptalk.auth.repository;

import chat.gptalk.common.entity.VirtualKeyEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiKeyRepository extends CrudRepository<VirtualKeyEntity, Integer> {

}
