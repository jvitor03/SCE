package cc.sce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
* This class executes the elevator's logic.
* @author Claudio Gonçalves
* @author João Vitor Rebouças
*/
public class ElevatorController {

    private boolean permitRequests;
    /** Requests per floor. */
    private HashMap<Integer, LinkedList<Request>> requests;
    /** Elevators attending requests. */
    private HashMap<Elevator, Integer> elevatorRequest;
    private int numberFloors;
    private Elevator[] elevators;

    /**
    * Constructor.
    * @param numberFloors (required) The number of floors in the building.
    */
    public ElevatorController(int numberFloors) {
        this.permitRequests = true;
        this.numberFloors = numberFloors;
        this.requests = new HashMap<Integer, LinkedList<Request>>();
        this.elevatorRequest = new HashMap<Elevator, Integer>();
    }

    /**
    * This method choose a floor for each elevator to attend.
    * it uses elevator distance from the floor and the number os requests as priority.
    */
    public synchronized void choose() {
        boolean firstTime = true;
        Elevator elevatorClosely = null;
        int absoluteDistance = -1;
        ArrayList<Integer> sortedListBySizeOfQueue = new ArrayList<Integer>();

        for(Integer i: this.requests.keySet()) {
            int priorityFloor = -1;
            int auxSize=-1;

            for(Integer j: this.requests.keySet()) {
                if( !sortedListBySizeOfQueue.contains(j) && this.requests.get(j).size()>auxSize ) {
                    priorityFloor=j;
                    auxSize=this.requests.get(j).size();
                }
            }
            sortedListBySizeOfQueue.add(priorityFloor);
        }

        for(Integer i: sortedListBySizeOfQueue) {
        	firstTime = true;

            for(Elevator e: this.elevators) {
                if( e.getElevatorState() == ElevatorState.WAITING && !this.elevatorRequest.containsKey(e) ) {
                    if( firstTime ) {
                        elevatorClosely = e;
                        absoluteDistance = Math.abs(i - e.getCurrentFloor());
                        firstTime = false;
                    } else {
                        int aux = Math.abs(i - e.getCurrentFloor());
                        if( aux <  absoluteDistance ) {
                            elevatorClosely = e;
                            absoluteDistance = aux;
                        }
                    }
                }
            }
            this.elevatorRequest.put(elevatorClosely, i);
       }
    }

    /**
    * This method sets the state RUNNING if an elevator has a floor to attend.
    * also it stops the thread if there's no more requests.
    * @param elevator The elevator.
    * @return The floor that must be attended to.
    */
    public synchronized int goToFloor(Elevator elevator) {

        while( ( this.numberOfRequests()>0 || this.permitRequests) && !this.elevatorRequest.containsKey(elevator) ) {
            try {
                System.out.println("I will blocked.");
                wait();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }

        if( !this.elevatorRequest.containsKey(elevator) )
            return elevator.getCurrentFloor();

        elevator.setElevatorState(ElevatorState.RUNNING);
        return this.elevatorRequest.get(elevator);
    }

    /**
    * Resets the elevator state to WAITING and remove it from the elevatorRequest HashMap then calls choose() for a new request.
    * If a thread is waiting, then its notified.
    * @param elevator An elevator.
    */
    public synchronized void terminateRound(Elevator elevator) {
        this.elevatorRequest.remove(elevator);
        elevator.setElevatorState(ElevatorState.WAITING);
        this.choose();

        try {
            notifyAll();
        } catch(Exception e) {}
    }

    /**
    * Calculates the number of requests in a determinated floor.
    * @param Floor Desired floor.
    * @return The number of requests in this floor.
    */
    public synchronized int numberOfRequests(int floor) {
        try {
            return this.requests.get(floor).size();
        } catch (Exception e){
            return 0;
        }
    }

    /**
    * @return The number of requests in all floors.
    */
    public synchronized int numberOfRequests() {
        int size = 0;
        for ( LinkedList<Request> q : requests.values() ) {
        	size+= q.size();
        }
        return size;
    }

    /**
    * This method add a new request to the requests HashMap
    * @param srcFloor The request's floor.
    * @param dstFloor The requested floor.
    */
    public synchronized void addRequest(int srcFloor, int dstFloor) {
        if( !this.requests.containsKey(srcFloor) ) {
            this.requests.put(srcFloor, new LinkedList<Request>());
        }

        LinkedList<Request> queue = requests.get(srcFloor);
        queue.add(new Request(dstFloor));
        requests.put(srcFloor, queue);
        //notifyAll();
    }

    /**
    * Get requests by its floor.
    * @param key The request's floor.
    * @return A linkedList with all floor's requests.
    * @deprecated Use getRequest instead.
    */
    public LinkedList<Request> getRequests(int key){
        return requests.get(key);
    }

    /**
    * This method return the requests in a determined floor.
    * @param currentFloor The desired floor.
    * @return The floor's requests.
    */
    public synchronized Request getRequest(int currentFloor) {

        if( !this.requests.containsKey(currentFloor) )
            return null;

        LinkedList<Request> queue = this.requests.get(currentFloor);

        if( queue.size() == 1 )
            this.requests.remove(currentFloor);

        return queue.poll();
    }

    /**
    * Set the elevators controlled by this class.
    * @param elevators A array containing the elevators.
    */
    public synchronized void setElevators(Elevator[] elevators) {
        this.elevators = elevators;
    }

    /** 
    * @return <tt>true</tt> If it can receive more requests. 
    */
    public synchronized boolean getPermitRequest() {
        return this.permitRequests;
    }

    /** Finish the requests and notify the elevators. */
    public void finishRequests() {
        permitRequests = false;

        try {
            notifyAll();
        } catch(Exception e) {}
    }

    /** Return the number of floors in the building. */
    public int getNumberFloors(){
    	return numberFloors;
    }

}
