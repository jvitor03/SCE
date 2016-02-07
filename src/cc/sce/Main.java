package cc.sce;
import java.util.Scanner;

public class Main {

    public final static long RUNTIME_START = System.currentTimeMillis();
    public final static long ELEVATOR_TIME_TO_UP_DOWN = 1;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

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

//int nElevators, floors, max;
//Scanner scanner = new Scanner(System.in);

//floors = scanner.nextInt();
//nElevators = scanner.nextInt();
//max = scanner.nextInt();

//Building building = new Building(floors,nElevators, max);
//building.listen();
