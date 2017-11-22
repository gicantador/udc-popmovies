package com.pgcn.udcpopmovies.service;

/**
 * * Classe criada com base no gist https://gist.github.com/dhiegoabrantes/7933078edf4ccb05f2de2bf3fca17ed0#file-asynctaskdelegate-java
 * Created by Giselle on 21/11/2017.
 */

public interface AsyncTaskDelegate {
    void processFinish(Object output);

    void preExecute();
}


