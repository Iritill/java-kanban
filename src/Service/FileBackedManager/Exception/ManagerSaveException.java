package Service.FileBackedManager.Exception;

public class ManagerSaveException extends RuntimeException{
    public ManagerSaveException(final String message, Exception exp) {
        super(message, exp);
    }

}
