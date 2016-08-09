import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Timer;

public class Driver {
    public static void main(String[] args) {
		//Values to store array information from input        
		String line;
        int[] inputArray;
        int[] checkpointArray;
        int numIntegers;
		String inputFileName;
		String outputFileName;
		double primaryFailureProb;
		double backupFailureProb;
		long time;

		//Reading in command-line arguments        
		if(args.length != 5) {
            System.out.println("Please provide five command line values!");
			return;	
        }
	
		//Converting arguments into correct types
		try{
        	inputFileName = args[0];
        	outputFileName = args[1];
        	primaryFailureProb = Double.parseDouble(args[2]);
        	backupFailureProb = Double.parseDouble(args[3]);
        	time = Long.parseLong(args[4]);
		}catch(NumberFormatException n){
            System.out.println("Error with values in the file");
            return;
        }
		//Reading the input file. The first line of the file will be the lenght of the array
        try{
            FileReader fileReader = new FileReader(inputFileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String numberOfInts = bufferedReader.readLine();
            numIntegers = Integer.parseInt(numberOfInts);
			
			//Creating two arrays. One that can be rolled back on in case of failure            
			inputArray = new int[numIntegers];
            checkpointArray = new int[numIntegers];

            int i = 0;
            while((line = bufferedReader.readLine()) != null){
                inputArray[i] = Integer.parseInt(line);
                i++;
            }
            System.arraycopy(inputArray, 0, checkpointArray, 0, inputArray.length);
			
			bufferedReader.close();
		}catch(IOException e){
            System.out.println("Error reading the file");
            return;
        }catch(NumberFormatException n){
            System.out.println("Error with values in the file");
            return;
        }

		//Creating the primary variant thread and it assoicated watchdog timer thread
		boolean[] fail = {false};
		PrimaryThread primaryThread = new PrimaryThread(primaryFailureProb, inputArray, fail);
       	WatchDogThread primaryTimerThread = new WatchDogThread(primaryThread,time);
		//Timer timer = new Timer();
        //Watchdog watchdog = new Watchdog(primaryThread);
        //timer.schedule(watchdog, time);
		
		primaryTimerThread.start();
        primaryThread.start();
		
		//Joining the thread and writing the output to a file
        try{
            primaryThread.join();
			primaryTimerThread.stopTimer();
			if (!fail[0]){

				FileWriter fileWriter = new FileWriter(outputFileName);
            	BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				for(int i = 0; i < numIntegers; i++ ){
					bufferedWriter.write(Integer.toString(inputArray[i]));
            	    bufferedWriter.newLine();				
				}
			
				bufferedWriter.close();
				System.out.println("Primary varient has succeded.");
				return;
			}
        }catch(InterruptedException e){
			System.out.println("Error in primary variant: Interuption. Attempting second variant.");
			
        }catch(IOException e){
            System.out.println("Error reading the file");
            return;
        }
		System.out.println("Error in primary variant: Heapsort fail. Attempting second variant.");
		
		//Running secondary variant in the event of a failure
		boolean[] fail2 = {false};
        SecondaryThread secondaryThread = new SecondaryThread(backupFailureProb, checkpointArray, fail2);
        WatchDogThread secondaryTimerThread = new WatchDogThread(secondaryThread,time);
		
		secondaryTimerThread.start();
        secondaryThread.start();

        try{
            secondaryThread.join();
			secondaryTimerThread.stopTimer();
				if(!fail2[0]){
            		FileWriter fileWriter = new FileWriter(outputFileName);
           			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
           			for(int i = 0; i < numIntegers; i++ ){
            		    bufferedWriter.write(Integer.toString(checkpointArray[i]));
            		    bufferedWriter.newLine();
            		}
            		bufferedWriter.close();
					System.out.println("Secondary varient has succeded.");
            		return;
				}
        }catch(InterruptedException e){
            System.out.println("Error in backup variant. Aborting operation.");
            return;
        }catch(IOException e) {
            System.out.println("Error reading the file");
            return;
        }catch(ThreadDeath td){
			System.out.println("Error in primary variant. Attempting second variant.");
			return;
        }
		
		System.out.println("Error in backup variant. Aborting operation.");
        return;
		
        

    }
}

