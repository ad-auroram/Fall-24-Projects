import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class main {

    public static int calculateFib(int val) {
        if (val == 0 || val == 1) return val;
        return calculateFib(val - 1) + calculateFib(val - 2);
    }

    public static BigInteger calculateFactorial(int val) {
        if (val == 0 || val == 1) return BigInteger.ONE;
        BigInteger fact = new BigInteger("1");
        for (int i=1; i<=val; i++){
            fact=fact.multiply(BigInteger.valueOf(i));
        }
        return fact;
        //return val * calculateFactorial(val - 1);
    }

    public static BigDecimal estimateE(int iterations) {
        BigDecimal estimate = new BigDecimal(0);
        BigDecimal one = new BigDecimal(1);
        MathContext rounding = new MathContext(10, RoundingMode.HALF_UP);
        for (int i = 0; i < iterations; i++) {
            BigInteger factorialN = calculateFactorial(i);
            BigDecimal n = new BigDecimal(factorialN);
            estimate = estimate.add(one.divide(n, rounding));
        }
        return estimate;
    }

    public static void printHelp() {
        System.out.println("--- Assign 1 Help ---\n" +
                "  -fib [n] : Compute the Fibonacci of [n]; valid range [0, 40]\n" +
                "  -fac [n] : Compute the factorial of [n]; valid range, [0, 2147483647]\n" +
                "  -e [n] : Compute the value of 'e' using [n] iterations; valid range [1, 2147483647]");
    }

    public static void main(String[] args) {
        if (args.length > 0 && args.length>=1) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-fib")){
                    try {
                        int fibVal = Integer.parseInt(args[i + 1]);
                        if (fibVal >= 0 && fibVal <= 40) {
                            System.out.println("Fibonacci of " + fibVal + " is " + calculateFib(fibVal) + ".");
                        }
                        else{
                            System.out.println("Fibonacci valid range is [0, 40]");
                        }
                        }catch(Exception e){
                            System.out.println("Unknown command line argument: "+ args[i+1]);
                            System.out.println("Error: "+ e.getMessage());
                            printHelp();
                        }
                }else if (args[i].equals("-fac")){
                    try{
                        int factorial = Integer.parseInt(args[i+1]);
                        if (factorial>=0){
                            System.out.println("Factorial of " + factorial + " is " + calculateFactorial(factorial) + ".");
                        }
                        else{
                            System.out.println("Valid range for factorial is [0, 2147483647]");
                        }
                    }catch(Exception e){
                        System.out.println("Unknown command line argument: "+ args[i+1]);
                        System.out.println("Error: "+ e.getMessage());
                        printHelp();
                    }
                }else if ((args[i].equals("-e"))){
                    try {
                        int taylorVal = Integer.parseInt(args[i + 1]);
                        System.out.println("Value of e using " + taylorVal + " iterations is " + estimateE(taylorVal) + ".");
                    } catch (Exception e) {
                        System.out.println("Unknown command line argument: "+ args[i+1]);
                        System.out.println("Error: "+ e.getMessage());
                        printHelp();
                    }
                }
            }
        } else {
            printHelp();
        }
    }
}
