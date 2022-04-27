package COMP3100Project46461019;

public class JobInfo {
    int submitTime;
    int index;
    int runTime;
    int coresRequired;
    int memRequired;
    int diskRequired;

    public JobInfo(int st, int i, int rt, int cr, int mr, int dr) {
        submitTime = st;
        index = i;
        runTime = rt;
        coresRequired = cr;
        memRequired = mr;
        diskRequired = dr;
    }

    public JobInfo(String[] info) {
        submitTime = Integer.parseInt(info[1]);
        index = Integer.parseInt(info[2]);
        runTime = Integer.parseInt(info[3]);
        coresRequired = Integer.parseInt(info[4]);
        memRequired = Integer.parseInt(info[5]);
        diskRequired = Integer.parseInt(info[6]);
    }

    public int getIndex() {
        return this.index;
    }

    public int getCores() {
        return this.coresRequired;
    }

    public int getMem() {
        return this.memRequired;
    }

    public int getDisk() {
        return this.diskRequired;
    }

    public void printJob() {
        System.out.print(this.submitTime + " ");
        System.out.print(this.index + " ");
        System.out.print(this.runTime+ " ");
        System.out.print(this.coresRequired+ " ");
        System.out.print(this.memRequired + " ");
        System.out.print(this.diskRequired + " ");
        System.out.println();
    }
}