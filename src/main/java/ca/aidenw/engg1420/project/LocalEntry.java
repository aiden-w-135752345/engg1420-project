/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.aidenw.engg1420.project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

/**
 * @author Lucy
 * @author Leonardo
 * @author Daniel
 * @author Vanessa
 * @author Aiden
 */
public class LocalEntry extends Entry {
    private final String name;
    private final String path;
    public LocalEntry(String name,String path){this.name=name;this.path=path;}
    @Override
    public CompletionStage<String> name(){return CompletableFuture.completedFuture(name);}
    @Override
    public CompletionStage<String> path(){return CompletableFuture.completedFuture(path);}
    CompletableFuture<BasicFileAttributes>metadata;
    private void getMetadata(){
        if(metadata==null){metadata=CompletableFuture.supplyAsync(()->{
            try {
                return Files.readAttributes(Path.of(path+name),BasicFileAttributes.class);
            } catch (IOException ex) {
                metadata.completeExceptionally(ex);return (BasicFileAttributes)null;
            }
        });}
    }
    @Override
    public CompletionStage<Boolean>isDirectory(){
        getMetadata();
        return metadata.thenApply(x->x.isDirectory());
    }
    @Override
    public CompletionStage<Long> length(){
        getMetadata();
        return metadata.thenApply(x->x.size());
    }
    private CompletableFuture<String[]> fileContents;
    @Override
    public CompletionStage<String[]> fileContents(){
        if(fileContents==null){fileContents=CompletableFuture.supplyAsync(()->{
            try {
                return Files.readAllLines(Path.of(path+name)).toArray(String[]::new);
            } catch (IOException ex) {fileContents.completeExceptionally(ex);return (String[])null;}
        });}
        return fileContents;
    }
    private CompletableFuture<Stream<Entry>>dirContents;
    @Override
    public CompletionStage<Stream<Entry>> dirContents(){
        if(dirContents==null){
            dirContents=CompletableFuture.supplyAsync(()->{
                try {
                    return Files.list(Path.of(path+name)).map(x->{
                        int count = x.getNameCount();
                        return new LocalEntry(x.getName(count-1).toString(),x.getParent().toString());
                    });
                } catch (IOException ex) {
                    dirContents.completeExceptionally(ex);return (Stream<Entry>)null;
                }
            });
        }
        return dirContents;
    }
}
