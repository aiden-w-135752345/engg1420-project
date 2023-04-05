/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.aidenw.engg1420.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

/**
 * Example ProcessingElement that creates a tree listing of a directory.
 * @author aiden
 */
public class FileTree extends ProcessingElement {
    @JsonProperty("Suffix") // tells Jackson that the property "Suffix" in the scenario description ...
    private String suffix; // ... should be stored in the suffix variable
    
    @Override // the accept method is the entrypoint. Whenever an Entry needs to be processed,
    protected void accept(Entry entry) { // this method is called with the Entry to be processed.
        ArrayList<String> contents=new ArrayList<>();
        recurse(entry,contents);
        
        // entry.makeFile creates a file in the same folder as entry, given a name and array of lines
        Entry fileTree=entry.makeFile(entry.name()+suffix, contents.toArray(String[]::new));
        
        // send fileTree to the next ProcessingElement in the chain.         
        next.accept(fileTree);
        // next.accept can be called multiple times, if multiple entries
        // need to be sent onward.
    }
    void recurse(Entry entry,ArrayList<String> fileTree){
        if(entry.isDirectory()){
            // entry.dirContents runs the "lambda" multiple times, once for
            // each entry in the directory. Stops early if false is returned.
            entry.dirContents(x->{recurse(x,fileTree);return true;});
        }else{
            fileTree.add("%s%s%s: %d bytes".formatted(
                    // entries have a path, name, and file extension. 
                    // the path ends with a / and the extension starts with a .
                    entry.path(),entry.name(),entry.extension(),
                    entry.length()
            ));
        }
    }
}
