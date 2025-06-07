package chat.gptalk.auth.repository;

import chat.gptalk.common.entity.VirtualKeyEntity;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VirtualKeyRepository extends CrudRepository<VirtualKeyEntity, Integer> {

    List<VirtualKeyEntity> findAll(Sort sort);

    boolean existsByTenantIdAndName(UUID tenantId, String name);

    List<VirtualKeyEntity> findAllByTenantIdAndVirtualKeyIdIn(UUID tenantId, Collection<UUID> virtualKeyId);

    VirtualKeyEntity findAllByTenantIdAndVirtualKeyId(UUID tenantId, UUID virtualKeyId);

    void deleteByTenantIdAndVirtualKeyIdIn(UUID tenantId, List<UUID> ids);
}
