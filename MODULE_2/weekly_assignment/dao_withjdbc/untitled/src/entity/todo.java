package entity;

public class todo {
    private int id;
    private String task;
    private boolean completed;

    public todo(int id, String task) {
        this.id = id;
        this.task = task;
        this.completed = false;

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isCompleted() {
        return completed;
    }
    public String getTask(){
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String toString(){
        return "the task"+task+" of id "+id+" is "+completed;
    }
}

