package chat.gptalk.auth.repository;

import chat.gptalk.common.entity.LlmProviderKeyEntity;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderKeyRepository extends CrudRepository<LlmProviderKeyEntity, Integer> {

    Integer countByTenantIdAndProviderIdIn(UUID tenantId, List<UUID> providerIds);

    List<LlmProviderKeyEntity> findByTenantId(UUID tenantId, Sort sort);

    boolean existsByTenantIdAndName(UUID tenantId, @NotBlank String name);

    void deleteByTenantIdAndProviderKeyIdIn(UUID tenantId, List<UUID> ids);

    LlmProviderKeyEntity findOneByTenantIdAndProviderKeyId(UUID tenantId, UUID keyId);

    boolean existsByTenantIdAndIdNotAndName(UUID tenantId, Integer id, String name);
}
