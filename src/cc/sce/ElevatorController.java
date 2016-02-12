package cc.sce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class ElevatorController {

    private boolean permitRequests;
    private HashMap<Integer, LinkedList<Integer>> requests;
    private int numberFloors;
    private HashMap<Integer,Integer> startingFloors;

    public ElevatorController(int numberFloors) {
        permitRequests = true;
        this.numberFloors = numberFloors;
        requests = new HashMap<Integer, LinkedList<Integer>>();
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
    	
    	LinkedList<Integer> floorList = this.getRequests(currentFloor);
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
        Integer source = new Integer(srcFloor);
        Integer destination = new Integer(dstFloor);

        if( !requests.containsKey(source) ) {
            requests.put(source, new LinkedList<Integer>());
        }

        LinkedList<Integer> queue = requests.get(source);
        queue.add(destination);
        requests.put(source, queue);
        notifyAll();
    }

    public LinkedList<Integer> getRequests(int key){
        return requests.get(key);
    }

    public synchronized boolean getPermitRequest() {
        return this.permitRequests;
    }

    public void finishRequests() {
        permitRequests = false;
        notifyAll();
    }

}
