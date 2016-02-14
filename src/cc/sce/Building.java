package cc.sce;

/**
* This class create and start the elevators, also create the requests.
* @author Claudio Gonçalves
* @author João Vitor Rebouças
*/
public class Building {

	private int numberFloors;
	private int numberElevators;
	private ElevatorController controller;
	private Elevator[] elevators;

	/**
	* Constructor.
	* @param numberFloors (required) Number of floors in the building
	* @param numberElevators (required) Number of elevators in the building
	*/
	public Building(int numberFloors, int numberElevators) {
		this.numberFloors = numberFloors;
		this.numberElevators = numberElevators;
		controller = new ElevatorController(numberFloors);
		this.elevators = new Elevator[numberElevators];
	}

	/**
	* Add a new request.
	* This method add a request from the input into the requests HashMap in the controller.
	* @param srcFloor The source floor, where the request originate.
	* @param dstFloor the destination floor.
	*/
	public void addRequest(int srcFloor, int dstFloor) {
		controller.addRequest(srcFloor, dstFloor);
	}

	/**
	* Create a new elevator
	* @param index The elevator index.
	* @param maxPeoplePerElevator
	* @param startingFloor The floor where the elevator will be when started.
	*/
	public void createElevator(int index, int maxPeoplePerElevator, int startingFloor) {
		try {
			elevators[index] = new Elevator(index, startingFloor, maxPeoplePerElevator, this.controller);
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	
	/** Start the elevator threads. */
	public void startElevators() {
        this.controller.setElevators(this.elevators);
		this.controller.choose();

		for (int i = 0; i < this.numberElevators; i++) {
			this.elevators[i].start();
		}
	}

	/** Return the elevatorController passed to the contructor. */
	public ElevatorController getElevatorController() {
		return this.controller;
	}

	/** Return the number of floors in the building */
	public int getNumberFloors() {
		return this.numberFloors;
	}

	/** Return the number of elevators in the building */
	public int getNumberElevators() {
		return this.numberElevators;
	}

}
