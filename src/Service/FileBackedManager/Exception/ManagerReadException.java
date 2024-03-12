package Service.FileBackedManager.Exception;

public class ManagerReadException extends RuntimeException{
    public ManagerReadException(final String message, Exception exp) {
        super(message, exp);
    }

}
