/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.aidenw.engg1420.project;

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
    public RemoteEntry(String repo,int entry){repositoryId=repo;entryId=entry;}
    public static void setClient(EntriesClient c){client=c;}
    private String name;
    private String path;
    private Boolean isDirectory;
    
    private void getMetadata(){
        com.laserfiche.repository.api.clients.impl.model.Entry metadata=client.getEntry(repositoryId, entryId, null).join();
        name=metadata.getName();
        path=metadata.getFolderPath();
        isDirectory=metadata.isContainer();
    }
    @Override
    public String name(){
        if(name==null)getMetadata();
        return name;
    };
    @Override
    public String path(){
        if(path==null)getMetadata();
        return path;
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
    public void dirContents(Predicate<Entry> consumer){
        client.getEntryListingForEach(
                z->z.thenApply(y->{
                    try(var stream=y.getValue().stream()){
                        for(var x: (Iterable<com.laserfiche.repository.api.clients.impl.model.Entry>)(stream::iterator)){
                            RemoteEntry entry=new RemoteEntry(repositoryId,x.getId());
                            entry.name=x.getName();
                            entry.path=x.getFolderPath();
                            entry.isDirectory=x.isContainer();
                            if(!consumer.test(entry)){return false;}
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
