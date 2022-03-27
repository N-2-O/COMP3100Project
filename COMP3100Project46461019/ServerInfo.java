package COMP3100Project46461019;

public class ServerInfo {
    String serverName;
    int serverID;
    String status;
    int startTime;
    int core;
    int memory;
    int disk;
    int wJobs; //Jobs waiting
    int rJobs; //Jobs running

    public ServerInfo(String n, int id, String s, int st, int c, int m, int d, int wj, int rj) {
        serverName = n;
        serverID = id;
        status = s;
        startTime = st;
        core = c;
        memory = m;
        disk = d;
        wJobs = wj;
        rJobs = rj;
    }

    public ServerInfo(String[] info) {
        serverName = info[0];
        serverID = Integer.parseInt(info[1]);
        status = info[2];
        startTime = Integer.parseInt(info[3]);
        core = Integer.parseInt(info[4]);
        memory = Integer.parseInt(info[5]);
        disk = Integer.parseInt(info[6]);
        wJobs = Integer.parseInt(info[7]);
        rJobs = Integer.parseInt(info[8]);
    }

    public String getName() {
        return this.serverName;
    }

    public int getCores() {
        return this.core;
    }

    public void printServer() {
        System.out.print(this.serverName + " ");
        System.out.print(this.serverID + " ");
        System.out.print(this.status + " ");
        System.out.print(this.startTime + " ");
        System.out.print(this.core + " ");
        System.out.print(this.memory + " ");
        System.out.print(this.disk + " ");
        System.out.println();
    }
}