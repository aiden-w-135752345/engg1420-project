/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.aidenw.engg1420.project;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.File;
import java.io.IOException;

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
  /*Seperate main method for entries donloaded from the server.
  *Changes will be made on the local system
  */
  public static void main(String[] argv) throws IOException {
        //Path which the entries are saved on the local system
        File folder = new File("\\Users\\16474\\OneDrive - University of Guelph\\Documents\\ENGG 1420\\Entries\\PrimeNumbers");
        File[] listOfFiles = folder.listFiles();
        //Desired suffix added to the end of the entries name
        String Suffix = "_edited";
        
  //Loop that runs to ensure that all files in the entry are edited
  for (int i = 0; i < listOfFiles.length; i++) {
        if (listOfFiles[i].isFile()) {
            String oldName = listOfFiles[i].getName();
            String newName = oldName.replaceFirst("\\.([^\\.]+)$", Suffix + ".$1");
            File oldFile = new File(folder, oldName);
            File newFile = new File(folder, newName);
            oldFile.renameTo(newFile);
        }
    }
        System.out.println("conversion is done");
    }
}