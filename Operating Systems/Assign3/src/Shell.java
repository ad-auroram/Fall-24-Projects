
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Shell {
    private static Path currentDir;
    private static ArrayList<String> history;

    public Shell() {
        currentDir = Paths.get(System.getProperty("user.dir"));
        history= new ArrayList<>();
    }


    public static void history() {
        for (String argument : history) {
            System.out.println(argument);
        }
    }

    public static void changeDir(String directory) {
        Path newPath;
        if ("..".equals(directory)) {
            newPath = currentDir.getParent();
            if (newPath != null) {
                currentDir = newPath.toAbsolutePath();
            } else {
                System.out.println("Error: Directory not found.");
            }
        }
        newPath = currentDir.resolve(directory);
        if (Files.exists(newPath) && Files.isDirectory(newPath)) {
            currentDir = newPath.toAbsolutePath();
        } else {
            System.out.println("Error: Directory not found.");
        }
    }

    private static void makeDir(String[] arguments) {
        System.out.println("making directory");
    }

    private static void removeDir(String[] arguments) {
        System.out.println("removing directory");
    }

    private static void ptime(String[] arguments) {
        System.out.println("time");
    }

    private static void list(String[] arguments){
        System.out.println("list");
    }

    private static void findCommand(String[] arguments) {
        String command = arguments[0];
        switch (command) {
            case "ptime" -> ptime(arguments);
            case "history" -> history();
            case "list" -> list(arguments);
            case "cd" -> changeDir(arguments[1]);
            case "mdir" -> makeDir(arguments);
            case "rdir" -> removeDir(arguments);
            //case "|" -> pipe(arguments);
            //case "^" -> number(arguments);
            default -> System.out.println("Command not found.");
        }
    }

    public static String[] splitCommand(String command) {
        java.util.List<String> matchList = new java.util.ArrayList<>();

        Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
        Matcher regexMatcher = regex.matcher(command);
        while (regexMatcher.find()) {
            if (regexMatcher.group(1) != null) {
                // Add double-quoted string without the quotes
                matchList.add(regexMatcher.group(1));
            } else if (regexMatcher.group(2) != null) {
                // Add single-quoted string without the quotes
                matchList.add(regexMatcher.group(2));
            } else {
                // Add unquoted word
                matchList.add(regexMatcher.group());
            }
        }

        return matchList.toArray(new String[matchList.size()]);
    }


    public static void main(String[] args) {
        Shell shell = new Shell();
        Scanner input = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.printf("[%s]: ", System.getProperty("user.dir"));
            String argument = input.nextLine();
            if (Objects.equals(argument, "exit")) {
                running = false;
            } else {
                history.add(argument);
                String[] arguments = splitCommand(argument);
                findCommand(arguments);
            }
        }
    }

}