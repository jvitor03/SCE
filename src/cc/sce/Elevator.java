package cc.sce;

public class Elevator extends Thread {

    private int id;
    private int currentFloor;
    private int maxPeople;
    private ElevatorController controller;
    private Logger log;
    // private ArrayList<Integer> people;

    public Elevator(int id, int startingFloor, int maxPeople, ElevatorController controller) {
        this.id = id;
        this.currentFloor = startingFloor;
        this.maxPeople = maxPeople;
        this.controller = controller;
        this.log = new Logger(this.id);
    }

    public void run() {
        log.write("Turn on service.");
        log.write("Starting floor: " + this.currentFloor);
        
        // TODO:
        
        log.write("Ending floor: " + this.currentFloor);
        log.write("Turn off elevator.");
        log.close();
    }

    @SuppressWarnings("finally")
    public int down() {
        try {
            Thread.sleep( Main.ELEVATOR_TIME_TO_UP_DOWN * 1000);

            if( this.currentFloor > 0 ) {
                this.currentFloor--;
            } else{
                throw new RuntimeException("[EXCEPTION] Impossible move down elevator.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            return this.currentFloor;
        }
    }

    @SuppressWarnings("finally")
    public int up() {
        try {
            Thread.sleep( Main.ELEVATOR_TIME_TO_UP_DOWN * 1000);

            if( this.currentFloor < this.maxPeople ) {
                this.currentFloor++;
            } else{
                throw new RuntimeException("[EXCEPTION] Impossible move up elevator.");
            }
        } catch(InterruptedException e) {
            e.printStackTrace();
        } finally {
            return this.currentFloor;
        }
    }

    public int getCurrentFloor() {
        return this.currentFloor;
    }

}
