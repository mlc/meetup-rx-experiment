package com.meetup.rxexperiments;

import com.squareup.okhttp.Response;

public class HttpException extends Exception {
    private final int code;
    private final String message;

    public HttpException(Response response) {
        super("HTTP " + response.code() + " " + response.message());
        this.code = response.code();
        this.message = response.message();
    }
}
