/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.aidenw.engg1420.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.laserfiche.repository.api.clients.EntriesClient;
import com.laserfiche.repository.api.clients.impl.ApiException;
import com.laserfiche.repository.api.clients.impl.model.ODataValueContextOfIListOfEntry;
import com.laserfiche.repository.api.clients.impl.model.PatchEntryRequest;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
    private final String repoId;
    private int entryId;
    @JsonCreator
    public RemoteEntry(@JsonProperty("repositoryId")String repo,@JsonProperty("entryId")int entry){repoId=repo;entryId=entry;}
    private RemoteEntry(String repo){repoId=repo;}
    public static void setClient(EntriesClient c){client=c;}
    private Integer parentId;
    private String path;
    private String name;
    private String extension;
    private Boolean isDirectory;
    
    private void getMetadata(){
        com.laserfiche.repository.api.clients.impl.model.Entry metadata=client.getEntry(repoId, entryId, null).join();
        parentId=metadata.getParentId();
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
            length=Long.valueOf(client.getDocumentContentType(repoId, entryId).join().get("Content-Length"));
        }
        return length;
    };
    private String[] fileContents;
    @Override
    public String[] fileContents(){
        if(fileContents==null){
            CompletableFuture<String[]>future=new CompletableFuture();
            client.exportDocument(repoId, entryId, null,x->{
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
        ODataValueContextOfIListOfEntry response = client.getEntryListing(
                repoId,entryId, null,null, null, null,
                null, null, "name", null, null, null
        ).join();
        for(;;) {
            for(var x: response.getValue()){
                RemoteEntry entry=new RemoteEntry(repoId,x.getId());
                entry.parentId=x.getParentId();
                entry.path=x.getFolderPath();
                String name=x.getName();int idx=name.lastIndexOf('.');
                if(idx>=0){entry.extension=name.substring(idx);entry.name=name.substring(0,idx);}
                else{entry.name=name;entry.extension="";}
                entry.isDirectory=x.isContainer();
                if(!callback.test(entry)){return;}
            }
            
            String nextLink=response.getOdataNextLink();
            if(nextLink==null){return;}
            response = client.getEntryListingNextLink(nextLink, null).join();
        }
    }
    private static boolean warnWriteFail=true;
    @Override
    public void rename(String newname) {
        name=newname;
        try {
            PatchEntryRequest requestBody=new PatchEntryRequest();
            if(parentId==null){getMetadata();}
            requestBody.setParentId(parentId);
            requestBody.setName(newname+extension);
            client.moveOrRenameEntry(repoId,entryId,requestBody,null,null).join();
        } catch (ApiException e) {if(warnWriteFail){
            System.out.println("Warning: some remote entries failed to write.");warnWriteFail=false;
        }}
    }

    @Override
    public Entry makeFile(String name, String[] contents) {
        RemoteEntry entry=new RemoteEntry(repoId);
        entry.path=path();
        entry.name=name;
        entry.extension=extension();
        entry.parentId=parentId;
        entry.isDirectory = false;
        entry.length = 0L;
        entry.fileContents = contents;
        for (String line : contents) {entry.length += line.length() + 1;}
        try {
            entry.entryId=client.importDocument(
                    repoId,parentId,name+extension,null,null,
                    new ByteArrayInputStream(String.join("\n", contents).getBytes(StandardCharsets.UTF_8)),
                    null
            ).join().getOperations().getEntryCreate().getEntryId();
        } catch (ApiException e) {if(warnWriteFail){
            System.out.println("Warning: some remote entries failed to write.");warnWriteFail=false;
        }}
        return entry;
    }
}
