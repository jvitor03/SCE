package cc.sce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class ElevatorController {

    private boolean permitRequests;
    private HashMap<Integer, LinkedList<Request>> requests;
    private int numberFloors;
    private HashMap<Integer,Integer> startingFloors;
    private Elevator[] elevators;

    public ElevatorController(int numberFloors) {
        permitRequests = true;
        this.numberFloors = numberFloors;
        requests = new HashMap<Integer, LinkedList<Request>>();
        startingFloors = new HashMap<Integer,Integer>();
    }
    
    

    public synchronized int goToFloor(int elevatorId, int currentFloor) {  	
    	startingFloors.put(elevatorId, currentFloor);
    	
    	while (numberOfRequests() == 0){
    		try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		if (!permitRequests){
    			return -1;//tratar
    		}
    	}
    	
    	LinkedList<Request> floorList = this.getRequests(currentFloor);
    	int offset = 0;
    	boolean signal = true;
    	
    	while (floorList == null && permitRequests){   	
    		offset++;
	    	if (numberOfRequests(currentFloor + offset) >= numberOfRequests(currentFloor - offset)){    			
	    		signal = true;
	    		floorList = this.getRequests(currentFloor + offset);
	    	} else {
	    		signal = false;
	    		floorList = this.getRequests(currentFloor - offset);
	    	}
        }
    	
    	//requests.remove(currentFloor + floor);
    	if (signal){
    		return currentFloor + offset;
    	} else {
    		return currentFloor - offset;
    	}
    	
    }

    public synchronized int numberOfRequests(int floor) {
        try {
        	return requests.get(new Integer(floor)).size();
        } catch (Exception e){
        	return 0;
        }
    }

    public int numberOfRequests() {
        return requests.size();
    }

    public void addRequest(int srcFloor, int dstFloor) {
        if( !this.requests.containsKey(srcFloor) ) {
            this.requests.put(srcFloor, new LinkedList<Request>());
        }
        LinkedList<Request> queue = requests.get(srcFloor);
        queue.add(new Request(dstFloor));
        requests.put(srcFloor, queue);
        notifyAll();
    }

    public LinkedList<Request> getRequests(int key){
        return requests.get(key);
    }
    
    public synchronized Request getRequest(int currentFloor) {
        LinkedList<Request> queue = this.requests.get(currentFloor);
        
        if( queue != null ) {
            return queue.poll();
        }
        
        return null;
    }

    public void setElevators(Elevator[] elevators) {
        this.elevators = elevators;
    }

    public synchronized boolean getPermitRequest() {
        return this.permitRequests;
    }

    public void finishRequests() {
        permitRequests = false;
        notifyAll();
    }

}
