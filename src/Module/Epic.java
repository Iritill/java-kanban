package Module;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subTaskId = new ArrayList<>();


    public Epic(String name, String description) {
        super(name, description);
    }

    public void setSubTaskId(int id){
        subTaskId.add(id);
    }

    public void deleteSubTaskId(Integer id){
        subTaskId.remove(id);
    }

    public void clearSubTaskId(){
        subTaskId.clear();
    }

    public ArrayList<Integer> getSubTaskId(){
        return subTaskId;
    }
}
