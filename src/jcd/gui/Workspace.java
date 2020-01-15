/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import static jcd.PropertyType.ADD_CLASS_BUTTON_ICON;
import static jcd.PropertyType.ADD_CLASS_BUTTON_TOOLTIP;
import static jcd.PropertyType.ADD_INTERFACE_BUTTON_ICON;
import static jcd.PropertyType.ADD_INTERFACE_BUTTON_TOOLTIP;
import static jcd.PropertyType.ADD_METHOD_ICON;
import static jcd.PropertyType.ADD_METHOD_TOOLTIP;
import static jcd.PropertyType.ADD_VARIABLE_ICON;
import static jcd.PropertyType.ADD_VARIABLE_TOOLTIP;
import static jcd.PropertyType.REDO_BUTTON_ICON;
import static jcd.PropertyType.REDO_BUTTON_TOOLTIP;
import static jcd.PropertyType.REMOVE_BUTTON_ICON;
import static jcd.PropertyType.REMOVE_BUTTON_TOOLTIP;
import static jcd.PropertyType.REMOVE_METHOD_ICON;
import static jcd.PropertyType.REMOVE_METHOD_TOOLTIP;
import static jcd.PropertyType.REMOVE_VARIABLE_ICON;
import static jcd.PropertyType.REMOVE_VARIABLE_TOOLTIP;
import static jcd.PropertyType.RESIZE_BUTTON_ICON;
import static jcd.PropertyType.RESIZE_BUTTON_TOOLTIP;
import static jcd.PropertyType.SELECT_BUTTON_ICON;
import static jcd.PropertyType.SELECT_BUTTON_TOOLTIP;
import static jcd.PropertyType.UNDO_BUTTON_ICON;
import static jcd.PropertyType.UNDO_BUTTON_TOOLTIP;
import static jcd.PropertyType.ZOOM_IN_BUTTON_ICON;
import static jcd.PropertyType.ZOOM_IN_BUTTON_TOOLTIP;
import static jcd.PropertyType.ZOOM_OUT_BUTTON_ICON;
import static jcd.PropertyType.ZOOM_OUT_BUTTON_TOOLTIP;
import jcd.controller.DesignRendererController;
import jcd.controller.PageEditController;
import jcd.data.DataManager;
import jcd.data.DesignerState;
import saf.AppTemplate;
import saf.components.AppWorkspaceComponent;
import saf.ui.AppGUI;

/**
 *
 * @author yan
 */
public class Workspace extends AppWorkspaceComponent {

    // HERE'S THE APP
    AppTemplate app;

    // IT KNOWS THE GUI IT IS PLACED INSIDE
    AppGUI gui;
    DataManager dataManager;

    PageEditController pageEditController;
    DesignRendererController designRendererController;

    BorderPane toolbar;
    BorderPane jClassDesignerPane;
    ScrollPane designRendererScrollPane;
    Pane designRenderer;
    HBox editToolbar;
    HBox viewToolbar;

    Button selectBtn;
    Button resizeBtn;
    Button addClassBtn;
    Button addInterfaceBtn;
    Button removeBtn;
    Button undoBtn;
    Button redoBtn;
    Button zoomInBtn;
    Button zoomOutBtn;
    Button isClickedBtn;

    VBox checkBoxes;
    CheckBox gridCheckBox;
    CheckBox snapCheckBox;

    VBox componentToolbar;

    HBox classSection;
    Label className;
    TextField classNameField;

    HBox packageSection;
    Label packageName;
    TextField packageTextField;

    HBox parentSection;
    Label parentName;
    SplitMenuButton parentClass;

    HBox variableSection;
    Label variableLabel;
    Button variableAddBtn;
    Button variableRemoveBtn;
    VBox variableTable;
    HBox variableLabelRow;
    Label variableRemove;
    Label variableName;
    Label variableType;
    Label variableStatic;
    Label variableAccess;
    ScrollPane variableScrollPane;

    BorderPane methodRegion;
    HBox methodSection;
    Label methodLabel;
    Button methodAddBtn;
    Button methodRemoveBtn;
    Button argumentAddBtn;
    VBox methodTable;
    HBox methodLabelRow;
    Label methodRemove;
    Label methodName;
    Label methodReturn;
    Label methodStatic;
    Label methodAbstract;
    Label methodAccess;
    ScrollPane methodScrollPane;

    public Workspace(AppTemplate initApp) {
        app = initApp;
        gui = app.getGUI();
        dataManager = (DataManager) app.getDataComponent();
        layoutGUI();
        setupHandlers();
    }

    public Pane getDesignRenderer() {
        return designRenderer;
    }

    public VBox getComponentToolbar() {
        return componentToolbar;
    }

    public TextField getClassNameField() {
        return classNameField;
    }

    public TextField getPackageNameField() {
        return packageTextField;
    }

    public SplitMenuButton getParentNameBox() {
        return parentClass;
    }

    public Button getRemoveButton() {
        return removeBtn;
    }

    public CheckBox getGridCheckBox() {
        return gridCheckBox;
    }

    public CheckBox getSnapCheckBox() {
        return snapCheckBox;
    }

    public VBox getVariableTable() {
        return variableTable;
    }

    public VBox getMethodTable() {
        return methodTable;
    }

    public Button getAddVariableBtn() {
        return variableAddBtn;
    }

    public Button getRemoveVariableBtn() {
        return variableRemoveBtn;
    }

    public Button getAddArgumentBtn() {
        return argumentAddBtn;
    }
    
    public Button getRedoBtn() {
        return redoBtn;
    }
    
    public Button getUndoBtn() {
        return undoBtn;
    }

    private void layoutGUI() {
        jClassDesignerPane = gui.getAppPane();
        toolbar = (BorderPane) gui.getAppPane().getTop();

        //make editToolbar
        editToolbar = new HBox();
        selectBtn = gui.initChildButton(editToolbar, SELECT_BUTTON_ICON.toString(), SELECT_BUTTON_TOOLTIP.toString(), false);
        resizeBtn = gui.initChildButton(editToolbar, RESIZE_BUTTON_ICON.toString(), RESIZE_BUTTON_TOOLTIP.toString(), false);
        addClassBtn = gui.initChildButton(editToolbar, ADD_CLASS_BUTTON_ICON.toString(), ADD_CLASS_BUTTON_TOOLTIP.toString(), false);
        addInterfaceBtn = gui.initChildButton(editToolbar, ADD_INTERFACE_BUTTON_ICON.toString(), ADD_INTERFACE_BUTTON_TOOLTIP.toString(), false);
        removeBtn = gui.initChildButton(editToolbar, REMOVE_BUTTON_ICON.toString(), REMOVE_BUTTON_TOOLTIP.toString(), true);
        undoBtn = gui.initChildButton(editToolbar, UNDO_BUTTON_ICON.toString(), UNDO_BUTTON_TOOLTIP.toString(), true);
        redoBtn = gui.initChildButton(editToolbar, REDO_BUTTON_ICON.toString(), REDO_BUTTON_TOOLTIP.toString(), true);

        viewToolbar = new HBox();
        zoomInBtn = gui.initChildButton(viewToolbar, ZOOM_IN_BUTTON_ICON.toString(), ZOOM_IN_BUTTON_TOOLTIP.toString(), false);
        zoomOutBtn = gui.initChildButton(viewToolbar, ZOOM_OUT_BUTTON_ICON.toString(), ZOOM_OUT_BUTTON_TOOLTIP.toString(), false);

        checkBoxes = new VBox();
        gridCheckBox = new CheckBox("Grid");
        snapCheckBox = new CheckBox("Snap");
        checkBoxes.getChildren().addAll(gridCheckBox, snapCheckBox);
        viewToolbar.getChildren().add(checkBoxes);

        classSection = new HBox();
        className = new Label("Class Name: ");
        classNameField = new TextField();
        classSection.getChildren().addAll(className, classNameField);
        classSection.setAlignment(Pos.CENTER_LEFT);

        packageSection = new HBox();
        packageName = new Label("Package:      ");
        packageTextField = new TextField();
        packageSection.getChildren().addAll(packageName, packageTextField);

        parentSection = new HBox();
        parentName = new Label("Parent:         ");
        parentClass = new SplitMenuButton();
        parentClass.setText("Add External Class");
        parentSection.getChildren().addAll(parentName, parentClass);

        variableSection = new HBox();
        variableLabel = new Label("Variables:    ");
        variableSection.getChildren().add(variableLabel);
        variableAddBtn = gui.initChildButton(variableSection, ADD_VARIABLE_ICON.toString(), ADD_VARIABLE_TOOLTIP.toString(), false);
        variableRemoveBtn = gui.initChildButton(variableSection, REMOVE_VARIABLE_ICON.toString(), REMOVE_VARIABLE_TOOLTIP.toString(), false);
        variableSection.setAlignment(Pos.CENTER_LEFT);

        methodRegion = new BorderPane();
        methodSection = new HBox();
        methodLabel = new Label("Methods:    ");
        methodSection.getChildren().add(methodLabel);
        methodAddBtn = gui.initChildButton(methodSection, ADD_METHOD_ICON.toString(), ADD_METHOD_TOOLTIP.toString(), false);
        methodRemoveBtn = gui.initChildButton(methodSection, REMOVE_METHOD_ICON.toString(), REMOVE_METHOD_TOOLTIP.toString(), false);
        methodSection.setAlignment(Pos.CENTER_LEFT);
        methodRegion.setLeft(methodSection);
        argumentAddBtn = new Button("+Arg");
        argumentAddBtn.setAlignment(Pos.CENTER_RIGHT);
        methodRegion.setRight(argumentAddBtn);

        variableTable = new VBox();
        variableLabelRow = new HBox();
        variableRemove = new Label("Remove?  ");
        variableName = new Label("Name        ");
        variableType = new Label("Type              ");
        variableStatic = new Label("Static     ");
        variableAccess = new Label("Access        ");
        variableLabelRow.getChildren().addAll(variableRemove, variableName, variableType, variableStatic, variableAccess);
        variableTable.getChildren().add(variableLabelRow);
        variableLabelRow.setMinWidth(400);
        variableTable.setMinSize(400, 400);

        methodTable = new VBox();
        methodLabelRow = new HBox();
        methodRemove = new Label("Remove?  ");
        methodName = new Label("Name        ");
        methodReturn = new Label("Return           ");
        methodStatic = new Label("Static   ");
        methodAbstract = new Label("Abstract   ");
        methodAccess = new Label("Access          ");
        methodLabelRow.getChildren().addAll(methodRemove, methodName, methodReturn, methodStatic, methodAbstract, methodAccess);
        methodTable.getChildren().add(methodLabelRow);
        methodLabelRow.setMinWidth(500);
        methodTable.setMinSize(600, 400);

        variableScrollPane = new ScrollPane();
        methodScrollPane = new ScrollPane();
        variableScrollPane.setPrefSize(500, 300);
        methodScrollPane.setPrefSize(500, 300);
        variableScrollPane.setContent(variableTable);
        methodScrollPane.setContent(methodTable);

        componentToolbar = new VBox();
        designRendererScrollPane = new ScrollPane();
        designRenderer = new Pane();
        designRenderer.setMinSize(1458, 820);
        designRendererScrollPane.setContent(designRenderer);
        componentToolbar.getChildren().addAll(classSection, packageSection, parentSection, variableSection, variableScrollPane, methodRegion, methodScrollPane);

        toolbar.setCenter(editToolbar);
        toolbar.setRight(viewToolbar);
        setWorkspace(new BorderPane());
        ((BorderPane) workspace).setCenter(designRendererScrollPane);
        ((BorderPane) workspace).setRight(componentToolbar);

        dataManager.setObjects(designRenderer.getChildren());
    }

    public void setupHandlers() {
        pageEditController = new PageEditController(app);
        designRendererController = new DesignRendererController(app);

        designRenderer.setOnMousePressed(e -> {
            designRendererController.handleDesignRendererMouseClicked(e.getX(), e.getY());
        });

        classNameField.setOnKeyReleased(e -> {
            dataManager.updateNameInfo();
        });

        packageTextField.setOnKeyReleased(e -> {
            dataManager.updatePackageInfo();
        });

        selectBtn.setOnAction(e -> {
            pageEditController.handleSectionTool();
        });

        resizeBtn.setOnAction(e -> {
            pageEditController.handleResizing();
        });

        addClassBtn.setOnAction(e -> {
            pageEditController.handleAddClass();
        });

        addInterfaceBtn.setOnAction(e -> {
            pageEditController.handleAddInterface();
        });

        removeBtn.setOnAction(e -> {
            pageEditController.handleRemove();
        });

        undoBtn.setOnAction(e -> {
            pageEditController.handleUndo();
        });

        redoBtn.setOnAction(e -> {
            pageEditController.handleRedo();
        });

        zoomInBtn.setOnAction(e -> {
            pageEditController.handleZoomIn();
        });

        zoomOutBtn.setOnAction(e -> {
            pageEditController.handleZoomOut();
        });

        gridCheckBox.setOnAction(e -> {
            pageEditController.handleGridRendering();
        });

        parentClass.setOnAction(e -> {
            Stage stage = new Stage();
            GridPane addBox = new GridPane();
            TextField textfield = new TextField();
            textfield.setPromptText("Enter class name");
            textfield.setFocusTraversable(false);
            Button submit = new Button("Submit");
            CheckBox isInterface = new CheckBox("Interface?");
            submit.setOnAction(a -> {
                dataManager.addExternalParent(textfield.getText(), isInterface);
                stage.close();
            });
            addBox.add(textfield, 0, 0);
            addBox.addRow(1, isInterface, submit);
            Scene scene = new Scene(addBox, 350, 100);
            stage.setTitle("Add External Parent");
            stage.setScene(scene);
            stage.showAndWait();
        });

        variableAddBtn.setOnAction(e -> {
            pageEditController.handleAddVariable();
        });

        variableRemoveBtn.setOnAction(e -> {
            pageEditController.handleRemoveVariable();
        });

        methodAddBtn.setOnAction(e -> {
            pageEditController.handleAddMethod();
        });

        methodRemoveBtn.setOnAction(e -> {
            pageEditController.handleRemoveMethod();
        });

        argumentAddBtn.setOnAction(e -> {
            Label arg = new Label();
            methodLabelRow.getChildren().add(arg);
            dataManager.addAdditionalArgument(arg);
        });

    }

    @Override
    public void reloadWorkspace() {
        if (dataManager.isInState(DesignerState.ADDING_CLASS_STATE)) {
            addClassBtn.setDisable(true);
            addInterfaceBtn.setDisable(false);
            removeBtn.setDisable(true);
            selectBtn.setDisable(false);
        } else if (dataManager.isInState(DesignerState.ADDING_INTERFACE_STATE)) {
            addInterfaceBtn.setDisable(true);
            addClassBtn.setDisable(false);
            removeBtn.setDisable(true);
            selectBtn.setDisable(false);
        } else if (dataManager.isInState(DesignerState.RESIZING_STATE)) {
            selectBtn.setDisable(false);
            addClassBtn.setDisable(false);
            addInterfaceBtn.setDisable(false);
        } else if (dataManager.isInState(DesignerState.SELECTING_STATE)) {
            selectBtn.setDisable(true);
            addClassBtn.setDisable(false);
            addInterfaceBtn.setDisable(false);
        }
        classNameField.setDisable(dataManager.getSelectedObject() == null);
        packageTextField.setDisable(dataManager.getSelectedObject() == null);
        resizeBtn.setDisable(designRenderer.getChildren().isEmpty());
        removeBtn.setDisable(dataManager.getSelectedObject() == null);
        variableAddBtn.setDisable(dataManager.getSelectedObject() == null);
        variableRemoveBtn.setDisable(dataManager.getSelectedObject() == null);
        methodAddBtn.setDisable(dataManager.getSelectedObject() == null);
        methodRemoveBtn.setDisable(dataManager.getSelectedObject() == null);
        argumentAddBtn.setDisable(dataManager.getSelectedObject() == null);
        parentClass.setDisable(dataManager.getSelectedObject() == null);
        variableTable.setDisable(dataManager.getSelectedObject() == null);
        methodTable.setDisable(dataManager.getSelectedObject() == null);
    }

    @Override
    public void initStyle() {
        toolbar.getLeft().getStyleClass().add("edit_toolbar_row");
        editToolbar.getStyleClass().add("edit_toolbar_row");
        viewToolbar.getStyleClass().add("edit_toolbar_row");
        className.getStyleClass().add("subheading_label");
        packageName.getStyleClass().add("subheading_label");
        parentName.getStyleClass().add("subheading_label");
        variableLabel.getStyleClass().add("subheading_label");
        methodLabel.getStyleClass().add("subheading_label");
        componentToolbar.getStyleClass().add("bordered_section");
        variableLabelRow.getStyleClass().add("custom_button");
        methodLabelRow.getStyleClass().add("custom_button");
        variableTable.getStyleClass().add("class_gui_style");
        methodTable.getStyleClass().add("class_gui_style");
    }

}
