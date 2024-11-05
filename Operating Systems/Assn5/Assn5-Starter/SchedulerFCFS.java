//first come first serve
//whatever's put on the queue first is the one that gets workd on first

public class SchedulerFCFS extends Scheduler{
    public SchedulerFCFS(Platform platform) {
        super();
    }

    public void notifyNewProcess(Process process){
        System.out.println("new Process");
    }
    public Process update(Process process, int cpu){
        return process;
    }
}