package me.discretesolutions.string.crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static me.discretesolutions.string.crypto.KeyHandler.decodeToPlainString;
import static me.discretesolutions.string.crypto.KeyHandler.encodeToBase64ByteArray;
import static me.discretesolutions.string.crypto.KeyHandler.parseBase64String;
import static me.discretesolutions.string.crypto.KeyHandler.printBase64ByteArray;

public class RSACipher {

    static String RSA_ECB_PKCS1PADDING = "RSA/ECB/PKCS1Padding";

    public static String encrypt(String plainText, PublicKey publicKey) throws Exception {

        try {
            byte[] encodedText = encodeToBase64ByteArray(plainText);
            byte[] encryptedText = encrypt(encodedText, publicKey);
            return printBase64ByteArray(encryptedText);

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    private static byte[] encrypt(byte[] input, PublicKey publicKey) throws Exception {

        try {
            Cipher cipher = Cipher.getInstance(RSA_ECB_PKCS1PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            return cipher.doFinal(input);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException e) {
            System.out.println(e);
            return null;
        }
    }

    public static String decrypt(String cipherText, PrivateKey privateKey) throws Exception {

        try {
            byte[] encryptedText = parseBase64String(cipherText);
            byte[] encodedText = decrypt(encryptedText, privateKey);
            return decodeToPlainString(encodedText);

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    private static byte[] decrypt(byte[] input, PrivateKey privateKey) throws Exception {

        try {
            Cipher cipher = Cipher.getInstance(RSA_ECB_PKCS1PADDING);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(input);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException e) {
            System.out.println(e);
            return null;
        }
    }
}
