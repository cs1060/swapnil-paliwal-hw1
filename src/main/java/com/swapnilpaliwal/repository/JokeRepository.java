package com.swapnilpaliwal.repository;
import com.swapnilpaliwal.model.Joke;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implementation of the RepositoryBase interface for managing Joke entities.
 * <p>
 * This repository maintains an in-memory list of jokes and provides
 * methods for adding and retrieving jokes in various ways.
 */
@Repository
public class JokeRepository implements RepositoryBase<Joke> {

    /** List to store all jokes in memory */
    final List<Joke> jokeList = new ArrayList<>();

    /** Random number generator for selecting random jokes */
    @Autowired
    Random random;

    /**
     * {@inheritDoc}
     * <p>
     * Returns a random joke from the repository using the injected Random instance.
     * Returns null if the repository is empty.
     * </p>
     */
    @Override
    public Joke getJoke() {
        // We cannot create a joke object
        if(jokeList.isEmpty()) {
            return null;
        }
        //Get a random joke and send it back
        final int index = random.nextInt(storageSize());
        return jokeList.get(index);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Adds a new joke to the end of the internal list.</p>
     */
    @Override
    public void addJoke(Joke joke) {
        jokeList.add(joke);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Returns the last joke added to the repository.
     * Returns null if the repository is empty.</p>
     */
    @Override
    public Joke getRecentJoke() {
        if(jokeList.isEmpty()) {
            return null;
        }
        final int index = jokeList.size();
        return jokeList.get(index-1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int storageSize() {
        return jokeList.size();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Returns a sublist of jokes up to the requested count.
     * If count exceeds available jokes, returns all available jokes.</p>
     */
    @Override
    public List<? extends Joke> getSomeJoke(final int count) {
        if(storageSize() == 0) {
            return  null;
        } else if(count >= storageSize()-1 && count > 0) {
            return jokeList;
        } else if(count > 0) {
            final int sizeRange = random.nextInt(storageSize());
            final List<Joke> jokes = jokeList.subList(0, sizeRange);
            return !jokes.isEmpty() ? jokes : null;
        }
        return null;
    }
}
