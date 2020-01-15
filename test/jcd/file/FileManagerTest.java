/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.file;

import java.util.ArrayList;
import javafx.scene.Node;
import javax.json.JsonObject;
import jcd.data.ClassType;
import jcd.data.DataManager;
import jcd.data.InterfaceType;
import jcd.data.Method;
import jcd.data.Variable;
import jcd.test_bed.TestSave;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import saf.components.AppDataComponent;

/**
 *
 * @author yan
 */
public class FileManagerTest {

    static DataManager dm1;
    static DataManager dm2;
    static DataManager dm3;

    public FileManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        FileManager fm = new FileManager();
        dm1 = TestSave.initClass();
        dm2 = TestSave.initClass();
        dm3 = TestSave.initClass();
        fm.saveData(dm1, ".\\work\\JUnitTest1.json");
        ArrayList<Node> objects2 = dm2.getObjects();
        ClassType absClass = new ClassType("Animal");
        TestSave.addVariable(absClass, "isAPet", "public", false, false, "boolean");
        TestSave.addVariable(absClass, "owner", "public", false, false, "String");
        TestSave.addMethod(absClass, "sleep", "public", false, "void", new ArrayList<Variable>());
        TestSave.addMethod(absClass, "eat", "public", false, "void", new ArrayList<Variable>());
        TestSave.addMethod(absClass, "move", "public", false, "void", new ArrayList<Variable>());
        TestSave.addMethod(absClass, "talk", "public", false, "void", new ArrayList<Variable>());
        absClass.getMethods().get(2).setAbstract(true);
        absClass.getMethods().get(3).setAbstract(true);
        absClass.setAbstract(true);
        absClass.setLayoutX(348.0);
        absClass.setLayoutY(123.0);
        objects2.add(absClass);
        fm.saveData(dm2, ".\\work\\JUnitTest2.json");
        dm3.setObs(objects2);
        ArrayList<Node> objects3 = dm3.getObjects();
        InterfaceType interfaceType = new InterfaceType("PersistentObject");
        TestSave.addVariable2(interfaceType, "OID", "public", true, true, "ObjectID");
        ArrayList<Variable> param = new ArrayList();
        param.add(new Variable("criteria", "String"));
        TestSave.addMethod2(interfaceType, "find", "public", true, "Array", param);
        TestSave.addMethod2(interfaceType, "save", "public", true, "void", new ArrayList<Variable>());
        TestSave.addMethod2(interfaceType, "delete", "public", true, "void", new ArrayList<Variable>());
        TestSave.addMethod2(interfaceType, "retrieve", "public", true, "void", new ArrayList<Variable>());
        interfaceType.setLayoutX(456);
        interfaceType.setLayoutY(32);
        objects3.add(interfaceType);
        fm.saveData(dm3, ".\\work\\JUnitTest3.json");
    }

    /**
     * Test of loadData method, of class FileManager.
     */
    @Test
    public void testLoadData() throws Exception {
        FileManager fm = new FileManager();
        fm.loadData(dm1, ".\\work\\JUnitTest1.json");
        fm.loadData(dm2, ".\\work\\JUnitTest2.json");
        fm.loadData(dm3, ".\\work\\JUnitTest3.json");
        ArrayList<Node> obs1 = dm1.getObjects();
        ClassType tExample = (ClassType)obs1.get(0);
        ClassType dTask = (ClassType)obs1.get(1);
        ClassType cTask = (ClassType)obs1.get(2);
        ClassType sHandler = (ClassType)obs1.get(3);
        ClassType pHandler = (ClassType)obs1.get(4);
        assertEquals(cTask.getLayoutX(), 123.2, 0.0);
        assertEquals(dTask.getLayoutY(), 432.5, 0.0);
        assertEquals(tExample.getVariables().get(0).getName(), "START_TEXT");
        assertEquals(pHandler.getVariables().get(0).getType(), "ThreadExample");
        assertEquals(tExample.getMethods().get(4).getParameters().get(0).getName(), "textToAppend");
        assertEquals(tExample.getMethods().get(4).getParameters().get(0).getType(), "String");
        assertEquals(sHandler.getMethods().get(1).getParameters().get(0).getName(), "event");
        assertEquals(sHandler.getMethods().get(1).getParameters().get(0).getType(), "Event");
        assertEquals(cTask.getMethods().get(1).getAccess(), "protected");
        assertEquals(((ClassType)dTask.getParents().get(0)).getName(), "Task");
        ArrayList<Node> obs2 = dm2.getObjects();
        ClassType tExample2 = (ClassType)obs2.get(0);
        ClassType dTask2 = (ClassType)obs2.get(1);
        ClassType cTask2 = (ClassType)obs2.get(2);
        ClassType sHandler2 = (ClassType)obs2.get(3);
        ClassType pHandler2 = (ClassType)obs2.get(4);
        ClassType absClass = (ClassType)obs2.get(8);
        assertEquals(tExample2.getLayoutX(), 400.0, 0.0);
        assertEquals(sHandler2.getLayoutY(), 924.0, 0.0);
        assertEquals(tExample2.getMethods().get(5).getParameters().get(0).getName(), "timeToSleep");
        assertEquals(tExample2.getMethods().get(5).getParameters().get(0).getType(), "int");
        assertEquals(pHandler2.getMethods().get(1).getParameters().get(0).getName(), "event");
        assertEquals(pHandler2.getMethods().get(1).getParameters().get(0).getType(), "Event");
        assertEquals(absClass.getMethods().get(3).getAbstract(), true);
        assertEquals(absClass.getMethods().get(1).getAbstract(), false);
        assertEquals(absClass.getMethods().get(0).getName(), "sleep" );
        assertEquals(absClass.getAbstract(), true);
        ArrayList<Node> obs3 = dm3.getObjects();
        ClassType tExample3 = (ClassType)obs3.get(0);
        ClassType dTask3 = (ClassType)obs3.get(1);
        ClassType cTask3 = (ClassType)obs3.get(2);
        ClassType sHandler3 = (ClassType)obs3.get(3);
        ClassType pHandler3 = (ClassType)obs3.get(4);
        ClassType absClass2 = (ClassType)obs3.get(8);
        InterfaceType interfaceType = (InterfaceType)obs3.get(9);
         assertEquals(absClass.getLayoutX(), 348.0, 0.0);
        assertEquals(tExample3.getLayoutY(), 600.0, 0.0);
        assertEquals(tExample3.getMethods().get(8).getParameters().get(0).getName(), "initPrimaryStage");
        assertEquals(tExample3.getMethods().get(8).getParameters().get(0).getType(), "Stage");
        assertEquals(absClass2.getMethods().get(2).getAbstract(), true);
        assertEquals(absClass2.getMethods().get(1).getAbstract(), false);
        assertEquals(absClass2.getMethods().get(1).getName(), "eat" );
        assertEquals(absClass2.getAbstract(), true);
        assertEquals(interfaceType.getType(), "interface");
        assertEquals(interfaceType.getMethods().get(0).getAbstract(), true);
        
        
    }
}
