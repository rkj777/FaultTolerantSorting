//The thread that runs the primary variant
public class PrimaryThread extends Thread {
    double primaryFailureProb;
    int[] inputArray;
	boolean[] fail;
    public PrimaryThread(double primaryFailureProb, int[] inputArray, boolean[] fail){
        this.primaryFailureProb = primaryFailureProb;
        this.inputArray = inputArray;
		this.fail = fail;
    }

    @Override
    public void run() {
        super.run();
        try {
            HeapSort primary = new HeapSort(inputArray, primaryFailureProb);
            inputArray = primary.heapSort();
       
        }catch(ThreadDeath threadDeath){
            System.out.println("Primary has timed out in thread. Starting backup variant.");
            throw new ThreadDeath();
        }catch(ThreadFailException t){
			System.out.println("Primary has failed in thread. Starting backup variant.");
			fail[0] = true;
		}
    }
}
