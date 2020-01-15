/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.controller;


import javafx.scene.Cursor;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import jcd.data.DataManager;
import jcd.data.DesignerState;
import jcd.gui.Workspace;
import saf.AppTemplate;

/**
 *
 * @author yan
 */
public class PageEditController {
    AppTemplate app;
        
    DataManager dataManager;
    
    public PageEditController(AppTemplate initApp) {
	app = initApp;
	dataManager = (DataManager)app.getDataComponent();
    }
    
    public void handleSectionTool(){
        ((BorderPane)app.getWorkspaceComponent().getWorkspace()).getCenter().setCursor(Cursor.DEFAULT);
        dataManager.setState(DesignerState.SELECTING_STATE);
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace();
    }
    
    public void handleResizing(){
        dataManager.setState(DesignerState.RESIZING_STATE);
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace();
    }
    
    public void handleAddClass(){
        dataManager.setState(DesignerState.ADDING_CLASS_STATE);
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace();
    }
    
    public void handleAddInterface(){
        dataManager.setState(DesignerState.ADDING_INTERFACE_STATE);
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace();
    }
    
    public void handleRemove(){
        dataManager.removeSelected();
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace();
    }
    
    public void handleUndo(){
        dataManager.undo();
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        workspace.getRedoBtn().setDisable(false);
    }
    
    public void handleRedo(){
        dataManager.redo();
    }
    
    public void handleZoomIn(){
        dataManager.zoomIn();
    }
    
    public void handleZoomOut(){
        dataManager.zoomOut();
    }
    
    public void handleGridRendering(){
        dataManager.setGridRendering();
    }
    
    public void handleAddVariable(){
        dataManager.addVariable();
    }
    
    public void handleRemoveVariable(){
        dataManager.removeVariable();
    }
    
    public void handleAddMethod(){
        dataManager.addMethod();
    }
    
    public void handleRemoveMethod(){
        dataManager.removeMethod();
    }
   
    
}
