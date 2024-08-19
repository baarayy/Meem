package com.example.meme.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ResourceError {
    private int status;
    private String message;
    private long timestamp;
}
