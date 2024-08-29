public class main {
    public static void main(String[] args) {
        //print arguments for testing
        if (args.length > 0 && args.length>=1) {
            System.out.println("Command-line arguments:");
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-fib")){
                    try{
                        int fibVal = Integer.parseInt(args[i+1]);
                        Fib fib=new Fib();
                        System.out.println("Fibonacci of "+fibVal+" is " +fib.calculateFib(fibVal)+".");
                    }catch(Exception e){
                        System.out.println("Argument for fibonacci should be and integer.");
                    }
                }else if (args[i].equals("-fac")){
                    try{
                        int facVal = Integer.parseInt(args[i+1]);
                        Factorial factorial=new Factorial();
                        System.out.println("Factorial of "+facVal+" is " +factorial.calculateFactorial(facVal)+".");
                    }catch(Exception e){
                        System.out.println("Argument for factorial should be an integer");
                    }
                }else if ((args[i].equals("-e"))){
                    try {
                        int taylorVal = Integer.parseInt(args[i + 1]);
                        Taylor taylor = new Taylor();
                        System.out.println("Value of e using " + taylorVal + " iterations is " + taylor.estimateE(taylorVal) + ".");
                    } catch (Exception e) {
                        System.out.println("Argument for e should be an integer");
                    }
                }
            }
        } else {
            System.out.println("--- Assign 1 Help ---\n" +
                    "  -fib [n] : Compute the Fibonacci of [n]; valid range [0, 40]\n" +
                    "  -fac [n] : Compute the factorial of [n]; valid range, [0, 2147483647]\n" +
                    "  -e [n] : Compute the value of 'e' using [n] iterations; valid range [1, 2147483647]");
        }
    }
}
