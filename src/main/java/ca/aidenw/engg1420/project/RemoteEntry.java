/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.aidenw.engg1420.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.laserfiche.repository.api.clients.EntriesClient;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Stream;
/**
 * @author Lucy
 * @author Leonardo
 * @author Daniel
 * @author Vanessa
 * @author Aiden
 */
public class RemoteEntry extends Entry {
    private static EntriesClient client;
    private final String repositoryId;
    private final int entryId;
    @JsonCreator
    public RemoteEntry(@JsonProperty("repositoryId")String repo,@JsonProperty("entryId")int entry){repositoryId=repo;entryId=entry;}
    public static void setClient(EntriesClient c){client=c;}
    private String path;
    private String name;
    private String extension;
    private Boolean isDirectory;
    
    private void getMetadata(){
        com.laserfiche.repository.api.clients.impl.model.Entry metadata=client.getEntry(repositoryId, entryId, null).join();
        path=metadata.getFolderPath();
        name=metadata.getName();int idx=name.lastIndexOf('.');
        if(idx>=0){extension=name.substring(idx);name=name.substring(0,idx);}
        else{extension="";}
        isDirectory=metadata.isContainer();
    }
    @Override
    public String path(){
        if(path==null)getMetadata();
        return path;
    };
    @Override
    public String name(){
        if(name==null)getMetadata();
        return name;
    };
    @Override
    public String extension(){
        if(extension==null)getMetadata();
        return extension;
    };
    @Override
    public boolean isDirectory(){
        if(isDirectory==null)getMetadata();
        return isDirectory;
    };
    private Long length;
    @Override
    public long length(){
        if(length==null){
            length=Long.valueOf(client.getDocumentContentType(repositoryId, entryId).join().get("Content-Length"));
        }
        return length;
    };
    private String[] fileContents;
    @Override
    public String[] fileContents(){
        if(fileContents==null){
            CompletableFuture<String[]>future=new CompletableFuture();
            client.exportDocument(repositoryId, entryId, null,x->{
                try(Stream<String> stream=new BufferedReader(new InputStreamReader(x)).lines()){
                    future.complete(stream.toArray(String[]::new));
                }
            });
            fileContents=future.join();
        }
        return fileContents;
    };
    @Override
    public void dirContents(Predicate<Entry> callback){
        client.getEntryListingForEach(
                z->z.thenApply(y->{
                    try(var stream=y.getValue().stream()){
                        for(var x: (Iterable<com.laserfiche.repository.api.clients.impl.model.Entry>)(stream::iterator)){
                            RemoteEntry entry=new RemoteEntry(repositoryId,x.getId());
                            entry.path=x.getFolderPath();
                            String name=x.getName();int idx=name.lastIndexOf('.');
                            if(idx>=0){entry.extension=name.substring(idx);entry.name=name.substring(0,idx);}
                            else{entry.name=name;entry.extension="";}
                            entry.isDirectory=x.isContainer();
                            if(!callback.test(entry)){return false;}
                        }
                    }
                    return true;
                }),
                null,repositoryId,entryId,
                true, null, null, null, null,null, "name", null, null, null
        ).join();
    }

    @Override
    public void rename(String newname) {throw new UnsupportedOperationException("Not supported.");}

    @Override
    public Entry makeFile(String name, String[] contents) {throw new UnsupportedOperationException("Not supported.");}
}
