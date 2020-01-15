/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.test_bed;

import java.util.ArrayList;
import javafx.collections.ObservableList;
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
public class TestSave {

    public static void main(String[] args) throws Exception {

        ClassType ThreadExample = new ClassType("ThreadExample");
        ClassType DateTask = new ClassType("DateTask");
        ClassType CounterTask = new ClassType("CounterTask");
        ClassType StartHandler = new ClassType("StartHandler");
        ClassType PauseHandler = new ClassType("PauseHandler");

        addVariable(ThreadExample, "START_TEXT", "public", true, true, "String");
        addVariable(ThreadExample, "PAUSE_TEXT", "public", true, true, "String");
        addVariable(ThreadExample, "window", "private", false, false, "Stage");
        addVariable(ThreadExample, "appPane", "private", false, false, "BorderPane");
        addVariable(ThreadExample, "topPane", "private", false, false, "FlowPane");
        addVariable(ThreadExample, "startButton", "private", false, false, "Button");
        addVariable(ThreadExample, "pauseButton", "private", false, false, "Button");
        addVariable(ThreadExample, "scrollPane", "private", false, false, "ScrollPane");
        addVariable(ThreadExample, "textArea", "private", false, false, "TextArea");
        addVariable(ThreadExample, "dateThread", "private", false, false, "Thread");
        addVariable(ThreadExample, "dateTask", "private", false, false, "Task");
        addVariable(ThreadExample, "counterThread", "private", false, false, "Thread");
        addVariable(ThreadExample, "counter", "private", false, false, "Task");
        addVariable(ThreadExample, "work", "private", false, false, "boolean");
        ArrayList<Variable> param = new ArrayList();
        param.add(new Variable("primaryStage", "Stage"));
        addMethod(ThreadExample, "start", "public", false, "void", param);
        addMethod(ThreadExample, "startWork", "public", false, "void", new ArrayList<>());
        addMethod(ThreadExample, "pauseWork", "public", false, "void", new ArrayList<>());
        addMethod(ThreadExample, "doWork", "public", false, "boolean", new ArrayList<>());
        ArrayList<Variable> p = new ArrayList();
        p.add(new Variable("textToAppend", "String"));
        addMethod(ThreadExample, "appendText", "public", false, "void", p);
        ArrayList<Variable> pa = new ArrayList();
        pa.add(new Variable("timeToSleep", "int"));
        addMethod(ThreadExample, "sleep", "public", false, "void", pa);
        addMethod(ThreadExample, "initLayout", "private", false, "void", new ArrayList<>());
        addMethod(ThreadExample, "initHandlers", "private", false, "void", new ArrayList<>());
        ArrayList<Variable> pe = new ArrayList();
        pe.add(new Variable("initPrimaryStage", "Stage"));
        addMethod(ThreadExample, "initWindow", "private", false, "void", pe);
        addMethod(ThreadExample, "initThreads", "private", false, "void", new ArrayList<>());
        ArrayList<Node> parent = ThreadExample.getParents();
        ClassType application = new ClassType("Application");
        parent.add(application);
        ArrayList<Variable> pd = new ArrayList();
        pd.add(new Variable("args", "String[]"));
        addMethod(ThreadExample, "main", "public", true, "void", pd);
        AppTemplate app = null;
        DataManager dataManager = new DataManager(app);
        ThreadExample.setAll("public", false, "default_package", 400.0, 600.0, 150.0, 50.0);
        addVariable(DateTask, "app", "private", false, false, "ThreadExample");
        addVariable(DateTask, "now", "private", false, false, "Date");
        ArrayList<Node> parent2 = DateTask.getParents();
        parent2.add(new ClassType("Task"));
        ArrayList<Node> parent3 = CounterTask.getParents();
        parent3.add(new ClassType("Task"));
        ArrayList<Node> parent4 = StartHandler.getParents();
        parent4.add(new InterfaceType("EventHandler"));
        ArrayList<Node> parent5 = PauseHandler.getParents();
        parent5.add(new InterfaceType("EventHandler"));
        ArrayList<Variable> par = new ArrayList();
        par.add(new Variable("initApp", "ThreadExample"));
        addMethod(DateTask, "DateTask", "public", false, "", par);
        addMethod(DateTask, "call", "protected", false, "Void", new ArrayList<>());
        addVariable(StartHandler, "app", "private", false, false, "ThreadExample");
        ArrayList<Variable> parb = new ArrayList();
        parb.add(new Variable("initApp", "ThreadExample"));
        addMethod(StartHandler, "StartHandler", "public", false, "", parb);
        ArrayList<Variable> pare = new ArrayList();
        pare.add(new Variable("event", "Event"));
        addMethod(StartHandler, "handle", "public", false, "void", pare);
        addVariable(PauseHandler, "app", "private", false, false, "ThreadExample");
        ArrayList<Variable> parbe = new ArrayList();
        parbe.add(new Variable("initApp", "ThreadExample"));
        addMethod(PauseHandler, "PauseHandler", "public", false, "", parbe);
        ArrayList<Variable> pared = new ArrayList();
        pared.add(new Variable("event", "Event"));
        addMethod(PauseHandler, "handle", "public", false, "void", pared);
        addVariable(CounterTask, "app", "private", false, false, "ThreadExample");
        addVariable(CounterTask, "counter", "private", false, false, "int");
        ArrayList<Variable> pars = new ArrayList();
        pars.add(new Variable("initApp", "ThreadExample"));
        addMethod(CounterTask, "CounterTask", "public", false, "", pars);
        addMethod(CounterTask, "call", "protected", false, "Void", new ArrayList<>());
        ClassType Application = new ClassType("Application");
        Application.setAll("public", true, "default_package", 0.0, 0.0, 150.0, 50.0);
        ArrayList<Variable> pars2 = new ArrayList();
        pars2.add(new Variable("primaryStage", "Stage"));
        Method absMeth = new Method("start");
        absMeth.setAll("public", false, true, "void");
        absMeth.setParameters(pars2);
        Application.getMethods().add(absMeth);
        Method handle = new Method("handle");
        handle.setAll("public", false, true, "void");
        handle.getParameters().add(new Variable("event", "Event"));
        InterfaceType EventHandler = new InterfaceType("EventHandler");
        ClassType Task = new ClassType("Task");
        Task.setAbstract(true);
        Method call = new Method("call");
        call.setAll("", false, true, "Void");
        Task.getMethods().add(call);
        Task.setAbstract(true);
        EventHandler.getMethods().add(handle);
        dataManager.getObjects().add(ThreadExample);
        dataManager.getObjects().add(DateTask);
        dataManager.getObjects().add(CounterTask);
        dataManager.getObjects().add(StartHandler);
        dataManager.getObjects().add(PauseHandler);
        dataManager.getObjects().add(Application);
        dataManager.getObjects().add(EventHandler);
        dataManager.getObjects().add(Task);
        DateTask.setAll("public", false, "default_package", 323.0, 432.5, 150.0, 50.0);
        CounterTask.setAll("public", false, "default_package", 123.2, 602.5, 150.0, 50.0);
        StartHandler.setAll("public", false, "default_package", 312.2, 924.0, 150.0, 50.0);
        PauseHandler.setAll("public", false, "default_package", 753.3, 321.0, 150.0, 50.0);
        FileManager fileManager = new FileManager();
        fileManager.saveData(dataManager, ".\\work\\DesignSaveTest.json");

    }

    public static void addVariable(ClassType c, String variableName, String access, boolean b, boolean f, String type) {
        Variable variable = new Variable(variableName);
        variable.setAccess(access);
        variable.setStatic(b);
        variable.setFinal(f);
        variable.setType(type);
        c.getVariables().add(variable);
    }

    public static void addMethod(ClassType c, String methodName, String access, boolean b, String ret, ArrayList<Variable> param) {
        Method method = new Method(methodName);
        method.setAccess(access);
        method.setStatic(b);
        method.setReturnType(ret);
        method.setParameters(param);
        c.getMethods().add(method);
    }

    public static void addVariable2(InterfaceType i, String variableName, String access, boolean b, boolean f, String type) {
        Variable variable = new Variable(variableName);
        variable.setAccess(access);
        variable.setStatic(b);
        variable.setFinal(f);
        variable.setType(type);
        i.getVariables().add(variable);
    }

    public static void addMethod2(InterfaceType i, String methodName, String access, boolean b, String ret, ArrayList<Variable> param) {
        Method method = new Method(methodName);
        method.setAccess(access);
        method.setAbstract(b);
        method.setReturnType(ret);
        method.setParameters(param);
        i.getMethods().add(method);
    }

    public static DataManager initClass() throws Exception {
        ClassType ThreadExample = new ClassType("ThreadExample");
        ClassType DateTask = new ClassType("DateTask");
        ClassType CounterTask = new ClassType("CounterTask");
        ClassType StartHandler = new ClassType("StartHandler");
        ClassType PauseHandler = new ClassType("PauseHandler");

       addVariable(ThreadExample, "START_TEXT", "public", true, true, "String");
        addVariable(ThreadExample, "PAUSE_TEXT", "public", true, true, "String");
        addVariable(ThreadExample, "window", "private", false, false, "Stage");
        addVariable(ThreadExample, "appPane", "private", false, false, "BorderPane");
        addVariable(ThreadExample, "topPane", "private", false, false, "FlowPane");
        addVariable(ThreadExample, "startButton", "private", false, false, "Button");
        addVariable(ThreadExample, "pauseButton", "private", false, false, "Button");
        addVariable(ThreadExample, "scrollPane", "private", false, false, "ScrollPane");
        addVariable(ThreadExample, "textArea", "private", false, false, "TextArea");
        addVariable(ThreadExample, "dateThread", "private", false, false, "Thread");
        addVariable(ThreadExample, "dateTask", "private", false, false, "Task");
        addVariable(ThreadExample, "counterThread", "private", false, false, "Thread");
        addVariable(ThreadExample, "counter", "private", false, false, "Task");
        addVariable(ThreadExample, "work", "private", false, false, "boolean");
        ArrayList<Variable> param = new ArrayList();
        param.add(new Variable("primaryStage", "Stage"));
        addMethod(ThreadExample, "start", "public", false, "void", param);
        addMethod(ThreadExample, "startWork", "public", false, "void", new ArrayList<>());
        addMethod(ThreadExample, "pauseWork", "public", false, "void", new ArrayList<>());
        addMethod(ThreadExample, "doWork", "public", false, "boolean", new ArrayList<>());
        ArrayList<Variable> p = new ArrayList();
        p.add(new Variable("textToAppend", "String"));
        addMethod(ThreadExample, "appendText", "public", false, "void", p);
        ArrayList<Variable> pa = new ArrayList();
        pa.add(new Variable("timeToSleep", "int"));
        addMethod(ThreadExample, "sleep", "public", false, "void", pa);
        addMethod(ThreadExample, "initLayout", "private", false, "void", new ArrayList<>());
        addMethod(ThreadExample, "initHandlers", "private", false, "void", new ArrayList<>());
        ArrayList<Variable> pe = new ArrayList();
        pe.add(new Variable("initPrimaryStage", "Stage"));
        addMethod(ThreadExample, "initWindow", "private", false, "void", pe);
        addMethod(ThreadExample, "initThreads", "private", false, "void", new ArrayList<>());
        ArrayList<Node> parent = ThreadExample.getParents();
        ClassType application = new ClassType("Application");
        parent.add(application);
        ArrayList<Variable> pd = new ArrayList();
        pd.add(new Variable("args", "String[]"));
        addMethod(ThreadExample, "main", "public", true, "void", pd);
        AppTemplate app = null;
        DataManager dataManager = new DataManager(app);
        ThreadExample.setAll("public", false, "default_package", 400.0, 600.0, 150.0, 50.0);
        addVariable(DateTask, "app", "private", false, false, "ThreadExample");
        addVariable(DateTask, "now", "private", false, false, "Date");
        ArrayList<Node> parent2 = DateTask.getParents();
        parent2.add(new ClassType("Task"));
        ArrayList<Node> parent3 = CounterTask.getParents();
        parent3.add(new ClassType("Task"));
        ArrayList<Node> parent4 = StartHandler.getParents();
        parent4.add(new InterfaceType("EventHandler"));
        ArrayList<Node> parent5 = PauseHandler.getParents();
        parent5.add(new InterfaceType("EventHandler"));
        ArrayList<Variable> par = new ArrayList();
        par.add(new Variable("initApp", "ThreadExample"));
        addMethod(DateTask, "DateTask", "public", false, "", par);
        addMethod(DateTask, "call", "protected", false, "Void", new ArrayList<>());
        addVariable(StartHandler, "app", "private", false, false, "ThreadExample");
        ArrayList<Variable> parb = new ArrayList();
        parb.add(new Variable("initApp", "ThreadExample"));
        addMethod(StartHandler, "StartHandler", "public", false, "", parb);
        ArrayList<Variable> pare = new ArrayList();
        pare.add(new Variable("event", "Event"));
        addMethod(StartHandler, "handle", "public", false, "void", pare);
        addVariable(PauseHandler, "app", "private", false, false, "ThreadExample");
        ArrayList<Variable> parbe = new ArrayList();
        parbe.add(new Variable("initApp", "ThreadExample"));
        addMethod(PauseHandler, "PauseHandler", "public", false, "", parbe);
        ArrayList<Variable> pared = new ArrayList();
        pared.add(new Variable("event", "Event"));
        addMethod(PauseHandler, "handle", "public", false, "void", pared);
        addVariable(CounterTask, "app", "private", false, false, "ThreadExample");
        addVariable(CounterTask, "counter", "private", false, false, "int");
        ArrayList<Variable> pars = new ArrayList();
        pars.add(new Variable("initApp", "ThreadExample"));
        addMethod(CounterTask, "CounterTask", "public", false, "", pars);
        addMethod(CounterTask, "call", "protected", false, "Void", new ArrayList<>());
        ClassType Application = new ClassType("Application");
        Application.setAll("public", true, "default_package", 0.0, 0.0, 150.0, 50.0);
        ArrayList<Variable> pars2 = new ArrayList();
        pars2.add(new Variable("primaryStage", "Stage"));
        Method absMeth = new Method("start");
        absMeth.setAll("public", false, true, "void");
        absMeth.setParameters(pars2);
        Application.getMethods().add(absMeth);
        Method handle = new Method("handle");
        handle.setAll("public", false, true, "void");
        handle.getParameters().add(new Variable("event", "Event"));
        InterfaceType EventHandler = new InterfaceType("EventHandler");
        ClassType Task = new ClassType("Task");
        Method call = new Method("call");
        call.setAll("", false, true, "Void");
        Task.getMethods().add(call);
        Task.setAbstract(true);
        EventHandler.getMethods().add(handle);
        dataManager.getObjects().add(ThreadExample);
        dataManager.getObjects().add(DateTask);
        dataManager.getObjects().add(CounterTask);
        dataManager.getObjects().add(StartHandler);
        dataManager.getObjects().add(PauseHandler);
        dataManager.getObjects().add(Application);
        dataManager.getObjects().add(EventHandler);
        dataManager.getObjects().add(Task);
        DateTask.setAll("public", false, "default_package", 323.0, 432.5, 150.0, 50.0);
        CounterTask.setAll("public", false, "default_package", 123.2, 602.5, 150.0, 50.0);
        StartHandler.setAll("public", false, "default_package", 312.2, 924.0, 150.0, 50.0);
        PauseHandler.setAll("public", false, "default_package", 753.3, 321.0, 150.0, 50.0);
        FileManager fileManager = new FileManager();
        fileManager.saveData(dataManager, ".\\work\\DesignSaveTest.json");
        return dataManager;
    }
}
