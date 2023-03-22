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
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Lucy
 * @author Leonardo
 * @author Daniel
 * @author Vanessa
 * @author Aiden
 */
public class LocalEntry extends Entry {
    private String name;
    private final String path;
    public LocalEntry(String name,String path){this.name=name;this.path=path;}
    @Override
    public String name(){return name;}
    @Override
    public String path(){return path;}
    private Boolean isDirectory;
    private Long size;
    private void getMetadata(){
        try {
            BasicFileAttributes metadata=Files.readAttributes(Path.of(path+name),BasicFileAttributes.class);
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
                fileContents=Files.readAllLines(Path.of(path+name)).toArray(String[]::new);
            } catch (IOException ex) {throw new UncheckedIOException(ex);}
        }
        return fileContents;
    }
    @Override
    public void dirContents(Function<Entry,Boolean> consumer){
        try(Stream<Path> files=Files.list(Path.of(path+name))){
            files.allMatch(x->{
                int count = x.getNameCount();
                return consumer.apply(new LocalEntry(x.getName(count-1).toString(),x.getParent().toString()));
            });
        } catch (IOException ex) {throw new UncheckedIOException(ex);}
    }

    @Override
    public void rename(String newname) {
        try {
            Files.move(Path.of(path+name),Path.of(newname,path));
            name=newname;
        } catch (IOException ex) {throw new UncheckedIOException(ex);}        
    }

    @Override
    public Entry makeFile(String name, String[] contents) {
        try (BufferedWriter writer=Files.newBufferedWriter(Path.of(path+name))){
            LocalEntry entry=new LocalEntry(name,path);
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
