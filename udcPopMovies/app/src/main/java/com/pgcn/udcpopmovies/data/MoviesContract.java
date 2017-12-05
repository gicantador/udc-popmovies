package com.pgcn.udcpopmovies.data;

import android.provider.BaseColumns;

/**
 * Created by Giselle on 02/12/2017.
 */

class MoviesContract {


    public static final class FavoriteMovies implements BaseColumns {
        public static final String TABLE_NAME = "favoriteMovies";
        // o id do filme na API
        public static final String COLUMN_API_ID = "apiId";
        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_ORIGINAL_TITLE = "title" ;
        public static final String COLUMN_VOTE_AVERAGE = "voteAverage";
        public static final String COLUMN_DTA_CRIACAO = "criadoEm";
    }
}
