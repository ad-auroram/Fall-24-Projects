import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.Random;

public class Assign6 {
    private static final int NUM_SIMULATIONS = 1000;
    private static final int SEQUENCE_LENGTH = 1000;
    private static final int MAX_PAGE_REF = 250;
    private static final int MAX_FRAMES = 100;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Random random = new Random();

        int[] fifoWins = new int[MAX_FRAMES];
        int[] lruWins = new int[MAX_FRAMES];
        int[] mruWins = new int[MAX_FRAMES];
        int fifoAnomalies = 0;

        long startTime = System.currentTimeMillis();

        for (int sim = 0; sim < NUM_SIMULATIONS; sim++) {
            int[] sequence = random.ints(SEQUENCE_LENGTH, 1, MAX_PAGE_REF + 1).toArray();

            // Shared arrays to store faults
            int[] fifoFaults = new int[MAX_FRAMES];
            int[] lruFaults = new int[MAX_FRAMES];
            int[] mruFaults = new int[MAX_FRAMES];

            // Submit tasks to executor
            List<Future<?>> futures = new ArrayList<>();
            for (int frames = 1; frames <= MAX_FRAMES; frames++) {
                futures.add(executor.submit(new TaskFIFO(sequence, frames, fifoFaults)));
                futures.add(executor.submit(new TaskLRU(sequence, frames, lruFaults)));
                futures.add(executor.submit(new TaskMRU(sequence, frames, mruFaults)));
            }

            // Wait for all tasks to complete
            for (Future<?> future : futures) {
                try {
                    future.get(); // Ensure each task completes
                } catch (ExecutionException e) {
                    System.err.println("Error in task execution: " + e.getMessage());
                }
            }

            // Detect Belady's Anomaly for FIFO
            for (int frames = 2; frames <= MAX_FRAMES; frames++) {
                if (fifoFaults[frames - 1] > fifoFaults[frames - 2]) {
                    fifoAnomalies++;
                }
            }

            // Determine minimum page faults and track wins
            for (int frames = 1; frames <= MAX_FRAMES; frames++) {
                int fifo = fifoFaults[frames - 1];
                int lru = lruFaults[frames - 1];
                int mru = mruFaults[frames - 1];

                if (fifo <= lru && fifo <= mru) fifoWins[frames - 1]++;
                if (lru <= fifo && lru <= mru) lruWins[frames - 1]++;
                if (mru <= fifo && mru <= lru) mruWins[frames - 1]++;
            }
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);

        long endTime = System.currentTimeMillis();


        System.out.println("Simulation took " + (endTime - startTime) + " ms");

        int totalWinsFIFO = Arrays.stream(fifoWins).sum();
        int totalWinsLRU = Arrays.stream(lruWins).sum();
        int totalWinsMRU = Arrays.stream(mruWins).sum();

        System.out.println("FIFO min PF: " + totalWinsFIFO);
        System.out.println("LRU min PF: " + totalWinsLRU);
        System.out.println("MRU min PF: " + totalWinsMRU);

        System.out.println("Belady's Anomaly Report for FIFO");
        System.out.println("  Anomaly detected " + fifoAnomalies + " times in " + NUM_SIMULATIONS + " simulations");
    }
}