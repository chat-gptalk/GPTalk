package chat.gptalk.connector.sp;

import chat.gptalk.common.exception.BadRequestException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ModelClientFactory {

    private final Map<String, ChatClient> chatClientMap;

    @Autowired
    public ModelClientFactory(ApplicationContext context) {
        this.chatClientMap = context.getBeansOfType(ChatClient.class);
    }

    public Mono<ChatClient> getChatClient(String model) {
        return Flux.fromIterable(chatClientMap.values())
            .filterWhen(it -> it.support(model).map(supported -> supported))
            .next()
            .switchIfEmpty(Mono.error(new BadRequestException("Unsupported model: " + model)));
    }
}

