//shortest job first
//whichever has the shortst time to complete goes first
public class SchedulerSJF extends Scheduler{
    public SchedulerSJF(Platform platform) {
        super();
    }

    public void notifyNewProcess(Process process){
        System.out.println("new Process");
    }
    public Process update(Process process, int cpu){
        return process;
    }
}