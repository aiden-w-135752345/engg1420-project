import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This takes in a list of directory entries and a maximum value, and returns a list of selected entries from each 
 *      directory in the input list. It first creates two directories with files for testing, then calls a method 
 *      that selects up to Max entries from each directory, and returns them as a list.
 * @author Lucy
 * @author Leonardo
 * @author Daniel
 * @author Vanessa
 * @author Aiden
 */
public class ListFiles extends ProcessingElement {
    /**
     * A method that takes in a list of directory entries and an integer Max, and returns a list of selected entries.
     *
     * @param entries The list of directory entries to process.
     * @param Max     The maximum number of entries to select from each directory.
     * @return A list of selected entries from the directories in the input list.
     */
    public static List<File> selectEntries(List<File> entries, int Max) {
        // Initialize an empty list to hold the selected entries.
        List<File> selectedEntries = new ArrayList<>(); 

        // Loop through each entry in the input list.
        for (File entry : entries) { 
            // Check if the entry is a directory.
            if (entry.isDirectory()) { 
                // Get an array of all the files in the directory.
                File[] files = entry.listFiles(); 

                int numFiles = files.length;
                // Determine the number of files to select from the directory.
                int numSelectedFiles = Math.min(numFiles, Max); 
                
                // Loop through the selected files in the directory.
                for (int i = 0; i < numSelectedFiles; i++) { 
                    // Add the selected file to the output list.
                    selectedEntries.add(files[i]); 
                }
            }
        }
        
        // Return the list of selected entries.
        return selectedEntries; 
    }
        
        // Create another directory object for testing.
        File directory2 = new File("directory2"); 
        // Make the directory if it doesn't exist.
        directory2.mkdirs(); 
        
        // Create 50 files in directory2.
        for (int i = 1; i <= 50; i++) { 
            File file = new File(directory2, "file" + i + ".txt");
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // Create a list of directory entries for testing.
        List<File> entries = new ArrayList<>(); 
        entries.add(directory1);
        entries.add(directory2);
        
        // Select up to 20 entries from each directory.
        List<File> selectedEntries = selectEntries(entries, 20); 
        
        // Print out the selected entries for verification.
        for (File entry : selectedEntries) { 
            System.out.println(entry.getName());
        }
    }
}
