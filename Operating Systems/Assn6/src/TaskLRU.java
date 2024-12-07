import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.CountDownLatch;

public class TaskLRU implements Runnable {
    private final int[] sequence;
    private final int maxMemoryFrames;
    private final AtomicInteger[] pageFaults;
    private final CountDownLatch latch;

    public TaskLRU(int[] sequence, int maxMemoryFrames, AtomicInteger[] pageFaults, CountDownLatch latch) {
        this.sequence = sequence;
        this.maxMemoryFrames = maxMemoryFrames;
        this.pageFaults = pageFaults;
        this.latch = latch;
    }

    @Override
    public void run() {
        List<Integer> memory = new LinkedList<>();
        int faults = 0;

        for (int page : sequence) {
            if (!memory.contains(page)) {
                faults++;
                if (memory.size() == maxMemoryFrames) {
                    memory.remove(0);
                }
                memory.add(page);
            } else {
                memory.remove((Integer) page);
                memory.add(page);
            }
        }

        pageFaults[maxMemoryFrames - 1].set(faults);
        latch.countDown();
    }
}