package cc.sce;

public class Building {

    private int numberFloors;
    private int numberElevators;
    private ElevatorController controller;
    private Elevator[] elevators;

    public Building(int numberFloors, int numberElevators) {
        this.numberFloors = numberFloors;
        this.numberElevators = numberElevators;
        controller = new ElevatorController();
        this.elevators  = new Elevator[numberElevators];
    }

    public void addRequest(int srcFloor, int dstFloor) {
        controller.addRequest(srcFloor, dstFloor);
    }

    public void createElevator(int index, int maxPeoplePerElevator, int startingFloor) {
        try {
            elevators[index] = new Elevator(startingFloor, maxPeoplePerElevator, this.controller);
        } catch(IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public void startElevators() {
        this.controller.setElevators(this.elevators);
        
        for(int i=0; i<this.numberElevators; i++) {
            elevators[i].start();
        }
    }

    public ElevatorController getElevatorController() {
        return this.controller;
    }

    public int getNumberFloors() {
        return this.numberFloors;
    }

    public int getNumberElevators() {
        return this.numberElevators;
    }

}
