public class Assn2 {

    public static void main(String[] args) {
        if (args.length > 0 && args.length>=1) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-fib")){
                    try {
                        int fibVal = Integer.parseInt(args[i + 1]);
                        if (fibVal >= 0 && fibVal <= 40) {
                            System.out.println("Fibonacci of " + fibVal + " is " + Fib.calculateFib(fibVal) + ".");
                        }
                        else{
                            System.out.println("Fibonacci valid range is [0, 40]");
                        }
                        }catch(Exception e){
                            System.out.println("Unknown command line argument: "+ args[i+1]);
                            System.out.println("Error: "+ e.getMessage());
                            help.printHelp();
                        }
                }else if (args[i].equals("-fac")){
                    try{
                        int factorial = Integer.parseInt(args[i+1]);
                        if (factorial>=0){
                            System.out.println("Factorial of " + factorial + " is " + Fac.calculateFactorial(factorial) + ".");
                        }
                        else{
                            System.out.println("Valid range for factorial is [0, 2147483647]");
                        }
                    }catch(Exception e){
                        System.out.println("Unknown command line argument: "+ args[i+1]);
                        System.out.println("Error: "+ e.getMessage());
                        help.printHelp();
                    }
                }else if ((args[i].equals("-e"))){
                    try {
                        int taylorVal = Integer.parseInt(args[i + 1]);
                        System.out.println("Value of e using " + taylorVal + " iterations is " + E.estimateE(taylorVal) + ".");
                    } catch (Exception e) {
                        System.out.println("Unknown command line argument: "+ args[i+1]);
                        System.out.println("Error: "+ e.getMessage());
                        help.printHelp();
                    }
                }
            }
        } else {
            help.printHelp();
        }
    }
}
