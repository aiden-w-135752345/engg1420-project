/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.aidenw.engg1420.project;

/**
 * @author Lucy
 * @author Leonardo
 * @author Daniel
 * @author Vanessa
 * @author Aiden
 */
public class NameFilter extends ProcessingElement {
    public static Entry[] content(Entry[] entries,String key){
        /* empty list to be appended to with the entries containing the key */
        List<Entry> list = new ArrayList<>();
        for (Entry entry : entries){
            if(entry.name().equals(key)){// if the name of the entry is equal to the key then add it to the list
                list.add(entry); // add the entry to the list of entries
            }
        }
        
        /* reformat the list back into an array to standardize output */
        Entry[] output = list.toArray(new Entry[0]);
        return output;
    }   
}  
    

