import java.util.ArrayList;
import java.util.List;

public class TaskFIFO implements Runnable {
    private final int[] sequence;
    private final int maxMemoryFrames;
    private final int[] pageFaults;

    public TaskFIFO(int[] sequence, int maxMemoryFrames, int[] pageFaults) {
        this.sequence = sequence;
        this.maxMemoryFrames = maxMemoryFrames;
        this.pageFaults = pageFaults;
    }

    @Override
    public void run() {
        List<Integer> memory = new ArrayList<>();
        int faults = 0;

        for (int page : sequence) {
            if (!memory.contains(page)) {
                faults++;
                if (memory.size() == maxMemoryFrames) {
                    memory.remove(0); // Remove the oldest page
                }
                memory.add(page); // Add the new page
            }
        }

        pageFaults[maxMemoryFrames - 1] = faults;
    }
}