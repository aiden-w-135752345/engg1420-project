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

public class Rename extends ProcessingElement {
  @JsonProperty("Suffix")
  private String suffix;
  @Override
  protected void accept (Entry entry){
    entry.rename(entry.name()+suffix);
    next.accept(entry);
}
}