package nashtech.rookies.security.exception;

public class UserExistException extends RuntimeException {
    public UserExistException (String mess) {
        super(mess);
    }
}
