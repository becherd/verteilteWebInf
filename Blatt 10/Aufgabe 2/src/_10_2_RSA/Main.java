package _10_2_RSA;

import java.math.BigInteger;

public class Main {

	public static void main(String[] args) {
		BigInteger p = new BigInteger("5");
		BigInteger q = new BigInteger("3");
		RSA rsa = new RSA(p, q, new BigInteger("3"));
		BigInteger h = rsa.encode(new BigInteger("7"));
		System.out.println("Daten bei Eingabe von p, q und d: ");
		rsa.printData();
		System.out.println("Verschlüsselung des Wertes 3 und Entschlüsselung des Ergebnisses: ");
		System.out.println(h.doubleValue());
		System.out.println(rsa.decode(h));
		
		System.out.println("---------------------------------------------------------------");
		
		RSA encode = new RSA(new BigInteger("3"), new BigInteger("15"));
		BigInteger h2 = encode.encode(new BigInteger("7"));
		System.out.println("Daten bei Eingabe von e und n (öffentlicher Schlüssel): ");
		encode.printData();
		System.out.println("Verschlüsselung des Wertes 3 und Entschlüsselung des Ergebnisses: ");
		System.out.println(h2.doubleValue());
		System.out.println(encode.decode(h2));
	}

}
