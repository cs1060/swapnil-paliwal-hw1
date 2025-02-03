package com.swapnilpaliwal.repository;
import java.util.List;

/**
 * A generic repository for storing and retrieving joke-like entities.
 *
 * @param <T> the type of joke stored by the repository
 */
public interface RepositoryBase<T> {

    /**
     * Retrieves a single joke from the repository.
     *
     * <p>Selects the random joke from the list</p>
     *
     * @return a joke model (possibly {@code null} if no jokes are available)
     */
    T getJoke();

    /**
     * Adds a joke to the internal repository.
     * <p> This helps reduce external API calls
     * by caching jokes locally.</p>
     *
     * @param joke the joke model to be added
     */
    void addJoke(T joke);

    /**
     * Retrieves the most recently added joke from the repository.
     *
     * <p>Gets the latest joke added</p>
     *
     * @return the most recent joke, or {@code null} if no jokes are stored
     */
    T getRecentJoke();

    /**
     * Returns the current number of jokes in the repository.
     *
     * @return the number of stored jokes
     */
    int storageSize();

    /**
     * Retrieves up to {@code count} jokes from the repository.
     * <p>
     * Implementations may return fewer jokes
     * than requested if the repository has fewer available.
     *
     * @param count the maximum number of jokes to retrieve
     * @return a list of jokes; if no jokes are available, an empty list is returned
     */
    List<? extends T> getSomeJoke(final int count);
}