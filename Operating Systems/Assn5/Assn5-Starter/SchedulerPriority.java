import java.util.Comparator;
import java.util.PriorityQueue;

//
public class SchedulerPriority extends Scheduler{

    private PriorityQueue<Process> readyQueue; // Queue to store ready processes
    private Platform platform;

    public SchedulerPriority(Platform platform) {
        this.platform = platform;
        readyQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getPriority));
    }

    public void notifyNewProcess(Process process) {
        readyQueue.add(process);

    }

    /**
     * Update method for scheduling a process on a CPU.
     * FCFS schedules the next process in the ready queue based on arrival time.
     */
    public Process update(Process process, int cpu) {
        if (process == null) {
            if (!readyQueue.isEmpty()){
                Process next= readyQueue.poll();
                platform.log("CPU "+cpu+" > Scheduled "+ next.getName());
                contextSwitches++;
                return next;
            } else {
                return null;
            }
        }

        if (!process.isBurstComplete()) {
            return process;
        } else {
            if (!process.isExecutionComplete()){
                platform.log("CPU "+cpu+" > Process "+ process.getName()+" burst complete");
                readyQueue.add(process);
                contextSwitches++;
            }
            else{
                platform.log("CPU "+cpu+" > Process "+ process.getName()+" burst complete");
                platform.log("CPU "+cpu+" > Process "+ process.getName()+" execution complete");
                contextSwitches++;
            }
            // go to next process if current is done
            if (!readyQueue.isEmpty()) {
                Process nextProcess = readyQueue.poll();
                platform.log("CPU "+cpu+" > Scheduled "+ nextProcess.getName());
                contextSwitches++;
                return nextProcess;
            } else {
                return null;
            }
        }
    }
}