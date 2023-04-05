/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.aidenw.engg1420.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
/**
 * @author Lucy
 * @author Leonardo
 * @author Daniel
 * @author Vanessa
 * @author Aiden
 */
public class ListFiles extends ProcessingElement {
    @JsonProperty("Max")
    private int max;
    @Override
    protected void accept(Entry directory) {
        if(max<=0||!directory.isDirectory()){return;}
        ArrayList<CompletableFuture>cfs=new ArrayList<>();
        directory.dirContents(entry->{
            cfs.add(CompletableFuture.runAsync(()->next.accept(entry),Main.executor));
            return cfs.size()<max;
        });
        CompletableFuture.allOf(cfs.toArray(CompletableFuture[]::new)).join();
    }
}
