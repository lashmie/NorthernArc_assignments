package entity;

public class todo {
    private int id;
    private String task;
    private Boolean completed;
    public todo(){

    }
    public todo(int id , String task, Boolean completed){
        this.id=id;
        this.task =task;
        this.completed=completed;
    }
    public todo(String task, Boolean completed){
        this.task =task;
        this.completed=completed;
    }
    public void setId(int id){
        this.id=id;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public void setTask(String task){
        this.task=task;
    }

    public int getId() {
        return id;
    }
    public String getTask(){
        return task;
    }

    public Boolean getCompleted() {
        return completed;
    }

    @Override
    public String toString() {
        return "id"+id+"task"+task+"status"+completed;
    }
}
