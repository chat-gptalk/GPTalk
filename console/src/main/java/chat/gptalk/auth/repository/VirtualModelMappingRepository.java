package chat.gptalk.auth.repository;

import chat.gptalk.common.entity.VirtualModelMappingEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VirtualModelMappingRepository extends CrudRepository<VirtualModelMappingEntity, Integer> {

    List<VirtualModelMappingEntity> findByVirtualModelId(UUID virtualModelId);

    void deleteByModelId(UUID virtualModelId);
}
