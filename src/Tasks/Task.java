package Tasks;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected String status;


    public Task(String name, String description, String status, int id){
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = "NEW";
    }

    public String getName(){
        return name;
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

}
