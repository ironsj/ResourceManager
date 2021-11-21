package ResourceManagerPackage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Scanner;

public class ResourceManger {
    Scanner scnr;

    //number of processes in simulation
    private final int numProcesses;
    //number of resources in simulation
    private final int numResources;
    //matrix representing the processes holding instances of resources
    private final int[][] holdMatrix;
    //matrix representing the processes waiting on instances of resources
    private final int[][] waitMatrix;
    //matrix representing the number of instances of each resource that is available
    private final int[] available;

    //array that represents a possible run sequence in the simulation
    private final int[] possibleRunSequence;
    //arraylist that holds processes that are currently blocked
    private ArrayList<Integer> blockList;

    public ResourceManger(String fileName) throws FileNotFoundException {
        //creates stream for input file
        FileInputStream fileByteStream = new FileInputStream(fileName);
        //scans the file for desired inputs
        scnr = new Scanner(fileByteStream);
        scnr.useDelimiter("[\\s \n]+");

        //takes the number of processes indicated in the file
        numProcesses = Integer.parseInt(scnr.next());
        //takes the number of resources indicated in the file
        numResources = Integer.parseInt(scnr.next());

        //instantiates arrays and matrices to proper size based on number of resources and processes
        available = new int[numResources];
        holdMatrix = new int[numProcesses][numResources];
        waitMatrix = new int[numProcesses][numResources];

        //fills available array with correct values indicated in the input file
        for(int i = 0; i < numResources; i++){
            available[i] = Integer.parseInt(scnr.next());
        }

        //instantiates the arraylist and array that holds a possible run sequence
        possibleRunSequence = new int[numProcesses];
        blockList = new ArrayList<>();
    }

    //checks if the simulation is in deadlock after each step
    private boolean checkDeadlock(){
        //copies available array into open
        int[] open = new int[numResources];
        System.arraycopy(available, 0, open, 0, numResources);

        //creates array that indicates if the processes can run or not
        boolean[] finish = new boolean[numProcesses];

        int numStartingPoints = 0;
        int numAttempts = 0;

        do {
            //loops through each process
            for (int i = 0; i < numProcesses; i++) {
                //indicates if process can run
                boolean check = true;
                //checks if the process has been able to run yet
                if (!finish[i]) {
                    //loops through each resource and checks if the current process can run based on the available resources
                    for (int j = 0; j < numResources; j++) {
                        if (open[j] < waitMatrix[i][j]) {
                            //if the process is waiting on a resource that can't be satisfied, the process can not run
                            check = false;
                            break;
                        }
                    }

                    //if the process can run
                    if (check) {
                        //adds the resources the process holds to the open array
                        for (int j = 0; j < numResources; j++) {
                            open[j] += holdMatrix[i][j];
                        }
                        //adds process to the possible run sequence
                        possibleRunSequence[numAttempts] = i;
                        numAttempts++;
                        //marks the process as able to run
                        finish[i] = true;
                    }
                }


            }
            //the number of processes we have tried to run with open array
            numStartingPoints++;
            //
        }while(numStartingPoints < numProcesses && numAttempts < numProcesses);

        //if any of the processes were not able to run in the sequence a deadlock has been reached
        for (boolean b : finish) {
            if (!b) {
                return false;
            }
        }
        //if all processes are able to run then return true (no deadlock)
        return true;
    }


    //executes the next line in the input file
    public void executeNextLine(){
        if(scnr.hasNext()) {
            //request or release
            String action = scnr.next();
            //which process is requesting/releasing
            int process = Integer.parseInt(scnr.next());
            //which resource they are requesting/releasing
            int resource = Integer.parseInt(scnr.next());
            //the number of units they are requesting/releasing
            int numUnits = Integer.parseInt(scnr.next());
            //keeps track of processes to be unblocked
            ArrayList<Integer> unblocked = new ArrayList<>();

            //the case when a process requests
            if (action.equals("request")) {
                //handles the case when there are enough resources for the request
                if (numUnits <= available[resource]) {
                    holdMatrix[process][resource] += numUnits;
                    available[resource] -= numUnits;
                    System.out.println("P" + process + " requested " + numUnits + " units of R" + resource + " and granted");
                }
                else { //handles the case when there are not enough resources for the request
                    holdMatrix[process][resource] += available[resource];
                    System.out.println("P" + process + " requested " + numUnits + " units of R" + resource +
                            " but only given " + available[resource] + ". P" + process + " is now blocked");
                    //process is now blocked
                    blockList.add(process);
                    waitMatrix[process][resource] += numUnits - available[resource];
                    available[resource] = 0;

                }

            } else { //the case when a process releases resources
                holdMatrix[process][resource] -= numUnits;
                available[resource] += numUnits;
                System.out.print("P" + process + " freed up " + numUnits + " units of R" + resource + ".");

                //handles processes that are waiting on recently released resources
                for (Integer pro : blockList) {
                    if (waitMatrix[pro][resource] != 0) {
                        int temp;
                        //handles the case when blocked process still needs more than available
                        if (waitMatrix[pro][resource] > available[resource]) {
                            temp = available[resource];
                            holdMatrix[pro][resource] += available[resource];
                            available[resource] = 0;
                            waitMatrix[pro][resource] -= temp;
                            System.out.print(" And P" + pro + " is allocated more resources.");
                        }
                        else { //handles the case when there are enough resources for a blocked process to be satisfied
                            temp = waitMatrix[pro][resource];
                            holdMatrix[pro][resource] += waitMatrix[pro][resource];
                            waitMatrix[pro][resource] = 0;
                            available[resource] -= temp;
                            unblocked.add(pro);
                            System.out.print(" And P" + pro + " is allocated more resources.");
                        }
                        //if there are no more instances of the resource to allocate, break from the loop
                        if (available[resource] == 0) {
                            break;
                        }
                    }
                }
                System.out.println();

                //removes processes that have been unblocked from the block list
                Iterator<Integer> itr = blockList.iterator();
                while (itr.hasNext()) {
                    int blocked = itr.next();
                    if (unblocked.contains(blocked)) {
                        itr.remove();
                    }
                }
            }

            //tells users if the simulation is deadlocked or not
            if (!checkDeadlock()) {
                System.out.println("All processes are deadlocked");
            } else {
                System.out.print("No deadlock. Possible run sequence: ");
                for (int i = 0; i < numProcesses; i++) {
                    System.out.print("P" + possibleRunSequence[i] + " ");
                }
                System.out.println();
            }
        }
        else{
            System.out.println("End of file");
        }
    }

    //returns the number of processes
    public int getNumProcesses() {
        return numProcesses;
    }

    //returns the number of resources
    public int getNumResources() {
        return numResources;
    }

    //returns the current hold matrix
    public int[][] getHoldMatrix() {
        return holdMatrix;
    }

    //returns the current wait matrix
    public int[][] getWaitMatrix() {
        return waitMatrix;
    }

    //returns the current available matrix
    public int[] getAvailable() {
        return available;
    }


}
