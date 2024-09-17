import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

/**
 * Config abject for starting the
 * settings for running our maze solver
 */

public class ConFig {
    // store mode
    private char mode = 0; // null character
    // store the output mode
    private char outputMode = 'm';

    private  boolean checkpoint1 = false;
    private boolean checkpoint2 = false;

    // Getopt options

    private static LongOpt[] longOptions = {
            new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h'),
            new LongOpt("stack", LongOpt.NO_ARGUMENT, null,'s'),
            new LongOpt("queue", LongOpt.NO_ARGUMENT,null, 'q'),
            new LongOpt("output",LongOpt.REQUIRED_ARGUMENT, null, 'o'),
            new LongOpt("checkpoint1", LongOpt.NO_ARGUMENT, null, 'x'),
            new LongOpt("checkpoint2", LongOpt.NO_ARGUMENT, null, 'y')
    };
    /**
     * construct our config object using getopt
     * @param args string array of command line arguments
     */

    public ConFig(String[] args){
        // we will do all get opt parsing
        //make a getopt object
        Getopt g  = new Getopt("mazeSolver", args,"hsqo:xy", longOptions);
        g.setOpterr(true);

        int choice;

        // process each argument from the command line in turn
        while((choice = g.getopt()) != -1){
            // check which argument we are processing
            switch (choice){
                case'h':
                    printHelp();
                    break;
                case 's':
                case 'q':
                    // verify this was provided once
                    // if mode is not "NULL" character (int valur 0 ), we already set a mode --> error!
                    if (mode != 0){
                        System.err.println("Mode was already specified");
                        System.exit(1);
                    }
                    mode = (char) choice;
                    break;
                case 'o':
                        String requestedOutput = g.getOptarg();
                        if ( requestedOutput.equals("map") || requestedOutput.equals("list")){
                            outputMode = requestedOutput.charAt(0);
                        }else{
                            System.err.println(1);
                        }
                    break;
                case 'x':
                        checkpoint1 = true;
                    break;
                case 'y':
                        checkpoint2 = true;
                    break;

                default:
                    System.err.println("Unknown command line option -" + (char) choice);
                    System.exit(1);

            } // switch()
        }//while()
        // do some final error checking
        // check if -- stack or-- queue mode specified

        if (mode == 0){
            System.err.println("You must specify -- stack or-- queue mode");
            System.exit(1);
        }
    }

    public boolean isQueueMode(){
        return mode == 'q';
    }

    public boolean isMapOutputMode(){
        return outputMode == 'm';
    }

    public boolean isCheckpoint1(){
        return checkpoint1;
    }

    public boolean isCheckpoint2(){
        return checkpoint2;
    }

    private void printHelp() {
        System.out.println("This is a helpful help message"); // rewrite this
        System.exit(0);
    }
}
