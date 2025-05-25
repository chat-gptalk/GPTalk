# GPTalk

> A lightweight, pluggable LLM gateway and orchestration framework built on **Spring AI**.
> Designed for real-world applications: **multi-provider**, **RAG-ready**, **rate-limited**, **observable**, and **production-ready**.

## ❤️ Why GPTalk?

"LangChain is for Python. GPTalk is for Java.
And it speaks Spring natively."


## 🌟 Overview

**GPTalk** is an open-source LLM (Large Language Model) gateway and orchestration framework built for Java developers. It is powered by [Spring AI](https://docs.spring.io/spring-ai/) and follows the Spring Boot philosophy: convention over configuration, modularity, and production readiness.

Whether you're building a chatbot, integrating RAG (Retrieval-Augmented Generation), or managing multi-provider LLM traffic, GPTalk provides a robust foundation with support for observability, token tracking, rate-limiting, and more.


## 🔧 Features

| Feature                   | Description                                                                      |
| ------------------------- | -------------------------------------------------------------------------------- |
| ✅ Multi-LLM Provider      | Supports OpenAI, Azure OpenAI, Anthropic Claude, Alibaba Qwen, Baidu ERNIE, etc. |
| ✅ OpenAI-Compatible API   | Acts as a drop-in replacement for OpenAI-style APIs                              |
| ✅ RAG-Ready               | Provides utilities for document splitting, embedding, and context injection      |
| ✅ Rate Limiting           | Token/request-based rate limiting per user/token/key                             |
| ✅ Observability           | Prometheus metrics, OpenTelemetry tracing, logging, and audit hooks              |
| ✅ Extensible Architecture | Easily add new providers, modify logging/storage/metrics mechanisms              |
| ✅ Spring Boot Native      | Supports auto-configuration, WebFlux, and cloud-native patterns                  |


## 🚀 Getting Started

### Prerequisites

* Java 17+
* Spring Boot 3.2+
* Maven or Gradle

### Example (Spring Boot)

```java
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatClient chatClient;

    @PostMapping("/chat")
    public String chat(@RequestBody String prompt) {
        ChatResponse response = chatClient.call(prompt);
        return response.getResult();
    }
}
```

Configuration example:

```yaml
spring:
  ai:
    openai:
      api-key: your-api-key
      chat:
        options:
          model: gpt-4
```

More examples can be found in the `examples/` folder.

---

## 📆 Module Structure

```bash
gptalk/
├── gptalk-core           # Core abstractions and interfaces
├── gptalk-provider       # LLM provider implementation
├── gptalk-metrics        # Prometheus / OpenTelemetry instrumentation
├── gptalk-rag            # Document splitting, vector store, RAG pipeline
├── gptalk-rate-limit     # Token/request-based rate limiter
├── gptalk-gateway        # OpenAI-compatible gateway with Spring Cloud Gateway
├── gptalk-logging        # Logging, audit, and request tracing
└── examples/             # Ready-to-run demos for various providers
```

---

## 📊 Observability

GPTalk provides built-in support for:

* **Prometheus**: export metrics for requests, token usage, latency
* **OpenTelemetry**: traces for each LLM call
* **Custom Logging Hooks**: store chat logs in your database or external systems

---

## ⛔️ Rate Limiting & Usage Tracking

GPTalk supports:

* Token-based rate limits (per user/API key)
* Redis-based usage counters
* Configurable thresholds for different models
* Usage logs for audit and billing purposes

---

## 🧠 RAG Support

The `gptalk-rag` module supports:

* Text splitting (recursive, sentence-based)
* Embedding generation (OpenAI, HuggingFace, etc.)
* Vector store integration (Weaviate, Pinecone, FAISS)
* Context injection into prompts

---

## 🌐 REST API Gateway

`gptalk-gateway` provides an OpenAI-compatible API layer:

* `POST /v1/chat/completions`
* `POST /v1/completions`
* `POST /v1/embeddings`

Supports routing to different models/providers based on request content.


## 🎯 Roadmap

* [ ] OpenAI-compatible API proxy
* [ ] RAG module (document -> vector -> context)
* [ ] Token-based rate limiting
* [ ] Prometheus/OpenTelemetry integration
* [ ] Function Calling & Tool Use
* [ ] Web-based dashboard for usage stats
* [ ] SDK clients (Java, Python)

## ❤️ Join Us

We welcome contributions from the community! If you're interested in:

* Adding support for more LLMs
* Improving the RAG components
* Enhancing the metrics or rate limiter
* Building a dashboard

Feel free to open issues or submit pull requests.

* GitHub: [https://github.com/chat-gptalk/GPTalk](https://github.com/chat-gptalk/GPTalk)
* Docs: [https://gptalk.chat/docs](https://gptalk.chat/docs)
* License: Apache License 2.0
