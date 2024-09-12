public class Fib {
    /**
     * Calculates a fibonacci number.
     * @param val - the value in the sequence to be calculated
     * @return the corresponding number of the Fibonacci sequence
     */
    public static int calculateFib(int val) {
        if (val == 0 || val == 1) return val;
        return calculateFib(val - 1) + calculateFib(val - 2);
    }
}
