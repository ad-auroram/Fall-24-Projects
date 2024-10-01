import java.util.Scanner;

public class shell {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("[Current directory]: ");
        String arguments = input.nextLine();
        System.out.println(arguments);
    }
}