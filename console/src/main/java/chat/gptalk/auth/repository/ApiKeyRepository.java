package chat.gptalk.auth.repository;

import chat.gptalk.common.entity.ApiKeyEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiKeyRepository extends CrudRepository<ApiKeyEntity, Integer> {

}
