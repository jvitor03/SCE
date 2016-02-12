package cc.sce;

import java.util.HashMap;
import java.util.LinkedList;

public class ElevatorController {

    private boolean permitRequests;
    private HashMap<Integer, LinkedList<Request>> requests;
    private Elevator[] elevators;

    public ElevatorController() {
        this.permitRequests = true;
        this.requests = new HashMap<Integer, LinkedList<Request>>();
    }

    public int goToFloor(int currentFloor) {
        return 0;
    }

    public int numberOfRequests(int floor) {
        return requests.get(new Integer(floor)).size();
    }

    public int numberOfRequests() {
        return requests.size();
    }

    public void addRequest(int srcFloor, int dstFloor) {
        if( !this.requests.containsKey(srcFloor) ) {
            this.requests.put(srcFloor, new LinkedList<Request>());
        }

        this.requests.get(srcFloor).add(new Request(dstFloor));
    }
    
    public Request getRequest(int currentFloor) {
        LinkedList<Request> queue = this.requests.get(currentFloor);
        
        if( queue != null ) {
            return queue.poll();
        }
        
        return null;
    }

    public void setElevators(Elevator[] elevators) {
        this.elevators = elevators;
    }

    public boolean getPermitRequest() {
        return this.permitRequests;
    }

    public void finishRequests() {
        permitRequests = false;
    }

}
