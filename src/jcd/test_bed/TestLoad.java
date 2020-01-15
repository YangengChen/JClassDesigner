/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.test_bed;

import java.util.ArrayList;
import javafx.scene.Node;
import jcd.data.ClassType;
import jcd.data.DataManager;
import jcd.data.InterfaceType;
import jcd.data.Method;
import jcd.data.Variable;
import jcd.file.FileManager;
import saf.AppTemplate;

/**
 *
 * @author yan
 */
public class TestLoad {

    public static void main(String[] args) throws Exception {
        FileManager fm = new FileManager();
        AppTemplate app = null;
        DataManager dm = new DataManager(app);
        fm.loadData(dm, ".\\work\\DesignSaveTest.json");
        ArrayList<Node> objects = dm.getObjects();
        for (Node node : objects) {
            if (node instanceof ClassType) {
                ClassType classType = (ClassType) node;
                String name = classType.getName();
                String access = classType.getAccess();
                String packageName = classType.getPackageName();
                String x = Double.toString(classType.getLayoutX());
                String y = Double.toString(classType.getLayoutY());
                String width = Double.toString(classType.getMinWidth());
                String height = Double.toString(classType.getMinHeight());
                ArrayList<Node> parents = classType.getParents();
                ArrayList<Variable> variables = classType.getVariables();
                ArrayList<Method> methods = classType.getMethods();
                classType.setAll(access, false, packageName, Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(width), Double.parseDouble(height));
                System.out.println("Class: " + name + "\t" + access + "\t" + packageName + "\t" + x + "\t" + y + "\t" + width + "\t" + height);
                for (Node n : parents) {
                    if(n instanceof ClassType){
                        ClassType parent = (ClassType)n;
                        System.out.println("Parent: " + parent.getType() + "\t" + parent.getName());
                    }
                    else if(n instanceof InterfaceType){
                        InterfaceType parent = (InterfaceType)n;
                        System.out.println("Parent: " + parent.getType() + "\t" + parent.getName());
                    }
                }
                System.out.println("\taccess\t" + "\tstatic\tfinal\ttype\tname");
                for (Variable var : variables) {
                    System.out.println("Variable: " + var.getAccess() + "\t" + var.getStatic() + "\t" + var.getFinal() + "\t" + var.getType() + "\t" + var.getName());
                }
                System.out.println("\taccess\tstatic\treturn\tname");
                for (Method meth : methods) {
                    System.out.println("Method: " + meth.getAccess() + "\t" + meth.getStatic() + "\t" + meth.getReturnType() + "\t" + meth.getName());
                    ArrayList<Variable> params = meth.getParameters();
                    for (Variable v : params) {
                        System.out.println("Parameter: " + v.getName() + "\t" + v.getType());
                    }
                }
            }
        }
    }
}
