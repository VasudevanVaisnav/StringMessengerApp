package me.discretesolutions.string.crypto;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.SecretKey;
//import static me.discretesolutions.string.crypto.
//import static me.discretesolutions.string.crypto.
import static me.discretesolutions.string.crypto.AESCipher.encrypt;
import static me.discretesolutions.string.crypto.KeyHandler.generateAESSecretKey;
import static me.discretesolutions.string.crypto.KeyHandler.generateRSAPrivateKey;
import static me.discretesolutions.string.crypto.KeyHandler.generateRSAPublicKey;
import static me.discretesolutions.string.crypto.KeyHandler.generateRsaKeyPair;
import static me.discretesolutions.string.crypto.KeyHandler.generateAESSecretKey;

public class CryptoHandler {

    public static JSONObject encrypt(String message, me.discretesolutions.string.crypto.RawRSAKey publicKey){
        me.discretesolutions.string.crypto.AesKey aesKeyObj = null;
        try {
            aesKeyObj = generateAESSecretKey();
            String cipherMessage = me.discretesolutions.string.crypto.AESCipher.encrypt(message,aesKeyObj.key);
            String cipherKey = me.discretesolutions.string.crypto.RSACipher.encrypt(aesKeyObj.keyString,generateRSAPublicKey(publicKey));
            JSONObject packet = new JSONObject();
            packet.put("key",cipherKey);
            packet.put("msg",cipherMessage);
            return packet;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(JSONObject packet, me.discretesolutions.string.crypto.RawRSAKey privateKey){
        try {
            String cipherKey = packet.getString("key");
            String cipherMessage = packet.getString("msg");
            String aesKeyString = me.discretesolutions.string.crypto.RSACipher.decrypt(cipherKey,generateRSAPrivateKey(privateKey));
            SecretKey aesKey = generateAESSecretKey(aesKeyString);
            String message = me.discretesolutions.string.crypto.AESCipher.decrypt(cipherMessage,aesKey);
            return message;
        }
        catch (Exception e){
            System.out.println(e);
            return null;
        }
    }


    public static String[] assist(String message, String mod, String exp) throws Exception {
        me.discretesolutions.string.crypto.AesKey aesKeyObj = generateAESSecretKey();
        String cipherMessage = me.discretesolutions.string.crypto.AESCipher.encrypt(message,aesKeyObj.key);
        String modStr = mod;
        String expStr = exp;
        System.out.println(mod);
        System.out.println(exp);
        me.discretesolutions.string.crypto.RawRSAKey rawRSAKey = new me.discretesolutions.string.crypto.RawRSAKey(new BigInteger(modStr),new BigInteger(expStr));
        PublicKey publicKey = generateRSAPublicKey(rawRSAKey);
        String cipherKey = me.discretesolutions.string.crypto.RSACipher.encrypt(aesKeyObj.keyString,publicKey);
        String[] ciphers = {cipherKey,cipherMessage};
        return ciphers;
    }

    public static String assist(String cipherKey, String cipherMessage, String mod, String exp) throws Exception {
        String modStr = mod;
        String expStr = exp;
        System.out.println(mod);
        System.out.println(exp);
        me.discretesolutions.string.crypto.RawRSAKey rawRSAKey = new me.discretesolutions.string.crypto.RawRSAKey(new BigInteger(modStr),new BigInteger(expStr));
        PrivateKey privateKey = generateRSAPrivateKey(rawRSAKey);
        String aesKeyString = me.discretesolutions.string.crypto.RSACipher.decrypt(cipherKey,privateKey);
        SecretKey aesKey = generateAESSecretKey(aesKeyString);
        String message = me.discretesolutions.string.crypto.AESCipher.decrypt(cipherMessage,aesKey);
        return message;
    }

    public static JSONObject assist() throws Exception {
        me.discretesolutions.string.crypto.RsaKey[] rsaKeys = generateRsaKeyPair();
        //Public Key
        me.discretesolutions.string.crypto.RawRSAKey publicRaw = rsaKeys[0].key;
        PublicKey publicKey = generateRSAPublicKey(publicRaw);
        //Private Key
        me.discretesolutions.string.crypto.RawRSAKey privateRaw = rsaKeys[1].key;
        PrivateKey privateKey = generateRSAPrivateKey(privateRaw);
        System.out.println("PUBLIC KEY MOD : "+rsaKeys[0].keyStringMod);
        System.out.println("PUBLIC KEY EXP : "+rsaKeys[0].keyStringExp);
        System.out.println("PRIVATE KEY MOD : "+rsaKeys[1].keyStringMod);
        System.out.println("PRIVATE KEY EXP : "+rsaKeys[1].keyStringExp);
        JSONObject keyStore = new JSONObject();
        keyStore.put("PublicKeyMod",rsaKeys[0].keyStringMod);
        keyStore.put("PrivateKeyMod",rsaKeys[1].keyStringMod);
        keyStore.put("PublicKeyExp",rsaKeys[0].keyStringExp);
        keyStore.put("PrivateKeyExp",rsaKeys[1].keyStringExp);
        return keyStore;
    }

    public static void test(int mode, String MOD, String EXP){
        try{
            if (mode == 0){
                //Encryption Mode
                String message = "Hello all ✌✌ 121#$%^";
                me.discretesolutions.string.crypto.AesKey aesKeyObj = generateAESSecretKey();
                String cipherMessage = me.discretesolutions.string.crypto.AESCipher.encrypt(message,aesKeyObj.key);
                String modStr = MOD;
                String expStr = EXP;
                me.discretesolutions.string.crypto.RawRSAKey rawRSAKey = new me.discretesolutions.string.crypto.RawRSAKey(new BigInteger(modStr),new BigInteger(expStr));
                PublicKey publicKey = generateRSAPublicKey(rawRSAKey);
                String cipherKey = me.discretesolutions.string.crypto.RSACipher.encrypt(aesKeyObj.keyString,publicKey);
                System.out.println("------------------------------");
                System.out.println("CIPHER TEXT : "+cipherMessage);
                System.out.println("CIPHER KEY : "+cipherKey);
                System.out.println("------------------------------");
            }
            else if(mode==1){
                //Decryption Mode
                String cipherKey = "PkD2FiDsC4jnsc/KieQdldbCZglfTwSiWBgRVpSD5E7zB1eocoPnnu96yrC9iN0cZ9gNqYykiytLDm7AlMo8Pg==";
                String cipherMessage = "DK6PQaxWjTaBWfyynTMGI5ReJYvizKJuZ/CCvbJLNyWS6H22fsp2j0tZMkwEr3gm/FqtYkSGii6NNo7Bxw==";
                String modStr = MOD;
                String expStr = EXP;
                me.discretesolutions.string.crypto.RawRSAKey rawRSAKey = new me.discretesolutions.string.crypto.RawRSAKey(new BigInteger(modStr),new BigInteger(expStr));
                PrivateKey privateKey = generateRSAPrivateKey(rawRSAKey);
                String aesKeyString = me.discretesolutions.string.crypto.RSACipher.decrypt(cipherKey,privateKey);
                SecretKey aesKey = generateAESSecretKey(aesKeyString);
                String message = me.discretesolutions.string.crypto.AESCipher.decrypt(cipherMessage,aesKey);
                System.out.println("------------------------------");
                System.out.println("PLAIN TEXT : "+message);
                System.out.println("------------------------------");
            }
            else{
                //RSA KEY GENERATION
                me.discretesolutions.string.crypto.RsaKey[] rsaKeys = generateRsaKeyPair();
                //Public Key
                me.discretesolutions.string.crypto.RawRSAKey publicRaw = rsaKeys[0].key;
                PublicKey publicKey = generateRSAPublicKey(publicRaw);
                //Private Key
                me.discretesolutions.string.crypto.RawRSAKey privateRaw = rsaKeys[1].key;
                PrivateKey privateKey = generateRSAPrivateKey(privateRaw);
                System.out.println("------------------------------");
                System.out.println("PUBLIC KEY MOD : "+rsaKeys[0].keyStringMod);
                System.out.println("PUBLIC KEY EXP : "+rsaKeys[0].keyStringExp);
                System.out.println("PRIVATE KEY MOD : "+rsaKeys[1].keyStringMod);
                System.out.println("PRIVATE KEY EXP : "+rsaKeys[1].keyStringExp);
                System.out.println("------------------------------");
            }
        }
        catch (Exception e){
            System.out.println(e);
        }

    }
    
}
/*
plaintext aes ->  cipher
x,y
 */
