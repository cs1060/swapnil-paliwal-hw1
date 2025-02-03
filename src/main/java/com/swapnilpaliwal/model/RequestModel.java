package com.swapnilpaliwal.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class RequestModel {

    @JsonProperty("jokes_minimum_range_send")
    private int count;
}
