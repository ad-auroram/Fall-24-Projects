public class Factorial {

    public int calculateFactorial(int val){
        if (val==0 || val==1) return 1;
        return val*calculateFactorial(val-1);
    }
}
