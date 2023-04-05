import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Note: this code assumes that the input files are located in the same directory as the Java program.

/**
 * This Java code takes a list of files, splits each file into parts based 
 *      on a specified number of lines, and creates new files for each part. 
 *      The output is a list of the generated files.
 * @author Lucy
 */
public class Split {
    
    /*
    * Description: The splitFiles method takes a list of input files and 
    * an integer lines as parameters. It reads each input file line by 
    * line, and for each lines number of lines, it creates a new output 
    * file. The output files are stored in a list.
    */
    public static List<File> splitFiles(List<File> inputFiles, int lines) throws IOException {
        // create an empty list to store output files
        List<File> outputFiles = new ArrayList<File>();
        // iterate over each input file in the list
        for (File inputFile : inputFiles) {
            // ignore directories
            if (inputFile.isDirectory()) {
                continue;
            }
            // create a buffered reader to read the input file
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            // initialize variables to keep track of the output files and part numbers
            int partNumber = 1;
            String line;
            List<String> linesList = new ArrayList<String>();
            // read each line of the input file
            while ((line = reader.readLine()) != null) {
                // add the line to the list of lines for the current part
                linesList.add(line);
                // if the list of lines has reached the specified number, create a new output file
                if (linesList.size() == lines) {
                    // create the output file with the appropriate name
                    String outputFileName = inputFile.getName() + ".part" + partNumber + ".txt";
                    File outputFile = new File(outputFileName);
                    // create a file writer to write to the output file
                    FileWriter writer = new FileWriter(outputFile);
                    // write each line to the output file
                    for (String outputLine : linesList) {
                        writer.write(outputLine + System.getProperty("line.separator"));
                    }
                    // close the writer and add the output file to the list
                    writer.close();
                    outputFiles.add(outputFile);
                    // increment the part number and clear the list of lines
                    partNumber++;
                    linesList.clear();
                }
            }
            // if there are remaining lines in the list, create a new output file
            if (!linesList.isEmpty()) {
                // create the output file with the appropriate name
                String outputFileName = inputFile.getName() + ".part" + partNumber + ".txt";
                File outputFile = new File(outputFileName);
                // create a file writer to write to the output file
                FileWriter writer = new FileWriter(outputFile);
                // write each line to the output file
                for (String outputLine : linesList) {
                    writer.write(outputLine + System.getProperty("line.separator"));
                }
                // close the writer and add the output file to the list
                writer.close();
                outputFiles.add(outputFile);
            }
            // close the reader for the current input file
            reader.close();
        }
        // return the list of output files
        return outputFiles;
    }

    /*
    * The main method is an example usage of the splitFiles method, where two input files 
    * "File1.txt" and "File2.txt" are created and the value of lines is set to 10.
    * The output file names are printed to the console for verification purposes.
    */
    public static void main(String[] args) throws IOException {
        // create a list of input files
        List<File> inputFiles = new ArrayList<File>();
        inputFiles.add(new File("File1.txt"));
        inputFiles.add(new File("File2.txt"));
        // set the number of lines for each part
        int lines = 10;
        // call the splitFiles method with the input files and lines
        List<File> outputFiles = splitFiles(inputFiles, lines);
        // print the names of the output files
        for (File outputFile : outputFiles) {
            System.out.println(outputFile.getName());
        }
    }

}
