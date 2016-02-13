package cc.sce;

public class Elevator extends Thread {

	private int id;
	private int currentFloor;
	private int maxPeople;
	private ElevatorController controller;
	private Logger log;
	private ElevatorState elevatorState;
	// private ArrayList<Integer> people;

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
		log.write("Starting floor: " + this.currentFloor);

        // ENQUANTO EXISTIR REQUISIÇÕES E A MAIN NÃO BLOQUEÁ-LAS.
		int aux = this.controller.goToFloor(this);

        if(aux != this.currentFloor) {
            if(aux > this.currentFloor) {
                while(this.currentFloor<aux) {
                    this.currentFloor = this.up();
                    log.write("Moving up to " + aux + ". Current floor " + this.currentFloor);
                }
            } else {
                while(this.currentFloor>aux) {
                    this.currentFloor = this.down();
                    log.write("Moving down to " + aux + ". Current floot" + this.currentFloor);
                }
            }
        }

        log.write("Attending request in " + this.currentFloor + " floor.");

        // PEGAR AS PESSOAS DESSE ANDAR.

        // LEVAR PARA SEUS RESPECTIVOS ANDARES

        // VOLTAR PARA O LOOP

		log.write("Ending floor: " + this.currentFloor);
		log.write("Turn off elevator.");
		log.close();
	}

	@SuppressWarnings("finally")
	private int down() {
		try {
			//Thread.sleep(Main.ELEVATOR_TIME_TO_UP_DOWN * 1000);

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

	public ElevatorState getElevatorState(){
		return this.elevatorState;
	}

}
