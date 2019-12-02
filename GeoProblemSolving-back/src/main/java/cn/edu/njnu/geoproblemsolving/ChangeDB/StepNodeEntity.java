package cn.edu.njnu.geoproblemsolving.ChangeDB;

import java.util.ArrayList;

public class StepNodeEntity {
    private int id;
    private String stepID;
    private String name;
    private int category;
    private ArrayList<StepListNodeEntity> last;
    private ArrayList<StepListNodeEntity> next;
    private float x;
    private float y;
    private int level;
    private boolean end;
    private boolean activeStatus;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLast(ArrayList<StepListNodeEntity> last) {
        this.last = last;
    }

    public void setNext(ArrayList<StepListNodeEntity> next) {
        this.next = next;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public void setStepID(String stepID) {
        this.stepID = stepID;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getStepID() {
        return stepID;
    }

    public void setActiveStatus(boolean activeStatus) {
        this.activeStatus = activeStatus;
    }

    public int getLevel() {
        return level;
    }

    public int getCategory() {
        return category;
    }

    public ArrayList<StepListNodeEntity> getLast() {
        return last;
    }

    public ArrayList<StepListNodeEntity> getNext() {
        return next;
    }

    public boolean isActiveStatus() {
        return activeStatus;
    }

    public boolean isEnd() {
        return end;
    }
}
