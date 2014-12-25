package _10_2_RSA;

import java.math.BigInteger;
import java.util.Random;

public class RSA {

    private BigInteger p, q, N, d, e, phi;

    /*
     * We generate private and public key here
     */
    public RSA() {
        //generate random numbers for p and q where the certainty that p and q are prime is almost 1
        //(in fact, it's 1-2^(-100000))
        p = new BigInteger(300, 100000, new Random(System.currentTimeMillis()));
        q =  new BigInteger(300, 100000, new Random(System.currentTimeMillis()));

        N = p.multiply(q);
        phi = (p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)));

        //generate e. For efficiency we use a relatively small e
        e = new BigInteger(20, new Random(System.currentTimeMillis()));

        //make sure the gcd of e and phi is 1
        while (!e.gcd(phi).equals(BigInteger.ONE)) {
            e = e.add(BigInteger.ONE);
        }

        //compute d such that e*d = 1 (mod phi)
        d = e.modInverse(phi);
    }

    public Key getPrivateKey() {
        return new Key(d, N);
    }

    public Key getPublicKey() {
        return new Key(e, N);
    }

    public void printData() {
        System.out.println("----------------------------------------");
        System.out.println("The following keys have been computed:");
        System.out.println("Public Key (e,N): "+getPublicKey());
        System.out.println("Private Key (d,N): "+getPrivateKey());
        System.out.println("----------------------------------------\n");
    }

    //encrypt message m with public key
    public static BigInteger encrypt(BigInteger m, Key publicKey) {
        return m.modPow(publicKey.exponent, publicKey.N);
    }

   //decrypt message m with private key
    public static BigInteger decrypt(BigInteger c, Key privateKey) {
        return c.modPow(privateKey.exponent, privateKey.N);
    }
}
