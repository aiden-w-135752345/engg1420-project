/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.aidenw.engg1420.project;
import com.fasterxml.jackson.annotation.JsonProperty;



/**
 * @author Lucy
 * @author Leonardo
 * @author Daniel
 * @author Vanessa
 * @author Aiden
 */
public class ContentFilter extends Filter {
    @JsonProperty("Key")
    private String key;
    @Override
    protected boolean condition(Entry entry){
            /* read the entries line by line to search for the key */
            for (String line : entry.fileContents()) {
                /* if the line of text contains the key string */
                if(line.contains(key)){
                    return true;
                }
            }
            return false;
    }
}
