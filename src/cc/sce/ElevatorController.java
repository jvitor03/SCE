package cc.sce;

import java.util.HashMap;
import java.util.LinkedList;

public class ElevatorController {

    private boolean permitRequests;
    private HashMap<Integer, LinkedList<Request>> requests;
    private HashMap<Integer, State> stateFloor;
    private HashMap<Elavator, Integer> array_dos_pica;
    private int numberFloors;
    private Elevator[] elevators;

    public ElevatorController(int numberFloors) {
        this.permitRequests = true;
        this.numberFloors = numberFloors;
        this.requests = new HashMap<Integer, LinkedList<Request>>();
        this.stateFloor = new HashMap<Integer, State>();
        this.array_dos_pica = new HashMap<Elevartor, Integer>();
    }

    private synchronized int chooseFloor() {

        LinkedList<Request> floorList = this.getRequests(currentFloor);
        int offset = 0;
        boolean signal = true;

        while ( floorList == null && permitRequests ){
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
     s   }
    }

    /*
        5 2 10
        0 4
        Andar 0: 3  2 2 1
        Andar 1:
        Andar 2:
        Andar 3: 3 0 1 1
        Andar 4:

        choose();

        ARRAY_DOS_PICA = [ (0, elevador0), (3, elevador1) ]

        goToFloor();
            -> for( ARRAY_DOS_PICA existe elevador_id )
        SE  EXISTIR
            return andar
        SENÃO
            recalculate()
    */



    // Pior... Choose() só deveria ao terminar de receber as requisições.
    private synchronized void choose() {

        for(Elevator e: elevators) {
            if(el.getState() == State.WAITING ) {
                for(Integer floor: this.requests.keys()) {
                    if( e.getCurrentFloor() ) {

                    }
                }
            }
        }

    }

    public synchronized int goToFloor(Elevator elevator) {

        while (numberOfRequests() == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if( !permitRequests ){
                return -1;  //tratar
            }
        }

        // ARRAY_DOS_PICA = [(ID_Elevador, andar destino)]
        choose(); // este método preenche um array com os (ID Elevador, andar que deve ir)

        if (array_dos_pica.containsValue(elevator)){
            return array_dos_pica.get(elevator);
        }  else {
            goToFloor(elevator);
        }// Procuro o ID deste Elevador no array acima,

        // Se o elevatorId estiver no ARRAY_DOS_PICA retorno o andar que o mesmo deve atender, E troco o estado do andar para RUNNING.
        // senão, chamo goToFloor novamente.

        //return chooseFloor();
    }

    public synchronized int numberOfRequests(int floor) {
        try {
            return requests.get(new Integer(floor)).size();
        } catch (Exception e){
            return 0;
        }
    }

    public synchronized int numberOfRequests() {
        return requests.size();
    }

    public synchronized void addRequest(int srcFloor, int dstFloor) {
        if( !this.requests.containsKey(srcFloor) ) {
            this.requests.put(srcFloor, new LinkedList<Request>());
        }

        LinkedList<Request> queue = requests.get(srcFloor);
        queue.add(new Request(dstFloor));
        requests.put(srcFloor, queue);

        stateFloor.add(srcFloor, State.WAITING);

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

    public synchronized void setElevators(Elevator[] elevators) {
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
