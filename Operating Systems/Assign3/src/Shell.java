import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
    private static double totalTime;

    public Shell() {
        currentDir = Paths.get(System.getProperty("user.dir"));
        history= new ArrayList<>();
        totalTime = 0;
    }


    public static void history() {
        int count = 1;
        System.out.println("---Command History---");
        for (String argument : history) {
            System.out.println(count+ ": " +argument);
            count++;
        }
    }

    public static void changeDir(String directory) {
        if("home".equals(directory)){
            currentDir = Paths.get(System.getProperty("user.home"));
        } else if ("..".equals(directory)) {
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
        Path path = currentDir.resolve(name);
        try {
            Files.createDirectory(path);
        } catch (IOException e) {
            System.err.println("Failed to create directory: " + e.getMessage());
        }
    }

    private static void removeDir(String name) {
        Path path = currentDir.resolve(name);
        try {
            Files.delete(path);
        } catch (IOException e) {
            System.err.println("Failed to remove directory: " + e.getMessage());
        }
    }

    private static void ptime() {
        System.out.println("Total time in child processes: "+String.format("%.4f", totalTime));
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
        long startTime = System.currentTimeMillis();
        boolean isBackground = command[command.length - 1].endsWith("&");
        if (isBackground) {
            command[command.length - 1] = command[command.length - 1].replace("&", "").trim();
        }

        try {
            executeCommands(command, isBackground, startTime);
        } catch (Exception e) {
            System.err.println("Error executing command: " + e.getMessage());
        }
    }

    private static void executeCommands(String[] commands, boolean isBackground, long startTime) throws IOException, InterruptedException {
        ProcessBuilder processBuilder;
        String commandString = String.join(" ", commands);
    
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            processBuilder = new ProcessBuilder("cmd.exe", "/c", commandString);
        } else {
            processBuilder = new ProcessBuilder("bash", "-c", commandString);
        }
        processBuilder.directory(currentDir.toFile());
        Process process = processBuilder.start();
        
    
        // Capture standard output
        try (InputStream is = process.getInputStream();
             InputStreamReader isr = new InputStreamReader(is);
             BufferedReader br = new BufferedReader(isr)) {
            
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);  // Print each line of output
            }
        }
    
        // Capture standard error
        try (InputStream es = process.getErrorStream();
             InputStreamReader esr = new InputStreamReader(es);
             BufferedReader ebr = new BufferedReader(esr)) {
            
            String errorLine;
            while ((errorLine = ebr.readLine()) != null) {
                System.err.println(errorLine);  // Print error messages
            }
        }
    
        // Wait for the process to finish and capture the exit code
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            System.err.println("Command exited with error code: " + exitCode);
        }
    
        // If there are additional commands to execute
        for (int i = 1; i < commands.length; i++) {
            // Set up and execute each subsequent command
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                processBuilder = new ProcessBuilder("cmd.exe", "/c", commands[i].trim());
            } else {
                processBuilder = new ProcessBuilder("bash", "-c", commands[i].trim());
            }
    
            Process nextProcess = processBuilder.start();
    
            // Capture output for the next command
            try (InputStream is = nextProcess.getInputStream();
                 InputStreamReader isr = new InputStreamReader(is);
                 BufferedReader br = new BufferedReader(isr)) {
                
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);  // Print each line of output
                }
            }
    
            // Capture errors for the next command
            try (InputStream es = nextProcess.getErrorStream();
                 InputStreamReader esr = new InputStreamReader(es);
                 BufferedReader ebr = new BufferedReader(esr)) {
                
                String errorLine;
                while ((errorLine = ebr.readLine()) != null) {
                    System.err.println(errorLine);  // Print error messages
                }
            }
    
            // Wait for the next process to finish
            int exitCodeNext = nextProcess.waitFor();
            if (exitCodeNext != 0) {
                System.err.println("Command exited with error code: " + exitCodeNext);
            }
        }
    
        if (!isBackground) {
            long endTime = System.currentTimeMillis();
            totalTime += (endTime - startTime) / 1000.0;
        }
    }

    private static void findCommand(String[] arguments) {
        ArrayList<String> builtins = new ArrayList<>(Arrays.asList("ptime", "history", "cd", "mdir", "rdir", "list", "^"));
        if (!builtins.contains(arguments[0])) {
            passCommand(arguments);
        }else {
            String command = arguments[0];
            switch (command) {
                case "ptime" -> ptime();
                case "history" -> history();
                case "list" -> list();
                case "cd" -> {
                    if (arguments.length > 1) {
                        changeDir(arguments[1]);
                    } else {
                        changeDir("home");
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