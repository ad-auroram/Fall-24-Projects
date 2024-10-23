import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class TaskQueue {
    public Lock lock;
    public LinkedList<Integer> tasks;

    public TaskQueue() {
        tasks = new LinkedList<>();
        lock = new ReentrantLock();
    }

    //add a task to the queue
    public void addTask(int task) {
        lock.lock();
        try {
            tasks.add(task);
        } finally {
            lock.unlock();
        }
    }

    //returns next task
    public Integer getTask() {
        lock.lock();
        try {
            return tasks.poll();
        } finally {
            lock.unlock();
        }
    }

    //checks if the task list is empty
    public boolean isEmpty(){
        lock.lock();
        try{
            return tasks.isEmpty();
        }finally{
            lock.unlock();
        }
    }
}

class ResultTable {
    public HashMap<Integer, Integer> results;
    public Lock lock;

    public ResultTable() {
        results = new HashMap<>();
        lock = new ReentrantLock();
    }


    //add result to the table
    public void addResult(int index, int val){
        lock.lock();
        try{
            results.put(index, val);
        }finally{
            lock.unlock();
        }
    }

    //return map of results
    public Map<Integer, Integer> getResults() {
        lock.lock();
        try {
            return new HashMap<>(results);
        }finally{
            lock.unlock();
        }
    }
}

class PiWorker extends Thread {
    public TaskQueue taskQueue;
    public ResultTable resultTable;
    public static final AtomicInteger completedCount = new AtomicInteger(0);

    public PiWorker(TaskQueue taskQueue, ResultTable resultTable) {
        this.taskQueue = taskQueue;
        this.resultTable = resultTable;

    }
    public void run() {
        Integer task;
        while ((task = taskQueue.getTask()) != null) {
            int result = computePiDigit(task);
            resultTable.addResult(task, result);
            int count = completedCount.incrementAndGet();
            if (count % 10 == 0) {
                System.out.print(".");
                System.out.flush();
            }
        }
    }

    private int computePiDigit(int index) {
        Bpp bpp = new Bpp();
        long num = bpp.getDecimal(index);
        String stringNum = String.valueOf(num);
        if (stringNum.length() == 8) return 0;
        else{
            String[] numbers = stringNum.split("");
            return Integer.parseInt(numbers[0]);
        }
    }
}

public class Calculator {
    public static void main(String[] args) {
        int numDigits = 1000;
        TaskQueue taskQueue = new TaskQueue();
        ResultTable resultTable = new ResultTable();
        List<Integer> tasks = new ArrayList<>();

        //create tasks and randomize
        for (int i = 0; i < numDigits; i++) {
            tasks.add(i+1);
        }
        Collections.shuffle(tasks);
        for (Integer task : tasks) {
            taskQueue.addTask(task);
        }

        long duration = System.currentTimeMillis();
        //create threads
        int numCores = Runtime.getRuntime().availableProcessors();
        List<PiWorker> workers = new ArrayList<>();
        for (int i = 0; i < numCores; i++) {
            PiWorker worker = new PiWorker(taskQueue, resultTable);
            workers.add(worker);
            worker.start();
        }
        // Wait for threads
        for (PiWorker worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Map<Integer, Integer> results = resultTable.getResults();
        StringBuilder piString = new StringBuilder("3.");
        for (int i = 0; i < numDigits+1; i++) {
            Integer result = results.get(i);
            if (result == null) continue;
            piString.append(result);
        }

        System.out.println("\n" + piString);
        duration = System.currentTimeMillis() - duration;
        System.out.println("Pi computation took "+duration+ " ms on "+numCores+" cores");
    }
}
