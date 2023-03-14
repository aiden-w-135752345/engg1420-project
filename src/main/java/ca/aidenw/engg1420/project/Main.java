/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ca.aidenw.engg1420.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laserfiche.api.client.model.AccessKey;
import com.laserfiche.repository.api.RepositoryApiClientImpl;
import java.io.File;
import java.io.IOException;
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
    static ExecutorService executor=Executors.newCachedThreadPool();
    /**
     * @param args the command line arguments
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    public static void main(String[] args) throws JsonProcessingException, IOException {
        try(var client=RepositoryApiClientImpl.createFromAccessKey(null,AccessKey.createFromBase64EncodedAccessKey(null))){
            RemoteEntry.setClient(client.getEntriesClient()); // load keys
            
            // read description
            ObjectMapper objectMapper = new ObjectMapper(); 
            Main scenario = objectMapper.readValue(new File(args[1]), Main.class);
            
            ProcessingElement prev = null;
            for (ProcessingElement next : scenario.processing_elements) {
                if (prev != null) prev.setNext(next);
                prev = next;
            }
            if (prev != null) prev.setNext(new ProcessingElement(){@Override protected void accept(Entry entry) {/* ignore */}});
            for (ProcessingElement elem : scenario.processing_elements) {elem.start();}
        }
    }
}
