package com.swapnilpaliwal.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.concurrent.CompletableFuture;

@Data
@AllArgsConstructor
public class ApiResponse<T> {

    @NonNull
    private T joke;

    @JsonProperty(value="custom_internal_note")
    private String note;

    @NonNull
    @JsonProperty(value = "api_status")
    private Status status;

}
