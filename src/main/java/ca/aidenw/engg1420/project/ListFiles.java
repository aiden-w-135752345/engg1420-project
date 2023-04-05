package ca.aidenw.engg1420.project;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This takes in a list of directory entries and a maximum value, 
 *      and returns a list of selected entries from each directory.
 * @author Lucy
 * @author Leonardo
 * @author Daniel
 * @author Vanessa
 * @author Aiden
 */
public class ListFiles extends ProcessingElement {
    /**
     * This takes in a list of directory entries and an integer Max,
     *      and returns a list of selected entries.
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
}