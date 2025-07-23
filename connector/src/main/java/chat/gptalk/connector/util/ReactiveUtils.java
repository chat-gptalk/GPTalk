package chat.gptalk.connector.util;

import java.util.function.Supplier;
import reactor.core.publisher.Mono;

public class ReactiveUtils {

    public static <T> Mono<T> ensureTrue(Mono<Boolean> condition, Supplier<T> onSuccess, Throwable error) {
        return condition
            .filter(Boolean::booleanValue)
            .switchIfEmpty(Mono.error(error))
            .map(ignore -> onSuccess.get());
    }

    public static <T> Mono<T> ensureTrue(Mono<Boolean> condition, Mono<T> success, Throwable error) {
        return condition
            .filter(Boolean::booleanValue)
            .switchIfEmpty(Mono.error(error))
            .then(success);
    }
}
