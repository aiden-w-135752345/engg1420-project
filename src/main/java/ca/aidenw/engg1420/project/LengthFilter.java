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
public class LengthFilter extends Filter {
    @JsonProperty("Length")
    private int threshold;
    @JsonProperty("Operator")
    private String operator;

    @Override
    protected boolean condition(Entry entry) {
        if(!entry.isDirectory()){return false;}
        long length=entry.length();
        return switch(operator){
            case"LT" -> length<threshold;
            case"LTE" -> length<=threshold;
            case"EQ" -> length==threshold;
            case"GTE" -> length>=threshold;
            case"GT" -> length>threshold;
            case"NEQ" -> length!=threshold;
            default -> throw new Error("+++ error subclass");
        };
    }
}
