package me.discretesolutions.string.crypto;

import java.io.Serializable;
import java.math.BigInteger;

public class RawRSAKey implements Serializable {
    private static final long serialVersionUID = 1L;

    public BigInteger modulus;
    public BigInteger exponent;

    public RawRSAKey(BigInteger modulus, BigInteger exponent) {
        this.modulus = modulus;
        this.exponent = exponent;
    }

    public BigInteger getExponent() {
        return this.exponent;
    }

    public BigInteger getModulus() {
        return this.modulus;
    }
}
