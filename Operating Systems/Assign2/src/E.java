import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class E {
    /**
     * Estimates the value of e given a number of iterations for the Taylor series
     * @param iterations
     * @return an estimate of e
     */
    public static BigDecimal estimateE(int iterations) {
        BigDecimal estimate = new BigDecimal(0);
        BigDecimal one = new BigDecimal(1);
        MathContext rounding = new MathContext(10, RoundingMode.HALF_UP);
        for (int i = 0; i < iterations; i++) {
            BigInteger factorialN = Fac.calculateFactorial(i);
            BigDecimal n = new BigDecimal(factorialN);
            estimate = estimate.add(one.divide(n, rounding));
        }
        return estimate;
    }
}
