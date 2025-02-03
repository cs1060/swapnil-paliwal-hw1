package com.swapnilpaliwal.controller;

import com.swapnilpaliwal.model.Joke;
import com.swapnilpaliwal.repository.JokeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

@Controller
public class WebController {
    
    private static final Logger logger = LoggerFactory.getLogger(WebController.class);

    @Autowired
    private JokeRepository jokeRepository;
    
    @Autowired
    private JokeController jokeController;

    @Value("${server.port}")
    private String serverPort;
    
    @Value("${app.base-url}")
    private String baseUrl;

    @GetMapping("/")
    public String home(Model model) {
        logger.info("Serving home page");
        // Get a random joke for display
        Joke currentJoke = jokeRepository.getJoke();
        model.addAttribute("randomJoke", currentJoke);
        model.addAttribute("jokeCount", jokeRepository.storageSize());
        return "index";
    }

    @PostMapping("/jokes")
    public String createJoke(@RequestParam("jokeText") String jokeText, Model model) {
        // Validate joke text
        if (!StringUtils.hasText(jokeText)) {
            logger.warn("Attempted to create empty joke");
            model.addAttribute("error", "Joke cannot be empty");
            model.addAttribute("randomJoke", jokeRepository.getJoke());
            model.addAttribute("jokeCount", jokeRepository.storageSize());
            return "index";
        }
        
        logger.info("Creating new joke: {}", jokeText);
        // Create joke with required fields
        String jokeUrl = baseUrl + "/jokes/" + (jokeRepository.storageSize() + 1);
        Joke joke = new Joke(jokeText.trim(), jokeUrl);
        joke.setLocal(true);
        jokeRepository.addJoke(joke);
        
        // Get the most recently added joke
        model.addAttribute("randomJoke", jokeRepository.getRecentJoke());
        model.addAttribute("jokeCount", jokeRepository.storageSize());
        return "index";
    }

    @GetMapping("/jokes/random")
    public String getRandomJoke(Model model) {
        logger.info("Getting random joke");
        // Use JokeController to get a joke (it will fetch from API if needed)
        Joke joke = (Joke) jokeController.getJoke().getJoke();
        model.addAttribute("randomJoke", joke);
        model.addAttribute("jokeCount", jokeRepository.storageSize());
        return "index";
    }
}
