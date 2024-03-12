package Service.FileBackedManager.Exception;

public class ManagerFileNotExistsException extends RuntimeException{
    public ManagerFileNotExistsException(final String message) {
        super(message);
    }
}
