package chat.gptalk.connector.sp.openai;

import chat.gptalk.connector.sp.LLMClient;

public interface OpenAiClient extends LLMClient {

    @Override
    default String name() {
        return "openai";
    }
}
