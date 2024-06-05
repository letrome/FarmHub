package farmhub.exception;

public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException() {
        super("INTERNAL_SERVER_ERROR");
    }
}
