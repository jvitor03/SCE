package cc.sce;

/**
* Request model
* @author Claudio Gonçalves
*/
public class Request {

    private static int REQUEST_IDENTIFIER=0;
    private int id;
    private int destinationFloor;

    /**
    * Constructor.
    * @param destinationFloor The requested floor.
    */
    public Request(int destinationFloor) {
        this.id = Request.REQUEST_IDENTIFIER++;
        this.destinationFloor = destinationFloor; 
    }

    /** Return the request's id */
    public int getID() {
        return this.id;
    }

    /** Return the destination floor */
    public int getDestinationFloor() {
        return this.destinationFloor;
    }

}
