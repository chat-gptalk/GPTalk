package chat.gptalk.auth.repository;

import chat.gptalk.common.entity.LlmProviderEntity;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRepository extends CrudRepository<LlmProviderEntity, Integer> {

    LlmProviderEntity findByProviderId(UUID providerId);

    List<LlmProviderEntity> findByTenantIdOrSystemOrderByIdDesc(UUID tenantId, Boolean system);

    boolean existsByNameAndTenantId(String name, UUID tenantId);

    List<LlmProviderEntity> findAllByTenantIdAndProviderIdIn(UUID tenantId, Collection<UUID> providerIds);

    void deleteByProviderIdIn(Collection<UUID> providerIds);

    LlmProviderEntity findOneByTenantIdAndProviderIdOrSystem(UUID tenantId, UUID providerId, Boolean system);
}
