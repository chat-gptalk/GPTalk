package chat.gptalk.common.crypto;

public interface CryptoProvider {
    String encrypt(String data);
    String decrypt(String data);
}
