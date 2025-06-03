package chat.gptalk.connector.sp.model.moderation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ModerationResult(
    @JsonProperty("flagged") boolean flagged,
    @JsonProperty("categories") Categories categories,
    @JsonProperty("category_scores") CategoryScores categoryScores) {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Categories(
        @JsonProperty("sexual") boolean sexual,
        @JsonProperty("hate") boolean hate,
        @JsonProperty("harassment") boolean harassment,
        @JsonProperty("self-harm") boolean selfHarm,
        @JsonProperty("sexual/minors") boolean sexualMinors,
        @JsonProperty("hate/threatening") boolean hateThreatening,
        @JsonProperty("violence/graphic") boolean violenceGraphic,
        @JsonProperty("self-harm/intent") boolean selfHarmIntent,
        @JsonProperty("self-harm/instructions") boolean selfHarmInstructions,
        @JsonProperty("harassment/threatening") boolean harassmentThreatening,
        @JsonProperty("violence") boolean violence) {

    }

    public record CategoryScores(
        @JsonProperty("sexual") double sexual,
        @JsonProperty("hate") double hate,
        @JsonProperty("harassment") double harassment,
        @JsonProperty("self-harm") double selfHarm,
        @JsonProperty("sexual/minors") double sexualMinors,
        @JsonProperty("hate/threatening") double hateThreatening,
        @JsonProperty("violence/graphic") double violenceGraphic,
        @JsonProperty("self-harm/intent") double selfHarmIntent,
        @JsonProperty("self-harm/instructions") double selfHarmInstructions,
        @JsonProperty("harassment/threatening") double harassmentThreatening,
        @JsonProperty("violence") double violence) {

    }
}

