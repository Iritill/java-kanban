package Tasks;

public class SubTask extends Task{
    private final int epicId;

    public SubTask(String name, String description, Epic epic) {
        super(name, description);
        this.status = "NEW";
        this.epicId = epic.getId();
    }

    public SubTask(String name, String description, String status, int id, Epic epic) {
        super(name, description, status, id);
        this.epicId = epic.getId();
    }

    public int getEpicId(){
         return epicId;
    }
}
