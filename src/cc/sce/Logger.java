package cc.sce;

import java.io.File;
import java.io.PrintWriter;

public class Logger {

    private static String directoryPath = "./output";
    private int threadID;
    private PrintWriter writer;

    public Logger(int threadID) {
        try {
            this.threadID = threadID;
            this.writer = new PrintWriter( Logger.directoryPath + "/Elevator-" + this.threadID + ".txt", "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

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

    public void write(String message) {
        long timeElapsed = System.currentTimeMillis() - Main.RUNTIME_START;

        writer.println( timeElapsed + "ms - [E" + this.threadID + "] " + message);
        System.out.println(timeElapsed + "ms - [E" + this.threadID + "] " + message);
    }

    public void close() {
        this.writer.close();
    }

}
