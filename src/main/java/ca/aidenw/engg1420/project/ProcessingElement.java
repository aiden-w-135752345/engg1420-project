/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.aidenw.engg1420.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
/**
 * @author Lucy
 * @author Leonardo
 * @author Daniel
 * @author Vanessa
 * @author Aiden
 */
public abstract class ProcessingElement {
    @JsonProperty("input_entries") private Entry[] input_entries;
    /**
     * provide an Entry to this ProcessingElement.
     * this element will process the entry, and can call next.accept() to pass it on to the next entry
     * @param entry
     */
    abstract protected void accept(Entry entry);
    protected ProcessingElement next;
    public final void setNext(ProcessingElement before){this.next=this;}
    /**
     * Starts processing, beginning with the input entries specified in the scenario description
     */
    public final void start(){
        CompletableFuture[] cfs=new CompletableFuture[input_entries.length];
        for(int i=0;i<input_entries.length;++i){
            final Entry entry=input_entries[i];
            cfs[i]=CompletableFuture.runAsync(()->{this.accept(entry);},Main.executor);
        }
        CompletableFuture.allOf(cfs).join();
    }
    // JSON parsing stuff follows...
    private static class Parameter extends AbstractMap.SimpleEntry<String,String>{
        public String getKey(){return super.getKey();}
        public String getValue(){return super.getValue();}
        @Override @JsonSetter("value")
        public String setValue(String value){return super.setValue(value);}
        @JsonCreator
        Parameter(@JsonProperty("name")String name, @JsonProperty("value") String value){super(name,value);}
    };    
    @JsonCreator
    private ProcessingElement fromJSON(@JsonProperty("type") String typeName,
            @JsonProperty("parameters") Parameter[] parameters
    ){
        Class<? extends ProcessingElement> typeClass;
        switch (typeName) {
            case "NameFilter" -> typeClass=NameFilter.class;
            case "LengthFilter" -> typeClass=LengthFilter.class;
            case "ContentFilter" -> typeClass=ContentFilter.class;
            case "CountFilter" -> typeClass=CountFilter.class;
            case "Split" -> typeClass=Split.class;
            case "List" -> typeClass=ListFiles.class;
            case "Rename" -> typeClass=Rename.class;
            case "Print" -> typeClass=Print.class;
            default -> throw new IllegalArgumentException();
        }
        ObjectMapper paramMapper=new ObjectMapper();
        return paramMapper.convertValue(Map.ofEntries(parameters), typeClass);
    }
}