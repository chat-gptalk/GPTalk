package chat.gptalk.auth.repository;

import chat.gptalk.common.entity.LlmModelEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelRepository extends CrudRepository<LlmModelEntity, Integer> {

    List<LlmModelEntity> findByTenantId(UUID tenantId);

    LlmModelEntity findByModelId(UUID modelId);

    List<LlmModelEntity> findByProviderId(UUID providerId);

    @Override
    List<LlmModelEntity> findAll();

    Integer countByProviderId(UUID providerId);
}
