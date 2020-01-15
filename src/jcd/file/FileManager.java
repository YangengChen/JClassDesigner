/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import jcd.data.ClassType;
import saf.components.AppDataComponent;
import saf.components.AppFileComponent;
import jcd.data.DataManager;
import jcd.data.InterfaceType;
import jcd.data.Method;
import jcd.data.Variable;
import jcd.gui.Workspace;

/**
 *
 * @author yan
 */
public class FileManager implements AppFileComponent {

    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
        DataManager dataManager = (DataManager) data;

        // NOW BUILD THE JSON OBJCTS TO SAVE
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
//        ObservableList<Node> objects = dataManager.getObjects();
        ArrayList<Node> objects = dataManager.getObjects();
        for (Node node : objects) {
            if (node instanceof ClassType) {
                JsonObject JsonClass = makeJsonClassObject(node);
                arrayBuilder.add(JsonClass);
            }
            if (node instanceof InterfaceType) {
                JsonObject JsonInterface = makeJsonInterfaceObject(node);
                arrayBuilder.add(JsonInterface);
            }

        }
        JsonArray objectsArray = arrayBuilder.build();

        // THEN PUT IT ALL TOGETHER IN A JsonObject
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add("JavaObjects", objectsArray)
                .build();

        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        StringWriter sw = new StringWriter();
        JsonWriter jsonWriter = writerFactory.createWriter(sw);
        jsonWriter.writeObject(dataManagerJSO);
        jsonWriter.close();

        // INIT THE WRITER
        if (!filePath.contains(".json")) {
            filePath += ".json";
        }
        OutputStream os = new FileOutputStream(filePath);
        JsonWriter jsonFileWriter = Json.createWriter(os);
        jsonFileWriter.writeObject(dataManagerJSO);
        String prettyPrinted = sw.toString();
        PrintWriter pw = new PrintWriter(filePath);
        pw.write(prettyPrinted);
        pw.close();
    }

    public JsonObject makeJsonClassObject(Node node) {
        ClassType classType = (ClassType) node;
        String name = classType.getName();
        String type = classType.getType();
        String access = classType.getAccess();
        boolean isAbstract = classType.getAbstract();
        String packageName = classType.getPackageName();
        String x = Double.toString(classType.getLayoutX());
        String y = Double.toString(classType.getLayoutY());
        String width = Double.toString(classType.getMinWidth());
        String height = Double.toString(classType.getMinHeight());
        ArrayList<Variable> variables = classType.getVariables();
        ArrayList<Method> methods = classType.getMethods();
        ArrayList<Node> parents = classType.getParents();
        JsonArrayBuilder variablesBuilder = Json.createArrayBuilder();
        JsonArrayBuilder methodsBuilder = Json.createArrayBuilder();
        JsonArrayBuilder parentsBuilder = Json.createArrayBuilder();
        for (Node parent : parents) {
            JsonObject JsonParent = makeJsonParentObject(parent);
            parentsBuilder.add(JsonParent);
        }
        JsonArray parentsArray = parentsBuilder.build();
        for (Variable variable : variables) {
            JsonObject JsonVariable = makeJsonVariableObject(variable);
            variablesBuilder.add(JsonVariable);
        }
        JsonArray variablesArray = variablesBuilder.build();
        for (Method method : methods) {
            JsonObject JsonMethod = makeJsonMethodObject(method);
            methodsBuilder.add(JsonMethod);
        }
        JsonArray methodsArray = methodsBuilder.build();
        JsonObject jsonClass = Json.createObjectBuilder()
                .add("type", type)
                .add("name", name)
                .add("access", access)
                .add("abstract", isAbstract)
                .add("package", packageName)
                .add("x", x)
                .add("y", y)
                .add("width", width)
                .add("height", height)
                .add("parents", parentsArray)
                .add("variables", variablesArray)
                .add("methods", methodsArray)
                .build();
        return jsonClass;
    }

    public JsonObject makeJsonParentObject(Node parent) {
        JsonObject jso = null;
        if (parent instanceof ClassType) {
            ClassType classParent = (ClassType) parent;
            jso = Json.createObjectBuilder()
                    .add("parentType", classParent.getType())
                    .add("parentName", classParent.getName())
                    .build();
        } else if (parent instanceof InterfaceType) {
            InterfaceType interfaceParent = (InterfaceType) parent;
            jso = Json.createObjectBuilder()
                    .add("parentType", interfaceParent.getType())
                    .add("parentName", interfaceParent.getName())
                    .build();
        }
        return jso;
    }

    public JsonObject makeJsonVariableObject(Variable variable) {
        JsonObject jso = Json.createObjectBuilder()
                .add("name", variable.getName())
                .add("access", variable.getAccess())
                .add("static", variable.getStatic())
                .add("final", variable.getFinal())
                .add("type", variable.getType())
                .build();
        return jso;
    }

    public JsonObject makeJsonMethodObject(Method method) {
        JsonArrayBuilder paramBuilder = Json.createArrayBuilder();
        ArrayList<Variable> param = method.getParameters();
        for (Variable p : param) {
            JsonObject pJson = makeJsonParamObject(p);
            paramBuilder.add(pJson);
        }
        JsonArray parameters = paramBuilder.build();
        JsonObject jso = Json.createObjectBuilder()
                .add("name", method.getName())
                .add("access", method.getAccess())
                .add("static", method.getStatic())
                .add("abstract", method.getAbstract())
                .add("return", method.getReturnType())
                .add("parameters", parameters)
                .build();
        return jso;
    }

    public JsonObject makeJsonParamObject(Variable param) {
        JsonObject jso = Json.createObjectBuilder()
                .add("name", param.getName())
                .add("type", param.getType())
                .build();
        return jso;
    }

    public JsonObject makeJsonInterfaceObject(Node node) {
        InterfaceType interfaceType = (InterfaceType) node;
        String name = interfaceType.getName();
        String type = interfaceType.getType();
        String access = interfaceType.getAccess();
        String packageName = interfaceType.getPackageName();
        String x = Double.toString(interfaceType.getLayoutX());
        String y = Double.toString(interfaceType.getLayoutY());
        String width = Double.toString(interfaceType.getMinWidth());
        String height = Double.toString(interfaceType.getMinHeight());
        ArrayList<Variable> variables = interfaceType.getVariables();
        ArrayList<Method> methods = interfaceType.getMethods();
        ArrayList<Node> parents = interfaceType.getParents();
        JsonArrayBuilder variablesBuilder = Json.createArrayBuilder();
        JsonArrayBuilder methodsBuilder = Json.createArrayBuilder();
        JsonArrayBuilder parentsBuilder = Json.createArrayBuilder();
        for (Node parent : parents) {
            JsonObject JsonParent = makeJsonParentObject(parent);
            parentsBuilder.add(JsonParent);
        }
        JsonArray parentsArray = parentsBuilder.build();
        for (Variable variable : variables) {
            JsonObject JsonVariable = makeJsonVariableObject(variable);
            variablesBuilder.add(JsonVariable);
        }
        JsonArray variablesArray = variablesBuilder.build();
        for (Method method : methods) {
            JsonObject JsonMethod = makeJsonMethodObject(method);
            methodsBuilder.add(JsonMethod);
        }
        JsonArray methodsArray = methodsBuilder.build();
        JsonObject jsonInterface = Json.createObjectBuilder()
                .add("type", type)
                .add("name", name)
                .add("access", access)
                .add("package", packageName)
                .add("x", x)
                .add("y", y)
                .add("width", width)
                .add("height", height)
                .add("parents", parentsArray)
                .add("variables", variablesArray)
                .add("methods", methodsArray)
                .build();
        return jsonInterface;
    }

    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
        DataManager dataManager = (DataManager) data;
        dataManager.reset();
        // LOAD THE JSON FILE WITH ALL THE DATA
        JsonObject json = loadJSONFile(filePath);

        JsonArray jsonObjectsArray = json.getJsonArray("JavaObjects");
//        ArrayList<Node> objects = new ArrayList();
        for (int i = 0; i < jsonObjectsArray.size(); i++) {
            JsonObject jsonObject = jsonObjectsArray.getJsonObject(i);
            String objectType = jsonObject.getString("type");
            String name = jsonObject.getString("name");
            String access = jsonObject.getString("access");
            String packageName = jsonObject.getString("package");
            Double x = Double.parseDouble(jsonObject.getString("x"));
            Double y = Double.parseDouble(jsonObject.getString("y"));
            Double width = Double.parseDouble(jsonObject.getString("width"));
            Double height = Double.parseDouble(jsonObject.getString("height"));
            if (objectType.equals("class")) {
                ClassType classType = new ClassType(name);
                boolean isAbstract = jsonObject.getBoolean("abstract");
                ArrayList<Variable> variables = classType.getVariables();
                ArrayList<Method> methods = classType.getMethods();
                ArrayList<Node> parents = classType.getParents();
                JsonArray jVariablesArray = jsonObject.getJsonArray("variables");
                JsonArray jMethodsArray = jsonObject.getJsonArray("methods");
                JsonArray jParentsArray = jsonObject.getJsonArray("parents");
                for (int n = 0; n < jParentsArray.size(); n++) {
                    JsonObject jsonParent = jParentsArray.getJsonObject(n);
                    String parentType = jsonParent.getString("parentType");
                    String parentName = jsonParent.getString("parentName");
                    if (parentType.equals("class")) {
                        ClassType classParent = new ClassType(parentName);
                        parents.add(classParent);
                    } else if (parentType.equals("interface")) {
                        InterfaceType interfaceParent = new InterfaceType(parentName);
                        parents.add(interfaceParent);
                    }
                }
                for (int j = 0; j < jVariablesArray.size(); j++) {
                    JsonObject jsonVariable = jVariablesArray.getJsonObject(j);
                    Variable variable = loadVariable(jsonVariable);
                    variables.add(variable);
                    classType.addVariable(variable);
                }
                for (int k = 0; k < jMethodsArray.size(); k++) {
                    JsonObject jsonMethod = jMethodsArray.getJsonObject(k);
                    Method method = loadMethod(jsonMethod);
                    methods.add(method);
                    classType.addMethod(method);
                }
                classType.setAll(access, isAbstract, packageName, x, y, width, height);
                classType.initStyle();
                classType.setNameText();
                dataManager.initHandlers(classType);
                dataManager.addOb(classType);
//                dataManager.addObject(classType);
            } else if (objectType.equals("interface")) {
                InterfaceType interfaceType = new InterfaceType(name);
                ArrayList<Variable> variables = interfaceType.getVariables();
                ArrayList<Method> methods = interfaceType.getMethods();
                ArrayList<Node> parents = interfaceType.getParents();
                JsonArray jVariablesArray = jsonObject.getJsonArray("variables");
                JsonArray jMethodsArray = jsonObject.getJsonArray("methods");
                JsonArray jParentsArray = jsonObject.getJsonArray("parents");
                for (int n = 0; n < jParentsArray.size(); n++) {
                    JsonObject jsonParent = jParentsArray.getJsonObject(n);
                    String parentType = jsonParent.getString("parentType");
                    String parentName = jsonParent.getString("parentName");
                    if (parentType.equals("class")) {
                        InterfaceType classParent = new InterfaceType(parentName);
                        parents.add(classParent);
                    } else if (parentType.equals("interface")) {
                        InterfaceType interfaceParent = new InterfaceType(parentName);
                        parents.add(interfaceParent);
                    }
                }
                for (int j = 0; j < jVariablesArray.size(); j++) {
                    JsonObject jsonVariable = jVariablesArray.getJsonObject(j);
                    Variable variable = loadVariable(jsonVariable);
                    variables.add(variable);
                    interfaceType.addVariable(variable);
                }
                for (int k = 0; k < jMethodsArray.size(); k++) {
                    JsonObject jsonMethod = jMethodsArray.getJsonObject(k);
                    Method method = loadMethod(jsonMethod);
                    methods.add(method);
                    interfaceType.addMethod(method);
                }
                interfaceType.setAll(access, packageName, x, y, width, height);
                interfaceType.initStyle();
                dataManager.initHandlers(interfaceType);
                dataManager.addOb(interfaceType);
            }   
        }
        dataManager.updateParentMenu();
        dataManager.initWorkspace().getParentNameBox().getItems().setAll(dataManager.returnParents());
    }

    public Variable loadVariable(JsonObject json) {
        String name = json.getString("name");
        Variable variable = new Variable(name);
        String type = json.getString("type");
        boolean isStatic = json.getBoolean("static");
        boolean isFinal = json.getBoolean("final");
        String access = json.getString("access");
        variable.setAll(type, isStatic, isFinal, access);
        return variable;
    }

    public Method loadMethod(JsonObject json) {
        String name = json.getString("name");
        Method method = new Method(name);
        String access = json.getString("access");
        boolean isAbstract = json.getBoolean("abstract");
        boolean isStatic = json.getBoolean("static");
        String returnType = json.getString("return");
        method.setAll(access, isStatic, isAbstract, returnType);
        JsonArray parameters = json.getJsonArray("parameters");
        ArrayList<Variable> params = method.getParameters();
        for (int i = 0; i < parameters.size(); i++) {
            JsonObject parameter = parameters.getJsonObject(i);
            Variable param = loadParameter(parameter);
            params.add(param);
        }
        return method;
    }

    public Variable loadParameter(JsonObject json) {
        String name = json.getString("name");
        Variable param = new Variable(name);
        String type = json.getString("type");
        param.setType(type);
        return param;
    }

    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
        InputStream is = new FileInputStream(jsonFilePath);
        JsonReader jsonReader = Json.createReader(is);
        JsonObject json = jsonReader.readObject();
        jsonReader.close();
        is.close();
        return json;
    }

    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        DataManager dataManager = (DataManager) data;
        //        ObservableList<Node> objects = dataManager.getObjects();
        ArrayList<Node> objects = dataManager.getObjects();
        ArrayList<String> packagesPath = new ArrayList();
        try {
            gatherPackages(objects, packagesPath);
        } catch (ClassNotFoundException ex) {
        }
        for (int i = 0; i < packagesPath.size(); i++) {
            System.out.println(filePath + "\\" + (packagesPath.get(i).replaceAll("\\.", "\\\\")));
            File newFile = new File(filePath + "\\" + packagesPath.get(i).replaceAll("\\.", "\\\\"));
            if (newFile.exists()) {
                clearDirectory(newFile);
                newFile.mkdirs();
            } else {
                newFile.mkdirs();
            }
        }
        for (Node n : objects) {
            PrintWriter pw;
            File file = null;
            if (n instanceof ClassType) {
                ClassType c = (ClassType) n;
                String className = c.getName();
                String packageName = c.getPackageName();
                String fullPath = packageName + "." + className;
                try {
                    Class.forName(fullPath);
                } catch (ClassNotFoundException e) {
                    file = new File(filePath + "\\" + c.getPackageName().replaceAll("\\.", "\\\\") + "\\" + c.getName() + ".java");
                }
            } else if (n instanceof InterfaceType) {
                InterfaceType i = (InterfaceType) n;
                String className = i.getName();
                String packageName = i.getPackageName();
                String fullPath = packageName + "." + className;
                try {
                    Class.forName(fullPath);
                } catch (ClassNotFoundException e) {
                    file = new File(filePath + "\\" + i.getPackageName().replaceAll("\\.", "\\\\") + "\\" + i.getName() + ".java");
                }
            }
            if (file != null) {
                pw = new PrintWriter(file.getPath());
                String code = generateCode(data, n);
                pw.print(code);
                pw.close();
            }
        }

    }

    public String generateCode(AppDataComponent data, Node n) {
        String code = "";
        DataManager dM = (DataManager) data;
        if (n instanceof ClassType) {
            ClassType c = (ClassType) n;
            code += "package " + c.getPackageName() + ";\n";
            String imports = generateImports(c, dM);
            code += imports;
            code += c.getAccess();
            if (c.getAbstract() == true) {
                code += " abstract class " + c.getName();
            } else {
                code += " class " + c.getName() + " ";
            }
            if (!c.getParents().isEmpty()) {
                for (Node node : c.getParents()) {
                    if (node instanceof ClassType) {
                        code += "extends " + ((ClassType) node).getName();
                    } else {
                        code += "implements " + ((InterfaceType) node).getName();
                    }
                }
            }
            code += " {\n";
            for (Variable v : c.getVariables()) {
                code += v.toString();
            }
            code += "\n";
            if (c.getAbstract() == true) {
                for (Method m : c.getMethods()) {
                    code += m.toString();
                }
            } else {
                ArrayList<Method> abstractMethods = getAbstractMethods(dM, c.getParents());
                ArrayList<String> absMethodNames = new ArrayList();
                for (Method absMethod : abstractMethods) {
                    absMethodNames.add(absMethod.getName());
                }
                for (Method meth : c.getMethods()) {
                    if (absMethodNames.contains(meth.getName())) {
                        code += "@Override\n";
                    }
                    code += meth.toString();
                }
            }
            code += "}\n";

        } else if (n instanceof InterfaceType) {
            InterfaceType i = (InterfaceType) n;
            code += "package " + i.getPackageName() + ";\n";
            String imports = generateImports(i, dM);
            code += imports;
            code += i.getAccess() + " interface " + i.getName();
            if (!i.getParents().isEmpty()) {
                for (Node node : i.getParents()) {
                    code += " implements " + ((InterfaceType) node).getName();
                }
            }
            code += " {\n";
            for (Variable v : i.getVariables()) {
                code += v.toString();
            }
            code += "\n";
            for (Method m : i.getMethods()) {
                code += m.toString();
            }
            code += "}\n";
        }
        return code;
    }
    
    public String generateImports(Node node, DataManager dataManager) {
        String imports = "";
        ArrayList<Node> objects = dataManager.getObjects();
        ArrayList<Variable> variables = new ArrayList();
        ArrayList<Method> methods = new ArrayList();
        ArrayList<Node> parents = new ArrayList();
        if (node instanceof ClassType) {
            ClassType classType = (ClassType) node;
            variables = classType.getVariables();
            methods = classType.getMethods();
            parents = classType.getParents();
        } else if (node instanceof InterfaceType) {
            InterfaceType interfaceType = (InterfaceType) node;
//            variables = interfaceType.getVariables();
            methods = interfaceType.getMethods();
            parents = interfaceType.getParents();
        }
        ArrayList<ClassType> classes = new ArrayList();
        for (Variable variable : variables) {
            String varType = variable.getType();
            for (Node object : objects) {
                if (object instanceof ClassType) {
                    ClassType c = (ClassType) object;
                    if (c.getName().equals(varType)) {
                        if (!classes.contains(c)) {
                            classes.add(c);
                        }
                    }
                }
            }
        }
        //TODO
        for (Method method : methods) {
            String methReturn = method.getReturnType();
            ArrayList<Variable> arguments = method.getParameters();
            for (Node object : objects) {
                if (object instanceof ClassType) {
                    ClassType c = (ClassType) object;
                    if (c.getName().equals(methReturn)) {
                        if (!classes.contains(c)) {
                            classes.add(c);
                        }
                    }
                } 
            }
            for (Variable argument : arguments) {
                String argType = argument.getType();
                for (Node object : objects) {
                    if (object instanceof ClassType) {
                        ClassType c = (ClassType) object;
                        if(c.getName().equals(argType)){
                            if(!classes.contains(c)){
                                classes.add(c);
                            }
                        }
                    }
                }
            }
        }
        ArrayList<InterfaceType> interfaces = new ArrayList();
        for(Node parent : parents) {
            if(parent instanceof ClassType){
                ClassType classType = (ClassType)parent;
                String className = classType.getName();
                for(Node object : objects) {
                    if(object instanceof ClassType) {
                        ClassType c = (ClassType) object;
                        if(c.getName().equals(className)){
                            if(!classes.contains(c)){
                                classes.add(c);
                            }
                        }
                    }
                }
            } else if(parent instanceof InterfaceType){
                InterfaceType interfaceType = (InterfaceType)parent;
                String interfaceName = interfaceType.getName();
                for(Node object : objects) {
                    if(object instanceof InterfaceType) {
                        InterfaceType i = (InterfaceType) object;
                        if(i.getName().equals(interfaceName)){
                            if(!interfaces.contains(i)){
                                interfaces.add(i);
                            }
                        }
                    }
                }
            }
        }
        for(ClassType c : classes) {
            String className = c.getName();
            String packageName = c.getPackageName();
            imports += "import " + packageName + "." + className + ";\n";
        }
        for(InterfaceType i : interfaces) {
            String interfaceName = i.getName();
            String packageName = i.getPackageName();
            imports += "import " + packageName + "." + interfaceName + ";\n";
        }
        imports += "\n";
        
        return imports;
    }

    public ArrayList<Method> getAbstractMethods(DataManager data, ArrayList<Node> parents) {
        ArrayList<Node> objects = data.getObjects();
        ArrayList<Method> abstractMethods = new ArrayList();
        for (int i = 0; i < parents.size(); i++) {
            if (parents.get(i) instanceof ClassType) {
                ClassType c = (ClassType) parents.get(i);
                objects.stream().filter((ob) -> (ob instanceof ClassType)).map((ob) -> (ClassType) ob).filter((o) -> (o.getName().equals(c.getName()))).filter((o) -> (o.getAbstract() == true)).forEach((o) -> {
                    for (Method meth : o.getMethods()) {
                        if (meth.getAbstract() == true) {
                            abstractMethods.add(meth);
                        }
                    }
                });
            } else {
                InterfaceType in = (InterfaceType) parents.get(i);
                objects.stream().filter((ob) -> (ob instanceof InterfaceType)).map((ob) -> (InterfaceType) ob).filter((o) -> (o.getName().equals(in.getName()))).forEach((o) -> {
                    for (Method meth : o.getMethods()) {
                        abstractMethods.add(meth);
                    }
                });
            }

        }
        return abstractMethods;
    }

    public void gatherPackages(ArrayList<Node> objects, ArrayList<String> packages) throws ClassNotFoundException {
        for (Node n : objects) {
            if (n instanceof ClassType) {
                ClassType c = (ClassType) n;
                String className = c.getName();
                String packageName = c.getPackageName();
                String fullPath = packageName + "." + className;
                try {
                    Class.forName(fullPath);
                } catch (ClassNotFoundException e) {
                    if (!packages.contains(packageName)) {
                        packages.add(packageName);
                    }
                }
            } else if (n instanceof InterfaceType) {
                InterfaceType i = (InterfaceType) n;
                String interfaceName = i.getName();
                String packageName = i.getPackageName();
                String fullPath = packageName + "." + interfaceName;
                try {
                    Class.forName(fullPath);
                } catch (ClassNotFoundException e) {
                    if (!packages.contains(packageName)) {
                        packages.add(packageName);
                    }
                }
            }
        }
    }

    public void clearDirectory(File newFile) {
        if (newFile.isDirectory()) {
            File[] files = newFile.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    clearDirectory(file);
                }
                file.delete();
            }
        }
        newFile.delete();
    }

    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
