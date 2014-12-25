
package _10_2_RSA;
import java.math.BigInteger;

public class Key {
    BigInteger exponent;
    BigInteger N;
    
    public Key(BigInteger exponent, BigInteger N){
        this.exponent=exponent;
        this.N=N;
    }
    
    @Override
    public String toString(){
        return "("+exponent+","+N+")";
    }
}
