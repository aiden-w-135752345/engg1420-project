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
public class NameFilter extends Filter {
    @JsonProperty("Key")
    private String key;
    @Override
    protected boolean condition(Entry entry){
        return entry.name().contains(key);
    }
}  
    

