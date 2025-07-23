package chat.gptalk.common.crypto.impl;

import chat.gptalk.common.config.CryptoProperties;
import chat.gptalk.common.crypto.CryptoProvider;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AesCryptoProvider implements CryptoProvider {

    private static final String ALGORITHM = "AES";
    private final byte[] secretKey;

    public AesCryptoProvider(CryptoProperties cryptoProperties) {
        this.secretKey = Base64.getDecoder().decode(cryptoProperties.aes().secret());
    }

    @Override
    public String encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secretKey, ALGORITHM));
            return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("AES encryption error", e);
        }
    }

    @Override
    public String decrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secretKey, ALGORITHM));
            return new String(cipher.doFinal(Base64.getDecoder().decode(data)));
        } catch (Exception e) {
            throw new RuntimeException("AES decryption error", e);
        }
    }

    private static SecretKey generateAESKey(int keySize) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(keySize);
        return keyGen.generateKey();
    }

    public static void main(String[] args) throws Exception {
        SecretKey secretKey = generateAESKey(256);
        System.out.println("AES Key (Base64): " +
            java.util.Base64.getEncoder().encodeToString(secretKey.getEncoded()));
    }
}
