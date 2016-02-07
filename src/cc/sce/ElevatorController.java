package cc.sce;

import java.util.HashMap;
import java.util.LinkedList;

public class ElevatorController {

    private boolean permitRequests;
    private HashMap<Integer, LinkedList<Integer>> requests;

    public ElevatorController() {
        permitRequests = true;
        requests = new HashMap<Integer, LinkedList<Integer>>();
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
        Integer source = new Integer(srcFloor);
        Integer destination = new Integer(dstFloor);

        if( !requests.containsKey(source) ) {
            requests.put(source, new LinkedList<Integer>());
        }

        LinkedList<Integer> queue = requests.get(source);
        queue.add(destination);

        requests.put(source, queue);
    }

    public boolean getPermitRequest() {
        return this.permitRequests;
    }

    public void finishRequests() {
        permitRequests = false;
    }

}
