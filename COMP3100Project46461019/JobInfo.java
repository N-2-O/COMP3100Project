package COMP3100Project46461019;
import java.util.ArrayList;

public class JobInfo {
    int submitTime;
    int index;
    int runTime;
    int coresRequired;
    int memRequired;
    int diskRequired;

    /**
     * Constructor to parse string array to class JobInfo
     * @param info - String array containing the job information
     */
    public JobInfo(String[] info) {
        submitTime = Integer.parseInt(info[1]);
        index = Integer.parseInt(info[2]);
        runTime = Integer.parseInt(info[3]);
        coresRequired = Integer.parseInt(info[4]);
        memRequired = Integer.parseInt(info[5]);
        diskRequired = Integer.parseInt(info[6]);
    }

    /**
     * Constructor
     */
    public JobInfo() {
        submitTime = 0;
        index = 0;
        runTime = 0;
        coresRequired = 0;
        memRequired = 0;
        diskRequired = 0;
    }

    /**
     * 
     * @return - The index of the job
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * 
     * @return - The cored required for the job
     */
    public int getCores() {
        return this.coresRequired;
    }

    /**
     * 
     * @return - The memory required for the job
     */
    public int getMem() {
        return this.memRequired;
    }

    /**
     * 
     * @return - The disk space required for the job
     */
    public int getDisk() {
        return this.diskRequired;
    }

    public static JobInfo getJob(ArrayList<JobInfo> jobs, int jobID) {
        for (int i = 0; i < jobs.size(); i++) {
            if (jobs.get(i).getIndex() == jobID) {
                return jobs.get(i);
            }
        }
        return new JobInfo();
    }

    /**
     * Prints job information in the same way displayed by the server
     */
    public void printJob() {
        System.out.print("Job ");
        System.out.print(this.submitTime + " ");
        System.out.print(this.index + " ");
        System.out.print(this.runTime+ " ");
        System.out.print(this.coresRequired+ " ");
        System.out.print(this.memRequired + " ");
        System.out.print(this.diskRequired + " ");
        System.out.println();
    }
}