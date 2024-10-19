import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class TaskQueue {
    private Lock lock;
    private LinkedList<Integer> tasks;

    public TaskQueue() {
        tasks = new LinkedList<>();
        lock = new ReentrantLock();
    }
    //add method to add task
    //add method to return next task (or null if empty)
    //add method to check if empty
}

class ResultTable {
    private HashMap<Integer, Integer> results;
    private Lock lock;

    public ResultTable() {
        results = new HashMap<>();
        lock = new ReentrantLock();
    }


    //method to add result to the table
    //method to return map of results
    public Map<Integer, Integer> getResults() {
        return new HashMap<>(results);
    }
}

class PiWorker extends Thread {
    private TaskQueue taskQueue;
    private ResultTable resultTable;
    private static int completedCount = 0;
    private static final Lock countLock = new ReentrantLock();

    public PiWorker(TaskQueue taskQueue, ResultTable resultTable) {
        this.taskQueue = taskQueue;
        this.resultTable = resultTable;
    }

    //make a loop to call bpp as long as there is a task in the queue
    //print a "." for every count%10=0 (use lock when checking for count)
    //method to call bpp with placement
}
public class Calculator {
    public static void main(String[] args) {
        int numDigits = 1000;
        TaskQueue taskQueue = new TaskQueue();
        ResultTable resultTable = new ResultTable();
        long duration = System.currentTimeMillis();
        //create tasks and randomize

        //create threads
        int numCores = Runtime.getRuntime().availableProcessors();
        List<PiWorker> workers = new ArrayList<>();
        for (int i = 0; i < numCores; i++) {
            PiWorker worker = new PiWorker(taskQueue, resultTable);
            workers.add(worker);
            worker.start();
        }
        // Wait for all threads to finish
        for (PiWorker worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Map<Integer, Integer> results = resultTable.getResults();
        StringBuilder piString = new StringBuilder("3.");
        for (int i = 0; i < numDigits; i++) {
            piString.append(results.get(i));
        }

        System.out.println("\n" + piString);
        duration = System.currentTimeMillis() - duration;
        System.out.println("Pi computation took "+duration+ " ms");
    }
}
