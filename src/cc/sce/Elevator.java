package cc.sce;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

/**
* This class run and moves the elevator around the building.
* @author Claudio Gonçalves
* @author João Vitor Rebouças
*/
public class Elevator extends Thread {

	private int id;
	private int currentFloor;
	private int maxPeople;
	private ElevatorController controller;
	private Logger log;
	private ElevatorState elevatorState;

	/**
    * Constructor.
    * @param id (required) The elevator id.
    * @param startingFloor (required) The floor which the elevator will be when started.
    * @param maxPeople (required) Max quantity of people allowed in the elevator.
    * @param controller (required) The elevator controller.
    */
    public Elevator(int id, int startingFloor, int maxPeople, ElevatorController controller) {
		this.id = id;
		this.currentFloor = startingFloor;
		this.maxPeople = maxPeople;
		this.controller = controller;
		this.log = new Logger(this.id);
		this.elevatorState = ElevatorState.WAITING;
	}

    /**
    * Start a new thread.
    * This method execute the main elevator logic
    */
    public void run() {
        log.write("Turn on service.");
        log.write("Starting floor: " + this.currentFloor + ".");

        while( this.controller.numberOfRequests() > 0 || this.controller.getPermitRequest() ) {
            log.write("Waiting receive a request.");
            this.moveToDestinationFloor(this.controller.goToFloor(this));
            log.write("Ready for attending request in " + this.currentFloor + " floor.");
            HashMap<Integer,Integer> people = this.getPeople();
            log.write("Ready for delivery the people.");
            deliveryPeople(people);
            people.clear();
            log.write("Finishing round at " + this.currentFloor + " floor.");
            this.controller.terminateRound(this);
        }

		log.write("Ending floor: " + this.currentFloor + ".");
		log.write("Turn off elevator.");
		log.close();
	}

    /**
    * Moves the elevator to it's destiny
    * @param destinationFloor
    */
    private void moveToDestinationFloor(int destinationFloor) {
        if( destinationFloor != this.currentFloor) {
            if( destinationFloor > this.currentFloor) {
                while( this.currentFloor < destinationFloor ) {
                    this.currentFloor = this.up();
                    log.write("Moving up to " + destinationFloor + ". Current floor " + this.currentFloor);
                }
            } else {
                while( this.currentFloor > destinationFloor ) {
                    this.currentFloor = this.down();
                    log.write("Moving down to " + destinationFloor + ". Current floor " + this.currentFloor);
                }
            }
        }
    }

    /**
    * This method get the people who made a request in the floor.
    * @return A HashMap with the destination floor as key and the number of people as value.
    */
    private HashMap<Integer,Integer> getPeople() {
        int counterPeople = 0;
        HashMap<Integer,Integer> people = new HashMap<Integer,Integer>();

        do {
            Request person = this.controller.getRequest(this.currentFloor);

            if( person == null ) {
                break;
            }

            counterPeople++;

            log.write("Getting request ID: " + person.getID() + ". Destination floor: " + person.getDestinationFloor() + ".");

            if( !people.containsKey(person.getDestinationFloor()) ) {
                people.put(person.getDestinationFloor(), 1);
            } else {
                people.put(person.getDestinationFloor(), people.get(person.getDestinationFloor()) + 1);
            }
        } while( counterPeople < this.maxPeople );

        log.write("People in this elevator at moment: " + counterPeople + ".");

        return people;
    }

    /**
    * Deliver the people in the elevator at the desired floor.
    * @param people A HashMap with the destination floor as key and the number of people as value.
    */
    private void deliveryPeople(HashMap<Integer,Integer> people) {
        ArrayList<Integer> up = new ArrayList<Integer>();
        ArrayList<Integer> down = new ArrayList<Integer>();

        for(Integer i: people.keySet()) {
            if( i > this.currentFloor )
                up.add(i);
            else
                down.add(i);
        }

        Collections.reverse(down);
        Collections.sort(up);

        for(Integer i: up) {
            this.moveToDestinationFloor(i);
            log.write("Arrived in the floor. Opening door to liberate " + people.get(this.currentFloor) + " people.");
            people.remove(this.currentFloor);
        }

        for(Integer i: down) {
            this.moveToDestinationFloor(i);
            log.write("Arrived in the floor. Opening door to liberate " + people.get(this.currentFloor) + " people.");
            people.remove(this.currentFloor);
        }
    }

	/**
    * Moves the elevator down one floor.
    * @return The actual floor.
    */
    @SuppressWarnings("finally")
	private int down() {
		try {
			Thread.sleep(Main.ELEVATOR_TIME_TO_UP_DOWN * 1000);

			if (this.currentFloor > 0) {
				this.currentFloor--;
			} else {
				throw new RuntimeException("[EXCEPTION] Impossible move down elevator.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return this.currentFloor;
		}
	}

	/**
    * Moves the elevator up one floor.
    * @return The actual floor.
    */
    @SuppressWarnings("finally")
	private int up() {
		try {
			Thread.sleep(Main.ELEVATOR_TIME_TO_UP_DOWN * 1000);

			if (this.currentFloor < controller.getNumberFloors()) {
				this.currentFloor++;
			} else {
				throw new RuntimeException("[EXCEPTION] Impossible move up elevator.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return this.currentFloor;
		}
	}

	/** Return the elevator current floor. */
    public int getCurrentFloor() {
		return this.currentFloor;
	}

    /**
    * Set the elevator state.
    * @param elevatorState The elevator state as defined in ElevatorState enum.
    */
    public void setElevatorState(ElevatorState elevatorState) {
        this.elevatorState = elevatorState;
    }

	/** Return the elevator current state. */
    public ElevatorState getElevatorState(){
		return this.elevatorState;
	}

}
