package Tasks;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private final ArrayList<Integer> subTaskId = new ArrayList<>();

    protected TasksType type;


    public Epic(String name, String description) {
        super(name, description);
        this.type = TasksType.EPIC;
    }

    public Epic(String name, String description, String taskStatus, int id) {
        super(name, description, taskStatus, id);
        this.type = TasksType.EPIC;
    }

    public void setSubTaskId(int id){
        subTaskId.add(id);
    }
    public TasksType getType(){
        return TasksType.EPIC;
    }

    public void deleteSubTaskId(Integer id){
        subTaskId.remove(id);
    }

    public void clearSubTaskId(){
        subTaskId.clear();
    }

    public ArrayList<Integer> getSubTaskId() {
        return subTaskId;
    }


}
