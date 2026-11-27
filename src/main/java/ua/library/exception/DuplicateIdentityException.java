package ua.library.exception;

public class DuplicateIdentityException extends IllegalStateException {
    private static final long serialVersionUID = 1L;

    public DuplicateIdentityException(String message) {
        super(message);
    }
}
