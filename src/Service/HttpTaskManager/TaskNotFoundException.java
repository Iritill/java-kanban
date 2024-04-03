package Service.HttpTaskManager;

public class TaskNotFoundException  extends RuntimeException{
    public TaskNotFoundException(final String message, Exception exp) {
        super(message, exp);
    }

}