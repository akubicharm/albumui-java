package com.example.albumui.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Album(
    Long id,
    String title,
    String artist,
    Double price,
    @JsonProperty("image_url") String imageUrl
) {}