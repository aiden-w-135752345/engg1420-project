/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.aidenw.engg1420.project;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class CountFilter extends Filter {
    @JsonProperty("Key")
    private String key;
    @JsonProperty("Min")
    private int min;
    @Override
    public boolean condition(Entry entry){
            int count = 0;
            /* read the entries line by line to search for the key */
            for (String line : entry.fileContents()) {
                /* if the line of text contains the key string */
                if(line.contains(key)){
                    // only add the entry to the list if the count = the Min number of keys
                    if(++count == min){
                        return true;
                    }
                }
            }
        return false;
    }    
}