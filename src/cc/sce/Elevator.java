package cc.sce;

public class Elevator extends Thread {

    private int currentFloor;
    private int maxPeople;
    private ElevatorController controller;
    // private ArrayList<Integer> people;

    public Elevator(int startingFloor, int maxPeople, ElevatorController controller) {
        this.currentFloor = startingFloor;
        this.maxPeople = maxPeople;
        this.controller = controller;
    }

    // TODO:
    public void run() {
        while( controller.numberOfRequests() > 0  && !controller.getPermitRequest() ) {
            /*int goToFloor = controller.goToFloor(this.currentFloor);

            if( goToFloor >= this.currentFloor ) {
                for(int i=this.currentFloor; i<goToFloor; i++)
                    this.up();
            } else {
                for(int i=this.currentFloor; i>goToFloor; i--){
                    this.down();
                    System.out.println(this + " " + this.currentFloor );
                }
            }*/
        }
    }

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
