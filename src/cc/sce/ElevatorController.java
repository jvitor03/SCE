package cc.sce;

import java.util.HashMap;
import java.util.LinkedList;

public class ElevatorController {

    private boolean permitRequests;
    private HashMap<Integer, LinkedList<Request>> requests;
    private HashMap<Integer, ElevatorState> stateFloor;
    private HashMap<Elevator, Integer> elevatorRequest;
    private int numberFloors;
    private Elevator[] elevators;

    public ElevatorController(int numberFloors) {
        this.permitRequests = true;
        this.numberFloors = numberFloors;
        this.requests = new HashMap<Integer, LinkedList<Request>>();
        this.stateFloor = new HashMap<Integer, ElevatorState>();
        this.elevatorRequest = new HashMap<Elevator, Integer>();
    }

    private synchronized int calculate(int currentFloor) {
        int offset = 0;
        int upValue   = 0;
        int downValue = 0;

        upValue = this.numberOfRequests(currentFloor);

        if( upValue > 0 ) {
            return currentFloor;
        }

        while ( upValue == 0 && downValue == 0 ) {
            offset++;
            if( currentFloor+offset < this.numberFloors ) {
                upValue = this.numberOfRequests(currentFloor+offset);
            }

            if( currentFloor-offset >= 0 ) {
                downValue = this.numberOfRequests(currentFloor-offset);
            }

            if( downValue == 0 && upValue==0 ) {
                continue;
            }
        }

        if( upValue >= downValue ) {
            return currentFloor+offset;
        } else {
            return currentFloor-offset;
        }
    }

    public void choose() {
        boolean firstTime = true;
        Elevator elevatorClosely = null;
        int absoluteDistance = -1;

        for(Integer i: this.requests.keySet()) {
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

    public synchronized int goToFloor(Elevator elevator) {

        while (numberOfRequests() == 0 && permitRequests) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if( !permitRequests ){
                return -1;  //tratar
            }
        }

        if (elevatorRequest.containsValue(elevator)){
            return elevatorRequest.get(elevator);
        }  else {
			//goToFloor(elevator);
        	return calculate(elevator.getCurrentFloor());
        }// Procuro o ID deste Elevador no array acima,

        // Se o elevatorId estiver no ARRAY_DOS_PICA retorno o andar que o mesmo deve atender, E troco o estado do andar para RUNNING.
        // senão, chamo goToFloor novamente.
    }

    public synchronized int numberOfRequests(int floor) {
        try {
            return this.requests.get(floor).size();
        } catch (Exception e){
            return 0;
        }
    }

    public synchronized int numberOfRequests() {
        int size = 0;
        for ( LinkedList<Request> q : requests.values() ) {
        	size+= q.size();
        }
        return size;
    }

    public synchronized void addRequest(int srcFloor, int dstFloor) {
        if( !this.requests.containsKey(srcFloor) ) {
            this.requests.put(srcFloor, new LinkedList<Request>());
        }

        LinkedList<Request> queue = requests.get(srcFloor);
        queue.add(new Request(dstFloor));
        requests.put(srcFloor, queue);
        //notifyAll();
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

    public synchronized void setElevators(Elevator[] elevators) {
        this.elevators = elevators;
    }

    public synchronized boolean getPermitRequest() {
        return this.permitRequests;
    }

    public void finishRequests() {
        permitRequests = false;
       // notifyAll();
    }

    public int getNumberFloors(){
    	return numberFloors;
    }

}
