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
    @JsonCreator
    public LocalEntry(@JsonProperty("path")String path){
        int idx=path.lastIndexOf('/');
        if(idx>=0){this.path=path.substring(0,idx+1);name=path.substring(idx+1);}
        else{this.path="";name=path;}
        idx=name.lastIndexOf('.');
        if(idx>=0){extension=name.substring(idx);name=name.substring(0,idx);}
        else{extension="";}
    }
    public LocalEntry(String path,String name,String extension){this.path=path;this.name=name;this.extension=extension;}
    @Override
    public String path(){return path;}
    @Override
    public String name(){return name;}
    @Override
    public String extension(){return extension;}
    private Boolean isDirectory;
    private Long size;
    private void getMetadata(){
        try {
            BasicFileAttributes metadata=Files.readAttributes(Path.of(path+name+extension),BasicFileAttributes.class);
            isDirectory=metadata.isDirectory();
            size=metadata.size();
        } catch (IOException ex) {throw new UncheckedIOException(ex);}
    }
    @Override
    public boolean isDirectory(){
        if(isDirectory==null)getMetadata();
        return isDirectory;
    }
    @Override
    public long length(){
        if(size==null)getMetadata();
        return size;
    }
    private String[] fileContents;
    @Override
    public String[] fileContents(){
        if(fileContents==null){
            try {
                fileContents=Files.readAllLines(Path.of(path+name+extension)).toArray(String[]::new);
            } catch (IOException ex) {throw new UncheckedIOException(ex);}
        }
        return fileContents;
    }
    @Override
    public void dirContents(Predicate<Entry> callback){
        try(Stream<Path> files=Files.list(Path.of(path+name+extension))){
            for(Path x: (Iterable<Path>)(files::iterator)){
                if(!callback.test(new LocalEntry(x.toString()))){break;}
            }
        } catch (IOException ex) {throw new UncheckedIOException(ex);}
    }

    @Override
    public void rename(String newname) {
        try {
            Files.move(Path.of(path+name+extension),Path.of(path+newname+extension));
            name=newname;
        } catch (IOException ex) {throw new UncheckedIOException(ex);}        
    }

    @Override
    public Entry makeFile(String name, String[] contents) {
        try (BufferedWriter writer=Files.newBufferedWriter(Path.of(path+name+extension))){
            LocalEntry entry=new LocalEntry(path,name,extension);
            entry.isDirectory=false;
            entry.size=0L;
            entry.fileContents=contents;
            for(String line:contents){
                writer.write(line);
                writer.write('\n');
                entry.size+=line.length()+1;
            }
            return entry;
        } catch (IOException ex) {throw new UncheckedIOException(ex);}
    }
}
