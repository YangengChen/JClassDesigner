/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import java.util.ArrayList;
import javafx.scene.text.Text;

/**
 *
 * @author yan
 */
public class Method extends Text {

    String name;
    String returnType;
    boolean isStatic;
    boolean isAbstract;
    String access;
    ArrayList<Variable> parameters;

    public Method(String name) {
        this.name = name;
        this.returnType = "";
        this.isStatic = false;
        this.isAbstract = false;
        this.access = "";
        parameters = new ArrayList();
    }

    public String getName() {
        return name;
    }

    public String getReturnType() {
        return returnType;
    }

    public boolean getStatic() {
        return isStatic;
    }

    public String getAccess() {
        return access;
    }

    public boolean getAbstract() {
        return isAbstract;
    }

    public ArrayList<Variable> getParameters() {
        return parameters;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public void setStatic(boolean b) {
        this.isStatic = b;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public void setAbstract(boolean isAbs) {
        this.isAbstract = isAbs;
    }

    public void setParameters(ArrayList<Variable> param) {
        this.parameters = param;
    }

    public void setAll(String access, boolean s, boolean a, String returnType) {
        this.access = access;
        this.isStatic = s;
        this.isAbstract = a;
        this.returnType = returnType;
    }

    @Override
    public String toString() {
        String myString = "";
        myString += access;
        if (isAbstract == true) {
            myString += " abstract " + returnType + " " + name;
        } else {
            if (isStatic == true) {
                myString += " static";
            }
            if (!returnType.equals("")) {
                myString += " " + returnType + " " + name;
            } else {
                myString += " " + name;
            }
        }
        myString += "(";
        if (!parameters.isEmpty()) {
            for (int i = 0; i < parameters.size(); i++) {
                Variable v = parameters.get(i);
                myString += v.getType() + " " + v.getName();
                if (i + 1 != parameters.size()) {
                    myString += ",";
                }
            }
        }
        myString += ")";
        if (isAbstract == true) {
            myString += ";";
        } else {
            myString += " {\n";
            if (returnType.equals("")) {

            } else if (returnType.equals("int") || returnType.equals("byte") || returnType.equals("short") 
                    || returnType.equals("long") || returnType.equals("float") || returnType.equals("double")) {
                myString += "return 0;\n";
            } else if (returnType.equals("boolean")){
                myString += "return false;\n";
            } else if (!returnType.equals("void")){
                myString += "return null;\n";
            }
            myString += "}\n";
        }
        return myString;
    }
}
