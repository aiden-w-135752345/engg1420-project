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
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.nio.file.DirectoryStream;

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
    public LocalEntry(@JsonProperty("path")String path){this(Path.of(path));}
    public LocalEntry(Path path){
        fullPath=path.normalize();
        this.path=fullPath.getParent().toString();name=fullPath.getFileName().toString();
        int idx=name.lastIndexOf('.');
        if(idx>0){extension=name.substring(idx);name=name.substring(0,idx);}
        else{extension="";}
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
        try(DirectoryStream<Path> files=Files.newDirectoryStream(fullPath)){
            for(Path x: files){
                if(!callback.test(new LocalEntry(x))){break;}
            }
        } catch (IOException ex) {throw new UncheckedIOException(ex);}
    }
    private static boolean warnWriteFail=true;
    @Override
    public void rename(String newname) {
        name=newname;
        Path target=fullPath.resolveSibling(newname+extension);
        try {
            Files.move(fullPath,target);
            fullPath=target;
        } catch (IOException ex) {if(warnWriteFail){
            System.out.println("Warning: some local entries failed to write.");warnWriteFail=false;
        }}
    }

    @Override
    public Entry makeFile(String name, String[] contents) {
        LocalEntry entry=new LocalEntry(fullPath.resolveSibling(name+extension));
        entry.isDirectory = false;
        entry.length = 0L;
        entry.fileContents = contents;
        for (String line : contents) {entry.length += line.length() + 1;}
        
        try (BufferedWriter writer=Files.newBufferedWriter(entry.fullPath)){
            for(String line:contents){writer.write(line);writer.write('\n');}
        } catch (IOException ex) {if(warnWriteFail){
            System.out.println("Warning: some local entries failed to write.");warnWriteFail=false;
        }}
        return entry;
    }
}
