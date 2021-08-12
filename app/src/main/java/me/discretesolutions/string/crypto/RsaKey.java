package me.discretesolutions.string.crypto;

public class RsaKey {
    public RawRSAKey key;
    public String keyStringMod;
    public String keyStringExp;
    public RsaKey(RawRSAKey k,String ksm, String kse){
        this.key=k;
        this.keyStringMod=ksm;
        this.keyStringExp=kse;
    }
}
