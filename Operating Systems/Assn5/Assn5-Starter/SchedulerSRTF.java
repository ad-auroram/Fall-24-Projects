//shortest remaining time first
public class SchedulerSRTF extends Scheduler{
    public SchedulerSRTF(Platform platform) {
        super();
    }

    public void notifyNewProcess(Process process){
        System.out.println("new Process");
    }
    public Process update(Process process, int cpu){
        return process;
    }
}