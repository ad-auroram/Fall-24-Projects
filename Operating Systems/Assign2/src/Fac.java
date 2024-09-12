import java.math.BigInteger;

public class Fac {
    /**
     * Calculates the factorial of a number
     * @param val - the number to calculate with
     * @return the factorial of the value
     */
    public static BigInteger calculateFactorial(int val) {
        if (val == 0 || val == 1) return BigInteger.ONE;
        BigInteger fact = new BigInteger("1");
        for (int i=1; i<=val; i++){
            fact=fact.multiply(BigInteger.valueOf(i));
        }
        return fact;
    }
}
