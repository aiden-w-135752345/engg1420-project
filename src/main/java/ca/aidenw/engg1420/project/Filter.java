/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.aidenw.engg1420.project;

/**
 * @author Lucy
 * @author Leonardo
 * @author Daniel
 * @author Vanessa
 * @author Aiden
 */
public abstract class Filter extends ProcessingElement {
    protected abstract boolean condition(Entry entry);
    @Override
    protected void accept(Entry entry) {
        if(condition(entry)){next.accept(entry);}
    }
}
