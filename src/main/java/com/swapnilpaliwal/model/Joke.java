package com.swapnilpaliwal.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class Joke {

    @JsonIgnore
    private boolean isLocal;

    @JsonProperty(value = "joke_id")
    private int jokeId;

    @NonNull
    @JsonProperty(value = "value")
    private String joke;

    @NonNull
    @JsonProperty(value = "url")
    private String url;

};
