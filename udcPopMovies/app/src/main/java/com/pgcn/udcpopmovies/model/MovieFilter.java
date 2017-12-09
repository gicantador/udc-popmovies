package com.pgcn.udcpopmovies.model;

import android.content.ContentResolver;
import android.util.Log;

import com.pgcn.udcpopmovies.utils.TiposDefinidos;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;

/**
 * Guarda os dados utilizados para filtrar a lista exibida em tela.
 * <p>
 * Created by Giselle on 21/11/2017.
 */
public class MovieFilter {

    private static final String TAG = MovieFilter.class.getSimpleName();

    private final int currentPage;
    private ArrayList<MovieModel> listaMovies;
    private final ContentResolver mContentResolver;

    @TiposDefinidos.Tipos
    private final int tipoFiltro;

    public MovieFilter(int filtro, int currentPage, ArrayList<MovieModel> listaMovies,
                       ContentResolver contentResolver) {
        this.currentPage = currentPage;
        this.listaMovies = listaMovies;
        this.mContentResolver = contentResolver;
        this.tipoFiltro = filtro;
        Log.d(TAG, "MovieFilter [" + toString() + " ]");
    }

    public int getTipoFiltro() {
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("currentPage", currentPage)
                .append("tipoFiltro", tipoFiltro)
                .append("listaMovies", listaMovies)
                .append("contentResolver", mContentResolver)
                .toString();
    }
}
