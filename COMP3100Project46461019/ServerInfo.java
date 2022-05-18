package COMP3100Project46461019;

public class ServerInfo {
    String serverName;
    int serverID;
    String status;
    int startTime;
    int core;
    int memory;
    int disk;
    int availCore;
    int availMem;
    int availDisk;
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
        availCore = core;
        availMem = memory;
        availDisk = disk;
        wJobs = Integer.parseInt(info[7]);
        rJobs = Integer.parseInt(info[8]);
    }

    /**
     * Constructor for empty server
     */
    public ServerInfo() {
        serverName = "";
        serverID = 0;
        status = "";
        startTime = 0;
        core = 0;
        memory = 0;
        disk = 0;
        availCore = core;
        availMem = memory;
        availDisk = disk;
        wJobs = 0;
        rJobs = 0;
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
     * @return - The number of cores available
     */
    public int getAvailCores() {
        return this.availCore;
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
     * @return - The amount of memory available
     */
    public int getAvailMem() {
        return this.availMem;
    }

    /**
     * 
     * @return - The amount of disk available
     */
    public int getAvailDisk() {
        return this.availDisk;
    }

    /**
     * 
     * @return - The server ID
     */
    public int getID() {
        return this.serverID;
    }

    /**
     * Sets the available cores of server after taking a job
     * @param taken - The amount of cores taken (or freed) by a job
     */
    public void setAvailCores(int taken) {
        this.availCore = this.availCore + taken;
    }

    /**
     * Sets the available memory of server after taking a job
     * @param taken - The amount of memory taken (or freed) by a job
     */
    public void setAvailMem(int taken) {
        this.availMem = this.availMem + taken;
    }

    /**
     * Sets the available disk of server after taking a job
     * @param taken - The amount of disk taken (or freed) by a job
     */
    public void setAvailDisk(int taken) {
        this.availDisk = this.availDisk + taken;
    }
    
    /**
     * 
     * @param c -
     * @param m
     * @param d
     */
    public void updateServer(int c, int m, int d) {
        this.setAvailCores(c);
        this.setAvailMem(m);
        this.setAvailDisk(d);
    }

    /**
     * 
     * @return - True if there are jobs running
     */
    public boolean hasJobRunning() {
        return (this.rJobs > 0);
    }

    /**
     * 
     * @return - True if there are jobs waiting
     */
    public boolean hasJobWaiting() {
        return (this.wJobs > 0);
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
        System.out.print(this.availCore + " ");
        System.out.print(this.availMem + " ");
        System.out.print(this.availDisk + " ");
        System.out.print(this.wJobs + " ");
        System.out.print(this.rJobs + " ");
        System.out.println();
    }

    public static ServerInfo findServer(String serverName, int serverID, ServerInfo[] servers) {
        for (ServerInfo i : servers) {
            if (i.getName().equals(serverName) && i.getID() == serverID) {
                return i;
            }
        }
        return new ServerInfo();
    }

    /**
     * Prints out server information
     * @param servers - An array containing servers to be printed
     */
    public static void viewServers(ServerInfo[] servers) {
        // Print server information
        for (int i = 0; i < servers.length; i++) {
            servers[i].printServer();
        }
    }

    /**
     * Filter servers of the given server type
     * @param servers - The array of servers
     * @param serverName - The name of the server to be filtered
     * @return An array containing servers of the same type
     */
    public static ServerInfo[] filterServersByName(ServerInfo[] servers, String serverName) {
        int num = 0;
        for (int i = 0; i < servers.length; i ++) {
            if (servers[i].getName().equals(serverName)) {
                num++;
            }
        }
        ServerInfo[] filtered = new ServerInfo[num];
        int index = 0;
        for (int i = 0; i < servers.length; i++) {
            if (servers[i].getName().equals(serverName)) {
                filtered[index] = servers[i];
                index++;
            }
        }
        return filtered;
    }

    /**
     * Finds the largest server type in a given array of servers
     * @param servers - The array of servers provided by server
     * @return The name of the largest server
     */
    public static String findLargestServerType(ServerInfo[] servers) {
        // Finds the name of the largest server
        String largestServer = servers[0].getName();
        int largestCore = servers[0].getCores();
        for (int i = 0; i < servers.length; i++) {
            if (servers[i].getCores() > largestCore) {
                largestServer = servers[i].getName();
                largestCore = servers[i].getCores();
            }
        }
        return largestServer;
    }

    /**
     * 
     * @param servers - The array of servers
     * @param job - The job information
     * @param minRemainingPartition - The minimum partition specified
     * @return - The first server that meets minimum core requirements and leaves a specified core partition
     */
    public static ServerInfo findClosestCore(ServerInfo[] servers, JobInfo job, int minRemainingPartition) {
        ServerInfo server = new ServerInfo();
        for (int i = 0; i < servers.length; i++) { 
            if (servers[i].getAvailCores() >= job.getCores() + minRemainingPartition) { //the first server that meets minimum core requirements and has no job waiting
                server = servers[i];
                return server;
            }
        } 

        for (int i = servers.length - 1; i >= 0; i--) { 
            if (servers[i].getAvailCores() >= job.getCores()) { //the largest first server that meets minimum core requirements 
                server = servers[i];
                return server;
            }
        } 
        
        for (int i = 0; i < servers.length; i++) {
            if (servers[i].getCores() >= job.getCores()) { //assign jobs to first capable
                server = servers[i];
                return server;
            }
        } 
        return server;
    }

}