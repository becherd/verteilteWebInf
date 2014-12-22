package _10_2_RSA;

import java.math.BigInteger;

public class RSA {
	BigInteger p, q, n, d, e, phi;
	boolean allowDecode;
	
	public RSA(BigInteger p, BigInteger q, BigInteger d){
		this.p = p;
		this.q = q;
		this.n = p.multiply(q);
		this.d = d;
		
		//calcuate e
		phi = new BigInteger("" + p.subtract(new BigInteger("1")).multiply(q.subtract(new BigInteger("1"))));
		e = new BigInteger("1");
		while(!e.multiply(d).mod(phi).equals(new BigInteger("1"))){
			e = e.add(new BigInteger("1"));
		}
		
		this.allowDecode = true;
	}
	
	public RSA(BigInteger e, BigInteger n){
		this.e = e;
		this.n = n;
		this.allowDecode = false;
		this.q = new BigInteger("0");
		this.p = new BigInteger("0");
		this.d = new BigInteger("0");
		this.phi = new BigInteger("0");
	}
	
	public static BigInteger suggestD(BigInteger p, BigInteger q){
		// calculate d
		BigInteger d = new BigInteger("" + p.max(q));
		BigInteger phi = new BigInteger("" + p.subtract(new BigInteger("1")).multiply(q.subtract(new BigInteger("1"))));
		System.out.println(ggt(d, phi));
		while(!ggt(d, phi).equals(new BigInteger("1"))){
			d = d.add(new BigInteger("1"));
		}
		return d;
	}
	
	public void printData(){
		System.out.println("p: " + p.floatValue());
		System.out.println("q: " + q.floatValue());
		System.out.println("n: " + n.floatValue());
		System.out.println("(p-1)*(q-1): " + phi.floatValue());
		System.out.println("d: " + d.floatValue());
		System.out.println("e: " + e.floatValue());
		
	}
	
	public BigInteger encode(BigInteger m){
		return m.modPow(e, n) ;
	}
	public BigInteger decode(BigInteger c){
		if(allowDecode){
			return c.modPow(d, n);
		}else
			return null;
	}
	
	private static BigInteger ggt(BigInteger a, BigInteger b){
		return a.gcd(b);
	}
}
