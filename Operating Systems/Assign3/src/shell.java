import java.util.Objects;
import java.util.Scanner;
public class shell {

    public static void mkdir(String directory, String arg){
        System.out.println("Current directory: " +directory);
        System.out.println(arg);
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.printf("[%s]: ", System.getProperty("user.dir"));
            String currentDir = System.getProperty("user.dir");
            String arguments = input.nextLine();
            if (Objects.equals(arguments, "exit")) {
                running = false;
            }else{
                mkdir(currentDir, arguments);
            }
        }
    }
}