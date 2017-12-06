package com.pgcn.udcpopmovies.model;

import com.pgcn.udcpopmovies.enums.TipoListaRetorno;

import java.util.ArrayList;

/**
 * Created by Giselle on 05/12/2017.
 */

public class MovieDetailBox {
    private final ArrayList<TrailerModel> movieTrailersList;
    private final ArrayList<ReviewModel> movieReviewList;
    private final TipoListaRetorno tipoListaRetorno;

    public MovieDetailBox(ArrayList<TrailerModel> movieTrailersList, ArrayList<ReviewModel> movieReviewList, TipoListaRetorno tipoListaRetorno) {
        this.movieTrailersList = movieTrailersList;
        this.movieReviewList = movieReviewList;
        this.tipoListaRetorno = tipoListaRetorno;
    }

    public ArrayList<TrailerModel> getMovieTrailersList() {
        return movieTrailersList;
    }

    public ArrayList<ReviewModel> getMovieReviewList() {
        return movieReviewList;
    }

    public TipoListaRetorno getTipoListaRetorno() {
        return tipoListaRetorno;
    }
}
