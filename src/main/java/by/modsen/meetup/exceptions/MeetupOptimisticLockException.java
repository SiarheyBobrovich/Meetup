package by.modsen.meetup.exceptions;

import javax.persistence.OptimisticLockException;

public class MeetupOptimisticLockException extends OptimisticLockException {
    public MeetupOptimisticLockException(String message) {
        super(message);
    }
}
