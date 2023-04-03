/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ca.aidenw.engg1420.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laserfiche.api.client.model.AccessKey;
import com.laserfiche.repository.api.RepositoryApiClient;
import com.laserfiche.repository.api.RepositoryApiClientImpl;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * @author Lucy
 * @author Leonardo
 * @author Daniel
 * @author Vanessa
 * @author Aiden
 */
public class Main {
    @JsonProperty("name")
    private String name;
    @JsonProperty("processing_elements")
    private ProcessingElement[] processing_elements;
    /**
     * @param args[1] the scenario description filename
     * @param args[2] the service principal key for LaserFiche
     * @param args[3] the access key for LaserFiche
     */
    public static void main(String[] args){
        if(args.length<3){
            System.out.println("usage: java -jar engg1420-project-1.0-SNAPSHOT.jar scenarioDescription.json servicePricipalKey accessKey");
            System.exit(1);
        }
        // read description
        Main scenario;
        try {
            scenario = new ObjectMapper().readValue(new File(args[1]), Main.class);
        } catch (IOException ex) {
            System.out.println("Could not read scenario description. ");
            System.exit(42);return;
        }
        // connect ProccesingElements
        ProcessingElement prev = null;
        for (ProcessingElement next : scenario.processing_elements) {
            if (prev != null) prev.setNext(next);
            prev = next;
        }
        // ignore outputs of the last ProcessingElement
        if (prev != null) prev.setNext(new ProcessingElement(){@Override protected void accept(Entry entry) {/* ignore */}});
        // load keys and connect to LaserFiche
        try(RepositoryApiClient client=RepositoryApiClientImpl.createFromAccessKey( args[2],AccessKey.createFromBase64EncodedAccessKey(args[3]))){
            RemoteEntry.setClient(client.getEntriesClient());
            // start running
            CompletableFuture[] cfs=new CompletableFuture[scenario.processing_elements.length];
            for(int i = 0; i < scenario.processing_elements.length; ++i ) {
                final ProcessingElement elem=scenario.processing_elements[i];
                cfs[i]=CompletableFuture.runAsync(elem::start,Main.executor);
            }
            CompletableFuture.allOf(cfs).join();
        }
    }
    /**
     * Thing for multi-threading:<br>
     * Start with {@code ArrayList<CompletableFuture>cfs=new ArrayList<>();}<br>
     * Then, {@code cfs.add(CompletableFuture.runAsync(()->{ code here;},Main.executor));} to run code asynchronously.<br>
     * Finally, {@code CompletableFuture.allOf(cfs.toArray(CompletableFuture[]::new)).join(); } to wait for the asynchronous code to finish.
     */
    public static ExecutorService executor=Executors.newCachedThreadPool();
}
