/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.aidenw.engg1420.project;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.Predicate;
import java.util.stream.Stream;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Lucy
 * @author Leonardo
 * @author Daniel
 * @author Vanessa
 * @author Aiden
 */
public class LocalEntry extends Entry {
    private final String path;
    private String name;
    private final String extension;
    private Path fullPath;
    @JsonCreator
    public LocalEntry(@JsonProperty("path")String path){
        fullPath=Path.of(path);
        int idx=path.lastIndexOf('/');
        if(idx>=0){this.path=path.substring(0,idx+1);name=path.substring(idx+1);}
        else{this.path="";name=path;}
        idx=name.lastIndexOf('.');
        if(idx>=0){extension=name.substring(idx);name=name.substring(0,idx);}
        else{extension="";}
    }
    public LocalEntry(String path,String name,String extension){
        this.path=path;this.name=name;this.extension=extension;
        fullPath=Path.of(path+name+extension);
    }
    @Override
    public String path(){return path;}
    @Override
    public String name(){return name;}
    @Override
    public String extension(){return extension;}
    private Boolean isDirectory;
    private Long length;
    private void getMetadata(){
        try {
            BasicFileAttributes metadata=Files.readAttributes(fullPath,BasicFileAttributes.class);
            isDirectory=metadata.isDirectory();
            length=metadata.size();
        } catch (IOException ex) {throw new UncheckedIOException(ex);}
    }
    @Override
    public boolean isDirectory(){
        if(isDirectory==null)getMetadata();
        return isDirectory;
    }
    @Override
    public long length(){
        if(length==null)getMetadata();
        return length;
    }
    private String[] fileContents;
    @Override
    public String[] fileContents(){
        if(fileContents==null){
            try {
                fileContents=Files.readAllLines(fullPath).toArray(String[]::new);
            } catch (IOException ex) {throw new UncheckedIOException(ex);}
        }
        return fileContents;
    }
    @Override
    public void dirContents(Predicate<Entry> callback){
        try(Stream<Path> files=Files.list(fullPath)){
            for(Path x: (Iterable<Path>)(files::iterator)){
                if(!callback.test(new LocalEntry(x.toString()))){break;}
            }
        } catch (IOException ex) {throw new UncheckedIOException(ex);}
    }
    private static boolean warnWriteFail=true;
    @Override
    public void rename(String newname) {
        name=newname;
        try {
            Path target=Path.of(path+newname+extension);
            Files.move(fullPath,target);
            fullPath=target;
        } catch (IOException ex) {if(warnWriteFail){
            System.out.println("Warning: some local entries failed to write.");warnWriteFail=false;
        }}
    }

    @Override
    public Entry makeFile(String name, String[] contents) {
        LocalEntry entry=new LocalEntry(path,name,extension);
        entry.isDirectory = false;
        entry.length = 0L;
        entry.fileContents = contents;
        for (String line : contents) {entry.length += line.length() + 1;}
        
        try (BufferedWriter writer=Files.newBufferedWriter(Path.of(path+name+extension))){
            for(String line:contents){writer.write(line);writer.write('\n');}
        } catch (IOException ex) {if(warnWriteFail){
            System.out.println("Warning: some local entries failed to write.");warnWriteFail=false;
        }}
        return entry;
    }
}
