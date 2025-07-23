package chat.gptalk.connector.model;

import java.util.UUID;

public record VirtualModel(
    UUID virtualModelId,
    String name) {

}
