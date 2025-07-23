import {ChatCompletionRequest} from "./types";
import {ChatCompletionChunk} from "./types";

function sleep(ms: number) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

export class OpenAIStreamClient {
  private readonly baseUrl: string;

  constructor(baseUrl = '/openapi/v1') {
    this.baseUrl = baseUrl;
  }

  async* chatCompletionStream(
      apiKeyId: string,
      body: ChatCompletionRequest,
  ): AsyncGenerator<ChatCompletionChunk, void, unknown> {
    const controller = new AbortController();

    const response = await fetch(`${this.baseUrl}/chat/completions`, {
      method: 'POST',
      headers: {
        'X-Api-Key-Id': apiKeyId,
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({...body, stream: true}),
      signal: controller.signal,
    });

    if (!response.ok || !response.body) {
      throw new Error(`HTTP ${response.status} - ${response.statusText}`);
    }

    const reader = response.body.getReader();
    const decoder = new TextDecoder('utf-8');
    let buffer = '';

    while (true) {
      const {value, done} = await reader.read();
      if (done) break;

      buffer += decoder.decode(value, {stream: true});

      const lines = buffer.split('\n');

      buffer = lines.pop() ?? '';

      for (const line of lines) {
        const trimmed = line.trim();

        if (!trimmed || !trimmed.startsWith('data:')) continue;

        const data = trimmed.replace(/^data:\s*/, '');

        if (data === '[DONE]') {
          return;
        }

        try {
          const parsed: ChatCompletionChunk = JSON.parse(data);
          for (let i = 0; i < 1000; i++) {
            console.log(i);
          }
          yield parsed;
        } catch (err) {
          console.warn('Failed to parse chunk:', data, err);
        }
      }
    }
  }
}