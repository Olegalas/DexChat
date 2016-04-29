package ua.dexchat.server.exceptions;

/**
 * Created by dexter on 29.04.16.
 */
public class WasNotFoundException extends RuntimeException{
    public WasNotFoundException(String message) {
        super(message);
    }
}
