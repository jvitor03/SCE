package cc.sce;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

public class Elevator extends Thread {

	private int id;
	private int currentFloor;
	private int maxPeople;
	private ElevatorController controller;
	private Logger log;
	private ElevatorState elevatorState;

	public Elevator(int id, int startingFloor, int maxPeople, ElevatorController controller) {
		this.id = id;
		this.currentFloor = startingFloor;
		this.maxPeople = maxPeople;
		this.controller = controller;
		this.log = new Logger(this.id);
		this.elevatorState = ElevatorState.WAITING;
	}

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

    private HashMap<Integer,Integer> getPeople() {
        int counterPeople = 0;
        HashMap<Integer,Integer> people = new HashMap<Integer,Integer>();

        do {
            Request person = this.controller.getRequest(this.currentFloor);

            if( person == null ) {
                break;
            }

            counterPeople++;

            log.write("Getting request ID: " + person.getID() + ". Destination floor: " + person.getDestionationFloor() + ".");

            if( !people.containsKey(person.getDestionationFloor()) ) {
                people.put(person.getDestionationFloor(), 1);
            } else {
                people.put(person.getDestionationFloor(), people.get(person.getDestionationFloor()) + 1);
            }
        } while( counterPeople < this.maxPeople );

        log.write("People in this elevator at moment: " + counterPeople + ".");

        return people;
    }

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

	@SuppressWarnings("finally")
	private int up() {
		try {
			//Thread.sleep(Main.ELEVATOR_TIME_TO_UP_DOWN * 1000);

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

	public int getCurrentFloor() {
		return this.currentFloor;
	}

    public void setElevatorState(ElevatorState elevatorState) {
        this.elevatorState = elevatorState;
    }

	public ElevatorState getElevatorState(){
		return this.elevatorState;
	}

}
