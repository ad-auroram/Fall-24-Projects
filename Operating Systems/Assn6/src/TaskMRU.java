import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class TaskMRU implements Runnable{
    private final int[] sequence;
    private final int maxMemoryFrames;
    private final int[] pageFaults;

    public TaskMRU(int[] sequence, int maxMemoryFrames, int[] pageFaults) {
            this.sequence = sequence;
            this.maxMemoryFrames = maxMemoryFrames;
            this.pageFaults = pageFaults;
    }

    //FIFO, change later
    @Override
    public void run() {
        List<Integer> memory = new ArrayList<>();
        int faults = 0;

        for (int page : sequence) {
            if (!memory.contains(page)) {
                faults++;
                if (memory.size() == maxMemoryFrames) {
                    //remove old page
                    memory.remove(0);
                }
                memory.add(page);
            }
        }

        pageFaults[maxMemoryFrames - 1] = faults;
    }
}
