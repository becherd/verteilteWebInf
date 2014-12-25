package _10_2_RSA;

import java.math.BigInteger;

public class Main {

	public static void main(String[] args) throws Exception {
		RSA r = new RSA();
                r.printData();
                Key publicKey=r.getPublicKey();
                Key privateKey=r.getPrivateKey();

                //the message
                BigInteger m= new BigInteger("1234567890");
                System.out.println("Message m: "+m);
                
                //cyphertext
                BigInteger c = RSA.encrypt(m, publicKey);
                System.out.println("c=E(m): "+c);

                System.out.println("m=D(E(m)): " + RSA.decrypt(c, privateKey));
	}
}
