/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import jcd.gui.Workspace;
import saf.AppTemplate;
import saf.components.AppDataComponent;

/**
 *
 * @author yan
 */
public class DataManager implements AppDataComponent {

    AppTemplate app;
    ObservableList<Node> guiObjects;
    ObservableList<CheckMenuItem> parents;
    ArrayList<Node> objects;
    Stack undoStack;
    Stack redoStack;

    DesignerState state;

    Node selectedObject;

    double initX;
    double initY;
    double multiplier;
    Workspace workspace;

    /**
     * THis constructor creates the data manager and sets up the
     *
     *
     * @param initApp The application within which this data manager is serving.
     * @throws java.lang.Exception
     */
    public DataManager(AppTemplate initApp) throws Exception {

        app = initApp;
        multiplier = 0.0;
        undoStack = new Stack();
        redoStack = new Stack();
        objects = new ArrayList();
        parents = FXCollections.observableArrayList();
    }

    public ObservableList<Node> getguiObjects() {
        return guiObjects;
    }

    public void addObject(Node node) {
        guiObjects.add(node);
    }

    public ArrayList<Node> getObjects() {
        return objects;
    }

    public void addOb(Node node) {
        objects.add(node);
    }

    public void setObjects(ObservableList<Node> objects) {
        this.guiObjects = objects;
    }

    public void setObs(ArrayList<Node> obs) {
        this.objects = obs;
    }

    public ObservableList<CheckMenuItem> returnParents() {
        return this.parents;
    }

    public void addClass(double x, double y) {
        ClassType classType = new ClassType("defaultname" + objects.size());
        classType.setLayoutX(x);
        classType.setLayoutY(y);
        classType.setScaleX(Math.pow(2.0, multiplier));
        classType.setScaleY(Math.pow(2.0, multiplier));
        addObject(classType);
        objects.add(classType);
        initHandlers(classType);
        updateParentMenu();
        initWorkspace().getParentNameBox().getItems().setAll(parents);
        initWorkspace().getUndoBtn().setDisable(false);
        undoStack.add(classType);
    }

    public void addInterface(double x, double y) {
        InterfaceType interfaceType = new InterfaceType("defaultname" + objects.size());
        interfaceType.setLayoutX(x);
        interfaceType.setLayoutY(y);
        interfaceType.setScaleX(Math.pow(2.0, multiplier));
        interfaceType.setScaleY(Math.pow(2.0, multiplier));
        addObject(interfaceType);
        objects.add(interfaceType);
        initHandlers(interfaceType);
        updateParentMenu();
        initWorkspace().getParentNameBox().getItems().setAll(parents);
        initWorkspace().getUndoBtn().setDisable(false);
        undoStack.add(interfaceType);
    }

    public void removeSelected() {
        ArrayList<SolidArrow> sArrows = new ArrayList();
        ArrayList<FeatheredArrow> fArrows = new ArrayList();
        ArrayList<DiamondArrow> dArrows = new ArrayList();
        if (selectedObject instanceof ClassType) {
            ClassType selectedClass = (ClassType) selectedObject;
            sArrows = selectedClass.getSArrows();
            fArrows = selectedClass.getFArrows();
            dArrows = selectedClass.getDArrows();
        } else if (selectedObject instanceof InterfaceType) {
            InterfaceType selectedInt = (InterfaceType) selectedObject;
            sArrows = selectedInt.getSArrows();
            fArrows = selectedInt.getFArrows();
        }
        for (DiamondArrow dArrow : dArrows) {
            if (selectedObject instanceof ClassType) {
                ClassType selectedClass = (ClassType) selectedObject;
                if (dArrow.getTargetClass().equals(selectedClass)) {
                    dArrow.getSourceClass().getDArrows().remove(dArrow);
                    guiObjects.remove(dArrow.getLine());
                    guiObjects.remove(dArrow.getArrow());
                } else if (dArrow.getSourceClass().equals(selectedClass)) {
                    dArrow.getTargetClass().getDArrows().remove(dArrow);
                    guiObjects.remove(dArrow.getLine());
                    guiObjects.remove(dArrow.getArrow());
                }
            }
        }
        for (SolidArrow sArrow : sArrows) {
            if (selectedObject instanceof ClassType) {
                ClassType selectedClass = (ClassType) selectedObject;
                if (sArrow.getTargetClass().equals(selectedClass)) {
                    sArrow.getSourceClass().getSArrows().remove(sArrow);
                    guiObjects.remove(sArrow.getLine());
                    guiObjects.remove(sArrow.getArrow());
                } else if (sArrow.getSourceClass().equals(selectedClass)) {
                    if (selectedClass.getParents().contains(sArrow.getTargetClass())) {
                        sArrow.getTargetClass().getSArrows().remove(sArrow);
                        guiObjects.remove(sArrow.getLine());
                        guiObjects.remove(sArrow.getArrow());
                    } else if (selectedClass.getParents().contains(sArrow.getTargetInterface())) {
                        sArrow.getTargetInterface().getSArrows().remove(sArrow);
                        guiObjects.remove(sArrow.getLine());
                        guiObjects.remove(sArrow.getArrow());
                    }
                }
            } else if (selectedObject instanceof InterfaceType) {
                InterfaceType selectedInt = (InterfaceType) selectedObject;
                if (sArrow.getTargetInterface().equals(selectedInt)) {
                    sArrow.getSourceClass().getSArrows().remove(sArrow);
                    sArrow.getSourceInterface().getSArrows().remove(sArrow);
                    guiObjects.remove(sArrow.getLine());
                    guiObjects.remove(sArrow.getArrow());
                } else if (sArrow.getSourceInterface().equals(selectedInt)) {
                    if (selectedInt.getParents().contains(sArrow.getTargetInterface())) {
                        sArrow.getTargetInterface().getSArrows().remove(sArrow);
                        guiObjects.remove(sArrow.getLine());
                        guiObjects.remove(sArrow.getArrow());
                    }
                }
            }
        }
        for (FeatheredArrow fArrow : fArrows) {
            if (selectedObject instanceof ClassType) {
                ClassType selectedClass = (ClassType) selectedObject;
                if (fArrow.getTargetClass().equals(selectedClass)) {
                    fArrow.getSourceClass().getFArrows().remove(fArrow);
                    guiObjects.remove(fArrow.getLine());
                    guiObjects.remove(fArrow.getArrow());
                } else if (fArrow.getSourceClass().equals(selectedClass)) {
                    fArrow.getTargetClass().getFArrows().remove(fArrow);
                    guiObjects.remove(fArrow.getLine());
                    guiObjects.remove(fArrow.getArrow());
                }
            } else if (selectedObject instanceof InterfaceType) {
                InterfaceType selectedInt = (InterfaceType) selectedObject;
                if (fArrow.getSourceInterface().equals(selectedInt)) {
                    fArrow.getTargetClass().getFArrows().remove(fArrow);
                    guiObjects.remove(fArrow.getLine());
                    guiObjects.remove(fArrow.getArrow());
                }
            }
            for (Node object : objects) {
                if (object instanceof ClassType) {
                    ClassType c = (ClassType) object;
                    if (c.getParents().contains(selectedObject)) {
                        c.getParents().remove(selectedObject);
                    }
                } else if (object instanceof InterfaceType) {
                    InterfaceType c = (InterfaceType) object;
                    if (c.getParents().contains(selectedObject)) {
                        c.getParents().remove(selectedObject);
                    }
                }
            }
        }
        guiObjects.remove(selectedObject);
        objects.remove(selectedObject);

        selectedObject = null;
        updateParentMenu();
        initWorkspace().getParentNameBox().getItems().setAll(parents);
    }

    public void undo() {
        try {
            if (!guiObjects.contains((Node) undoStack.peek())) {
                Node node = (Node) undoStack.pop();
                addObject(node);
                redoStack.add(node);
                objects.add(node);
            } else {
                Node node = (Node) undoStack.pop();
                guiObjects.remove(node);
                objects.remove(node);
                redoStack.add(node);
            }
            updateParentMenu();
            initWorkspace().getParentNameBox().getItems().setAll(parents);
        } catch (Exception e) {

        }
    }

    public void redo() {
        try {
            if (guiObjects.contains((Node) redoStack.peek())) {
                Node node = (Node) redoStack.pop();
                guiObjects.remove(node);
                objects.remove(node);
            } else {
                Node node = (Node) redoStack.pop();
                guiObjects.add(node);
                objects.add(node);
                undoStack.add(node);
            }
            updateParentMenu();
            initWorkspace().getParentNameBox().getItems().setAll(parents);
        } catch (Exception e) {

        }
    }

    public void zoomIn() {
        multiplier += 0.5;
        for (Node n : guiObjects) {
            n.setScaleX(Math.pow(2.0, multiplier));
            n.setScaleY(Math.pow(2.0, multiplier));

        }
    }

    public void zoomOut() {
        multiplier -= 0.5;
        for (Node n : guiObjects) {
            n.setScaleX(Math.pow(2.0, multiplier));
            n.setScaleY(Math.pow(2.0, multiplier));

        }
    }

    public void updateParentMenu() {
        parents.clear();
        for (Node n : objects) {
            if (n instanceof ClassType) {
                ClassType cT = (ClassType) n;
                CheckMenuItem parent = new CheckMenuItem(cT.getName());
                parent.setOnAction(e -> {
                    if (parent.isSelected()) {
                        setParent(parent.getText());
                    } else if (!parent.isSelected()) {
                        removeParent(parent.getText());
                    }
                });
                parents.add(parent);
            } else if (n instanceof InterfaceType) {
                InterfaceType iT = (InterfaceType) n;
                CheckMenuItem parent = new CheckMenuItem(iT.getName());
                parent.setOnAction(e -> {
                    if (parent.isSelected()) {
                        setParent(parent.getText());
                    } else if (!parent.isSelected()) {
                        removeParent(parent.getText());
                    }
                });
                parents.add(parent);
            }
        }
    }

    public void addExternalParent(String name, CheckBox isInterface) {
        if (!isInterface.isSelected()) {
            ClassType classType = new ClassType(name);
            classType.setScaleX(Math.pow(2.0, multiplier));
            classType.setScaleY(Math.pow(2.0, multiplier));
            addObject(classType);
            objects.add(classType);
            initHandlers(classType);
        } else {
            InterfaceType interfaceType = new InterfaceType(name);
            interfaceType.setScaleX(Math.pow(2.0, multiplier));
            interfaceType.setScaleY(Math.pow(2.0, multiplier));
            addObject(interfaceType);
            objects.add(interfaceType);
            initHandlers(interfaceType);
        }
        updateParentMenu();
        initWorkspace().getParentNameBox().getItems().setAll(parents);
    }

    public void setParent(String parentName) {
        if (selectedObject instanceof ClassType) {
            ClassType c = (ClassType) selectedObject;
            for (Node object : objects) {
                if (object instanceof ClassType) {
                    ClassType ob = (ClassType) object;
                    if (ob.getName().equals(parentName)) {
                        if (!c.getParents().contains(ob)) {
                            c.getParents().add(ob);
                            addSolidArrow(c, ob);
                        }
                    }
                } else if (object instanceof InterfaceType) {
                    InterfaceType ob = (InterfaceType) object;
                    if (ob.getName().equals(parentName)) {
                        if (!c.getParents().contains(ob)) {
                            c.getParents().add(ob);
                            addSolidArrow(c, ob);
                        }
                    }
                }
            }
        } else if (selectedObject instanceof InterfaceType) {
            InterfaceType i = (InterfaceType) selectedObject;
            for (Node object : objects) {
                if (object instanceof ClassType) {
                    ClassType ob = (ClassType) object;
                    if (ob.getName().equals(parentName)) {
                        if (!i.getParents().contains(ob)) {
                            i.getParents().add(ob);
                            addSolidArrow(i, ob);
                        }
                    }
                } else if (object instanceof InterfaceType) {
                    InterfaceType ob = (InterfaceType) object;
                    if (ob.getName().equals(parentName)) {
                        if (!i.getParents().contains(ob)) {
                            i.getParents().add(ob);
                            addSolidArrow(i, ob);
                        }
                    }
                }
            }
        }
    }

    public void removeParent(String parentName) {
        if (selectedObject instanceof ClassType) {
            ClassType c = (ClassType) selectedObject;
            ArrayList<Node> classParents = c.getParents();
            ArrayList<Node> parentsToRemove = new ArrayList();
            for (Node parent : classParents) {
                if (parent instanceof ClassType) {
                    ClassType p = (ClassType) parent;
                    if (p.getName().equals(parentName)) {
                        parentsToRemove.add(p);
                        removeSolidArrow(c, p);
//                        parents.remove(p);
                    }
                } else if (parent instanceof InterfaceType) {
                    InterfaceType p = (InterfaceType) parent;
                    if (p.getName().equals(parentName)) {
                        parentsToRemove.add(p);
                        removeSolidArrow(c, p);
//                        parents.remove(p);
                    }
                }
            }
            classParents.removeAll(parentsToRemove);
        } else if (selectedObject instanceof InterfaceType) {
            InterfaceType i = (InterfaceType) selectedObject;
            ArrayList<Node> interfaceParents = i.getParents();
            ArrayList<Node> parentsToRemove = new ArrayList();
            interfaceParents.stream().filter((parent) -> (parent instanceof InterfaceType)).map((parent) -> (InterfaceType) parent).filter((p) -> (p.getName().equals(parentName))).forEach((p) -> {
//                parents.remove(p);
                parentsToRemove.add(p);
                removeSolidArrow(i, p);
            });
            interfaceParents.removeAll(parentsToRemove);
        }
    }

    public void updateParentInfo(Node child) {
        parents.stream().forEach((item) -> {
            item.setSelected(false);
        });
        if (child instanceof ClassType) {
            ClassType childClass = (ClassType) child;
            ArrayList<Node> childParents = childClass.getParents();
            childParents.stream().forEach((parent) -> {
                if (parent instanceof ClassType) {
                    ClassType p = (ClassType) parent;
                    parents.stream().filter((item) -> (item.getText().equals(p.getName()))).forEach((item) -> {
                        item.setSelected(true);
                    });
                } else if (parent instanceof InterfaceType) {
                    InterfaceType p = (InterfaceType) parent;
                    parents.stream().filter((item) -> (item.getText().equals(p.getName()))).forEach((item) -> {
                        item.setSelected(true);
                    });
                }
            });
        } else {
            InterfaceType childInterface = (InterfaceType) child;
            ArrayList<Node> childParents = childInterface.getParents();
            childParents.stream().filter((parent) -> (parent instanceof InterfaceType)).map((parent) -> (InterfaceType) parent).forEach((p) -> {
                parents.stream().filter((item) -> (item.getText().equals(p.getName()))).forEach((item) -> {
                    item.setSelected(true);
                });
            });

        }

    }

    public void addVariable() {
        if (selectedObject instanceof ClassType) {
            ClassType classType = (ClassType) selectedObject;
            Variable variable = new Variable("varName" + classType.getVariables().size());
            classType.getVariables().add(variable);
            HBox variableRow = new HBox();
            CheckBox remove = new CheckBox();
            TextField varName = new TextField(variable.getName());
            TextField varType = new TextField();
            CheckBox varStatic = new CheckBox();
            TextField varAccess = new TextField();
            varName.setMaxWidth(100);
            varType.setMaxWidth(100);
            varAccess.setMaxWidth(100);
            setVariableHandlers(varName, varType, varStatic, varAccess, classType, variable);
            classType.setVariable(variable);
            classType.getVariableBox().getChildren().add(variable);
            classType.initStyle();
            variableRow.getChildren().addAll(remove, varName, varType, varStatic, varAccess);
            variableRow.getStyleClass().add("custom_variable");
            initWorkspace().getVariableTable().getChildren().add(variableRow);
        }
    }

    public void setVariableHandlers(TextField varName, TextField varType, CheckBox varStatic, TextField varAccess, ClassType classType, Variable variable) {
        varName.setOnKeyReleased(e -> {
            variable.setName(varName.getText());
            classType.setVariable(variable);
        });
        DiamondArrow dArrow = new DiamondArrow();
        varType.setOnKeyReleased(e -> {
            variable.setType(varType.getText());
            classType.setVariable(variable);
            addDiamondArrow(dArrow, variable.getType(), classType);
            if (e.getCode().equals(KeyCode.ENTER)) {
                ClassType newClass = createCustomClass(variable.getType());
                DiamondArrow newArrow = new DiamondArrow();
                if (newClass != null) {
                    addDiamondArrow(newArrow, variable.getType(), classType);
                }
            }
        });
        varStatic.setOnAction(e -> {
            if (varStatic.isSelected()) {
                variable.setStatic(true);
            } else {
                variable.setStatic(false);
            }
            classType.setVariable(variable);
        });
        varAccess.setOnKeyReleased(e -> {
            variable.setAccess(varAccess.getText());
            classType.setVariable(variable);
        });
    }

    public ClassType createCustomClass(String type) {
        ArrayList<String> objectNames = gatherObjectNames();
        ClassType classType = null;
        if (!objectNames.contains(type)) {
            classType = new ClassType(type);
            classType.setLayoutX(20.0);
            classType.setLayoutY(30.0);
            classType.setScaleX(Math.pow(2.0, multiplier));
            classType.setScaleY(Math.pow(2.0, multiplier));
            addObject(classType);
            objects.add(classType);
            initHandlers(classType);
            updateParentMenu();
            initWorkspace().getParentNameBox().getItems().setAll(parents);
        }
        return classType;
    }

    public void removeVariable() {
        ObservableList<Node> variableRows = initWorkspace().getVariableTable().getChildren();
        ArrayList<Node> toRemove = new ArrayList();
        ArrayList<Variable> varToRemove = new ArrayList();
        if (selectedObject instanceof ClassType) {
            ClassType classType = (ClassType) selectedObject;
            for (int i = 1; i < variableRows.size(); i++) {
                CheckBox remove = (CheckBox) ((HBox) (variableRows.get(i))).getChildren().get(0);
                if (remove.isSelected()) {
                    toRemove.add(variableRows.get(i));
                    varToRemove.add(classType.getVariables().get(i - 1));
                }
            }
            ArrayList<Variable> variables = classType.getVariables();
            variableRows.removeAll(toRemove);
            variables.removeAll(varToRemove);
            classType.getVariableBox().getChildren().clear();
            for (Variable variable : variables) {
                classType.addVariable(variable);
            }
        }
    }

    public void addMethod() {
        Method method = null;
        if (selectedObject instanceof ClassType) {
            ClassType classType = (ClassType) selectedObject;
            method = new Method("methName" + classType.getMethods().size());
            classType.getMethods().add(method);
        } else if (selectedObject instanceof InterfaceType) {
            InterfaceType interfaceType = (InterfaceType) selectedObject;
            method = new Method("methName" + interfaceType.getMethods().size());
            interfaceType.getMethods().add(method);
        }
        HBox methodRow = new HBox();
        CheckBox remove = new CheckBox();
        TextField methName = new TextField(method.getName());
        TextField methReturn = new TextField();
        CheckBox methStatic = new CheckBox();
        CheckBox methAbstract = new CheckBox();
        TextField methAccess = new TextField();
        methName.setMaxWidth(100);
        methReturn.setMaxWidth(100);
        methAccess.setMaxWidth(100);
        if (selectedObject instanceof ClassType) {
            ClassType classType = (ClassType) selectedObject;
            setMethodHandlers(methName, methReturn, methStatic, methAbstract, methAccess, classType, method);
            classType.setMethod(method);
            classType.getMethodBox().getChildren().add(method);
            classType.initStyle();
        } else if (selectedObject instanceof InterfaceType) {
            InterfaceType interfaceType = (InterfaceType) selectedObject;
            methStatic.setSelected(true);
            methAbstract.setSelected(true);
            method.setStatic(true);
            method.setAbstract(true);
            setMethodHandlers(methName, methReturn, methStatic, methAbstract, methAccess, interfaceType, method);
            interfaceType.setMethod(method);
            interfaceType.getMethodBox().getChildren().add(method);
            interfaceType.initStyle();
        }
        methodRow.getChildren().addAll(remove, methName, methReturn, methStatic, methAbstract, methAccess);
        methodRow.getStyleClass().add("custom_variable");
        initWorkspace().getMethodTable().getChildren().add(methodRow);
        initWorkspace().getAddArgumentBtn().setDisable(false);
        ObservableList<Node> methodRows = initWorkspace().getMethodTable().getChildren();

        HBox methodRowLabel = (HBox) methodRows.get(0);
        int length = methodRowLabel.getChildren().size() - 6;
        ArrayList<Variable> arguments = method.getParameters();
        for (Variable arg : arguments) {
            TextField argType = new TextField(arg.getType());
            argType.setMaxWidth(100);
            if (selectedObject instanceof ClassType) {
                ClassType classType = (ClassType) selectedObject;
                setArgumentHandlers(argType, arg, method, classType);
                methodRow.getChildren().add(argType);
            } else if (selectedObject instanceof InterfaceType) {
                InterfaceType interfaceType = (InterfaceType) selectedObject;
                setArgumentHandlers(argType, arg, method, interfaceType);
                methodRow.getChildren().add(argType);
            }
        }
        if (arguments.size() < length) {
            for (int i = arguments.size(); i < length; i++) {
                Variable arg = new Variable("arg" + i);
                TextField argType = new TextField();
                argType.setMaxWidth(100);
                if (selectedObject instanceof ClassType) {
                    ClassType classType = (ClassType) selectedObject;
                    setArgumentHandlers(argType, arg, method, classType);
                    methodRow.getChildren().add(argType);
                } else if (selectedObject instanceof InterfaceType) {
                    InterfaceType interfaceType = (InterfaceType) selectedObject;
                    setArgumentHandlers(argType, arg, method, interfaceType);
                    methodRow.getChildren().add(argType);
                }
            }
        }
    }

    public void setMethodHandlers(TextField methName, TextField methReturn, CheckBox methStatic, CheckBox methAbstract, TextField methAccess, Node node, Method method) {
        if (node instanceof ClassType) {
            ClassType classType = (ClassType) node;
            methName.setOnKeyReleased(e -> {
                method.setName(methName.getText());
                classType.setMethod(method);
            });
            FeatheredArrow fArrow = new FeatheredArrow();
            methReturn.setOnKeyReleased(e -> {
                method.setReturnType(methReturn.getText());
                classType.setMethod(method);
                addFeatheredLine(fArrow, method.getReturnType(), method);
                if (e.getCode().equals(KeyCode.ENTER)) {
                    FeatheredArrow newArrow = new FeatheredArrow();
                    ClassType newClass = createCustomClass(method.getReturnType());
                    if (newClass != null) {
                        addFeatheredLine(newArrow, method.getReturnType(), method);
                    }
                }
            });
            methStatic.setOnAction(e -> {
                if (methStatic.isSelected()) {
                    method.setStatic(true);
                } else {
                    method.setStatic(false);
                }
                classType.setMethod(method);
            });
            methAbstract.setOnAction(e -> {
                if (methAbstract.isSelected()) {
                    method.setAbstract(true);
                    classType.setAbstract(true);
                } else {
                    method.setAbstract(false);
                    ArrayList<Method> isAbstract = new ArrayList();
                    for (Method meth : classType.getMethods()) {
                        if (meth.getAbstract() == true) {
                            isAbstract.add(meth);
                        }
                    }
                    if (isAbstract.isEmpty()) {
                        classType.setAbstract(false);
                    }
                }
                classType.setMethod(method);
                classType.setNameText();
            });
            methAccess.setOnKeyReleased(e -> {
                method.setAccess(methAccess.getText());
                classType.setMethod(method);
            });
        } else {
            InterfaceType interfaceType = (InterfaceType) node;
            methName.setOnKeyReleased(e -> {
                method.setName(methName.getText());
                interfaceType.setMethod(method);
            });
            methReturn.setOnKeyReleased(e -> {
                method.setReturnType(methReturn.getText());
                interfaceType.setMethod(method);
            });
            methStatic.setOnAction(e -> {
                if (methStatic.isSelected()) {
                    method.setStatic(true);
                } else {
                    method.setStatic(false);
                }
                interfaceType.setMethod(method);
            });
            methAbstract.setOnAction(e -> {
                if (methAbstract.isSelected()) {
                    method.setAbstract(true);
                } else {
                    method.setAbstract(false);
                }
                interfaceType.setMethod(method);
            });
            methAccess.setOnKeyReleased(e -> {
                method.setAccess(methAccess.getText());
                interfaceType.setMethod(method);
            });
        }
    }

    public void removeMethod() {
        ObservableList<Node> methodRows = initWorkspace().getMethodTable().getChildren();
        ArrayList<Node> toRemove = new ArrayList();
        ArrayList<Method> methToRemove = new ArrayList();
        if (selectedObject instanceof ClassType) {
            ClassType classType = (ClassType) selectedObject;
            for (int i = 1; i < methodRows.size(); i++) {
                CheckBox remove = (CheckBox) ((HBox) (methodRows.get(i))).getChildren().get(0);
                if (remove.isSelected()) {
                    toRemove.add(methodRows.get(i));
                    methToRemove.add(classType.getMethods().get(i - 1));
                }
            }
            ArrayList<Method> methods = classType.getMethods();
            methodRows.removeAll(toRemove);
            methods.removeAll(methToRemove);
            classType.getMethodBox().getChildren().clear();
            for (Method method : methods) {
                classType.addMethod(method);
            }
        } else if (selectedObject instanceof InterfaceType) {
            InterfaceType interfaceType = (InterfaceType) selectedObject;
            for (int i = 1; i < methodRows.size(); i++) {
                CheckBox remove = (CheckBox) ((HBox) (methodRows.get(i))).getChildren().get(0);
                if (remove.isSelected()) {
                    toRemove.add(methodRows.get(i));
                    methToRemove.add(interfaceType.getMethods().get(i - 1));
                }
            }
            ArrayList<Method> methods = interfaceType.getMethods();
            methodRows.removeAll(toRemove);
            methods.removeAll(methToRemove);
            interfaceType.getMethodBox().getChildren().clear();
            for (Method method : methods) {
                interfaceType.addMethod(method);
            }
        }
    }

    public void setGridRendering() {
        if (initWorkspace().getGridCheckBox().isSelected()) {
            Pane designRenderer = initWorkspace().getDesignRenderer();
            ArrayList<Node> objectsToRemove = new ArrayList();
            for (Node object : guiObjects) {
                if (object instanceof Line) {
                    objectsToRemove.add(object);
                }
            }
            guiObjects.removeAll(objectsToRemove);
            Double width = designRenderer.getWidth();
            Double height = designRenderer.getHeight();
            for (int i = 35; i < width; i += 35) {
                Line line = new Line(i, 0, i, height);
                addObject(line);
            }
            for (int i = 35; i < height; i += 35) {
                Line line = new Line(0, i, width, i);
                addObject(line);
            }
        } else {
            ArrayList<Node> objectsToRemove = new ArrayList();
            for (Node object : guiObjects) {
                if (object instanceof Line) {
                    objectsToRemove.add(object);
                }
            }
            guiObjects.removeAll(objectsToRemove);
        }
    }

    public void updateNameInfo() {
        if (selectedObject != null) {
            if (selectedObject instanceof ClassType) {
                ((ClassType) (selectedObject)).setName(initWorkspace().getClassNameField().getText());
                ((ClassType) (selectedObject)).nameText.setText(initWorkspace().getClassNameField().getText());
                updateParentMenu();
                initWorkspace().getParentNameBox().getItems().setAll(parents);
            } else if (selectedObject instanceof InterfaceType) {
                ((InterfaceType) (selectedObject)).setName(initWorkspace().getClassNameField().getText());
                ((InterfaceType) (selectedObject)).nameText.setText(initWorkspace().getClassNameField().getText());
                updateParentMenu();
                initWorkspace().getParentNameBox().getItems().setAll(parents);
            }
        }
    }

    public void updatePackageInfo() {
        if (selectedObject != null) {
            if (selectedObject instanceof ClassType) {
                ((ClassType) (selectedObject)).setPackageName(initWorkspace().getPackageNameField().getText());
            } else if (selectedObject instanceof InterfaceType) {
                ((InterfaceType) (selectedObject)).setPackageName(initWorkspace().getPackageNameField().getText());
            }
        }
    }

    public void loadVariablesInfo() {
        ObservableList<Node> variableRows = initWorkspace().getVariableTable().getChildren();
        if (variableRows.size() > 2) {
            variableRows.remove(1, variableRows.size());
        } else if (variableRows.size() == 2) {
            variableRows.remove(1);
        }
        if (selectedObject instanceof ClassType) {
            ClassType classType = (ClassType) selectedObject;
            ArrayList<Variable> variables = classType.getVariables();
            for (Variable variable : variables) {
                HBox variableRow = new HBox();
                CheckBox remove = new CheckBox();
                TextField varName = new TextField(variable.getName());
                TextField varType = new TextField(variable.getType());
                CheckBox varStatic = new CheckBox();
                if (variable.getStatic() == true) {
                    varStatic.setSelected(true);
                }
                TextField varAccess = new TextField(variable.getAccess());
                varName.setMaxWidth(100);
                varType.setMaxWidth(100);
                varAccess.setMaxWidth(100);
                setVariableHandlers(varName, varType, varStatic, varAccess, classType, variable);
                variableRow.getChildren().addAll(remove, varName, varType, varStatic, varAccess);
                variableRow.getStyleClass().add("custom_variable");
                variableRows.add(variableRow);
            }
        }
    }

// TODO
    public void addAdditionalArgument(Label argLabel) {
        ObservableList<Node> methodRows = initWorkspace().getMethodTable().getChildren();
        argLabel.setText("Arg" + (((HBox) (methodRows.get(0))).getChildren().size() - 7) + "               ");
        if (selectedObject instanceof ClassType) {
            ClassType classType = (ClassType) selectedObject;
            ArrayList<Method> methods = classType.getMethods();
            if (methodRows.size() > 1) {
                for (int i = 1; i < methodRows.size(); i++) {
                    HBox methodRow = (HBox) methodRows.get(i);
                    Method method = methods.get(i - 1);
                    Variable arg = new Variable("arg" + (methodRow.getChildren().size() - 6));
                    TextField argType = new TextField();
                    argType.setMaxWidth(100);
                    setArgumentHandlers(argType, arg, method, classType);
                    methodRow.getChildren().add(argType);
                }
            }
        } else if (selectedObject instanceof InterfaceType) {
            InterfaceType interfaceType = (InterfaceType) selectedObject;
            ArrayList<Method> methods = interfaceType.getMethods();
            if (methodRows.size() > 1) {
                for (int i = 1; i < methodRows.size(); i++) {
                    HBox methodRow = (HBox) methodRows.get(i);
                    Method method = methods.get(i - 1);
                    Variable arg = new Variable("arg" + (methodRow.getChildren().size() - 6));
                    TextField argType = new TextField();
                    argType.setMaxWidth(100);
                    setArgumentHandlers(argType, arg, method, interfaceType);
                    methodRow.getChildren().add(argType);
                }
            }
        }
    }

    public void loadMethodsInfo() {
        ObservableList<Node> methodRows = initWorkspace().getMethodTable().getChildren();
        HBox methodRowLabel = (HBox) methodRows.get(0);
        int length = methodRowLabel.getChildren().size() - 6;
        if (methodRows.size() > 2) {
            methodRows.remove(1, methodRows.size());
        } else if (methodRows.size() == 2) {
            methodRows.remove(1);
        }
        ArrayList<Method> methods = null;
        if (selectedObject instanceof ClassType) {
            ClassType classType = (ClassType) selectedObject;
            methods = classType.getMethods();
        } else if (selectedObject instanceof InterfaceType) {
            InterfaceType interfaceType = (InterfaceType) selectedObject;
            methods = interfaceType.getMethods();
        }
        for (Method method : methods) {
            HBox methodRow = new HBox();
            CheckBox remove = new CheckBox();
            TextField methName = new TextField(method.getName());
            TextField methReturn = new TextField(method.getReturnType());
            CheckBox methStatic = new CheckBox();
            if (method.getStatic() == true) {
                methStatic.setSelected(true);
            }
            CheckBox methAbstract = new CheckBox();
            if (method.getAbstract() == true) {
                methAbstract.setSelected(true);
            }
            TextField methAccess = new TextField(method.getAccess());
            ArrayList<Variable> arguments = method.getParameters();
            methName.setMaxWidth(100);
            methReturn.setMaxWidth(100);
            methAccess.setMaxWidth(100);
            if (selectedObject instanceof ClassType) {
                ClassType classType = (ClassType) selectedObject;
                setMethodHandlers(methName, methReturn, methStatic, methAbstract, methAccess, classType, method);
            } else if (selectedObject instanceof InterfaceType) {
                InterfaceType interfaceType = (InterfaceType) selectedObject;
                setMethodHandlers(methName, methReturn, methStatic, methAbstract, methAccess, interfaceType, method);
            }
            methodRow.getChildren().addAll(remove, methName, methReturn, methStatic, methAbstract, methAccess);
            // TODO
            for (Variable arg : arguments) {
                TextField argType = new TextField(arg.getType());
                argType.setMaxWidth(100);
                if (selectedObject instanceof ClassType) {
                    ClassType classType = (ClassType) selectedObject;
                    setArgumentHandlers(argType, arg, method, classType);
                    methodRow.getChildren().add(argType);
                } else if (selectedObject instanceof InterfaceType) {
                    InterfaceType interfaceType = (InterfaceType) selectedObject;
                    setArgumentHandlers(argType, arg, method, interfaceType);
                    methodRow.getChildren().add(argType);
                }
            }
            if (arguments.size() < length) {
                for (int i = arguments.size(); i < length; i++) {
                    Variable arg = new Variable("arg" + i);
                    TextField argType = new TextField();
                    argType.setMaxWidth(100);
                    if (selectedObject instanceof ClassType) {
                        ClassType classType = (ClassType) selectedObject;
                        setArgumentHandlers(argType, arg, method, classType);
                        methodRow.getChildren().add(argType);
                    } else if (selectedObject instanceof InterfaceType) {
                        InterfaceType interfaceType = (InterfaceType) selectedObject;
                        setArgumentHandlers(argType, arg, method, interfaceType);
                        methodRow.getChildren().add(argType);
                    }
                }
            }
            methodRow.getStyleClass().add("custom_variable");
            methodRows.add(methodRow);

        }
    }

    public void initHandlers(Node node) {
        if (node instanceof ClassType) {
            ClassType classType = (ClassType) node;
            classType.setOnMousePressed(e -> {
                e.consume();
                if (isInState(DesignerState.SELECTING_STATE)) {
                    initX = classType.getLayoutX() - e.getSceneX();
                    initY = classType.getLayoutY() - e.getSceneY();
                    selectedObject = classType;
                    highlightSelected(selectedObject);
                    initWorkspace().reloadWorkspace();
                    initWorkspace().getClassNameField().setText(classType.getName());
                    initWorkspace().getPackageNameField().setText(classType.getPackageName());
                    updateParentInfo(classType);
                    enableParents();
                    disableSelf(classType.getName());
                    initWorkspace().getAddVariableBtn().setDisable(false);
                    initWorkspace().getRemoveVariableBtn().setDisable(false);
                    initWorkspace().getVariableTable().setDisable(false);
                    loadVariablesInfo();
                    loadMethodsInfo();
                    if (initWorkspace().getMethodTable().getChildren().size() == 1) {
                        initWorkspace().getAddArgumentBtn().setDisable(true);
                    } else {
                        initWorkspace().getAddArgumentBtn().setDisable(false);
                    }
                }

            });
            classType.setOnMouseDragged(e -> {
                e.consume();
                if (isInState(DesignerState.SELECTING_STATE)) {
                    if (e.getSceneX() + initX <= 0 && e.getSceneY() + initY >= 0) {
                        if (initWorkspace().getSnapCheckBox().isSelected()) {
                            classType.setLayoutY(((int) ((e.getSceneY() + initY) / 35)) * 35);
                        } else {
                            classType.setLayoutY(e.getSceneY() + initY);
                        }
                    } else if (e.getSceneX() + initX >= 0 && e.getSceneY() + initY <= 0) {
                        if (initWorkspace().getSnapCheckBox().isSelected()) {
                            classType.setLayoutX(((int) ((e.getSceneX() + initX) / 35)) * 35);
                        } else {
                            classType.setLayoutX(e.getSceneX() + initX);
                        }
                    } else if (e.getSceneX() + initX <= 0 && e.getSceneY() + initY <= 0) {
                        classType.setLayoutX(0.0);
                        classType.setLayoutY(0.0);
                    } else if (initWorkspace().getSnapCheckBox().isSelected()) {
                        classType.setLayoutX(((int) ((e.getSceneX() + initX) / 35)) * 35);
                        classType.setLayoutY(((int) ((e.getSceneY() + initY) / 35)) * 35);
                    } else {
                        classType.setLayoutX(e.getSceneX() + initX);
                        classType.setLayoutY(e.getSceneY() + initY);
                    }
                    ArrayList<FeatheredArrow> fArrows = classType.getFArrows();
                    for (FeatheredArrow fArrow : fArrows) {
                        Line line = fArrow.getLine();
                        if (fArrow.getSourceClass().equals(classType)) {
                            line.setStartX(classType.getLayoutX());
                            line.setStartY(classType.getLayoutY());
                            if (classType.getLayoutX() < fArrow.getTargetClass().getLayoutX()) {
                                line.setStartX(classType.getLayoutX() + classType.getWidth() - 10);
                            }
                            if (classType.getLayoutY() < fArrow.getTargetClass().getLayoutY()) {
                                line.setStartY(classType.getLayoutY() + classType.getHeight() - 10);
                            }
                        } else if (fArrow.getTargetClass().equals(classType)) {
                            fArrow.setEndCoordinates(classType.getLayoutX(), classType.getLayoutY());
                            if (classType.getLayoutX() < fArrow.getSourceClass().getLayoutX()) {
                                fArrow.setEndCoordinates(classType.getLayoutX() + classType.getWidth() - 10, classType.getLayoutY());
                            }
                            if (classType.getLayoutY() < fArrow.getSourceClass().getLayoutY()) {
                                fArrow.setEndCoordinates(classType.getLayoutX(), classType.getLayoutY() + classType.getHeight() - 10);
                            }
                        }
                    }
                    ArrayList<SolidArrow> sArrows = classType.getSArrows();
                    for (SolidArrow sArrow : sArrows) {
                        Line line = sArrow.getLine();
                        if (sArrow.getSourceClass().equals(classType)) {
                            line.setStartX(classType.getLayoutX());
                            line.setStartY(classType.getLayoutY());
                            if (classType.getLayoutX() < sArrow.getTargetClass().getLayoutX()) {
                                line.setStartX(classType.getLayoutX() + classType.getWidth() - 10);
                            }
                            if (classType.getLayoutY() < sArrow.getTargetClass().getLayoutY()) {
                                line.setStartY(classType.getLayoutY() + classType.getHeight() - 10);
                            }
                        } else if (sArrow.getTargetClass().equals(classType)) {
                            sArrow.setEndCoordinates(classType.getLayoutX(), classType.getLayoutY());
                            if (classType.getLayoutX() < sArrow.getSourceClass().getLayoutX()) {
                                sArrow.setEndCoordinates(classType.getLayoutX() + classType.getWidth() - 10, classType.getLayoutY());
                            }
                            if (classType.getLayoutY() < sArrow.getSourceClass().getLayoutY()) {
                                sArrow.setEndCoordinates(classType.getLayoutX(), classType.getLayoutY() + classType.getHeight() - 10);
                            }
                        }
                    }
                    ArrayList<DiamondArrow> dArrows = classType.getDArrows();
                    for (DiamondArrow dArrow : dArrows) {
                        Line line = dArrow.getLine();
                        if (dArrow.getSourceClass().equals(classType)) {
                            line.setStartX(classType.getLayoutX());
                            line.setStartY(classType.getLayoutY());
                            if (classType.getLayoutX() < dArrow.getTargetClass().getLayoutX()) {
                                line.setStartX(classType.getLayoutX() + classType.getWidth() - 10);
                            }
                            if (classType.getLayoutY() < dArrow.getTargetClass().getLayoutY()) {
                                line.setStartY(classType.getLayoutY() + classType.getHeight() - 10);
                            }
                        } else if (dArrow.getTargetClass().equals(classType)) {
                            dArrow.setEndCoordinates(classType.getLayoutX(), classType.getLayoutY());
                            if (classType.getLayoutX() < dArrow.getSourceClass().getLayoutX()) {
                                dArrow.setEndCoordinates(classType.getLayoutX() + classType.getWidth() - 10, classType.getLayoutY());
                            }
                            if (classType.getLayoutY() < dArrow.getSourceClass().getLayoutY()) {
                                dArrow.setEndCoordinates(classType.getLayoutX(), classType.getLayoutY() + classType.getHeight() - 10);
                            }
                        }
                    }
                } else if (isInState(DesignerState.RESIZING_STATE)) {
                    Double x = classType.getLayoutX() + classType.getWidth();
                    Double y = classType.getLayoutY() + classType.getHeight();
                    Double mouseX = e.getSceneX();
                    Double mouseY = e.getSceneY() - 145;
                    classType.setMinSize(classType.getWidth() + (mouseX - x), classType.getHeight() + (mouseY - y));
                }
            });
            classType.setOnMouseReleased(e -> {
                if (isInState(DesignerState.SELECTING_STATE)) {
                    if (initWorkspace().getGridCheckBox().isSelected()) {
                        setGridRendering();
                    }
                }
            });
            classType.setOnMouseMoved(e -> {
                if (isInState(DesignerState.RESIZING_STATE)) {
                    classType.setCursor(Cursor.NW_RESIZE);
                } else {
                    classType.setCursor(Cursor.DEFAULT);
                }
            });
        } else if (node instanceof InterfaceType) {
            InterfaceType interfaceType = (InterfaceType) node;
            interfaceType.setOnMousePressed(e -> {
                e.consume();
                if (isInState(DesignerState.SELECTING_STATE)) {
                    initX = interfaceType.getLayoutX() - e.getSceneX();
                    initY = interfaceType.getLayoutY() - e.getSceneY();
                    selectedObject = interfaceType;
                    highlightSelected(selectedObject);
                    initWorkspace().reloadWorkspace();
                    initWorkspace().getClassNameField().setText(interfaceType.getName());
                    initWorkspace().getPackageNameField().setText(interfaceType.getPackageName());
                    updateParentInfo(interfaceType);
                    enableParents();
                    disableParents();
                    disableSelf(interfaceType.getName());
                    initWorkspace().getAddVariableBtn().setDisable(true);
                    initWorkspace().getRemoveVariableBtn().setDisable(true);
                    initWorkspace().getVariableTable().setDisable(true);
                    loadMethodsInfo();
                    if (initWorkspace().getMethodTable().getChildren().size() == 1) {
                        initWorkspace().getAddArgumentBtn().setDisable(true);
                    } else {
                        initWorkspace().getAddArgumentBtn().setDisable(false);
                    }
                }
            });
            interfaceType.setOnMouseDragged(e -> {
                e.consume();
                if (isInState(DesignerState.SELECTING_STATE)) {
                    if (e.getSceneX() + initX <= 0 && e.getSceneY() + initY >= 0) {
                        if (initWorkspace().getSnapCheckBox().isSelected()) {
                            interfaceType.setLayoutY(((int) ((e.getSceneY() + initY) / 35)) * 35);
                        } else {
                            interfaceType.setLayoutY(e.getSceneY() + initY);
                        }
                    } else if (e.getSceneX() + initX >= 0 && e.getSceneY() + initY <= 0) {
                        if (initWorkspace().getSnapCheckBox().isSelected()) {
                            interfaceType.setLayoutX(((int) ((e.getSceneX() + initX) / 35)) * 35);
                        } else {
                            interfaceType.setLayoutX(e.getSceneX() + initX);
                        }
                    } else if (e.getSceneX() + initX <= 0 && e.getSceneY() + initY <= 0) {
                        interfaceType.setLayoutX(0.0);
                        interfaceType.setLayoutY(0.0);
                    } else if (initWorkspace().getSnapCheckBox().isSelected()) {
                        interfaceType.setLayoutX(((int) ((e.getSceneX() + initX) / 35)) * 35);
                        interfaceType.setLayoutY(((int) ((e.getSceneY() + initY) / 35)) * 35);
                    } else {
                        interfaceType.setLayoutX(e.getSceneX() + initX);
                        interfaceType.setLayoutY(e.getSceneY() + initY);
                    }
                    ArrayList<FeatheredArrow> fArrows = interfaceType.getFArrows();
                    for (FeatheredArrow fArrow : fArrows) {
                        Line line = fArrow.getLine();
                        if (fArrow.getSourceInterface().equals(interfaceType)) {
                            line.setStartX(interfaceType.getLayoutX());
                            line.setStartY(interfaceType.getLayoutY());
                            if (interfaceType.getLayoutX() < fArrow.getTargetClass().getLayoutX()) {
                                line.setStartX(interfaceType.getLayoutX() + interfaceType.getWidth() - 10);
                            }
                            if (interfaceType.getLayoutY() < fArrow.getTargetClass().getLayoutY()) {
                                line.setStartY(interfaceType.getLayoutY() + interfaceType.getHeight() - 10);

                            }
                        }
                    }
                    ArrayList<SolidArrow> sArrows = interfaceType.getSArrows();
                    for (SolidArrow sArrow : sArrows) {
                        Line line = sArrow.getLine();
                        if (sArrow.getSourceInterface().equals(interfaceType)) {
                            line.setStartX(interfaceType.getLayoutX());
                            line.setStartY(interfaceType.getLayoutY());
                            if (interfaceType.getLayoutX() < sArrow.getTargetClass().getLayoutX()) {
                                line.setStartX(interfaceType.getLayoutX() + interfaceType.getWidth() - 10);
                            }
                            if (interfaceType.getLayoutY() < sArrow.getTargetClass().getLayoutY()) {
                                line.setStartY(interfaceType.getLayoutY() + interfaceType.getHeight() - 10);

                            }
                        } else if (sArrow.getTargetInterface().equals(interfaceType)) {
                            sArrow.setEndCoordinates(interfaceType.getLayoutX(), interfaceType.getLayoutY());
                            if (interfaceType.getLayoutX() < sArrow.getSourceClass().getLayoutX()) {
                                sArrow.setEndCoordinates(interfaceType.getLayoutX() + interfaceType.getWidth() - 10, interfaceType.getLayoutY());
                            }
                            if (interfaceType.getLayoutY() < sArrow.getSourceClass().getLayoutY()) {
                                sArrow.setEndCoordinates(interfaceType.getLayoutX(), interfaceType.getLayoutY() + interfaceType.getHeight() - 10);
                            }
                        }
                    }
                } else if (isInState(DesignerState.RESIZING_STATE)) {
                    Double x = interfaceType.getLayoutX() + interfaceType.getWidth();
                    Double y = interfaceType.getLayoutY() + interfaceType.getHeight();
                    Double mouseX = e.getSceneX();
                    Double mouseY = e.getSceneY() - 145;
                    interfaceType.setMinSize(interfaceType.getWidth() + (mouseX - x), interfaceType.getHeight() + (mouseY - y));
                }
            });
            interfaceType.setOnMouseReleased(e -> {
                if (isInState(DesignerState.SELECTING_STATE)) {
                    if (initWorkspace().getGridCheckBox().isSelected()) {
                        setGridRendering();
                    }
                }
            });
            interfaceType.setOnMouseMoved(e -> {
                if (isInState(DesignerState.RESIZING_STATE)) {
                    interfaceType.setCursor(Cursor.NW_RESIZE);
                } else {
                    interfaceType.setCursor(Cursor.DEFAULT);
                }
            });
        }
    }

    @Override
    public void loadClasses() {
        for (Node node : objects) {
            addObject(node);
        }
    }

    @Override
    public void reset() {
        state = null;
        selectedObject = null;
        if (guiObjects != null) {
            guiObjects.clear();
        }
        if (objects != null) {
            objects.clear();
        }
        if (app != null) {
            initWorkspace().getClassNameField().clear();
            initWorkspace().getPackageNameField().clear();
        }
        multiplier = 0.0;
        undoStack.clear();
        redoStack.clear();
    }

    public void disableParents() {
        parents.stream().forEach((parent) -> {
            String name = parent.getText();
            objects.stream().filter((object) -> (object instanceof ClassType)).filter((object) -> (((ClassType) object).getName().equals(name))).forEach((item) -> {
                parent.setDisable(true);
            });
        });
    }

    public void enableParents() {
        for (CheckMenuItem parent : parents) {
            parent.setDisable(false);
        }
    }

    public void disableSelf(String name) {
        for (CheckMenuItem parent : parents) {
            if (parent.getText().equals(name)) {
                parent.setDisable(true);
            }
        }
    }

    public void highlightSelected(Node object) {
        if (object != null) {
            object.getStyleClass().add("highlight_effect");
            objects.stream().filter((n) -> (n != object)).forEach((Node n) -> {
                n.getStyleClass().clear();
            });
        }
    }

    public void unhighlightSelected(Node object) {
        if (object != null) {
            object.getStyleClass().clear();
        }
    }

    public ArrayList<String> gatherObjectNames() {
        ArrayList<String> objectNames = new ArrayList();
        for (Node object : objects) {
            if (object instanceof ClassType) {
                String name = ((ClassType) object).getName();
                objectNames.add(name);
            } else {
                String name = ((InterfaceType) object).getName();
                objectNames.add(name);
            }
        }
        return objectNames;
    }

    public ArrayList<String> gatherArgumentNames(ArrayList<Variable> args) {
        ArrayList<String> argTypes = new ArrayList();
        for (Variable arg : args) {
            argTypes.add(arg.getType());
        }
        return argTypes;
    }

    public ArrayList<String> gatherVariableNames(ArrayList<Variable> variables) {
        ArrayList<String> varTypes = new ArrayList();
        for (Variable var : variables) {
            varTypes.add(var.getType());
        }
        return varTypes;
    }

    public Node getObjectOfName(String name) {
        for (Node object : objects) {
            if (object instanceof ClassType) {
                String cName = ((ClassType) object).getName();
                if (cName.equals(name)) {
                    return object;
                }
            } else {
                String iName = ((InterfaceType) object).getName();
                if (iName.equals(name)) {
                    return object;
                }
            }
        }
        return null;
    }

    public void setArgumentHandlers(TextField argField, Variable arg, Method method, Node node) {
        ArrayList<Variable> arguments = method.getParameters();
        if (node instanceof ClassType) {
            ClassType classType = (ClassType) node;
            FeatheredArrow fArrow = new FeatheredArrow();
            argField.setOnKeyReleased(e -> {
                if (!argField.getText().equals("")) {
                    arg.setType(argField.getText());
                    classType.setMethod(method);
                    addFeatheredLine(fArrow, arg.getType(), method);
                    if (e.getCode().equals(KeyCode.ENTER)) {
                        ClassType newClass = createCustomClass(arg.getType());
                        FeatheredArrow newArrow = new FeatheredArrow();
                        if (newClass != null) {
                            addFeatheredLine(newArrow, arg.getType(), method);
                        }
                    }
                    if (!arguments.contains(arg)) {
                        arguments.add(arg);
                    }
                } else if (argField.getText().equals("") && arguments.contains(arg)) {
                    arguments.remove(arg);
                    classType.setMethod(method);
                }
            });
        } else if (node instanceof InterfaceType) {
            InterfaceType interfaceType = (InterfaceType) node;
            FeatheredArrow fArrow = new FeatheredArrow();
            argField.setOnKeyReleased(e -> {
                if (!argField.getText().equals("")) {
                    arg.setType(argField.getText());
                    interfaceType.setMethod(method);
                    addFeatheredLine(fArrow, arg.getType(), method);
                    if (!arguments.contains(arg)) {
                        arguments.add(arg);
                    }
                } else if (argField.getText().equals("") && arguments.contains(arg)) {
                    arguments.remove(arg);
                    interfaceType.setMethod(method);
                }
            });
        }
    }

    public void addDiamondArrow(DiamondArrow dArrow, String type, ClassType selectedClass) {
        try {
            ArrayList<String> objectNames = gatherObjectNames();
            if (objectNames.contains(type)) {
                Node object = getObjectOfName(type);
                ClassType sourceClass = (ClassType) object;
                dArrow.createDiamondArrow(sourceClass, selectedClass);
                if (!selectedClass.getDArrows().contains(dArrow)) {
                    selectedClass.getDArrows().add(dArrow);
                    sourceClass.getDArrows().add(dArrow);
                    addObject(dArrow.getLine());
                    addObject(dArrow.getArrow());
                }
            } else if (!objectNames.contains(type)) {
                ArrayList<DiamondArrow> dArrows = selectedClass.getDArrows();
                for (DiamondArrow dArrowed : dArrows) {
                    String sourceName = dArrowed.getSourceClass().getName();
                    if (sourceName.length() < type.length()) {
                        if (sourceName.equals(type.substring(0, type.length() - 1))) {
                            dArrow = dArrowed;
                        }
                    } else if (sourceName.length() == type.length() + 1) {
                        if (sourceName.startsWith(type)) {
                            dArrow = dArrowed;
                        }
                    }
                }
                ArrayList<String> varTypes = gatherArgumentNames(selectedClass.getVariables());
                if (!varTypes.contains(dArrow.getSourceClass().getName())) {
                    if (selectedClass.getDArrows().contains(dArrow)) {
                        ClassType sourceClass = dArrow.getSourceClass();
                        selectedClass.getDArrows().remove(dArrow);
                        sourceClass.getDArrows().remove(dArrow);
                        guiObjects.remove(dArrow.getLine());
                        guiObjects.remove(dArrow.getArrow());
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    public void addFeatheredLine(FeatheredArrow fArrow, String type, Method method) {
        try {
            ArrayList<String> objectNames = gatherObjectNames();
            if (objectNames.contains(type)) {
                Node object = getObjectOfName(type);
                ClassType targetClass = (ClassType) object;
                if (selectedObject instanceof ClassType) {
                    ClassType selectedClass = (ClassType) selectedObject;
                    fArrow.createFeatheredArrow(selectedClass, targetClass);
                    if (!selectedClass.getFArrows().contains(fArrow)) {
                        selectedClass.getFArrows().add(fArrow);
                        targetClass.getFArrows().add(fArrow);
                        addObject(fArrow.getLine());
                        addObject(fArrow.getArrow());
                    }
                } else if (selectedObject instanceof InterfaceType) {
                    InterfaceType selectedInterface = (InterfaceType) selectedObject;
                    fArrow.createFeatheredArrow(selectedInterface, targetClass);
                    if (!selectedInterface.getFArrows().contains(fArrow)) {
                        selectedInterface.getFArrows().add(fArrow);
                        targetClass.getFArrows().add(fArrow);
                        addObject(fArrow.getLine());
                        addObject(fArrow.getArrow());
                    }
                }
            } else if (!objectNames.contains(type)) {
                if (selectedObject instanceof ClassType) {
                    ClassType selectedClass = (ClassType) selectedObject;
                    ArrayList<FeatheredArrow> fArrows = selectedClass.getFArrows();
                    for (FeatheredArrow fArrowed : fArrows) {
                        String targetName = fArrowed.getTargetClass().getName();
                        if (targetName.length() < type.length()) {
                            if (targetName.equals(type.substring(0, type.length() - 1))) {
                                fArrow = fArrowed;
                            }
                        } else if (targetName.length() > type.length()) {
                            if (type.length() == targetName.length() - 1 && targetName.startsWith(type)) {
                                fArrow = fArrowed;
                            }
                        }
                    }
                    ArrayList<String> argTypes = gatherArgumentNames(method.getParameters());
                    if (!argTypes.contains(fArrow.getTargetClass().getName())) {
                        if (selectedClass.getFArrows().contains(fArrow)) {
                            ClassType targetClass = fArrow.getTargetClass();
                            selectedClass.getFArrows().remove(fArrow);
                            targetClass.getFArrows().remove(fArrow);
                            guiObjects.remove(fArrow.getLine());
                            guiObjects.remove(fArrow.getArrow());
                        }
                    }
                } else if (selectedObject instanceof InterfaceType) {
                    InterfaceType selectedInterface = (InterfaceType) selectedObject;
                    ArrayList<FeatheredArrow> fArrows = selectedInterface.getFArrows();
                    for (FeatheredArrow fArrowed : fArrows) {
                        String targetName = fArrowed.getTargetClass().getName();
                        if (targetName.length() < type.length()) {
                            if (targetName.equals(type.substring(0, type.length() - 1))) {
                                fArrow = fArrowed;
                            }
                        } else if (targetName.length() > type.length()) {
                            if (targetName.startsWith(type)) {
                                fArrow = fArrowed;
                            }
                        }
                    }
                    ArrayList<String> argTypes = gatherArgumentNames(method.getParameters());
                    if (!argTypes.contains(fArrow.getTargetClass().getName())) {
                        if (selectedInterface.getFArrows().contains(fArrow)) {
                            ClassType targetClass = fArrow.getTargetClass();
                            selectedInterface.getFArrows().remove(fArrow);
                            targetClass.getFArrows().remove(fArrow);
                            guiObjects.remove(fArrow.getLine());
                            guiObjects.remove(fArrow.getArrow());
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    public void addSolidArrow(Node child, Node parent) {
        try {
            SolidArrow sArrow = new SolidArrow();
            if (child instanceof ClassType && parent instanceof ClassType) {
                ClassType childClass = (ClassType) child;
                ClassType parentClass = (ClassType) parent;
                sArrow.createSolidArrow(childClass, parentClass);
                childClass.getSArrows().add(sArrow);
                parentClass.getSArrows().add(sArrow);
            } else if (child instanceof ClassType && parent instanceof InterfaceType) {
                ClassType childClass = (ClassType) child;
                InterfaceType parentInterface = (InterfaceType) parent;
                sArrow.createSolidArrow(childClass, parentInterface);
                childClass.getSArrows().add(sArrow);
                parentInterface.getSArrows().add(sArrow);
            } else {
                InterfaceType childInterface = (InterfaceType) child;
                InterfaceType parentInterface = (InterfaceType) parent;
                sArrow.createSolidArrow(childInterface, parentInterface);
                childInterface.getSArrows().add(sArrow);
                parentInterface.getSArrows().add(sArrow);
            }
            guiObjects.add(sArrow.getLine());
            guiObjects.add(sArrow.getArrow());
        } catch (Exception e) {

        }
    }

    public void removeSolidArrow(Node child, Node parent) {
        ArrayList<SolidArrow> sArrows = new ArrayList();
        ArrayList<SolidArrow> toRemove = new ArrayList();
        if (child instanceof ClassType && parent instanceof ClassType) {
            ClassType childClass = (ClassType) child;
            ClassType parentClass = (ClassType) parent;
            sArrows = childClass.getSArrows();
            for (SolidArrow sArrow : sArrows) {
                if (sArrow.getSourceClass().equals(childClass) && sArrow.getTargetClass().equals(parentClass)) {
                    toRemove.add(sArrow);
                    guiObjects.remove(sArrow.getLine());
                    guiObjects.remove(sArrow.getArrow());
                }
            }
            sArrows.removeAll(toRemove);
            parentClass.getSArrows().removeAll(toRemove);
        } else if (child instanceof ClassType && parent instanceof InterfaceType) {
            ClassType childClass = (ClassType) child;
            InterfaceType parentInterface = (InterfaceType) parent;
            sArrows = childClass.getSArrows();
            for (SolidArrow sArrow : sArrows) {
                if (sArrow.getSourceClass().equals(childClass) && sArrow.getTargetInterface().equals(parentInterface)) {
                    toRemove.add(sArrow);
                    guiObjects.remove(sArrow.getLine());
                    guiObjects.remove(sArrow.getArrow());
                }
            }
            sArrows.removeAll(toRemove);
            parentInterface.getSArrows().removeAll(toRemove);
        } else {
            InterfaceType childInterface = (InterfaceType) child;
            InterfaceType parentInterface = (InterfaceType) parent;
            sArrows = childInterface.getSArrows();
            for (SolidArrow sArrow : sArrows) {
                if (sArrow.getSourceInterface().equals(childInterface) && sArrow.getTargetInterface().equals(parentInterface)) {
                    toRemove.add(sArrow);
                    guiObjects.remove(sArrow.getLine());
                    guiObjects.remove(sArrow.getArrow());
                }
            }
            sArrows.removeAll(toRemove);
            parentInterface.getSArrows().removeAll(toRemove);
        }
    }

    public Node getSelectedObject() {
        return selectedObject;
    }

    public void setSelectedObjectToNull() {
        selectedObject = null;
    }

    public void setState(DesignerState initState) {
        state = initState;
    }

    public boolean isInState(DesignerState testState) {
        return state == testState;
    }

    public AppTemplate getApp() {
        return app;
    }

    public Workspace initWorkspace() {
        return workspace = (Workspace) app.getWorkspaceComponent();
    }

}
