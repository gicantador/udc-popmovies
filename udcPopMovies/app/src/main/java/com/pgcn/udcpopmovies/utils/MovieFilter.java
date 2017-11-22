package com.pgcn.udcpopmovies.utils;

import java.util.ArrayList;

/**
 * Guarda os dados utilizados para filtrar a lista exibida em tela.
 * <p>
 * Created by Giselle on 21/11/2017.
 */
public class MovieFilter {

    private String tipoSort;
    private String tipoLista;
    private int currentPage;
    private ArrayList<MovieModel> listaMovies;

    public MovieFilter(String tipoSort, String tipoLista, int currentPage, ArrayList<MovieModel> listaMovies) {
        this.tipoSort = tipoSort;
        this.tipoLista = tipoLista;
        this.currentPage = currentPage;
        this.listaMovies = listaMovies;
    }

    public String getTipoSort() {
        return tipoSort;
    }

    public void setTipoSort(String tipoSort) {
        this.tipoSort = tipoSort;
    }

    public String getTipoLista() {
        return tipoLista;
    }

    public void setTipoLista(String tipoLista) {
        this.tipoLista = tipoLista;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public ArrayList<MovieModel> getListaMovies() {
        return listaMovies;
    }

    public void setListaMovies(ArrayList<MovieModel> listaMovies) {
        this.listaMovies = listaMovies;
    }
}
