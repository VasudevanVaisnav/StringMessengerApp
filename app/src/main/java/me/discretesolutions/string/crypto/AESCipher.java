package me.discretesolutions.string.crypto;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;

import static me.discretesolutions.string.crypto.KeyHandler.printBase64ByteArray;


public class AESCipher {
    static String CIPHER_TRANSFORMATION_CBC = "AES/CBC/PKCS5Padding";
    static String CIPHER_TRANSFORMATION_GCM = "AES/GCM/NoPadding";
    static int TAG_LENGTH_BIT = 128;
    static int IV_LENGTH_BYTE = 12;
    private static AESCipher instance = null;
    private static ThreadLocal<Cipher> cipherWrapper = new ThreadLocal<>();

    public static String encrypt(String plainText, SecretKey secretKey) throws Exception {

        byte[] encodedText = KeyHandler.encodeToBase64ByteArray(plainText);
        byte[] encryptedText = encrypt(encodedText, secretKey);
        return printBase64ByteArray(encryptedText);
    }

    @Deprecated
    private byte[] encryptWithCBC(byte[] input, SecretKey secretKey) throws Exception {

        Cipher cipher;
        try {
            cipher = Cipher.getInstance(CIPHER_TRANSFORMATION_CBC);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedText = cipher.doFinal(input);
            byte[] iv = cipher.getIV();

            return KeyHandler.concat(iv, encryptedText);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException | IOException e) {
            System.out.println(e);
            return null;
        }
    }

    private static byte[] encrypt(byte[] input, SecretKey secretKey) throws Exception {

        Cipher cipher;
        try {
            cipher = getCipher();
            byte[] iv = new byte[IV_LENGTH_BYTE];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(iv);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
            byte[] encryptedText = cipher.doFinal(input);

            return KeyHandler.concatGCM(iv, encryptedText);

        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException
                | InvalidAlgorithmParameterException e) {
            System.out.println(e);
            return null;
        }
    }

    public static String decrypt(String cipherText, SecretKey secretKey) throws Exception {

        byte[] encryptedText = KeyHandler.parseBase64String(cipherText);
        byte[] encodedText = decrypt(encryptedText, secretKey);

        return KeyHandler.decodeToPlainString(encodedText);
    }

    @Deprecated
    private static byte[] decryptWithCBC(byte[] encryptedData, SecretKey secretKey) throws Exception {

        try {
            byte[][] splittedInput = KeyHandler.split(encryptedData, 16);
            byte[] iv = splittedInput[0];
            byte[] encryptedText = splittedInput[1];
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION_CBC);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            return cipher.doFinal(encryptedText);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            System.out.println(e);
            return null;
        }
    }

    private static byte[] decrypt(byte[] encryptedData, SecretKey secretKey) throws Exception {

        try {
            ByteBuffer byteBuffer = ByteBuffer.wrap(encryptedData);
            int ivLength = byteBuffer.get();
            byte[] iv = new byte[ivLength];
            byteBuffer.get(iv);
            byte[] encryptedText = new byte[byteBuffer.remaining()];
            byteBuffer.get(encryptedText);

            Cipher cipher = getCipher();
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);
            return cipher.doFinal(encryptedText);

        } catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException
                | BadPaddingException e) {
            System.out.println(e);
            return null;
        }
    }
    public static AESCipher getInstance() {

        if (instance == null) {
            instance = new AESCipher();
        }

        return instance;
    }

    public static Cipher getCipher() {

        Cipher cipher = cipherWrapper.get();
        if (cipher == null) {
            try {
                cipher = Cipher.getInstance(CIPHER_TRANSFORMATION_GCM);

            } catch (Exception e) {
                throw new IllegalStateException("could not get cipher instance", e);
            }
            cipherWrapper.set(cipher);

            return cipherWrapper.get();

        } else {
            return cipher;
        }
    }

}
