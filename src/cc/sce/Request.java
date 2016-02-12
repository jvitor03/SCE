package cc.sce;

public class Request {

    private static int REQUEST_IDENTIFIER=0;
    private int id;
    private int destinationFloor;

    public Request(int destinationFloor) {
        this.id = Request.REQUEST_IDENTIFIER++;
        this.destinationFloor = destinationFloor; 
    }

    public int getID() {
        return this.id;
    }

    public int getDestionationFloor() {
        return this.destinationFloor;
    }

}
