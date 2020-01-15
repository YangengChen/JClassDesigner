/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import javafx.scene.input.KeyCode;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;

/**
 *
 * @author yan
 */
public class SolidArrow extends Line {

    ClassType sourceClass;
    InterfaceType sourceInterface;
    ClassType targetClass;
    InterfaceType targetInterface;
    Polyline arrow;
    Line line;

    public SolidArrow() {
        line = new Line();
        sourceClass = new ClassType("");
        sourceInterface = new InterfaceType("");
        targetClass = new ClassType("");
        targetInterface = new InterfaceType("");
        arrow = new Polyline();
    }

    public void createSolidArrow(ClassType source, ClassType dest) {
        line.setStartX(source.getLayoutX());
        line.setStartY(source.getLayoutY());
        line.setEndX(dest.getLayoutX());
        line.setEndY(dest.getLayoutY());
        line.setStrokeWidth(2);
        setSourceClass(source);
        setTargetClass(dest);
        arrow.getPoints().setAll(dest.getLayoutX() - 15, dest.getLayoutY() - 10,
                dest.getLayoutX(), dest.getLayoutY(), dest.getLayoutX() - 15, dest.getLayoutY() + 10, dest.getLayoutX() - 15, dest.getLayoutY() - 10);
        line.setOnMouseClicked(e -> {
            line.getStyleClass().add("highlight_effect");
            if(line.isFocused()){
                line.setOnKeyPressed(k -> { 
                    if(k.getCode().equals(KeyCode.SPACE)){
                        
                    }
                });
            }
        });
    }

    public void createSolidArrow(ClassType source, InterfaceType dest) {
        line.setStartX(source.getLayoutX());
        line.setStartY(source.getLayoutY());
        line.setEndX(dest.getLayoutX());
        line.setEndY(dest.getLayoutY());
        line.setStrokeWidth(2);
        setSourceClass(source);
        setTargetInterface(dest);
        arrow.getPoints().setAll(dest.getLayoutX() - 15, dest.getLayoutY() - 10,
                dest.getLayoutX(), dest.getLayoutY(), dest.getLayoutX() - 15, dest.getLayoutY() + 10, dest.getLayoutX() - 15, dest.getLayoutY() - 10);
    }

    public void createSolidArrow(InterfaceType source, InterfaceType dest) {
        line.setStartX(source.getLayoutX());
        line.setStartY(source.getLayoutY());
        line.setEndX(dest.getLayoutX());
        line.setEndY(dest.getLayoutY());
        line.setStrokeWidth(2);
        setSourceInterface(source);
        setTargetInterface(dest);
        arrow.getPoints().setAll(dest.getLayoutX() - 15, dest.getLayoutY() - 10,
                dest.getLayoutX(), dest.getLayoutY(), dest.getLayoutX() - 15, dest.getLayoutY() + 10, dest.getLayoutX() - 15, dest.getLayoutY() - 10);
    }

    public ClassType getTargetClass() {
        return targetClass;
    }

    public ClassType getSourceClass() {
        return sourceClass;
    }

    public InterfaceType getSourceInterface() {
        return sourceInterface;
    }
    
    public InterfaceType getTargetInterface() {
        return targetInterface;
    }

    public Polyline getArrow() {
        return arrow;
    }

    public Line getLine() {
        return line;
    }

    public void setSourceClass(ClassType source) {
        this.sourceClass = source;
    }

    public void setSourceInterface(InterfaceType source) {
        this.sourceInterface = source;
    }

    public void setTargetClass(ClassType target) {
        this.targetClass = target;
    }

    public void setTargetInterface(InterfaceType target) {
        this.targetInterface = target;
    }

    public void setEndCoordinates(Double x, Double y) {
        line.setEndX(x);
        line.setEndY(y);
        arrow.getPoints().setAll(x - 15, y - 10, x, y, x - 15, y + 10, x - 15, y - 10);
    }

}
