/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.aidenw.engg1420.project;

import java.util.function.Function;

/**
 * @author Lucy
 * @author Leonardo
 * @author Daniel
 * @author Vanessa
 * @author Aiden
 */
public abstract class Entry {
    public abstract String name();
    public abstract String path();
    public abstract boolean isDirectory();
    public abstract long length();
    public abstract String[] fileContents();
    public abstract void dirContents(Function<Entry,Boolean> consumer);
    public abstract void rename(String newname);
    public abstract Entry makeFile(String name,String[] contents);
}
