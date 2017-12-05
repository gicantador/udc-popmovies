package com.pgcn.udcpopmovies.model;

import com.pgcn.udcpopmovies.data.MoviesDbHelper;

import java.util.ArrayList;

/**
 * Guarda os dados utilizados para filtrar a lista exibida em tela.
 * <p>
 * Created by Giselle on 21/11/2017.
 */
public class MovieFilter {

    private final String tipoSort;
    private final String tipoLista;
    private final int currentPage;
    private ArrayList<MovieModel> listaMovies;
    private final MoviesDbHelper dbHelper;

    public MovieFilter(String tipoSort, String tipoLista, int currentPage, ArrayList<MovieModel> listaMovies,
                       MoviesDbHelper moviesDbHelper) {
        this.tipoSort = tipoSort;
        this.tipoLista = tipoLista;
        this.currentPage = currentPage;
        this.listaMovies = listaMovies;
        this.dbHelper = moviesDbHelper;
    }

    public String getTipoSort() {
        return tipoSort;
    }

    public String getTipoLista() {
        return tipoLista;
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

    public MoviesDbHelper getDbHelper() {
        return dbHelper;
    }
}
