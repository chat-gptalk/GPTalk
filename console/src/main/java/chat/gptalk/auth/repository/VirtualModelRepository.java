package chat.gptalk.auth.repository;

import chat.gptalk.common.entity.VirtualModelEntity;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VirtualModelRepository extends CrudRepository<VirtualModelEntity, Integer> {

    List<VirtualModelEntity> findAll(Sort sort);

    boolean existsByTenantIdAndName(UUID tenantId, String name);

    void deleteByVirtualModelIdIn(Collection<UUID> virtualModelIds);

    List<VirtualModelEntity> findAllByTenantIdAndVirtualModelIdIn(UUID tenantId, Collection<UUID> virtualModelId);
}
