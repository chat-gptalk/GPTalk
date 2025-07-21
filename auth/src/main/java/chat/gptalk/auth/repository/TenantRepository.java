package chat.gptalk.auth.repository;

import chat.gptalk.common.entity.TenantEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantRepository extends CrudRepository<TenantEntity, Integer> {

}
