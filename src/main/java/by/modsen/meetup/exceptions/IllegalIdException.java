package by.modsen.meetup.exceptions;

public class IllegalIdException extends IllegalArgumentException {
    public IllegalIdException(Long id) {
        super("Id must be more then 1. Current id: " + id);
    }
}
