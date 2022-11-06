package by.modsen.meetup.dao.mapper.api;

public interface ModelMapper<T> {

    /**
     * Merge T class
     * @param source object to map from
     * @param destination object to map to
     * @throws IllegalArgumentException if source or destination are null
     */
    void rebase(T source, T destination) throws IllegalArgumentException;
}
