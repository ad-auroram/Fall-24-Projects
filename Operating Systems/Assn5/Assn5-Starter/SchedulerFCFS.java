//first come first serve
//whatever's put on the queue first is the one that gets workd on first

import java.util.LinkedList;
import java.util.Queue;

public class SchedulerFCFS extends Scheduler {
    private Queue<Process> readyQueue; // Queue to store ready processes
    private Platform platform;

    public SchedulerFCFS(Platform platform) {
        this.platform = platform;
        this.readyQueue = new LinkedList<>();
    }

    /**
     * This method is called when a new process enters the ready state.
     * Add it to the ready queue.
     */

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
            }
            else{
                platform.log("CPU "+cpu+" > Process "+ process.getName()+" burst complete");
                platform.log("CPU "+cpu+" > Process "+ process.getName()+" execution complete");
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