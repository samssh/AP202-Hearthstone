package ir.sam.hearthstone.server.util.hibernate;

public class DatabaseDisconnectException extends Exception{
    public DatabaseDisconnectException(Throwable cause) {
        super(cause);
    }

    public DatabaseDisconnectException() {

    }
}
