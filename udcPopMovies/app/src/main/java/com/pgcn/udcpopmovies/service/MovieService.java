package com.pgcn.udcpopmovies.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.pgcn.udcpopmovies.exceptions.MovieServiceException;
import com.pgcn.udcpopmovies.model.MovieFilter;
import com.pgcn.udcpopmovies.model.MovieModel;
import com.pgcn.udcpopmovies.utils.NetworkUtils;
import com.pgcn.udcpopmovies.utils.TheMoviedbJsonUtils;

import java.net.URL;
import java.util.ArrayList;

/**
 * Classe criada com base no gist https://gist.github.com/dhiegoabrantes/7933078edf4ccb05f2de2bf3fca17ed0#file-asynctaskdelegate-java
 * Created by Giselle on 21/11/2017.
 */

public class MovieService extends AsyncTask<Object, String, ArrayList<MovieModel>> {

    private static final String TAG = MovieService.class.getSimpleName();


    private AsyncTaskDelegate delegate = null;

    /**
     * No construtor da classe, passamos uma classe responsável por "responder" a requisição após a
     * sua execução Esse responsável é o AsyncTaskDelegate
     *
     * @param context
     * @param responder
     */
    public MovieService(Context context, AsyncTaskDelegate responder) {
        this.delegate = responder;
    }

    @Override
    protected ArrayList<MovieModel> doInBackground(Object... objects) {

        Log.d(TAG, "=== Inicio Async MovieService - doInBackground");


        try {
            MovieFilter movieFilter = (MovieFilter) objects[0];
            if (null != movieFilter) {

                URL movieRequestUrl = NetworkUtils.buildMoviesUrl(movieFilter.getTipoLista(),
                        movieFilter.getTipoSort(), movieFilter.getCurrentPage());
                String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);

                if (jsonMoviesResponse != null) {
                    if (null == movieFilter.getListaMovies() || movieFilter.getListaMovies().isEmpty()) {
                        movieFilter.setListaMovies(TheMoviedbJsonUtils.getSimpleMovieStringsFromJson(jsonMoviesResponse));
                    } else if (!movieFilter.getListaMovies().isEmpty()) {
                        movieFilter.getListaMovies().addAll(TheMoviedbJsonUtils
                                .getSimpleMovieStringsFromJson(jsonMoviesResponse));
                    }
                    Log.d(TAG,"Filmes recuperados: " + movieFilter.getListaMovies().size());
                    return movieFilter.getListaMovies();
                }
            } else throw new MovieServiceException("MovieFilter não pode ser nulo.");
        } catch (Exception e) {
            Log.e(TAG, " doInBackground ", e);
            e.printStackTrace();
            return null;
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<MovieModel> movies) {
        //Neste ponto, no onPostExecute, chamamos o método `processFinish` do delegate para finalizar o processamento da requisição
        //Aqui, conseguimos isolar responsabilidades! A classe de serviço agora é responsavel apenas por executar a requisição
        //e avisar quando estiver pronta para ser processada.
        super.onPostExecute(movies);
        if (delegate != null)
            delegate.processFinish(movies);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (delegate != null)
            delegate.preExecute();
    }
}
