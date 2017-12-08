package com.pgcn.udcpopmovies.model;

import android.content.ContentResolver;

import com.pgcn.udcpopmovies.enums.TipoFiltro;

import java.util.ArrayList;

/**
 * Guarda os dados utilizados para filtrar a lista exibida em tela.
 * <p>
 * Created by Giselle on 21/11/2017.
 */
public class MovieFilter {

    private final TipoFiltro tipoFiltro;
    private final int currentPage;
    private ArrayList<MovieModel> listaMovies;
    private final ContentResolver mContentResolver;

    public MovieFilter(TipoFiltro filtro, int currentPage, ArrayList<MovieModel> listaMovies,
                       ContentResolver contentResolver) {
        this.currentPage = currentPage;
        this.listaMovies = listaMovies;
        this.mContentResolver = contentResolver;
        this.tipoFiltro = filtro;
    }

    public TipoFiltro getTipoFiltro() {
        return tipoFiltro;
    }


    public int getCurrentPage() {
        return currentPage;
    }

    public ArrayList<MovieModel> getListaMovies() {
        return listaMovies;
    }

    public void setListaMovies(ArrayList<MovieModel> listaMovies) {
        this.listaMovies = listaMovies;
    }

    public ContentResolver getContentResolver() {
        return mContentResolver;
    }


}
