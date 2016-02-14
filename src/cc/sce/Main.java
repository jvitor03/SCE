package cc.sce;
import java.util.Scanner;

/**
*
* <p>Elevator Control System.
* <p>Created for Concurrent Computing class(MAB117) at Federal University of Rio de Janeiro (UFRJ).
* @Author Claudio Gonçalves
* @Author João Vitor Rebouças
* @version 1.0
*
*/
public class Main {

    public final static long RUNTIME_START = System.currentTimeMillis();
    public final static long ELEVATOR_TIME_TO_UP_DOWN = 1;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Logger.prepareDirectoryOutput();

        int numberFloors = scanner.nextInt();
        int numberElevators = scanner.nextInt();
        int maxPeoplePerElevator = scanner.nextInt();

        Building building = new Building(numberFloors, numberElevators);

        for(int i=0; i<numberElevators; i++) {
            building.createElevator(i, maxPeoplePerElevator, scanner.nextInt());
        }

        for(int i=0; i<numberFloors; i++) {
            int peopleInTheFloor = scanner.nextInt();

            for(int j=0; j<peopleInTheFloor; j++) {
                building.addRequest(i, scanner.nextInt());
            }
        }

        building.startElevators();

        building.getElevatorController().finishRequests();

    }

}
