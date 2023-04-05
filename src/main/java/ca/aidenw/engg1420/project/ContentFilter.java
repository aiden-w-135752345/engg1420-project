/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.aidenw.engg1420.project;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



/**
 * @author Lucy
 * @author Leonardo
 * @author Daniel
 * @author Vanessa
 * @author Aiden
 */
public class ContentFilter extends ProcessingElement {
    public static Entry[] content(Entry[] entries,String key){
        /* empty list to be appended to with the entries containing the key */
        List<Entry> list = new ArrayList<>();
        for (Entry entry : entries){
            /* read the entries line by line to search for the key */
            List<String> allLines = Arrays.asList(entry.fileContents());

            for (String line : allLines) {
                /* if the line of text contains the key string */
                if(line.contains(key)){
                    list.add(entry); // add the entry to the list of entries
                    break; // end iteration to prevent duplicate entries
                }
            }
        }
        /* reformat the list back into an array to standardize output */
        Entry[] output = list.toArray(new Entry[0]);
        return output;
    }

    @Override
    protected void accept(Entry entry) {
        throw new UnsupportedOperationException("Unimplemented method 'accept'");
    }     
    
}
