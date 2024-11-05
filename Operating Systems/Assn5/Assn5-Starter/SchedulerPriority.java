//
public class SchedulerPriority extends Scheduler{

    public SchedulerPriority(Platform platform) {
        super();
    }
    public void notifyNewProcess(Process process){
        System.out.println("new Process");
    }
    public Process update(Process process, int cpu){
        return process;
    }
}