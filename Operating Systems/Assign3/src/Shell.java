import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
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
        int count = 1;
        for (String argument : history) {
            System.out.println(count+ ": " +argument);
            count++;
        }
    }

    public static void changeDir(String directory) {
        if ("..".equals(directory)) {
            Path parentDir = currentDir.getParent();
            if (parentDir != null) {
                currentDir = parentDir.toAbsolutePath();
            } else {
                System.out.println("Error: Already at the root directory.");
            }
        } else {
            Path newPath = currentDir.resolve(directory);
            if (Files.exists(newPath) && Files.isDirectory(newPath)) {
                currentDir = newPath.toAbsolutePath();
            } else {
                System.out.println("Error: Directory not found.");
            }
        }
    }

    private static void makeDir(String name) {
        System.out.println("making directory");
        Path path = Paths.get(name);
        try {
            Files.createDirectory(path);
        } catch (IOException e) {
            System.err.println("Failed to create directory: " + e.getMessage());
        }
    }

    private static void removeDir(String name) {
        System.out.println("removing directory");
        Path path = Paths.get(name);
        try {
            Files.delete(path);
        } catch (IOException e) {
            System.err.println("Failed to remove directory: " + e.getMessage());
        }
    }

    private static void ptime(String[] arguments) {
        System.out.println("time");
    }

    private static void list(){
        File current = new File(String.valueOf(currentDir));
        File[] files = current.listFiles();
        if (files != null) {
            for (File file : files) {
                String permissions = (file.isDirectory() ? "d" : "-") +
                        (file.canRead() ? "r" : "-") +
                        (file.canWrite() ? "w" : "-") +
                        (file.canExecute() ? "x" : "-");
                long size = file.length();
                String formattedDate = formatDate(file.lastModified());
                String fileName = file.getName();
                System.out.printf("%s %10d %s %s\n", permissions, size, formattedDate, fileName);
            }
        } else {
            System.out.println("Error: Unable to list files.");
        }
    }
    private static String formatDate(long lastModified) {
        Date date = new Date(lastModified);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        return sdf.format(date);
    }

    private static void fromHist(String number){
        try {
            int index = Integer.parseInt(number) - 1;
            if (index < 0 || index >= history.size()) {
                System.out.println("Invalid index.");
            }
            String pastCommand = history.get(index);
            history.add(pastCommand);
            String[] arguments = splitCommand(pastCommand);
            findCommand(arguments);
        }catch(NumberFormatException e) {
            System.out.println("Invalid number format.");
        }
    }

    private static void passCommand(String[] command){

    }


    private static void findCommand(String[] arguments) {
        ArrayList<String> builtins = new ArrayList<>(Arrays.asList("ptime", "history", "cd", "mdir", "rdir", "list", "^"));
        if (!builtins.contains(arguments[0])) {
            passCommand(arguments);
        }else {
            String command = arguments[0];
            switch (command) {
                case "ptime" -> ptime(arguments);
                case "history" -> history();
                case "list" -> list();
                case "cd" -> {
                    if (arguments.length > 1) {
                        changeDir(arguments[1]);
                    } else {
                        System.out.println("Error: No directory specified.");
                    }
                }
                case "mdir" -> {
                    if (arguments.length > 1) {
                        makeDir(arguments[1]);
                    } else {
                        System.out.println("Error: No directory name specified.");
                    }
                }
                case "rdir" -> {
                    if (arguments.length > 1) {
                        removeDir(arguments[1]);
                    } else {
                        System.out.println("Error: No directory name specified.");
                    }
                }
                //case "|" -> pipe(arguments);
                case "^" -> fromHist(arguments[1]);
                default -> System.out.println("Command not found.");
            }
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
            System.out.printf("[%s]: ", currentDir.toString());
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