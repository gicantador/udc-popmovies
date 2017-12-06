package com.pgcn.udcpopmovies.model;

import com.pgcn.udcpopmovies.data.MoviesDbHelper;
import com.pgcn.udcpopmovies.enums.SortOrder;
import com.pgcn.udcpopmovies.enums.TipoFiltro;

import java.util.ArrayList;

/**
 * Guarda os dados utilizados para filtrar a lista exibida em tela.
 * <p>
 * Created by Giselle on 21/11/2017.
 */
public class MovieFilter {

    private final TipoFiltro tipoFiltro;
    private final SortOrder sortOrder;
    private final int currentPage;
    private ArrayList<MovieModel> listaMovies;
    private final MoviesDbHelper dbHelper;

    public MovieFilter(TipoFiltro filtro, SortOrder sort, int currentPage, ArrayList<MovieModel> listaMovies,
                       MoviesDbHelper moviesDbHelper) {
        this.currentPage = currentPage;
        this.listaMovies = listaMovies;
        this.dbHelper = moviesDbHelper;
        tipoFiltro = filtro;
        sortOrder = sort;
    }

    public TipoFiltro getTipoFiltro() {
        return tipoFiltro;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
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
