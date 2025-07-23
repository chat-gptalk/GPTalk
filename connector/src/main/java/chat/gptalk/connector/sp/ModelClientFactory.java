package chat.gptalk.connector.sp;

import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ModelClientFactory {

    private final Map<String, ChatClient> chatClientMap;

    @Autowired
    public ModelClientFactory(ApplicationContext context) {
        Map<String, ChatClient> beans = context.getBeansOfType(ChatClient.class);
        this.chatClientMap = beans.values().stream().collect(Collectors.toMap(ChatClient::name, it -> it));
    }

    public Mono<ChatClient> getChatClient(String clientName) {
        return Mono.justOrEmpty(chatClientMap.get(clientName));
    }
}

