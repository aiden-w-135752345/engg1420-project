/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.aidenw.engg1420.project;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.function.Predicate;

/**
 * @author Lucy
 * @author Leonardo
 * @author Daniel
 * @author Vanessa
 * @author Aiden
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, property="type")
@JsonSubTypes({@JsonSubTypes.Type(value=LocalEntry.class,name="local"),@JsonSubTypes.Type(value=RemoteEntry.class,name="remte")})
public abstract class Entry {
    public abstract String path();
    public abstract String name();
    public abstract String extension();
    public abstract boolean isDirectory();
    public abstract long length();
    public abstract String[] fileContents();
    /**
     * Reads directory contents, passing the Entries to {@code consumer}, until {@code callback} returns false.<br>
     * This would list the filenames of the contents of a directory, while they are all over 1KB:
     * <pre>{@code dir.dirContents(entry->{
     * System.out.println(entry.name);
     * return entry.length()>1000;
     * })}</pre>
     * @param callback lambda to receive directory contents
     */
    public abstract void dirContents(Predicate<Entry> callback);
    public abstract void rename(String newname);
    public abstract Entry makeFile(String name,String[] contents);
}
