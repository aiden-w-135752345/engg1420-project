/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.aidenw.engg1420.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
/**
 * @author Lucy
 * @author Leonardo
 * @author Daniel
 * @author Vanessa
 * @author Aiden
 */
public class Split extends ProcessingElement {
    @JsonProperty("Lines")
    private int lines;
    @Override
    protected void accept(Entry file) {
        if(file.isDirectory()){return;}
        ArrayList<CompletableFuture>cfs=new ArrayList<>();
        String name=file.name();
        String[] contents=file.fileContents();
        for(int i=0,j=0;j<contents.length;i++,j+=lines){
            int ii=i,jj=j;
            cfs.add(CompletableFuture.runAsync(()->next.accept(file.makeFile(
                    name+".part"+ii,Arrays.copyOfRange(contents, jj, (int)Math.min(jj+lines,contents.length))
            )),Main.executor));
        }
        CompletableFuture.allOf(cfs.toArray(CompletableFuture[]::new)).join();
    }
}
