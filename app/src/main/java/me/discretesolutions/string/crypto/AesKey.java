package me.discretesolutions.string.crypto;

import javax.crypto.SecretKey;

public class AesKey {
    public static String keyString;
    public static SecretKey key;
    public AesKey(String ks, SecretKey k){
        this.keyString=ks;
        this.key=k;
    }
}
