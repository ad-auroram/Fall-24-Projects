import java.util.Scanner;
public class shell {

    public static void mkdir(String directory, String arg){
        System.out.println("Currecnt directory: " +directory);
        System.out.println(arg);
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.printf("[%s]: ", System.getProperty("user.dir"));
        String currentDir = System.getProperty("user.dir");
        String arguments = input.nextLine();
        mkdir(currentDir, arguments);
    }
}