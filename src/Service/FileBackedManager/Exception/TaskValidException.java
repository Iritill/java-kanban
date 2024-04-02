package Service.FileBackedManager.Exception;

public class TaskValidException extends RuntimeException {
    public TaskValidException() {
    }

    public TaskValidException(String message) {
        super(message);
    }

    public TaskValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskValidException(Throwable cause) {
        super(cause);
    }

}