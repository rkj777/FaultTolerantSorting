
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

//Class that will generate the input file
public class DataGenerator {
    public static void main(String[] args) {

        if(args.length != 2) {
            System.out.println("Please provide two command line values!");
        }

        //TODO santize inputs
        String outputFileName = args[0];
        int numIntegers = Integer.parseInt((args[1]));

        try{
            FileWriter fileWriter = new FileWriter(outputFileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            Random generator = new Random();

            bufferedWriter.write(Integer.toString(numIntegers));
            bufferedWriter.newLine();
            for(int i = 0; i < numIntegers; i++ ){
                bufferedWriter.write(Integer.toString(generator.nextInt(100)));
                bufferedWriter.newLine();
            }

            bufferedWriter.close();
        }catch(IOException e){
            System.out.println("Error writing to the file");
        }





    }
}
