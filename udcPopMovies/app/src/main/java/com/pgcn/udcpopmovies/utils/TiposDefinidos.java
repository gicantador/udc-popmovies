package com.pgcn.udcpopmovies.utils;

import android.support.annotation.IntDef;
import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Giselle on 08/12/2017.
 */


public class TiposDefinidos {

    public static final int LISTA_POPULAR = 1;
    public static final int LISTA_TOP_RATED = 2;
    public static final int LISTA_FAVORITES = 3;
    public static final int REVIEWS = 4;
    public static final int TRAILERS = 5;

    private static final String TAG = TheMoviedbJsonUtils.class.getSimpleName();


    public TiposDefinidos(@Tipos int season) {
        Log.d(TAG, "Tipos :" + season);
    }

    @IntDef({LISTA_POPULAR, LISTA_TOP_RATED, LISTA_FAVORITES, REVIEWS, TRAILERS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Tipos {
    }

}