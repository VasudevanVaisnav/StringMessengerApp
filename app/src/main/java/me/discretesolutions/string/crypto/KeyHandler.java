package me.discretesolutions.string.crypto;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.HashMap;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class KeyHandler {
    SecretKey AesKey;
    RawRSAKey publicKey;
    RawRSAKey privateKey;

    public static RawRSAKey readRSAKeyFromString(String mod, String exp) throws IOException, ClassNotFoundException {
        BigInteger modI = new BigInteger(mod);
        BigInteger expI = new BigInteger(exp);
        return new RawRSAKey(modI,expI);
    }

    public static HashMap<String,String> writeRSAKeyToString(RawRSAKey rsaKey) throws IOException {
        BigInteger modI = rsaKey.modulus;
        BigInteger expI = rsaKey.exponent;
        String mod = String.valueOf(modI);
        String exp = String.valueOf(expI);
        HashMap <String,String> key = new HashMap<>();
        key.put("mod",mod);
        key.put("exp",exp);
        return key;
    }

    private static String writeAESKeyToString(SecretKey secretKey) {

        if (secretKey != null) {
            String encodedSecretKey = printBase64ByteArray(secretKey.getEncoded());
            return encodedSecretKey;
        }
        else{
            return "error";
        }
    }

    //utility functions from here

    public static byte[] encodeToBase64ByteArray(String plainString) {
        return Base64.encodeBase64(plainString.getBytes());
    }

    public static String decodeToPlainString(byte[] base64ByteArray) {
        return new String(Base64.decodeBase64(base64ByteArray));
    }

    public static byte[] parseBase64String(String base64String) {
        return DatatypeConverter.parseBase64Binary(base64String);
    }

    public static String printBase64ByteArray(byte[] base64ByteArray) {
        return DatatypeConverter.printBase64Binary(base64ByteArray);//what does this do?
    }

    public static byte[] concat(byte[] iv, byte[] encryptedText) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(iv);
        byteArrayOutputStream.write(encryptedText);

        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] concatGCM(byte[] iv, byte[] encryptedText) {

        ByteBuffer byteBuffer = ByteBuffer.allocate(1 + iv.length + encryptedText.length);
        byteBuffer.put((byte) iv.length);
        byteBuffer.put(iv);
        byteBuffer.put(encryptedText);

        return byteBuffer.array();
    }

    public static byte[][] split(byte[] input, int seperatorIndex) {

        byte[] firstInput = Arrays.copyOf(input, seperatorIndex);
        byte[] secondInput = Arrays.copyOfRange(input, seperatorIndex, input.length);
        byte[][] splittedInput = new byte[2][];
        splittedInput[0] = firstInput;
        splittedInput[1] = secondInput;

        return splittedInput;
    }

    public static me.discretesolutions.string.crypto.AesKey generateAESSecretKey() throws Exception {

        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecretKey aesKey = keyGenerator.generateKey();
            String aesKeyString = writeAESKeyToString(aesKey);
            return new me.discretesolutions.string.crypto.AesKey(aesKeyString,aesKey);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return null;
    }

    public static SecretKey generateAESSecretKey(String rawAESKey) throws Exception {

        byte[] encodedRawAESKey = parseBase64String(rawAESKey);
        SecretKey originalKey = new SecretKeySpec(encodedRawAESKey, 0, encodedRawAESKey.length, "AES");
//            SecretKeySpec secretKeySpec = new SecretKeySpec(encodedRawAESKey, "AES");
//            SecretKeyFactory secretKeyGenerator = SecretKeyFactory.getInstance("AES");
        return originalKey;

    }

    public static me.discretesolutions.string.crypto.RsaKey[] generateRsaKeyPair() throws Exception {

        try {
            KeyPair keyPair = generateKeyPair();
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RawRSAKey rsaPublicKey = generatePublicKey(keyPair, keyFactory);
            RawRSAKey rsaPrivateKey = generatePrivateKey(keyPair, keyFactory);
//            CipherUtil.writeRSAKeyToFile(rsaPublicKey, RSA_PUBLIC);
//            CipherUtil.writeRSAKeyToFile(rsaPrivateKey, RSA_PRIVATE);
            HashMap<String,String> pubs = writeRSAKeyToString(rsaPublicKey);
            me.discretesolutions.string.crypto.RsaKey publicKey = new me.discretesolutions.string.crypto.RsaKey(rsaPublicKey,pubs.get("mod").toString(),pubs.get("exp").toString());
            HashMap<String,String> pivs = writeRSAKeyToString(rsaPrivateKey);
            me.discretesolutions.string.crypto.RsaKey privateKey = new me.discretesolutions.string.crypto.RsaKey(rsaPrivateKey,pivs.get("mod").toString(),pivs.get("exp").toString());
            me.discretesolutions.string.crypto.RsaKey[] returnValue = {publicKey,privateKey};
            return returnValue;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static PublicKey generateRSAPublicKey(RawRSAKey rawRSAKey) throws Exception {

        try {
            BigInteger modulus = rawRSAKey.getModulus();
            BigInteger publicExponent = rawRSAKey.getExponent();
            RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus, publicExponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            return keyFactory.generatePublic(rsaPublicKeySpec);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.out.println(e);
            return null;
        }
    }

    public static PrivateKey generateRSAPrivateKey(RawRSAKey rawRSAKey) throws Exception {

        try {
            BigInteger modulus = rawRSAKey.getModulus();
            BigInteger privateExponent = rawRSAKey.getExponent();
            RSAPrivateKeySpec rsaPrivateKeySpec = new RSAPrivateKeySpec(modulus, privateExponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            return keyFactory.generatePrivate(rsaPrivateKeySpec);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.out.println(e);
            return null;
        }
    }

    private static KeyPair generateKeyPair() throws NoSuchAlgorithmException {

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(512);
        return keyPairGenerator.generateKeyPair();
    }


    private static RawRSAKey generatePublicKey(KeyPair keyPair, KeyFactory keyFactory) throws InvalidKeySpecException {

        PublicKey publicKey = keyPair.getPublic();
        RSAPublicKeySpec rsaPublicKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
        BigInteger modulus = rsaPublicKeySpec.getModulus();
        BigInteger publicExponent = rsaPublicKeySpec.getPublicExponent();

        return new RawRSAKey(modulus, publicExponent);
    }

    private static RawRSAKey generatePrivateKey(KeyPair keyPair, KeyFactory keyFactory) throws InvalidKeySpecException {

        PrivateKey privateKey = keyPair.getPrivate();
        RSAPrivateKeySpec rsaPrivateKeySpec = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);
        BigInteger modulus = rsaPrivateKeySpec.getModulus();
        BigInteger privateExponent = rsaPrivateKeySpec.getPrivateExponent();
        return new RawRSAKey(modulus, privateExponent);
    }

    //review

    public static SecretKey generateAESSecretKeyFromMessageDigest(String rawSecretKey) throws Exception {

        byte[] key;
        MessageDigest sha = null;
        try {
            key = parseBase64String(rawSecretKey);
            sha = MessageDigest.getInstance("SHA-256");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            return new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
            return null;
        }
    }//why sha-256?

}