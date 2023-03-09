/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.aidenw.engg1420.project;

import com.laserfiche.repository.api.RepositoryApiClient;
import java.io.BufferedReader;
import java.io.InputStreamReader;
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
public class RemoteEntry extends Entry {
    private static RepositoryApiClient client;
    private final String repositoryId;
    private final int entryId;
    public RemoteEntry(String repo,int entry){repositoryId=repo;entryId=entry;}
    public void setApiClient(RepositoryApiClient c){client=c;}
    
    private CompletionStage<com.laserfiche.repository.api.clients.impl.model.Entry> metadata;
    private void getMetadata(){
        if(metadata==null){metadata=client.getEntriesClient().getEntry(repositoryId, entryId, null);}
    }
    @Override
    public CompletionStage<String> name(){
        getMetadata();
        return metadata.thenApply(x->{return x.getName();});
    };
    @Override
    public CompletionStage<String> path(){
        getMetadata();
        return metadata.thenApply(x->{return x.getName();});
    };
    @Override
    public CompletionStage<Boolean>isDirectory(){
        getMetadata();
        return metadata.thenApply(x->{return x.isContainer();});
    };
    private CompletableFuture<Long> length;
    @Override
    public CompletionStage<Long> length(){
        if(length==null){
            length=client.getEntriesClient().getDocumentContentType(repositoryId, entryId).thenApply(x->Long.valueOf(x.get("Content-Length")));
        }
        return length;
    };
    private CompletableFuture<String[]> fileContents;
    @Override
    public CompletionStage<String[]> fileContents(){
        if(fileContents==null){
            fileContents=new CompletableFuture();
            client.getEntriesClient().exportDocument(
                repositoryId, entryId, null,
                x->fileContents.complete(new BufferedReader(new InputStreamReader(x)).lines().toArray(String[]::new))
            );
        }
        return fileContents;
    };
    private CompletionStage<Stream<Entry>> dirContents;
    @Override
    public CompletionStage<Stream<Entry>> dirContents(){
        if (dirContents == null) {
            dirContents=client.getEntriesClient().getEntryListing(
                    repositoryId, entryId, true, null, null, null, null, 
                    null, "name", null, null, null
            ).thenApply(x->x.getValue().stream().map(y->{
                RemoteEntry entry =new RemoteEntry(repositoryId,y.getId());
                entry.metadata=CompletableFuture.completedFuture(y);
                return entry;
            }));
        }
        return dirContents;
    }
}
