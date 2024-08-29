public class Fib {

    public int calculateFib(int val){
        if (val==0 || val==1) return val;
        return calculateFib(val-1)+calculateFib(val-2);
    }



}
