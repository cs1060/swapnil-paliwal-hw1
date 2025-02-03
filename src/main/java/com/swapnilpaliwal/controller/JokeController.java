package com.swapnilpaliwal.controller;

import com.swapnilpaliwal.model.ApiResponse;
import com.swapnilpaliwal.model.Joke;
import com.swapnilpaliwal.model.RequestModel;
import com.swapnilpaliwal.model.Status;
import com.swapnilpaliwal.repository.RepositoryBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.BiPredicate;

@RestController
@RequestMapping("/v1/jokeController")
public class JokeController {

    private final BiPredicate<Integer, Integer> shouldCallLocal =
            (firstNumber, secondNumber) -> firstNumber > secondNumber;

    // Injected through the yaml file
    @Value("${chucknorris.random-joke-url}")
    private String apiUrl;

    @Autowired
    RepositoryBase<Joke> repositoryBase;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    Random random;


    /**
     * A test endpoint to get a joke
     *
     * <p>This is a test endpoint made to query the API and see
     * if the model and everything is working as expected</p>
     *
     * @return APiResponse<? super Joke> that includes a joke object
     */
    @GetMapping("/testJoke")
    public ApiResponse<? super Joke> testJoke() {
        final Joke joke = defaultJoke();
        joke.setLocal(true);
        final ApiResponse<? super Joke> apiResponse = new ApiResponse<>(joke, "", Status.STORED);
        configureObject(joke.isLocal(), apiResponse);
        return apiResponse;
    };


    /**
     * An endpoint that returns a joke from storage or API call
     *
     * <p>This API either makes an external API call or
     * Retrieves information from the storage.</p>
     *
     * @return ApiResponse<? super Joke> that includes a Joke obect
     */
    @GetMapping
    public ApiResponse<? super Joke> getJoke() {
        final int storageSize = repositoryBase.storageSize();

        Joke joke;
        Status status;
        if(!shouldCallLocal.test(random.nextInt(1000),
                random.nextInt(1000)) || storageSize < 5) {
            joke = restTemplate.getForObject(apiUrl, Joke.class);
            joke.setLocal(false);
            joke.setJokeId(storageSize);
            status = Status.RETRIEVED_ONLINE;
            repositoryBase.addJoke(joke);
        } else {
            // Get the joke from the storage
            joke = repositoryBase.getJoke();
            status = Status.STORED;
        }
        final ApiResponse<? super Joke> apiResponse = new ApiResponse<>(joke, "", status);
        configureObject(joke.isLocal(), apiResponse);
        return apiResponse;
    };


    /**
     * Returns multiple jokes from the beginning
     *
     * <p>Can be used to get all jokes if count is higher than storage</p>
     *
     * @param requestModel that holds the request count for number of first X jokes to hold
     * @return List<? extends Joke> that holds all the jokes
     */
    @GetMapping("/multiple-early-jokes")
    public ApiResponse<? super Joke> firstXJokes(@RequestBody RequestModel requestModel) {
        final int jokeAmount = requestModel.getCount();
        final List<? extends Joke> jokes = repositoryBase.getSomeJoke(jokeAmount);

        final List<? extends Joke> finalJokes =
                (jokes == null)
                        ? Collections.singletonList(defaultJoke())
                        : jokes;

        // Create one ApiResponse object
        final ApiResponse<? super Joke> apiResponse = new ApiResponse<>(finalJokes, "", Status.STORED);
        configureObject(true, apiResponse);

        return apiResponse;
    };


    /**
     * Adding a custom joke to the API
     *
     * <p>If you want to add the recent joke this is the
     * API that makes that happen</p>
     *
     * @param joke that takes this model in
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void postJoke(@RequestBody final Joke joke) {
        // Add a joke to the repository
        final int id = repositoryBase.storageSize();
        joke.setLocal(false);
        joke.setJokeId(id);
        // Status of the API call
        Status status = Status.RETRIEVED_ONLINE;
        repositoryBase.addJoke(joke);
    };


    // Configure the object for the is it local or retrieved online?
    private void configureObject(boolean isLocal, ApiResponse<? super Joke> apiResponse) {

        final Status jokeStatus = isLocal ? Status.STORED : Status.RETRIEVED_ONLINE;
        final String localString = "This Joke is returned from the DB";
        final String apiString = "This Joke is returned from the API";
        final String apiNote = isLocal ? localString : apiString;

        // Set the object
        apiResponse.setStatus(jokeStatus);
        apiResponse.setNote(apiNote);
    };


    // Default Joke to show when there is nothing to show
    private Joke defaultJoke() {
        return new Joke("Jesus once walked on water;Chuck Norris once swam on land", apiUrl);
    };


}
