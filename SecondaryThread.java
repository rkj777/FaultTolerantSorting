/**
 * Created by rajanjassal on 16-02-24.
 */
public class SecondaryThread extends Thread {
    double secondaryFailureProb;
    int[] inputArray;
	boolean[] fail;

    public SecondaryThread(double secondaryFailureProb, int[] inputArray, boolean[] fail){
        this.secondaryFailureProb = secondaryFailureProb;
        this.inputArray = inputArray;
		this.fail = fail;
    }

    @Override
    public void run() {
        super.run();
        try {
            InsertationSort insSort = new InsertationSort();
            System.loadLibrary("insertionsort");
            inputArray = insSort.insearch(inputArray ,secondaryFailureProb);
            if(inputArray == null){
               fail[0] = true;
            }
        }catch(ThreadDeath threadDeath){
            System.out.println("Secondary has failed.Aborting operation.");
            throw new ThreadDeath();
        }
    }
}
