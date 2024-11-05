//round robin
public class SchedulerRR extends Scheduler{
    public SchedulerRR(Platform platform, int time) {
        super();
    }
    public void notifyNewProcess(Process process){
        System.out.println("new Process");
    }
    public Process update(Process process, int cpu){
        return process;
    }
}