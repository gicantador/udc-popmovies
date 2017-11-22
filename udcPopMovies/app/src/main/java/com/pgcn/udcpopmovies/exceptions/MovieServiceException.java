package com.pgcn.udcpopmovies.exceptions;

/**
 * Created by Giselle on 21/11/2017.
 */

public class MovieServiceException extends Exception {
    public MovieServiceException() {
        super();
    }

    public MovieServiceException(String message) {
        super(message);
    }

    public MovieServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public MovieServiceException(Throwable cause) {
        super(cause);
    }


}
