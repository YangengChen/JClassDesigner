/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;

/**
 *
 * @author yan
 */
public class DiamondArrow extends Line {

    ClassType sourceClass;
    ClassType targetClass;
    Line line;
    Polyline arrow;

    public DiamondArrow() {
        line = new Line();
        sourceClass = new ClassType("");
        targetClass = new ClassType("");
        arrow = new Polyline();
    }

    public void createDiamondArrow(ClassType source, ClassType dest) {
        line.setStartX(source.getLayoutX());
        line.setStartY(source.getLayoutY());
        line.setEndX(dest.getLayoutX());
        line.setEndY(dest.getLayoutY());
        line.setStrokeWidth(2);
        setSourceClass(source);
        setTargetClass(dest);
        arrow.getPoints().setAll(dest.getLayoutX() - 15, dest.getLayoutY() - 10,
                dest.getLayoutX(), dest.getLayoutY(), dest.getLayoutX() - 15,
                dest.getLayoutY() + 10, dest.getLayoutX() - 30, dest.getLayoutY(), dest.getLayoutX() - 15, dest.getLayoutY() - 10);
    }

    public ClassType getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(ClassType target) {
        this.targetClass = target;
    }

    public ClassType getSourceClass() {
        return sourceClass;
    }

    public void setSourceClass(ClassType source) {
        this.sourceClass = source;
    }

    public Polyline getArrow() {
        return arrow;
    }

    public Line getLine() {
        return line;
    }

    public void setEndCoordinates(Double x, Double y) {
        line.setEndX(x);
        line.setEndY(y);
        arrow.getPoints().setAll(x - 15, y - 10, x, y, x - 15, y + 10, x - 30, y, x - 15, y - 10);
    }

}
