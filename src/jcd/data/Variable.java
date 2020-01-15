/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import javafx.scene.text.Text;

/**
 *
 * @author yan
 */
public class Variable extends Text {
    
    String name;
    String type;
    boolean isFinal;
    boolean isStatic;
    String access;
    
    public Variable(String name){
        this.name = name;
        this.type = "";
        this.isStatic = false;
        this.access = "";
    }
    
    public Variable(String name, String type){
        this.name = name;
        this.type = type;
        this.isStatic = false;
        this.access = "";
    }
    public String getName(){
        return name;
    }
    
    public String getType(){
        return type;
    }
    
    public boolean getStatic(){
        return isStatic;
    }
    
    public boolean getFinal(){
        return isFinal;
    }
    
    public String getAccess(){
        return access;
    }
    
    public void setName(String name){
       this.name = name;
    }
    
    public void setType(String type){
        this.type = type;
    }
    
    public void setStatic(boolean b){
        this.isStatic = b;
    }
    
    public void setFinal(boolean b){
        this.isFinal = b;
    }
    
    public void setAccess(String access){
        this.access = access;
    }
    
    public void setAll(String type, boolean s, boolean f, String access) {
        this.type = type;
        this.isStatic = s;
        this.isFinal = f;
        this.access = access;
    }
    public void updateAll(){
        
    }
    
    @Override
    public String toString() {
        String myString = "";
        myString += access;
        if(isStatic == true) {
            myString += " static";
        }
        if(isFinal == true) {
            myString += " final";
        }
        myString += " "+type + " " + name;
        if(isStatic == true && isFinal == true){
            myString += " = \"Something\"";
        }
        myString += ";\n";
        return myString;
    }
    
}



