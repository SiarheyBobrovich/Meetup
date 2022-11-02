package by.modsen.meetup.exceptions;

import javax.persistence.PersistenceException;

public class MeetupNotFoundException extends PersistenceException {

    public MeetupNotFoundException(String message) {
        super(message);
    }
}
