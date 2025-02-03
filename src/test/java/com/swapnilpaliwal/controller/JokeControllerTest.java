package com.swapnilpaliwal.controller;

import com.swapnilpaliwal.model.ApiResponse;
import com.swapnilpaliwal.model.Joke;
import com.swapnilpaliwal.model.Status;
import com.swapnilpaliwal.repository.RepositoryBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class JokeControllerTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RepositoryBase<Joke> repositoryBase;

    @Mock
    private Random random;

    @InjectMocks
    private JokeController jokeController;

    private static final String TEST_API_URL = "https://api.chucknorris.io/jokes/random";
    private static final String TEST_JOKE_TEXT = "Test joke";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jokeController, "apiUrl", TEST_API_URL);
    }

    @Test
    void testJoke_ShouldReturnDefaultJoke() {
        // Act
        ApiResponse<? super Joke> response = jokeController.testJoke();

        // Assert
        assertNotNull(response);
        assertTrue(((Joke) response.getJoke()).isLocal());
        assertEquals(Status.STORED, response.getStatus());
        assertEquals("This Joke is returned from the DB", response.getNote());
    }

    @Test
    void getJoke_WhenShouldCallAPI_ShouldReturnAPIJoke() {
        // Arrange
        Joke mockJoke = new Joke(TEST_JOKE_TEXT, TEST_API_URL);
        when(repositoryBase.storageSize()).thenReturn(0); // Force API call due to small storage
        when(restTemplate.getForObject(eq(TEST_API_URL), eq(Joke.class))).thenReturn(mockJoke);

        // Act
        ApiResponse<? super Joke> response = jokeController.getJoke();

        // Assert
        assertNotNull(response);
        assertFalse(((Joke) response.getJoke()).isLocal());
        assertEquals(TEST_JOKE_TEXT, ((Joke) response.getJoke()).getJoke());
        assertEquals(Status.RETRIEVED_ONLINE, response.getStatus());
        assertEquals("This Joke is returned from the API", response.getNote());
        verify(repositoryBase).addJoke(any(Joke.class));
    }

    @Test
    void getJoke_WhenShouldCallLocal_ShouldReturnLocalJoke() {
        // Arrange
        Joke mockJoke = new Joke(TEST_JOKE_TEXT, TEST_API_URL);
        mockJoke.setLocal(true);
        
        when(repositoryBase.storageSize()).thenReturn(10); // Enough jokes stored
        when(random.nextInt(1000)).thenReturn(800, 200); // Make shouldCallLocal return true
        when(repositoryBase.getJoke()).thenReturn(mockJoke);

        // Act
        ApiResponse<? super Joke> response = jokeController.getJoke();

        // Assert
        assertNotNull(response);
        assertTrue(((Joke) response.getJoke()).isLocal());
        assertEquals(TEST_JOKE_TEXT, ((Joke) response.getJoke()).getJoke());
        assertEquals(Status.STORED, response.getStatus());
        assertEquals("This Joke is returned from the DB", response.getNote());
    }

    @Test
    void postJoke_ShouldAddJokeToRepository() {
        // Arrange
        Joke inputJoke = new Joke(TEST_JOKE_TEXT, "http://test.com");
        when(repositoryBase.storageSize()).thenReturn(5);

        // Act
        jokeController.postJoke(inputJoke);

        // Assert
        verify(repositoryBase).addJoke(argThat(joke -> {
            assertEquals(5, joke.getJokeId());
            assertFalse(joke.isLocal());
            assertEquals(TEST_JOKE_TEXT, joke.getJoke());
            return true;
        }));
    }
}
