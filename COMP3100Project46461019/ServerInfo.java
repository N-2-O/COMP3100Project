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

    /**
     * Constructor to parse string array to to class ServerInfo
     * @param info - 
     */
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

    /**
     * 
     * @return - The name of the server
     */
    public String getName() {
        return this.serverName;
    }

    /**
     * 
     * @return - The number of cores 
     */
    public int getCores() {
        return this.core;
    }

    /**
     * 
     * @return - The server ID
     */
    public int getID() {
        return this.serverID;
    }

    /**
     * Print the server information
     */
    public void printServer() {
        System.out.print("Server: ");
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