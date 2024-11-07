//round robin
import java.util.LinkedList;
import java.util.Queue;
public class SchedulerRR extends Scheduler{
        private Queue<Process> readyQueue; // Queue to store ready processes
        private Platform platform;
        private int time;
        private int savedTime;

        public SchedulerRR(Platform platform, int time) {
            this.platform = platform;
            this.readyQueue = new LinkedList<>();
            this.time = time;
            this.savedTime=time;
        }
        //starting with fcfs code, modify to take time into account
        public void notifyNewProcess(Process process){
            readyQueue.add(process);
        }
        public Process update(Process process, int cpu) {
            if (process == null) {
                if (!readyQueue.isEmpty()){
                    time = savedTime;
                    Process next= readyQueue.poll();
                    platform.log("CPU "+cpu+" > Scheduled "+ next.getName());
                    time--;
                    contextSwitches++;
                    return next;
                }else {
                    return null;
                }
            }
            //check if time is up
            if (time == 0) {
                    readyQueue.add(process);
                    Process next = readyQueue.poll();
                    platform.log("CPU " + cpu + " > Time quantum complete for process " + process.getName());
                    contextSwitches++;
                    platform.log("CPU " + cpu + " > Scheduled " + next.getName());
                    contextSwitches++;
                    time = savedTime - 1; // Reset time and start counting down immediately
                    return next;
            }
            //check if burst is done
            if (!process.isBurstComplete()) {
                time--;
                return process;
            } else {
                // Process burst is complete
                if (!process.isExecutionComplete()) {
                    platform.log("CPU " + cpu + " > Process " + process.getName() + " burst complete");
                    contextSwitches++;
                    readyQueue.add(process);
                } else {
                    platform.log("CPU " + cpu + " > Process " + process.getName() + " burst complete");
                    platform.log("CPU " + cpu + " > Process " + process.getName() + " execution complete");
                    contextSwitches++;
                }

                // Schedule the next process in the queue
                if (!readyQueue.isEmpty()) {
                    Process nextProcess = readyQueue.poll();
                    platform.log("CPU " + cpu + " > Scheduled " + nextProcess.getName());
                    contextSwitches++;
                    time = savedTime;
                    return nextProcess;
                } else {
                    return null;
                }
            }
    }
}