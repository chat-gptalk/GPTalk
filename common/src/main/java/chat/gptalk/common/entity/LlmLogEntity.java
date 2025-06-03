package chat.gptalk.common.entity;


import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "usages")
public record LlmLogEntity(
    @Id
    Long id,
    UUID userId,
    UUID tenantId,
    UUID llmKeyId,
    UUID modelId,
    UUID virtualModelId,
    UUID apiKeyId,
    String endUserId,

    Integer promptTokens,
    Integer cachedTokens,
    Integer completionTokens,
    Integer totalTokens,

    Integer functionCalls,

    Integer inputCharacters,
    String responseFormat,

    Boolean cached,
    Boolean stream,

    Integer imageCount,
    String imageResolution,
    Long imageBytes,
    Integer audioDurationMs,

    String requestData,   // Consider using `JsonNode` or custom type if using Jackson
    String responseData,  // Same here
    Integer responseStatus,
    String errorMessage,  // 推荐新增，用于记录非 200 响应的错误简述

    Double cost,
    OffsetDateTime createdAt
) {

}

