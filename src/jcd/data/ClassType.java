/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import java.util.ArrayList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 *
 * @author yan
 */
public class ClassType extends VBox {

    String name;
    String type;
    String access;
    String packageName;
    boolean isAbstract;
    ArrayList<Node> parents;
    ArrayList<Variable> variables;
    ArrayList<Method> methods;
    Text nameText;
    VBox classBox;
    VBox variableBox;
    VBox methodBox;
    Text absText;
    ArrayList<FeatheredArrow> fArrows;
    ArrayList<SolidArrow> sArrows;
    ArrayList<DiamondArrow> dArrows;

    public ClassType(String n) {
        setName(n);
        setType("class");
        setAccess("public");
        setAbstract(false);
        parents = new ArrayList();
        setPackageName("default_package");
        variables = new ArrayList();
        methods = new ArrayList();
        fArrows = new ArrayList();
        sArrows = new ArrayList();
        dArrows = new ArrayList();
        classBox = new VBox();
        variableBox = new VBox();
        methodBox = new VBox();
        nameText = new Text(n);
        absText = new Text();
        classBox.getChildren().addAll(nameText);
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

    public ArrayList<Node> getParents() {
        return parents;
    }

    public String getPackageName() {
        return packageName;
    }

    public boolean getAbstract() {
        return isAbstract;
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

    public void setAbstract(boolean isAbs) {
        this.isAbstract = isAbs;
    }

    public VBox getVariableBox() {
        return variableBox;
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

    public ArrayList<DiamondArrow> getDArrows() {
        return dArrows;
    }

    public ArrayList<Variable> getVariables() {
        return variables;
    }

    public ArrayList<Method> getMethods() {
        return methods;
    }

    public void setAll(String access, boolean isAbstract, String packageName, Double x, Double y, Double width, Double height) {
        this.access = access;
        this.isAbstract = isAbstract;
        this.packageName = packageName;
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.setMinSize(width, height);
    }

    //-interface : InterfaceType
    public void addVariable(Variable variable) {
        setVariable(variable);
        variableBox.getChildren().add(variable);
    }

    public void setVariable(Variable variable) {
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
        variable.setText(varString);
    }

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
        methString += ")";
        if (!method.getReturnType().equals("")) {
            methString += " : " + method.getReturnType();
        } else if (method.getReturnType().equals("")) {
            methString += " : " + "void";
        }
        if (method.getAbstract() == true) {
            methString += " {abstract} ";
        }
        method.setText(methString);
    }

    public void setNameText() {
        if (isAbstract == true) {
            absText.setText("{abstract}");
            if (!classBox.getChildren().contains(absText)) {
                classBox.getChildren().add(absText);
            } 
        } else {
            classBox.getChildren().remove(absText);
        }
    }

    public ArrayList<Method> getAbstractMethods() {
        ArrayList<Method> abstractMethods = new ArrayList();
        for (Method method : methods) {
            if (method.getAbstract() == true) {
                abstractMethods.add(method);
            }
        }
        return abstractMethods;
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
