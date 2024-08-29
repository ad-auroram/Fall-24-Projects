import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Taylor {
    public BigDecimal estimateE(int iterations) {
        BigDecimal estimate = new BigDecimal(0);
        BigDecimal one = new BigDecimal(1);
        Factorial fact = new Factorial();
        MathContext rounding = new MathContext(10, RoundingMode.HALF_UP);
        for (int i = 0; i < iterations; i++) {
            int factorialN = fact.calculateFactorial(i);
            BigDecimal n = new BigDecimal(factorialN);
            estimate = estimate.add(one.divide(n, rounding));
        }
        return estimate;
    }

}