package com.pgcn.udcpopmovies.model;

import com.pgcn.udcpopmovies.utils.TiposDefinidos;

import java.util.ArrayList;

/**
 * Created by Giselle on 05/12/2017.
 */

public class MovieDetailBox {
    private final ArrayList<TrailerModel> movieTrailersList;
    private final ArrayList<ReviewModel> movieReviewList;

    @TiposDefinidos.Tipos
    private final int tipoListaRetorno;

    public MovieDetailBox(ArrayList<TrailerModel> movieTrailersList, ArrayList<ReviewModel> movieReviewList, int tiposDefinidos) {
        this.movieTrailersList = movieTrailersList;
        this.movieReviewList = movieReviewList;
        this.tipoListaRetorno = tiposDefinidos;
    }

    public ArrayList<TrailerModel> getMovieTrailersList() {
        return movieTrailersList;
    }

    public ArrayList<ReviewModel> getMovieReviewList() {
        return movieReviewList;
    }

    public int getTipoListaRetorno() {
        return tipoListaRetorno;
    }
}
