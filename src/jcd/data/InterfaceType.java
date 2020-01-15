/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import java.util.ArrayList;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 *
 * @author yan
 */
public class InterfaceType extends VBox {

    String name;
    String type;
    String access;
    String packageName;
    ArrayList<Node> parents;
    ArrayList<Variable> variables;
    ArrayList<Method> methods;
    Text nameText;
    VBox classBox;
    VBox variableBox;
    VBox methodBox;
    ArrayList<FeatheredArrow> fArrows;
    ArrayList<SolidArrow> sArrows;

    public InterfaceType(String n) {
        setName(n);
        setType("interface");
        setAccess("public");
        parents = new ArrayList();
        setPackageName("default_package");
        variables = new ArrayList();
        methods = new ArrayList();
        fArrows = new ArrayList();
        sArrows = new ArrayList();
        classBox = new VBox();
        variableBox = new VBox();
        methodBox = new VBox();
        nameText = new Text(n);
        classBox.getChildren().addAll(new Text("<<Interface>>"),nameText);
        this.getChildren().addAll(classBox, variableBox, methodBox);
        this.setMinSize(150, 50);
        initStyle();
    }

    public String getName() {
        return name;
    }
    
    public String getType() {
        return type;
    }
    
    public String getAccess() {
        return access;
    }

    public String getPackageName() {
        return packageName;
    }

    public ArrayList<Node> getParents() {
        return parents;
    }
    
    public ArrayList<Variable> getVariables() {
        return variables;
    }
    
    public ArrayList<Method> getMethods() {
        return methods;
    }
    
    public VBox getMethodBox() {
        return methodBox;
    }
    
    public ArrayList<FeatheredArrow> getFArrows() {
        return fArrows;
    } 
    
    public ArrayList<SolidArrow> getSArrows() {
        return sArrows;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void setType(String type) {
        this.type = type;
    } 
    
    public void setAccess(String access) {
        this.access = access;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setParents(ArrayList<Node> parents) {
        this.parents = parents;
    }
    
//      interfaceType.setAll(access, packageName, x, y, width, height);
    public void setAll(String access, String packageName, Double x, Double y, Double width, Double height) {
        this.access = access;
        this.packageName = packageName;
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.setMinSize(width, height);
    }
    
    public void addVariable(Variable variable) {
        Text varText;
        String varString = "";
        if (variable.getAccess().equals("public")) {
            varString += "+";
        } else if (variable.getAccess().equals("private")) {
            varString += "-";
        } else if (variable.getAccess().equals("protected")) {
            varString += "#";
        } else {
            varString += "~";
        }
        if (variable.getStatic() == true) {
            varString += "$";
        }
        if (variable.getFinal() == true) {
            varString += " ";
        }
        varString += variable.getName() + " : " + variable.getType();
        varText = new Text(varString);
        variableBox.getChildren().add(varText);
    }

    //+markAsEdited(gui : saf.ui.AppGUI) : void
    public void addMethod(Method method) {
        setMethod(method);
        methodBox.getChildren().add(method);
    }
    
    public void setMethod(Method method) {
        String methString = "";
        if (method.getAccess().equals("public")) {
            methString += "+";
        } else if (method.getAccess().equals("private")) {
            methString += "-";
        } else if (method.getAccess().equals("protected")) {
            methString += "#";
        } else {
            methString += "~";
        }
        if (method.getStatic() == true) {
            methString += "$";
        }
        methString += method.getName() + "(";
        ArrayList<Variable> parameters = method.getParameters();
        for (int i = 0; i < parameters.size(); i++) {
            methString += parameters.get(i).getName() + " : " + parameters.get(i).getType();
            if (i + 1 != parameters.size()) {
                methString += ",";
            }
        }
        methString += ")" + method.getReturnType();
        if(!method.getReturnType().equals("")){
            methString += " : " + method.getReturnType();
        }
//        if(method.getAbstract() == true) {
//            methString += " {abstract} ";
//        }
        method.setText(methString);
    }
    
    public void initStyle() {
        this.classBox.getStyleClass().add("class_gui_style");
        if (!variableBox.getChildren().isEmpty()) {
            this.variableBox.getStyleClass().add("class_gui_style");
        }
        if (!methodBox.getChildren().isEmpty()) {
            this.methodBox.getStyleClass().add("class_gui_style");
        }
    }
}
