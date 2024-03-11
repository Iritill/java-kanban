package Tasks;

import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected String status;

    protected TasksType type;


    public Task(String name, String description, String status, int id){
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.type = TasksType.TASK;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = "NEW";
        this.type = TasksType.TASK;
    }

    public String getName(){
        return name;
    }
    
    public TasksType getType(){
        return type;
    }
    
    public Integer getEpicId(){
        return null;
    }

    public String getDescription(){
        return description;
    }

    public String getStatus(){
        return status;
    }

    public int getId(){
        return id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public void setId(int id){
        this.id = id;
    }

    @Override
    public String toString(){
        return "name=" + name + '\'' +
                "description=" + description + '\'' +
                "status=" + status + '\'' +
                "id=" + id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status);
    }
}
