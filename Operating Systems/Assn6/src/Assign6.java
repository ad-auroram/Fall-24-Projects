import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

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
        int maxDelta = 0;
        List<String> anomalyDetails = new ArrayList<>();

        long startTime = System.currentTimeMillis();


        List<AtomicInteger[]> fifoFaultsList = new ArrayList<>();
        List<AtomicInteger[]> lruFaultsList = new ArrayList<>();
        List<AtomicInteger[]> mruFaultsList = new ArrayList<>();

        for (int sim = 0; sim < NUM_SIMULATIONS; sim++) {
            int[] sequence = random.ints(SEQUENCE_LENGTH, 1, MAX_PAGE_REF + 1).toArray();


            AtomicInteger[] fifoFaults = new AtomicInteger[MAX_FRAMES];
            AtomicInteger[] lruFaults = new AtomicInteger[MAX_FRAMES];
            AtomicInteger[] mruFaults = new AtomicInteger[MAX_FRAMES];

            for (int i = 0; i < MAX_FRAMES; i++) {
                fifoFaults[i] = new AtomicInteger();
                lruFaults[i] = new AtomicInteger();
                mruFaults[i] = new AtomicInteger();
            }


            CountDownLatch latch = new CountDownLatch(3 * MAX_FRAMES);

            for (int frames = 1; frames <= MAX_FRAMES; frames++) {
                executor.submit(new TaskFIFO(sequence, frames, fifoFaults, latch));
                executor.submit(new TaskLRU(sequence, frames, lruFaults, latch));
                executor.submit(new TaskMRU(sequence, frames, mruFaults, latch));
            }

            latch.await();

            fifoFaultsList.add(fifoFaults);
            lruFaultsList.add(lruFaults);
            mruFaultsList.add(mruFaults);

            for (int frames = 2; frames <= MAX_FRAMES; frames++) {
                int previousFaults = fifoFaults[frames - 2].get();
                int currentFaults = fifoFaults[frames - 1].get();
                if (currentFaults > previousFaults) {
                    fifoAnomalies++;
                    int delta = currentFaults - previousFaults;
                    if (delta > maxDelta){
                        maxDelta = delta;
                    }
                    anomalyDetails.add(String.format(
                            "Simulation #%03d: Anomaly at %3d frames (%d PFs) vs. %3d frames (%d PFs) (Δ%d)",
                            sim + 1, frames - 1, previousFaults, frames, currentFaults, delta
                    ));
                }
            }

            for (int frames = 1; frames <= MAX_FRAMES; frames++) {
                int fifo = fifoFaults[frames - 1].get();
                int lru = lruFaults[frames - 1].get();
                int mru = mruFaults[frames - 1].get();

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
        if (anomalyDetails.isEmpty()) {
            System.out.println("  No anomalies detected in " + NUM_SIMULATIONS + " simulations.");
        } else {
            anomalyDetails.forEach(System.out::println);
            System.out.printf(
                    "\nTotal anomalies detected: %d in %d simulations with a max delta of %d.",
                    fifoAnomalies, NUM_SIMULATIONS, maxDelta
            );
        }
    }
}