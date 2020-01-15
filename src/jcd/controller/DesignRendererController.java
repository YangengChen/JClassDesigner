/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.controller;

import jcd.data.DataManager;
import jcd.data.DesignerState;
import jcd.gui.Workspace;
import saf.AppTemplate;

/**
 *
 * @author yan
 */
public class DesignRendererController {
    
    AppTemplate app;
    
    DataManager dataManager;
    
    public DesignRendererController(AppTemplate initApp) {
        app = initApp;
        dataManager = (DataManager) app.getDataComponent();
    }
    
    public void handleDesignRendererMouseClicked(double x, double y) {
        if (dataManager.isInState(DesignerState.ADDING_CLASS_STATE)) {
            dataManager.addClass(x, y);
            ((Workspace) app.getWorkspaceComponent()).reloadWorkspace();
        } else if (dataManager.isInState(DesignerState.ADDING_INTERFACE_STATE)) {
            dataManager.addInterface(x, y);
            ((Workspace) app.getWorkspaceComponent()).reloadWorkspace();
        } else if (dataManager.isInState(DesignerState.SELECTING_STATE)) {
            if (dataManager.getSelectedObject() != null) {
                  dataManager.unhighlightSelected(dataManager.getSelectedObject());
                  dataManager.setSelectedObjectToNull();
                  ((Workspace) app.getWorkspaceComponent()).reloadWorkspace();
            }
        }
    }
}
