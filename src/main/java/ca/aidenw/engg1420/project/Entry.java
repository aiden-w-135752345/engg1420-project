/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.aidenw.engg1420.project;

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
public abstract class Entry {
    public abstract CompletionStage<String> name();
    public abstract CompletionStage<String> path();
    public abstract CompletionStage<Boolean>isDirectory();
    public abstract CompletionStage<Long> length();
    public abstract CompletionStage<String[]> fileContents();
    public abstract CompletionStage<Stream<Entry>> dirContents();
}
