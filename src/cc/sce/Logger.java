package cc.sce;

import java.io.File;
import java.io.PrintWriter;

/**
* This class creates the output logs of the elevators.
* @author Claudio Gonçalves
*/
public class Logger {

    private static String directoryPath = "./output";
    private int threadID;
    private PrintWriter writer;

    /**
    * Constructor.
    * @param threadID (required) A thread id.
    */
    public Logger(int threadID) {
        try {
            this.threadID = threadID;
            this.writer = new PrintWriter( Logger.directoryPath + "/Elevator-" + this.threadID + ".txt", "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /** Create the output directory, if not exists or delete existing outputs in the directory. */
    public static void prepareDirectoryOutput() {
        File outputDirectory = new File( Logger.directoryPath );

        if( !outputDirectory.exists() ) {
            if( ! outputDirectory.mkdir() ) {
               System.exit(-1);
            }
        } else {
            for(File file: outputDirectory.listFiles()) {
                file.delete();
            }
        }
    }

    /**
    * Write a output log.
    * @param message The message to be wrote.
    */
    public void write(String message) {
        long timeElapsed = System.currentTimeMillis() - Main.RUNTIME_START;

        writer.println( timeElapsed + "ms - [E" + this.threadID + "] " + message);
        System.out.println(timeElapsed + "ms - [E" + this.threadID + "] " + message);
    }

    /** closes the writer */
    public void close() {
        this.writer.close();
    }

}
